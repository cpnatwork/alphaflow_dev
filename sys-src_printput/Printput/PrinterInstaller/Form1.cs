using System;
using System.ComponentModel;
using System.Drawing.Printing;
using System.Linq;
using System.Windows.Forms;
using System.Runtime.InteropServices;
using System.Collections.Generic;
using Microsoft.Win32;

namespace PrinterInstaller
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        const String MonitorName = "PrintPut Monitor";
        const String PrinterPortName = "PrintPutPort:";
        const String PrinterName = "PrintPut";
        private const String PrinterDriverName = "PrintPutDriver";



        //DLL dynamisch laden
        //[DllImport("kernel32.dll", SetLastError=true, CharSet = CharSet.Unicode)]
        //public static extern IntPtr LoadLibrary(string dllToLoad);
        //[DllImport("kernel32", CharSet = CharSet.Ansi, ExactSpelling = true, SetLastError = true)]
        //internal static extern IntPtr GetProcAddress(IntPtr hModule, string procName);
        //[DllImport("kernel32.dll")]
        //public static extern bool FreeLibrary(IntPtr hModule);


        //PrintUi-Befehle direkt ausführen        
        [DllImport("printui.dll", SetLastError = true, CharSet = CharSet.Unicode)]
        private static extern int PrintUIEntryW(IntPtr hwnd, IntPtr hinst, string lpszCmdLine, int nCmdShow);



        //Printer auflisten
        [DllImport("winspool.drv", CharSet = CharSet.Auto, SetLastError = true)]
        private static extern bool EnumPrinters(PrinterEnumFlags flags, string name, uint level, IntPtr pPrinterEnum, uint cbBuf, ref uint pcbNeeded, ref uint pcReturned);

        //Printer hinzufügen
        [DllImport("winspool.drv", CharSet = CharSet.Auto, CallingConvention = CallingConvention.StdCall, SetLastError = true)]
        static extern IntPtr AddPrinter(String pName, uint level, ref PRINTER_INFO_2 pPrinter);

        //Printer entfernen
        [DllImport("winspool.drv", CharSet = CharSet.Auto, CallingConvention = CallingConvention.StdCall, SetLastError = true)]
        static extern bool DeletePrinter(IntPtr hPrinter);
        
        //Printer öffnen
        [DllImport("winspool.drv", CharSet = CharSet.Auto, CallingConvention = CallingConvention.StdCall, SetLastError = true)]
        static extern bool OpenPrinter(String pPrinterName, out IntPtr phPrinter, ref PRINTER_DEFAULTS pDefault);

        //Printer schließen
        [DllImport("winspool.drv", SetLastError = true)]
        static extern bool ClosePrinter(IntPtr hPrinter);

        //Um Jobs zu löschen
        [DllImport("winspool.Drv", SetLastError = true, CharSet = CharSet.Auto, CallingConvention = CallingConvention.StdCall)]
        private static extern bool SetPrinter(IntPtr hPrinter, uint level, IntPtr pPrinter, uint command);


        //PrinterDriver auflisten
        [DllImport("winspool.drv", CharSet = CharSet.Auto, SetLastError = true)]
        public static extern bool EnumPrinterDrivers(String pName, String pEnvironment, uint level, IntPtr pDriverInfo, uint cdBuf, ref uint pcbNeeded, ref uint pcRetruned); 

        //PrinterDriver entfernen
        [DllImport("winspool.drv", CharSet = CharSet.Auto, SetLastError = true)]
        public static extern bool DeletePrinterDriver(String pName, String pEnvironment, String pDriverName);


        //Ports auflisten
        [DllImport("winspool.drv", CharSet = CharSet.Auto, SetLastError = true)]
        private static extern bool EnumPorts(String pName, uint level, IntPtr lpbPorts, uint cbBuf, ref uint pcbNeeded, ref uint pcReturned);
        
        //Port entfernen
        [DllImport("winspool.drv", SetLastError = true, CharSet = CharSet.Auto)]
        private static extern bool DeletePort(String pName, uint hwnd, String pPortName);


        //Monitore auflisten
        [DllImport("winspool.drv", CharSet = CharSet.Auto, SetLastError = true)]
        private static extern bool EnumMonitors(String pName, uint level, IntPtr pMonitors, uint cbBuf, ref uint pcbNeeded, ref uint pcReturned); 

        //Monitor hinzufügen
        [DllImport("winspool.drv", SetLastError = true, CharSet = CharSet.Auto)]
        private static extern Int32 AddMonitor(String pName, UInt32 level, ref MONITOR_INFO_2 pMonitors);

        //Monitor entfernen
        [DllImport("winspool.drv", SetLastError = true, CharSet = CharSet.Auto)]
        private static extern bool DeleteMonitor(String pName, String pEnvironment, String pMonitorName);




        // ReSharper disable InconsistentNaming

        [StructLayout(LayoutKind.Sequential, CharSet = CharSet.Auto)]
        public struct MONITOR_INFO_2
        {
            [MarshalAs(UnmanagedType.LPTStr)]
            public string pName;
            [MarshalAs(UnmanagedType.LPTStr)]
            public string pEnvironment;
            [MarshalAs(UnmanagedType.LPTStr)]
            public string pDLLName;
        }

        [StructLayout(LayoutKind.Sequential, CharSet = CharSet.Auto)]
        public struct PRINTER_INFO_2
        {
            [MarshalAs(UnmanagedType.LPTStr)]
            public string pServerName;
            [MarshalAs(UnmanagedType.LPTStr)]
            public string pPrinterName;
            [MarshalAs(UnmanagedType.LPTStr)]
            public string pShareName;
            [MarshalAs(UnmanagedType.LPTStr)]
            public string pPortName;
            [MarshalAs(UnmanagedType.LPTStr)]
            public string pDriverName;
            [MarshalAs(UnmanagedType.LPTStr)]
            public string pComment;
            [MarshalAs(UnmanagedType.LPTStr)]
            public string pLocation;
            public IntPtr pDevMode;
            [MarshalAs(UnmanagedType.LPTStr)]
            public string pSepFile;
            [MarshalAs(UnmanagedType.LPTStr)]
            public string pPrintProcessor;
            [MarshalAs(UnmanagedType.LPTStr)]
            public string pDatatype;
            [MarshalAs(UnmanagedType.LPTStr)]
            public string pParameters;
            public IntPtr pSecurityDescriptor;
            public uint Attributes;
            public uint Priority;
            public uint DefaultPriority;
            public uint StartTime;
            public uint UntilTime;
            public uint Status;
            public uint cJobs;
            public uint AveragePPM;
        }

        [StructLayout(LayoutKind.Sequential, CharSet = CharSet.Auto)]
        public struct PORT_INFO_2
        {
            [MarshalAs(UnmanagedType.LPTStr)]
            public string pPortName;
            [MarshalAs(UnmanagedType.LPTStr)]
            public string pMonitorName;
            [MarshalAs(UnmanagedType.LPTStr)]
            public string pDescription;
            public PortType fPortType;
            public uint Reserved;
        }

        [StructLayout(LayoutKind.Sequential, CharSet = CharSet.Auto)]
        public struct DRIVER_INFO_2 {
            public uint cVersion;
            [MarshalAs(UnmanagedType.LPTStr)]
            public string pName;
            [MarshalAs(UnmanagedType.LPTStr)]
            public string pEnvironment;
            [MarshalAs(UnmanagedType.LPTStr)]
            public string pDriverPath;
            [MarshalAs(UnmanagedType.LPTStr)]
            public string pDataFile;
            [MarshalAs(UnmanagedType.LPTStr)]
            public string pConfigFile;
         }

        [StructLayout(LayoutKind.Sequential, CharSet = CharSet.Auto)]
        public struct PRINTER_DEFAULTS
        {
            public IntPtr pDatatype;
            public IntPtr pDevMode;
            public uint DesiredAccess;
        }

        [FlagsAttribute]
        public enum PrinterEnumFlags
        {
            PRINTER_ENUM_LOCAL = 0x00000002,
            PRINTER_ENUM_DEFAULT = 0x00000001,
            PRINTER_ENUM_CONNECTIONS = 0x00000004,
            PRINTER_ENUM_FAVORITE = 0x00000004,
            PRINTER_ENUM_NAME = 0x00000008,
            PRINTER_ENUM_REMOTE = 0x00000010,
            PRINTER_ENUM_SHARED = 0x00000020,
            PRINTER_ENUM_NETWORK = 0x00000040,
            PRINTER_ENUM_EXPAND = 0x00004000,
            PRINTER_ENUM_CONTAINER = 0x00008000,
            PRINTER_ENUM_ICONMASK = 0x00ff0000,
            PRINTER_ENUM_ICON1 = 0x00010000,
            PRINTER_ENUM_ICON2 = 0x00020000,
            PRINTER_ENUM_ICON3 = 0x00040000,
            PRINTER_ENUM_ICON4 = 0x00080000,
            PRINTER_ENUM_ICON5 = 0x00100000,
            PRINTER_ENUM_ICON6 = 0x00200000,
            PRINTER_ENUM_ICON7 = 0x00400000,
            PRINTER_ENUM_ICON8 = 0x00800000,
            PRINTER_ENUM_HIDE = 0x01000000
        }
        
        [Flags]
        public enum PortType
        {
            Write = 0x1,
            Read = 0x2,
            Redirected = 0x4,
            NetAttached = 0x8
        }

        // ReSharper restore InconsistentNaming



        public List<String> GetInstalledMonitors()
        {
            uint pcbNeeded = 0;
            uint pcReturned = 0;

            if (EnumMonitors(null, 2, IntPtr.Zero, 0, ref pcbNeeded, ref pcReturned))
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
            if (EnumMonitors(null, 2, pMonitors, pcbNeeded, ref pcbNeeded, ref pcReturned))
            {
                IntPtr currentMonitor = pMonitors;
                MONITOR_INFO_2[] minfo = new MONITOR_INFO_2[pcReturned];

                for (int i = 0; i < pcReturned; i++)
                {
                    minfo[i] = (MONITOR_INFO_2)Marshal.PtrToStructure(currentMonitor, typeof(MONITOR_INFO_2));
                    currentMonitor = (IntPtr)(currentMonitor.ToInt32() + Marshal.SizeOf(typeof(MONITOR_INFO_2)));

                }
                Marshal.FreeHGlobal(pMonitors);

                List<String> result = new List<string>();
                for (int i = 0; i < pcReturned; i++)
                    result.Add(minfo[i].pName);
                return result;
            }

            throw new Win32Exception(Marshal.GetLastWin32Error());
        }

        public void InstallMonitor(String monitorName, String pathToMonitorDLL)
        {
            MONITOR_INFO_2 mi2;
            mi2.pName = monitorName;
            mi2.pEnvironment = null;
            mi2.pDLLName = pathToMonitorDLL;

            if (AddMonitor(null, 2, ref mi2) == 0)
            {
                int errno = Marshal.GetLastWin32Error();

                //3006 == Monitor bereits installiert!
                if (errno != 3006)
                    throw new Win32Exception(errno);
                //else
                MessageBox.Show("Could not install monitor. Monitor already installed!");
            }
        }

        public void UninstallMonitor(String monitorName)
        {
            //Wenn Monitor gelöscht wird, dann auch automatisch dazugehöriger Port!

            if (DeleteMonitor(null, null, monitorName) == false)
            {
                int errno = Marshal.GetLastWin32Error();

                //3000 == Monitor ist nicht installiert!
                if (errno != 3000)
                    throw new Win32Exception(errno);
                //else
                MessageBox.Show("Could not remove Monitor. Monitor not installed!");
            }
        }
        

        public List<String> GetInstalledPorts()
        {
            //Alternative: prnport.vbs, 

            uint pcbNeeded = 0;
            uint pcReturned = 0;

            if (EnumPorts(null, 2, IntPtr.Zero, 0, ref pcbNeeded, ref pcReturned))
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
            if (EnumPorts(null, 2, pPorts, pcbNeeded, ref pcbNeeded, ref pcReturned))
            {
                IntPtr currentPort = pPorts;
                PORT_INFO_2[] pinfo = new PORT_INFO_2[pcReturned];



                for (int i = 0; i < pcReturned; i++)
                {
                    pinfo[i] = (PORT_INFO_2)Marshal.PtrToStructure(currentPort, typeof(PORT_INFO_2));
                    currentPort = (IntPtr)(currentPort.ToInt32() + Marshal.SizeOf(typeof(PORT_INFO_2)));

                }
                Marshal.FreeHGlobal(pPorts);

                List<String> result = new List<string>();
                for (int i = 0; i < pcReturned; i++)
                    result.Add(pinfo[i].pPortName);
                return result;
            }

            throw new Win32Exception(Marshal.GetLastWin32Error());
        }

        public void InstallPort(String printerPortName, String monitorName, String redmonCommand, String printerName)
        {
            //Über die Registry, weil die Api einen Benutzereingriff erfordert: AddPort() / ConfigurePort()
            try
            {
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
            catch (Exception ex)
            {
                throw new Exception(ex.Message);
            }
        }

        public void UninstallPort(String printerPortName)
        {
            if (DeletePort(null, 0, printerPortName) == false)
            {
                int errno = Marshal.GetLastWin32Error();
                throw new Win32Exception(errno);
            }
        }
        

        public List<String> GetInstalledPrintersManaged()
        {
            return PrinterSettings.InstalledPrinters.Cast<string>().ToList();
        }

        public List<String> GetInstalledPrintersUnmanaged()
        {
            //siehe http://msdn.microsoft.com/en-us/library/dd162692(v=vs.85).aspx

            uint cbNeeded = 0;
            uint cReturned = 0;
            if (EnumPrinters(PrinterEnumFlags.PRINTER_ENUM_LOCAL, null, 2, IntPtr.Zero, 0, ref cbNeeded, ref cReturned))
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
            if (EnumPrinters(PrinterEnumFlags.PRINTER_ENUM_LOCAL, null, 2, pAddr, cbNeeded, ref cbNeeded, ref cReturned))
            {
                PRINTER_INFO_2[] printerInfo2 = new PRINTER_INFO_2[cReturned];
                int offset = pAddr.ToInt32();
                Type type = typeof(PRINTER_INFO_2);
                int increment = Marshal.SizeOf(type);
                for (int i = 0; i < cReturned; i++)
                {
                    printerInfo2[i] = (PRINTER_INFO_2)Marshal.PtrToStructure(new IntPtr(offset), type);
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

        public void InstallPrinter(String printerName, String printerPortName, String printerDriverName)
        {
            PRINTER_INFO_2 pInfo = new PRINTER_INFO_2
                                       {
                                           pPrinterName = printerName,
                                           pPortName = printerPortName,
                                           pDriverName = printerDriverName,
                                           pPrintProcessor = "WinPrint",
                                           pShareName = printerName,
                                           pDatatype = "RAW",
                                           Priority = 1,
                                           DefaultPriority = 1,
                                       };
            

            IntPtr hPrt = AddPrinter("", 2, ref pInfo);
            if (hPrt == IntPtr.Zero)
            {
                int errno = Marshal.GetLastWin32Error();
                throw new Win32Exception(errno);
            }

            if (ClosePrinter(hPrt) == false)
            {
                int errno = Marshal.GetLastWin32Error();
                throw new Win32Exception(errno);
            }
        }
        
        public void UninstallPrinter(String printerName)
        {
            IntPtr hPrinter;
            PRINTER_DEFAULTS defaults = new PRINTER_DEFAULTS
                                            {
                                                //PRINTER_ALL_ACCESS
                                                DesiredAccess = 0x000F000C,
                                                pDatatype = IntPtr.Zero,
                                                pDevMode = IntPtr.Zero
                                            };


            if (OpenPrinter(printerName, out hPrinter, ref defaults) == false)
            {
                int errno = Marshal.GetLastWin32Error();
                throw new Win32Exception(errno);
            }

            if (hPrinter == IntPtr.Zero)
            {
                int errno = Marshal.GetLastWin32Error();
                throw new Win32Exception(errno);
            }

            if (DeletePrinter(hPrinter) == false)
            {
                int errno = Marshal.GetLastWin32Error();
                throw new Win32Exception(errno);
            }

            if (ClosePrinter(hPrinter) == false)
            {
                int errno = Marshal.GetLastWin32Error();
                throw new Win32Exception(errno);
            }
        }

        public void RemoveJobs(String printerName)
        {
            IntPtr hPrinter;
            PRINTER_DEFAULTS defaults = new PRINTER_DEFAULTS
            {
                //PRINTER_ALL_ACCESS
                DesiredAccess = 0x000F000C,
                pDatatype = IntPtr.Zero,
                pDevMode = IntPtr.Zero
            };


            if (OpenPrinter(printerName, out hPrinter, ref defaults) == false)
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
            if (SetPrinter(hPrinter, 0, IntPtr.Zero, 3) == false)
            {
                int errno = Marshal.GetLastWin32Error();
                throw new Win32Exception(errno);
            }

            if (ClosePrinter(hPrinter) == false)
            {
                int errno = Marshal.GetLastWin32Error();
                throw new Win32Exception(errno);
            }
        }


        List<String> GetInstalledPrinterDrivers()
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
            if (EnumPrinterDrivers(null, null, 2, IntPtr.Zero, 0, ref cbNeeded, ref cReturned))
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
            if (EnumPrinterDrivers(null, null, 2, pAddr, cbNeeded, ref cbNeeded, ref cReturned))
            {
                DRIVER_INFO_2[] printerInfo2 = new DRIVER_INFO_2[cReturned];
                int offset = pAddr.ToInt32();
                Type type = typeof(DRIVER_INFO_2);
                int increment = Marshal.SizeOf(type);
                for (int i = 0; i < cReturned; i++)
                {
                    printerInfo2[i] = (DRIVER_INFO_2)Marshal.PtrToStructure(new IntPtr(offset), type);
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
        
        public void InstallPrinterDriver(String printerDriverName, String appPath)
        {
            string parameter = @" /ia /m " + printerDriverName + @" /f ""C:\Users\pr0m3d\Dropbox\Projects\Printput\Printput\gs9.02\lib\printputpdf.inf""";
            
            int res = PrintUIEntryW(IntPtr.Zero, IntPtr.Zero, parameter, 0);
            if (res != 0)
            {
                int lastWin32Error = Marshal.GetLastWin32Error();
                throw new Win32Exception(lastWin32Error);
            }
        }

        public void UninstallPrinterDriver(String printerDriverName)
        {
            if (DeletePrinterDriver(null, null, printerDriverName) == false)
            {
                int errno = Marshal.GetLastWin32Error();
                throw new Win32Exception(errno);
            }
        }


        private void InstallMonitorBtnClick(object sender, EventArgs e)
        {
            try
            {
                const string pathToMonitorDLL = @"C:\Users\pr0m3d\Dropbox\Projects\Printput\Printput\RedMon\redmonnt.dll";

                List<String> installedMonitors = GetInstalledMonitors();

                if (installedMonitors == null)
                    throw new Exception("Could not get installed Monitors!");

                if (installedMonitors.Contains(MonitorName) == false)
                    InstallMonitor(MonitorName, pathToMonitorDLL);
                else throw new Exception("Monitor already installed!");
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void UnInstallMonitorBtnClick(object sender, EventArgs e)
        {
            try
            {
                List<String> installedMonitors = GetInstalledMonitors();

                if (installedMonitors  == null)
                    throw new Exception("Could not get installed Ports!");
                
                if (installedMonitors.Contains(MonitorName))
                    UninstallMonitor(MonitorName);
                else throw new Exception("Monitor not installed!");
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }


        private void InstallPortBtnClick(object sender, EventArgs e)
        {
            try
            {
                List<String> installedPorts = GetInstalledPorts();

                if (installedPorts == null)
                    throw new Exception("Could not get installed Ports!");

                if (installedPorts.Contains(PrinterPortName) == false)
                    InstallPort(PrinterPortName, MonitorName, "...", PrinterName);
                else throw new Exception("Port already installed!");
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }
        
        private void UnInstallPortBtnClick(object sender, EventArgs e)
        {
            try
            {
                List<String> installedPorts = GetInstalledPorts();
                if (installedPorts == null)
                    throw new Exception("Could not get installed Ports!");

               if (installedPorts.Contains(PrinterPortName))
                    UninstallPort(PrinterPortName);
                else throw new Exception("Port not installed!");
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }


        private void InstallPrinterBtnClick(object sender, EventArgs e)
        {
            try
            {
                List<String> printerList = GetInstalledPrintersManaged();

                if (printerList.Contains(PrinterName) == false)
                {
                    //Damit die Veränderung der Ports bemerkt wird??? oO
                    GetInstalledPorts();

                    InstallPrinter(PrinterName, PrinterPortName, PrinterDriverName);
                }
                else throw new Exception("Printer already installed!");
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }   

        }

        private void UnInstallPrinterBtnClick(object sender, EventArgs e)
        {
            try
            {
                List<String> printerList = GetInstalledPrintersManaged();

                if (printerList.Contains(PrinterName))
                {
                    RemoveJobs(PrinterName);
                    UninstallPrinter(PrinterName);
                }
                else throw new Exception("Printer not installed!");
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }


        private void InstallPrinterDriverBtnClick(object sender, EventArgs e)
        {
            try
            {
                List<String> installedPrinterDrivers = GetInstalledPrinterDrivers();
                if (installedPrinterDrivers == null)
                    throw new Exception("Could not get installed PrinterDrivers!");

                if (installedPrinterDrivers.Contains(PrinterDriverName) == false)
                {
                    InstallPrinterDriver(PrinterDriverName, "appPath");
                }
                else throw new Exception("Driver already installed!");
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void UnInstallPrinterDriverBtnClick(object sender, EventArgs e)
        {
            try
            {
                List<String> installedPrinterDrivers = GetInstalledPrinterDrivers();
                if (installedPrinterDrivers == null)
                    throw new Exception("Could not get installed PrinterDrivers!");

                if (installedPrinterDrivers.Contains(PrinterDriverName))
                {
                    UninstallPrinterDriver(PrinterDriverName);
                }
                else throw new Exception("Driver not installed!");
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

       

        

        
    }
}
