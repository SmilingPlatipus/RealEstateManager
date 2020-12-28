package com.openclassrooms.realestatemanager.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.activities.DetailEstateActivity
import com.openclassrooms.realestatemanager.activities.MainActivity.Companion.ID_OF_SELECTED_ESTATE
import com.openclassrooms.realestatemanager.model.Estate
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.roundToInt

class EstateListAdapter(context: Context, list: MutableList<Estate>? = null) : RecyclerView.Adapter<EstateListAdapter.EstateViewHolder>() {
    private val inflater : LayoutInflater = LayoutInflater.from(context)
    private var estateList : MutableList<Estate>? = list

    inner class EstateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
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
        val spaceSymbol = DecimalFormatSymbols(Locale.FRENCH)
        spaceSymbol.setDecimalSeparator(' ')
        val formatString = "#,###,###,###,### $"
        val df = DecimalFormat(formatString, spaceSymbol)
        var estatePhotoList = estateList?.get(position)?.photosUriWithDescriptions
        // In the RecyclerView, just showing the first photo in the thumbnail
        var uriPhoto = Uri.parse(estatePhotoList?.get(0)?.uri)
        Glide.with(holder.image.context)
                .load(uriPhoto)
                .into(holder.image)

        holder.typeOfEstate.text = estateList?.get(position)?.type
        holder.cityOfEstate.text = estateList?.get(position)?.address?.last()
        var price = estateList?.get(position)?.price?.roundToInt()
        holder.priceOfEstate.text = df.format(price)
        holder.image.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                var idOfSelectedEstate = estateList?.get(position)?.id
                var detailIntent = Intent(v?.context, DetailEstateActivity::class.java)

                detailIntent.putExtra(ID_OF_SELECTED_ESTATE,idOfSelectedEstate)
                v?.context?.let { startActivity(it,detailIntent,null) }
            }
        })
    }

    override fun getItemCount(): Int {
        if (this.estateList?.isNotEmpty()!!)
            return estateList!!.size
        else
            return 0
    }

    fun setEstateList(estates: MutableList<Estate>){
        this.estateList = estates
        notifyDataSetChanged()
    }

}