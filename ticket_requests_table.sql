USE biletbay;

DROP TABLE IF EXISTS ticket_requests_table;
-- remove table if it already exists and start from scratch

CREATE TABLE ticket_requests_table(
                        sent_by char(20),
                        sent_to char(20),
                        ticket_id char(10)
);

INSERT INTO ticket_requests_table VALUES
                        ('nshan', 'Dream', 'mfdhu53543');
