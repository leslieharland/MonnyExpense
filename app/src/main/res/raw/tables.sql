CREATE TABLE dateGroup(
id integer primary key,
dateId integer not null,
dateString text collate nocase not null,
expenseId integer not null);


CREATE TABLE category(
categoryId integer primary key,
categoryName varchar(50) not null,
categoryIcon varchar(50) not null);

CREATE TABLE expense(
expenseId integer primary key,
categoryId integer not null,
description varchar(100),
amount numeric not null,
image varchar(2000),
accountId integer not null);


CREATE TABLE budget(
budgetId integer primary key,
accountId integer not null,
amount numeric not null
);

CREATE TABLE accountDatabase(
accountId integer primary key,
accountName varchar(20) not null,
image varchar(50) not null,
currencyId integer not null,
defaultAccount bit not null
);

CREATE TABLE currency(
currencyId integer primary key,
currency varchar(20) not null,
currencySymbol varchar(10) not null,
county varchar(50) not null,
abv char(3) not null
);

CREATE TABLE recurring(
recurringId integer primary key,
accountId integer not null,
categoryId integer not null,
amount numeric not null,
startDate  text collate nocase not null,
endDate  text collate nocase not null,
recurringMode integer not null,
description varchar(100),
image varchar(2000));