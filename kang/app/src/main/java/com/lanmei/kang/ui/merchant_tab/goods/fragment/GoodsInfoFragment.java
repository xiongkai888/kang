package com.lanmei.kang.ui.merchant_tab.goods.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.lanmei.kang.R;
import com.lanmei.kang.bean.GoodsDetailsBean;
import com.lanmei.kang.ui.merchant_tab.goods.activity.GoodsDetailsActivity;
import com.lanmei.kang.util.CommonUtils;
import com.lanmei.kang.widget.SlideDetailsLayout;
import com.xson.common.app.BaseFragment;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.FormatTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 商品
 */
public class GoodsInfoFragment extends BaseFragment implements SlideDetailsLayout.OnSlideDetailsListener {


    @InjectView(R.id.banner)
    ConvenientBanner banner;
    @InjectView(R.id.goods_detail)
    TextView goodsDetail;
    @InjectView(R.id.goods_config)
    TextView goodsConfig;
    @InjectView(R.id.tab_cursor)
    View tabCursor;
    @InjectView(R.id.frameLayout_content)
    FrameLayout frameLayoutContent;
    @InjectView(R.id.slideDetailsLayout)
    SlideDetailsLayout slideDetailsLayout;
    @InjectView(R.id.fab_up)
    FloatingActionButton fabUp;
    @InjectView(R.id.scrollView)
    ScrollView scrollView;
    @InjectView(R.id.name_tv)
    TextView nameTv;
    @InjectView(R.id.price_tv)
    TextView priceTv;
    @InjectView(R.id.pay_num_tv)
    TextView payNumTv;
    @InjectView(R.id.comment_num_tv)
    FormatTextView commentNumTv;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.all_comment_tv)
    TextView allCommentTv;
    @InjectView(R.id.sale_price_tv)
    TextView salePriceTv;//市场价

    private GoodsConfigFragment goodsConfigFragment;
    private GoodsInfoWebFragment goodsInfoWebFragment;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<TextView> tabTextList = new ArrayList<>();
    private Fragment currFragment;
    private int currIndex = 0;
    public GoodsDetailsActivity activity;
    private float fromX;
    GoodsDetailsBean bean;//商品信息bean

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (GoodsDetailsActivity) context;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_goods_info;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        init();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    //本数据的代码可以再优化，写到另一个Controller处理
    private void init() {
        Bundle bundle = getArguments();
        if (!StringUtils.isEmpty(bundle)) {
            bean = (GoodsDetailsBean) bundle.getSerializable("bean");
        }
        if (bean == null) {
            return;
        }
        initSlide();
        setParameter();
        initView();
        initTabView();
        initBannerView(bean.getImgs());
        initDetailView();
    }

    private void setParameter() {
        nameTv.setText(bean.getGoodsname());
        priceTv.setText(String.format(context.getString(R.string.price), bean.getPrice()));
        payNumTv.setText(String.format(context.getString(R.string.pay_num), bean.getSales()));
        salePriceTv.setText(String.format(context.getString(R.string.sale_price), bean.getSale_price()));
    }

    private void initTabView() {
        tabTextList.add(goodsDetail);
        tabTextList.add(goodsConfig);
    }

    private void initSlide() {
        slideDetailsLayout.setOnSlideDetailsListener(this);
    }


    private void initView() {
        fabUp.hide();
        goodsConfigFragment = new GoodsConfigFragment();
        goodsInfoWebFragment = new GoodsInfoWebFragment();
        Bundle bundle = new Bundle();
        bundle.putString("content", bean.getContent());
        goodsInfoWebFragment.setArguments(bundle);

        fragmentList.add(goodsConfigFragment);
        fragmentList.add(goodsInfoWebFragment);
        currFragment = goodsInfoWebFragment;
        //默认显示商品详情tab
        getChildFragmentManager().beginTransaction().replace(R.id.frameLayout_content, currFragment).commitAllowingStateLoss();
    }

    private void initBannerView(List<String> list) {
        //初始化商品图片轮播
        CommonUtils.setBanner(banner,list,true);

    }

    private void initDetailView() {

    }


    private void scrollCursor() {
        TranslateAnimation anim = new TranslateAnimation(fromX, currIndex * tabCursor.getWidth(), 0, 0);
        anim.setFillAfter(true);
        anim.setDuration(50);
        fromX = currIndex * tabCursor.getWidth();
        tabCursor.startAnimation(anim);

        for (int i = 0; i < tabTextList.size(); i++) {
            tabTextList.get(i).setTextColor(i == currIndex ? getResources().getColor(R.color.colorPrimaryDark) : getResources().getColor(R.color.black));
        }
    }

    private void switchFragment(Fragment fromFragment, Fragment toFragment) {
        if (currFragment != toFragment) {
            if (!toFragment.isAdded()) {
                getFragmentManager().beginTransaction().hide(fromFragment).add(R.id.frameLayout_content, toFragment).commitAllowingStateLoss();
            } else {
                getFragmentManager().beginTransaction().hide(fromFragment).show(toFragment).commitAllowingStateLoss();
            }
        }
    }

    //可以继续优化
    @Override
    public void onStatusChanged(SlideDetailsLayout.Status status) {
        //当前为图文详情页
        if (status == SlideDetailsLayout.Status.OPEN) {
            fabUp.show();
            activity.operaTitleBar(true, true);
        } else {
            //当前为商品详情页
            fabUp.hide();
            activity.operaTitleBar(false, false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.goods_detail, R.id.goods_config, R.id.all_comment_tv, R.id.fab_up, R.id.pull_up_view, R.id.ll_choose_parameter})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.goods_detail: //商品详情tab
                currIndex = 0;
                scrollCursor();
                switchFragment(currFragment, goodsInfoWebFragment);
                currFragment = goodsInfoWebFragment;
                break;
            case R.id.goods_config://商品规格tab
                currIndex = 1;
                scrollCursor();
                switchFragment(currFragment, goodsConfigFragment);
                currFragment = goodsConfigFragment;
                break;
            case R.id.all_comment_tv://查看所有评论
                activity.toCommentPager();
                break;
            case R.id.fab_up://滚到顶部
                scrollView.smoothScrollTo(0, 0);
                slideDetailsLayout.smoothClose(true);
                break;
            case R.id.pull_up_view://上拉查看图文详情
                slideDetailsLayout.smoothOpen(true);
                break;
            case R.id.ll_choose_parameter://选择参数
                UIHelper.ToastMessage(context, R.string.developing);
                break;
        }
    }

    //只设置显示一个评论item
    @Subscribe(sticky = true)
    public void showOnlyComment(Object event) {
//        GoodsCommentAdapter adapter = new GoodsCommentAdapter(context);
//        adapter.setOnlyItem(true);
//        adapter.setData(event.getList());
//        recyclerView.setLayoutManager(new LinearLayoutManager(context));
//        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
