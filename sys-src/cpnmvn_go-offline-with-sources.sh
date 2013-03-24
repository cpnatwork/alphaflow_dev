#!/bin/bash

MVNOPTS="-Dsilent=true"

mvn dependency:go-offline $MVNOPTS
mvn dependency:sources $MVNOPTS
