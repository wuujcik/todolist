package com.wuujcik.todolist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.wuujcik.todolist.R
import com.wuujcik.todolist.databinding.ActivityMainBinding
import com.wuujcik.todolist.utils.internetAvailable
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding
    private var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        if (database == null) {
            database?.setPersistenceEnabled(true)
            database = Firebase.database
        }
        setupAppBarMenu()

        if (internetAvailable(this.application)) {
            viewModel.refreshFromFirebase()
        }
    }

    private fun setupAppBarMenu() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_refresh -> {
                    if (internetAvailable(this.application)) {
                        viewModel.refreshFromFirebase()
                    } else {
                        Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_LONG)
                    }
                    true
                }
                else -> false
            }
        }
    }
}
