using System;
using Microsoft.Win32;

namespace PrintPutDialog
{
    class RegUtil
    {
        public static string GetUserSettings(string key)
        {
            const string softwareRegistryPath = @"Software\PrintPut";

            RegistryKey myKey = Registry.CurrentUser.OpenSubKey(softwareRegistryPath, false);
            if (myKey == null)
            {
                throw new Exception("Could not find registry-entry!");
            }


            Object result = myKey.GetValue(key) ?? "";


            return result.ToString();
        }

        public static string GetGlobalSettings(string key)
        {
            String softwareRegistryPath;
            if (EnvironmentCheck.Is64BitOperatingSystem)
                softwareRegistryPath = @"Software\Wow6432Node\PrintPut";
            else
                softwareRegistryPath = @"Software\PrintPut";


            RegistryKey myKey = Registry.LocalMachine.OpenSubKey(softwareRegistryPath, false);
            if (myKey == null)
            {
                throw new Exception("Could not find registry-entry!");
            }

            Object result = myKey.GetValue(key) ?? String.Empty;

            return result.ToString();
        }


        public static void SetUserSettings(string key, string value)
        {
            const string softwareRegistryPath = @"Software\PrintPut";

            RegistryKey myKey = Registry.CurrentUser.OpenSubKey(softwareRegistryPath, true);
            if (myKey == null)
            {
                throw new Exception("Could not find registry-entry!");
            }

            myKey.SetValue(key, value);
        }


        public static string GetJavaInstallationPath()
        {
            string result = GetUserSettings("JAVA_HOME");
            if (!string.IsNullOrEmpty(result))
            {
                return result;
            }

            result = Environment.GetEnvironmentVariable("JAVA_HOME");

            if (!string.IsNullOrEmpty(result))
            {
                return result;
            }
           
            const string javaKey = "SOFTWARE\\JavaSoft\\Java Runtime Environment\\";
            using (RegistryKey rk = Registry.LocalMachine.OpenSubKey(javaKey, false))
            {
                if (rk == null)
                    result = "";
                else
                {
                    string currentVersion = rk.GetValue("CurrentVersion").ToString();
                    using (RegistryKey key = rk.OpenSubKey(currentVersion, false))
                    {
                        if (key == null)
                            result = "";
                        else
                            result = key.GetValue("JavaHome").ToString();
                    }
                }
            }

            return result;
        }


        //needs admin-privileges
        //public static void SetGlobalSettings(string key, string value)
        //{
        //    String softwareRegistryPath;
        //    if (EnvironmentCheck.Is64BitOperatingSystem)
        //        softwareRegistryPath = @"Software\Wow6432Node\PrintPut";
        //    else
        //        softwareRegistryPath = @"Software\PrintPut";


        //    RegistryKey myKey = Registry.LocalMachine.OpenSubKey(softwareRegistryPath, true);
        //    if (myKey == null)
        //    {
        //        throw new Exception("Could not find registry-entry!");
        //    }

        //    myKey.SetValue(key, value);
        //}


    }
}
