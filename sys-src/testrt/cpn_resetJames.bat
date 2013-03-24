@echo off

REM Path to the James Remote Manager executable/start script
SET RMTPATH=.\bin\james-cli.bat

REM Host of the James installation
SET RMTHOST=localhost

REM Remote management port of the James installation
SET RMTPORT=9999

REM Default username
SET DEFAULTUSERNAME=promed.alphaflow

REM Default password
SET DEFAULTPW=pr,R0m3d

REM Default domain for created users
rem SET DEFAULTDOMAIN=localhost
SET DEFAULTDOMAIN=googlemail.com

REM Number of accounts to be reset/created
SET NUMACC=5

REM Command to invoke the James Remote Manager
SET RMTCOMM=%RMTPATH% -h %RMTHOST% -p %RMTPORT%


REM Reset the default domain
cmd /D/C %RMTCOMM% removedomain %DEFAULTDOMAIN%
cmd /D/C %RMTCOMM% adddomain %DEFAULTDOMAIN%

REM ***********************
REM Reset user accounts
REM ***********************

REM Base account (un-numbered)
cmd /D/C %RMTCOMM% removeuser %DEFAULTUSERNAME%@%DEFAULTDOMAIN%
cmd /D/C %RMTCOMM% adduser %DEFAULTUSERNAME%@%DEFAULTDOMAIN% %DEFAULTPW%
REM numbered-user accounts
FOR /L %%I IN (1,1,%NUMACC%) DO (
	cmd /D/C %RMTCOMM% removeuser %DEFAULTUSERNAME%%%I@%DEFAULTDOMAIN%
	cmd /D/C %RMTCOMM% adduser %DEFAULTUSERNAME%%%I@%DEFAULTDOMAIN% %DEFAULTPW%
)

exit
