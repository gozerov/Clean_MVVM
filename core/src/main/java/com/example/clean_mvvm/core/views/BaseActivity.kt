package com.example.clean_mvvm.core.views

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment

abstract class BaseActivity(@IdRes val fragmentContainerId: Int, @LayoutRes val layoutId: Int): AppCompatActivity() {

    abstract fun updateToolbar(message: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        navHostFragment = supportFragmentManager.findFragmentById(fragmentContainerId) as NavHostFragment
        navHostFragment.childFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, false)
    }

    override fun onDestroy() {
        navHostFragment.childFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
        super.onDestroy()
    }

    private val fragmentListener = object: FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            checkFragmentType()
        }
    }

    private fun checkFragmentType() {
        val fragment = getCurrentFragment()
        if (fragment is HasCustomBar) {
            updateToolbar(fragment.getCustomTitle())
        } else
            updateToolbar("App")
    }

    private lateinit var navHostFragment: NavHostFragment
    private fun getCurrentFragment() : Fragment? = navHostFragment.childFragmentManager.findFragmentById(fragmentContainerId)

}