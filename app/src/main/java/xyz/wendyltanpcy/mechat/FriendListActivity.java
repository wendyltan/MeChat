package xyz.wendyltanpcy.mechat;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

import xyz.wendyltanpcy.mechat.adapter.FriendAdapter;
import xyz.wendyltanpcy.mechat.model.FriendInfo;

public class FriendListActivity extends BaseActivity {

    private RecyclerView friendRecycler;
    private List<FriendInfo> mFriendInfos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent);

        initData();
        friendRecycler = (RecyclerView) findViewById(R.id.friend_recycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        friendRecycler.setLayoutManager(layoutManager);
        FriendAdapter adapter = new FriendAdapter(mFriendInfos);
        friendRecycler.setAdapter(adapter);

    }

    public void initData(){
        Connector.getDatabase();
        mFriendInfos = DataSupport.findAll(FriendInfo.class);

        if (mFriendInfos.isEmpty()) {

            FriendInfo info = new FriendInfo();
            info.setFriendName("wendy");
            info.setFriendLatestTalk("long time no see!");
            info.save();
            mFriendInfos.add(info);
            FriendInfo info1 = new FriendInfo();
            info1.setFriendName("jaci");
            info1.setFriendLatestTalk("haha you are so funny");
            info1.save();
            mFriendInfos.add(info1);
            FriendInfo info2 = new FriendInfo();
            info2.setFriendName("monica");
            info2.setFriendLatestTalk("i think you have talent in writing poem");
            info2.save();
            mFriendInfos.add(info2);
            FriendInfo info3 = new FriendInfo();
            info3.setFriendName("sayori");
            info3.setFriendLatestTalk("i'm hungry.. again!");
            info3.save();
            mFriendInfos.add(info3);
        }


    }
}
