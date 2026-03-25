create table users (
    id uuid primary key,
    name varchar(255) not null,
    email varchar(320) not null unique,
    created_at timestamp with time zone not null
);
