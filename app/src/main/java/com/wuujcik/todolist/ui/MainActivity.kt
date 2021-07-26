package com.wuujcik.todolist.ui

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.wuujcik.todolist.R
import com.wuujcik.todolist.TodoListApp
import com.wuujcik.todolist.databinding.ActivityMainBinding
import com.wuujcik.todolist.di.MainActivityComponent


class MainActivity : AppCompatActivity() {

    lateinit var mainActivityComponent: MainActivityComponent
    private lateinit var binding: ActivityMainBinding
    private var database: FirebaseDatabase? = null
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        mainActivityComponent =
            (application as TodoListApp).appComponent.mainActivityComponent().create()
        mainActivityComponent.inject(this)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setTheme()
        setContentView(binding.root)

        if (database == null) {
            database?.setPersistenceEnabled(true)
            database = Firebase.database
        }
        setupAppBarMenu()
    }

    override fun getTheme(): Resources.Theme {
        val theme: Resources.Theme = super.getTheme()
        theme.applyStyle(viewModel.themeId, true)
        return theme
    }

    private fun setupAppBarMenu() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                    true
                }
//                R.id.menu_theme_standard -> {
//                    if (viewModel.themeId != R.style.Theme_Todo) {
//                        viewModel.changeTheme(R.style.Theme_Todo)
//                        recreate()
//                    }
//                    true
//                }
//                R.id.menu_theme_blue -> {
//                    if (viewModel.themeId != R.style.Theme_Todo_New) {
//                        viewModel.changeTheme(R.style.Theme_Todo_New)
//                        recreate()
//                    }
//                    true
//                }
                else -> false
            }
        }
    }

    private fun setTheme() {
        setTheme(viewModel.themeId)
    }
}
