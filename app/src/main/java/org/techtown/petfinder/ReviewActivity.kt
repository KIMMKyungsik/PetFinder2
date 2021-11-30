package org.techtown.petfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import org.techtown.adapters.RecyclerReviewAdapter
import org.techtown.petfinder.databinding.ActivityReviewBinding

class ReviewActivity : BaseActivity() {
    lateinit var binding: ActivityReviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review)
        setValues()
        setupEvents()

    }

    override fun setupEvents() {

        binding.reviewRecyclerview.layoutManager = GridLayoutManager(mContext,2)
        binding.reviewRecyclerview.adapter = RecyclerReviewAdapter()

    }

    override fun setValues() {

    }

}