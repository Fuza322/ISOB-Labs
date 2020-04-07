import socket
from flask import Flask, g
import json

from LoginForm import LoginForm

app = Flask(__name__)
with app.app_context():
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    g.socket = sock
    sock.bind(('', 0))
    g.port = sock.getsockname()[1]
    with open('data/key.json') as fp:
        auth_key = json.load(fp)['key']
    g.auth_key = auth_key
    print(auth_key)
    LoginForm()
    request = {
        'action': 'logout',
        'token': getattr(g, 'token', None)
    }
    sock.sendto(bytearray(str(request), 'utf-8'), ('', 7777))
    sock.close()
