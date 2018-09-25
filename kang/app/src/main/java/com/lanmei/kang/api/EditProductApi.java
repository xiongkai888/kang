package com.lanmei.kang.api;

/**
 * Created by xkai on 2018/2/8.
 */

public class EditProductApi extends KangApi{

    public String pid;//商品id(添加时不传)
    public String mid;//商家id(编辑时可不传)
    public String name;//商品名称
    public String img;//商品图片
    public String content;//商品描述
    public String pic;//商品banner图(多张用,分隔)
    public String file;//商品详情图片(多张用,分隔)
    public String sell_price;//价格
    public String category_id;//分类id(从属多个分类用,分隔)
    public int is_del;//0正常2下架


    @Override
    protected String getPath() {
        return "member/editProduct";
    }

    @Override
    public Method requestMethod() {
        return Method.POST;
    }
}
