@echo off
REM Script cleans the testrt directory

SET ROOTDIR=%cd%
SET HOMEDIR=%ROOTDIR%\.
SET TESTDIR=%HOMEDIR%\testrt
set ONEJAR=alph-o-matic.jar
set TESTFILENAME=Arztbrief

echo ROOTDIR = %ROOTDIR%
echo HOMEDIR = %HOMEDIR%
echo TESTDIR = %TESTDIR%

cd %TESTDIR%
CALL clean_directory.bat %TESTDIR%

cd %HOMEDIR%
cmd /D/C mvn install -DskipTests=true

cd %TESTDIR%
set JAVAOPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=5555
echo JAVAOPTS = %JAVAOPTS%
cmd /D/C java -jar %ONEJAR% %TESTFILENAME%.pdf
cmd /D/C java %JAVAOPTS% -jar %TESTFILENAME%.jar
cd %ROOTDIR%
