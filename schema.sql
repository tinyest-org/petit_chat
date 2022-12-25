-- TODO: describe schema here
drop table signal;
CREATE TABLE signal (
    chat_id uuid,
    created_at timestamp,
    user_id uuid,
    deleted_at timestamp,
    content text,
    type text,
        
    PRIMARY KEY (chat_id, created_at) -- can find by chat id and order / page by created_at
)
WITH CLUSTERING ORDER BY (created_at DESC);


drop table chat;

CREATE TABLE chat (
    id uuid,
    created_at timestamp,
    last_updated_at timestamp,
    --    archived boolean, -- if should not be shown
    name text,
        
    PRIMARY KEY (id, last_update_at) -- can find by chat id and order / page by last_updated_at
)
WITH CLUSTERING ORDER BY (last_update_at DESC);

drop table chat_by_user;

CREATE TABLE chat_by_user (
    chat_id uuid,
    user_id uuid,
        
    PRIMARY KEY (user_id, chat_id) -- can find by chat id and order / page by last_updated_at
)

drop table user_by_chat;

CREATE TABLE user_by_chat (
    chat_id uuid,
    user_id uuid,
        
    PRIMARY KEY (chat_id) -- can find by chat id and order / page by last_updated_at
)