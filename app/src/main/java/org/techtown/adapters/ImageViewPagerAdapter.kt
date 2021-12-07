package org.techtown.adapters

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import org.techtown.GlideApp

class ImageViewPagerAdapter : RecyclerView.Adapter<ImageViewPagerAdapter.ImageItemViewHolder> {

    private val storage: FirebaseStorage
    var dataSet = ArrayList<String>()
    private var listener: ((Int, ArrayList<String>) -> Unit)? = null


    constructor() {
        storage = FirebaseStorage.getInstance()
    }

    constructor(dataSet: List<String>) {
        storage = FirebaseStorage.getInstance()
        this.dataSet = ArrayList(dataSet)
    }

    fun setOnClickListener(listener: ((Int, ArrayList<String>) -> Unit)?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageItemViewHolder {
        val imageView = ImageView(parent.context)
        imageView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return ImageItemViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: ImageItemViewHolder, position: Int) {
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

        imageView.setOnClickListener { v: View? ->
            if (listener != null) {
                listener!!.invoke(position, dataSet)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }


    class ImageItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}