package xyz.wendyltanpcy.mechat.adapter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

import xyz.wendyltanpcy.mechat.MsgActivity;
import xyz.wendyltanpcy.mechat.R;
import xyz.wendyltanpcy.mechat.model.FriendInfo;
import xyz.wendyltanpcy.mechat.model.Msg;

/**
 * Created by Wendy on 2017/10/15.
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    private List<FriendInfo> mFriendInfos;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView friendName;
        TextView friendLatestTalk;
        ImageView friendIcon;

        public ViewHolder(View view){
            super(view);
            friendName = view.findViewById(R.id.friend_name);
            friendLatestTalk =  view.findViewById(R.id.friend_latest_talk);
            friendIcon = view.findViewById(R.id.friend_image);

        }
    }

    public FriendAdapter(List<FriendInfo> infoList){
        mFriendInfos = infoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list_item,parent,false);
        mContext = parent.getContext();
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final FriendInfo info = mFriendInfos.get(position);
        holder.friendName.setText(info.getFriendName());
        holder.friendLatestTalk.setText(info.getFriendLatestTalk());
        holder.friendName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MsgActivity.actionStart(mContext,info);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Connector.getDatabase();
                Snackbar.make(view,"delete this friend?",Snackbar.LENGTH_SHORT)
                        .setAction("Yes", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DataSupport.deleteAll(Msg.class,"friendName = ?",info.getFriendName());
                                info.delete();
                                mFriendInfos.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(mContext,"delete friend success!",Toast.LENGTH_SHORT).show();
                            }
                        }).show();
                return true;

            }
        });




    }

    @Override
    public int getItemCount() {
        return mFriendInfos.size();
    }

}
