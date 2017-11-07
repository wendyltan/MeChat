package xyz.wendyltanpcy.mechat.model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Wendy on 2017/10/15.
 */

public class FriendInfo extends DataSupport implements Serializable{
    private String friendName;
    private String friendLatestTalk;

    public void setFriendLatestTalk(String friendLatestTalk) {
        this.friendLatestTalk = friendLatestTalk;
    }


    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendLatestTalk() {
        return friendLatestTalk;
    }

    public String getFriendName() {
        return friendName;
    }


}
