using System;
using System.Diagnostics;
using System.IO;
using System.Text;
using System.Windows.Forms;

namespace PrintPutDialog
{
    public partial class PrintPutDialog : Form
    {
        private readonly String _pdfFileName;
        private readonly String _psFile;

        public PrintPutDialog(String psFile, String fileName)
        {
            _psFile = psFile;
            _pdfFileName = fileName;
            InitializeComponent();
        }              
        
        

        private void FormLoad(object sender, EventArgs e)
        {
            LoadDefaultValues();
        }

        private void LoadDefaultValues()
        {
            alphaCardTitle_tb.Text = _pdfFileName;

            String actor = RegUtil.GetUserSettings("ActorID");
            if (actor == "")
                actor = Environment.UserName;

            actorID_tb.Text = actor;

            roleID_tb.Text = RegUtil.GetUserSettings("RoleID");
            institutionID_tb.Text = RegUtil.GetUserSettings("InstitutionID");
          
            alphaCardType_cb.Text = RegUtil.GetUserSettings("AlphaCardType");

            javaPath_tb.Text = RegUtil.GetJavaInstallationPath();

            String conversion = RegUtil.GetUserSettings("DefaultConversion");
            if ((conversion == "Default") || (conversion == String.Empty))
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


        private void SaveBtnClick(object sender, EventArgs e)
        {
            try
            {
                SaveFileDialog saveFileDialog = new SaveFileDialog
                {
                    Filter = "PDF Documents|*.pdf",
                    Title = "Save PDF File",
                    FileName = _pdfFileName + ".pdf"
                };


                // If the file name is not an empty string open it for saving.
                if (saveFileDialog.ShowDialog() == DialogResult.OK)
                {

                    String arg = GetConversionArgs();
                    String pdfFile = PostScriptConverter.PostscriptToPdf(_psFile, arg);

                    Stream myStream = saveFileDialog.OpenFile();
                    {
                        StreamWriter sw = new StreamWriter(myStream, Encoding.Default);
                        sw.Write(pdfFile);
                        sw.Close();

                        MessageBox.Show("Done!");
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }
        
        private void SavePsBtnClick(object sender, EventArgs e)
        {
            try
            {
                SaveFileDialog saveFileDialog = new SaveFileDialog
                {
                    Filter = "PS Documents|*.ps",
                    Title = "Save PS File",
                    FileName = _pdfFileName + ".ps"
                };


                // If the file name is not an empty string open it for saving.
                if (saveFileDialog.ShowDialog() == DialogResult.OK)
                {
                    Stream myStream = saveFileDialog.OpenFile();
                    {
                        StreamWriter sw = new StreamWriter(myStream);
                        sw.Write(_psFile);
                        sw.Close();

                        MessageBox.Show("Done!");
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
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

        private void IntegrateBtnClick(object sender, EventArgs e)
        {
            if (alphaCardType_cb.Text == "")
            {
                MessageBox.Show("Please chose a \"AlphaCard Type\"!");
                return;
            }

            string javaPath = Path.Combine(javaPath_tb.Text, "bin\\java.exe");
            if (File.Exists(javaPath) == false)
            {
                MessageBox.Show("Incorrect java path!");
                return;
            }

            String aDoc;

            openFileDialog.Title = "Please choose the α-Doc";
            DialogResult objResult = openFileDialog.ShowDialog();
            if (objResult == DialogResult.OK)
                aDoc = openFileDialog.FileName;
            else
                return;

            String arg = GetConversionArgs();
            String pdfFile = PostScriptConverter.PostscriptToPdf(_psFile, arg);

            String guidTempFile = Path.GetTempPath() + Guid.NewGuid().ToString() + ".pdf";
            
            StreamWriter wStream = new StreamWriter(guidTempFile, false, Encoding.Default);
            wStream.Write(pdfFile);
            wStream.Close();


            int result = RunJavaWithArgs(javaPath, aDoc, guidTempFile);
            if (result == 0)
                MessageBox.Show("Done!");

            Close();
        }

        private string GetConversionArgs()
        {
            if (defaultSettings_rb.Checked)
                return defaultSettings_tb.Text;
            
            return ownSettings_tb.Text;
        }

        private int RunJavaWithArgs(string javaPath, String aDoc, string payloadPath)
        {
            String arg = CombineJavaArgs() + "-jar"+ " " + aDoc + " " + payloadPath;
           

            var processInfo = new ProcessStartInfo(javaPath, arg)
            {
                CreateNoWindow = true,
                UseShellExecute = false
            };

            Process proc;

            if ((proc = Process.Start(processInfo)) == null)
            {
                throw new InvalidOperationException("??");
            }

            proc.WaitForExit();
            int exitCode = proc.ExitCode;
            proc.Close();

            return exitCode;
        }

        private string CombineJavaArgs()
        {
            String result = String.Empty;

            if (silent_cb.Checked)
                result = result + "-Dsilent=\"true\" ";
            
            
            result = result + "-DalphaCardTitle=\"" + alphaCardTitle_tb.Text + "\" ";
            result = result + "-DactorID=\"" + actorID_tb.Text + "\" ";
            result = result + "-DroleID=\"" + roleID_tb.Text + "\" ";
            result = result + "-DinstitutionID=\"" + institutionID_tb.Text + "\" ";
            result = result + "-DalphaCardType=\"" + alphaCardType_cb.Text + "\" ";

            if (cardID_cb.Checked)
                result = result + "-DcardID=\"" + cardID_tb.Text + "\" ";
            

            return result;
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

        private void DefaultSettingsBtnClick(object sender, EventArgs e)
        {
            RegUtil.SetUserSettings("DefaultConversion", "Default");
        }

        private void OwnSettingsBtnClick(object sender, EventArgs e)
        {
            RegUtil.SetUserSettings("DefaultConversion", ownSettings_tb.Text);
        }
        
        private void CardIdCheckBoxCheckedChanged(object sender, EventArgs e)
        {
            if (cardID_cb.Checked)
                cardID_tb.Enabled = true;
            else
                cardID_tb.Enabled = false;
        }

       
        
    }
}
