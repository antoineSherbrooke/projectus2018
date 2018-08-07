#!/bin/bash
#find . -name "RUNNING_PID" -exec rm -r "{}" \;

PROJECTUS_EMPTY="/Sites"
PROJECTUS=$PROJECTUS_EMPTY"/projectus"
PROD_SERVER="exitlab@exitlabserver.gel.usherbrooke.ca"
STAGING_SERVER="tictac@tictacserver.gel.usherbrooke.ca"
POSTGRES_DATA="/Library/PostgreSQL/9.4/data/"
POSTGRES_BIN="/Library/PostgreSQL/9.4/bin/postmaster"

possibleActions='
- start\n
- stop\n
- restart\n
- append\n
- choose\n
- status\n
- restoredb\n
'

get_processus="
    ps -ef | egrep projectus | egrep -v egrep | tr ' ' '\n' | head -n 4 | tail -n 1
"

stop_projectus="
    if test \$($get_processus) != ''
    then
        launchctl stop projectus
        if test \$($get_processus) = ''
        then
            echo 'STOPPING OF PROJECTUS WITH SUCCESS'
        else
            kill -9 \$($get_processus)
            echo 'STOPPING OF PROJECTUS WITH SUCCESS'
        fi
    else
        echo 'Projectus is not active'
    fi
"

start_projectus="
    if [ \"\$($get_processus)\" != \"\" ]; then
        echo 'Projectus is already running'
        exit 0
    fi
    cd Sites/projectus;
    find . -name 'RUNNING_PID' -exec rm -r \"{}\" \;
    launchctl start projectus
    echo 'The start can be long'
    sleep 2
    if [ \"\$($get_processus)\" == \"\" ]; then
        echo 'ERROR WHEN STARTING PROJECTUS'
    else
        echo 'STARTING OF PROJECTUS WITH SUCCESS'
    fi
"

restart_postgres="
    sudo -u postgres killall postgres;
    sudo -u postgres $POSTGRES_BIN -D $POSTGRES_DATA;
"

empty_directory="
    if [ -d "$PROJECTUS" ]; then
        mkdir './projectus'
    fi
"

if test $# = 0
then
    echo "Usage : "$0" action [target]"
    echo -e "List of action : "$possibleActions
    echo "target is staging by default, it can be :"
    echo " - staging"
    echo " - prod"
    echo " - username@hostname"
    exit
fi

echo "SSH password :"
read -s password

if test $# = 1
then
    serverName=$STAGING_SERVER
elif test $# = 2
then
    if test $2 = "prod"
    then
        serverName=$PROD_SERVER
    elif test $2 = "staging"
    then
        serverName=$STAGING_SERVER
    else
        serverName=$2
    fi
fi

if test $1 = "start"
then
    echo "########## PROJECTUS STARTING  ##########\n"
    sshpass -p $password ssh $serverName "
        $start_projectus
        "
    exit 0
fi

if test $1 = "status"
then
    sshpass -p $password ssh $serverName "
        if [ \"\$($get_processus)\" == \"\" ]; then
            echo 'Projectus is not running'
            exit 0
        else
            echo 'Projectus is running'
        fi
    "
    exit 0
fi

if test $1 = "restart"
then
    echo "########## PROJECTUS RESTARTING  ##########\n"
    sshpass -p $password ssh $serverName "
        $stop_projectus
        $start_projectus
        "
    exit 0
fi

if test $1 = "stop"
then
    echo "########## PROJECTUS STOPING  ##########\n"
    sshpass -p $password ssh $serverName "
        $stop_projectus
        "
    exit 0
fi

if test $1 = "append"
then
    sshpass -p $password ssh $serverName $empty_directory

    #Edit version of production
    dayVersion=$(date +%Y.%m.%d)
    echo $dayVersion > "./public/version.txt"

    day=$(date +%y-%m-%d-%H-%M)

    echo "########## UPDATE OF PROJECTUS FROM SVN ##########\n"
    svn update

    echo "########## COMPILE SOURCE ##########"
    echo "Loading ..."
    dist=$(activator dist | tail -n 1 | egrep -i "SUCCESS")
    if [ -s "$dist" ]
    then
        echo "Error : Compile error"
        exit 0
    else
        echo "Success"
    fi

    echo "########## SEND ZIP TO SERVER ##########"
    sshpass -p $password scp "./target/universal/projectus-1.0-SNAPSHOT.zip" $serverName:$PROJECTUS/release-$day.zip
    echo "SUCCESS"

    echo "########## BACKUP CURRENT DATABASE ###########"
    sshpass -p $password ssh $serverName "
        name=backupdb-\$(cat "CURRENT").gz
        cd $POSTGRES_DATA;"
        $stop_projectus
        "sudo -u postgres pg_dump postgres | gzip > $name;"
        echo \"Backup in "+$POSTGRES_DATA+$name"\";
    "

    echo "########## RESTART PROJECTUS ##########"
    sshpass -p $password ssh $serverName "
        cd $PROJECTUS;

        launchctl stop projectus;

        if ! unzip -o './release-$day.zip'
        then
            echo Error when unzip file
            exit 0
        fi
        mv projectus-1.0-SNAPSHOT 'release-$day'
        echo 'release-$day' > "CURRENT"
        rm './release-$day.zip'

        launchctl start projectus

        echo 'Success: ALL IS DONE';
        echo 'Start the new release with $0 start';

        "
    exit
fi

if test $1 = "choose"
then
    echo "########## CONNECTING TO SERVER ##########\n"
    sshpass -p $password ssh $serverName "
        cd $PROJECTUS;
        $stop_projectus
        i=0
        for f in \$(ls -d release*)
        do
            echo \$i') '\$f
            ((i=i+1))
        done
        if test \$i = 0
        then
             echo 'Error : You must add a release first'
             exit 0
        fi
        read number
        dir=(\$(ls -d release*))
        dir=\${dir[\$number]}
        if [ -z \${dir// } ]
        then
            echo 'Error when selecting folder'
            exit
        fi
        echo 'You have choose :'
        echo \$dir
        echo 'Start projectus with $0 start'
        echo "\$dir" > "CURRENT"
        exit 0
        "
    exit
fi

if test $1 = "restoredb"
then
    sshpass -p $password ssh $serverName "
        name=backupdb-\$(cat "CURRENT").gz
        cd $POSTGRES_DATA;
        echo 'stopping projectus...';
        $stop_projectus
        echo 'restarting postgres...';
        $restart_postgres
        echo 'This next step will OVERWRITE current database with backup !';
        echo '   (on "$serverName")';
        read -p 'Are you sure?(y/n) ' -n 1 -r;
        if [[ \$REPLY =~ ^[Yy]\$ ]]
        then
            sudo -u postgres dropdb postgres;
            sudo -u postgres createdb postgres;
            gunzip -c \$name | sudo -u postgres psql postgres;
        fi
        $start_projectus"
    exit
fi


echo "Error : The argument $1 is not good\n"
echo -e "List of argument :\n"$possibleActions
exit


#
