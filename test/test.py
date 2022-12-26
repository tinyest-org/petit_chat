import requests

url = "http://localhost:8090"

uid = '43c0db5c-d829-4929-8efc-5e4a13bb202f'

def get_msg():
    r = requests.get(f'{url}/chat/{uid}')
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
    get_msg()
    new_msg("Yay")
    
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