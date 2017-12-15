package xyz.wendyltanpcy.mechat.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import xyz.wendyltanpcy.mechat.R;
import xyz.wendyltanpcy.mechat.model.Msg;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {

    private List<Msg> mMsgList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout leftLayout;

        LinearLayout rightLayout;

        TextView leftMsg;

        TextView rightMsg;

        ImageView leftPic,rightPic;

        public ViewHolder(View view) {
            super(view);
            leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
            rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
            leftMsg = (TextView) view.findViewById(R.id.left_msg);
            rightMsg = (TextView) view.findViewById(R.id.right_msg);
            leftPic = view.findViewById(R.id.left_pic);
            rightPic = view.findViewById(R.id.right_pic);
        }
    }

    public MsgAdapter(List<Msg> msgList) {
        mMsgList = msgList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Msg msg = mMsgList.get(position);

        if (msg.getType() == Msg.TYPE_RECEIVED&&msg.isHasPic()) {
            // 如果是收到的消息，则显示左边的消息布局，将右边的消息布局隐藏
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setVisibility(View.GONE);
            holder.leftPic.setImageURI(Uri.parse(msg.getPicUri()));
        } else if(msg.getType() == Msg.TYPE_RECEIVED&&!msg.isHasPic()){
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftPic.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getContent());
        }else if(msg.getType() == Msg.TYPE_SENT&&msg.isHasPic()) {
            // 如果是发出的消息，则显示右边的消息布局，将左边的消息布局隐藏
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightMsg.setVisibility(View.GONE);
            holder.rightPic.setImageURI(Uri.parse(msg.getPicUri()));

        }else if(msg.getType() == Msg.TYPE_SENT&&!msg.isHasPic()){
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightPic.setVisibility(View.GONE);
            holder.rightMsg.setText(msg.getContent());
        }

    }

    @Override
    public int getItemCount() {
        return mMsgList.size();
    }

}
