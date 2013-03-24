@echo off

set TESTDIR=%CD%
set ONEJAR=alph-o-matic.jar
set INJECTOR_TESTFILE_A=tmplt_full.xml
set INJECTOR_TESTFILE_B=tmplt_partly.xml

set JAVAOPTS=-client -Xms192m -Xmx512m -XX:MaxPermSize=128m
echo JAVAOPTS = %JAVAOPTS%

cd %TESTDIR%
rem ...
cmd /D/C java %JAVAOPTS% -jar %ONEJAR% %INJECTOR_TESTFILE_A%
cmd /D/C java %JAVAOPTS% -jar %ONEJAR% %INJECTOR_TESTFILE_B%
