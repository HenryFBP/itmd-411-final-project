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
    user_type			INTEGER		NOT NULL,
    
    
    PRIMARY KEY (PID, username)
);	
DESCRIBE users;

DROP TABLE IF EXISTS tickets;
CREATE TABLE IF NOT EXISTS tickets 
(
	TID			INTEGER 		NOT NULL UNIQUE AUTO_INCREMENT,
    PID	 		INTEGER 		NOT NULL,
    shortDesc	TEXT		 	NOT NULL,
    category	VARCHAR(20) 	NOT NULL,
    severity	INTEGER 		NOT NULL,
    longDesc	TEXT,
    startDate	DATE 			NOT NULL,
    endDate		DATE,
    
    PRIMARY KEY (TID),
    FOREIGN KEY (PID) REFERENCES users(PID)
);
DESCRIBE tickets;

DROP TABLE IF EXISTS user_types;
CREATE TABLE IF NOT EXISTS user_types
(
	user_type			INTEGER			NOT NULL UNIQUE,
    user_type_string	VARCHAR(20)		NOT NULL,
    
    PRIMARY KEY (user_type)
);



INSERT INTO user_types VALUES(1, 'NORMAL_USER');
INSERT INTO user_types VALUES(2, 'ADMINISTRATOR');

INSERT INTO users VALUES(1,'HenryFBP','iamapassword','hpost@hawk.iit.edu','Henry Post',2);
INSERT INTO users VALUES(2,'bolo','alsopassword','jbolo@hawk.iit.edu','Dartor Bolo',1);
INSERT INTO users VALUES(3,'peter','passwordhere','peter@hawk.iit.edu','Peter',1);
INSERT INTO users VALUES(4,'mike','mikerocks12345','mike@hawk.iit.edu','Michael Bernauer',1);
INSERT INTO users VALUES(4,'max','password','max@hawk.iit.edu','Max Oellean',1);

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