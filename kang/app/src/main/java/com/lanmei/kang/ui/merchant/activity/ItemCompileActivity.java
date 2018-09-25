package com.lanmei.kang.ui.merchant.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.data.volley.Response;
import com.data.volley.error.VolleyError;
import com.lanmei.kang.R;
import com.lanmei.kang.adapter.ItemsCompileAdapter;
import com.lanmei.kang.api.DelProductApi;
import com.lanmei.kang.api.EditProductApi;
import com.lanmei.kang.api.ItemsCategoryApi;
import com.lanmei.kang.bean.CategoryBean;
import com.lanmei.kang.bean.ItemsCompileBean;
import com.lanmei.kang.bean.MerchantItemsListBean;
import com.lanmei.kang.event.AddCategoryEvent;
import com.lanmei.kang.event.ChooseCategoryEvent;
import com.lanmei.kang.event.CompileProductEvent;
import com.lanmei.kang.ui.merchant.ClassifyToDialogFragment;
import com.lanmei.kang.util.AKDialog;
import com.lanmei.kang.util.CommonUtils;
import com.lanmei.kang.util.CompressPhotoUtils;
import com.oss.ManageOssUpload;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIBaseUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.DividerItemDecoration;
import com.xson.common.widget.ProgressHUD;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 编辑产品
 */
