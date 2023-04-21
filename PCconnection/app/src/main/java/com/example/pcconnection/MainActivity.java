package com.example.pcconnection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    SocketSingelton socket = SocketSingelton.GetInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConnectToPC();

        MouseClick();
        ShowSpace();
        SwitchToArrowKeyView();
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

    void MouseClick() {
        Button clickButton = findViewById(R.id.click_button);
        clickButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (socket.GetSocket() != null) {
                    System.out.println("Click");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // Send a message to the server
                            String message = "Click button";
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

    boolean lastdone = true;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = 0, y = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Get the x and y coordinates of the touch event
                x = event.getX();
                y = event.getY();
                // Do something with the x and y coordinates
                break;
            case MotionEvent.ACTION_MOVE:
                // Get the x and y coordinates of the touch event
                x = event.getX();
                y = event.getY();
                // Do something with the x and y coordinates
                break;
            case MotionEvent.ACTION_UP:
                // Get the x and y coordinates of the touch event
                x = event.getX();
                y = event.getY();
                // Do something with the x and y coordinates
                break;
        }
        System.out.println("Coordinates: " + x + ", " + y);

        final String message = x + "," + y;
        if (lastdone && socket.GetSocket() != null) {
            lastdone = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // Send a message to the server
                    byte[] data = message.getBytes();
                    try {
                        socket.GetSocket().getOutputStream().write(data);

                        // Wait for server response to know it is safe to send new coordinates
                        InputStream inputStream = socket.GetSocket().getInputStream();
                        lastdone = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                }).start();
        }

        return super.onTouchEvent(event);
    }

    boolean isSpace = false;
    void ShowSpace() {

        final Button buttonShow = (Button) findViewById(R.id.arrow_left);
        final Button buttonClick = findViewById(R.id.click_button);
        buttonShow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!isSpace) {
                    buttonClick.setY(-300);
                    ViewGroup.LayoutParams params = buttonClick.getLayoutParams();
                    params.height = 200;
                    buttonClick.setLayoutParams(params);
                    isSpace = true;
                }
                else {
                    buttonClick.setY(1731);
                    ViewGroup.LayoutParams params = buttonClick.getLayoutParams();
                    params.height = 1322;
                    buttonClick.setLayoutParams(params);
                    isSpace = false;
                }
            }
        });
    }

    void SwitchToArrowKeyView() {
        Button button = findViewById(R.id.switch_to_arrows_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ArrowKeys.class);
                startActivity(intent);
            }
        });
    }
}
