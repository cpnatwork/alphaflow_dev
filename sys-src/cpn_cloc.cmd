@echo off

rem set EXEC=cloc-1.53.exe --exclude-dir=target,.svn,testrt,archive,_archive,.settings,agitar --force-lang=Java,drl
set EXEC=cloc-1.53.exe --exclude-dir=target,.svn,testrt,archive,_archive,.settings,agitar --exclude-lang=XML,make

set SUMREPORT=all

del *.lang.cloc 2> nul
del %SUMREPORT%*.file.* %SUMREPORT%*.lang.* 2> nul

for /D %%D in (alpha* hydra*) do (
	cmd /D/C %EXEC% --report-file=%%D.lang.cloc %%D
)
cmd /D/C cloc-1.53.exe --sum-reports --report_file=%SUMREPORT% *.lang.cloc
move %SUMREPORT%.file %SUMREPORT%.cloc.file.txt
move %SUMREPORT%.lang %SUMREPORT%.cloc.lang.txt
type %SUMREPORT%.cloc.file.txt
type %SUMREPORT%.cloc.lang.txt

cmd /D/C cloc-1.53.exe --sum-reports --csv --report_file=%SUMREPORT%_csv alpha*.lang.cloc hydra*.lang.cloc
move %SUMREPORT%.file %SUMREPORT%.cloc.file.csv
move %SUMREPORT%.lang %SUMREPORT%.cloc.lang.csv
type %SUMREPORT%.cloc.file.csv
echo
type %SUMREPORT%.cloc.lang.csv

del *.lang.cloc 2> nul

pause