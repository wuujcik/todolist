package com.wuujcik.todolist.ui

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.wuujcik.todolist.R
import com.wuujcik.todolist.MealListApp
import com.wuujcik.todolist.databinding.ActivityMainBinding
import com.wuujcik.todolist.di.MainActivityComponent
import com.wuujcik.todolist.utils.internetAvailable
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: MainViewModel
    lateinit var mainActivityComponent: MainActivityComponent
    private lateinit var binding: ActivityMainBinding
    private var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        mainActivityComponent =
            (application as MealListApp).appComponent.mainActivityComponent().create()
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

        if (internetAvailable(application as MealListApp)) {
            viewModel.refreshFromFirebase()
        }
    }

    override fun getTheme(): Resources.Theme {
        val theme: Resources.Theme = super.getTheme()
        theme.applyStyle(viewModel.themeId, true)
        return theme
    }

    private fun setupAppBarMenu() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_refresh -> {
                    viewModel.refreshFromFirebase()
                    true
                }
                else -> false
            }
        }
    }

    private fun setTheme() {
        setTheme(viewModel.themeId)
    }
}
