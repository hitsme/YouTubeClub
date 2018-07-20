package com.hitsme.youtubeclub;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private WebView webView;
    /** 视频全屏参数 */
    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private View customView;
    private FrameLayout fullscreenContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private Intent intentVideo;
    private View mErrorView;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.webView);
        setupTransition();
        initWebView();
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupTransition() {
        Slide slide = new Slide(Gravity.LEFT);
        slide.setDuration(1000);
        slide.setInterpolator(new FastOutSlowInInterpolator());
        slide.excludeTarget(android.R.id.statusBarBackground, true);
        slide.excludeTarget(android.R.id.navigationBarBackground, true);
       // slide.excludeTarget(R.id.app_bar,true);
        getWindow().setExitTransition(slide);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            final Intent intentV=new Intent(this,WebVideoActivity.class);
            final Bundle bundleV=new Bundle();
            bundleV.putString("url","http://www.22kb.club/forum.php?forumlist=1&mobile=2");
            intentV.putExtras(bundleV);
            //startActivity(intentV);
            ActivityOptions options = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                options = ActivityOptions.makeSceneTransitionAnimation(this, webView, "");
            }
            startActivity(intentV, options.toBundle());
        } else if (id == R.id.nav_newactive) {
            final Intent intentV=new Intent(this,WebVideoActivity.class);
            final Bundle bundleV=new Bundle();
                    bundleV.putString("url","http://www.22kb.club/forum.php?mod=guide&view=newthread&mobile=2&device=360");
                    intentV.putExtras(bundleV);
                    startActivity(intentV);

        } else if (id == R.id.nav_search) {
            final Intent intentV = new Intent(this, WebVideoActivity.class);
            final Bundle bundleV = new Bundle();
            bundleV.putString("url", "http://www.22kb.club/search.php?mod=forum&mobile=2");
            intentV.putExtras(bundleV);
            startActivity(intentV);
        }  else if (id == R.id.nav_write) {
            final Intent intentV = new Intent(this, WebVideoActivity.class);
            final Bundle bundleV = new Bundle();
            bundleV.putString("url", "http://www.22kb.club/forum.php?forumlist=1&do=t9_post&mobile=2");
            intentV.putExtras(bundleV);
            startActivity(intentV);
        }else if (id == R.id.nav_login) {
            final Intent intentV=new Intent(this,WebVideoActivity.class);
            final Bundle bundleV=new Bundle();
            bundleV.putString("url","http://www.22kb.club/member.php?mod=logging&action=login&mobile=2");
            intentV.putExtras(bundleV);
            startActivity(intentV);
        } else if (id == R.id.nav_share) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.hitsme.youtubeclub.app")));
        } else if (id == R.id.nav_about) {
            final Intent intentV=new Intent(this,AboutActivity.class);
            startActivity(intentV);
        }else if (id == R.id.nav_aichat) {
            final Intent intentV=new Intent(this, com.hitsme.robot.chat.MainActivity.class);
            startActivity(intentV);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                FrameLayout frameLayout = new FrameLayout(MainActivity.this);
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
      //  Toast.makeText(webActivity.this,webView.getUrl(),Toast.LENGTH_SHORT).show();
       // webActivity.this.getWindow().getDecorView();

        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        fullscreenContainer = new webActivity.FullscreenHolder(MainActivity.this);
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
        while (webParentView!=null&&webParentView.getChildCount() > 0) {
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
        while (webParentView.getChildCount() > 0) {
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
    //记录用户首次点击返回键的时间
    private long firstTime = 0;
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                System.exit(0);
            }
        }

        return super.onKeyUp(keyCode, event);
    }
}
