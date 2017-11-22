CREATE DATABASE IF NOT EXISTS testdb;
USE testdb;

DROP TABLE IF EXISTS users;
CREATE TABLE IF NOT EXISTS users
(
	PID					INTEGER 	NOT NULL UNIQUE AUTO_INCREMENT,
    username			VARCHAR(64) NOT NULL UNIQUE,
    password			VARCHAR(64) NOT NULL,
    email				VARCHAR(64) NOT NULL UNIQUE,
    name				VARCHAR(64) NOT NULL,
    
    
    PRIMARY KEY (PID, username)
);	
DESCRIBE users;

DROP TABLE IF EXISTS tickets;
CREATE TABLE IF NOT EXISTS tickets 
(
	TID			INTEGER 		NOT NULL UNIQUE AUTO_INCREMENT,
    PID	 		INTEGER 		NOT NULL,
    shortDesc	VARCHAR(100) 	NOT NULL,
    category	VARCHAR(20) 	NOT NULL,
    severity	INTEGER 		NOT NULL,
    longDesc	VARCHAR(1000),
    startDate	DATE 			NOT NULL,
    endDate		DATE,
    
    PRIMARY KEY (TID),
    FOREIGN KEY (PID) REFERENCES users(PID)
);
DESCRIBE tickets;

INSERT INTO users VALUES(1,'HenryFBP','iamapassword','hpost@hawk.iit.edu','Henry Post');
INSERT INTO users VALUES(2,'bolo','alsopassword','jbolo@hawk.iit.edu','Dartor Bolo');
INSERT INTO users VALUES(3,'peter','passwordhere','peter@hawk.iit.edu','Peter');

INSERT INTO tickets VALUES(1, 2, 'henry is asleep', 'other', 1, 'henry has fallen asleep at his desk', DATE(NOW()), null);
INSERT INTO tickets VALUES(2, 1, 'bolo staring at me', 'other', 1, 'bolo is staring at me. I think he thinks i\'m asleep.', DATE(NOW()), null);

SELECT * FROM users;
SELECT * FROM tickets;

UPDATE tickets
	SET endDate = ADDDATE(NOW(), INTERVAL 1 DAY)
    WHERE TID = 1;

SELECT TID, name, t.PID, shortDesc, longDesc, category, startDate, endDate FROM tickets t
INNER JOIN (SELECT name, PID from users) u
ON t.PID = u.PID