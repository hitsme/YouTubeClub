package com.hitsme.youtubeclub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class webActivity extends Activity {

    private WebView webView;

    /** 视频全屏参数 */
    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private View customView;
    private FrameLayout fullscreenContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private Intent intentVideo;
    private View mErrorView;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.webView);
        initWebView();
        intentVideo=new Intent(this,WebVideoActivity.class);
    }

    @Override
    protected void onStop() {
        super.onStop();
        webView.reload();
    }


    public void initWebView() {
        WebChromeClient wvcc = new WebChromeClient();
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true); // 关键点
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 不加载缓存内容

        webView.setWebChromeClient(wvcc);
        WebViewClient wvc = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                showErrorPage();//显示错误页面
            };
        };
        webView.setWebViewClient(wvc);

        webView.setWebChromeClient(new WebChromeClient() {

            /*** 视频播放相关的方法 **/

            @Override
            public View getVideoLoadingProgressView() {
                FrameLayout frameLayout = new FrameLayout(webActivity.this);
                frameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                return frameLayout;
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                showCustomView(view, callback);
                // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//播放时横屏幕
            }

            @Override
            public void onHideCustomView() {
                hideCustomView();
                //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//不播放时竖屏
            }
        });

        // 加载Web地址
        webView.loadUrl("http://www.22kb.club/");
    }

    /** 视频播放全屏 **/
    private void showCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden();
            return;
        }
        Toast.makeText(webActivity.this,webView.getUrl(), Toast.LENGTH_SHORT).show();
        webActivity.this.getWindow().getDecorView();

        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        fullscreenContainer = new FullscreenHolder(webActivity.this);
        fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
        customView = view;
        setStatusBarVisibility(false);
        customViewCallback = callback;
    }

    /** 隐藏视频全屏 */
    private void hideCustomView() {
        if (customView == null) {
            return;
        }
        setStatusBarVisibility(true);
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        decor.removeView(fullscreenContainer);
        fullscreenContainer = null;
        customView = null;
        customViewCallback.onCustomViewHidden();
        webView.setVisibility(View.VISIBLE);
    }

    /** 全屏容器界面 */
    static class FullscreenHolder extends FrameLayout {

        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    private void setStatusBarVisibility(boolean visible) {
        int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    protected void showErrorPage() {
        ConstraintLayout webParentView = (ConstraintLayout)webView.getParent();
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
       // ActionBar ab = getSupportActionBar();
        //ab.setDisplayHomeAsUpEnabled(true);
        //ab.setDisplayShowHomeEnabled(true);
        hideErrorPage();
        initErrorPage();//初始化自定义页面
        while (webParentView.getChildCount() > 0) {
            webParentView.removeViewAt(0);
        }
        @SuppressWarnings("deprecation")
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewPager.LayoutParams.FILL_PARENT, ViewPager.LayoutParams.FILL_PARENT);
        webParentView.addView(mErrorView, 0, lp);
        mIsErrorPage = true;
    }
    boolean mIsErrorPage;

    /****
     * 把系统自身请求失败时的网页隐藏
     */
    protected void hideErrorPage() {
        ConstraintLayout webParentView = (ConstraintLayout)webView.getParent();
        mIsErrorPage = false;
        while (webParentView.getChildCount() > 1) {
            webParentView.removeViewAt(0);
        }
    }
    /***
     * 显示加载失败时自定义的网页
     */
    protected void initErrorPage() {
        if (mErrorView == null) {
            mErrorView = View.inflate(this, R.layout.network404, null);
            ImageView layout = (ImageView) mErrorView.findViewById(R.id.imageView2);
            layout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    webView.reload();
                }
            });
            mErrorView.setOnClickListener(null);
        }
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                /** 回退键 事件处理 优先级:视频播放全屏-网页回退-关闭页面 */
                if (customView != null) {
                    hideCustomView();
                } else if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }
}