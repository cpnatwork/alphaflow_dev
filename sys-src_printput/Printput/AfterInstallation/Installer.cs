using System;
using System.Collections;
using System.ComponentModel;
using System.Configuration.Install;


namespace InstallationOverride
{
    [RunInstaller(true)]
    public partial class OverrideInstaller : Installer
    {


        public OverrideInstaller()
        {
            InitializeComponent();
        }

        
        [System.Security.Permissions.SecurityPermission(System.Security.Permissions.SecurityAction.Demand)]
        public override void Install(IDictionary stateSaver)
        {
            base.Install(stateSaver);

            //String applicationPath = getAppPathFromRegistry();
            if (Context.Parameters["TargetDir"] == null)
                throw new Exception("Custom Data: TargetDir not set");

            String applicationPath = Context.Parameters["TargetDir"].Substring(0, Context.Parameters["TargetDir"].Length - 1);

            PrinterMonitorInstaller installer = new PrinterMonitorInstaller(applicationPath);
            try
            {
                installer.InstallAll();
            }
            catch (Win32Exception ex)
            {
                throw new InstallException(ex.Message);
            }
        }

        //private String getAppPathFromRegistry()
        //{
        //    String result = null;

        //    RegistryKey myKey = Registry.LocalMachine.OpenSubKey(@"Software\PrintPut", false);
        //    if (myKey == null)
        //    {
        //        String error = "Could not find registry key." + Environment.NewLine + " Installation aborted!";
        //        MessageBox.Show(error);
        //        throw new InstallException(error);
        //    }

        //    result = myKey.GetValue("Path").ToString();
        //    if (result == null)
        //    {
        //        String error = "Could not find registry key." + Environment.NewLine + " Installation aborted!";
        //        MessageBox.Show(error);
        //        throw new InstallException(error);
        //    }

        //    return result;
        //}

        [System.Security.Permissions.SecurityPermission(System.Security.Permissions.SecurityAction.Demand)]
        public override void Commit(IDictionary savedState)
        {
            base.Commit(savedState);
        }

        [System.Security.Permissions.SecurityPermission(System.Security.Permissions.SecurityAction.Demand)]
        public override void Rollback(IDictionary savedState)
        {
            base.Rollback(savedState);
        }

        [System.Security.Permissions.SecurityPermission(System.Security.Permissions.SecurityAction.Demand)]
        public override void Uninstall(IDictionary savedState)
        {
            if (Context.Parameters["TargetDir"] == null)
                throw new Exception("Custom Data: TargetDir not set");

            String applicationPath = Context.Parameters["TargetDir"].Substring(0, Context.Parameters["TargetDir"].Length - 1);

            
            PrinterMonitorInstaller installer = new PrinterMonitorInstaller(applicationPath);
            try
            {


                installer.UninstallAll();
            }
            catch (Win32Exception ex)
            {
                throw new InstallException(ex.Message);
            }
            

            base.Uninstall(savedState);
        }


    }
}
