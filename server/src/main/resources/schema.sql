DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS bookings CASCADE;
DROP TABLE IF EXISTS comments CASCADE;
DROP TABLE IF EXISTS items CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE IF NOT EXISTS users(
id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name VARCHAR,
email VARCHAR UNIQUE
);

CREATE TABLE IF NOT EXISTS requests(
id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
description VARCHAR,
requestor_id BIGINT REFERENCES users(id),
created TIMESTAMP
);

CREATE TABLE IF NOT EXISTS items(
id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name VARCHAR,
description VARCHAR,
is_available BOOLEAN,
owner_id BIGINT REFERENCES users(id),
request_id BIGINT REFERENCES requests(id)
);

CREATE TABLE IF NOT EXISTS bookings(
id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
start_date TIMESTAMP,
end_date TIMESTAMP,
item_id BIGINT REFERENCES items(id),
booker_id BIGINT REFERENCES users(id),
status VARCHAR
);

CREATE TABLE IF NOT EXISTS comments(
id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
text VARCHAR,
item_id BIGINT REFERENCES items(id),
author_id BIGINT REFERENCES users(id),
created TIMESTAMP
);

