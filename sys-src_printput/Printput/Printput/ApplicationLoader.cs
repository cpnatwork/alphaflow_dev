using System;
using System.Runtime.InteropServices;
using System.IO;
using Microsoft.Win32.SafeHandles;
using System.Threading;
using System.Text;


namespace Printput
{    
    /// <summary>
    /// Class to launch a application and bypass UAC!
    /// </summary>
    /// <ToDo>
    /// Einheitliche Sprache
    /// Einheitliches Logging-System 
    /// </ToDo>
    class ApplicationLoader
    {

        private static void WriteLog(String line)
        {
            StreamWriter sw = new StreamWriter(Environment.GetEnvironmentVariable("TEMP") + "\\ApplicationLoaderLog.txt", true);

            sw.Write(line + Environment.NewLine);
            sw.Close();
        }

        

    private static void LogEnvironmentVariables(String applicationName, String[] args)
    {        
            //Environment-Variablen wird nur gesetzt, wenn von Redmon aufgerufen! 
            String redmonUser = Environment.GetEnvironmentVariable("REDMON_USER");
            String redmonDocname = Environment.GetEnvironmentVariable("REDMON_DOCNAME");
            String redmonJob = Environment.GetEnvironmentVariable("REDMON_JOB");
            String redmonMachine = Environment.GetEnvironmentVariable("REDMON_MACHINE");
            String redmonPrinter = Environment.GetEnvironmentVariable("REDMON_PRINTER");
            String redmonPort = Environment.GetEnvironmentVariable("REDMON_PORT");
            String redmonFilename = Path.GetFileNameWithoutExtension(Environment.GetEnvironmentVariable("REDMON_DOCNAME"));
            String redmonSessionid = Environment.GetEnvironmentVariable("REDMON_SESSIONID");

            WriteLog("REDMON_SESSIONID=" + redmonSessionid);
            for (int i = 0; i < args.Length; i++)
                WriteLog("Parameter" + i.ToString() + "=" + args[i]);
            WriteLog("REDMON_USER=" + redmonUser);
            WriteLog("REDMON_DOCNAME=" + redmonDocname);
            WriteLog("REDMON_JOB=" + redmonJob);
            WriteLog("REDMON_MACHINE=" + redmonMachine);
            WriteLog("REDMON_PRINTER=" + redmonPrinter);
            WriteLog("REDMON_PORT=" + redmonPort);
            WriteLog("REDMON_FILENAME=" + redmonFilename);


            String commandLine = applicationName + " " + redmonFilename;
            WriteLog("CommandLine:" + Environment.NewLine + commandLine);
    }


