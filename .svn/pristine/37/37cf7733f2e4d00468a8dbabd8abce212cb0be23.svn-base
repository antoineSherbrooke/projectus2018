#!/bin/bash


#-- Constants and arguments

possibleActions='
- start\n
- stop\n
- restart\n
- append\n
- choose\n
- status\n
'

PROJECTUS_EMPTY="/Sites"
PROJECTUS=$PROJECTUS_EMPTY"/projectus"
PROD_SERVER="exitlab@exitlabserver.gel.usherbrooke.ca"
PROD_USER="exitLab"     #careful, it is with a L uppercase
STAGING_SERVER="tictac@tictacserver.gel.usherbrooke.ca"
STAGING_USER="tictac"

if test $# = 0
then
    echo "Usage : "$0" action [target]"
    echo -e "List of action : "$possibleActions
    echo "target is staging by default, it can be :"
    echo " - staging"
    echo " - production"
    echo " - username@hostname"
    exit
fi

if test $# = 1
then
    serverName=$STAGING_SERVER
    user=$STAGING_USER
elif test $# = 2
then
    if [ $2 = "prod" -o $2 = "production" ]
    then
        serverName=$PROD_SERVER
        user=$PROD_USER
    elif [ $2 = "stag" -o $2 = "staging" ]
    then
        serverName=$STAGING_SERVER
        user=$STAGING_USER
    else
        serverName=$2
        user=$(echo "$serverName" | cut -d '@' -f 1)
    fi
fi

DAEMON="
<?xml version='1.0' encoding='UTF-8'?>
<!DOCTYPE plist PUBLIC '-//Apple//DTD PLIST 1.0//EN' 'http://www.apple.com/DTDs/PropertyList-1.0.dtd'>
<plist version='1.0'>
  <dict>
    <key>Label</key>
    <string>projectus</string>
    <key>Program</key>
    <string>/Users/$user/Sites/projectus/projectus.sh</string>
    <key>RunAtLoad</key>
    <true/>
  </dict>
</plist>
"


#-- "Functions"

get_processus_pid="
    ps -eo pid,args | egrep /usr/bin/java | egrep projectus | egrep -v egrep | sed 's/^[ ]*//' | cut -d ' ' -f 1
"

stop_projectus="
    if test \$($get_processus_pid) != ''
    then
        launchctl stop projectus
        if test \$($get_processus_pid) = ''
        then
            echo '✓ STOPPING PROJECTUS WITH SUCCESS'
        else
            kill -9 \$($get_processus_pid)
            echo '✓ STOPPING PROJECTUS WITH SUCCESS'
        fi
    else
        echo 'Projectus is not active'
    fi
"

