using System;
using System.Text;
using System.Windows.Forms;
using System.IO;
using System.Threading;
using System.Diagnostics;
using Microsoft.Win32;

namespace PrintPutDialog
{
    class PostScriptConverter
    {
        private static String GetAppPathFromRegistry()
        {
            String result = null;

            RegistryKey myKey = Registry.LocalMachine.OpenSubKey(@"Software\PrintPut", false);
            if (myKey == null)
            {
                String error = "Could not find registry key." + Environment.NewLine + " Installation aborted!";
                MessageBox.Show(error);
            }


            if (myKey != null)
            {
                result = myKey.GetValue("Path").ToString();
            }

            return result;
        }

        public static string PostscriptToPdf(String postscript, String arg)
        {
            String result = String.Empty;

            try
            {
                String appPath = GetAppPathFromRegistry();

                var psInfo = new ProcessStartInfo
                {
                    FileName = appPath + @"gs9.02\bin\gswin32c.exe",
                    Arguments = arg,
                    //Arguments = @"-dQUIET -sDEVICE=pdfwrite -o - -_",
                    WindowStyle = ProcessWindowStyle.Hidden,
                    UseShellExecute = false,

                    RedirectStandardInput = true,
                    RedirectStandardOutput = true,
                    RedirectStandardError = false,
                    StandardOutputEncoding = Encoding.Default,
                    CreateNoWindow = true,
                };


                Process process = Process.Start(psInfo);

                ThreadClass tc = new ThreadClass(process);
                Thread t = new Thread(tc.Start);
                t.Start();


                StreamWriter sw = process.StandardInput;
                sw.Write(postscript);
                sw.Flush();
                sw.Close();

                t.Join();

                result = tc.Result;
                //Hängt sich auf
                //string stderr = process.StandardError.ReadToEnd();
                //if (stderr != string.Empty)
                //    throw new System.InvalidOperationException(stderr);


                //process.WaitForExit();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }

            return result;
        }
        
    }
}
