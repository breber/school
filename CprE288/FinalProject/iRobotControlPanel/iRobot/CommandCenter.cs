using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Threading;
using System.IO.Ports;

namespace iRobot
{
    public partial class CommandCenter : Form
    {
        /// <summary>
        /// The port to read and write to
        /// </summary>
        private static SerialPort port;

        /// <summary>
        /// The user interface. Used on separate threads
        /// </summary>
        private static CommandCenter singleton;

        /// <summary>
        /// The thread used to retrieve data from the port
        /// </summary>
        private Thread thread;

        /// <summary>
        /// Creates a new CommandCenter
        /// </summary>
        public CommandCenter()
        {
            singleton = this;
            InitializeComponent();
        }

        /// <summary>
        /// Reads from the Port and updates the ConsoleBox on the UI
        /// </summary>
        public static void Read()
        {
            while (port.IsOpen)
            {
                try
                {
                    CommandCenter.singleton.ConsoleBox.AppendText(port.ReadLine());
                }
                catch (TimeoutException) { }
            }
        }

        /// <summary>
        /// Initializes the port and starts the thread that reads data from the port
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void OpenPortButton_Click(object sender, EventArgs e)
        {
            port = new SerialPort(textBox2.Text);
            port.BaudRate = 57600;
            port.DataBits = 8;
            port.Parity = Parity.None;
            port.StopBits = StopBits.Two;
            port.Open();
            ConsoleBox.AppendText("Port opened\n");

            thread = new Thread(Read);
            thread.Name = "Robot";
            thread.Start();
        }

        /// <summary>
        /// Joins the UI thread and the port reading thread, and closes the port
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void ClosePortButton_Click(object sender, EventArgs e)
        {
            thread.Join();
            port.Close();
        }

        /// <summary>
        /// Performs a scan of the IR and Sonar sensors
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void ScanButton_Click(object sender, EventArgs e)
        {
            if (port.IsOpen)
            {
                port.Write("1");
            }
        }

        /// <summary>
        /// Moves the robot forward 10 centimeters
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void ForwardButton_Click(object sender, EventArgs e)
        {
            if (port.IsOpen)
            {
                port.Write("w");
            }
        }

        /// <summary>
        /// Moves the robot backward 5 centimeters
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void BackwardButton_Click(object sender, EventArgs e)
        {
            if (port.IsOpen)
            {
                port.Write("s");
            }
        }

        /// <summary>
        /// Rotates the robot left 45 degrees
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void Left45Button_Click(object sender, EventArgs e)
        {
            if (port.IsOpen)
            {
                port.Write("a");
            }
        }

        /// <summary>
        /// Rotates the robot right 45 degrees
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void Right45Button_Click(object sender, EventArgs e)
        {
            if (port.IsOpen)
            {
                port.Write("d");
            }
        }

        /// <summary>
        /// Rotates the robot left 10 degrees
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void Left10Button_Click(object sender, EventArgs e)
        {
            if (port.IsOpen)
            {
                port.Write("z");
            }
        }

        /// <summary>
        /// Rotates the robot right 10 degrees
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void Right10Button_Click(object sender, EventArgs e)
        {
            if (port.IsOpen)
            {
                port.Write("c");
            }
        }

        /// <summary>
        /// Moves the robot straight forward into the retrieval zone
        /// and flashes the LEDs to indicate it is ready for retrieval
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void RetrievalButton_Click(object sender, EventArgs e)
        {
            if (port.IsOpen)
            {
                port.Write("r");
            }
        }

        /// <summary>
        /// Flashes the LEDs to indicate it is ready for retrieval
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void ThereButton_Click(object sender, EventArgs e)
        {
            if (port.IsOpen)
            {
                port.Write("p");
            }
        }

        /// <summary>
        /// Stops the robot immediately
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void EmergencyStopButton_Click(object sender, EventArgs e)
        {
            if (port.IsOpen)
            {
                port.Write(new byte[] { 11 }, 0, 1);
            }
        }
    }
}
