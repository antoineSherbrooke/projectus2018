#!/bin/bash
 
###########################
####### LOAD CONFIG #######
###########################

. /Users/tictac/Sites/projectus/pg_backup.config


###########################
### INITIALISE DEFAULTS ###
###########################
 
if [ ! $HOSTNAME ]; then
	HOSTNAME="localhost"
fi;
 
if [ ! $USER_NAME ]; then
	USER_NAME="postgres"
fi;
 
 
###########################
#### START THE BACKUPS ####
###########################
SUFFIX=$1
FINAL_BACKUP_DIR=$BACKUP_DIR"`date +\%Y-\%m-\%d-\%H:%M`$SUFFIX/"
 
echo "Making backup directory in $FINAL_BACKUP_DIR"
 
if ! mkdir -p $FINAL_BACKUP_DIR; then
	echo "Cannot create backup directory in $FINAL_BACKUP_DIR. Abort script!" 1>&2
	exit 1;
fi;
 
 
####################
### ROLES BACKUP ###
####################
 
if [ $ENABLE_ROLE_BACKUP = "yes" ];
then
	echo "--------------------------------------------"
    echo "Roles backup"
 
        if ! pg_dumpall -g -h "$HOSTNAME" -U "$USER_NAME" | gzip > $FINAL_BACKUP_DIR"roles".sql.gz.in_progress; then
                echo "[!!ERROR!!] Failed to produce roles backup" 1>&2
        else
                mv $FINAL_BACKUP_DIR"roles".sql.gz.in_progress $FINAL_BACKUP_DIR"roles".sql.gz
        fi
    echo "End of role backup"
fi
 
 
##############################
### SCHEMA_AND_DATA BACKUP ###
##############################

for SCHEMA_AND_DATA_DB in ${SCHEMA_AND_DATA_LIST}
do
	SCHEMA_AND_DATA_CLAUSE="$SCHEMA_AND_DATA_CLAUSE or datname ~ '$SCHEMA_AND_DATA_DB'"
done
 
SCHEMA_AND_DATA_QUERY="select datname from pg_database where false $SCHEMA_AND_DATA_CLAUSE order by datname;"
 
echo "Performing schema and data backups"
echo "--------------------------------------------"
 
SCHEMA_AND_DATA_DB_LIST=`psql -h "$HOSTNAME" -U "$USER_NAME" -At -c "$SCHEMA_AND_DATA_QUERY" postgres`
 
echo "The following databases were matched for schema-only backup:\n${SCHEMA_AND_DATA_DB_LIST}"
 
for DATABASE in $SCHEMA_AND_DATA_DB_LIST
do
	echo "Backup of $DATABASE"
 
	if ! pg_dump -Fp -h "$HOSTNAME" -U "$USER_NAME" "$DATABASE" | gzip > $FINAL_BACKUP_DIR"$DATABASE".sql.gz.in_progress; then
		echo "[!!ERROR!!] Failed to backup database of $DATABASE" 1>&2
	else
		mv $FINAL_BACKUP_DIR"$DATABASE".sql.gz.in_progress $FINAL_BACKUP_DIR"$DATABASE".sql.gz
	fi
done
echo "End of schema and data backup"

echo "End of the backup ! \n"

