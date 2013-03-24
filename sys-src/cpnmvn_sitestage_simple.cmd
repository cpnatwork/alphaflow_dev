@echo off

set ALPHAROOT=%CD%
set ALPHAHOME=%ALPHAROOT%

rem -DskipTests=false -Dmaven.test.skip=false -Dmaven.test.failure.ignore=true
set MVNSTDOPT=-Dsilent=true 

cd /D %ALPHAHOME%
cmd /D/C mvn install %MVNSTDOPT%
cmd /D/C mvn site:site %MVNSTDOPT%
cmd /D/C mvn site:stage %MVNSTDOPT%

pause