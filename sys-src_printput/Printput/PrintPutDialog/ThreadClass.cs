using System;
using System.Windows.Forms;
using System.Diagnostics;

namespace PrintPutDialog
{
    class ThreadClass
    {
        private readonly Process _process;
        public string Result;

        /*
        private static void WriteLog(String line)
        {
            StreamWriter sw = new StreamWriter(Environment.GetEnvironmentVariable("TEMP") + "\\mainlog.txt", true);

            sw.Write(line + Environment.NewLine);
            sw.Close();
        }
        */


        public ThreadClass(Process process)
        {
            _process = process;
        }

        public void Start()
        {
            String result = GetStdOut();
            if (result != null)
                Result = result;
        }



        private String GetStdOut()
        {
            String result = null;
            try
            {
                result = _process.StandardOutput.ReadToEnd();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
            return result;
        }
    }
}
