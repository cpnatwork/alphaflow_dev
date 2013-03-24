@echo off
SetLocal EnableDelayedExpansion

rem **************************************************
rem * VARIABLE INITIALIZATION
rem **************************************************

set LOGFILE=%~n0.log


rem **************************************************
rem * MAIN LOGIC
rem **************************************************

for /D %%D in (alpha*) do (
	set EXEC=svn propset svn:ignore %%D -F %~n0.txt
	echo INFO: !EXEC!
	echo.       >> %LOGFILE%
	echo !EXEC! >> %LOGFILE%
	cmd /V /C !EXEC!      >> %LOGFILE%
)

rem AND: apply it to ROOT folder
set EXEC=svn propset svn:ignore . -F %~n0.txt
echo INFO: !EXEC!
echo.       >> %LOGFILE%
echo !EXEC! >> %LOGFILE%
cmd /V /C !EXEC!      >> %LOGFILE%

pause
