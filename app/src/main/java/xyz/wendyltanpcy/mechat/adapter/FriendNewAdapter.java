package xyz.wendyltanpcy.mechat.adapter;

import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xyz.wendyltanpcy.mechat.MsgActivity;
import xyz.wendyltanpcy.mechat.R;
import xyz.wendyltanpcy.mechat.model.FriendInfo;

/**
 * Created by Wendy on 2017/12/14.
 */

public class FriendNewAdapter implements ExpandableListAdapter {

    public String[] groupStrings = {"亲朋好友", "工作同事", "网友", "黑名单"};

    private List<FriendInfo> mFriendInfos;
    private List<FriendInfo> firstCategory = new ArrayList<>();
    private List<FriendInfo> secondCategory= new ArrayList<>();
    private List<FriendInfo> thirdCategory= new ArrayList<>();
    private List<FriendInfo> fourthCategory= new ArrayList<>();

    public FriendNewAdapter(List<FriendInfo> infos){
        mFriendInfos = infos;
        for (FriendInfo info : mFriendInfos){
            if (info.getFriendCategory()==1)
                firstCategory.add(info);
            else if(info.getFriendCategory()==2)
                secondCategory.add(info);
            else if(info.getFriendCategory()==3)
                thirdCategory.add(info);
            else if (info.getFriendCategory()==4)
                fourthCategory.add(info);
        }
        mFriendInfos.clear();
    }



    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getGroupCount() {
        return groupStrings.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        switch (groupPosition){
            case 0:
                return firstCategory.size();
            case 1:
                return secondCategory.size();
            case 2:
                return thirdCategory.size();
            case 3:
                return fourthCategory.size();
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupStrings[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        switch (groupPosition){
            case 0:
                return firstCategory.get(childPosition);
            case 1:
                return secondCategory.get(childPosition);
            case 2:
                return thirdCategory.get(childPosition);
            case 3:
                return fourthCategory.get(childPosition);
        }
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
//        switch (groupPosition){
//            case 0:
//                return firstCategory.get(childPosition).getId();
//            case 1:
//                return secondCategory.get(childPosition).getId();
//            case 2:
//                return thirdCategory.get(childPosition).getId();
//            case 3:
//                return fourthCategory.get(childPosition).getId();
//        }
//        return -1;
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expand_group, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tvTitle = convertView.findViewById(R.id.label_expand_group);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.tvTitle.setText(groupStrings[groupPosition]);
        return convertView;
    }

    //       获取显示指定分组中的指定子选项的视图
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list_item, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.friendName =  convertView.findViewById(R.id.friend_name);
            childViewHolder.friendLatestTalk = convertView.findViewById(R.id.friend_latest_talk);
            childViewHolder.friendImage = convertView.findViewById(R.id.friend_image);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        //select category

        if (groupPosition==0)
            mFriendInfos = firstCategory;
        else if(groupPosition==1)
            mFriendInfos = secondCategory;
        else if(groupPosition==2)
            mFriendInfos = thirdCategory;
        else if (groupPosition==3)
            mFriendInfos = fourthCategory;

        final FriendInfo info = mFriendInfos.get(childPosition);
        childViewHolder.friendName.setText(info.getFriendName());
        childViewHolder.friendLatestTalk.setText(info.getFriendLatestTalk());
        childViewHolder.friendImage.setImageResource(R.mipmap.ic_launcher);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MsgActivity.actionStart(parent.getContext(),info);
            }
        });
        return convertView;
    }

    // 指定位置上的子元素是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class GroupViewHolder {
        TextView tvTitle;
    }
    static class ChildViewHolder {
        TextView friendName;
        TextView friendLatestTalk;
        ImageView friendImage;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int i) {

    }

    @Override
    public void onGroupCollapsed(int i) {

    }

    @Override
    public long getCombinedChildId(long l, long l1) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long l) {
        return 0;
    }
}
