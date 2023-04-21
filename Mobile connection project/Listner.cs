using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Runtime.InteropServices;
using System.Text;
using System.Globalization;
using System.IO;
using System.Windows.Forms;

class Program
{
    [DllImport("user32.dll")]
    static extern void mouse_event(uint dwFlags, uint dx, uint dy, uint dwData, int dwExtraInfo);
    [DllImport("user32.dll")]
    static extern bool SetCursorPos(int x, int y);

    static void Main(string[] args)
    {
        // Start the TCP server on a new thread
        Thread serverThread = new Thread(new ThreadStart(StartServer));
        serverThread.Start();

        // Do other operations in the main thread while the server is running
        Console.WriteLine("Press Ctrl + C to exit");
    }

    static void StartServer()
    {
        // Create a TcpListener object and start listening for incoming connections
        TcpListener server = new TcpListener(IPAddress.Any, 8710);
        server.Start();
        Console.WriteLine("Server started");

        while (true)
        {
            // Wait for a client to connect
            TcpClient client = server.AcceptTcpClient();
            try {
                Thread handleClient = new Thread(() => HandleClient(client));
                handleClient.Start();
            } catch (IOException e) {
                Console.WriteLine("Error: " + e);
            }
        }
    }

    static void HandleClient(TcpClient client) {
        Console.WriteLine("Client connected");
        // Get the network stream for the client
        NetworkStream stream = client.GetStream();

        while (true) {
            try {
                // Receive data from the client
                byte[] data = new byte[1024];
                int bytesRead = stream.Read(data, 0, data.Length);
                string request = System.Text.Encoding.ASCII.GetString(data, 0, bytesRead);

                if (request != "") Console.WriteLine("Received: {0}", request);

            switch (request) {
                case "Click button":
                    // Simulate a left mouse button down event
                    mouse_event(0x0002, 0, 0, 0, 0);

                    // Simulate a left mouse button up event
                    mouse_event(0x0004, 0, 0, 0, 0);
                    break;

                case "Arrow right":
                    SendKeys.SendWait("{RIGHT}");
                    break;

                case "Arrow left":
                    SendKeys.SendWait("{LEFT}");
                    break;
            }

            if (request == "Disconnect") break;

            if (request.Contains(',')) {
                string[] parts = request.Split(',');
                Console.WriteLine("Parts: " + parts[0] + ", " + parts[1]);
                float x = float.Parse(parts[0], CultureInfo.InvariantCulture.NumberFormat);
                float y = float.Parse(parts[1], CultureInfo.InvariantCulture.NumberFormat);
                
                byte[] outputData = System.Text.Encoding.ASCII.GetBytes("Done parsing");
                stream.Write(outputData, 0, outputData.Length);

                float screenWidth = 1920;
                float screenHeight = 1080;
                float phoneScreenWidth = 1080/1.3f;
                float phoneScreenHeight = 2340/2;

                float widthMultiplier = screenWidth/phoneScreenWidth;
                float heightMultiplier = screenHeight/phoneScreenHeight;

                int mouseX = (int)(widthMultiplier * x - 400);
                int mouseY = (int)(heightMultiplier * y - screenHeight/2 - 200);

                //int mouseX = (int)(x * screenWidth);
                //int mouseY = (int)(y * screenHeight);

                SetCursorPos(mouseX, mouseY);
                Console.WriteLine("Mouse coords: " + mouseX + ", " + mouseY);
            }
            } catch (IOException e) {
                Console.WriteLine("Error: " + e);
                break;
            }

        }
        client.Close();
        Console.WriteLine("Client disconnected");
    }
}
