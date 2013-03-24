namespace PrintPutDialog
{
    partial class SettingsDialog
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.alphaCardType_btn = new System.Windows.Forms.Button();
            this.defaultSettings_btn = new System.Windows.Forms.Button();
            this.ownSettings_btn = new System.Windows.Forms.Button();
            this.alphaCardType_cb = new System.Windows.Forms.ComboBox();
            this.ownSettings_tb = new System.Windows.Forms.TextBox();
            this.folderBrowser = new System.Windows.Forms.FolderBrowserDialog();
            this.label2 = new System.Windows.Forms.Label();
            this.defaultSettings_rb = new System.Windows.Forms.RadioButton();
            this.institutionID_btn = new System.Windows.Forms.Button();
            this.ownSettings_rb = new System.Windows.Forms.RadioButton();
            this.actorID_btn = new System.Windows.Forms.Button();
            this.tabPage2 = new System.Windows.Forms.TabPage();
            this.roleID_btn = new System.Windows.Forms.Button();
            this.tabPage1 = new System.Windows.Forms.TabPage();
            this.label1 = new System.Windows.Forms.Label();
            this.institutionID_tb = new System.Windows.Forms.TextBox();
            this.roleID_tb = new System.Windows.Forms.TextBox();
            this.actorID_tb = new System.Windows.Forms.TextBox();
            this.label5 = new System.Windows.Forms.Label();
            this.label4 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.pdfConversion_tab = new System.Windows.Forms.TabControl();
            this.close_btn = new System.Windows.Forms.Button();
            this.openFileDialog = new System.Windows.Forms.OpenFileDialog();
            this.defaultSettings_tb = new System.Windows.Forms.TextBox();
            this.tabPage3 = new System.Windows.Forms.TabPage();
            this.javaPath_btn = new System.Windows.Forms.Button();
            this.javaPath_tb = new System.Windows.Forms.TextBox();
            this.label8 = new System.Windows.Forms.Label();
            this.tabPage2.SuspendLayout();
            this.tabPage1.SuspendLayout();
            this.pdfConversion_tab.SuspendLayout();
            this.tabPage3.SuspendLayout();
            this.SuspendLayout();
            // 
            // alphaCardType_btn
            // 
            this.alphaCardType_btn.Location = new System.Drawing.Point(401, 147);
            this.alphaCardType_btn.Name = "alphaCardType_btn";
            this.alphaCardType_btn.Size = new System.Drawing.Size(85, 21);
            this.alphaCardType_btn.TabIndex = 54;
            this.alphaCardType_btn.Text = "Set As Default";
            this.alphaCardType_btn.UseVisualStyleBackColor = true;
            this.alphaCardType_btn.Click += new System.EventHandler(this.AlphaCardTypeBtnClick);
            // 
            // defaultSettings_btn
            // 
            this.defaultSettings_btn.Location = new System.Drawing.Point(402, 18);
            this.defaultSettings_btn.Name = "defaultSettings_btn";
            this.defaultSettings_btn.Size = new System.Drawing.Size(85, 21);
            this.defaultSettings_btn.TabIndex = 65;
            this.defaultSettings_btn.Text = "Set As Default";
            this.defaultSettings_btn.UseVisualStyleBackColor = true;
            this.defaultSettings_btn.Click += new System.EventHandler(this.DefaultSettingsBtnClick);
            // 
            // ownSettings_btn
            // 
            this.ownSettings_btn.Location = new System.Drawing.Point(402, 59);
            this.ownSettings_btn.Name = "ownSettings_btn";
            this.ownSettings_btn.Size = new System.Drawing.Size(85, 21);
            this.ownSettings_btn.TabIndex = 64;
            this.ownSettings_btn.Text = "Set As Default";
            this.ownSettings_btn.UseVisualStyleBackColor = true;
            this.ownSettings_btn.Click += new System.EventHandler(this.OwnSettingsBtnClick);
            // 
            // alphaCardType_cb
            // 
            this.alphaCardType_cb.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.Suggest;
            this.alphaCardType_cb.AutoCompleteSource = System.Windows.Forms.AutoCompleteSource.ListItems;
            this.alphaCardType_cb.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.alphaCardType_cb.FormattingEnabled = true;
            this.alphaCardType_cb.Items.AddRange(new object[] {
            "",
            "DOCUMENTATION",
            "REFERRAL_VOUCHER",
            "RESULTS_REPORT"});
            this.alphaCardType_cb.Location = new System.Drawing.Point(123, 148);
            this.alphaCardType_cb.Name = "alphaCardType_cb";
            this.alphaCardType_cb.Size = new System.Drawing.Size(262, 21);
            this.alphaCardType_cb.TabIndex = 53;
            // 
            // ownSettings_tb
            // 
            this.ownSettings_tb.Location = new System.Drawing.Point(121, 61);
            this.ownSettings_tb.Name = "ownSettings_tb";
            this.ownSettings_tb.Size = new System.Drawing.Size(262, 20);
            this.ownSettings_tb.TabIndex = 63;
            this.ownSettings_tb.Text = "-dQUIET -sOutputFile=- -sDEVICE=pdfwrite -";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(16, 151);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(86, 13);
            this.label2.TabIndex = 52;
            this.label2.Text = "AlphaCard Type:";
            // 
            // defaultSettings_rb
            // 
            this.defaultSettings_rb.AutoSize = true;
            this.defaultSettings_rb.Checked = true;
            this.defaultSettings_rb.Location = new System.Drawing.Point(16, 19);
            this.defaultSettings_rb.Name = "defaultSettings_rb";
            this.defaultSettings_rb.Size = new System.Drawing.Size(100, 17);
            this.defaultSettings_rb.TabIndex = 0;
            this.defaultSettings_rb.TabStop = true;
            this.defaultSettings_rb.Text = "Default Settings";
            this.defaultSettings_rb.UseVisualStyleBackColor = true;
            // 
            // institutionID_btn
            // 
            this.institutionID_btn.Location = new System.Drawing.Point(401, 120);
            this.institutionID_btn.Name = "institutionID_btn";
            this.institutionID_btn.Size = new System.Drawing.Size(85, 21);
            this.institutionID_btn.TabIndex = 49;
            this.institutionID_btn.Text = "Set As Default";
            this.institutionID_btn.UseVisualStyleBackColor = true;
            this.institutionID_btn.Click += new System.EventHandler(this.InstitutionIdBtnClick);
            // 
            // ownSettings_rb
            // 
            this.ownSettings_rb.AutoSize = true;
            this.ownSettings_rb.Location = new System.Drawing.Point(16, 61);
            this.ownSettings_rb.Name = "ownSettings_rb";
            this.ownSettings_rb.Size = new System.Drawing.Size(88, 17);
            this.ownSettings_rb.TabIndex = 1;
            this.ownSettings_rb.TabStop = true;
            this.ownSettings_rb.Text = "Own Settings";
            this.ownSettings_rb.UseVisualStyleBackColor = true;
            // 
            // actorID_btn
            // 
            this.actorID_btn.Location = new System.Drawing.Point(401, 66);
            this.actorID_btn.Name = "actorID_btn";
            this.actorID_btn.Size = new System.Drawing.Size(85, 21);
            this.actorID_btn.TabIndex = 47;
            this.actorID_btn.Text = "Set As Default";
            this.actorID_btn.UseVisualStyleBackColor = true;
            this.actorID_btn.Click += new System.EventHandler(this.ActorIdBtnClick);
            // 
            // tabPage2
            // 
            this.tabPage2.Controls.Add(this.defaultSettings_tb);
            this.tabPage2.Controls.Add(this.defaultSettings_btn);
            this.tabPage2.Controls.Add(this.ownSettings_btn);
            this.tabPage2.Controls.Add(this.ownSettings_tb);
            this.tabPage2.Controls.Add(this.ownSettings_rb);
            this.tabPage2.Controls.Add(this.defaultSettings_rb);
            this.tabPage2.Location = new System.Drawing.Point(4, 22);
            this.tabPage2.Name = "tabPage2";
            this.tabPage2.Padding = new System.Windows.Forms.Padding(3);
            this.tabPage2.Size = new System.Drawing.Size(500, 202);
            this.tabPage2.TabIndex = 1;
            this.tabPage2.Text = "PDF-Conversion-Settings";
            this.tabPage2.UseVisualStyleBackColor = true;
            // 
            // roleID_btn
            // 
            this.roleID_btn.Location = new System.Drawing.Point(401, 93);
            this.roleID_btn.Name = "roleID_btn";
            this.roleID_btn.Size = new System.Drawing.Size(85, 21);
            this.roleID_btn.TabIndex = 48;
            this.roleID_btn.Text = "Set As Default";
            this.roleID_btn.UseVisualStyleBackColor = true;
            this.roleID_btn.Click += new System.EventHandler(this.RoleIdBtnClick);
            // 
            // tabPage1
            // 
            this.tabPage1.Controls.Add(this.label1);
            this.tabPage1.Controls.Add(this.alphaCardType_btn);
            this.tabPage1.Controls.Add(this.alphaCardType_cb);
            this.tabPage1.Controls.Add(this.label2);
            this.tabPage1.Controls.Add(this.institutionID_btn);
            this.tabPage1.Controls.Add(this.roleID_btn);
            this.tabPage1.Controls.Add(this.actorID_btn);
            this.tabPage1.Controls.Add(this.institutionID_tb);
            this.tabPage1.Controls.Add(this.roleID_tb);
            this.tabPage1.Controls.Add(this.actorID_tb);
            this.tabPage1.Controls.Add(this.label5);
            this.tabPage1.Controls.Add(this.label4);
            this.tabPage1.Controls.Add(this.label3);
            this.tabPage1.Location = new System.Drawing.Point(4, 22);
            this.tabPage1.Name = "tabPage1";
            this.tabPage1.Padding = new System.Windows.Forms.Padding(3);
            this.tabPage1.Size = new System.Drawing.Size(500, 202);
            this.tabPage1.TabIndex = 0;
            this.tabPage1.Text = "α-Card-Settings";
            this.tabPage1.UseVisualStyleBackColor = true;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label1.Location = new System.Drawing.Point(16, 24);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(167, 16);
            this.label1.TabIndex = 64;
            this.label1.Text = "Default Settings Dialog";
            // 
            // institutionID_tb
            // 
            this.institutionID_tb.Location = new System.Drawing.Point(123, 121);
            this.institutionID_tb.Name = "institutionID_tb";
            this.institutionID_tb.Size = new System.Drawing.Size(262, 20);
            this.institutionID_tb.TabIndex = 44;
            // 
            // roleID_tb
            // 
            this.roleID_tb.Location = new System.Drawing.Point(123, 94);
            this.roleID_tb.Name = "roleID_tb";
            this.roleID_tb.Size = new System.Drawing.Size(262, 20);
            this.roleID_tb.TabIndex = 43;
            // 
            // actorID_tb
            // 
            this.actorID_tb.Location = new System.Drawing.Point(123, 67);
            this.actorID_tb.Name = "actorID_tb";
            this.actorID_tb.Size = new System.Drawing.Size(262, 20);
            this.actorID_tb.TabIndex = 42;
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Location = new System.Drawing.Point(16, 124);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(69, 13);
            this.label5.TabIndex = 39;
            this.label5.Text = "Institution ID:";
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(16, 97);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(46, 13);
            this.label4.TabIndex = 38;
            this.label4.Text = "Role ID:";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(16, 70);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(49, 13);
            this.label3.TabIndex = 37;
            this.label3.Text = "Actor ID:";
            // 
            // pdfConversion_tab
            // 
            this.pdfConversion_tab.Controls.Add(this.tabPage1);
            this.pdfConversion_tab.Controls.Add(this.tabPage2);
            this.pdfConversion_tab.Controls.Add(this.tabPage3);
            this.pdfConversion_tab.Location = new System.Drawing.Point(2, 2);
            this.pdfConversion_tab.Name = "pdfConversion_tab";
            this.pdfConversion_tab.SelectedIndex = 0;
            this.pdfConversion_tab.Size = new System.Drawing.Size(508, 228);
            this.pdfConversion_tab.TabIndex = 39;
            // 
            // close_btn
            // 
            this.close_btn.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.close_btn.Location = new System.Drawing.Point(385, 236);
            this.close_btn.Name = "close_btn";
            this.close_btn.Size = new System.Drawing.Size(121, 23);
            this.close_btn.TabIndex = 38;
            this.close_btn.Text = "Close";
            this.close_btn.UseVisualStyleBackColor = true;
            this.close_btn.Click += new System.EventHandler(this.CloseBtnClick);
            // 
            // openFileDialog
            // 
            this.openFileDialog.Filter = "α-Docs|*.jar";
            // 
            // defaultSettings_tb
            // 
            this.defaultSettings_tb.Location = new System.Drawing.Point(121, 19);
            this.defaultSettings_tb.Name = "defaultSettings_tb";
            this.defaultSettings_tb.ReadOnly = true;
            this.defaultSettings_tb.Size = new System.Drawing.Size(262, 20);
            this.defaultSettings_tb.TabIndex = 67;
            this.defaultSettings_tb.Text = "-dQUIET -sOutputFile=- -sDEVICE=pdfwrite -";
            // 
            // tabPage3
            // 
            this.tabPage3.Controls.Add(this.javaPath_btn);
            this.tabPage3.Controls.Add(this.javaPath_tb);
            this.tabPage3.Controls.Add(this.label8);
            this.tabPage3.Location = new System.Drawing.Point(4, 22);
            this.tabPage3.Name = "tabPage3";
            this.tabPage3.Size = new System.Drawing.Size(500, 202);
            this.tabPage3.TabIndex = 2;
            this.tabPage3.Text = "Java Configuration";
            this.tabPage3.UseVisualStyleBackColor = true;
            // 
            // javaPath_btn
            // 
            this.javaPath_btn.Location = new System.Drawing.Point(400, 16);
            this.javaPath_btn.Name = "javaPath_btn";
            this.javaPath_btn.Size = new System.Drawing.Size(85, 23);
            this.javaPath_btn.TabIndex = 69;
            this.javaPath_btn.Text = "...";
            this.javaPath_btn.UseVisualStyleBackColor = true;
            this.javaPath_btn.Click += new System.EventHandler(this.JavaPathBtnClick);
            // 
            // javaPath_tb
            // 
            this.javaPath_tb.Location = new System.Drawing.Point(122, 19);
            this.javaPath_tb.Name = "javaPath_tb";
            this.javaPath_tb.Size = new System.Drawing.Size(262, 20);
            this.javaPath_tb.TabIndex = 68;
            // 
            // label8
            // 
            this.label8.AutoSize = true;
            this.label8.Location = new System.Drawing.Point(15, 20);
            this.label8.Name = "label8";
            this.label8.Size = new System.Drawing.Size(58, 13);
            this.label8.TabIndex = 67;
            this.label8.Text = "Java Path:";
            // 
            // SettingsDialog
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(509, 262);
            this.Controls.Add(this.pdfConversion_tab);
            this.Controls.Add(this.close_btn);
            this.Name = "SettingsDialog";
            this.Text = "SettingsDialog";
            this.Load += new System.EventHandler(this.SettingsDialogLoad);
            this.tabPage2.ResumeLayout(false);
            this.tabPage2.PerformLayout();
            this.tabPage1.ResumeLayout(false);
            this.tabPage1.PerformLayout();
            this.pdfConversion_tab.ResumeLayout(false);
            this.tabPage3.ResumeLayout(false);
            this.tabPage3.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button alphaCardType_btn;
        private System.Windows.Forms.Button defaultSettings_btn;
        private System.Windows.Forms.Button ownSettings_btn;
        private System.Windows.Forms.ComboBox alphaCardType_cb;
        private System.Windows.Forms.TextBox ownSettings_tb;
        private System.Windows.Forms.FolderBrowserDialog folderBrowser;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.RadioButton defaultSettings_rb;
        private System.Windows.Forms.Button institutionID_btn;
        private System.Windows.Forms.RadioButton ownSettings_rb;
        private System.Windows.Forms.Button actorID_btn;
        private System.Windows.Forms.TabPage tabPage2;
        private System.Windows.Forms.Button roleID_btn;
        private System.Windows.Forms.TabPage tabPage1;
        private System.Windows.Forms.TextBox institutionID_tb;
        private System.Windows.Forms.TextBox roleID_tb;
        private System.Windows.Forms.TextBox actorID_tb;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.TabControl pdfConversion_tab;
        private System.Windows.Forms.Button close_btn;
        private System.Windows.Forms.OpenFileDialog openFileDialog;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.TextBox defaultSettings_tb;
        private System.Windows.Forms.TabPage tabPage3;
        private System.Windows.Forms.Button javaPath_btn;
        private System.Windows.Forms.TextBox javaPath_tb;
        private System.Windows.Forms.Label label8;
    }
}