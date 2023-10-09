# technical-support

## Database Tables Postgres
```
CREATE TABLE chats(  
    id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_name VARCHAR(255),
    operator VARCHAR(255),
    operator_id VARCHAR(255),
    priority_level INT,
    messages JSONB,
    created_at TIMESTAMP
);
```
```
CREATE TABLE messages(  
    id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    chat_id BIGINT,
    name VARCHAR(255),
    text TEXT,
    created_at TIMESTAMP
);
```
