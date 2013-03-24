@echo off

set ALPHAROOT=%CD%
set ALPHAHOME=%ALPHAROOT%

set MVNSTDOPT=-Dsilent=true -DskipTests=false -Dmaven.test.skip=false -Dmaven.test.failure.ignore=true

cd /D %ALPHAHOME%
cmd /D/C mvn clean %MVNSTDOPT%
cmd /D/C mvn install %MVNSTDOPT%
cmd /D/C mvn jxr:jxr jxr:test-jxr %MVNSTDOPT%
cmd /D/C mvn site:site %MVNSTDOPT%
cmd /D/C mvn site:stage %MVNSTDOPT%

pause