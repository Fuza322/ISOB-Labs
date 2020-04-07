import mysql.connector


class database:
    def __init__(self):
        self.mydb = mysql.connector.connect(
            host='localhost',
            user='artem',
            passwd='1q2w3e4r',
            database='userStore'
        )
        self.mycursor = self.mydb.cursor(buffered=True)

    def create_key(self, key):
        self.mycursor.execute("INSERT INTO `key` (`key`) VALUES ('{}')".format(key))
        self.mydb.commit()

    def create_user(self, login, password, auth_key):
        self.mycursor.execute("SELECT * FROM `key` WHERE `key` = '{}'".format(auth_key))
        key_db = self.mycursor.fetchone()
        if key_db is None:
            return -1
        self.mycursor.execute("DELETE FROM `key` WHERE `key` = '{}'".format(auth_key))
        self.mycursor.execute("INSERT INTO user (login, password, role) VALUES {}".format((login, password, 'user')))
        self.mydb.commit()
        return 0

    def get_user_by_credentials(self, login, password):
        # TODO `sql-injection`
        sql_query = "SELECT * FROM user WHERE login = '{}' AND password = {}".format(login, password)
        # sql_query = "SELECT * FROM user WHERE login = '{}' AND password = '{}'".format(login, password)

        self.mydb.commit()
        self.mycursor.execute(sql_query)
        return get_user(self.mycursor.fetchone())

    def get_user_by_id(self, id):
        sql_query = "SELECT * FROM user WHERE id = '{}'".format(id)
        self.mydb.commit()
        self.mycursor.execute(sql_query)
        return get_user(self.mycursor.fetchone())

    def change_secret(self, id, secret):
        sql_query = "UPDATE user SET secret ='{}' WHERE id = '{}'".format(secret, id)
        self.mydb.commit()
        self.mycursor.execute(sql_query)

    def get_all_users(self):
        sql_query = "SELECT * FROM user ".format()
        self.mydb.commit()
        self.mycursor.execute(sql_query)
        users_bd = self.mycursor.fetchall()
        users = []
        for u in users_bd:
            users.append(get_user(u))
        return users

    def change_role(self, id, role):
        sql_query = "UPDATE user SET role ='{}' WHERE id = '{}'".format(role, id)
        self.mydb.commit()
        self.mycursor.execute(sql_query)

    def delete(self, id):
        sql_query = "DELETE FROM user WHERE id = '{}'".format(id)
        self.mydb.commit()
        self.mycursor.execute(sql_query)


def get_user(user):
    return {
        'id': user[0],
        'login': user[1],
        # 'password': user[2],
        'role': user[3],
        'secret': user[4]
    }
