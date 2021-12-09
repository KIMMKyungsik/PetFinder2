package org.techtown.petfinder

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.github.chrisbanes.photoview.PhotoView
import com.google.firebase.storage.FirebaseStorage
import org.techtown.GlideApp
import org.techtown.petfinder.databinding.ActivityImageClickBinding
import java.util.*

class ImageClickActivity : AppCompatActivity() {

    companion object {
        private val ARGUMENT_IMAGES = "ARGUMENT_IMAGES"
        private val ARGUMENT_CURRENT_POSITION = "ARGUMENT_CURRENT_POSITION"

        fun startActivity(activity: Context, images: ArrayList<String>, currentPosition: Int) {
            val intent = Intent(activity, ImageClickActivity::class.java)
            intent.putExtra(ARGUMENT_IMAGES, images)
            intent.putExtra(ARGUMENT_CURRENT_POSITION, currentPosition)
            activity.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityImageClickBinding
    private var images: ArrayList<String>? = null
    private var position = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent != null) {
            images = intent.getStringArrayListExtra(ARGUMENT_IMAGES)
        }

        if (images == null && savedInstanceState != null) {
            images = savedInstanceState.getStringArrayList(ARGUMENT_IMAGES)
        }

        if (images == null) {
            finish()
            return
        }

        if (intent != null) {
            position = intent.getIntExtra(ARGUMENT_CURRENT_POSITION, 0)
        }

        if (savedInstanceState != null) {
            position = savedInstanceState.getInt(ARGUMENT_CURRENT_POSITION, 0)
        }

        binding = ActivityImageClickBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        val adapter = PhotoViewPagerAdapter(images!!)
        binding.viewPager.setAdapter(adapter)
        binding.dotsIndicator.setViewPager2(binding.viewPager)
        binding.viewPager.setCurrentItem(position, false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putStringArrayList(ARGUMENT_IMAGES, images)
        outState.putInt(ARGUMENT_CURRENT_POSITION, binding.viewPager.getCurrentItem())
        super.onSaveInstanceState(outState)
    }


    private class PhotoViewPagerAdapter(val dataSet: ArrayList<String>) : RecyclerView.Adapter<PhotoViewPagerAdapter.PhotoItemViewHolder>() {

        private val storage: FirebaseStorage = FirebaseStorage.getInstance()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoItemViewHolder {
            val photoView = PhotoView(parent.context)
            photoView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            return PhotoItemViewHolder(photoView)
        }

        override fun onBindViewHolder(holder: PhotoItemViewHolder, position: Int) {
            val imageView = holder.itemView as ImageView
            val data = dataSet[position]
            if (data.startsWith("gs://")) {
                val reference = storage.getReferenceFromUrl(data)
                GlideApp.with(imageView)
                    .load(reference)
                    .into(imageView)
            } else {
                val uri = Uri.parse(data)
                GlideApp.with(imageView)
                    .load(uri)
                    .into(imageView)
            }
        }

        override fun getItemCount(): Int {
            return dataSet.size
        }

        private class PhotoItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }
}








