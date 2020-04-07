from tkinter import *
from flask import g
from ast import literal_eval


class ControlForm:
    def __init__(self, user, previousWindow):
        self.socket = getattr(g, 'socket', None)
        self.token = getattr(g, 'token', None)
        self.user = user
        self.previousWindow = previousWindow
        self.start()

    def start(self):
        self.root = Tk()
        root = self.root
        root.protocol('WM_DELETE_WINDOW', self.on_destroy)
        root.geometry('550x600+700+250')
        root.title("Управление")

        print(self.user)
        print(self.token)

        Label(root, text="Id:", font=("bold", 10)).grid(row=0, column=0, sticky=W)
        Label(root, text=self.user['id'], font=("bold", 16)).grid(row=0, column=1, sticky=W)
        Label(root, text="Login:", font=("bold", 10)).grid(row=1, column=0, sticky=W)
        Label(root, text=self.user['login'], font=("bold", 16)).grid(row=1, column=1, sticky=W)
        Label(root, text="Secret:", font=("bold", 10)).grid(row=1, column=0, sticky=W)
        self.secret_label = StringVar()
        self.secret_label.set(self.user['secret'])
        Label(root, textvariable=self.secret_label, font=("bold", 16)).grid(row=1, column=1, sticky=W)

        self.secret = StringVar()
        self.secret.set(self.user['secret'])
        Label(root, text="Change secret:", width=20, font=("bold", 10)).grid(row=2, column=0, sticky=E)
        Entry(root, textvariable=self.secret).grid(row=2, column=1)

        Button(root, text='Change', width=20, bg='brown', fg='white', command=self.on_change_secret) \
            .grid(row=3, column=0, columnspan=2)

        root.mainloop()

    def on_destroy(self):
        self.root.destroy()
        self.previousWindow.start()

    def on_change_secret(self):
        request = {
            'token': self.token,
            'action': 'change-secret',
            'secret': self.secret.get()
        }
        self.socket.sendto(bytearray(str(request), 'utf-8'), ('', 7777))
        self.user = literal_eval(str(self.socket.recv(2048), 'utf-8'))['user']
        self.secret_label.set(self.user['secret'])
