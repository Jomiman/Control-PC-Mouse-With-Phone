package com.example.pcconnection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.net.Socket;

public class ArrowKeys extends AppCompatActivity {
    SocketSingelton socket = SocketSingelton.GetInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrow_keys);

        ConnectToPC();
        ArrowRight();
        ArrowLeft();
        SwitchToMainView();
    }

    void ConnectToPC() {
        Button connectButton = findViewById(R.id.connect_button);
        connectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Connecting");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Connect to the TCP server
                        String serverAddress = "192.168.97.152"; // replace with your computer's IP address
                        int serverPort = 8710; // replace with the port number of your TCP server
                        try {
                            socket.SetSocket(new Socket(serverAddress, serverPort));
                            Log.d("MyApp", "Connected to server");

                        } catch (IOException e) {
                            Log.e("MyApp", "Error connecting to server", e);
                        }
                    }
                }).start();
            }
        });
    }

    void ArrowRight() {
        Button clickButton = findViewById(R.id.arrow_right);
        clickButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (socket.GetSocket() != null) {
                    System.out.println("Arrow Right");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // Send a message to the server
                            String message = "Arrow right";
                            try {
                                socket.GetSocket().getOutputStream().write(message.getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });
    }

    void ArrowLeft() {
        Button clickButton = findViewById(R.id.arrow_left);
        clickButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (socket.GetSocket() != null) {
                    System.out.println("Arrow Left");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // Send a message to the server
                            String message = "Arrow left";
                            try {
                                socket.GetSocket().getOutputStream().write(message.getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });
    }



    void SwitchToMainView() {
        Button button = findViewById(R.id.switch_to_main_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ArrowKeys.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
