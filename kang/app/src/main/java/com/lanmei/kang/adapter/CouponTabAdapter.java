package com.lanmei.kang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lanmei.kang.R;
import com.lanmei.kang.bean.NewsCategoryListBean;
import com.lanmei.kang.util.Constant;
import com.xson.common.adapter.SwipeRefreshAdapter;

import butterknife.ButterKnife;


/**
 * 优惠券
 */
public class CouponTabAdapter extends SwipeRefreshAdapter<NewsCategoryListBean> {
    private final Context context;

    public CouponTabAdapter(Context context) {
        super(context);
        this.context = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_coupon, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (CouponTabAdapter.ViewHolder)holder;
//        final NewsCategoryListBean bean = getItem(position);
//        if (bean == null){
//            return;
//        }
//        viewHolder.mTitleTv.setText(bean.getTitle());
////        viewHolder.mSubTitleTv.setText(bean.getSub_title());
//        String price = bean.getPrice();
//        if (!StringUtils.isEmpty(price)){
//            viewHolder.mPriceTv.setText("￥"+bean.getPrice());
//        }
//        viewHolder.mLabelTv.setText(bean.getLabel());
//        List<String> list = bean.getFile();
//        if (list != null && list.size() >0){
//            ImageHelper.load(context,list.get(0),viewHolder.mFileIv,null,true,R.mipmap.default_pic,R.mipmap.default_pic);
//        }
//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CourseDetailsActivity.startActivityCourseDetails(context,bean);
//            }
//        });
    }

    @Override
    public int getCount() {
        return Constant.quantity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

//        @InjectView(R.id.title_tv)
//        TextView mTitleTv;//
////        @InjectView(R.id.sub_title_tv)
////        TextView mSubTitleTv;//
//        @InjectView(R.id.price_tv)
//        TextView mPriceTv;//价格
//        @InjectView(R.id.label_tv)
//        TextView mLabelTv;//价格
//        @InjectView(R.id.pic_iv)
//        ImageView mFileIv;//

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
