import requests

url = "http://localhost:8090"

uid = '43c0db5c-d829-4929-8efc-5e4a13bb202f'

def get_msg():
    r = requests.get(f'{url}/chat/{uid}')
    print(r)
    print(r.text)
    
    
get_msg()