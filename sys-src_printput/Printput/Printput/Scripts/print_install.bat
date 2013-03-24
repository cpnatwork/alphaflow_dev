@echo off
set printerName="PrintPut" 
set pathDriver="%CD%\..\gs9.02\lib\ghostpdf.inf"
 
if not exist %pathDriver% goto Error


rem Install printer driver
rundll32 printui.dll,PrintUIEntry /ia /m "Ghostscript PDF" /h "Intel" /f %pathDriver%


rem Install printer
rundll32 printui.dll,PrintUIEntry /if /b %printerName% /f %pathDriver% /r file: /m "Ghostscript PDF"

 
:End
echo Printer %printerName% installed
exit /b 0

:Error
echo pathDriver %pathDriver% not found
exit /b 1