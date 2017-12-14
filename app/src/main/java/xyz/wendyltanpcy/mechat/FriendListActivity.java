package xyz.wendyltanpcy.mechat;

import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

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
        Connector.getWritableDatabase();
        mFriendInfos = DataSupport.findAll(FriendInfo.class);
        refreshData();
        for (FriendInfo info:mFriendInfos)
        {
            Log.i("TAG",info.getFriendName());
            Log.i("TAG", String.valueOf(info.getFriendCategory()));
        }

        adapter = new FriendNewAdapter(mFriendInfos);
        wrapper = new WrapperExpandableListAdapter(adapter);
        myList.setAdapter(wrapper);

    }



    private void refreshData() {
        if (mFriendInfos.isEmpty()) {

            FriendInfo info = new FriendInfo();
            info.setFriendName("wendy");
            info.setFriendCategory(1);//category 1
            info.setId(1);
            info.save();
            mFriendInfos.add(info);
            FriendInfo info1 = new FriendInfo();
            info1.setFriendName("jaci");
            info1.setFriendCategory(2);//category 2
            info1.setId(2);
            info1.save();
            mFriendInfos.add(info1);
            FriendInfo info2 = new FriendInfo();
            info2.setFriendName("monica");
            info2.setFriendCategory(3);//category 3
            info2.setId(3);
            info2.save();
            mFriendInfos.add(info2);
            FriendInfo info3 = new FriendInfo();
            info3.setFriendName("sayori");
            info3.setFriendCategory(4);//category 4
            info3.setId(4);
            info3.save();
            mFriendInfos.add(info3);
        }else {
            //获取各好友相关的信息列表
            for (FriendInfo info : mFriendInfos) {
                mMsgs = DataSupport.where("friendName = ?", info.getFriendName()).find(Msg.class);
                info.setFriendMsgList(mMsgs);
                //get last message
                if (!mMsgs.isEmpty()) {
                    Msg msg = mMsgs.get(mMsgs.size() - 1);
                    info.setFriendLatestTalk(msg.getContent());
                } else {
                    info.setFriendLatestTalk("no lastest talk currently");
                }

                mMsgs.clear();
            }

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
        wrapper.notifyDataSetChanged();
    }
}
