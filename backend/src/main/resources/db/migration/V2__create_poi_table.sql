CREATE TABLE point_of_interest(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(100) NOT NULL,
    city_id      SERIAL, FOREIGN KEY (city_id) references city (id),
    price       DOUBLE PRECISION NOT NULL,
    type        VARCHAR(20) NOT NULL
);
