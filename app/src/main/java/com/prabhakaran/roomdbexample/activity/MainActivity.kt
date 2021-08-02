package com.prabhakaran.roomdbexample.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.prabhakaran.roomdbexample.fragment.FriendsFragment
import com.prabhakaran.roomdbexample.fragment.ToolsFragment
import com.prabhakaran.roomdbexample.R
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by P.Prabhakaran on 09,November,2019
 **/
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private var toolsFragment: ToolsFragment? = null
    private var friendsFragment: FriendsFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        bottomNavigation.setOnNavigationItemSelectedListener(this)
        bottomNavigation.menu.findItem(R.id.menu_tools).isChecked = true
        //initialize tools list fragment for default page
        defaultPage()
    }

    private fun defaultPage() {
        toolsFragment = ToolsFragment()
        toolsFragment?.let { supportFragmentManager.beginTransaction().replace(R.id.frame_container, it).commit() }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.menu_tools -> {
                defaultPage()
            }
            R.id.menu_friends -> {
                friendsFragment = FriendsFragment()
                friendsFragment?.let {
                    supportFragmentManager.beginTransaction().replace(R.id.frame_container, it).commit()
                }
            }


        }
        return true

    }


}
