CREATE TABLE proposals (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           type VARCHAR(20) NOT NULL,
                           status VARCHAR(20) NOT NULL,
                           city_id      SERIAL, FOREIGN KEY (city_id) references city (id),
                           description TEXT,
                           price DOUBLE PRECISION,
                           average_price DOUBLE PRECISION,
                           cuisine_type VARCHAR(100),
                           poi_type VARCHAR(50)
);