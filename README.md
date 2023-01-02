# Petit chat

## Requirements

- send notifications to users
- chat with 2 or more users
- use an sso to connect
- messages can have
  - text OK
  - attachments OK
  - reactions OK

- scylladb as db

## Notifications

Use external notification gateway
-> talk to it using a queue system // topic by userId
-> can have many workers

Push notifications
SSE when app is opened -> requires internal notification system to handle distributed app -> Queue / reactive messaging

## SSO

-> use keycloak

## Messages / Signal (to support more use cases)

-> use cassandra udt / collections to store the infos ? -> handle the query get all files in chat
-> use S3 to store attachments -> [seaweedfs](https://github.com/seaweedfs/seaweedfs)
-> dedupe using hashing ?

## Queries

db: scylladb

- find user by username -> ok but not paged
- find all my chats paged -> ok but not paged
- get messages from chat at offset -> ok
- find in chat by message content -> need text indexer
  - should implem meili search first for basic use -> OK
  - implem elastic or more powerful with sharding later

For each message
-> handle reactions -> ok not paged for now
-> handle views (wich users saw the message) / can handle the deliver state as well -> ok, not paged for now

### Optimizations ?

store the count of reactions on the signal directly -> use atomic increment / decrement

### Signal types

- msg
- joined / leaved
- calls

### TODO

- add signal types
  - joined / leaved -> WIP, need test / support for jitsi for audio / video ?
  - calls

- add support for current status
- add support for share point api for status
- add support for a way to add plugings
  - add support for webhook
- check websocket infrastructure for user handling
- add support for E2E encryption
  - add support for asymetric encryption
- add image pipeline
  when sending a file into the chat, check if it is an image, if it's an image, then transform it and then upload it to file service and add extension to it
- add support for settings for images
  - auto load
  - low quality to save bandwidth

## Architecture

```d2


client <-> network-provider.gateway
network-provider.gateway <->  api.controller
network-provider.gateway -> seaweed.volume
network-provider.gateway -> sso.controller

api -> sso.controller
api.service <-> queue
api.service -> seaweed.master
api.service -> seaweed.volume
api.service -> text-indexer
api.repository -> db-cluster.db

network-provider {
  gateway
}

seaweed {
  volume
  master
  master -> volume
}

api {
  controller
  service
  repository
  controller -> service
  service -> repository
}

sso {
  controller
  postgres
  controller -> postgres
}

db-cluster {
  db
  manager
  manager -> db
}

# d2-vscode can syntax highlight nested markdown correctly.
y: |`md
  
`|
```
