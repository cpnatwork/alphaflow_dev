#!/bin/bash

ALPHAROOT=`pwd`
HYDRAHOME=$ALPHAROOT/../sys-src_hvs/hydra

cd $HYDRAHOME
mvn clean
mvn install

