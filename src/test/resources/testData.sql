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

CREATE TABLE orders(
id bigserial PRIMARY KEY,
create_time timestamp,
amount bigint,
user_id bigint,
FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE items(
id bigserial PRIMARY KEY,
quantity int,
order_id bigint,
cert_id bigint,
FOREIGN KEY (order_id) REFERENCES orders(id),
FOREIGN KEY (cert_id) REFERENCES gift_certificate(id)
);

INSERT INTO tag (name) VALUES ('relax'), ('sport'), ('quiz'), ('motor');

INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date)
VALUES ('Rest1', 'sauna', 1900, 10, '2023-04-17', '2023-04-17'),
('Rest2', 'spa light', 2500, 10, '2023-04-17', '2023-04-17'),
('Rest3', 'spa inclusive', 9500, 15, '2023-04-18', '2023-04-18'),
('Auto','Carting', 3000, 15, '2023-04-18', '2023-04-18'),
('Quest Zone1', 'WILD WEST: GOLD RUSH', 8000, 14, '2023-04-19', '2023-04-19'),
('Quest Zone2', 'THE MAGEs TOWER', 8500, 14, '2023-04-19', '2023-04-19'),
('Quad bike', 'Road 10km, 1 person', 7000, 30, '2023-04-20', '2023-04-20');


INSERT INTO certificate_tag (tag_id, certificate_id) VALUES (1, 1), (1, 2), (1, 3),
(2, 4), (3, 5), (3, 6), (4, 7), (4, 4);

INSERT INTO users (user_name, password, email) VALUES ('Elen', '321', 'elen@mail.com'),
('Jon', '123', 'jon@mail.com'), ('Sema', '789', 'sema@mail.com');

INSERT INTO orders (create_time, amount, user_id) VALUES ('2023-05-02', 30500, 1);
INSERT INTO items (order_id, cert_id, quantity) VALUES (1,4,1), (1,6,1), (1,3,2);

INSERT INTO orders (create_time, amount, user_id) VALUES ('2023-05-02', 21000, 2);
INSERT INTO items (order_id, cert_id, quantity) VALUES (2,4,1), (2,6,1), (2,3,1);

INSERT INTO orders (create_time, amount, user_id) VALUES ('2023-05-03', 21500, 2);
INSERT INTO items (order_id, cert_id, quantity) VALUES (3,5,2), (3,5,1), (3,6,1);

INSERT INTO orders (create_time, amount, user_id) VALUES ('2023-05-03', 44000, 2);
INSERT INTO items (order_id, cert_id, quantity) VALUES (4,1,1), (4,2,1), (4,4,1), (4,6,1), (4,3,3);

INSERT INTO orders (create_time, amount, user_id) VALUES ('2023-05-04', 17500, 3);
INSERT INTO items (order_id, cert_id, quantity) VALUES (5,3,1), (5,5,1);