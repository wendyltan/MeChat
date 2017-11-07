package xyz.wendyltanpcy.mechat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

import xyz.wendyltanpcy.mechat.adapter.MsgAdapter;
import xyz.wendyltanpcy.mechat.model.FriendInfo;
import xyz.wendyltanpcy.mechat.model.Msg;

/**
 * Created by Wendy on 2017/10/15.
 */

public class MsgActivity extends BaseActivity {
    private List<Msg> msgList = new ArrayList<>();

    private EditText inputText;

    private Button send;

    private boolean nowType = false;

    private RecyclerView msgRecyclerView;

    private MsgAdapter adapter;

    private FriendInfo mFriendInfo;

    public static void actionStart(Context context, FriendInfo info){
        Intent i = new Intent(context,MsgActivity.class);
        i.putExtra("info",info);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Intent i  = getIntent();
        mFriendInfo = (FriendInfo) i.getSerializableExtra("info");

        initMsgs(); // 初始化消息数据

        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);
        msgRecyclerView = (RecyclerView) findViewById(R.id.msg_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if (!"".equals(content)) {
                    Msg msg = new Msg(content, chooseType(nowType));
                    msg.setFriendName(mFriendInfo.getFriendName());
                    msg.save();
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size() - 1); // 当有新消息时，刷新ListView中的显示
                    msgRecyclerView.scrollToPosition(msgList.size() - 1); // 将ListView定位到最后一行
                    inputText.setText(""); // 清空输入框中的内容
                    nowType = !nowType;
                }
            }
        });
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
