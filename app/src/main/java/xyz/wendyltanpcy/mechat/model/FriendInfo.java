package xyz.wendyltanpcy.mechat.model;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Wendy on 2017/10/15.
 */

public class FriendInfo extends DataSupport implements Serializable{

    @Column (unique = true)
    private String friendName;

    private String friendLatestTalk;
    private List<Msg> friendMsgList;
    private int friendCategory;

    @Column (unique = true)
    private int id;


    public void setFriendLatestTalk(String friendLatestTalk) {
        this.friendLatestTalk = friendLatestTalk;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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

    public void setFriendMsgList(List<Msg> friendMsgList) {
        this.friendMsgList = friendMsgList;
    }

    public List<Msg> getFriendMsgList() {
        return friendMsgList;
    }

    public void setFriendCategory(int friendCategory) {
        this.friendCategory = friendCategory;
    }

    public int getFriendCategory() {
        return friendCategory;
    }
}
