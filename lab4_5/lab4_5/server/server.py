from database import database
import socket
import jwt
from datetime import datetime, timezone
from ast import literal_eval

database = database()

PORT = 7777
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sock.bind(('', PORT))

TOKENS = []


def auth(user, port):
    if len(TOKENS) > 100:
        print('TOKENS overflow')
        exit(-1)
    dt = datetime.now()
    utc_timestamp = dt.replace(tzinfo=timezone.utc).timestamp()
    token_obj = {
        'id': user['id'],
        'role': user['role'],
        'datetime': utc_timestamp,
        'port': port
    }
    token_jwt = jwt.encode(token_obj, 'secret', algorithm='HS256')

    # TODO `dos` защита от переполнения пользователей с одного аккаунта
    # for t in TOKENS:
    #     if jwt.decode(t, 'secret', algorithms=['HS256'])['id'] == user['id']:
    #         return -1
    #

    TOKENS.append(token_jwt)
    print(TOKENS)
    return token_jwt


def action_create(request):
    creating_res = database.create_user(request['login'], request['password'], request['auth_key'])
    if creating_res == 0:
        action_login(request)
    else:
        response = {
            'status': -1,
            'error': 'auth key error'
        }
        sock.sendto(bytearray(str(response), 'utf-8'), ('', int(request['source_port'])))


def action_login(request):
    user = database.get_user_by_credentials(request['login'], request['password'])
    if user != 'None':
        token = auth(user, request['source_port'])
        if token == -1:
            user = None
        response = {
            'status': 0,
            'user': user,
            'token': token
        }
    else:
        response = {
            'status': -1,
            'error': 'User not found'
        }
    sock.sendto(bytearray(str(response), 'utf-8'), ('', int(request['source_port'])))


def action_logout(request):
    token = request['token']
    TOKENS.remove(token)
    print(TOKENS)


def action_change_secret(request):
    token = jwt.decode(request['token'], 'secret', algorithm='HS256')
    database.change_secret(token['id'], request['secret'])
    user = database.get_user_by_id(token['id'])
    response = {
        'user': user
    }
    sock.sendto(bytearray(str(response), 'utf-8'), ('', int(token['port'])))


def action_get_all_users(request):
    token = jwt.decode(request['token'], 'secret', algorithm='HS256')
    role = token['role']
    print(token)
    if role == 'admin' or role == 'superAdmin':
        users = database.get_all_users()
        response = {
            'status': 0,
            'users': users
        }
        sock.sendto(bytearray(str(response), 'utf-8'), ('', int(token['port'])))
    else:
        send_error(token)


def action_change_role(request):
    token = jwt.decode(request['token'], 'secret', algorithm='HS256')
    role = token['role']
    if role == 'admin' or role == 'superAdmin':
        database.change_role(request['id'], request['role'])
        response = {
            'status': 0
        }
        sock.sendto(bytearray(str(response), 'utf-8'), ('', int(token['port'])))
    else:
        send_error(token)


def action_delete(request):
    token = jwt.decode(request['token'], 'secret', algorithm='HS256')
    role = token['role']
    if role == 'admin' or role == 'superAdmin':
        database.delete(request['id'])
        response = {
            'status': 0
        }
        sock.sendto(bytearray(str(response), 'utf-8'), ('', int(token['port'])))
    else:
        send_error(token)


def send_error(token):
    response = {
        'status': -1
    }
    sock.sendto(bytearray(str(response), 'utf-8'), ('', int(token['port'])))


def can_be_int(s):
    try:
        int(s)
        return True
    except ValueError:
        return False


try:
    while True:
        try:
            # TODO `xss-attack`
            request = eval(str(sock.recv(2048), 'utf-8'))
            # request = literal_eval(str(sock.recv(2048), 'utf-8'))
        except Exception as inst:
            print(inst)
            continue

        if type(request) is not dict:
            print('request ERROR')
            continue

        try:
            action = request['action']
            if action == 'create':
                action_create(request)
            elif action == 'login':
                action_login(request)
            elif action == 'logout':
                action_logout(request)
            elif action == 'change-secret':
                action_change_secret(request)
            elif action == 'get-users':
                action_get_all_users(request)
            elif action == 'change-role':
                action_change_role(request)
            elif action == 'delete':
                action_delete(request)
            else:
                raise ValueError('Action did not found')
        except Exception as inst:
            print(inst)
            if 'source_port' not in request.keys() or not can_be_int(request['source_port']):
                print('request ERROR')
            else:
                sock.sendto(bytearray('Error:' + str(inst), 'utf-8'), ('', int(request['source_port'])))


except():
    sock.close()

print('Bye')
