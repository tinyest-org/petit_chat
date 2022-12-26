import requests
import datetime

url = "http://localhost:8090"

uid = '43c0db5c-d829-4929-8efc-5e4a13bb202f'


def get_msg(after: str = None):
    if after is None:
        r = requests.get(f'{url}/chat/{uid}')
    else:
        r = requests.get(f'{url}/chat/{uid}?lastMessage={after}')
    print(r)
    print(r.text)


def new_msg(payload: str):
    r = requests.post(f'{url}/chat/{uid}', json={"content": payload})
    print(r)
    print(r.text)


def get_chat_for_user():
    r = requests.get(f'{url}/user/me/chats')
    print(r)
    print(r.text)


def scenario_msg():
    get_msg(after="427bf120-8520-11ed-805d-0933ac6bdfd8")
    new_msg(datetime.datetime.now().isoformat())


def scenario_chat():
    get_chat_for_user()
    get_users_in_chat()


def get_user_by_name():
    r = requests.get(f'{url}/user/find?q=P')
    print(r)
    print(r.text)


def get_users_in_chat():
    r = requests.get(f'{url}/chat/{uid}/users')
    print(r)
    print(r.text)


scenario_msg()
