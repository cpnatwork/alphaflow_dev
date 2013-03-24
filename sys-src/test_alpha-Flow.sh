#!/bin/bash
#Skript zum Testen von alpha-Flow

ROOTDIR=`pwd`
HOMEDIR=$ROOTDIR/.
STARTUP=$HOMEDIR/alphastartup
TESTDIR=$HOMEDIR/testrt
ONEJAR=alph-o-matic.jar
TESTFILENAME=Arztbrief

cd $TESTDIR
./clean_directory.sh $TESTDIR

if [ $# -eq 0 ]; then
	cd $HOMEDIR
        mvn install -DskipTests=true
else
        cd $HOMEDIR/$1
        mvn install
        cd $STARTUP
        mvn install
fi

cd $TESTDIR
JAVAOPTS="$JAVAOPTS"
java $JAVAOPTS -jar $ONEJAR $TESTFILENAME.pdf
java $JAVAOPTS -jar $TESTFILENAME.jar
cd $ROOTDIR
