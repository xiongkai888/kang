package com.oss;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.lanmei.kang.R;


public class ActivityOss extends Activity {
    private OSS oss;

    // 运行sample前需要配置以下字段为有效的值

    private static final String accessKeyId = OssUserInfo.accessKeyId;
    private static final String accessKeySecret = OssUserInfo.accessKeySecret;
    private static final String uploadFilePath = "/storage/emulated/0/ckw/img/head1473820263615.jpeg";

    private static final String testBucket = OssUserInfo.testBucket;
    private static final String uploadObject = "wlyg/img/1473820263615.jpeg";
    private static final String downloadObject = "sampleObject";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oss_main);

        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        oss = new OSSClient(getApplicationContext(), OssUserInfo.endpoint, credentialProvider, conf);

        // 上传
        Button upload = (Button) findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new PutObjectSamples(oss, testBucket, uploadObject, uploadFilePath).asyncPutObjectFromLocalFile(new OssUploadListener() {
                            @Override
                            public void onUpLoadListener(boolean isSuccess, String path) {

                            }
                        });
                    }
                }).start();
            }
        });

        // 删除
        Button delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ManageObjectSamples manageObjectSamples =   new ManageObjectSamples(oss,testBucket,uploadObject);
                        if (manageObjectSamples.checkIsObjectExist())
                            manageObjectSamples.asyncDeleteObject(uploadObject);
                    }
                }).start();
            }
        });

        // 下载
        Button download = (Button) findViewById(R.id.download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new GetObjectSamples(oss, testBucket, downloadObject).asyncGetObjectSample();
                    }
                }).start();
            }
        });

        // 罗列
        Button list = (Button) findViewById(R.id.list);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new ListObjectsSamples(oss, testBucket).asyncListObjectsWithPrefix();
                    }
                }).start();
            }
        });

        // manage
        Button manage = (Button) findViewById(R.id.manage);
        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new ManageObjectSamples(oss, testBucket, uploadObject).headObject();
                    }
                }).start();
            }
        });

        // multipart上传
        Button multipart = (Button) findViewById(R.id.multipart);
        multipart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            new MultipartUploadSamples(oss, testBucket, uploadObject, uploadFilePath).multipartUpload();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });


        // 断点上传
        Button resumable = (Button) findViewById(R.id.resumable);
        resumable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new ResuambleUploadSamples(oss, testBucket, uploadObject, uploadFilePath).resumableUpload();
                    }
                }).start();
            }
        });

        // 签名URL
        Button sign = (Button) findViewById(R.id.sign);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new SignURLSamples(oss, testBucket, uploadObject, uploadFilePath).presignConstrainedURL();
                    }
                }).start();
            }
        });

        // bucket管理
        Button bucket = (Button) findViewById(R.id.bucket);
        bucket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new ManageBucketSamples(oss, testBucket, uploadFilePath).deleteNotEmptyBucket();
                    }
                }).start();
            }
        });
    }
}
