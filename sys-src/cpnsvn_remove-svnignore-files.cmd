@echo off
SetLocal EnableDelayedExpansion

rem **************************************************
rem * VARIABLE INITIALIZATION
rem **************************************************

set LOGFILE=%~n0.log

set TARGETS=.classpath .project .settings target .wtpmodules

rem **************************************************
rem * MAIN LOGIC
rem **************************************************

for /D %%D in (*) do (
	for %%T in (%TARGETS%) do (
		set EXEC=svn remove --keep-local "%%~D\%%T"
		echo INFO: !EXEC!
		echo.       >> %LOGFILE%
		echo !EXEC! >> %LOGFILE%
		cmd /V /C !EXEC!      >> %LOGFILE%
	)
)

pause
