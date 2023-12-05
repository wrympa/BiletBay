USE biletbay;

DROP TABLE IF EXISTS interests;
-- remove table if it already exists and start from interests

CREATE TABLE interests (
                        event_name CHAR(40),
                        username char(20)
);

INSERT INTO interests VALUES
                          ('imagine dragons 2023', 'GSagh'),
                          ('oppenheimer', 'GSagh'),
                          ('bruno mars 2023', 'Dream'),
                          ('uefa u21 championship finale', 'Dream'),
                          ('oppenheimer', 'Dream'),
                          ('bruno mars 2023', 'nshan'),
                          ('barbie', 'ragdollboy'),
                          ('uefa u21 championship finale', 'astakun')