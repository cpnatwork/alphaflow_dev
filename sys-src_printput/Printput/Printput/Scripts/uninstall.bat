@echo off
set printerName="PrintPut"

rem Uninstall old Printer
rundll32 printui.dll,PrintUIEntry /n %printerName% /dl