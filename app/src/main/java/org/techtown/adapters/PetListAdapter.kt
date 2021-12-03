package org.techtown.adapters

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import org.techtown.GlideApp
import org.techtown.datas.Petdata
import org.techtown.petfinder.R
import org.techtown.petfinder.databinding.ItemPetListBinding

import kotlin.math.min

class PetListAdapter(context: Context) : ListAdapter<Petdata, PetListAdapter.PetListItemViewHolder>(DIFF_CALLBACK) {

    private val imageWidth: Int
    private val storage: FirebaseStorage


    init {
        val displayWidth = min(context.resources.displayMetrics.widthPixels, context.resources.displayMetrics.heightPixels)
        imageWidth = (displayWidth - (context.resources.getDimensionPixelSize(R.dimen.pet_list_item_spacing) * 3)) / 2
        storage = FirebaseStorage.getInstance()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetListItemViewHolder {
        val binding = ItemPetListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        (binding.imageViewContainer.layoutParams as? ConstraintLayout.LayoutParams)?.let {
            it.width = imageWidth
            binding.imageViewContainer.layoutParams = it
        }

        return PetListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PetListItemViewHolder, position: Int) {
        val item = currentList[position]

        with(holder.binding) {
            GlideApp.with(imageView)
                .load(storage.getReferenceFromUrl(item.pictures.firstOrNull() ?: ""))
                .into(imageView)

            titleTextView.text = item.title
            raceTextView.text = "품종: ${item.race}"
            ageTextView.text = "나이: ${item.age}"
            sexTextView.text = "성별: ${item.sex}"
            dateTextView.text = "실종일: ${
                DateUtils.formatDateTime(
                    root.context,
                    item.date.time,
                    DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_NUMERIC_DATE
                )
            }"
            locationTextView.text = "실종위치: ${item.location}"
        }
    }


    companion object {
        val DIFF_CALLBACK by lazy {
            object : DiffUtil.ItemCallback<Petdata>() {
                override fun areItemsTheSame(oldItem: Petdata, newItem: Petdata): Boolean {
                    return oldItem.documentId == newItem.documentId
                }

                override fun areContentsTheSame(oldItem: Petdata, newItem: Petdata): Boolean {
                    return (oldItem.timestamp == null && newItem.timestamp == null) ||
                            (oldItem.timestamp != null && newItem.timestamp != null && oldItem.timestamp == newItem.timestamp)
                }
            }
        }
    }


    class PetListItemViewHolder(val binding: ItemPetListBinding) : RecyclerView.ViewHolder(binding.root)
}