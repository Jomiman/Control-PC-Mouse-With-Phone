package com.example.pcconnection;

import java.io.OutputStream;
import java.net.Socket;

public class SocketSingelton {
    Socket pcSocket;

    private static SocketSingelton instance = null;
    private SocketSingelton() { }

    public static SocketSingelton GetInstance() {
        if (instance == null) {
            instance = new SocketSingelton();
        }
        return instance;
    }

    public Socket GetSocket() {
        return pcSocket;
    }
    public void SetSocket(Socket socket) {
        pcSocket = socket;
    }
}
