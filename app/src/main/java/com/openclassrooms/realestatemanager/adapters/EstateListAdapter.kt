package com.openclassrooms.realestatemanager.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Estate

class EstateListAdapter (context : Context, list : MutableList<Estate>) : RecyclerView.Adapter<EstateListAdapter.EstateViewHolder>() {
    private val inflater : LayoutInflater = LayoutInflater.from(context)
    private var estateList : MutableList<Estate> = list

    inner class EstateViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val image : ImageView = itemView.findViewById(R.id.cardview_image)
        val typeOfEstate : TextView = itemView.findViewById(R.id.cardview_type_of_estate)
        val cityOfEstate : TextView = itemView.findViewById(R.id.cardview_city_of_estate)
        val priceOfEstate : TextView = itemView.findViewById(R.id.cardview_price_of_estate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstateListAdapter.EstateViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return EstateViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EstateListAdapter.EstateViewHolder, position: Int) {
        var estatePhotoList = estateList[position].photosUriWithDescriptions
        // In the RecyclerView, just showing the first photo in the thumbnail
        var uriPhoto = Uri.parse(estatePhotoList?.get(0)?.uri)
        Glide.with(holder.image.context)
                .load(uriPhoto)
                .into(holder.image)

        holder.typeOfEstate.setText(estateList[position].type)
        holder.cityOfEstate.setText(estateList[position].address?.last())
        holder.priceOfEstate.setText(estateList[position].price.toString())
    }

    override fun getItemCount(): Int {
        return estateList.size
    }

    fun setEstateList(estates : MutableList<Estate>){
        this.estateList = estates
        notifyDataSetChanged()
    }

}