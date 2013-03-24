@echo off

set ALPHAROOT=%CD%
set ALPHAHOME=%ALPHAROOT%\alphabuildhub

set MVNOPTS=-Dsilent=true

cd /D %ALPHAROOT%
cmd /D/C mvn clean eclipse:clean %MVNOPTS%
cmd /D/C mvn dependency:go-offline %MVNOPTS%
cmd /D/C mvn dependency:sources %MVNOPTS%
cmd /D/C mvn install %MVNOPTS%

cd /D %ALPHAHOME%
cmd /D/C mvn jxr:jxr jxr:test-jxr %MVNOPTS%
cmd /D/C mvn site:site %MVNOPTS%
cmd /D/C mvn javancss:report %MVNOPTS%

pause