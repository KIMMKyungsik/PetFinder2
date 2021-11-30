package org.techtown.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.techtown.petfinder.R

class RecyclerReviewAdapter : RecyclerView.Adapter<CustomViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val row = LayoutInflater.from(parent.context).inflate(R.layout.review_list_item,parent,false)
        return CustomViewHolder(row)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 10

    }

}
class CustomViewHolder(view : View) : RecyclerView.ViewHolder(view){}