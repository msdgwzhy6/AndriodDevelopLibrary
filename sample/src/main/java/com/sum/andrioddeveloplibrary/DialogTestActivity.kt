package com.sum.andrioddeveloplibrary

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.blankj.utilcode.util.ToastUtils
import com.sum.andrioddeveloplibrary.fragment.ItemListDialogFragment
import com.sum.library.app.BaseDialogFragment
import com.sum.library.view.sheet.DialogTimeChooseView
import com.sum.library.view.sheet.DialogChooseView
import kotlinx.android.synthetic.main.activity_dialog_test.*

class DialogTestActivity : AppCompatActivity(), ItemListDialogFragment.Listener {
    override fun onItemClicked(position: Int) {
        ToastUtils.showShort("pos=$position")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_test)

        bt1.setOnClickListener {
            ItemListDialogFragment.newInstance(3)
                    .show(supportFragmentManager, "3")
        }
        bt2.setOnClickListener {
            var time = ""
            if (it.tag is String) {
                time = it.tag as String
            }
            DialogTimeChooseView.Builder()
                    .setShowHours()
                    .setHours(time)
                    .setListener { _, content ->
                        it.tag = content
                        ToastUtils.showLong("time = $content")
                    }.showFast(this)

        }

        bt5.setOnClickListener {
            var time = "2018-08-18"
            if (it.tag is String) {
                time = it.tag as String
            }
            DialogTimeChooseView.Builder()
                    .setCurrentTime(time)
                    .setListener { _, content ->
                        it.tag = content
                        ToastUtils.showLong("date = $content")
                    }.showFast(this)

        }

        bt3.setOnClickListener { _ ->
            DialogChooseView().setMessage("message").setTitle("Test Title")
                    .setPos("Next").setCancel(true).setNeg("Cancel")
                    .setPosListener { ToastUtils.showLong("Next") }
                    .setNegListener { ToastUtils.showLong("Cancel") }
                    .show(supportFragmentManager, "show")
        }

        bt4.setOnClickListener {
            var index = 0
            if (it.tag is Int) {
                index = it.tag as Int
            }
            val data = arrayOf("单选1", "单选2", "单选3", "单选4", "单选5", "单选6", "单选7", "单选8", "单选9", "单选10", "单选11", "单选12")
            DialogTimeChooseView.Builder().setItems(data).setChooseIndex(index).setListener { pos, content ->
                it.tag = pos
                ToastUtils.showLong("pos=$pos,content$content")
            }.showFast(this)
        }

        bt6.setOnClickListener {
            DialogF().show(supportFragmentManager, "test")
        }
    }


    class DialogF : BaseDialogFragment() {
        override fun getLayoutId(): Int = R.layout.bsv_bottom_in_view

        override fun initParams(view: View) {
            view.findViewById<View>(R.id.fl_content).setOnClickListener { dismiss() }
            view.findViewById<View>(R.id.btn_cancel).setOnClickListener { dismiss() }

        }
    }
}
