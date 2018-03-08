package com.jiafeng.codegun.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jiafeng.codegun.R;
import com.jiafeng.codegun.http.ProgressHelper;
import com.jiafeng.codegun.http.ProgressResponseListener;
import com.jiafeng.codegun.http.UIProgressResponseListener;
import com.jiafeng.codegun.model.VersionData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yangshuquan on 2018/3/5.
 */

public class UpgradeActivity extends Activity {
    VersionData versionData;
    TextView contentTv;
    TextView progressTv;
    Button cancelBtn;
    Button confirmBtn;
    LinearLayout buttonBar;
    ProgressBar captionProgressBar;

    public static Intent getCallIntent(Context context, VersionData versionData) {
        return new Intent(context, UpgradeActivity.class).putExtra("data", versionData);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.upgrade_dialog);

        initView();
        initData();
    }

    private void initView() {
        cancelBtn = findViewById(R.id.cancelBtn);
        confirmBtn = findViewById(R.id.confirmBtn);
        contentTv = findViewById(R.id.contentTv);
        progressTv = findViewById(R.id.progressTv);
        buttonBar = findViewById(R.id.buttonBar);
        captionProgressBar = findViewById(R.id.captionProgressBar);
    }

    private void initData() {
        this.versionData = getIntent().getParcelableExtra("data");
        if (this.versionData == null) {
            finish();
            return;
        }

        contentTv.setText(versionData.message);

        if (versionData.isForce) {
            cancelBtn.setText(getResources().getString(R.string.version_exit));
        } else {
            cancelBtn.setText(getResources().getString(R.string.version_not_update));
        }

        confirmBtn.setText(getResources().getString(R.string.version_upgrade_immediately));

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUploadClick();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    void onUploadClick() {
        buttonBar.setVisibility(View.GONE);

        progressTv.setVisibility(View.VISIBLE);
        captionProgressBar.setVisibility(View.VISIBLE);

        final String filePath = getDiskCacheDir(this) + "/app.apk";

        downloadFile(versionData.downloadUrl, filePath, new UIProgressResponseListener() {

            @Override
            public void onFailure(Throwable error) {
                //下载失败
                finish();
            }

            @Override
            public void onUIResponseProgress(long bytesRead, long contentLength, boolean done) {
               // LogUtils.log("hdr", "进度:", bytesRead, "/", contentLength);
                int progress = (int) (bytesRead * 100 / contentLength);
                captionProgressBar.setProgress(progress);
                progressTv.setText(progress + "%");
                if (done) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 启动新的activity
                    // 设置Uri和类型
                    intent.setDataAndType(Uri.fromFile(new File(filePath)),
                            "application/vnd.android.package-archive");
                    startActivity(intent);
                }
            }
        });
    }


    public Call downloadFile(final String url, final String filePath, final ProgressResponseListener progressResponseListener) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient client = ProgressHelper.addProgressResponseListener(progressResponseListener);
        Call call = client.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, final IOException e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        progressResponseListener.onFailure(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
              //  LogUtils.log("http", "文件路径是：", filePath);
                File file = new File(filePath);
                //先确保父目录存在
                File parent = file.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                //如果之前有这个文件，则把这个文件上删除
                if (file.exists()) {
                    file.delete();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024 * 4];
                InputStream inputStream = response.body().byteStream();
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, length);
                }
                inputStream.close();
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        });
        return call;
    }

    public  String getDiskCacheDir(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                && !Environment.isExternalStorageRemovable()) {
            File file = context.getExternalCacheDir();
            if (file != null) {
                cachePath = file.getPath();
            } else {
                return context.getCacheDir().getPath();
            }

        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

}
