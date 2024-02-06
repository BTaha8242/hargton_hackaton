CREATE TABLE client
(
    ID          SERIAL PRIMARY KEY,
    REF_CLIENT  VARCHAR(255),
    CLIENT_NAME VARCHAR(255),
    MAIL        VARCHAR(255)
);

CREATE TABLE product
(
    ID               SERIAL PRIMARY KEY,
    PRODUCTNAME     VARCHAR(255),
    PRODUCTQUANTITY INTEGER,
    PRODUCTPRICE    FLOAT

);
CREATE TABLE client_product
(
    CLIENT_ID  INTEGER,
    PRODUCTS_ID INTEGER
);



insert into client
values (1, 'taha', 'tahaname', 'tahamail');