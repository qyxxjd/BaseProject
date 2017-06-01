<p>
  <a href="https://developer.android.com/reference/android/os/Build.VERSION_CODES.html#GINGERBREAD"><img src="https://img.shields.io/badge/API-9%2B-blue.svg?style=flat" alt="API" /></a>
  <a href="LICENSE"><img src="https://img.shields.io/npm/l/express.svg?maxAge=2592000" alt="License" /></a>
</p>

`BaseProject`是一个简易的`Android`基础项目，方便您快速进行开发。

#### 基于`RxJava 1.X`的版本： [示例代码点这里](https://github.com/qyxxjd/RxJava2Demo)

- 包含基础项目的所有功能
- 集成`RxJava (v1.3.0)`、`RxAndroid (v1.2.1)`、`OkHttp (v3.3.1)`、`Retrofit (v2.0.2)`
- `RxActivity`、`RxFragment`
- 网络相关的一些封装

`Gradle`依赖
```gradle
dependencies {
    compile 'com.classic.android:rxjava1:1.0'
}
```

## 使用步骤

第一步：按需添加依赖

第二步：
```java
public class YourApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();

        ...

        final BasicProject.Builder builder = new BasicProject.Builder()
                .setDebug(BuildConfig.DEBUG)
                .setRootDirectoryName(getPackageName())
                //自定义异常信息处理，实现ICrashProcess
                .setExceptionHandler(new CustomCrashProcessImpl())
                .setLog(BuildConfig.DEBUG ? LogLevel.ALL : LogLevel.NONE);

        BasicProject.config(builder);
    }
}
```

## 代码示例

Activity示例
```java
public class TestActivity extends BaseActivity {
    private RecyclerView mRecyclerView;

    @Override public int getLayoutResId() {
        return R.layout.activity_main;
    }

    //初始化一些数据
    @Override public void initData() {
        super.initData();
        Intent intent = getIntent();
        params = intent.getStringExtra(...);
    }

    //初始化view
    @Override public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mRecyclerView = (RecyclerView) findViewById(R.id.main_rv);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        if(savedInstanceState == null){
            /**
             * 使用Fragment
             * 参数1：被替换为Fragment的视图id
             * 参数2：BaseFragment对象
             */
            changeFragment(R.id.fragment_layout, new ImageFragment());
        }

    }


    //以下为可选方法，根据需要进行重载.
    //方法执行顺序：
    //initPre() --> initData() --> initView(Bundle savedInstanceState) --> register()

    //这里可以注册一些广播、服务
    @Override public void register() { }
    //注销广播、服务,在onDestroy()内部执行
    @Override public void unRegister() { }
    //只有第一次才会执行，这里可以做一些界面功能引导
    @Override public void onFirst() { }
    //这个方法会在setContentView(...)方法之前执行
    @Override public void initPre() { }
    //view点击事件统一处理
    @Override public void viewClick(View v) { }
    @Override public void showProgress() { }
    @Override public void hideProgress() { }
}
```

Fragment示例
```java
public class TestFragment extends BaseFragment {
    private TextView mTitle;

    @Override public int getLayoutResId() {
        return R.layout.activity_listview_item;
    }

    @Override public void initView(View parentView, Bundle savedInstanceState) {
        super.initView(parentView, savedInstanceState);
        mTitle = (TextView) parentView.findViewById(R.id.item_title_tv);
    }

    //以下为可选方法，根据需要进行重载.
    //方法执行顺序：
    //initData() --> initView(View parentView, Bundle savedInstanceState) --> register()

    //这里可以注册一些广播、服务
    @Override public void register() { }
    //注销广播、服务, 在onDestroyView()内部执行
    @Override public void unRegister() { }
    //只有第一次才会执行，这里可以做一些界面功能引导
    @Override public void onFirst() { }
    @Override public void initData() { }
    //view点击事件统一处理
    @Override public void viewClick(View v) { }
    @Override public void showProgress() { }
    @Override public void hideProgress() { }

    //Fragment被切换到前台时调用
    @Override public void onFragmentShow() { }
    //Fragment被切换到后台时调用
    @Override public void onFragmentHide() { }
}
```

启动页示例
```java
public class SplashActivity extends BaseSplashActivity {

    @Override protected void setSplashResources(List<SplashImgResource> resources) {
        /**
         * SplashImgResource参数:
         * mResId - 图片资源的ID。
         * playerTime - 图片资源的播放时间，单位为毫秒。。
         * startAlpha - 图片资源开始时的透明程度。0-255之间。
         * isExpand - 如果为true，则图片会被拉伸至全屏幕大小进行展示，否则按原大小展示。
         */
        resources.add(new SplashImgResource(R.mipmap.splash, 1500, 100f, true));
        resources.add(new SplashImgResource(R.mipmap.splash1, 1500, 100f, true));
        resources.add(new SplashImgResource(R.mipmap.splash2, 1500, 100f, true));
    }
    
    @Override protected boolean isAutoStartNextActivity() {
        return false;
    }
    @Override protected Class<?> nextActivity() {
        return null;
        //如果isAutoStartNextActivity设置为true,这里需要指定跳转的activity
        //return MainActivity.class;
    }
    
    @Override protected void runOnBackground() {
        //这里可以执行耗时操作、初始化工作
        //请注意：如果执行了耗时操作，那么启动页会等到耗时操作执行完才会进行跳转
        //try {
        //  Thread.sleep(15 * 1000);
        //} catch (InterruptedException e) {
        //  e.printStackTrace();
        //}
    }
}
```

打印日志  [更多使用方法点这里](https://github.com/elvishew/xLog)
```java
XLog.d(content);
XLog.e(content);
XLog.w(content);
XLog.v(content);
XLog.json(jsonContent);
XLog.xml(xmlContent);
```


Android6.0权限管理  [更多使用方法点这里](https://github.com/googlesamples/easypermissions)
```java
//以使用相机为例，在Activity/Fragment添加以下代码

private static final int REQUEST_CODE_CAMERA = 101;//请求相机权限的requestCode

@AfterPermissionGranted(REQUEST_CODE_CAMERA)
public void useCamera() {
    if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
        ToastUtil.showToast(getApplicationContext(), "相机权限已授权,可以开始使用相机了");
    } else {
        //请求权限
        EasyPermissions.requestPermissions(this, "应用需要访问你的相机进行拍照",
                                           REQUEST_CODE_CAMERA, Manifest.permission.CAMERA);
    }
}

@Override
public void onPermissionsGranted(int requestCode, List<String> perms) {
    //用户授权成功
}

@Override
public void onPermissionsDenied(int requestCode, List<String> perms) {
    //用户拒绝授权
}
```


## 感谢

[XLog](https://github.com/elvishew/xLog)

[EasyPermissions](https://github.com/googlesamples/easypermissions)


## 关于

* Blog: [http://blog.csdn.net/qy1387](http://blog.csdn.net/qy1387)
* Email: [pgliubin@gmail.com](http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=pgliubin@gmail.com)

## License

```
Copyright 2015 classic

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
```
