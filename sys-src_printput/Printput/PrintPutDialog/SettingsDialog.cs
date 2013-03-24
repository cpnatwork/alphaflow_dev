using System;
using System.IO;
using System.Windows.Forms;

namespace PrintPutDialog
{
    public partial class SettingsDialog : Form
    {
        public SettingsDialog()
        {
            InitializeComponent();
        }

        private void CloseBtnClick(object sender, EventArgs e)
        {
            Close();
        }

        private void ActorIdBtnClick(object sender, EventArgs e)
        {
            RegUtil.SetUserSettings("ActorID", actorID_tb.Text);
        }

        private void RoleIdBtnClick(object sender, EventArgs e)
        {
            RegUtil.SetUserSettings("RoleID", roleID_tb.Text);
        }

        private void InstitutionIdBtnClick(object sender, EventArgs e)
        {
            RegUtil.SetUserSettings("InstitutionID", institutionID_tb.Text);
        }

        private void AlphaCardTypeBtnClick(object sender, EventArgs e)
        {
            RegUtil.SetUserSettings("AlphaCardType", alphaCardType_cb.Text);
        }

        private void DefaultSettingsBtnClick(object sender, EventArgs e)
        {
            RegUtil.SetUserSettings("DefaultConversion", "Default");
        }

        private void OwnSettingsBtnClick(object sender, EventArgs e)
        {
            RegUtil.SetUserSettings("DefaultConversion", ownSettings_tb.Text);
        }

        private void SettingsDialogLoad(object sender, EventArgs e)
        {
            LoadDefaultValues();
        }

        private void LoadDefaultValues()
        {
            String actor = RegUtil.GetUserSettings("ActorID");
            if (actor == "")
                actor = Environment.UserName;

            actorID_tb.Text = actor;

            roleID_tb.Text = RegUtil.GetUserSettings("RoleID");
            institutionID_tb.Text = RegUtil.GetUserSettings("InstitutionID");

            alphaCardType_cb.Text = RegUtil.GetUserSettings("AlphaCardType");

            javaPath_tb.Text = RegUtil.GetJavaInstallationPath();

            String conversion = RegUtil.GetUserSettings("DefaultConversion");
            if (conversion == "Default")
            {
                ownSettings_rb.Checked = false;
                defaultSettings_rb.Checked = true;
            }
            else
            {
                defaultSettings_rb.Checked = false;
                ownSettings_rb.Checked = true;
                ownSettings_tb.Text = conversion;
            }
        }

        private void JavaPathBtnClick(object sender, EventArgs e)
        {
            folderBrowser.Description = "Please choose the jre installation path.";
            DialogResult objResult = folderBrowser.ShowDialog(this);
            if (objResult == DialogResult.OK)
            {
                String selectedPath = folderBrowser.SelectedPath;

                string javaPath = Path.Combine(selectedPath, "bin\\java.exe");
                if (File.Exists(javaPath) == false)
                {
                    MessageBox.Show("Incorrect java path!");
                }
                else
                {
                    javaPath_tb.Text = selectedPath;
                    RegUtil.SetUserSettings("JAVA_HOME", selectedPath);
                }
            }
        }
    }
}
