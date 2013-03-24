using System;
using System.Collections.Generic;
using System.Runtime.InteropServices;
using System.ComponentModel;
using System.ServiceProcess;
using System.Windows.Forms;
using Microsoft.Win32;


namespace InstallationOverride
{

    /// <summary>
    /// Diese Klasse beherbergt die wichtigsten Installationsroutinen: Port, Monitor und Treiberinstallation
    /// </summary>
    /// <ToDo>
    /// Vereinheitlicher der Sprache
    /// Logging
    /// </ToDo>
    class PrinterMonitorInstaller
    {

        private readonly String _applicationPath;

        //Installationsverzeichnis wird dem Konstruktor übergeben
        public PrinterMonitorInstaller(String path)
        {
            _applicationPath = path;
        }


        //In Settings-Configdatei ausgelagert
        private readonly String MonitorName = Properties.Settings.Default.MonitorName;
        private readonly String PrinterPortName = Properties.Settings.Default.PrinterPortName;
        private readonly String PrinterName = Properties.Settings.Default.PrinterName;
        private readonly String PrinterDriverName = Properties.Settings.Default.PrinterDriverName;
        private readonly String PrintPutExeFileName = Properties.Settings.Default.PrintPutExeFileName;

        private readonly String RedMon32FileName = Properties.Settings.Default.RedMon32FileName;
        private readonly String RedMon64FileName = Properties.Settings.Default.RedMon64FileName;


        /// <summary>
        /// Gets list of installed printer monitors
        /// </summary>        
        /// <returns>A list of installed printer monitors</returns>
        private List<String> GetInstalledMonitors()
        {
            uint pcbNeeded = 0;
            uint pcReturned = 0;

            if (PrinterMonitorInstallerPInvoke.EnumMonitors(null, 2, IntPtr.Zero, 0, ref pcbNeeded, ref pcReturned))
            {
                //succeeds, but must not, because buffer is zero (too small)!
                throw new Exception("EnumPorts should fail!");
            }

            int lastWin32Error = Marshal.GetLastWin32Error();
            //ERROR_INSUFFICIENT_BUFFER = 122 expected, if not -> Exception
            if (lastWin32Error != 122)
            {
                throw new Win32Exception(lastWin32Error);
            }

            IntPtr pMonitors = Marshal.AllocHGlobal((int)pcbNeeded);
            if (PrinterMonitorInstallerPInvoke.EnumMonitors(null, 2, pMonitors, pcbNeeded, ref pcbNeeded, ref pcReturned))
            {
                IntPtr currentMonitor = pMonitors;
                PrinterMonitorInstallerPInvoke.MONITOR_INFO_2[] minfo = new PrinterMonitorInstallerPInvoke.MONITOR_INFO_2[pcReturned];

                for (int i = 0; i < pcReturned; i++)
                {
                    minfo[i] = (PrinterMonitorInstallerPInvoke.MONITOR_INFO_2)Marshal.PtrToStructure(currentMonitor, typeof(PrinterMonitorInstallerPInvoke.MONITOR_INFO_2));
                    currentMonitor = (IntPtr)(currentMonitor.ToInt32() + Marshal.SizeOf(typeof(PrinterMonitorInstallerPInvoke.MONITOR_INFO_2)));

                }
                Marshal.FreeHGlobal(pMonitors);

                List<String> result = new List<string>();
                for (int i = 0; i < pcReturned; i++)
                    result.Add(minfo[i].pName);
                return result;
            }

            throw new Win32Exception(Marshal.GetLastWin32Error());
        }

        /// <summary>
        /// Installs a printer monitor
        /// </summary>
        /// <param name="monitorName">The Name of the printer monitor</param>
        /// <param name="pathToMonitorDLL">The Path to the Monitor-DLL</param>
        private void InstallMonitor(String monitorName, String pathToMonitorDLL)
        {
            PrinterMonitorInstallerPInvoke.MONITOR_INFO_2 mi2;
            mi2.pName = monitorName;
            mi2.pEnvironment = null;
            mi2.pDLLName = pathToMonitorDLL;

            if (PrinterMonitorInstallerPInvoke.AddMonitor(null, 2, ref mi2) == 0)
            {
                int errno = Marshal.GetLastWin32Error();

                //3006 == Monitor bereits installiert!
                if (errno != 3006)
                    throw new Win32Exception(errno);
                //else
                MessageBox.Show("Could not install monitor. Monitor already installed!");
            }
        }

