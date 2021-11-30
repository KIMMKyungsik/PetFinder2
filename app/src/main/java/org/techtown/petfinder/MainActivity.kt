package org.techtown.petfinder

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import org.techtown.petfinder.databinding.ActivityMainBinding


class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setValues()
        setupEvents()
        auth = FirebaseAuth.getInstance()
    }

    override fun setupEvents() {
        binding.signUpButton.setOnClickListener() {

            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)

        }

        binding.signInButton.setOnClickListener {
            val intent = Intent(this, ReviewActivity::class.java)
            startActivity(intent)

        }

    }

    override fun setValues() {

    }
}