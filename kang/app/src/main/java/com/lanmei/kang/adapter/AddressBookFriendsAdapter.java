package com.lanmei.kang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.ContactsBean;

import java.util.List;

/**
 * Created by xkai on 2017/7/5.
 * 通讯录好友
 */

public class AddressBookFriendsAdapter extends BaseAdapter{

    private List<ContactsBean> mList;
    private LayoutInflater inflater;
    public AddressBookFriendsAdapter(Context context, List<ContactsBean> list){
        mList = list;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<ContactsBean> list){
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList == null?0:mList.size();
    }

    @Override
    public ContactsBean getItem(int position) {
        if (mList == null){
            return null;
        }
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemHolder holder;
        if (convertView == null) {
            holder = new ItemHolder();
            convertView = inflater.inflate(R.layout.item_address_book_friends, parent, false);
            holder.name  = (TextView) convertView.findViewById(R.id.name_tv);
            holder.recommend  = (TextView) convertView.findViewById(R.id.recommend_tv);
            convertView.setTag(holder);
        } else {
            holder = (ItemHolder) convertView.getTag();
        }
        ContactsBean bean = getItem(position);
        if (bean != null){
            holder.name.setText(bean.getContactsName());
        }
        holder.recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecommendListener != null){
                    mRecommendListener.recommend();
                }
            }
        });
        return convertView;
    }

    RecommendListener mRecommendListener;//推荐监听

    public void setRecommendListener(RecommendListener l){
        mRecommendListener = l;
    }

    public interface RecommendListener{
        void recommend();
    }

    public static class ItemHolder {
        TextView name;
        TextView recommend;
    }

}
