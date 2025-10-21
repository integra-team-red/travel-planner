CREATE TABLE coffee_shop(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    city_id SERIAL, FOREIGN KEY (city_id) references city (id),
    address VARCHAR (100) NOT NULL,
    opening_hours VARCHAR(50) NOT NULL,
    description VARCHAR(100) NOT NULL,
    average_price DOUBLE PRECISION NOT NULL,
    rating DOUBLE PRECISION NOT NULL,
    image VARCHAR (300)
);