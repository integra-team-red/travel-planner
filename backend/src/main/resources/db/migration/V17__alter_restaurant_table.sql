ALTER TABLE restaurant
    ADD COLUMN address VARCHAR(100),
    ADD COLUMN opening_hours VARCHAR(50),
    ADD COLUMN description VARCHAR(100),
    ADD COLUMN rating DOUBLE PRECISION,
    ADD COLUMN image VARCHAR(300);

UPDATE restaurant
SET address = '.',
    opening_hours = '.',
    description = '.',
    rating = 0.0;


ALTER TABLE restaurant
    ALTER COLUMN address SET NOT NULL,
ALTER COLUMN opening_hours SET NOT NULL,
    ALTER COLUMN description SET NOT NULL,
    ALTER COLUMN rating SET NOT NULL;
