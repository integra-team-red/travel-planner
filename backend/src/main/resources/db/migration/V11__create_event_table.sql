CREATE TABLE event(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(100) NOT NULL,
    start_time  TIMESTAMP NOT NULL,
    end_time    TIMESTAMP NOT NULL,
    poi_id      SERIAL, FOREIGN KEY (poi_id) references point_of_interest (id),
    price       DOUBLE PRECISION NOT NULL,
    audience    VARCHAR(20) NOT NULL
);
