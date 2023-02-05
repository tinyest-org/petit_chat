drop table if EXISTS signal;
CREATE TABLE signal (
    
    chat_id uuid,
    created_at timeuuid,
    deleted_at timestamp,
    
    user_id uuid,
    type int,
    content text,
        
    PRIMARY KEY ((chat_id), created_at, type) -- can find by chat id and order / page by created_at
)
WITH CLUSTERING ORDER BY (created_at DESC, type ASC);

drop table if EXISTS  chat;
CREATE TABLE chat (
    id uuid,
    created_at timeuuid,
    last_updated_at timeuuid,
    --    archived boolean, -- if should not be shown
    name text,
        
    PRIMARY KEY (id, last_updated_at) -- can find by chat id and order / page by last_updated_at
)
WITH CLUSTERING ORDER BY (last_updated_at DESC);

drop table if EXISTS  chat_by_user;
CREATE TABLE chat_by_user (
    chat_id uuid,
    user_id uuid,
        
    PRIMARY KEY (user_id, chat_id) -- can find by chat id and order / page by last_updated_at
);

drop table  if EXISTS user_by_chat;

CREATE TABLE user_by_chat (
    chat_id uuid,
    user_id uuid,
        
    PRIMARY KEY (chat_id, user_id) -- can find by chat id and order / page by last_updated_at
);

drop table  if EXISTS reaction;
CREATE TABLE reaction (
    signal_id uuid,
    user_id uuid, 

    created_at timeuuid,

    value text,
        
    PRIMARY KEY (signal_id, user_id, value) -- can find by chat id and order / page by last_updated_at
);
WITH CLUSTERING ORDER BY (created_at DESC);




drop table if EXISTS  user;

CREATE TABLE user (
    id uuid,
    name text,
    profile_picture text,

    PRIMARY KEY (id) -- can find by chat id and order / page by last_updated_at
);


drop table  if EXISTS chat_user_settings;

CREATE TABLE chat_user_settings (
    chat_id uuid,
    user_id uuid,
        
    PRIMARY KEY ((chat_id, user_id))
);

drop table if EXISTS  chat_user_cursor;

CREATE TABLE chat_user_cursor (
    chat_id uuid,
    user_id uuid,
    last_signal_read timeuuid,
    updated_at timeuuid,

    PRIMARY KEY ((chat_id), user_id, last_signal_read)
)
WITH CLUSTERING ORDER BY (user_id DESC, last_signal_read DESC);

-- INSERT INTO "chat2"."chat" ("id", "last_updated_at", "created_at", "name") VALUES (43c0db5c-d829-4929-8efc-5e4a13bb202f, NOW(), NOW(), 'test');
-- INSERT INTO "chat2"."chat_by_user" ("user_id", "chat_id") VALUES (43c0db5c-d829-4929-8efc-5e4a13bb202f, 43c0db5c-d829-4929-8efc-5e4a13bb202f);
-- INSERT INTO "chat2"."user_by_chat" ("chat_id", "user_id") VALUES (43c0db5c-d829-4929-8efc-5e4a13bb202f, 43c0db5c-d829-4929-8efc-5e4a13bb202f);
INSERT INTO "chat2"."user" ("id", "name", "profile_picture") VALUES (1d7d2f85-1ef1-4e7b-994b-ebf24cac2b99, 'Paul', 'https://documents.junior-entreprises.com/kiwi-public/user/45120/profile/f24b5ac1-4521-4081-86c1-61d690176529.png');
-- INSERT INTO "chat2"."user" ("id", "name", "profile_picture") VALUES (43c0db5c-d829-4929-8ecc-5e4a13bb202f, 'Turbo', null);
