package xyz.wendyltanpcy.mechat.model;

import org.litepal.crud.DataSupport;

public class Msg extends DataSupport{

    public static final int TYPE_RECEIVED = 0;

    public static final int TYPE_SENT = 1;

    private String friendName;

    private String content;

    private int type;

    public Msg(String content, int type) {
        this.content = content;
        this.type = type;
    }


    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendName() {
        return friendName;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }

}
