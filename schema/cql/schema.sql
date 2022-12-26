drop table signal;
CREATE TABLE signal (
    
    chat_id uuid,
    created_at timeuuid,
    deleted_at timestamp,
    
    user_id uuid,
    type text,
    content text,
        
    PRIMARY KEY (chat_id, created_at) -- can find by chat id and order / page by created_at
)
WITH CLUSTERING ORDER BY (created_at DESC);
drop table chat;

CREATE TABLE chat (
    id uuid,
    created_at timeuuid,
    last_updated_at timeuuid,
    --    archived boolean, -- if should not be shown
    name text,
        
    PRIMARY KEY (id, last_updated_at) -- can find by chat id and order / page by last_updated_at
)
WITH CLUSTERING ORDER BY (last_updated_at DESC);

drop table chat_by_user;

CREATE TABLE chat_by_user (
    chat_id uuid,
    user_id uuid,
        
    PRIMARY KEY (user_id, chat_id) -- can find by chat id and order / page by last_updated_at
);

drop table user_by_chat;

CREATE TABLE user_by_chat (
    chat_id uuid,
    user_id uuid,
        
    PRIMARY KEY (chat_id) -- can find by chat id and order / page by last_updated_at
);



drop table user;

CREATE TABLE user (
    id uuid,
    name text,
        
    PRIMARY KEY (id) -- can find by chat id and order / page by last_updated_at
);


INSERT INTO "chat2"."chat" ("id", "last_updated_at", "created_at", "name") VALUES (43c0db5c-d829-4929-8efc-5e4a13bb202f, NOW(), NOW(), 'test');
INSERT INTO "chat2"."chat_by_user" ("user_id", "chat_id") VALUES (43c0db5c-d829-4929-8efc-5e4a13bb202f, 43c0db5c-d829-4929-8efc-5e4a13bb202f);
INSERT INTO "chat2"."user_by_chat" ("chat_id", "user_id") VALUES (43c0db5c-d829-4929-8efc-5e4a13bb202f, 43c0db5c-d829-4929-8efc-5e4a13bb202f);