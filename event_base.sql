USE biletbay;

DROP TABLE IF EXISTS events;
-- remove table if it already exists and start from scratch

CREATE TABLE events (
                        event_name CHAR(40),
                        popularity INTEGER,
                        event_date date,
                        event_time time,
                        category char(20),
                        img char(45),
                        description char(80),
                        approved smallint
);

INSERT INTO events VALUES
                       ('imagine dragons 2023', 11164, '2023-07-31', '17:00', 'music', 'imagine dragons 2023.jpg', 'imagine dragons tbilisshi', 1),
                       ('bruno mars 2023', 9076 , '2023-10-01', '16:00', 'music', 'bruno mars 2023.jpg', 'bruno mars tbilisshi', 1),
                       ('uefa u21 championship finale', 12000 , '2023-06-08', '20:00', 'sport', 'uefa u21 championship finale.jpg', 'u21 wc finale tbilisshi', 1),
                       ('oppenheimer', 5000 , '2023-07-20', '20:00', 'cinema', 'oppenheimer.jpg', 'openhaimeris premiera', 1),
                       ('barbie', 7000 , '2023-07-20', '20:00', 'cinema', 'barbie.jpg', 'barbis premiera', 1),
                       ('Arctic Monkeys Concert', 100 , '2023-08-07', '20:00', 'music', 'default.png', 'yvelaze slei koncerti', 0),
                       ('The Strokes Concert', 80 , '2023-08-10', '20:00', 'music', 'default.png', 'meore slei koncerti', 0)