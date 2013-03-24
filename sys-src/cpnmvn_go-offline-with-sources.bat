@echo off

set MVNOPTS=-T 4 -Dsilent=true

cmd /D /C mvn dependency:go-offline %MVNOPTS%
cmd /D /C mvn dependency:sources %MVNOPTS%

pause