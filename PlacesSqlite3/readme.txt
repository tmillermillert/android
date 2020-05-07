Author: Marcus Miller
Version: April, 2020
Project: Ser423 Mobile Computing Database Example

Following through this readme.txt file and setting up the database requires
a command line version of sqlite3. On Mac OSX, you can install sqlite3 using
MacPorts with the following command:
 sudo port install sqlite3

Versions are available for Windows, linux and Mac OSX at:
   http://www.sqlite.org/download.html

SQLite is an in process (light-weight) relational database manager.

Instructions for using sqlite3
***************************************************
I. Creating and Populating the Database Using sqlite3 from the terminal

   1. Create a new database placesdb
      sqlite3 coursedb.db

   2. To populate the database from the file initplacesdb.sql
       sqlite>.read initplacesdb.sql
      The first time you execute this it will give errors as sqlite3
      tries to drop tables that don't exist yet.

   3. To Create a backup of the database int the file backupplacesdb.db
       sqlite>.backup main backupplacesdb.db
  
   4. To recover the database from the backup created above
       sqlite>.restore main backupplacesdb.db

   5. To query or modify the database, use any of the queries below from
      the sqlite> prompt. Be sure to end the sql statement with a semi-colon

   6. To exit sqlite interpreter
      sqlite>.quit


