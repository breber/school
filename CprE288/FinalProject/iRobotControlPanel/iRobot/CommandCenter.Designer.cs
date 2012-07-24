namespace iRobot
{
    partial class CommandCenter
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
            this.OpenPortButton = new System.Windows.Forms.Button();
            this.ScanButton = new System.Windows.Forms.Button();
            this.ForwardButton = new System.Windows.Forms.Button();
            this.BackwardButton = new System.Windows.Forms.Button();
            this.ConsoleBox = new System.Windows.Forms.TextBox();
            this.Left45Button = new System.Windows.Forms.Button();
            this.Right45Button = new System.Windows.Forms.Button();
            this.Left10Button = new System.Windows.Forms.Button();
            this.Right10Button = new System.Windows.Forms.Button();
            this.EmergencyStopButton = new System.Windows.Forms.Button();
            this.textBox2 = new System.Windows.Forms.TextBox();
            this.RetrievalButton = new System.Windows.Forms.Button();
            this.ThereButton = new System.Windows.Forms.Button();
            this.ClosePortButton = new System.Windows.Forms.Button();
            this.portNameLabel = new System.Windows.Forms.Label();
            this.CommandsLabel = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // OpenPortButton
            // 
            this.OpenPortButton.Location = new System.Drawing.Point(12, 54);
            this.OpenPortButton.Name = "OpenPortButton";
            this.OpenPortButton.Size = new System.Drawing.Size(100, 23);
            this.OpenPortButton.TabIndex = 0;
            this.OpenPortButton.Text = "Open Port";
            this.OpenPortButton.UseVisualStyleBackColor = true;
            this.OpenPortButton.Click += new System.EventHandler(this.OpenPortButton_Click);
            // 
            // ScanButton
            // 
            this.ScanButton.Location = new System.Drawing.Point(12, 148);
            this.ScanButton.Name = "ScanButton";
            this.ScanButton.Size = new System.Drawing.Size(100, 23);
            this.ScanButton.TabIndex = 1;
            this.ScanButton.Text = "Scan";
            this.ScanButton.UseVisualStyleBackColor = true;
            this.ScanButton.Click += new System.EventHandler(this.ScanButton_Click);
            // 
            // ForwardButton
            // 
            this.ForwardButton.Location = new System.Drawing.Point(12, 177);
            this.ForwardButton.Name = "ForwardButton";
            this.ForwardButton.Size = new System.Drawing.Size(100, 23);
            this.ForwardButton.TabIndex = 2;
            this.ForwardButton.Text = "Move Forward";
            this.ForwardButton.UseVisualStyleBackColor = true;
            this.ForwardButton.Click += new System.EventHandler(this.ForwardButton_Click);
            // 
            // BackwardButton
            // 
            this.BackwardButton.Location = new System.Drawing.Point(12, 206);
            this.BackwardButton.Name = "BackwardButton";
            this.BackwardButton.Size = new System.Drawing.Size(100, 23);
            this.BackwardButton.TabIndex = 3;
            this.BackwardButton.Text = "Move Backward";
            this.BackwardButton.UseVisualStyleBackColor = true;
            this.BackwardButton.Click += new System.EventHandler(this.BackwardButton_Click);
            // 
            // ConsoleBox
            // 
            this.ConsoleBox.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.ConsoleBox.Location = new System.Drawing.Point(118, 12);
            this.ConsoleBox.Multiline = true;
            this.ConsoleBox.Name = "ConsoleBox";
            this.ConsoleBox.ReadOnly = true;
            this.ConsoleBox.Size = new System.Drawing.Size(729, 480);
            this.ConsoleBox.TabIndex = 4;
            // 
            // Left45Button
            // 
            this.Left45Button.Location = new System.Drawing.Point(12, 235);
            this.Left45Button.Name = "Left45Button";
            this.Left45Button.Size = new System.Drawing.Size(100, 23);
            this.Left45Button.TabIndex = 5;
            this.Left45Button.Text = "Turn Left45";
            this.Left45Button.UseVisualStyleBackColor = true;
            this.Left45Button.Click += new System.EventHandler(this.Left45Button_Click);
            // 
            // Right45Button
            // 
            this.Right45Button.Location = new System.Drawing.Point(12, 264);
            this.Right45Button.Name = "Right45Button";
            this.Right45Button.Size = new System.Drawing.Size(100, 23);
            this.Right45Button.TabIndex = 6;
            this.Right45Button.Text = "Turn Right45";
            this.Right45Button.UseVisualStyleBackColor = true;
            this.Right45Button.Click += new System.EventHandler(this.Right45Button_Click);
            // 
            // Left10Button
            // 
            this.Left10Button.Location = new System.Drawing.Point(12, 293);
            this.Left10Button.Name = "Left10Button";
            this.Left10Button.Size = new System.Drawing.Size(100, 23);
            this.Left10Button.TabIndex = 7;
            this.Left10Button.Text = "Turn Left10";
            this.Left10Button.UseVisualStyleBackColor = true;
            this.Left10Button.Click += new System.EventHandler(this.Left10Button_Click);
            // 
            // Right10Button
            // 
            this.Right10Button.Location = new System.Drawing.Point(12, 322);
            this.Right10Button.Name = "Right10Button";
            this.Right10Button.Size = new System.Drawing.Size(100, 23);
            this.Right10Button.TabIndex = 8;
            this.Right10Button.Text = "Turn Right10";
            this.Right10Button.UseVisualStyleBackColor = true;
            this.Right10Button.Click += new System.EventHandler(this.Right10Button_Click);
            // 
            // EmergencyStopButton
            // 
            this.EmergencyStopButton.Location = new System.Drawing.Point(11, 410);
            this.EmergencyStopButton.Name = "EmergencyStopButton";
            this.EmergencyStopButton.Size = new System.Drawing.Size(100, 23);
            this.EmergencyStopButton.TabIndex = 9;
            this.EmergencyStopButton.Text = "STOP!";
            this.EmergencyStopButton.UseVisualStyleBackColor = true;
            this.EmergencyStopButton.Click += new System.EventHandler(this.EmergencyStopButton_Click);
            // 
            // textBox2
            // 
            this.textBox2.Location = new System.Drawing.Point(12, 29);
            this.textBox2.Name = "textBox2";
            this.textBox2.Size = new System.Drawing.Size(100, 20);
            this.textBox2.TabIndex = 10;
            // 
            // RetrievalButton
            // 
            this.RetrievalButton.Location = new System.Drawing.Point(12, 351);
            this.RetrievalButton.Name = "RetrievalButton";
            this.RetrievalButton.Size = new System.Drawing.Size(99, 23);
            this.RetrievalButton.TabIndex = 12;
            this.RetrievalButton.Text = "Retrieval";
            this.RetrievalButton.UseVisualStyleBackColor = true;
            this.RetrievalButton.Click += new System.EventHandler(this.RetrievalButton_Click);
            // 
            // ThereButton
            // 
            this.ThereButton.Location = new System.Drawing.Point(12, 381);
            this.ThereButton.Name = "ThereButton";
            this.ThereButton.Size = new System.Drawing.Size(99, 23);
            this.ThereButton.TabIndex = 13;
            this.ThereButton.Text = "We\'re There!";
            this.ThereButton.UseVisualStyleBackColor = true;
            this.ThereButton.Click += new System.EventHandler(this.ThereButton_Click);
            // 
            // ClosePortButton
            // 
            this.ClosePortButton.Location = new System.Drawing.Point(12, 83);
            this.ClosePortButton.Name = "ClosePortButton";
            this.ClosePortButton.Size = new System.Drawing.Size(99, 23);
            this.ClosePortButton.TabIndex = 14;
            this.ClosePortButton.Text = "Close Port";
            this.ClosePortButton.UseVisualStyleBackColor = true;
            this.ClosePortButton.Click += new System.EventHandler(this.ClosePortButton_Click);
            // 
            // portNameLabel
            // 
            this.portNameLabel.AutoSize = true;
            this.portNameLabel.Location = new System.Drawing.Point(13, 12);
            this.portNameLabel.Name = "portNameLabel";
            this.portNameLabel.Size = new System.Drawing.Size(60, 13);
            this.portNameLabel.TabIndex = 15;
            this.portNameLabel.Text = "Port Name:";
            // 
            // CommandsLabel
            // 
            this.CommandsLabel.AutoSize = true;
            this.CommandsLabel.Location = new System.Drawing.Point(12, 129);
            this.CommandsLabel.Name = "CommandsLabel";
            this.CommandsLabel.Size = new System.Drawing.Size(62, 13);
            this.CommandsLabel.TabIndex = 16;
            this.CommandsLabel.Text = "Commands:";
            // 
            // CommandCenter
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(859, 504);
            this.Controls.Add(this.CommandsLabel);
            this.Controls.Add(this.portNameLabel);
            this.Controls.Add(this.ClosePortButton);
            this.Controls.Add(this.ThereButton);
            this.Controls.Add(this.RetrievalButton);
            this.Controls.Add(this.textBox2);
            this.Controls.Add(this.EmergencyStopButton);
            this.Controls.Add(this.Right10Button);
            this.Controls.Add(this.Left10Button);
            this.Controls.Add(this.Right45Button);
            this.Controls.Add(this.Left45Button);
            this.Controls.Add(this.ConsoleBox);
            this.Controls.Add(this.BackwardButton);
            this.Controls.Add(this.ForwardButton);
            this.Controls.Add(this.ScanButton);
            this.Controls.Add(this.OpenPortButton);
            this.Name = "CommandCenter";
            this.Text = "iRobot Movement Control Panel";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button OpenPortButton;
        private System.Windows.Forms.Button ScanButton;
        private System.Windows.Forms.Button ForwardButton;
        private System.Windows.Forms.Button BackwardButton;
        private System.Windows.Forms.TextBox ConsoleBox;
        private System.Windows.Forms.Button Left45Button;
        private System.Windows.Forms.Button Right45Button;
        private System.Windows.Forms.Button Left10Button;
        private System.Windows.Forms.Button Right10Button;
        private System.Windows.Forms.Button EmergencyStopButton;
        private System.Windows.Forms.TextBox textBox2;
        private System.Windows.Forms.Button RetrievalButton;
        private System.Windows.Forms.Button ThereButton;
        private System.Windows.Forms.Button ClosePortButton;
        private System.Windows.Forms.Label portNameLabel;
        private System.Windows.Forms.Label CommandsLabel;
    }
}

