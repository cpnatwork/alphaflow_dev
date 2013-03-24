@echo off
REM Script cleans the testrt directory

SET ROOTDIR=%cd%
SET HOMEDIR=%ROOTDIR%\.
SET TESTDIR=%HOMEDIR%\testrt
set ONEJAR=alph-o-matic.jar
set TESTFILENAME=Arztbrief
set ALPHOMATIC_TESTFILENAME=%TESTFILENAME%.pdf
set INJECTOR_TESTFILE=Anamnesebericht.pdf

echo ROOTDIR = %ROOTDIR%
echo HOMEDIR = %HOMEDIR%
echo TESTDIR = %TESTDIR%

cd %TESTDIR%
CALL clean_directory.bat %TESTDIR%

cd %HOMEDIR%
cmd /D/C mvn install -DskipTests=true

rem *************************************************
rem Unterscheide 'options' von 'arguments'!!
rem (http://docs.oracle.com/javase/1.3/docs/tooldocs/solaris/java.html)
rem 
rem  java [ options ] -jar file.jar [ argument ... ]
rem
rem  options: e.g. -Dproperty=value (a "system property value")
rem       but also -Xfoo or -Xfoo:bar (the "Non-Standard Options")
rem     as well as -XX:+FooBar or -XX:foo=bar
rem             (like -XX:+UseParallelGC, -XX:+AggressiveHeap, -XX:MaxPermSize=128m)
rem cf. http://www.oracle.com/technetwork/java/javase/tech/vmoptions-jsp-140102.html
rem *************************************************

cd %TESTDIR%
set JAVAOPTS=-client -Xms192m -Xmx512m -XX:MaxPermSize=128m
echo JAVAOPTS = %JAVAOPTS%
rem Create new a-Doc based on %TESTFILENAME%.pdf per Injector.createNewDoc(..)
cmd /D/C java %JAVAOPTS% -DalphaCardTitle=LoremIpsum -jar %ONEJAR% %ALPHOMATIC_TESTFILENAME%
rem Test adding another a-Card per Injector.addToCurrent(..)
cmd /D/C java %JAVAOPTS% -DalphaCardTitle="AB 2" -jar %TESTFILENAME%.jar %INJECTOR_TESTFILE%
rem Take a look at the correct result
cmd /D/C java %JAVAOPTS% -jar %TESTFILENAME%.jar
cd %ROOTDIR%
