package xyz.wendyltanpcy.mechat;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Created by Wendy on 2017/12/3.
 */

class SendThread implements Runnable {
    private Socket socket;
    private String msg;

    public SendThread(Socket socket,String sendmsg) {
        this.socket = socket;
        this.msg = sendmsg;
    }

    @Override
    public void run() {
        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(msg.getBytes(Charset.forName("UTF-8")));
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
