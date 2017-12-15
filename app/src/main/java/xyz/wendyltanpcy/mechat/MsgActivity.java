package xyz.wendyltanpcy.mechat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import xyz.wendyltanpcy.mechat.adapter.MsgAdapter;
import xyz.wendyltanpcy.mechat.model.FriendInfo;
import xyz.wendyltanpcy.mechat.model.Msg;

/**
 * Created by Wendy on 2017/10/15.
 */

public class MsgActivity extends BaseActivity {


    private List<Msg> msgList = new ArrayList<>();

    //view
    @BindView(R.id.input_text)
    EditText inputText;
    @BindView(R.id.send)
    Button send;
    @BindView(R.id.msg_recycler_view)
    RecyclerView msgRecyclerView;
    @BindView(R.id.send_pic)
    Button sendPic;

    @BindView(R.id.mychat_image)
    ImageView addButton;
    @BindView(R.id.title_text)
    TextView titleText;

    //image
    public static final int REQUEST_ALBUM = 2;

    private boolean nowType = false;

    private MsgAdapter adapter;

    private FriendInfo mFriendInfo;

    //network
    private Socket socketClient;
    private String serverString = "";
    private ServerSocket serverSocket;
    private int serverport = 9999;

    public static void actionStart(Context context, FriendInfo info){
        Intent i = new Intent(context,MsgActivity.class);
        i.putExtra("info",info);
        context.startActivity(i);
    }

    @SuppressLint("HandlerLeak")
    Handler Serverhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            serverString =msg.obj.toString();
        }
    };

    @SuppressLint("HandlerLeak")
    Handler Clienthandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };



    //Server side init
    private void initServer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    serverSocket = new ServerSocket(serverport,100, InetAddress.getByName("localhost"));
                    while(true){
                        Socket socket = serverSocket.accept();
                        Message msg = new Message();
                        msg.obj="client connected..\n";
                        Serverhandler.sendMessage(msg);
                        new Thread(new SocketThread(socket,Serverhandler)).start();
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    //Client side init
    private void initClient(){
        if (socketClient!=null){
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socketClient = new Socket("localhost",9999);
                    InputStream inputStream = socketClient.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = null;
                    while((line=bufferedReader.readLine())!=null){
                        Message msg = new Message();
                        msg.obj = line+"\n";
                        Clienthandler.sendMessage(msg);
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);



        Intent i  = getIntent();
        mFriendInfo = (FriendInfo) i.getSerializableExtra("info");
        //setting name,hidding add button
        addButton.setVisibility(View.GONE);
        titleText.setText(mFriendInfo.getFriendName());

        //request permission
        PermissionGen.with(MsgActivity.this)
                .addRequestCode(100)
                .permissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request();

        initMsgs(); // 初始化消息数据

        initServer();
        initClient();

        //set recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content = inputText.getText().toString();
                if (!"".equals(content)) {
                    new Thread(new SendThread(socketClient,content)).start();

                    //msg i send
                    Msg msgSend = new Msg(serverString, chooseType(nowType));
                    msgSend.setFriendName(mFriendInfo.getFriendName());
                    msgSend.save();
                    msgList.add(msgSend);
                    nowType = !nowType;

                    //server back msg
                    Msg msgServer = new Msg(content, chooseType(nowType));
                    msgServer.setFriendName(mFriendInfo.getFriendName());
                    msgServer.save();
                    msgList.add(msgServer);
                    nowType = !nowType;

                    adapter.notifyItemInserted(msgList.size() - 1); // 当有新消息时，刷新ListView中的显示
                    msgRecyclerView.scrollToPosition(msgList.size() - 1); // 将ListView定位到最后一行
                    inputText.setText(""); // 清空输入框中的内容
                }
            }
        });

        sendPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickPicker(view);

            }
        });


    }


    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                     int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = 100)
    public void doSomething(){
        //do nothing
//        Toast.makeText(this, "permission is granted", Toast.LENGTH_SHORT).show();
    }

    @PermissionFail(requestCode = 100)
    public void doFailSomething(){
        Toast.makeText(this, "permission is not granted", Toast.LENGTH_SHORT).show();
    }
    public void onClickPicker(View v) {
        new AlertDialog.Builder(this)
                .setTitle("选择照片")
                .setItems(new String[]{"相册"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            selectAlbum();
                        }
                    }
                })
                .create()
                .show();
    }

    private void selectAlbum() {
        Intent albumIntent = new Intent(Intent.ACTION_PICK);
        albumIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(albumIntent, REQUEST_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }
        switch (requestCode) {
            case REQUEST_ALBUM:

                Uri uri = data.getData();
                if (uri != null) {
                    Msg msgSend = new Msg(true, chooseType(nowType));
                    msgSend.setFriendName(mFriendInfo.getFriendName());
                    msgSend.setHasPic(true);
                    msgSend.setPicUri(String.valueOf(uri));
                    msgSend.save();
                    msgList.add(msgSend);
                    nowType = !nowType;
                    adapter.notifyItemInserted(msgList.size() - 1); // 当有新消息时，刷新ListView中的显示
                    msgRecyclerView.scrollToPosition(msgList.size() - 1); // 将ListView定位到最后一行
                }
                break;
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private int chooseType(boolean nowType){
        if (nowType){
            return Msg.TYPE_SENT;
        }else{
            return Msg.TYPE_RECEIVED;
        }
    }

    private void initMsgs() {
        Connector.getDatabase();
        msgList =DataSupport.select("*")
                .where("friendName = ?", mFriendInfo.getFriendName()).find(Msg.class);
    }


}
