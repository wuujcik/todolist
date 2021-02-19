package com.wuujcik.todolist.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wuujcik.todolist.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupAppBarMenu()
    }


    private fun setupAppBarMenu() {
        top_app_bar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_settings -> {
//                    val dialog = SettingsDialog()
//                    dialog.show(supportFragmentManager, SettingsDialog.TAG)
                    true
                }
                R.id.menu_about -> {
//                    val dialog = AboutDialog()
//                    dialog.show(supportFragmentManager, AboutDialog.TAG)
                    true
                }
                else -> false
            }
        }
    }
}
