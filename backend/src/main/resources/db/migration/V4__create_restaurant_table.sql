CREATE TABLE restaurant(
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    average_price DOUBLE PRECISION NOT NULL,
    cuisine_type VARCHAR(100) NOT NULL,
    city_id BIGINT NOT NULL, FOREIGN KEY (city_id) references city (id)
);
