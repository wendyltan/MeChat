package xyz.wendyltanpcy.mechat;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Wendy on 2017/12/3.
 */

public class SocketThread implements Runnable {
    private Socket socket;
    private Handler handler;

    public SocketThread(Socket socket, Handler handler) {
        this.socket = socket;
        this.handler = handler;
    }

    @Override
    public void run() {
        String rmsg="";
        try{
            final InputStream inputStream = socket.getInputStream();
            final OutputStream outputStream = socket.getOutputStream();
            byte[] buffer = new byte[1024];
            int len=-1;
            while((len=inputStream.read(buffer))!=-1){
                String data = new String(buffer,0,len);
                rmsg = "Server received: "+ data+"\n";
                outputStream.write(rmsg.getBytes());
                Message msg = new Message();
                msg.obj = "Received from" + socket.getLocalAddress().toString()+" : "+data+"\n";
                handler.sendMessage(msg);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
