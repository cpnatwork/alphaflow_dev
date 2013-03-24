@echo off
REM Script cleans the testrt directory

REM SET ONEJAR=alph-o-matic.jar

IF EXIST %1 GOTO Run 
echo "No parameter given for path!"
echo "Exiting without success..."
pause
EXIT

:Run
FOR %%J in (*.jar) do (
	rem IF "%%J"=="alph-o-matic.jar" «continue»;
	DEL "%%~J"
	rd /S /Q "%%~nJ"
	DEL "%%~nJ*.log"
)

REM copy latest onejar from target
REM this is done by maven now
REM XCOPY /Q /Y ..\target\%ONEJAR% .
