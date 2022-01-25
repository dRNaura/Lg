package com.lg_project.modelclass;

import android.app.Application;
import android.util.Log;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.engineio.client.transports.WebSocket;
import okhttp3.OkHttpClient;


public class SocketManager extends Application {

    private Socket socket;
    {
        /* OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        IO.setDefaultOkHttpWebSocketFactory(okHttpClient);
        IO.setDefaultOkHttpCallFactory(okHttpClient);

        IO.Options opts = new IO.Options();
        opts.callFactory = okHttpClient;
        opts.webSocketFactory = okHttpClient;
*/
        try {
           /* IO.Options opts = new IO.Options();
            opts.transports = new String[] { WebSocket.NAME };*/
            socket = IO.socket("http://3.99.23.167:8080");
        }catch (Exception e){
            Log.e("Exception",e.toString());
        }
    }

    public Socket getSocket() {
        return socket;
    }

}