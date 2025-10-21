CREATE TABLE spa(
    id SERIAL PRIMARY KEY,
    name VARCHAR(500) NOT NULL,
    schedule VARCHAR(500) NOT NULL,
    price_lower_bound DOUBLE PRECISION NOT NULL,
    price_upper_bound DOUBLE PRECISION NOT NULL,
    rating BIGINT NOT NULL,
    description VARCHAR(500),
    city_id BIGINT NOT NULL, FOREIGN KEY (city_id) REFERENCES city(id)
);