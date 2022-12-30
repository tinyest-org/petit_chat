drop table reaction;
CREATE TABLE reaction (
    signal_id uuid,
    user_id uuid, 

    created_at timeuuid,

    value text,
        
    PRIMARY KEY (signal_id, user_id, value, created_at) -- can find by chat id and order / page by last_updated_at
);
