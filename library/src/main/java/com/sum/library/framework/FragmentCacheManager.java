package com.sum.library.framework;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Sum on 15/11/26.
 */
public class FragmentCacheManager {

    public static FragmentCacheManager instance() {
        return new FragmentCacheManager();
    }

    private FragmentCacheManager() {
        mCacheFragment = new HashMap<>();
    }

    //Activity 中的Fragment管理
    private FragmentManager mFragmentManager;

    private FragmentActivity mBaseActivity;

    private int mContainerId;

    private long mLastBackTime;

    private BootCallBackListener mListener;

    //缓存的Fragment集合数据
    private HashMap<Object, FragmentInfo> mCacheFragment;
    //
    private boolean mCheckRoot = true;

    //当前展示的Fragment
    private Fragment mCurrentFragment;
    private Object mCurrentFragmentIndex = null;

    public void setUp(FragmentActivity activity, @IdRes int containerId) {
        this.mBaseActivity = activity;
        this.mContainerId = containerId;
        mFragmentManager = activity.getSupportFragmentManager();
    }

    public void setUp(Fragment fragment, @IdRes int containerId) {
        this.mContainerId = containerId;
        mFragmentManager = fragment.getChildFragmentManager();
        //Fragment所在的Activity
        mBaseActivity = fragment.getActivity();
    }

    /**
     * 获取所有显示过的缓存的Fragment
     * mFragmentManager.getFragments() 只能获取Add状态的Fragment
     */
    public List<Fragment> getCacheFragment() {
        ArrayList<Fragment> list = new ArrayList<>();
        Set<Map.Entry<Object, FragmentInfo>> entries = mCacheFragment.entrySet();
        for (Map.Entry<Object, FragmentInfo> entry : entries) {
            FragmentInfo info = entry.getValue();
            if (info.fragment != null) {
                list.add(info.fragment);
            }
        }
        return list;
    }

    /**
     * 获取缓存的Fragment
     */
    public Fragment getCacheFragment(Object index) {
        FragmentInfo info = mCacheFragment.get(index);
        if (info == null) {
            return null;
        }
        return info.fragment;
    }

    /**
     * 显示index对应的Fragment
     */
    public Fragment setCurrentFragment(Object index) {
        if (index == mCurrentFragmentIndex) {
            return mCurrentFragment;
        }
        FragmentInfo info = mCacheFragment.get(index);
        mCurrentFragmentIndex = index;
        if (info != null) {
            return goToThisFragment(info);
        }
        return null;
    }

    /**
     * 添加Fragment到管理栈里，同一个实力对象只会创建一次
     * 功能实现原理FragmentTabhost相同,注意hide和detach区别
     */

    public void addFragmentToCache(Object index, Class<?> clss) {
        FragmentInfo info = createInfo(clss.getName(), clss, null);
        mCacheFragment.put(index, info);
    }

    /**
     * 要实现同一个对象多次创建必须通过不同的Tag来做唯一标示
     *
     * @param index Fragment对应的索引，通过索引找到对应的显示Fragment
     * @param c     需要创建的Fragment.class 文件
     * @param tag   Fragment的唯一标示
     * @param args  Bundle 会传递给生产的Fragment对象，
     */
    public void addFragmentToCache(Object index, Class<?> c, String tag, Bundle args) {
        FragmentInfo info = createInfo(tag, c, args);
        mCacheFragment.put(index, info);
    }


    public void setListener(BootCallBackListener listener) {
        this.mListener = listener;
    }

    public void setCheckRoot(boolean checkRoot) {
        this.mCheckRoot = checkRoot;
    }

    private static final class FragmentInfo {
        private final String tag;
        private final Class<?> clss;
        private final Bundle args;
        Fragment fragment;

        FragmentInfo(String _tag, Class<?> _class, Bundle _args) {
            tag = _tag;
            clss = _class;
            args = _args;
        }
    }

    private FragmentInfo createInfo(String tag, Class<?> clss, Bundle args) {
        return new FragmentInfo(tag, clss, args);
    }

    private Fragment goToThisFragment(FragmentInfo param) {
        int containerId = mContainerId;
        Class<?> cls = param.clss;
        if (cls == null) {
            return null;
        }
        try {
            String fragmentTag = param.tag;
            //通过Tag查找活动的Fragment，相同到Fragment可以创建多个实力对象通过设置不同到Tag
            //在系统回收的情况下，FragmentManager会自动保存Fragment，findFragmentByTag找到新的实例对象
            Fragment fragment = mFragmentManager.findFragmentByTag(fragmentTag);
            if (fragment == null) {
                //创建对象将数据传递给Fragment对象
                fragment = (Fragment) cls.newInstance();
            }
            if (param.args != null) {
                fragment.setArguments(param.args);
            }
            param.fragment = fragment;

            if (mCurrentFragment != null && mCurrentFragment != fragment) {
                //去除跟Activity关联的Fragment
                //detach会将Fragment所占用的View从父容器中移除，但不会完全销毁，还处于活动状态
                mFragmentManager.beginTransaction().detach(mCurrentFragment).commit();
            }

            FragmentTransaction ft = mFragmentManager.beginTransaction();
            if (fragment.isDetached()) {
                //重新关联到Activity
                ft.attach(fragment);
            } else if (fragment.isHidden()) {
                //显示fragment
                ft.show(fragment);
            } else {
                if (!fragment.isAdded()) {
                    ft.add(containerId, fragment, fragmentTag);
                }
            }
            mCurrentFragment = fragment;
            ft.commitAllowingStateLoss();
            return fragment;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void onBackPress() {
        if (mBaseActivity.isTaskRoot() || !mCheckRoot) {
            int cnt = mFragmentManager.getBackStackEntryCount();
            long secondClickBackTime = System.currentTimeMillis();
            if (cnt < 1 && (secondClickBackTime - mLastBackTime) > 2000) {
                if (mListener != null) {
                    mListener.rootCallBack();
                }
                mLastBackTime = secondClickBackTime;
            } else {
                doReturnBack();
            }
        } else {
            doReturnBack();
        }
    }

    public void onFastBackClick() {
        long secondClickBackTime = System.currentTimeMillis();
        if ((secondClickBackTime - mLastBackTime) > 2000) {
            if (mListener != null) {
                mListener.rootCallBack();
            }
            mLastBackTime = secondClickBackTime;
        } else {
            mBaseActivity.finish();
        }
    }

    public void popTopBackStack() {
        int count = mFragmentManager.getBackStackEntryCount();
        if (count > 0) {
            mFragmentManager.popBackStackImmediate();
        }
    }

    private void doReturnBack() {
        int count = mFragmentManager.getBackStackEntryCount();
        if (count < 1) {
            mBaseActivity.finish();
        } else {
            mFragmentManager.popBackStackImmediate();
        }
    }

    public boolean removeShowFragment() {
        if (mCurrentFragment != null && mCurrentFragmentIndex != null) {
            //去除跟Activity关联的Fragment
            mFragmentManager.beginTransaction().detach(mCurrentFragment).commit();
            mCurrentFragmentIndex = null;
            return true;
        }
        return false;
    }

    public interface BootCallBackListener {
        //返回键事件触发
        void rootCallBack();
    }
}
