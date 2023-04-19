using System;
using System.Net.Sockets;

class Client
{
    public void Connect()
    {
        // Create a TcpClient object and connect to a server
        TcpClient client = new TcpClient();
        client.Connect("127.0.0.1", 8710);

        // Get the network stream for the TcpClient
        NetworkStream stream = client.GetStream();

        // Send data to the server
        byte[] data = System.Text.Encoding.ASCII.GetBytes("Click button");
        stream.Write(data, 0, data.Length);

        // Close the TcpClient and network stream
        stream.Close();
        client.Close();
    }
}