        /// <summary>
        /// Uninstalls a printer monitor
        /// </summary>
        /// <param name="monitorName">The Name of the printer monitor</param>
        private void UninstallMonitor(String monitorName)
        {
            //Wenn Monitor gelöscht wird, dann auch automatisch dazugehöriger Port!

            if (PrinterMonitorInstallerPInvoke.DeleteMonitor(null, null, monitorName) == false)
            {
                int errno = Marshal.GetLastWin32Error();

                //3000 == Monitor ist nicht installiert!
                if (errno != 3000)
                    throw new Win32Exception(errno);
                //else
                MessageBox.Show("Could not remove Monitor. Monitor not installed!");
            }
        }


        /// <summary>
        /// Gets list of installed printer ports
        /// </summary>        
        /// <returns>A list of installed printer ports</returns>
        private List<String> GetInstalledPorts()
        {
            uint pcbNeeded = 0;
            uint pcReturned = 0;

            if (PrinterMonitorInstallerPInvoke.EnumPorts(null, 2, IntPtr.Zero, 0, ref pcbNeeded, ref pcReturned))
            {
                //succeeds, but must not, because buffer is zero (too small)!
                throw new Exception("EnumPorts should fail!");
            }

            int lastWin32Error = Marshal.GetLastWin32Error();
            //ERROR_INSUFFICIENT_BUFFER = 122 expected, if not -> Exception
            if (lastWin32Error != 122)
            {
                throw new Win32Exception(lastWin32Error);
            }

            IntPtr pPorts = Marshal.AllocHGlobal((int)pcbNeeded);
            if (PrinterMonitorInstallerPInvoke.EnumPorts(null, 2, pPorts, pcbNeeded, ref pcbNeeded, ref pcReturned))
            {
                IntPtr currentPort = pPorts;
                PrinterMonitorInstallerPInvoke.PORT_INFO_2[] pinfo = new PrinterMonitorInstallerPInvoke.PORT_INFO_2[pcReturned];



                for (int i = 0; i < pcReturned; i++)
                {
                    pinfo[i] = (PrinterMonitorInstallerPInvoke.PORT_INFO_2)Marshal.PtrToStructure(currentPort, typeof(PrinterMonitorInstallerPInvoke.PORT_INFO_2));
                    currentPort = (IntPtr)(currentPort.ToInt32() + Marshal.SizeOf(typeof(PrinterMonitorInstallerPInvoke.PORT_INFO_2)));

                }
                Marshal.FreeHGlobal(pPorts);

                List<String> result = new List<string>();
                for (int i = 0; i < pcReturned; i++)
                    result.Add(pinfo[i].pPortName);
                return result;
            }

            throw new Win32Exception(Marshal.GetLastWin32Error());
        }

