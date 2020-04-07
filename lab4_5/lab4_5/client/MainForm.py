from tkinter import *
from flask import g
from ControlForm import ControlForm
from UsersForm import UsersForm


class MainForm:
    def __init__(self):
        self.socket = getattr(g, 'socket', None)
        self.user = getattr(g, 'user', None)
        self.start()

    def start(self):
        self.root = Tk()
        root = self.root
        root.geometry('550x600+700+250')
        root.title("Главная страницы")

        Button(root, text='Управление', width=20, bg='brown', fg='white', command=self.to_control_form) \
            .grid(row=3, column=4)

        Button(root, text='Пользователи', width=20, bg='brown', fg='white', command=self.to_users_form) \
            .grid(row=5, column=4)

        root.mainloop()

    def to_control_form(self):
        self.root.destroy()
        ControlForm(self.user, self)


    def to_users_form(self):
        self.root.destroy()
        UsersForm(self)
