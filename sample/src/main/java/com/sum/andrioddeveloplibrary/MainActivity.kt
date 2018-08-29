package com.sum.andrioddeveloplibrary

import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ToastUtils
import com.sum.andrioddeveloplibrary.fragment.ItemListDialogFragment
import com.sum.andrioddeveloplibrary.net.NetActivity
import com.sum.andrioddeveloplibrary.testActivity.ServiceTestActivity
import com.sum.library.app.BaseActivity
import com.sum.library.framework.AppDownloadManager
import jetpack.demo.NewStartActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), ItemListDialogFragment.Listener {
    override fun onItemClicked(position: Int) {
        ToastUtils.showShort("click->" + position)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun statusBarTranslate(): Boolean {
        return false
    }

    override fun initParams() {

        b1.setOnClickListener { startActivity(StickyActivity::class.java) }
        b2.setOnClickListener { startActivity(NetActivity::class.java) }
        b3.setOnClickListener { startActivity(SwipeActivity::class.java) }
        b4.setOnClickListener { startActivity(CustomViewActivity::class.java) }
        b5.setOnClickListener {
            startActivity(DialogTestActivity::class.java)
        }
        b6.setOnClickListener {
            val url = "http://app.house365.com/d/house365-rent-XiaZaiYe-v3.6.8.apk"
            AppDownloadManager().setActivity(this).setLabel("更新内容")
                    .setUrl(url)
                    .setIsForceDownload(false).start()

        }
        b7.setOnClickListener {
            startActivity(SplashActivity::class.java)
        }
        b8.setOnClickListener {
            startActivity(LibWidgetUseActivity::class.java)
        }
        b9.setOnClickListener {
            startActivity(LibUIActivity::class.java)
            overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit)
        }

        b12.setOnClickListener {
            startActivity(ServiceTestActivity::class.java)
        }
        b13.setOnClickListener {
            startActivity(EncryptionActivity::class.java)
        }
        b14.setOnClickListener {
            startActivity(NewStartActivity::class.java)
        }

        b15.setOnClickListener {
            val intent = Intent()
            intent.action = tv_b.text.toString()
            sendBroadcast(intent)
            ToastUtils.showLong("intent.action->" + intent.action)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PermissionUtils.permission(*PermissionConstants.getPermissions(PermissionConstants.STORAGE)).request()
    }

}
