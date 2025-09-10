CREATE TABLE proposals(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(20) NOT NULL,
    status  VARCHAR(20) NOT NULL
);
