package org.techtown.petfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.techtown.adapters.ImageViewPagerAdapter
import org.techtown.datas.Petdata
import org.techtown.petfinder.databinding.ActivityDetailBinding
import org.techtown.petfinder.databinding.ActivityImageClickBinding

class ImageClickActivity : AppCompatActivity() {

    private lateinit var adapter: ImageViewPagerAdapter
    private lateinit var binding: ActivityImageClickBinding
    private lateinit var data: Petdata
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageClickBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        imageUi()

    }


    fun imageUi(){


        adapter = ImageViewPagerAdapter(data.pictures)
        binding.viewPager.adapter = adapter
        binding.dotsIndicator.setViewPager2(binding.viewPager)

    }

}







