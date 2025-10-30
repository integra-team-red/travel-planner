ALTER TABLE trips
    DROP COLUMN city,
    ADD COLUMN city_id SERIAL,
    ADD CONSTRAINT fk_city_id
    FOREIGN KEY (city_id) references city(id);