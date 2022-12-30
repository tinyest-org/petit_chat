import asyncio
import websockets

import requests
import datetime

host = "localhost:8090"

url = f"http://{host}"

chat_id = '43c0db5c-d829-4929-8efc-5e4a13bb202f'
signal_id = "427bf120-8520-11ed-805d-0933ac6bdfd8"


def get_msg(after: str = None):
    if after is None:
        r = requests.get(f'{url}/chat/{chat_id}')
    else:
        r = requests.get(f'{url}/chat/{chat_id}?lastMessage={after}')
    print(r)
    print(r.text)


def new_msg(payload: str):
    r = requests.post(f'{url}/chat/{chat_id}', json={"content": payload})
    print(r)
    print(r.text)


def get_chat_for_user():
    r = requests.get(f'{url}/user/me/chats')
    print(r)
    print(r.text)


def scenario_msg():
    get_msg(after=signal_id)
    new_msg(datetime.datetime.now().isoformat())


def scenario_chat():
    get_chat_for_user()
    get_users_in_chat()


def get_user_by_name():
    r = requests.get(f'{url}/user/find?q=P')
    print(r)
    print(r.text)


def get_users_in_chat():
    r = requests.get(f'{url}/chat/{chat_id}/users')
    print(r)
    print(r.text)


def add_reaction():
    r = requests.put(f'{url}/chat/{chat_id}/{signal_id}/test')
    print(r)
    print(r.text)


def get_ws_token():
    r = requests.get(f'{url}/ws/token-provider')
    print(r)
    print(r.text)
    return r.json()['token']


# add_reaction()
# get_msg(after="44a84700-8520-11ed-805d-0933ac6bdfd8")
new_msg("Hello ça va ?")
# token = get_ws_token()
# print(f'token is: {token}')

