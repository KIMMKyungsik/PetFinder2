package org.techtown.petfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import org.techtown.petfinder.databinding.ActivityDetailBinding


class DetailActivity : BaseActivity(){
    lateinit var binding : ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        setValues()
        setupEvents()

    }

    override fun setupEvents() {

    }

    override fun setValues() {

    }


}