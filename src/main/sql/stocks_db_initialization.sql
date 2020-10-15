/**
  Create the stocks database
 */

DROP DATABASE stocks;
CREATE DATABASE stocks;

/* Drop and create stocks.person table */

DROP TABLE IF EXISTS stocks.person CASCADE;
CREATE TABLE stocks.person
(
    id        INT          NOT NULL AUTO_INCREMENT,
    user_name VARCHAR(256) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS stocks.stock_symbol CASCADE;
CREATE TABLE stocks.stock_symbol
(
    id     INT        NOT NULL AUTO_INCREMENT,
    symbol VARCHAR(4) NOT NULL,
    PRIMARY KEY (id)
);

/* Drop and create stocks.stock_symbol table */

DROP TABLE IF EXISTS stocks.stock_symbol CASCADE;
CREATE TABLE stocks.stock_symbol
(
    id     INT        NOT NULL AUTO_INCREMENT,
    symbol VARCHAR(4) NOT NULL,
    PRIMARY KEY (id)
);

/* Drop and create stocks.quote table */

DROP TABLE IF EXISTS stocks.quote CASCADE;
CREATE TABLE stocks.quote
(
    id        INT      NOT NULL AUTO_INCREMENT,
    symbol_id INT      NOT NULL,
    time      DATETIME NOT NULL,
    price     DECIMAL  NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (symbol_id) REFERENCES STOCK_SYMBOL (id)
);

/* Drop and create stocks.person_stocks table */

DROP TABLE IF EXISTS stocks.person_stocks CASCADE;
CREATE TABLE stocks.person_stocks
(
    id        INT NOT NULL AUTO_INCREMENT,
    person_id INT NOT NULL,
    symbol_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (person_id) REFERENCES PERSON (id),
    FOREIGN KEY (symbol_id) REFERENCES STOCK_SYMBOL (id)
);


INSERT INTO stocks.stock_symbol (symbol) VALUES ('APPL');
INSERT INTO stocks.quote (symbol_id, time, price) VALUES (LAST_INSERT_ID(), '2000-01-01 00:00:01', '118.27');

INSERT INTO stocks.stock_symbol (symbol) VALUES ('GOOG');
SET @last_id_in_stock_symbol = LAST_INSERT_ID();
INSERT INTO stocks.quote (symbol_id, time, price) VALUES (@last_id_in_stock_symbol, '2004-08-19 00:00:01', '85.00');
INSERT INTO stocks.quote (symbol_id, time, price) VALUES (@last_id_in_stock_symbol, '2015-02-03 00:00:01', '527.35');

INSERT INTO stocks.stock_symbol (symbol) VALUES ('AMZN');
SET @last_id_in_stock_symbol = LAST_INSERT_ID();
INSERT INTO stocks.quote (symbol_id, time, price) VALUES (@last_id_in_stock_symbol, '2015-02-10 00:00:01', '363.21');
INSERT INTO stocks.quote (symbol_id, time, price) VALUES (@last_id_in_stock_symbol, '2015-02-10 00:02:01', '250.21');
INSERT INTO stocks.quote (symbol_id, time, price) VALUES (@last_id_in_stock_symbol, '2015-02-10 00:03:01', '251.21');
INSERT INTO stocks.quote (symbol_id, time, price) VALUES (@last_id_in_stock_symbol, '2015-02-10 00:04:01', '253.21');


INSERT INTO stocks.person (user_name) VALUES ('sam');
SET @last_id_in_person = LAST_INSERT_ID();
INSERT INTO stocks.person_stocks (person_id, symbol_id) VALUES (@last_id_in_person, @last_id_in_stock_symbol);