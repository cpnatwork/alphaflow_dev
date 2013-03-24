using System;
using System.IO;
using Microsoft.Win32;

namespace Printput
{    
    class Program
    {
        private static void WriteLog(String line)
        {
            StreamWriter sw = new StreamWriter(Environment.GetEnvironmentVariable("TEMP") + "\\mainlog.txt", true);

            sw.Write(line + Environment.NewLine);
            sw.Close();
        }

        private static String GetInstalledPathFromRegistry()
        {
            /*
               //http://www.rhyous.com/2011/01/24/how-read-the-64-bit-registry-from-a-32-bit-application-or-vice-versa/
                RegistryKey localMachine;
                if (EnvironmentCheck.Is64BitOperatingSystem)
                    localMachine = RegistryKey.OpenBaseKey(RegistryHive.LocalMachine, RegistryView.Registry64);
                else
                    localMachine = RegistryKey.OpenBaseKey(RegistryHive.LocalMachine, RegistryView.Registry32);


                RegistryKey myKey = localMachine.OpenSubKey(@"Software\PrintPut", false);
                if (myKey == null)
            */

            //get the name of the application to launch from registry (HKLM is for all users, restricted user can only read!)
            //http://robseder.wordpress.com/2009/04/25/cas-and-how-to-properly-elevate-with-vista-uac/
            String softwareRegistryPath;
            if (EnvironmentCheck.Is64BitOperatingSystem)
                softwareRegistryPath = @"Software\Wow6432Node\PrintPut";
            else
                softwareRegistryPath = @"Software\PrintPut";


            RegistryKey myKey = Registry.LocalMachine.OpenSubKey(softwareRegistryPath, false);
            if (myKey == null)
            {
                WriteLog("Could not find registry-entry!");
                return null;
            }

            string result = myKey.GetValue("Path").ToString();

            return result;
        }

        private static String GetPostScriptFile()
        {
            //PS-Datei aus dem Input-Pipe lesen 
            String pipedFile = Console.In.ReadToEnd();
            return pipedFile;
        }

        static void Main(string[] args)
        {
            string guiName = PrintPut.Properties.Settings.Default.GUIName;
           

            String installPath = GetInstalledPathFromRegistry();
            if (installPath == null)
            {
                WriteLog("getInstalledPathFromRegistry failed");
                return;
            }
            
            //Das "-" signalisiert, es wird in den stdin geschrieben
            String applicationName = installPath + guiName + " -";


            String postScriptFile = GetPostScriptFile();            
            
            
            //launch the application            
            ApplicationLoader.StartProcessAndBypassUAC(applicationName, args, postScriptFile);
            
        }     
    }
}
