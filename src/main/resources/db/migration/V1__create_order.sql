CREATE TABLE product (
    id SERIAL  PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price NUMERIC NOT NULL
);

CREATE TABLE tb_order (
    id SERIAL  PRIMARY KEY,
    ref_id VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(50),
    total_amount NUMERIC
);

CREATE TABLE order_products (
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES tb_order(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);
