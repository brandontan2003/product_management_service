CREATE TABLE IF NOT EXISTS product_table (
    product_id VARCHAR(36) PRIMARY KEY,
    product_name VARCHAR(100),
    product_desc VARCHAR(350),
    price DECIMAL(10, 2)

);