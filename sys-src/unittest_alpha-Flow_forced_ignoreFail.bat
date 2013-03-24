@echo off
REM Script cleans the testrt directory

SET ROOTDIR=%cd%
SET HOMEDIR=%ROOTDIR%\.
echo ROOTDIR = %ROOTDIR%
echo HOMEDIR = %HOMEDIR%

set MVNOPTS=-T 4 -Dsilent=true -DskipTests=false -Dmaven.test.skip=false -Dmaven.test.failure.ignore=true
echo MVNOPTS = %MVNOPTS%

cd %HOMEDIR%
cmd /D/C mvn verify %MVNOPTS%

pause