start_projectus="
    pwd
    if [ \"\$($get_processus_pid)\" != \"\" ]; then
        echo 'Projectus is already running'
        exit 0
    fi
    rm \$(more \"CURRENT\")'/'RUNNING_PID 2> /dev/null
    launchctl start projectus
    echo 'The start can be long, wait 10+ seconds'
    sleep 12
    echo \$($get_processus_pid)
    if [ \"\$($get_processus_pid)\" != \"\" ]; then
        echo '✓ STARTING PROJECTUS WITH SUCCESS'
    else
        echo 'ERROR WHEN STARTING PROJECTUS'
    fi
"

empty_directory="
    cd './'$PROJECTUS_EMPTY;
    mkdir -p './projectus'

    cd './projectus';
    chmod 777 ./projectus.sh

    echo \"$DAEMON\" > '/Library/LaunchDaemons/projectus.plist'
    launchctl load /Library/LaunchDaemons/projectus.plist
"


#-- Actions

echo -n "SSH password for $serverName: "
read -s password
echo ""

if test $1 = "start"
then
    sshpass -p $password ssh $serverName "
        if [ \"\$($get_processus_pid)\" != \"\" ]; then
            echo 'Projectus is already running'
            exit 0
        fi"
    echo "#### PROJECTUS STARTING  ####"
    sshpass -p $password ssh $serverName "
        cd './'$PROJECTUS;
        $start_projectus
        "
    exit 0
fi

if test $1 = "status"
then
    sshpass -p $password ssh $serverName "
        if [ \"\$($get_processus_pid)\" == \"\" ]; then
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
    echo "#### PROJECTUS RESTARTING  ####"
    sshpass -p $password ssh $serverName "
        $stop_projectus
        $start_projectus
        "
    exit 0
fi

if test $1 = "stop"
then
    echo "#### PROJECTUS STOPING  ####"
    sshpass -p $password ssh $serverName "
        $stop_projectus
        "
    exit 0
fi

if test $1 = "append"
then
    totalTimeStart=$(date +%s)
    sshpass -p $password ssh $serverName "
        cd './'$PROJECTUS;
        mv ./projectus.sh ./projectus.sh.bak;
    "
    sshpass -p $password scp ./projectus.sh "$serverName:./$PROJECTUS"
    sshpass -p $password ssh $serverName "
        $empty_directory
    "
    cd ..;

    day=$(date +%y-%m-%d-%H-%M)

    echo "#### DATABASE BACKUP ####"
    if [ "$serverName" != "$STAGING_SERVER" ]; then
        echo -n "ssh password for $STAGING_SERVER (to access the server with the databases): "
        read -s password2
        echo ""
    else
        password2=password
    fi
    sshpass -p $password2 ssh $STAGING_SERVER "
        cd './'$PROJECTUS;
        ./pg_backup.sh
    "

    echo "#### FETCH AND COMPILE SOURCE ####"
    echo "Checking out clean version from repository..."
    TMP_BUILD_DIR='tmp_build'
    svn_addr=$(svn info | grep -i ^url | cut -d' ' -f 2)
    mkdir -p $TMP_BUILD_DIR; cd $TMP_BUILD_DIR
    echo -e " Temporary source directory is $(pwd)"
    if ! svn checkout $svn_addr --quiet; then
        echo "Error : during svn checkout"
        exit 1
    fi
    cp ../conf/dbRes.conf projectus/conf/
    cd projectus/
    #Edit version of production
    dayVersion=$(date +%Y.%m.%d)
    echo $dayVersion > "./public/version.txt"

    echo "Compiling..."
    timeStart=$(date +%s)
    dist=$(activator dist)
    exitcode_dist=$?
    if [ $exitcode_dist -ne 0 ]; then
        echo -e $dist
        echo "Error : Compile error (code:$exitcode_dist)"
        exit 1
    else
        timeEnd=$(date +%s)
        timeDuration=$(echo "$timeEnd - $timeStart" | bc)
        echo -e "\t...done in $timeDuration s"
        echo "✓ Success"
    fi

    echo "#### SEND ZIP TO SERVER ####"
    timeStart=$(date +%s)
    sshpass -p $password scp ./target/universal/projectus-1.0-SNAPSHOT.zip "$serverName:./$PROJECTUS/release-$day.zip"
    lastres=$?
    if [ $lastres -ne 0 ]; then
        echo "Error when send zip to server (sshpass exitcode:$lastres)"
        exit 1
    else
        timeEnd=$(date +%s)
        timeDuration=$(echo "$timeEnd - $timeStart" | bc)
        echo -e "\t...done in $timeDuration s"
        echo "✓ OK"
    fi

    cd "../.."
    echo "#### DELETE TEMPORARY SOURCE DIRECTORY ####"
    rm -Rf $TMP_BUILD_DIR

    echo "#### RESTART PROJECTUS ####"
    sshpass -p $password ssh $serverName "
        cd './'$PROJECTUS;
        $stop_projectus

        echo 'Extracting release-$day.zip...'
        if ! unzip -oq './release-$day.zip'; then
            echo Error unzipping file
            exit 1
        fi

        mv projectus-1.0-SNAPSHOT 'release-$day'
        echo 'release-$day' > \"CURRENT\"
        rm './release-$day.zip'

        $start_projectus
        "
    totalTimeEnd=$(date +%s)
    totalTimeDuration=$(echo "$totalTimeEnd - $totalTimeStart" | bc)
    echo -e "\t...total append was done in $totalTimeDuration s"
    exit
fi

if test $1 = "choose"
then
    echo "#### CONNECTING TO SERVER ####\n"
    sshpass -p $password ssh $serverName "
        cd './'$PROJECTUS;
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
             exit 1
        fi
        read number
        dir=(\$(ls -d release*))
        dir=\${dir[\$number]}
        if [ -z \${dir// } ]
        then 
            echo 'Error when selecting folder'
            exit 1
        fi
        echo 'You have choose :'
        echo \$dir
        echo "\$dir" > \"CURRENT\"
        $start_projectus
        exit 0
        "
    exit
fi

echo "Error : The argument $1 is not good\n"
echo -e "List of argument :\n"$possibleActions
exit

