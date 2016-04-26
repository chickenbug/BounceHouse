CREATE DATABASE IF NOT EXISTS proj2016;
USE proj2016;

DROP TABLE IF EXISTS Administrator;
DROP TABLE IF EXISTS Auction;
DROP TABLE IF EXISTS AutoBid;
DROP TABLE IF EXISTS Bid;
DROP TABLE IF EXISTS Card;
DROP TABLE IF EXISTS CustomerRep;
DROP TABLE IF EXISTS Email;
DROP TABLE IF EXISTS Item;
DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS UserQuestion;
DROP TABLE IF EXISTS Wishlist;

CREATE TABLE User(
	Address varchar(255) DEFAULT NULL,
	BirthDate date DEFAULT NULL,
	City varchar(255) DEFAULT NULL,
	Country varchar(255) DEFAULT NULL, /* Full country name, e.g. Canada instead of CA */
	Email varchar(255) DEFAULT NULL,
	FirstName varchar(255) DEFAULT NULL,
	LastName varchar(255) DEFAULT NULL,
	Password varchar(255) NOT NULL,
	Phone varchar(12) DEFAULT NULL,
	PostCode varchar(9) DEFAULT NULL,
	Role varchar(255) DEFAULT NULL, /* EndUser for reg users, Admin for admins, Rep for customer reps*/
	State varchar(255) DEFAULT NULL, /* Full state name, e.g. New Jersey instead of NJ */
	UserID int(9) NOT NULL AUTO_INCREMENT,
	Username varchar(255) NOT NULL,
	PRIMARY KEY(UserID)
);

INSERT INTO User VALUES('610 Taylor Rd', 7-7-1994, 'Piscataway', 'USA', 'tgoetjen@gmail.com', 'Tim','Goetjen', 'password', '5555555', 
	'08901','Admin','NJ',1,'tgoetjen');

CREATE TABLE Item(
	Bounciness int(2) DEFAULT 0,
	Category varchar(255) DEFAULT NULL,
	Color varchar(255) DEFAULT NULL,
	Description varchar(3000) DEFAULT NULL,
	Image varchar(255) DEFAULT NULL, /* This should be a URL to the image's location */
	ItemID int(9) NOT NULL AUTO_INCREMENT,
	Size varchar(2) DEFAULT NULL, /* Should be XS, S, M, L, XL */
	SubCategory varchar(255) DEFAULT NULL,
	PRIMARY KEY(ItemID)
);

CREATE TABLE Auction(
	AuctionID int(9) NOT NULL AUTO_INCREMENT,
	CloseDate datetime DEFAULT NULL,
	Completed int(1) DEFAULT 0,
	ItemID int(9) NOT NULL,
	MinBid float(7) DEFAULT 0,
	UserID int(9) NOT NULL,
	WinBid float(7) DEFAULT NULL,
	FOREIGN KEY (ItemID) REFERENCES Item (ItemID) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (UserID) REFERENCES User (UserID) ON UPDATE CASCADE ON DELETE CASCADE,
	PRIMARY KEY(AuctionID)
);

CREATE TABLE AutoBid(
	AuctionID int(9) NOT NULL,
	AutoBidID int(9) NOT NULL AUTO_INCREMENT,
	MaxBid float(7) DEFAULT 0,
	FOREIGN KEY (AuctionID) REFERENCES Auction (AuctionID) ON UPDATE CASCADE ON DELETE CASCADE,
	PRIMARY KEY(AutoBidID)
);

CREATE TABLE Bid(
	Amount float(7) DEFAULT 0,
	AuctionID int(9) NOT NULL,
	BidID int(9) NOT NULL AUTO_INCREMENT,
	BidTime datetime DEFAULT NULL,
	UserID int(9) NOT NULL,
	FOREIGN KEY (AuctionID) REFERENCES Auction (AuctionID) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (UserID) REFERENCES User (UserID) ON UPDATE CASCADE ON DELETE CASCADE,
	PRIMARY KEY(BidID)
);

CREATE TABLE Card(
	CardNumber bigint(16) NOT NULL,
	ExpirationDate date DEFAULT NULL,
	Name varchar(255) DEFAULT NULL,
	Type varchar(7) DEFAULT NULL,
	UserID int(9) NOT NULL,
	FOREIGN KEY (UserID) REFERENCES User (UserID) ON UPDATE CASCADE ON DELETE CASCADE,
	PRIMARY KEY(CardNumber)
);

/* 
	NULLS are allowed in this table only so that a deletion from "User" won't break a record -
	we do not want to cascade a delete (we lose records of our communication) or take no action 
	(user should be able to delete his or her account from our database at any point). The answer 
	to this is setting NULL instead. We keep our info, while the user's account is deleted.
*/ 
CREATE TABLE Email(
	Content varchar(8000) DEFAULT NULL,
	EmailID int(9) NOT NULL AUTO_INCREMENT,
	Recipient varchar(255) NOT NULL,
	RecipientID int(9) DEFAULT NULL, 
	Sender varchar(255) NOT NULL,
	SenderID int(9) DEFAULT NULL,
	SendTime datetime DEFAULT NULL,
	FOREIGN KEY (RecipientID) REFERENCES User (UserID) ON UPDATE CASCADE ON DELETE SET NULL,
	FOREIGN KEY (SenderID) REFERENCES User (UserID) ON UPDATE CASCADE ON DELETE SET NULL,
	PRIMARY KEY (EmailID)
);

/*
	See the comment on the email table for why it's better to set NULL on deletion of a user's
	account from the "User" table. The same principle applies to the customer rep - in short,
	we lose a record of communication between us and the user if we cascade a delete of the rep
	or the user, and if we specify NO ACTION, the database will reject the deletes, which is
	also unacceptable.
*/
CREATE TABLE UserQuestion(
	Answer varchar(8000) DEFAULT NULL,
	QuestionID int(9) NOT NULL AUTO_INCREMENT,
	QText varchar(8000) DEFAULT NULL,
	RepID int(9) DEFAULT NULL,
	UserID int(9) DEFAULT NULL,
	FOREIGN KEY (RepID) REFERENCES CustomerRep (RepID) ON UPDATE CASCADE ON DELETE SET NULL,
	FOREIGN KEY (UserID) REFERENCES User (UserID) ON UPDATE CASCADE ON DELETE SET NULL,
	PRIMARY KEY (QuestionID)
);

CREATE TABLE Wishlist(
	Bounciness int(2) DEFAULT 0, /* On a scale of {1, 2, ... 10} */
	Category varchar(255) DEFAULT NULL,
	Color varchar(255) DEFAULT NULL,
	ListID int(9) NOT NULL AUTO_INCREMENT,
	Size varchar(2) DEFAULT NULL,
	SubCategory varchar(255) DEFAULT NULL,
	UserID int(9) NOT NULL,
	FOREIGN KEY (UserID) REFERENCES User (UserID) ON UPDATE CASCADE ON DELETE CASCADE,
	PRIMARY KEY(ListID)
);