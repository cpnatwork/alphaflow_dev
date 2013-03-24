using System;
using System.Windows.Forms;

namespace PrintPutDialog
{

    static class Program
    {
        
        private static String GetFileFromPipe()
        {
            return Console.In.ReadToEnd();
        }


        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        /// <ToDo>
        /// SplashScreen/WaitDialog
        /// Logging
        /// </ToDo>
        [STAThread]
        static void Main(String[] args)
        {
            try
            {
                String fileName = String.Empty;

                if (args.Length == 0)
                {
                    Application.EnableVisualStyles();
                    Application.SetCompatibleTextRenderingDefault(false);
                    Application.Run(new SettingsDialog());
                }
                else if ((args.Length > 0) && (args[0] == "-"))
                {
                    for (int i = 1; i < args.Length; i++)
                        fileName = fileName + " " + args[i];
                    fileName = fileName.Trim();

                    String psFile = GetFileFromPipe();
                    if (psFile == null)
                        throw new Exception("GetFileFromPipe failed");

                    Application.EnableVisualStyles();
                    Application.SetCompatibleTextRenderingDefault(false);
                    Application.Run(new PrintPutDialog(psFile, fileName));
                }
                else
                {
                    MessageBox.Show("Wrong parameters!");
                    return;
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }
    }
}
