#!/bin/bash
#Script cleans the testrt directory

#ONEJAR=alph-o-matic.jar

if [ $# -lt 1 ]; then
  echo "No parameter given for path!"
  echo "Exiting without success..."
  exit 1
fi
echo "Cleaning the test directory..."
cd $1

#remove all existing jar files
find . -maxdepth 1 -name "*.jar" -exec rm {} \;

#remove all existing log files
find . -maxdepth 1 -name "*.log" -exec rm {} \;

#remove all directories except .svn
numberOfDirs=`find -maxdepth 1 -type d | wc -l`
# 2 = current dir (.) and svn dir (.svn)
if [ $numberOfDirs -gt 2 ]; then
  for i in $(ls -d */); do
    if [ "$i"==".svn" ]; then
      rm -rf $i
    fi
  done
fi

# copy latest onejar from target
# this is done by maven now
#cp ../target/$ONEJAR .

exit 0

