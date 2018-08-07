#!/bin/bash

USER=$(whoami)
PROJECTUS="/Users/$USER/Sites/projectus"


cd $PROJECTUS;
if [ -f "CURRENT" ]; then
    cd $(cat "CURRENT");
    kill -9 $(cat RUNNING_PID);
    rm RUNNING_PID
    echo "projectus.sh: the user is $USER" > projectus.sh.log
    if [ "$USER" == "exitLab" ]; then
        echo -e "tlaunched with production settings" >> projectus.sh.log
        ./bin/projectus  -Dconfig.file=./conf/production.conf
    elif [ "$USER" == "tictac" ]; then
        echo -e "tlaunched with staging settings" >> projectus.sh.log
        ./bin/projectus  -Dconfig.file=./conf/staging.conf
    else
        echo "Error: unexpected user in projectus.sh" >> projectus.sh.log
    fi
else
    echo "Choose a release with ./deploytool.sh choose" >> projectus.sh.log
fi

