package com.wit.venues.views.venuelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.card_venue.view.*
import com.wit.venues.R
import com.wit.venues.models.VenueModel
import com.wit.venues.models.VenueModelAPI


interface VenueListenerAPI {

    fun onVenueClick(Venue: VenueModelAPI)
}

class VenueAdapterAPI constructor(
    private var venues: List<VenueModel>,
    private val listener: VenueListener

) : RecyclerView.Adapter<VenueAdapter.MainHolder>() {


    override fun onBindViewHolder(holder: MainHolderAPI, position: Int) {
        val venue = venues[holder.adapterPosition]
        holder.bind(venue, listener)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolderAPI{
        return MainHolderAPI(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.card_venue,
                parent,
                false
            )
        )
    }



    override fun getItemCount(): Int = venues.size

    class MainHolderAPI constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(venue: VenueModelAPI, listener: VenueListenerAPI) {

        }

    }




}