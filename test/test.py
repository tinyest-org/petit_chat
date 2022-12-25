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
        

def scenario():
    get_msg()
    new_msg("Yay")
    
scenario()