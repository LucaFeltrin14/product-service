CREATE TABLE product.product (
    id_product VARCHAR(36) PRIMARY KEY,
    tx_name VARCHAR(256) NOT NULL,
    vl_price DOUBLE PRECISION NOT NULL,
    tx_unit VARCHAR(64) NOT NULL
);
