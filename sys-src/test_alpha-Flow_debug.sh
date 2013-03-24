#!/bin/bash
#Skript zum Testen von alpha-Flow

ROOTDIR=`pwd`
HOMEDIR=$ROOTDIR/.
TESTDIR=$HOMEDIR/testrt
ONEJAR=alph-o-matic.jar
TESTFILENAME=Arztbrief

cd $TESTDIR
./clean_directory.sh $TESTDIR

cd $HOMEDIR
mvn install -DskipTests=true

cd $TESTDIR
JAVAOPTS="$JAVAOPTS -Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=5555"
java $JAVAOPTS -jar $ONEJAR $TESTFILENAME.pdf
java $JAVAOPTS -jar $TESTFILENAME.jar
cd $ROOTDIR
