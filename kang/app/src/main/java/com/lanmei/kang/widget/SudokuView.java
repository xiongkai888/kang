package com.lanmei.kang.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.lanmei.kang.R;
import com.lanmei.kang.adapter.ItemImageAdapter;
import com.lanmei.kang.view.DoubleScaleImageView;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.FullGridView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by xkai on 2018/1/3.
 */

public class SudokuView extends RelativeLayout {

    @InjectView(R.id.only_imageView)
    DoubleScaleImageView oneImageView;
    @InjectView(R.id.gridView)
    FullGridView gridView;

    Context context;

    public SudokuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    public void setListData(final List<String> list) {

        if (!StringUtils.isEmpty(list) && list.size() == 1) {
            gridView.setVisibility(View.GONE);
            oneImageView.setVisibility(View.VISIBLE);
            ImageHelper.load(context, list.get(0), oneImageView, null, true, R.mipmap.default_pic, R.mipmap.default_pic);
            oneImageView.setDoubleClickListener(new DoubleScaleImageView.DoubleClickListener() {
                @Override
                public void onSingleTapConfirmed() {
                    if (l != null) {
                        l.onClick(0);
                    }
                }

                @Override
                public void onDoubleTap() {
                    if (l != null) {
                        l.onDoubleTap(0);
                    }
                }
            });

        } else {
            gridView.setVisibility(View.VISIBLE);
            oneImageView.setVisibility(View.GONE);
            ItemImageAdapter adapter = new ItemImageAdapter(context, list);
            gridView.setAdapter(adapter);
            adapter.setOnClickListener(new ItemImageAdapter.SingleTapConfirmedListener() {
                @Override
                public void onClick(int position) {
                    if (l != null) {
                        l.onClick(position);
                    }
                }

                @Override
                public void onDoubleTap(int position) {
                    if (l != null) {
                        l.onDoubleTap(position);
                    }
                }
            });
        }
    }

    SudokuViewClickListener l;

    public interface SudokuViewClickListener {
        void onClick(int position);

        void onDoubleTap(int position);
    }

    public void setOnSingleClickListener(SudokuViewClickListener l) {
        this.l = l;
    }


}
