from ast import literal_eval
from tkinter import *

from flask import g


class UsersForm:
    def __init__(self, previousWindow):
        self.socket = getattr(g, 'socket', None)
        self.token = getattr(g, 'token', None)
        self.previousWindow = previousWindow
        self.start()

    def init_users(self):
        request = {
            'token': self.token,
            'action': 'get-users'
        }
        self.socket.sendto(bytearray(str(request), 'utf-8'), ('', 7777))
        response = literal_eval(str(self.socket.recv(2048), 'utf-8'))
        print(response)
        if response['status'] == 0:
            self.users = response['users']
        else:
            self.onDestroy()

    def start(self):
        # column=2 is empty
        self.root = Tk()
        self.init_users()
        root = self.root
        root.protocol('WM_DELETE_WINDOW', self.onDestroy)
        root.geometry('+%d+%d' % (550, 250))
        root.title("Пользователи")

        scrollbar = Scrollbar(root)
        self.listbox = Listbox(yscrollcommand=scrollbar.set)
        self.listbox.grid(row=0, column=0, columnspan=5, sticky=N + S + W + E)
        scrollbar.config(command=self.listbox.yview)

        self.сtrl_type = StringVar()
        Radiobutton(root, text='user', padx=5, variable=self.сtrl_type, value='user') \
            .grid(row=1, column=0, sticky=W)
        Radiobutton(root, text='admin', padx=5, variable=self.сtrl_type, value='admin') \
            .grid(row=2, column=0, sticky=W)
        Radiobutton(root, text='superAdmin', padx=5, variable=self.сtrl_type, value='superAdmin') \
            .grid(row=3, column=0, sticky=W)
        Button(root, text='Применить', width=20, bg='brown', fg='white', command=self.on_change_type) \
            .grid(row=4, column=0)

        Button(root, text='Удалить', width=20, bg='brown', fg='white', command=self.on_delete) \
            .grid(row=1, column=4)

        self.update_listbox()
        root.mainloop()

    def onDestroy(self):
        self.root.destroy()
        self.previousWindow.start()

    def get_selected_user(self):
        list_id = self.listbox.curselection()[0]
        return self.users[list_id]

    def on_change_type(self):
        user = self.get_selected_user()
        request = {
            'token': self.token,
            'action': 'change-role',
            'role': self.сtrl_type.get(),
            'id': user['id']
        }
        self.socket.sendto(bytearray(str(request), 'utf-8'), ('', 7777))
        response = literal_eval(str(self.socket.recv(2048), 'utf-8'))
        if response['status'] == 0:
            self.init_users()
            self.update_listbox()

    def on_delete(self):
        user = self.get_selected_user()
        request = {
            'token': self.token,
            'action': 'delete',
            'id': user['id']
        }
        self.socket.sendto(bytearray(str(request), 'utf-8'), ('', 7777))
        response = literal_eval(str(self.socket.recv(2048), 'utf-8'))
        if 'status' in response.keys():
            if response['status'] == 0:
                self.init_users()
                self.update_listbox()

    def update_listbox(self):
        while self.listbox.size() > 0:
            self.listbox.delete(0)

        for user in self.users:
            line = "{} - {} - {}".format(user['id'], user['login'], user['role'])

            self.listbox.insert(END, line)
