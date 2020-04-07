from tkinter import *
from flask import g
from tkinter import messagebox
from ast import literal_eval
from MainForm import MainForm


class RegisterForm:
    def __init__(self):
        self.socket = getattr(g, 'socket', None)
        self.port = getattr(g, 'port', None)
        self.auth_key = getattr(g, 'auth_key', None)
        self.start()

    def start(self):
        self.root = Tk()
        root = self.root
        root.geometry('550x400+700+250')
        root.title("Регистрация")
        label_0 = Label(root, text="Регистрация", width=20, font=("bold", 20))
        label_0.place(x=90, y=53)

        self.login = StringVar()
        label_3 = Label(root, text="Логин", width=20, font=("bold", 10))
        label_3.place(x=100, y=130)
        entry_3 = Entry(root, textvariable=self.login)
        entry_3.place(x=240, y=130)

        self.password = StringVar()
        label_4 = Label(root, text="Пароль", width=20, font=("bold", 10))
        label_4.place(x=100, y=180)
        entry_4 = Entry(root, textvariable=self.password)
        entry_4.place(x=240, y=180)

        Button(root, text='Зарегистрироваться', width=20, bg='brown', fg='white', command=self.on_submit) \
            .place(x=180, y=320)

        root.mainloop()

    def on_submit(self):
        try:
            request = {
                'source_port': self.port,
                'action': 'create',
                'auth_key': self.auth_key,
                'login': self.login.get(),
                'password': self.password.get()
            }
            if request['login'] == '' or request['password'] == '':
                raise ValueError('Validation error')
            self.socket.sendto(bytearray(str(request), 'utf-8'), ('', 7777))
            response = literal_eval(str(self.socket.recv(2048), 'utf-8'))
            if response['status'] != 0:
                raise ValueError(response['error'])
            else:
                self.root.destroy()
                g.user = response['user']
                g.token = response['token']
                MainForm()
        except Exception as inst:
            print("Register form: submit error")
            print(inst)
            messagebox.showerror("Ошибка", inst)
