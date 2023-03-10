import time
import asyncio
import websockets

import requests
import datetime

host = "localhost:8090"

url = f"http://{host}"

chat_id = '43c0db5c-d829-4929-8efc-5e4a13bb202f'
signal_id = "427bf120-8520-11ed-805d-0933ac6bdfd8"


def time_me(func):
    def f(*args, **kwargs):
        start = time.time()
        r = func(*args, **kwargs)
        end = time.time()
        took = end- start
        print(f'took: {int(took * 1000)} ms')
        return r
    return f

@time_me
def get_msg(after: str = None):
    if after is None:
        r = requests.get(f'{url}/chat/{chat_id}')
    else:
        r = requests.get(f'{url}/chat/{chat_id}?lastMessage={after}')
    print(r)
    res = r.json()
    print(res)
    print(len(res))


@time_me
def new_msg(payload: str, with_file: bool):
    print('beb')
    if with_file:
        r = requests.post(f'{url}/chat/{chat_id}', files={"file":open('test.py', 'r')}, data={"content": payload})
        print(r.request.headers)
        # 'Content-Type': 'multipart/form-data; boundary=37875940cc2656b838712b8f18933118'}
    else:
        r = requests.post(f'{url}/chat/{chat_id}', data={"content": payload}, headers={"Content-Type":"multipart/form-data"})
        print(r.request.headers)
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
    r = requests.put(f'{url}/chat/{chat_id}/{signal_id}/reaction/test')
    print(r)
    print(r.text)

def remove_reaction():
    r = requests.delete(f'{url}/chat/{chat_id}/{signal_id}/reaction/test')
    print(r)
    print(r.text)


@time_me
def search(query: str):
    r = requests.get(f'{url}/chat/{chat_id}/search?q={query}')
    print(r)
    print(r.text)


@time_me
def get_cursor():
    r = requests.get(f'{url}/chat/{chat_id}/cursor')
    print(r)
    print(r.text)
    

@time_me
def update_cursor(idx: str):
    r = requests.put(f'{url}/chat/{chat_id}/cursor/{idx}')
    print(r)
    print(r.text)

def get_ws_token():
    r = requests.get(f'{url}/ws/token-provider')
    print(r)
    print(r.text)
    return r.json()['token']

@time_me
def ping():
    r = requests.get(f'{url}/utils/ping')
    print(r)
    print(r.text)

def upload_file():
    files = []
    files.append(open('test.py', 'r'))
    r = requests.post(f'{url}/file/test', files={"file":open('test.py', 'r')})
    print(r)
    print(r.text)

# add_reaction()
# 
new_msg("hum ?", True)
# ping()
# get_msg()

# token = get_ws_token()
# print(f'token is: {token}')
# search("hello")
# update_cursor("f61e3ab0-8879-11ed-a455-9532819b0e78")
# get_cursor()
# upload_file()