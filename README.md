
## 个人开发库

-  [快速开发的基础功能库](https://github.com/Sum-sdl/AndriodDevelopLibrary)
 
-  [RecyclerView通用适配器库](https://github.com/Sum-sdl/RvAdapter)

-  [常用开源库总结连接](https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/sample/%E5%B8%B8%E7%94%A8%E5%BC%80%E6%BA%90%E5%BA%93.md)

-  [后台接口开发规范](https://github.com/Sum-sdl/StudyNode/blob/master/TeamWork/Interface_development_standard.md)


<table border="1"  bgcolor="#cccccc" cellpadding="7">
 <caption style ="text-align:center;font-size:16px;font-weight:bold;color:#000;">扫码下载</caption>
    <tr style ="background:#f2fbfe !important;">
        <td >
        <img src="https://raw.githubusercontent.com/Sum-sdl/AndriodDevelopLibrary/master/sample/demoUi/download_demo.png"   height="120" width="120" >
        </td>
        <td>
        <img src="https://raw.githubusercontent.com/Sum-sdl/AndriodDevelopLibrary/master/sample/demoUi/project.png"  height="120" width="120"  >
        </td>
    </tr>
    <tr>
        <td style ="text-align:center"><a href="https://raw.githubusercontent.com/Sum-sdl/AndriodDevelopLibrary/master/sample/other/sample-release.apk">测试Demo下载</a></td>
        <td style ="text-align:center"><a href="https://gitee.com/Sum-sdl/code_project" target="_blank">其他项目地址</a></td>
    </tr>
</table>
</br>


---

##### Dependency

[![JitPack](https://jitpack.io/v/Sum-sdl/AndriodDevelopLibrary.svg)](https://jitpack.io/#Sum-sdl/AndriodDevelopLibrary)

 ```gradle
 dependencies {
        implementation 'com.github.Sum-sdl:AndriodDevelopLibrary:2.3.0
     }
 ```
 **2.3.0 升级库并发布到JCenter**

### 项目功能介绍

> **结构模块**
>
- [JetPack框架封装 --> ViewModel+LiveData (App->ViewModel->DataRepository)](https://github.com/Sum-sdl/AndriodDevelopLibrary/tree/master/sample/src/main/java/jetpack/demo/framework)
- Fragment的管理类[FragmentCacheManager](https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/library/src/main/java/com/sum/library/framework/FragmentCacheManager.java)
用来管理多个Fragment的切换操作（如App主页的底部按钮）
- RecyclerView的通用适配器[RvAdapter](https://github.com/Sum-sdl/RvAdapter)
- [LiveDataEventBus](https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/library/src/main/java/com/sum/library/utils/LiveDataEventBus.java)->[测试代码](https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/sample/src/main/java/jetpack/demo/NewStartActivity.kt)基于LiveData搞定EventBus全部功能。支持Observer可见刷新和ObserverForever一直监听

> **UI组件模块**
>
- 底部滑动出来的通用浮层的[BaseBottomSheetFragment](https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/library/src/main/java/com/sum/library/view/sheet/BaseBottomSheetFragment.java)
- 底部滑动通用浮层案例[DialogTimeChooseView](https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/library/src/main/java/com/sum/library/view/sheet/DialogTimeChooseView.java)
支持时间，日期选择，单选滚动功能
- 统一的空UI展示[PubEmptyView](https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/library/src/main/java/com/sum/library/view/widget/PubEmptyView.java),类似ViewStub的功能实现,调用setVisibility()的时候才会初始化View的内容
- 统一的标题展示[PubTitleView](https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/library/src/main/java/com/sum/library/view/widget/PubTitleView.kt)

> **集成模块**
>
-  集成[Retrofit2](http://square.github.io/retrofit/)网络框架（未添加RxJava）[BaseDynamicInterceptor](https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/library/src/main/java/com/sum/library/net/token/BaseDynamicInterceptor.java)请求公共参数添加
-  集成[Glide](https://github.com/bumptech/glide)图片框架
-  推荐[SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout/blob/master/art/md_property.md)下拉刷新框架，需要手动添加

> **基础功能模块**
>
- [图片预览功能（支持手势）](https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/library/src/main/java/com/sum/library/ui/image/AppImageUtils.java)
- [自定义相册选择功能（支持预览，多选，拍照）](https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/library/src/main/java/com/sum/library/ui/image/AppImageUtils.java)
- [图片裁剪功能](https://github.com/Sum-sdl/AndriodDevelopLibrary/blob/master/library/src/main/java/com/sum/library/ui/image/AppImageUtils.java)

> **功能图片展示**
>
<div align="center">
<img src="https://github.com/Sum-sdl/AndriodDevelopLibrary/raw/master/sample/demoUi/img.jpg" height="330" width="190" >
<img src="https://github.com/Sum-sdl/AndriodDevelopLibrary/raw/master/sample/demoUi/bt_1.jpg" height="330" width="190" >
<img src="https://github.com/Sum-sdl/AndriodDevelopLibrary/raw/master/sample/demoUi/bt_2.jpg" height="330" width="190" >
<img src="https://github.com/Sum-sdl/AndriodDevelopLibrary/raw/master/sample/demoUi/bt_3.jpg" height="330" width="190" >
</div>
<div align="center">
<img src="https://github.com/Sum-sdl/AndriodDevelopLibrary/raw/master/sample/demoUi/img1.jpg" height="330" width="190" >
<img src="https://github.com/Sum-sdl/AndriodDevelopLibrary/raw/master/sample/demoUi/img3.jpg" height="330" width="190" >
<img src="https://github.com/Sum-sdl/AndriodDevelopLibrary/raw/master/sample/demoUi/img4.jpg" height="330" width="190" >
<img src="https://github.com/Sum-sdl/AndriodDevelopLibrary/raw/master/sample/demoUi/img5.jpg" height="330" width="190" >
</div>

> **JetPack框架结构**
>
<img src="https://github.com/Sum-sdl/AndriodDevelopLibrary/raw/master/sample/demoUi/architecture.png">


