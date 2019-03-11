CREATE TABLE user
(
  userId       INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  userName     VARCHAR(50),
  password     VARCHAR(50),
  active       TINYINT,
  createDate   DATETIME,
  createdBy    varchar(40),
  lastUpdate   TIMESTAMP,
  lastUpdateBy VARCHAR(40)
);

INSERT INTO user value(NULL, 'test', 'test', 0, CURDATE(), 'admin', CURRENT_TIMESTAMP, 'admin');
# SELECT * from user where userName = 'test' and password = 'test';

CREATE TABLE country
(
  countryId    INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  country      VARCHAR(50),
  createDate   DATETIME,
  createdBy    VARCHAR(40),
  lastUpdate   TIMESTAMP,
  lastUpdateBy VARCHAR(40)
);
INSERT INTO country VALUE(null, 'America', CURDATE(), 'test', CURRENT_TIMESTAMP, 'test');
INSERT INTO country VALUE(null, 'Australia', CURDATE(), 'test', CURRENT_TIMESTAMP, 'test');
INSERT INTO country VALUE(null, 'Canada', CURDATE(), 'test', CURRENT_TIMESTAMP, 'test');
INSERT INTO country VALUE(null, 'China', CURDATE(), 'test', CURRENT_TIMESTAMP, 'test');
INSERT INTO country VALUE(null, 'Germany', CURDATE(), 'test', CURRENT_TIMESTAMP, 'test');
INSERT INTO country VALUE(null, 'India', CURDATE(), 'test', CURRENT_TIMESTAMP, 'test');
INSERT INTO country VALUE(null, 'Mexico', CURDATE(), 'test', CURRENT_TIMESTAMP, 'test');
INSERT INTO country VALUE(null, 'New Zealand', CURDATE(), 'test', CURRENT_TIMESTAMP, 'test');
INSERT INTO country VALUE(null, 'Russia', CURDATE(), 'test', CURRENT_TIMESTAMP, 'test');
INSERT INTO country VALUE(null, 'South Africa', CURDATE(), 'test', CURRENT_TIMESTAMP, 'test');

CREATE TABLE city
(
  cityId       INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  city         VARCHAR(50),
  countryId    INT(10),
  createDate   DATETIME,
  createdBy    VARCHAR(40),
  lastUpdate   TIMESTAMP,
  lastUpdateBy VARCHAR(40),
  FOREIGN KEY (countryId) REFERENCES country (countryId) ON DELETE SET NULL ON UPDATE CASCADE
);
INSERT INTO city VALUE(null, 'American fork', 1, CURDATE(), 'test', CURRENT_TIMESTAMP, 'test');
# SELECT * FROM city;
# DELETE FROM city;

CREATE TABLE address
(
  addressId    INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  address      VARCHAR(50),
  address2     VARCHAR(50),
  cityId       INT(10),
  postalCode   varchar(10),
  phone        varchar(20),
  createDate   DATETIME,
  createdBy    VARCHAR(40),
  lastUpdate   TIMESTAMP,
  lastUpdateBy VARCHAR(40),
  FOREIGN KEY (cityId) REFERENCES city (cityId) ON DELETE SET NULL ON UPDATE CASCADE
);

INSERT INTO address VALUE(null, '828s 7540e', '828s 7402e', 1, '84003', '801-885-4158',  CURDATE(), 'test', CURRENT_TIMESTAMP, 'test');
INSERT INTO address VALUE(null, '828s 5740e', '838s 74023e', 1, '84004', '801-885-4158',  CURDATE(), 'test', CURRENT_TIMESTAMP, 'test');
INSERT INTO address VALUE(null, '828s 7540e', '884s 7403e', 1, '84005', '801-885-4158',  CURDATE(), 'test', CURRENT_TIMESTAMP, 'test');
INSERT INTO address VALUE(null, '838s 7540e', '885s 7404e', 1, '84006', '801-885-4158',  CURDATE(), 'test', CURRENT_TIMESTAMP, 'test');
INSERT INTO address VALUE(null, '848s 7450e', '886s 74320e', 1, '84007', '801-885-4158',  CURDATE(), 'test', CURRENT_TIMESTAMP, 'test');

CREATE TABLE customer
(
  customerId   INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  customerName VARCHAR(45),
  addressId    INT(10),
  active       TINYINT(1),
  createDate   DATETIME,
  createdBy    VARCHAR(40),
  lastUpdate   TIMESTAMP,
  lastUpdateBy VARCHAR(40),
  FOREIGN KEY (addressId) REFERENCES address (addressId) ON DELETE SET NULL ON UPDATE CASCADE
);

INSERT INTO customer VALUE(NULL, 'Test', 1, 1, CURDATE(), 'test', CURRENT_TIMESTAMP, 'test');
INSERT INTO customer VALUE(NULL, 'Test2', 1, 1, CURDATE(), 'test', CURRENT_TIMESTAMP, 'test');
INSERT INTO customer VALUE(NULL, 'Test3', 1, 1, CURDATE(), 'test', CURRENT_TIMESTAMP, 'test');

#  SELECT * from customer;
# delete from customer where customerId = 1;

CREATE TABLE appointment
(
  appointmentId INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  customerId    INT(10),
  userId        INT,
  title         VARCHAR(255),
  description   TEXT,
  location      TEXT,
  contact       TEXT,
  type          TEXT,
  url           VARCHAR(255),
  start         DATETIME,
  end           DATETIME,
  createDate    DATETIME,
  createdBy     VARCHAR(40),
  lastUpdate    TIMESTAMP,
  lastUpdateBy  VARCHAR(40),
  FOREIGN KEY (customerId) REFERENCES customer (customerId) ON DELETE SET NULL ON UPDATE CASCADE,
  FOREIGN KEY (userId) references user (userId) ON DELETE SET NULL ON UPDATE CASCADE
);

INSERT INTO appointment VALUE(NULL, 1, 1, 'test wants something', 'testing', 'testing location', 'testing contract', 'testing type', 'http://youtube.com', CURDATE(), CURDATE(), CURDATE(), 'test', CURRENT_TIMESTAMP, 'test');


