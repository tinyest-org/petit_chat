# Petit chat


## Requirements

- send notifications to users
- chat with 2 or more users
- use an sso to connect
- messages can have
 - text
 - attachments
 - reactions


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
-> use S3 to store attachments -> [seaweedfs](https://github.com/seaweedfs/seaweedfs)
-> dedupe using hashing ?

## Queries

db: scylladb

- find user by username
- find all my chats paged -> ok but not paged
- get messages from chat at offset -> ok but not paged -> but in correct order
- find in chat by message content
