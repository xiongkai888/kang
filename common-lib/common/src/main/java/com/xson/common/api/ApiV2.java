package com.xson.common.api;

import android.content.Context;

/**
 * @author Milk <249828165@qq.com>
 */
public abstract class ApiV2 extends AbstractApi {

    protected abstract String getUserId(Context context);

    protected abstract String getToken(Context context);
}
