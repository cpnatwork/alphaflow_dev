@echo off

set ALPHAROOT=%CD%
set HYDRAHOME=%ALPHAROOT%\hydra

cd /D %HYDRAHOME%
cmd /D/C mvn clean
cmd /D/C mvn install