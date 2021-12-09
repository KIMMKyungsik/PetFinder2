package org.techtown.petfinder

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.techtown.adapters.ImageViewPagerAdapter
import org.techtown.datas.Petdata
import org.techtown.petfinder.databinding.ActivityDetailBinding


class DetailActivity : AppCompatActivity(){


    companion object {
        private const val PET_DATA = "PET_DATA"

        public fun startActivity(fragment: Fragment, data: Petdata) {
            val intent = Intent(fragment.context, DetailActivity::class.java).apply {
                putExtra(PET_DATA, data)
            }

            fragment.startActivity(intent)
        }
    }

    private lateinit var data: Petdata
    private lateinit var binding: ActivityDetailBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    private lateinit var adapter: ImageViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.data = intent?.getParcelableExtra<Petdata>(PET_DATA)
            ?: savedInstanceState?.getParcelable<Petdata>(PET_DATA)
                    ?: run {
                finish()
                return
            }

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        initUi()


    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(PET_DATA, data)

        super.onSaveInstanceState(outState)
    }
    private fun initUi() {

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        if (data.type == 0) {
            binding.dateTitleTextView.text = "실종일"
            binding.locationTitleTextView.text = "실종위치"
            binding.contactButton.text = "펫 주인에게 전화걸기"

        } else {
            binding.dateTitleTextView.text = "발견일"
            binding.locationTitleTextView.text = "발견위치"
            binding.contactButton.text = "발견자에게 전화걸기"
        }

        adapter = ImageViewPagerAdapter(data.pictures).apply {
            setOnClickListener { position, arrayList ->
                ImageClickActivity.startActivity(this@DetailActivity, arrayList, position)
            }
        }
        binding.viewPager.adapter = adapter
        binding.dotsIndicator.setViewPager2(binding.viewPager)

        binding.titleTextView.text = data.title
        binding.contentTextView.text = data.content
        binding.raceTextView.text = data.race
        binding.ageTextView.text = data.age
        binding.sexTextView.text = data.sex
        binding.dateTextView.text = DateUtils.formatDateTime(
            this@DetailActivity, data.date.time,
            DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_SHOW_DATE
        )
        binding.locationTextView.text = "${data.area} ${data.location}"
        binding.contactTextView.text = data.writerContact


        binding.contactButton.setOnClickListener {

            val intent = Intent(
                Intent.ACTION_DIAL,
                Uri.parse("tel:" + data.writerContact.replace("[- ]", "").trim())
            )
            startActivity(intent)


        }

        binding.viewPager.setOnClickListener {
            var myIntent = Intent(this, ImageClickActivity::class.java)
            startActivity(myIntent)

        }

    }


}