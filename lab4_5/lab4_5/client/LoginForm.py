from tkinter import *
from flask import g
from tkinter import messagebox
from ast import literal_eval
from MainForm import MainForm
from RegisterForm import RegisterForm


class LoginForm:
    def __init__(self):
        self.socket = getattr(g, 'socket', None)
        self.port = getattr(g, 'port', None)
        self.root = Tk()
        self.start()

    def start(self):
        root = self.root
        root.geometry('350x260+700+250')
        root.title("Авторизация")
        label_0 = Label(root, text="Авторизация", width=20, font=("bold", 20))
        label_0.place(x=-10, y=20)

        self.login = StringVar()
        label_1 = Label(root, text="Логин", width=20, font=("bold", 10))
        label_1.place(x=0, y=80)
        entry_1 = Entry(root, textvariable=self.login)
        entry_1.place(x=140, y=80)

        self.password = StringVar()
        label_2 = Label(root, text="Пароль", width=20, font=("bold", 10))
        label_2.place(x=0, y=130)
        entry_2 = Entry(root, textvariable=self.password)
        entry_2.place(x=140, y=130)

        Button(root, text='Войти', width=20, bg='brown', fg='white', command=self.on_submit) \
            .place(x=80, y=180)
        Button(root, text='Регистрация', width=10, bg='brown', fg='white', command=self.open_registration) \
            .place(x=115, y=215)

        root.mainloop()

    def open_registration(self):
        self.root.destroy()
        RegisterForm()

    def on_submit(self):
        request = {
            'source_port': self.port,
            'action': 'login',
            'login': self.login.get(),
            'password': self.password.get()
        }
        self.socket.sendto(bytearray(str(request), 'utf-8'), ('', 7777))
        response = literal_eval(str(self.socket.recv(2048), 'utf-8'))
        print(response)
        if response['status'] == 0:
            self.root.destroy()
            g.user = response['user']
            g.token = response['token']
            MainForm()
        else:
            print("Login form: submit error")
            messagebox.showerror("Ошибка", "Не верный пароль или логин!")
