import requests
import websockets
import asyncio

from conf import url, host, chat_id, signal_id

def get_ws_token():
    r = requests.get(f'{url}/ws/token-provider')
    print(r)
    print(r.text)
    return r.json()['token']


# add_reaction()
# get_msg(after="44a84700-8520-11ed-805d-0933ac6bdfd8")

token = get_ws_token()
print(f'token is: {token}')

#!/usr/bin/env python


async def hello(token: str):
    async with websockets.connect(f"ws://{host}/ws/{token}") as websocket:
        await websocket.send("Hello world!")
        while True:
            await websocket.recv()

asyncio.run(hello(token))
