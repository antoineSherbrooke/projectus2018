0. Inspired from
------------------------------------------------------------
Our scrips are adapted form the script given by postgres (https://wiki.postgresql.org/wiki/Automated_Backup_on_Linux).
Our scripts are customized to :
    -Backup only selected databases
    -Save the structure and the data of the databases
    -Can save all the role used for the database

Note the backup us donne by the script pg_backup.sh.
The command pg_backup_rotated.sh allows to make and discard (old) backups, this command calls pg_backup.sh.


I. File list
------------------------------------------------------------
*In the "scripts" folder
	-pg_backup.config		//Contain all the config for the 2 following scripts
	-pg_backup.sh			//Backup script will go through each database and save a gzipped and/or a custom format copy of the backup into a date-based directory
	-pg_backup_rotated.sh	//Same as above except it will delete expired backups based on the configuration
*In the "home repository" of the user
	-.pgpass				//Contain all the password needed, for postgres, to acces to the data base
*In the server config
    -crontab                //Crontab needed to run scripts automatically


II. pg_backup.config
------------------------------------------------------------

#### SETTINGS FOR ALL BACKUPS ####

-HOSTNAME				//Address of the data base.
-USER_NAME				//Username used to connect to the database. If blank, the -default is "postgres".
-BACKUP_DIR				//Directory where the backups will be saved. It will be created if it doesn't exist.
-SCHEMA_AND_DATA_LIST	//List of strings to match against in database name, separated by space or comma, for wich one wish to keep the backups. (e.g. "system_log" will match "dev_system_log_2010-01")
-ENABLE_CUSTOM_BACKUPS	//Produce a custom-format backup if set to "yes"
-ENABLE_ROLE_BACKUP		//Produce gzipped sql file containing all the role in the database if set to "yes"

#### SETTINGS FOR ROTATED BACKUPS ONLY ####

-DAY_OF_WEEK_TO_KEEP	//Which day to take the weekly backup from (1-7 = Monday-Sunday)
-DAYS_TO_KEEP			//Number of days to keep daily backups
-WEEKS_TO_KEEP			//How many weeks to keep weekly backups


III. pg_backup.sh
to run	./pg_backup.sh or sh pg_backup.sh
------------------------------------------------------------
Shell scrpit that allow the user to do a backup of a database. It can take an argument. This will be use in the naming of the repository of the backup.

#### LOAD CONFIG #### 
Load of the config file explained above.

#### INITIALISE DEFAULTS ####
Put the default hostname as "localhost" and the default username as "postgres" if empty on the config file.

#### START THE BACKUPS ####
Create a folder name using the date of the run, (AAAA-MM-DD-HH:MM-$SUFFIX) in the directory defined by the config file.

#### ROLES BACKUPS ####
Create a backup of the different roles into a gzip file.

#### SCHEMA_AND_DATA BACKUPS ####
Create a complete backup of the database that have name that match the list of String written in the config file (SCHEMA_AND_DATA_LIST), into a gzip file.


IV. pg_backup_rotated.sh
to run	./pg_backup_rotated.sh or sh pg_backup_rotated.sh
------------------------------------------------------------

#### LOAD CONFIG #### 
Load of the config file explained above.

#### MONTHLY BACKUPS/WEEKLY BACKUPS/DAILY BACKUPS #### 
Will delete all the backup files older than the variable define in the config file.
Then the script execute pg_backup.sh with an argument. This argument depends on the date. If it's the day of the monthly backup, the argument is "monthly", for the weekly backup, it's "weekly". Otherwise, the default argument is "daily". 


V. .pgpass
------------------------------------------------------------

Contain all the password needed for postgres with the following format:
hostname:port:database:username:password


V. crontab
------------------------------------------------------------
#### PATH ####
PATH=/bin:/sbin:/usr/bin:/usr/sbin:/Library/PostgreSQL/9.3/bin
This is needed to run the script. Otherwise, the script will fail where command like "psql" are run.

#### CRON ####
0 3 * * * /Users/tictac/Sites/projectus/pg_backup_rotated.sh
This will run the script pg_backup_rotated.sh every day at 3.00 am

#### CRON TEST ####
#*/5 * * * * /Users/tictac/Sites/projectus/pg_backup_rotated.sh
Is in comment. Used only during period of test. Run the script pg_backup_rotated.sh every 5 minutes.


VI. How to restore the backup
------------------------------------------------------------
gunzip -c filename.gz | psql -U username -d databasename