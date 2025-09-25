ALTER TABLE proposals
    ADD COLUMN city_id SERIAL,
    ADD COLUMN description TEXT,
    ADD COLUMN price DOUBLE PRECISION,
    ADD COLUMN average_price DOUBLE PRECISION,
    ADD COLUMN cuisine_type VARCHAR(100),
    ADD COLUMN poi_type VARCHAR(50);

ALTER TABLE proposals
    ADD CONSTRAINT fk_proposals_city
        FOREIGN KEY (city_id) REFERENCES city(id);