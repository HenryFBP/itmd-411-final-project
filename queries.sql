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

DROP TABLE IF EXISTS categories;
CREATE TABLE IF NOT EXISTS categories
(
	CID			INTEGER			NOT NULL UNIQUE AUTO_INCREMENT,
    category	VARCHAR(64)		NOT NULL,
    
    PRIMARY KEY (CID)
);
DESCRIBE categories;

DROP TABLE IF EXISTS severities;
CREATE TABLE IF NOT EXISTS severities
(
	SID				INTEGER			NOT NULL UNIQUE AUTO_INCREMENT,
    severity_level	VARCHAR(64)		NOT NULL,
    
    PRIMARY KEY (SID)
);
DESCRIBE severities;


DROP TABLE IF EXISTS tickets;
CREATE TABLE IF NOT EXISTS tickets 
(
	TID			INTEGER 		NOT NULL UNIQUE AUTO_INCREMENT,
    PID	 		INTEGER 		NOT NULL,
    CID			INTEGER 		NOT NULL,
    shortDesc	TEXT		 	NOT NULL,
    SID			INTEGER 		NOT NULL,
    longDesc	TEXT,
    startDate	DATE 			NOT NULL,
    endDate		DATE,
    
    PRIMARY KEY (TID),
    FOREIGN KEY (PID) REFERENCES users(PID),
    FOREIGN KEY (CID) REFERENCES categories(CID)
);
DESCRIBE tickets;

DROP TABLE IF EXISTS user_types;
CREATE TABLE IF NOT EXISTS user_types
(
	user_type			INTEGER			NOT NULL UNIQUE AUTO_INCREMENT,
    user_type_string	VARCHAR(20)		NOT NULL,
    
    PRIMARY KEY (user_type)
);
DESCRIBE user_types;


INSERT INTO
	categories (category)
VALUES
('other'),
('tech'),
('building');
SELECT * FROM categories;

INSERT INTO
	severities (severity_level)
VALUES
('only me affected'), 
('room affected'), 
('office affected'), 
('building affected'), 
('armageddon');
SELECT * FROM severities;


INSERT INTO 
	user_types (user_type_string) 
VALUES
('NORMAL_USER'),
('ADMINISTRATOR');
SELECT * FROM user_types;
 
INSERT INTO
	users (username, password, email, name, user_type)
VALUES
('HenryFBP','iamapassword','hpost@hawk.iit.edu','Henry Post',2),
('bolo','alsopassword','jbolo@hawk.iit.edu','Dartor Bolo',1),
('peter','passwordhere','peter@hawk.iit.edu','Peter',1),
('mike','mikerocks12345','mike@hawk.iit.edu','Michael Bernauer',1),
('max','password','max@hawk.iit.edu','Max Oellean',1),
('admin','','admin@admin.admin','Admin admin',2);
SELECT * FROM users;

INSERT INTO
	tickets (PID, shortDesc, longDesc, CID, SID, startDate)
VALUES
	(2, 'henry is asleep', 		'henry has fallen asleep at his desk', 1, 1, DATE(NOW())),
	(1, 'bolo staring at me', 	'bolo is staring at me. I think he thinks i\'m asleep.', 1, 1, DATE(NOW()));
SELECT * FROM tickets;

-- 1 row(s) affected, 1 warning(s): 1292 Incorrect date value: '2017-11-30 19:09:37' for column 'endDate' at row 1 Rows matched: 1  Changed: 1  Warnings: 1


UPDATE tickets
	SET endDate = ADDDATE(NOW(), INTERVAL 1 DAY)
    WHERE TID = 1;
    
SELECT
	TID 'Ticket ID',
	PID 'Person ID',
	name 'Name',
	RES.CID 'Category ID',
	category 'Category',
	RES.SID 'Severity ID',
    severity_level 'Severity Level',
	shortDesc 'Short Description',
	longDesc 'Long Description',
	startDate 'Start date',
	endDate 'End date'
FROM
	(
    SELECT
		TID,
		PID,
		name,
		RES.CID,
		category,
		SID,
		shortDesc,
		longDesc,
		startDate,
		endDate
	FROM
		(
		SELECT
			TID,
			t.PID,
			name,
			CID,
			SID,
			shortDesc,
			longDesc, 
			startDate, 
			endDate 
		FROM
			tickets t
		INNER JOIN
			(
            SELECT
				name,
                PID    -- get users
			FROM 
				users
			) u
		ON
			t.PID = u.PID  -- put ticket ids on users
		) AS RES
	INNER JOIN
		(SELECT CID, category FROM categories) c 
	ON
		c.CID = RES.CID -- put category name on CID
	) AS RES
INNER JOIN 
	(SELECT * FROM severities) s 
ON
	s.SID = RES.SID -- put severity name on SID

