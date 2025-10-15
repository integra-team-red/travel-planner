ALTER TABLE point_of_interest
    ADD COLUMN address VARCHAR(500);
UPDATE point_of_interest SET address = '.' WHERE true;
ALTER TABLE point_of_interest
    ALTER COLUMN address SET NOT NULL;

