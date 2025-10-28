CREATE TABLE trips (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       city VARCHAR(255) NOT NULL,
                       days INT NOT NULL,
                       price REAL NOT NULL,
                       user_id BIGINT NOT NULL, CONSTRAINT fk_trips_user FOREIGN KEY (user_id) REFERENCES users (id)
);