        /// <summary>
        /// Installs a port
        /// </summary>
        /// <param name="printerPortName">The name of the printer port</param>        
        /// <param name="monitorName">The name of the printer monitor using this port</param>
        /// <param name="redmonCommand">The redmon command, which should be executed wile executing</param> 
        /// <param name="printerName">The printer name using this port</param>
        private void InstallPort(String printerPortName, String monitorName, String redmonCommand, String printerName)
        {
            //Über die Registry, weil die Api einen Benutzereingriff erfordert: Siehe MSDNAA AddPort() oder ConfigurePort()

            RegistryKey newKey = Registry.LocalMachine.OpenSubKey(@"SYSTEM\CurrentControlSet\Control\Print\Monitors", true);
            if (newKey == null)
                throw new Exception("InstallPort: NewKey is null!");

            RegistryKey subKey = newKey.CreateSubKey(monitorName);
            if (subKey == null)
                throw new Exception("InstallPort: SubKey is null!");

            using (RegistryKey portConfig = subKey.CreateSubKey(@"Ports\" + printerPortName))
            {
                if (portConfig != null)
                {
                    portConfig.SetValue("Arguments", "", RegistryValueKind.String);
                    portConfig.SetValue("Command", redmonCommand, RegistryValueKind.String);
                    portConfig.SetValue("Delay", 300, RegistryValueKind.DWord);
                    portConfig.SetValue("Description", "Redirected Port", RegistryValueKind.String);
                    portConfig.SetValue("LogFileDebug", 0, RegistryValueKind.DWord);
                    portConfig.SetValue("LogFileName", "", RegistryValueKind.String);
                    portConfig.SetValue("LogFileUse", 0, RegistryValueKind.DWord);
                    portConfig.SetValue("Output", 0, RegistryValueKind.DWord);
                    portConfig.SetValue("Printer", printerName, RegistryValueKind.String);
                    portConfig.SetValue("PrintError", 0, RegistryValueKind.DWord);
                    portConfig.SetValue("RunUser", 0, RegistryValueKind.DWord);
                    portConfig.SetValue("ShowWindow", 0, RegistryValueKind.DWord);
                }
                else
                    throw new Exception("InstallPort: PortConfigKey is null!");
            }
        }

        /// <summary>
        /// Uninstalls a printer port
        /// </summary>
        /// <param name="printerPortName">The Name of the printer port</param>
        private void UninstallPort(String printerPortName)
        {
            if (PrinterMonitorInstallerPInvoke.DeletePort(null, 0, printerPortName) == false)
            {
                int errno = Marshal.GetLastWin32Error();
                throw new Win32Exception(errno);
            }
        }


        //public List<String> GetInstalledPrintersManaged()
        //{
        //    return PrinterSettings.InstalledPrinters.Cast<string>().ToList();
        //}

        private List<String> GetInstalledPrintersUnmanaged()
        {
            //siehe http://msdn.microsoft.com/en-us/library/dd162692(v=vs.85).aspx

            uint cbNeeded = 0;
            uint cReturned = 0;
            if (PrinterMonitorInstallerPInvoke.EnumPrinters(PrinterMonitorInstallerPInvoke.PrinterEnumFlags.PRINTER_ENUM_LOCAL, null, 2, IntPtr.Zero, 0, ref cbNeeded, ref cReturned))
            {
                //succeeds, but shouldn't, because buffer is zero (too small)!
                throw new Exception("EnumPrinters should fail!");
            }

            int lastWin32Error = Marshal.GetLastWin32Error();
            //ERROR_INSUFFICIENT_BUFFER = 122 expected, if not -> Exception
            if (lastWin32Error != 122)
            {
                throw new Win32Exception(lastWin32Error);
            }

            IntPtr pAddr = Marshal.AllocHGlobal((int)cbNeeded);
            if (PrinterMonitorInstallerPInvoke.EnumPrinters(PrinterMonitorInstallerPInvoke.PrinterEnumFlags.PRINTER_ENUM_LOCAL, null, 2, pAddr, cbNeeded, ref cbNeeded, ref cReturned))
            {
                PrinterMonitorInstallerPInvoke.PRINTER_INFO_2[] printerInfo2 = new PrinterMonitorInstallerPInvoke.PRINTER_INFO_2[cReturned];
                int offset = pAddr.ToInt32();
                Type type = typeof(PrinterMonitorInstallerPInvoke.PRINTER_INFO_2);
                int increment = Marshal.SizeOf(type);
                for (int i = 0; i < cReturned; i++)
                {
                    printerInfo2[i] = (PrinterMonitorInstallerPInvoke.PRINTER_INFO_2)Marshal.PtrToStructure(new IntPtr(offset), type);
                    offset += increment;
                }
                Marshal.FreeHGlobal(pAddr);

                List<String> result = new List<string>();
                for (int i = 0; i < cReturned; i++)
                    result.Add(printerInfo2[i].pPrinterName);
                return result;
            }

            throw new Win32Exception(Marshal.GetLastWin32Error());
        }

        private void InstallPrinter(String printerName, String printerPortName, String printerDriverName)
        {
            PrinterMonitorInstallerPInvoke.PRINTER_INFO_2 pInfo = new PrinterMonitorInstallerPInvoke.PRINTER_INFO_2
            {
                pPrinterName = printerName,
                pPortName = printerPortName,
                pDriverName = printerDriverName,
                pPrintProcessor = "WinPrint",
                pShareName = printerName,
                pDatatype = "RAW",
                pComment = printerName,
                Priority = 1,
                DefaultPriority = 1
            };


            IntPtr hPrt = PrinterMonitorInstallerPInvoke.AddPrinter("", 2, ref pInfo);
            if (hPrt == IntPtr.Zero)
            {
                int errno = Marshal.GetLastWin32Error();
                throw new Win32Exception(errno);
            }

            if (PrinterMonitorInstallerPInvoke.ClosePrinter(hPrt) == false)
            {
                int errno = Marshal.GetLastWin32Error();
                throw new Win32Exception(errno);
            }
        }

        private void UninstallPrinter(String printerName)
        {
            IntPtr hPrinter;
            PrinterMonitorInstallerPInvoke.PRINTER_DEFAULTS defaults = new PrinterMonitorInstallerPInvoke.PRINTER_DEFAULTS
            {
                DesiredAccess = 0x000F000C,  //PRINTER_ALL_ACCESS
                pDatatype = IntPtr.Zero,
                pDevMode = IntPtr.Zero
            };


            if (PrinterMonitorInstallerPInvoke.OpenPrinter(printerName, out hPrinter, ref defaults) == false)
            {
                int errno = Marshal.GetLastWin32Error();
                throw new Win32Exception(errno);
            }

            if (hPrinter == IntPtr.Zero)
            {
                int errno = Marshal.GetLastWin32Error();
                throw new Win32Exception(errno);
            }

            if (PrinterMonitorInstallerPInvoke.DeletePrinter(hPrinter) == false)
            {
                int errno = Marshal.GetLastWin32Error();
                throw new Win32Exception(errno);
            }

            if (PrinterMonitorInstallerPInvoke.ClosePrinter(hPrinter) == false)
            {
                int errno = Marshal.GetLastWin32Error();
                throw new Win32Exception(errno);
            }
        }


        private void RemoveJobs(String printerName)
        {
            IntPtr hPrinter;
            PrinterMonitorInstallerPInvoke.PRINTER_DEFAULTS defaults = new PrinterMonitorInstallerPInvoke.PRINTER_DEFAULTS
            {
                DesiredAccess = 0x000F000C, //PRINTER_ALL_ACCESS
                pDatatype = IntPtr.Zero,
                pDevMode = IntPtr.Zero
            };


            if (PrinterMonitorInstallerPInvoke.OpenPrinter(printerName, out hPrinter, ref defaults) == false)
            {
                int errno = Marshal.GetLastWin32Error();
                throw new Win32Exception(errno);
            }

            if (hPrinter == IntPtr.Zero)
            {
                int errno = Marshal.GetLastWin32Error();
                throw new Win32Exception(errno);
            }

            //3 == PRINTER_CONTROL_PURGE => Entfernt alle Jobs!
            if (PrinterMonitorInstallerPInvoke.SetPrinter(hPrinter, 0, IntPtr.Zero, PrinterMonitorInstallerPInvoke.PRINTER_CONTROL_PURGE) == false)
            {
                int errno = Marshal.GetLastWin32Error();
                throw new Win32Exception(errno);
            }

            if (PrinterMonitorInstallerPInvoke.ClosePrinter(hPrinter) == false)
            {
                int errno = Marshal.GetLastWin32Error();
                throw new Win32Exception(errno);
            }
        }


        private List<String> GetInstalledPrinterDrivers()
        {
            /*
                'To determine the required buffer size,
                'call EnumPrinterDrivers with cbBuffer set
                'to zero. The call will fails specifying
                'ERROR_INSUFFICIENT_BUFFER and filling in
                'cbRequired with the required size, in bytes,
                'of the buffer required to hold the array
                'of structures and data.
            */
            uint cbNeeded = 0;
            uint cReturned = 0;
            if (PrinterMonitorInstallerPInvoke.EnumPrinterDrivers(null, null, 2, IntPtr.Zero, 0, ref cbNeeded, ref cReturned))
            {
                //succeeds, but shouldn't, because buffer is zero (too small)!
                throw new Exception("EnumPrinters should fail!");
            }

            int lastWin32Error = Marshal.GetLastWin32Error();
            //ERROR_INSUFFICIENT_BUFFER = 122 expected, if not -> Exception
            if (lastWin32Error != 122)
            {
                throw new Win32Exception(lastWin32Error);
            }

            IntPtr pAddr = Marshal.AllocHGlobal((int)cbNeeded);
            if (PrinterMonitorInstallerPInvoke.EnumPrinterDrivers(null, null, 2, pAddr, cbNeeded, ref cbNeeded, ref cReturned))
            {
                PrinterMonitorInstallerPInvoke.DRIVER_INFO_2[] printerInfo2 = new PrinterMonitorInstallerPInvoke.DRIVER_INFO_2[cReturned];
                int offset = pAddr.ToInt32();
                Type type = typeof(PrinterMonitorInstallerPInvoke.DRIVER_INFO_2);
                int increment = Marshal.SizeOf(type);
                for (int i = 0; i < cReturned; i++)
                {
                    printerInfo2[i] = (PrinterMonitorInstallerPInvoke.DRIVER_INFO_2)Marshal.PtrToStructure(new IntPtr(offset), type);
                    offset += increment;
                }
                Marshal.FreeHGlobal(pAddr);

                List<String> result = new List<string>();
                for (int i = 0; i < cReturned; i++)
                    result.Add(printerInfo2[i].pName);
                return result;
            }

            throw new Win32Exception(Marshal.GetLastWin32Error());
        }

        private void InstallPrinterDriver(String printerDriverName, String appPath)
        {
            //Parameter siehe http://www.winfaq.de/faq_html/Content/tip2000/onlinefaq.php?h=tip2028.htm
            string parameter = @" /ia /m " + printerDriverName + @" /f """ + appPath + @"gs9.02\lib\printputpdf.inf""";


            int res = PrinterMonitorInstallerPInvoke.PrintUIEntryW(IntPtr.Zero, IntPtr.Zero, parameter, 0);
            if (res != 0)
            {
                int lastWin32Error = Marshal.GetLastWin32Error();
                throw new Win32Exception(lastWin32Error);
            }
        }

        private void UninstallPrinterDriver(String printerDriverName)
        {
            if (PrinterMonitorInstallerPInvoke.DeletePrinterDriver(null, null, printerDriverName) == false)
            {
                int errno = Marshal.GetLastWin32Error();
                throw new Win32Exception(errno);
            }
        }


        private void SetPrinterAsDefault(String printerName)
        {
            //Über Registry, da die anderen Methoden nicht klappten!
            if ((MessageBox.Show("Set PrintPut as default printer for the current user?", "PrintPut", MessageBoxButtons.YesNo)) == DialogResult.Yes)
            {
                RegistryKey key = Registry.CurrentUser.OpenSubKey(@"Software\Microsoft\Windows NT\CurrentVersion\Windows", true);
                if (key == null)
                    throw new Exception("SetPrinterAsDefault: OpenSubKey failed!");

                key.SetValue("Device", printerName + @", winspool");
            }


            //Funktioniert nicht
            //if ((MessageBox.Show("Set PrintPut as default printer?", "PrintPut", MessageBoxButtons.YesNo)) == DialogResult.Yes)
            //{
            //    string parameter = @" /y /n " + printerName;


            //    int res = PrintUIEntryW(IntPtr.Zero, IntPtr.Zero, parameter, 0);
            //    if (res != 0)
            //    {
            //        int lastWin32Error = Marshal.GetLastWin32Error();
            //        throw new Win32Exception(lastWin32Error);
            //    }


            //    if (SetDefaultPrinter(printerName) == false)
            //    {
            //        int errno = Marshal.GetLastWin32Error();
            //        throw new Win32Exception(errno);
            //    }
            //}




            //Funktioniert nicht!
            /*
            if ((MessageBox.Show("Set PrintPut as default printer?", "PrintPutSetup", MessageBoxButtons.YesNo)) == DialogResult.Yes)
            {
                if (SetDefaultPrinter(printerName) == false)
                {
                    int errno = Marshal.GetLastWin32Error();
                    throw new Win32Exception(errno);
                }


                
                IntPtr hwndBroadcast = new IntPtr(0xffff);
                const uint wmSettingchange = 0x1A;

                if (SendNotifyMessage(hwndBroadcast, wmSettingchange, UIntPtr.Zero, IntPtr.Zero) == false)
                {
                    int errno = Marshal.GetLastWin32Error();
                    throw new Win32Exception(errno);
                }
            }
            */
        }

        private void InstallMonitor()
        {
            String pathToMonitorDLL;

            if (EnvironmentCheck.Is64BitOperatingSystem)
                pathToMonitorDLL = _applicationPath + @"RedMon\" + RedMon64FileName;
            else
                pathToMonitorDLL = _applicationPath + @"RedMon\" + RedMon32FileName;


            List<String> installedMonitors = GetInstalledMonitors();

            if (installedMonitors == null)
                throw new Exception("Could not get installed Monitors!");

            if (installedMonitors.Contains(MonitorName) == false)
                InstallMonitor(MonitorName, pathToMonitorDLL);
        }

        private void InstallPort()
        {
            List<String> installedPorts = GetInstalledPorts();

            if (installedPorts == null)
                throw new Exception("Could not get installed Ports!");

            if (installedPorts.Contains(PrinterPortName) == false)
                InstallPort(PrinterPortName, MonitorName, (_applicationPath + PrintPutExeFileName), PrinterName);
        }

        private void InstallPrinterDriver()
        {
            List<String> installedPrinterDrivers = GetInstalledPrinterDrivers();
            if (installedPrinterDrivers == null)
                throw new Exception("Could not get installed PrinterDrivers!");

            if (installedPrinterDrivers.Contains(PrinterDriverName) == false)
            {
                InstallPrinterDriver(PrinterDriverName, _applicationPath);
            }
        }

        private void InstallPrinter()
        {
            List<String> printerList = GetInstalledPrintersUnmanaged();

            if (printerList.Contains(PrinterName) == false)
            {
                //Damit die Veränderung der Ports bemerkt wird??? oO
                GetInstalledPorts();

                InstallPrinter(PrinterName, PrinterPortName, PrinterDriverName);
            }
        }

        private void SetPrinterAsDefault()
        {
            List<String> printerList = GetInstalledPrintersUnmanaged();

            if (printerList.Contains(PrinterName))
            {
                SetPrinterAsDefault(PrinterName);
            }
        }


        private void RemoveJobs()
        {
            List<String> printerList = GetInstalledPrintersUnmanaged();

            if (printerList.Contains(PrinterName))
            {
                RemoveJobs(PrinterName);
            }
        }

        private void UninstallPrinter()
        {
            List<String> printerList = GetInstalledPrintersUnmanaged();

            if (printerList.Contains(PrinterName))
            {
                UninstallPrinter(PrinterName);
            }
        }

        private void UninstallPrinterDriver()
        {
            List<String> installedPrinterDrivers = GetInstalledPrinterDrivers();
            if (installedPrinterDrivers == null)
                throw new Exception("Could not get installed PrinterDrivers!");

            if (installedPrinterDrivers.Contains(PrinterDriverName))
            {
                UninstallPrinterDriver(PrinterDriverName);
            }
        }

        private void UninstallPort()
        {
            List<String> installedPorts = GetInstalledPorts();
            if (installedPorts == null)
                throw new Exception("Could not get installed Ports!");

            if (installedPorts.Contains(PrinterPortName))
                UninstallPort(PrinterPortName);
        }

        private void UninstallMonitor()
        {
            List<String> installedMonitors = GetInstalledMonitors();

            if (installedMonitors == null)
                throw new Exception("Could not get installed Ports!");

            if (installedMonitors.Contains(MonitorName))
                UninstallMonitor(MonitorName);
        }


        private void StopService(String serviceName, TimeSpan timeout)
        {
            if (serviceName == null) throw new ArgumentNullException("serviceName");

            ServiceController service = new ServiceController(serviceName);

            if (service.Status != ServiceControllerStatus.Stopped)
            {
                service.Stop();
                service.WaitForStatus(ServiceControllerStatus.Stopped, timeout);
            }
        }

        private void StartService(String serviceName, TimeSpan timeout)
        {
            if (serviceName == null) throw new ArgumentNullException("serviceName");

            ServiceController service = new ServiceController(serviceName);

            if (service.Status != ServiceControllerStatus.Running)
            {
                service.Start();
                service.WaitForStatus(ServiceControllerStatus.Running, timeout);
            }
        }


        public void InstallAll()
        {
            //ActivityDiagram
            InstallMonitor();
            InstallPort();
            InstallPrinterDriver();
            InstallPrinter();
            SetPrinterAsDefault();
        }


        public void UninstallAll()
        {
            StopService("Spooler", TimeSpan.MaxValue);
            StartService("Spooler", TimeSpan.MaxValue);

            RemoveJobs();
            UninstallPrinter();
            StopService("Spooler", TimeSpan.MaxValue);
            StartService("Spooler", TimeSpan.MaxValue);

            UninstallPrinterDriver();
            UninstallPort();
            UninstallMonitor();
        }
    }
}
