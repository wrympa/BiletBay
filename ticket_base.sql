USE biletbay;

DROP TABLE IF EXISTS tickets;
-- remove table if it already exists and start from scratch

CREATE TABLE tickets(
                        ticket_id char(10), -- 5char 5num
                        event_name CHAR(40),
                        post_date date,
                        posted_by char(20),
                        bought_by char(20),
                        category char(20),
                        price DECIMAL(6, 2),
                        description char(200),
                        img char(14),
                        type char(3),
                        pending_buyer char(20),
                        auct_date DATE,
                        auct_time TIME
);

INSERT INTO tickets VALUES
                        ('oUbDa63954', 'imagine dragons 2023', '2023-05-15', 'Dream', 'GSagh', 'music', 150.00,'imagine dragons koncertis bileti, adgili D62', 'tickpick1.jpg', 'buy', 'null', null, null),
                        ('mfdhu53543', 'bruno mars 2023', '2023-06-15', 'Dream', 'null', 'music', 300.00, 'bruno marsis koncertis bileti, adgili C53', 'tickpick1.jpg', 'buy', 'null', null, null),
                        ('hgowl45394', 'uefa u21 championship finale', '2023-06-24', 'Dream', 'null', 'sport', 200.00, 'finalis matchis bileti, row 4 seat 5 seqtori 10', 'tickpick2.jpg', 'buy', 'null', null, null),
                        ('bdhds83472', 'uefa u21 championship finale', '2023-06-24', 'Dream', 'null', 'sport',  200.00, 'finalis matchis bileti, row 4 seat 6 seqtori 10', 'tickpick2.jpg', 'buy', 'null', null, null),
                        ('fahiw74281', 'oppenheimer', '2023-06-25', 'Dream', 'null', 'cinema', 55.00, 'openhaimeris pirveli seansis 20 ivliss, row 4 seat 10 galeria 19:10', 'tickpick2.jpg', 'buy', 'null', null, null),
                        ('fasfw74281', 'oppenheimer', '2023-06-25', 'Dream', 'null', 'cinema', 49.50, 'openhaimeris pirveli seansis 20 ivliss, row 4 seat 9 galeria 19:10', 'tickpick2.jpg', 'buy', 'null', null, null),
                        ('jhfbq32489', 'barbie', '2023-06-23', 'Dream', 'null', 'cinema', 50.00, 'barbis pirveli seansis 20 ivlibss, biletebi, row 1 seat 2 galeria 15:10', 'tickpick2.jpg', 'buy', 'null', null, null),
                        ('hgowl45395', 'uefa u21 championship finale', '2023-06-24', 'Dream', 'null', 'sport', 200.00, 'finalis matchis bileti, row 4 seat 5 seqtori 11', 'tickpick3.jpg', 'auc', 'null', '2023-08-23', '15:00:00'),
                        ('bdhds83473', 'uefa u21 championship finale', '2023-06-24', 'Dream', 'null', 'sport',  200.00, 'finalis matchis bileti, row 4 seat 6 seqtori 11', 'tickpick3.jpg', 'auc', 'null', '2023-08-23', '15:00:00'),
                        ('fahiw74282', 'oppenheimer', '2023-06-25', 'Dream', 'null', 'cinema', 50.00, 'openhaimeris pirveli seansis 20 ivliss, row 4 seat 11 galeria 19:10', 'tickpick3.jpg', 'auc', 'null', '2023-08-23', '15:00:00'),
                        ('fasfw74283', 'oppenheimer', '2023-06-25', 'Dream', 'null', 'cinema', 47.00, 'openhaimeris pirveli seansis 20 ivliss, row 4 seat 12 galeria 19:10', 'tickpick3.jpg', 'auc', 'null', '2023-08-23', '15:00:00'),
                        ('jhfbq32490', 'barbie', '2023-06-23', 'Dream', 'null', 'cinema', 50.00, 'barbis pirveli seansis 20 ivlibss, biletebi, row 1 seat 3 galeria 15:10', 'tickpick3.jpg', 'auc', 'null', '2023-08-23', '15:00:00')