CREATE TABLE IF NOT EXISTS tag (
id bigserial PRIMARY KEY,
name VARCHAR(255));

CREATE TABLE IF NOT EXISTS gift_certificate (
id bigserial PRIMARY KEY,
name VARCHAR(255),
description VARCHAR(255),
price INTEGER,
duration INTEGER,
create_date timestamptz,
last_update_date timestamp
);

CREATE TABLE IF NOT EXISTS certificate_tag (
id bigserial PRIMARY KEY,
tag_id bigint,
certificate_id bigint,
FOREIGN KEY (tag_id) REFERENCES tag(id),
FOREIGN KEY (certificate_id) REFERENCES gift_certificate(id)
);

CREATE TABLE IF NOT EXISTS users(
 id bigserial PRIMARY KEY,
 user_name VARCHAR(100) NOT NULL,
 password VARCHAR(100) NOT NULL,
 email VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS orders(
id bigserial PRIMARY KEY,
create_time timestamp,
amount bigint,
user_id bigint,
FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS items(
id bigserial PRIMARY KEY,
quantity int,
order_id bigint,
cert_id bigint,
FOREIGN KEY (order_id) REFERENCES orders(id),
FOREIGN KEY (cert_id) REFERENCES gift_certificate(id)
);