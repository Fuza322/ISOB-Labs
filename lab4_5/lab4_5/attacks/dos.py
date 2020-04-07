import socket

count = 1000

for _ in range(count):
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    sock.bind(('', 0))
    port = sock.getsockname()[1]

    request = {
        'source_port': port,
        'action': 'login',
        'login': '1',
        'password': '1'
    }
    sock.sendto(bytearray(str(request), 'utf-8'), ('', 7777))
