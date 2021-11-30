package org.techtown.petfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import org.techtown.petfinder.databinding.ActivityMainBinding

class BottomNavMainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bottom_nav_main)
        setValues()
        setupEvents()

    }

    override fun setupEvents() {

    }

    override fun setValues() {

    }
}