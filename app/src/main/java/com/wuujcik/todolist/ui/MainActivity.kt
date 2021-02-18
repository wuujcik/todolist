package com.wuujcik.todolist.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.wuujcik.todolist.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var mainViewModelFactory: MainViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModelFactory = MainViewModelFactory(application)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory)
                .get(MainViewModel::class.java)

        setupAppBarMenu()

    }


    private fun setupAppBarMenu() {
        top_app_bar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_settings -> {

                    // Write a message to the database
                    val database = Firebase.database
                    val myRef = database.getReference("message")

                    myRef.setValue("Hello, Worlds!")

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

/**
 * Easier access to main activity.
 */
fun Fragment.mainActivity(): MainActivity {
    return (activity as MainActivity)
}
