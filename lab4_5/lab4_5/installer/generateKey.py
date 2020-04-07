import sys

sys.path.append('..')
from server.database import database

import json
import uuid

if __name__ == '__main__':
    data = {'key': str(uuid.uuid1())}

    database().create_key(data['key'])

    with open('../client/data/key.json', 'w') as fp:
        json.dump(data, fp)
