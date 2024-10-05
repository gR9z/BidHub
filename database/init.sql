IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'AuctionApp')
BEGIN
    CREATE DATABASE AuctionApp;
END
GO

USE AuctionApp;
Go

CREATE TABLE CATEGORIES (
    categoryId   INTEGER IDENTITY(1,1) NOT NULL,
    [label]        VARCHAR(255) NOT NULL
);
ALTER TABLE CATEGORIES ADD CONSTRAINT category_pk PRIMARY KEY (categoryId);


CREATE TABLE AUCTIONS (
    auctionId INTEGER IDENTITY(1,1) NOT NULL,
    userId   INTEGER NOT NULL,
    itemId       INTEGER NOT NULL,
    auctionDate     DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
	bidAmount  INTEGER NOT NULL
);
ALTER TABLE AUCTIONS ADD CONSTRAINT auction_pk PRIMARY KEY (auctionId);

CREATE TABLE WITHDRAWALS (
	itemId         INTEGER NOT NULL,
    street              VARCHAR(30) NOT NULL,
    zipCode      VARCHAR(15) NOT NULL,
    city            VARCHAR(30) NOT NULL
);

ALTER TABLE WITHDRAWALS ADD CONSTRAINT withdrawals_pk PRIMARY KEY (itemId);


CREATE TABLE USERS (
    userId   INTEGER IDENTITY(1,1) NOT NULL,
    username           VARCHAR(30) NOT NULL,
    lastName              VARCHAR(30) NOT NULL,
    firstName           VARCHAR(30) NOT NULL,
    email            VARCHAR(255) NOT NULL,
    phone        VARCHAR(50),
    street              VARCHAR(255) NOT NULL,
    zipCode      VARCHAR(10) NOT NULL,
    city            VARCHAR(255) NOT NULL,
    [password]     VARCHAR(255) NOT NULL,
    credit           INTEGER NOT NULL,
    isAdmin   bit NOT NULL
);
ALTER TABLE USERS ADD CONSTRAINT user_pk PRIMARY KEY (userId);
ALTER TABLE USERS ADD CONSTRAINT user_username_uc UNIQUE (username), CONSTRAINT user_email_uc UNIQUE (email);

CREATE TABLE ITEMS (
    itemId                    INTEGER IDENTITY(1,1) NOT NULL,
    itemName                   VARCHAR(30) NOT NULL,
    [description]                   VARCHAR(255) NOT NULL,
	auctionStartingDate           DATETIME NOT NULL,
    auctionEndingDate             DATETIME NOT NULL,
    startingPrice                  INTEGER,
    sellingPrice                    INTEGER,
    imageUrl VARCHAR(255) NOT NULL,
    userId                INTEGER NOT NULL,
    categoryId                  INTEGER NOT NULL
);
ALTER TABLE ITEMS ADD CONSTRAINT item_pk PRIMARY KEY (itemId);

ALTER TABLE ITEMS
ADD CONSTRAINT auctions_user_fk FOREIGN KEY (userId) REFERENCES USERS (userId)
ON DELETE NO ACTION 
ON UPDATE no action; 

ALTER TABLE AUCTIONS
ADD CONSTRAINT auctions_items_fk FOREIGN KEY (itemId) REFERENCES ITEMS (itemId)
ON DELETE NO ACTION 
ON UPDATE no action; 

ALTER TABLE WITHDRAWALS
ADD CONSTRAINT withdrawals_items_fk
FOREIGN KEY (itemId)
REFERENCES ITEMS (itemId)
ON DELETE CASCADE
ON UPDATE NO ACTION;

ALTER TABLE ITEMS
ADD CONSTRAINT items_categories_fk FOREIGN KEY (categoryId) REFERENCES CATEGORIES (categoryId)
ON DELETE NO ACTION 
ON UPDATE no action; 

ALTER TABLE ITEMS
ADD CONSTRAINT sales_user_fk FOREIGN KEY (userId) REFERENCES users (userId)
ON DELETE NO ACTION 
ON UPDATE no action;