        /// <summary>
        /// Launches the given application with full admin rights, and in addition bypasses the Vista UAC prompt
        /// </summary>
        /// <param name="applicationName">The name of the application to launch</param>
        /// <param name="args">Übergebene Argumente</param>
        /// <param name="postScriptFile">Postscript-Data</param>
        /// <returns>true if successful</returns>
        public static bool StartProcessAndBypassUAC(String applicationName, string[] args, String postScriptFile)
        {
            LogEnvironmentVariables(applicationName, args);

            String commandLine = applicationName + " " + Path.GetFileNameWithoutExtension(Environment.GetEnvironmentVariable("REDMON_DOCNAME"));
            String redmonSessionid = Environment.GetEnvironmentVariable("REDMON_SESSIONID");

            uint dwSessionId = Convert.ToUInt32(redmonSessionid);
            

            WriteLog("Get User token...");
            IntPtr hPToken;
            
            //Klappt nur bei Ausführung durch System/Service => Schlecht zu debuggen
            if (!ApplicationLoaderPInvoke.WTSQueryUserToken(dwSessionId, out hPToken))
                return false;
            

            WriteLog("Setting security attributes...");

            // Security attibute structure used in DuplicateTokenEx and CreateProcessAsUser
            // I would prefer to not have to use a security attribute variable and to just 
            // simply pass null and inherit (by default) the security attributes
            // of the existing token. However, in C# structures are value types and therefore
            // cannot be assigned the null value.
            ApplicationLoaderPInvoke.SECURITY_ATTRIBUTES sa = new ApplicationLoaderPInvoke.SECURITY_ATTRIBUTES
                                                                  {bInheritHandle = true};
            sa.Length = Marshal.SizeOf(sa);


            SafeFileHandle stdInR;
            SafeFileHandle stdInW;
            SafeFileHandle stdOutR;
            SafeFileHandle stdOutW;

            WriteLog("Creating pipes...");
            //Pipe erzeugen
            if (!ApplicationLoaderPInvoke.CreatePipe(out stdInR, out stdInW, ref sa, 0))
                return false;
           
            if (!ApplicationLoaderPInvoke.CreatePipe(out stdOutR, out stdOutW, ref sa, 0))
                return false;


            WriteLog("SetHandleInformation on pipes...");
            // Ensure the write handle to the pipe for STDIN is not inherited. 
            if (!ApplicationLoaderPInvoke.SetHandleInformation(stdInW.DangerousGetHandle(), ApplicationLoaderPInvoke.HANDLE_FLAGS.INHERIT, 0))
                return false;

            WriteLog("SetHandleInformation on pipe...");
            if (!ApplicationLoaderPInvoke.SetHandleInformation(stdOutR.DangerousGetHandle(), ApplicationLoaderPInvoke.HANDLE_FLAGS.INHERIT, 0))
                return false;


            WriteLog("Create primary token...");
            IntPtr hUserTokenDup = IntPtr.Zero;            
            // impersonation session token isn’t good enough for CreateProcessAsUser... => use DuplicateTokenEx to convert an impersonation token into a primary token.
            if (!ApplicationLoaderPInvoke.DuplicateTokenEx(hPToken, ApplicationLoaderPInvoke.MAXIMUM_ALLOWED, ref sa, (int)ApplicationLoaderPInvoke.SECURITY_IMPERSONATION_LEVEL.SecurityIdentification, (int)ApplicationLoaderPInvoke.TOKEN_TYPE.TokenPrimary, ref hUserTokenDup))
            {                
                ApplicationLoaderPInvoke.CloseHandle(hPToken);
                return false;
            }

            WriteLog("Create environment...");
            //Environment aufbauen, um auf Temp und HKCU zugreifen zu können oder Umgebungsvariablen im fremden Prozess setzen zu können
            IntPtr env = IntPtr.Zero;
            if (!ApplicationLoaderPInvoke.CreateEnvironmentBlock(ref env, hUserTokenDup, false))
            {
                ApplicationLoaderPInvoke.CloseHandle(hPToken);
                return false;
            }



            // By default CreateProcessAsUser creates a process on a non-interactive window station, meaning
            // the window station has a desktop that is invisible and the process is incapable of receiving
            // user input. To remedy this we set the lpDesktop parameter to indicate we want to enable user 
            // interaction with the new process.        
            ApplicationLoaderPInvoke.STARTUPINFO startupInfo = new ApplicationLoaderPInvoke.STARTUPINFO();
            startupInfo.cb = Marshal.SizeOf(startupInfo);                               
            startupInfo.lpDesktop = @"winsta0\default"; // interactive window station parameter; basically this indicates that the process created can display a GUI on the desktop

            // initialize safe handles, sonst klappt die Prozesserzeugung nicht
            startupInfo.dwFlags = ApplicationLoaderPInvoke.STARTF_USESTDHANDLES;
            startupInfo.hStdInput = stdInR;
            startupInfo.hStdOutput = stdOutW;
            startupInfo.hStdError = stdOutW;

                                                                                            
                                           
            // flags that specify the priority and creation method of the process
            // CREATE_UNICODE_ENVIRONMENT nötig wenn Environment aufgebaut werden soll!            
            const int dwCreationFlags = ApplicationLoaderPInvoke.CREATE_UNICODE_ENVIRONMENT | ApplicationLoaderPInvoke.NORMAL_PRIORITY_CLASS | ApplicationLoaderPInvoke.CREATE_NEW_CONSOLE | ApplicationLoaderPInvoke.CREATE_DEFAULT_ERROR_MODE;


            WriteLog("Create process...");

            ApplicationLoaderPInvoke.PROCESS_INFORMATION procInfo;
            
            // create a new process in the user's logon session
            bool result = ApplicationLoaderPInvoke.CreateProcessAsUser(
                                            hUserTokenDup,          // client's access token
                                            null,                   // file to execute
                                            commandLine,            // command line
                                            ref sa,                 // pointer to process SECURITY_ATTRIBUTES
                                            ref sa,                 // pointer to thread SECURITY_ATTRIBUTES
                                            true,                   // handles are inherited
                                            dwCreationFlags,        // creation flags
                                            env,                    // pointer to new environment block 
                                            null,                   // name of current directory 
                                            ref startupInfo,        // pointer to STARTUPINFO structure
                                            out procInfo            // receives information about new process
                                            );

            if (result)
            {
                WriteLog("CreateProcessAsUser successful");
            }
            else
            {
                WriteLog("CreateProcessAsUser failed");
            }


            byte[] binaryString = Encoding.Default.GetBytes(postScriptFile);
            uint written;
            NativeOverlapped no = new NativeOverlapped();
            bool bSuccess = ApplicationLoaderPInvoke.WriteFile(stdInW.DangerousGetHandle(), binaryString, (uint)binaryString.Length, out written, ref no);
            if (!bSuccess)
            {
                WriteLog("Writing in stdin failed");
                return false;
            }
            WriteLog("Writing in stdin successful");


            stdInW.Close();          

            WriteLog("Close Handles...");
                     
            // invalidate the handles
            if (hPToken != IntPtr.Zero)
                ApplicationLoaderPInvoke.CloseHandle(hPToken);

            if (hUserTokenDup != IntPtr.Zero)
                ApplicationLoaderPInvoke.CloseHandle(hUserTokenDup);

            WriteLog("Close Handles successfull...");

            return result;
        }
    }
}