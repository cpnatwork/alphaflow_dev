#!/bin/bash

ALPHAROOT=`pwd`
ALPHAHOME=$ALPHAROOT/alphaflow

MVNOPTS="-Dsilent=true"

cd $ALPHAROOT
mvn clean eclipse:clean $MVNOPTS
mvn dependency:go-offline $MVNOPTS
mvn dependency:sources $MVNOPTS
mvn install $MVNOPTS

cd $ALPHAHOME
mvn jxr:jxr jxr:test-jxr $MVNOPTS
mvn site:site $MVNOPTS
mvn javancss:report $MVNOPTS
