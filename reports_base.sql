USE biletbay;

DROP TABLE IF EXISTS reports;
-- remove table if it already exists and start from interests

CREATE TABLE reports (
                        reported CHAR(20),
                        reported_by char(20),
                        reason text
);
