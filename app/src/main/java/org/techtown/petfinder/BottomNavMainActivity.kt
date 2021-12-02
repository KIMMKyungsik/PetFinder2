package org.techtown.petfinder

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.techtown.petfinder.databinding.ActivityBottomNavMainBinding
import java.util.*

class BottomNavMainActivity : BaseActivity() {

    lateinit var binding: ActivityBottomNavMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bottom_nav_main)
        setValues()
        setupEvents()


    }


    override fun setupEvents() {



//
//
        }



    override fun setValues() {


    }


  }