public class ItemCompileActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.ll_bottom_two_button)
    LinearLayout mllView;//下架和删除
    ClassifyToDialogFragment mDialog;

    @InjectView(R.id.project_name_et)
    EditText projectNameEt;
    @InjectView(R.id.sold_out_tv)
    TextView soldOutTv;//下架
    @InjectView(R.id.recyclerView1)
    RecyclerView recyclerView1;//商品图片
    @InjectView(R.id.inform_content_et)
    EditText informContentEt;
    @InjectView(R.id.recyclerView2)
    RecyclerView recyclerView2;//商品详情图片
    @InjectView(R.id.price_et)
    EditText priceEt;
    @InjectView(R.id.category_tv)
    TextView categoryTv;
    MerchantItemsListBean bean;
    boolean isAdd;
    String is_del;//是否下架0正常2下架
    ItemsCompileAdapter adapter1;
    ItemsCompileAdapter adapter2;
    List<ItemsCompileBean> list1;//商品图片
    List<ItemsCompileBean> list2;//商品详情图片
    private String pid;
    List<String> paths1; // 本地需要上传图片的集合路径(压缩后的)
    List<String> paths2; // 本地需要上传图片的集合路径(压缩后的)
    List<String> paths3; // 本地需要上传图片的集合路径(压缩后的)  (封面的)
    private ManageOssUpload manageOssUpload1;//图片上传类
    private ManageOssUpload manageOssUpload2;//图片上传类
    private ManageOssUpload manageOssUpload3;//图片上传类（封面的）

    private static final int REQUEST_CODE_PERMISSION_PHOTO_PICKER = 1;

    String name;
    String content;
    String price;

    private void done(int what) {
        name = CommonUtils.getStringByEditText(projectNameEt);
        if (StringUtils.isEmpty(name)) {
            UIHelper.ToastMessage(this, R.string.input_project_name);
            return;
        }

        if (StringUtils.isEmpty(list1)) {
            UIHelper.ToastMessage(this, "请选择商品图片");
        }

        content = CommonUtils.getStringByEditText(informContentEt);
        if (StringUtils.isEmpty(content)) {
            UIHelper.ToastMessage(this, "请输入商品描述");
            return;
        }
        if (StringUtils.isEmpty(list2)) {
            UIHelper.ToastMessage(this, "请选择商品图片");
        }
        price = CommonUtils.getStringByEditText(priceEt);
        if (StringUtils.isEmpty(price)) {
            UIHelper.ToastMessage(this, R.string.input_price);
            return;
        }

        if (StringUtils.isEmpty(categoryBean)) {
            UIHelper.ToastMessage(this, "请选择分类");
            return;
        }
        mProgressHUD.show();
        successPath1 = null;
        successPath2 = null;
        successPath3 = null;
        List<String> stringList1 = getStringList1();//没有封面
        if (!StringUtils.isEmpty(stringList1)) {
            isCompress1 = true;//在压缩
            successPath1 = new ArrayList<>();
            new CompressPhotoUtils().CompressPhoto(ItemCompileActivity.this, stringList1, new CompressPhotoUtils.CompressCallBack() {//压缩图片（可多张）
                @Override
                public void success(List<String> list) {
                    paths1 = list;
                    new UpdateImageViewTask(1).execute();
                }
            },"1");
        }
        List<String> cover = getCoverString();//只要封面的
        if (!StringUtils.isEmpty(cover)) {
            successPath3 = new ArrayList<>();
            isCompress3 = true;//在压缩
            new CompressPhotoUtils().CompressPhoto(ItemCompileActivity.this, cover, new CompressPhotoUtils.CompressCallBack() {//压缩图片（可多张）
                @Override
                public void success(List<String> list) {
                    paths3 = list;
                    new UpdateImageViewTask(3).execute();
                }
            },"3");
        }
        List<String> stringList2 = getStringList2();
        if (!StringUtils.isEmpty(stringList2)) {
            successPath2 = new ArrayList<>();
            isCompress2 = true;//在压缩
            new CompressPhotoUtils().CompressPhoto(ItemCompileActivity.this, stringList2, new CompressPhotoUtils.CompressCallBack() {//压缩图片（可多张）
                @Override
                public void success(List<String> list) {
                    paths2 = list;
                    new UpdateImageViewTask(2).execute();
                }
            },"2");
        }
        if (!isCompress1 && !isCompress2 && !isCompress3) {
            loadCompile(what);
        }
    }

    private void loadCompile(int what) {
        EditProductApi api = new EditProductApi();
        if (isAdd) {
            api.mid = pid;//(编辑时可不传)
        } else {
            api.pid = bean.getId();//商品id(添加时不传)
        }
        api.category_id = categoryBean.getId();
        api.name = name;
        api.content = content;
        api.sell_price = price;
        api.is_del = what;
        if (!StringUtils.isEmpty(successPath3)) {//
            api.img = successPath3.get(0);
        } else {
            api.img = getCoverStringNoCompress(list1);
        }
        api.file = getFileString();
        api.pic = getPicString();
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                UIHelper.ToastMessage(ItemCompileActivity.this, response.getInfo());
                EventBus.getDefault().post(new CompileProductEvent());
                mProgressHUD.cancel();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressHUD.cancel();
                UIHelper.ToastMessage(ItemCompileActivity.this, error.getMessage());
            }
        });
    }

    //商品图片
    private String getFileString() {
        StringBuffer buffer = new StringBuffer();
        List<ItemsCompileBean> beanList = adapter1.getData();
        if (!StringUtils.isEmpty(beanList)) {
            int size = beanList.size();
            for (int i = 0; i < size; i++) {
                ItemsCompileBean bean = beanList.get(i);
                if (!bean.isPicker() && !bean.isCover()) {//不是来自相册,也不是封面
                    buffer.append(bean.getPic() + ",");

                }
            }
        }
        if (!StringUtils.isEmpty(successPath1)) {
            int size = successPath1.size();
            for (int i = 0; i < size; i++) {
                buffer.append(successPath1.get(i) + ",");
            }
        }
        String file = buffer.toString();
        if (!StringUtils.isEmpty(file)) {
            file = file.substring(0, file.length() - 1);
        }
        return file;
    }

    //商品详情图片
    private String getPicString() {
        StringBuffer buffer = new StringBuffer();
        List<ItemsCompileBean> beanList = adapter2.getData();
        if (!StringUtils.isEmpty(beanList)) {
            int size = beanList.size();
            for (int i = 0; i < size; i++) {
                ItemsCompileBean bean = beanList.get(i);
                if (!bean.isPicker()) {//不是来自相册
                    buffer.append(bean.getPic() + ",");
                }
            }
        }
        if (!StringUtils.isEmpty(successPath2)) {
            int size = successPath2.size();
            for (int i = 0; i < size; i++) {
                buffer.append(successPath2.get(i) + ",");
            }
        }

        String pic = buffer.toString();
        if (!StringUtils.isEmpty(pic)) {
            pic = pic.substring(0, pic.length() - 1);
        }
        return pic;
    }

    List<String> successPath1;  // 存储上传阿里云成功后的上传路径(商品图片)
    List<String> successPath2;  // 存储上传阿里云成功后的上传路径(商品详情图片)
    List<String> successPath3;  // 存储上传阿里云成功后的上传路径(封面的)
    boolean isCompress1 = false;//
    boolean isCompress2 = false;//是否在压缩图片
    boolean isCompress3 = false;//是否在压缩图片

    public class UpdateImageViewTask extends AsyncTask<Void, Integer, Void> {

        int status;

        public UpdateImageViewTask(int status) {
            this.status = status;
        }

        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {
            //            Toast.makeText(PublishDynamicActivity.this, "开始执行", Toast.LENGTH_SHORT).show();
        }

        /**
         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
         */
        @Override
        protected Void doInBackground(Void... params) {
            if (status == 1) {//商品图片
                int size = paths1.size();
                for (int i = 0; i < size; i++) {
                    String picPath1 = paths1.get(i);
                    String urlPic1 = manageOssUpload1.uploadFile_img(picPath1,""+status);
                    L.d("CompressPhotoUtils", "上传1："+urlPic1+",本地路径："+picPath1);
                    if (StringUtils.isEmpty(urlPic1)) {
                        //写上传失败逻辑
                        Message msg = mHandler.obtainMessage();
                        msg.what = 1;
                        msg.arg1 = i;
                        msg.obj = picPath1;
                        mHandler.sendMessage(msg);
                    } else {
                        successPath1.add(urlPic1);
                    }
                }
            } else if (status == 2) {//商品详情图片
                int size = paths2.size();
                for (int i = 0; i < size; i++) {
                    String picPath2 = paths2.get(i);
                    String urlPic2 = manageOssUpload2.uploadFile_img(picPath2,""+status);
                    L.d("CompressPhotoUtils", "上传2："+urlPic2+",本地路径："+picPath2);
                    if (StringUtils.isEmpty(urlPic2)) {
                        //写上传失败逻辑
                        Message msg = mHandler.obtainMessage();
                        msg.what = 1;
                        msg.arg1 = i;
                        msg.obj = picPath2;
                        mHandler.sendMessage(msg);
                    } else {
                        successPath2.add(urlPic2);
                    }
                }
            } else if (status == 3) {//封面
                int size = paths3.size();
                for (int i = 0; i < size; i++) {
                    String picPath3 = paths3.get(i);
                    String urlPic3 = manageOssUpload3.uploadFile_img(picPath3,""+status);
                    L.d("CompressPhotoUtils", "上传3："+urlPic3+",本地路径："+picPath3);
                    if (StringUtils.isEmpty(urlPic3)) {
                        //写上传失败逻辑
                        Message msg = mHandler.obtainMessage();
                        msg.what = 1;
                        msg.arg1 = i;
                        msg.obj = picPath3;
                        mHandler.sendMessage(msg);
                    } else {
                        successPath3.add(urlPic3);
                    }
                }
            }
            return null;
        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(Void integer) {
            //            mProgressDialog.cancel();
            if (status == 1) {
                isCompress1 = false;
            } else if (status == 2) {
                isCompress2 = false;
            } else if (status == 3) {
                isCompress3 = false;
            }
            if (!isCompress1 && !isCompress2 && !isCompress3) {
                loadCompile(0);
            }
        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1://上传某张图片失败
                    UIHelper.ToastMessage(ItemCompileActivity.this, "上传图片失败：" + msg.obj);
                    break;
                case 2:
                    break;
            }
        }
    };


    //获取本地图片路径（不包括封面）
    private List<String> getStringList1() {
        if (StringUtils.isEmpty(list1)) {
            return null;
        }
        List<String> stringList = new ArrayList<>();
        int size = list1.size();
        for (int i = 0; i < size; i++) {
            ItemsCompileBean bean = list1.get(i);
            if (bean.isPicker() && !bean.isCover()) {
                stringList.add(bean.getPic());
            }
        }
        return stringList;
    }
    //获取本地图片路径
    private List<String> getStringList2() {
        if (StringUtils.isEmpty(list2)) {
            return null;
        }
        List<String> stringList = new ArrayList<>();
        int size = list2.size();
        for (int i = 0; i < size; i++) {
            ItemsCompileBean bean = list2.get(i);
            if (bean.isPicker()) {
                stringList.add(bean.getPic());
            }
        }
        return stringList;
    }

    //获取封面的本地路径
    private List<String> getCoverString() {
        if (StringUtils.isEmpty(list1)) {
            return null;
        }
        List<String> stringList = new ArrayList<>();
        int size = list1.size();
        for (int i = 0; i < size; i++) {
            ItemsCompileBean bean = list1.get(i);
            if (bean.isCover() && bean.isPicker()) {
                stringList.add(bean.getPic());
                return stringList;
            }
        }
        return stringList;
    }

    //获取封面
    private String getCoverStringNoCompress(List<ItemsCompileBean> list) {
        if (StringUtils.isEmpty(list)) {
            return null;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            ItemsCompileBean bean = list.get(i);
            if (bean.isCover() && !bean.isPicker()) {
                return bean.getPic();
            }
        }
        return null;
    }


    @Override
    public int getContentViewId() {
        return R.layout.activity_item_compile;
    }

    ProgressHUD mProgressHUD;

    private void initProgressDialog() {
        mProgressHUD = ProgressHUD.show(this, "", true, false, null);
        mProgressHUD.setCancelable(true);
        mProgressHUD.cancel();
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        manageOssUpload1 = new ManageOssUpload(this);
        manageOssUpload2 = new ManageOssUpload(this);
        manageOssUpload3 = new ManageOssUpload(this);
        initProgressDialog();
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.mipmap.back);
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        if (isAdd) {
            actionbar.setTitle("添加新产品");
            mllView.setVisibility(View.GONE);
        } else {
            is_del = bean.getIs_del();
            if (!StringUtils.isSame(CommonUtils.isZero, is_del)) {//是否下架0正常2下架
//                soldOutTv.setText("已下架");
                mllView.setVisibility(View.GONE);
            }
            actionbar.setTitle("编辑产品");
            setData1();//商品图片
            setData2();//商品详情图片
        }
        initRecyclerView();
        EventBus.getDefault().register(this);
    }

    private void setData1() {
        projectNameEt.setText(bean.getName());
        informContentEt.setText(bean.getContent());
        priceEt.setText(bean.getSell_price());
        List<MerchantItemsListBean.CategoryBean> list = bean.getCategory();
        if (!StringUtils.isEmpty(list)) {//分类
            MerchantItemsListBean.CategoryBean bean = list.get(0);
            categoryBean = new CategoryBean();
            categoryBean.setId(bean.getId());
            categoryBean.setName(bean.getName());
            categoryTv.setText(bean.getName());//
        } else {
            categoryBean = null;
            categoryTv.setText(R.string.choose_category);//
        }
        if (!StringUtils.isEmpty(bean.getImg())) {
            ItemsCompileBean bean1 = new ItemsCompileBean();
            bean1.setPic(bean.getImg());
            bean1.setCover(true);//设为封面
            list1.add(bean1);
        }
        List<String> stringList = bean.getFile();
        if (!StringUtils.isEmpty(stringList)) {
            int size = stringList.size();
//            UIHelper.ToastMessage(this,size+"");
            for (int i = 0; i < size; i++) {
                ItemsCompileBean bean2 = new ItemsCompileBean();
                if (i == 0 && StringUtils.isEmpty(list1)) {
                    bean2.setCover(true);
                }
                bean2.setPic(stringList.get(i));
                list1.add(bean2);
            }
        }

    }

    private void setData2() {
        List<String> stringList = bean.getPic();
        if (!StringUtils.isEmpty(stringList)) {
            int size = stringList.size();
            for (int i = 0; i < size; i++) {
                ItemsCompileBean bean2 = new ItemsCompileBean();
                bean2.setPic(stringList.get(i));
                list2.add(bean2);
            }
        }
    }


    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_PHOTO_PICKER)
    private void choicePhotoWrapper() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {//点击加号时调用
            // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
            int num = 0;
            if (type == 1) {
                num = adapter1.getCount();
            } else {
                num = adapter2.getCount();
            }
            File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "uploading" + type);
            startActivityForResult(BGAPhotoPickerActivity.newIntent(this, takePhotoDir, 9 - num, null, false), type);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {//选择图片点击确定时
            ArrayList<String> list = BGAPhotoPickerActivity.getSelectedImages(data);
            if (!StringUtils.isEmpty(list)) {
                int size = list.size();
//                if (requestCode == 1) {
//                    num1 += size;
//                } else if (requestCode == 2) {
//                    num2 += size;
//                }
                for (int i = 0; i < size; i++) {
                    ItemsCompileBean bean = new ItemsCompileBean();
                    bean.setPicker(true);
                    bean.setPic(list.get(i));
                    if (requestCode == 1) {//商家图片
                        list1.add(bean);
                        L.d("CompressPhotoUtils", "requestCode1:"+list.get(i));
                    } else if (requestCode == 2) {//商家详情图片
                        list2.add(bean);
                        L.d("CompressPhotoUtils", "requestCode2:"+list.get(i));
                    }
                }
                if (requestCode == 1) {//商家图片
                    isCover();
                    adapter1.setData(list1);
                    adapter1.notifyDataSetChanged();
                } else if (requestCode == 2) {//商家详情图片
                    adapter2.setData(list2);
                    adapter2.setStringArry(list2);
                    adapter2.notifyDataSetChanged();
                }
            }
        }
    }

    private boolean isCover() {//要是没有封面就设置第一张为封面（添加产品的时候）
        if (StringUtils.isEmpty(list1)) {
            return false;
        }
        int size = list1.size();
        for (int i = 0; i < size; i++) {
            ItemsCompileBean bean = list1.get(i);
            if (bean.isCover()) {
                return true;
            }
        }
        ItemsCompileBean bean = list1.get(0);
        bean.setCover(true);
        return false;
    }

    private void initRecyclerView() {
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL_LIST);
        itemDecoration.setDivider(new ColorDrawable(Color.TRANSPARENT));
        itemDecoration.setDividerHeight((int) UIBaseUtils.dp2px(this, 5));
        recyclerView1.addItemDecoration(itemDecoration);
        recyclerView2.addItemDecoration(itemDecoration);
        adapter1 = new ItemsCompileAdapter(this, 1);
        adapter1.setData(list1);
        adapter2 = new ItemsCompileAdapter(this, 2);
        adapter2.setData(list2);
        adapter2.setStringArry(list2);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView1.setAdapter(adapter1);
        recyclerView2.setAdapter(adapter2);

    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null) {
            bean = (MerchantItemsListBean) bundle.getSerializable("bean");
            pid = bundle.getString("pid");
        }
        isAdd = (bean == null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                done(0);//正常
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private static final String DIALOG = "dialog_fragment";


    @OnClick({R.id.sold_out_tv, R.id.delete_tv, R.id.ll_classify_to, R.id.pic1_iv, R.id.pic2_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sold_out_tv://下架
                AKDialog.getAlertDialog(this, getString(R.string.out_product), new AKDialog.AlertDialogListener() {
                    @Override
                    public void yes() {
                        UIHelper.ToastMessage(ItemCompileActivity.this, R.string.developing);
                    }
                });
                break;
            case R.id.delete_tv://删除
                AKDialog.getAlertDialog(this, getString(R.string.del_product), new AKDialog.AlertDialogListener() {
                    @Override
                    public void yes() {
                        delProduct();
                    }
                });
                break;
            case R.id.ll_classify_to://分类至
                if (StringUtils.isEmpty(beanList)) {
                    loadClass();
                } else {
                    dialog();
                }
                break;
            case R.id.pic1_iv://商品图片
                type = 1;
                choicePhotoWrapper();
                break;
            case R.id.pic2_iv://商品详情图片
                type = 2;
                choicePhotoWrapper();
                break;
        }
    }

    int type;

    //删除产品
    private void delProduct() {
        DelProductApi api = new DelProductApi();
        api.goods_id = bean.getId();
        api.mid = pid;
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                UIHelper.ToastMessage(ItemCompileActivity.this, response.getInfo());
                EventBus.getDefault().post(new CompileProductEvent());//刷新服务项目列表
                finish();
            }
        });
    }

    List<CategoryBean> beanList;

    private void loadClass() {
        ItemsCategoryApi api = new ItemsCategoryApi();
        api.mid = pid;
        api.token = api.getToken(this);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<NoPageListBean<CategoryBean>>() {
            @Override
            public void onResponse(NoPageListBean<CategoryBean> response) {
                beanList = response.data;
                if (StringUtils.isEmpty(beanList)) {//
                    IntentUtil.startActivity(ItemCompileActivity.this, CategoryActivity.class, pid);
                } else {
                    dialog();
                }
            }
        });
    }


    public void dialog() {
        if (mDialog == null) {
            mDialog = new ClassifyToDialogFragment();
            mDialog.setPid(pid);
            mDialog.setList(beanList);
        }
        mDialog.show(getSupportFragmentManager(), DIALOG);
    }

    CategoryBean categoryBean;//选择的分类数据


    //
    @Subscribe
    public void chooseCategoryEvent(ChooseCategoryEvent event) {
        categoryBean = event.getBean();
        categoryTv.setText(categoryBean.getName());//
    }

    //编辑分类或添加分类时调用
    @Subscribe
    public void addCategoryEvent(AddCategoryEvent event) {

        beanList = event.getList();
        mDialog = new ClassifyToDialogFragment();
        mDialog.setPid(pid);
        mDialog.setList(beanList);

        categoryBean = null;
        categoryTv.setText(R.string.choose_category);//
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
