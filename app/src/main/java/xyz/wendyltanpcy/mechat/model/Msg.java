package xyz.wendyltanpcy.mechat.model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

public class Msg extends DataSupport implements Serializable{

    public static final int TYPE_RECEIVED = 0;

    public static final int TYPE_SENT = 1;

    private String friendName;

    private String content;

    private int type;

    private boolean hasPic = false;

    private String picUri;

    public Msg(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public Msg(boolean hasPic,int type){
        this.type = type;
        this.hasPic = hasPic;
    }

    public void setPicUri(String picUri) {
        this.picUri = picUri;
    }

    public String getPicUri() {
        return picUri;
    }

    public boolean isHasPic() {
        return hasPic;
    }

    public void setHasPic(boolean hasPic) {
        this.hasPic = hasPic;
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
