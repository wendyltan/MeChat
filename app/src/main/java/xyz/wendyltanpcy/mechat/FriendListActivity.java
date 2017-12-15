package xyz.wendyltanpcy.mechat;

import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.diegocarloslima.fgelv.lib.WrapperExpandableListAdapter;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.wendyltanpcy.mechat.adapter.FriendNewAdapter;
import xyz.wendyltanpcy.mechat.model.FriendInfo;
import xyz.wendyltanpcy.mechat.model.Msg;

public class FriendListActivity extends BaseActivity {


    @BindView(R.id.my_list)
    ExpandableListView myList;

    @BindView(R.id.title_text)
    TextView titleText;

    private List<FriendInfo> mFriendInfos = new ArrayList<>();
    private List<Msg> mMsgs= new ArrayList<>();
    private Socket socket;
    private  ExpandableListAdapter adapter;
    private  WrapperExpandableListAdapter wrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent);
        ButterKnife.bind(this);
        Connector.getDatabase();
        //setting name,hidding add button
        titleText.setText("FriendList");

        mFriendInfos = DataSupport.findAll(FriendInfo.class);
        refreshData();
        adapter = new FriendNewAdapter(mFriendInfos);
        wrapper = new WrapperExpandableListAdapter(adapter);
        myList.setAdapter(wrapper);
        for (int i=0;i<3;i++){
            myList.expandGroup(i);
        }

    }



    private void refreshData() {
        //获取各好友相关的信息列表
        if (!mFriendInfos.isEmpty()) {
            for (FriendInfo info : mFriendInfos) {
                mMsgs = DataSupport.where("friendName = ?", info.getFriendName()).find(Msg.class);
                info.setFriendMsgList(mMsgs);
                //get last message
                if (!mMsgs.isEmpty()) {
                    Msg msg = mMsgs.get(mMsgs.size() - 1);
                    if (msg.isHasPic())
                        info.setFriendLatestTalk("[pic]");
                    else
                        info.setFriendLatestTalk(msg.getContent());
                } else {
                    info.setFriendLatestTalk("[no lastest talk currently]");
                }

                mMsgs.clear();
            }

        }
        else
            Toast.makeText(getApplicationContext(),"还没有好友呢！",Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

}
