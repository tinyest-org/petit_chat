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
-> talk to it using a queue system
-> can have many workers

Push notifications
SSE when app is opened -> requires internal notification system to handle distributed app -> Queue / reactive messaging

## SSO
-> use keycloak

## Messages
-> use S3 to store attachments

## Queries

- find user by username
- find all my chats paged
- get messages from chat at offset
- find in chat by message content
