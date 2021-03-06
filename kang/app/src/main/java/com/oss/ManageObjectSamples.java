package com.oss;

import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.CopyObjectRequest;
import com.alibaba.sdk.android.oss.model.CopyObjectResult;
import com.alibaba.sdk.android.oss.model.DeleteObjectRequest;
import com.alibaba.sdk.android.oss.model.DeleteObjectResult;
import com.alibaba.sdk.android.oss.model.HeadObjectRequest;
import com.alibaba.sdk.android.oss.model.HeadObjectResult;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;


/**
 * Created by xkai on 12/3/15.
 */
public class ManageObjectSamples {

    private OSS oss;
    private String testBucket;
    private String testObject;

    public ManageObjectSamples(OSS client, String testBucket, String testObject) {
        this.oss = client;
        this.testBucket = testBucket;
        this.testObject = testObject;
    }

    // 检查文件是否存在
    public boolean checkIsObjectExist() {
        try {
            if (oss.doesObjectExist(testBucket, testObject)) {
                Log.d("doesObjectExist", "object exist.");
                return true;
            } else {
                Log.d("doesObjectExist", "object does not exist.");
                return false;
            }
        } catch (ClientException e) {
            // 本地异常如网络异常等
            e.printStackTrace();
        } catch (ServiceException e) {
            // 服务异常
            Log.e("ErrorCode", e.getErrorCode());
            Log.e("RequestId", e.getRequestId());
            Log.e("HostId", e.getHostId());
            Log.e("RawMessage", e.getRawMessage());
        }
        return false;
    }

    // 只获取一个文件的元信息
    public void headObject() {
        // 创建同步获取文件元信息请求
        HeadObjectRequest head = new HeadObjectRequest(testBucket, testObject);

        OSSAsyncTask task = oss.asyncHeadObject(head, new OSSCompletedCallback<HeadObjectRequest, HeadObjectResult>() {
            @Override
            public void onSuccess(HeadObjectRequest request, HeadObjectResult result) {
                Log.d("headObject", "object Size: " + result.getMetadata().getContentLength());
                Log.d("headObject", "object Content Type: " + result.getMetadata().getContentType());
            }

            @Override
            public void onFailure(HeadObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });

        task.waitUntilFinished();
    }

    // 复制object到一个新的object，再把它copy出来的object删除，调用同步接口
    public void CopyAndDeleteObject() {
        // 创建copy请求
        String key="testCopy";
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(testBucket, testObject,
                testBucket, key);
        // 设置copy文件新的元信息(Content Type)
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("application/octet-stream");
        copyObjectRequest.setNewObjectMetadata(objectMetadata);

        try {
            // 发送同步copy请求
            CopyObjectResult copyResult = oss.copyObject(copyObjectRequest);
            // 获取copy文件的元信息
            HeadObjectRequest head = new HeadObjectRequest(testBucket, "testCopy");
            HeadObjectResult result = oss.headObject(head);

            // 同步删除该copy文件
            syncDeleteObject(key);
        }
        // 本地异常
        catch (ClientException e) {
            e.printStackTrace();
        }
        // 服务异常
        catch (ServiceException e) {
            Log.e("ErrorCode", e.getErrorCode());
            Log.e("RequestId", e.getRequestId());
            Log.e("HostId", e.getHostId());
            Log.e("RawMessage", e.getRawMessage());
        }

    }

    // 复制object到一个新的object，再把它copy出来的object删除，调用异步接口
    public void asyncCopyAndDeleteObject() {
        // 创建copy请求
        String key="testCopy";
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(testBucket, testObject,
                testBucket, key);

        //设置copy文件新元信息（Content Type）
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("application/octet-stream");
        copyObjectRequest.setNewObjectMetadata(objectMetadata);

        // 异步copy
        OSSAsyncTask copyTask = oss.asyncCopyObject(copyObjectRequest, new OSSCompletedCallback<CopyObjectRequest, CopyObjectResult>() {
            @Override
            public void onSuccess(CopyObjectRequest request, CopyObjectResult result) {
                    Log.d("copyObject", "copy success!");
            }

            @Override
            public void onFailure(CopyObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });

        // 等待直到操作完成
        copyTask.waitUntilFinished();

        asyncDeleteObject(key);
    }

    private boolean asyncDel;
    /**异步删除*/
    public void asyncDeleteObject(String key) {
        // 创建删除请求
        DeleteObjectRequest delete = new DeleteObjectRequest(testBucket, key);
        // 异步删除
        asyncDel=false;
        OSSAsyncTask deleteTask = oss.asyncDeleteObject(delete, new OSSCompletedCallback<DeleteObjectRequest, DeleteObjectResult>() {
            @Override
            public void onSuccess(DeleteObjectRequest request, DeleteObjectResult result) {
                Log.d("异步删除", "success!："+result.getUrl());
                asyncDel=true;

            }

            @Override
            public void onFailure(DeleteObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }

        });
        deleteTask.waitUntilFinished();
    }

    public boolean syncDeleteObject(String key) throws ClientException, ServiceException {
        // 同步删除该copy文件
        /*同步删除*/
        boolean syncDel = false;
        DeleteObjectRequest delete = new DeleteObjectRequest(testBucket, key);
        DeleteObjectResult deleteResult = oss.deleteObject(delete);
        if (deleteResult.getStatusCode() == 204) {
            Log.d("同步删除", "Success："+deleteResult.getUrl());
            syncDel =true;
        }
        return syncDel;
    }
}
