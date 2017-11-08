package xyz.wendyltanpcy.mechat;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.wendyltanpcy.mechat.adapter.FriendAdapter;
import xyz.wendyltanpcy.mechat.model.FriendInfo;
import xyz.wendyltanpcy.mechat.model.Msg;

public class FriendListActivity extends BaseActivity {

    @BindView(R.id.friend_recycle)
    RecyclerView friendRecycler;

    private List<FriendInfo> mFriendInfos = new ArrayList<>();
    private List<Msg> mMsgs= new ArrayList<>();
    private FriendAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent);
        ButterKnife.bind(this);
        Connector.getDatabase();
        mFriendInfos = DataSupport.findAll(FriendInfo.class);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        friendRecycler.setLayoutManager(layoutManager);
        adapter = new FriendAdapter(mFriendInfos);
        friendRecycler.setAdapter(adapter);

    }

    public void refreshData(){


        if (mFriendInfos.isEmpty()) {

            FriendInfo info = new FriendInfo();
            info.setFriendName("wendy");
            info.save();
            mFriendInfos.add(info);
            FriendInfo info1 = new FriendInfo();
            info1.setFriendName("jaci");
            info1.save();
            mFriendInfos.add(info1);
            FriendInfo info2 = new FriendInfo();
            info2.setFriendName("monica");
            info2.save();
            mFriendInfos.add(info2);
            FriendInfo info3 = new FriendInfo();
            info3.setFriendName("sayori");
            info3.save();
            mFriendInfos.add(info3);
        }else{
            //获取各好友相关的信息列表
            for (FriendInfo info:mFriendInfos){
                mMsgs = DataSupport.where("friendName = ?",info.getFriendName()).find(Msg.class);
                info.setFriendMsgList(mMsgs);
                //get last message
                if (!mMsgs.isEmpty()){
                    Msg msg = mMsgs.get(mMsgs.size()-1);
                    info.setFriendLatestTalk(msg.getContent());
                }else{
                    info.setFriendLatestTalk("no lastest talk currently");
                }

                mMsgs.clear();
            }
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }
}

/**
 * litepal query example
 */

//        List<News> newsList = DataSupport.where("commentcount > ?","0").find(News.class);
//        List<News> newsList = DataSupport.select("title", "content")
//        .where("commentcount > ?", "0").find(News.class);  	// 只要title和content两列数据
//        List<News> newsList = DataSupport.select("title", "content")
//        .where("commentcount > ?", "0")
//        .order("publishdate desc").find(News.class);  	// asc正序排序、desc倒序排序
//        List<News> newsList = DataSupport.select("title", "content")
//        .where("commentcount > ?", "0")
//        .order("publishdate desc").limit(10).find(News.class);  // 只查询10条数据
//        List<News> newsList = DataSupport.select("title", "content")
//        .where("commentcount > ?", "0")
//        .order("publishdate desc").limit(10).offset(10)
//        .find(News.class);
