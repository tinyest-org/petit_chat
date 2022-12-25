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