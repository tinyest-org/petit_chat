-- TODO: describe schema here

CREATE TABLE signal (
    id uuid,
    user_id uuid,
    chat_id uuid,
    created_at timeuuid,
    deleted_at timeuuid,
    content text,
    type text,
        
    PRIMARY KEY (chat_id, created_at) -- can find by chat id and order / page by created_at
)
WITH CLUSTERING ORDER BY (created_at DESC);