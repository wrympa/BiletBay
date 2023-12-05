USE biletbay;

DROP TABLE IF EXISTS accounts;
-- remove table if it already exists and start from scratch

CREATE TABLE accounts
(
    username     char(20),
    pfp          char(20),
    phone_number char(11),
    password     char(40),
    interests    TEXT,
    moderator int
);

INSERT INTO accounts
VALUES ('GSagh', 'biker.PNG', '599 999 444', 'd0ef368b57a861d5ebffdc3f469c049eb3d7b8db',
        'imagine dragons 2023', 0),
       ('Dream', 'jin.jpg', '599 423 432', '547c7fe125a4ad2776612869f8739ababe17a311',
        'bruno mars 2023$uefa u21 championship finale$oppenheimer', 0),
       ('nshan', 'nerd.jpg', '599 349 243', 'c959e8dd5fa10a8707759417bc60e7d3b4ee6aed', 'bruno mars 2023', 0),
       ('admin', 'default.jpg', ' ', 'cae1826bd5738775ea4d826fc2d31bf46706bfac', '', 1),
       ('ragdollboy', 'zheongxina.jpg', '567 764 284', '16f75f3d4c89ca893d28a837f67ee5b332b0271e', 'barbie', 0)