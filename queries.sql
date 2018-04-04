CREATE DATABASE IF NOT EXISTS 411labs;
USE 411labs;

DROP TABLE IF EXISTS h1_j1_users;
CREATE TABLE IF NOT EXISTS h1_j1_users
(
	PID					INTEGER 	NOT NULL UNIQUE AUTO_INCREMENT,
    username			VARCHAR(64) NOT NULL UNIQUE,
    password			VARCHAR(64) NOT NULL,
    email				VARCHAR(64) NOT NULL UNIQUE,
    name				VARCHAR(64) NOT NULL,
    user_type			INTEGER		NOT NULL,
    
    
    PRIMARY KEY (PID, username)
);	
DESCRIBE h1_j1_users;

DROP TABLE IF EXISTS h1_j1_categories;
CREATE TABLE IF NOT EXISTS h1_j1_categories
(
	CID			INTEGER			NOT NULL UNIQUE AUTO_INCREMENT,
    category	VARCHAR(64)		NOT NULL,
    
    PRIMARY KEY (CID)
);
DESCRIBE h1_j1_categories;

DROP TABLE IF EXISTS h1_j1_severities;
CREATE TABLE IF NOT EXISTS h1_j1_severities
(
	SID				INTEGER			NOT NULL UNIQUE AUTO_INCREMENT,
    severity_level	VARCHAR(64)		NOT NULL,
    
    PRIMARY KEY (SID)
);
DESCRIBE h1_j1_severities;


DROP TABLE IF EXISTS h1_j1_tickets;
CREATE TABLE IF NOT EXISTS h1_j1_tickets 
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
    FOREIGN KEY (PID) REFERENCES h1_j1_users(PID),
    FOREIGN KEY (CID) REFERENCES h1_j1_categories(CID)
);
DESCRIBE h1_j1_tickets;

DROP TABLE IF EXISTS h1_j1_user_types;
CREATE TABLE IF NOT EXISTS h1_j1_user_types
(
	user_type			INTEGER			NOT NULL UNIQUE AUTO_INCREMENT,
    user_type_string	VARCHAR(20)		NOT NULL,
    
    PRIMARY KEY (user_type)
);
DESCRIBE h1_j1_user_types;


INSERT INTO
	h1_j1_categories (category)
VALUES
('other'),
('tech'),
('building');
SELECT * FROM h1_j1_categories;

INSERT INTO
	h1_j1_severities (severity_level)
VALUES
('only me affected'), 
('room affected'), 
('office affected'), 
('building affected'), 
('armageddon');
SELECT * FROM h1_j1_severities;


INSERT INTO 
	h1_j1_user_types (user_type_string) 
VALUES
('NORMAL_USER'),
('ADMINISTRATOR');
SELECT * FROM h1_j1_user_types;
 
INSERT INTO
	h1_j1_users (username, password, email, name, user_type)
VALUES
('HenryFBP','iamapassword','hpost@hawk.iit.edu','Henry Post',2),
('bolo','alsopassword','jbolo@hawk.iit.edu','Dartor Bolo',1),
('peter','passwordhere','peter@hawk.iit.edu','Peter',1),
('mike','mikerocks12345','mike@hawk.iit.edu','Michael Bernauer',1),
('max','password','max@hawk.iit.edu','Max Oellean',1),
('admin','','admin@admin.admin','Admin admin',2);
SELECT * FROM users;

INSERT INTO
	h1_j1_tickets (PID, shortDesc, longDesc, CID, SID, startDate)
VALUES
	(2, 'henry is asleep', 		'henry has fallen asleep at his desk', 1, 1, DATE(NOW())),
	(1, 'bolo staring at me', 	'bolo is staring at me. I think he thinks im asleep.', 1, 1, DATE(NOW()));
SELECT * FROM h1_j1_tickets;


UPDATE h1_j1_tickets
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
			h1_j1_tickets t
		INNER JOIN
			(
            SELECT
				name,
                PID    -- get users
			FROM 
				h1_j1_users
			) u
		ON
			t.PID = u.PID  -- put ticket ids on users
		) AS RES
	INNER JOIN
		(SELECT CID, category FROM h1_j1_categories) c 
	ON
		c.CID = RES.CID -- put category name on CID
	) AS RES
INNER JOIN 
	(SELECT * FROM h1_j1_severities) s 
ON
	s.SID = RES.SID -- put severity name on SID

