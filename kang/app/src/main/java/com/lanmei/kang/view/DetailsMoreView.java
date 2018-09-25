package com.lanmei.kang.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.lanmei.kang.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xkai on 2017/8/10.
 * 详情中的更多弹框
 */

public class DetailsMoreView extends LinearLayout {


    Context mContext;

    public DetailsMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    @OnClick({R.id.delete_tv,R.id.share_tv})
    public void showDetailsCompileItem(View view){
        switch (view.getId()){
            case R.id.delete_tv://删除
                if (mDetailsMoreListener != null){
                    mDetailsMoreListener.delete();
                }
                break;
            case R.id.share_tv://分享
                if (mDetailsMoreListener != null){
                    mDetailsMoreListener.shareMore();
                }
                break;
        }
    }



    DetailsMoreListener mDetailsMoreListener;//详情更多分享、删除监听

    public void setDetailsMoreListener(DetailsMoreListener l){
        mDetailsMoreListener = l;
    }

    public interface DetailsMoreListener{
        void delete();
        void shareMore();
    }


}
