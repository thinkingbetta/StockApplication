# StockApplication
This is Stock Application created by Elisabetta Spagnuolo for the Assignment Week of the Extended Java JumpStart. 

## Info about creation of tables in database
The queries to create the tables in the database are in the folder `resources/` in the file ["DBTables"](https://github.com/thinkingbetta/StockApplication/blob/main/resources/DBTables).

The entity–relationship diagram is also provided [here](https://github.com/thinkingbetta/StockApplication/blob/main/resources/EER%20diagram.svg).

## Requirements
 - Java
 - MySQL

## CSV Data
CSV data for input and output are stored in `data/` folder.

## Implemented Features
1.	The __import__ command allows importing the file “STOCK_DATA.csv” to the application.
2.	The __delete__ command allows to delete all data from the database.
3.	The __search__ command helps the user finding the id for a company.
4.	The __show__ command shows the last ten prices for a stock with a specific id. 
5.	The __add__ command allows adding a new price for a specific time.
6.	The __max__ command shows the highest price a stock ever had.
7.	The __low__ command shows the lowest price a stock ever had.
8.	The __gap__ command shows the difference between the highest and the lowest price ever recorded.
9.	The __updateindustry__ command updates a stocks industry.
10.	The __industries__ command lists all industries in the database with its ID and the number of stocks assigned to this industry.
11.	_BONUS:_ The __export__ command exports all data to a CSV file which could be imported again.

## Usage

The Java application assumes that the MySQL database is up and running locally at `localhost:3306`
