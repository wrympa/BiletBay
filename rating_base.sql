USE biletbay;

DROP TABLE IF EXISTS rating;
-- remove table if it already exists and start from interests

CREATE TABLE rating (
                        rated CHAR(20),
                        rated_by char(20),
                        rating int
);
