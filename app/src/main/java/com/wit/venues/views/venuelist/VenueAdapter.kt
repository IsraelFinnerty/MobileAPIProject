package com.wit.venues.views.venuelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.card_venue.view.*
import com.wit.venues.R
import com.wit.venues.models.VenueModel


interface VenueListener {

    fun onVenueClick(Venue: VenueModel)
}

class VenueAdapter constructor(
    private var venues: List<VenueModel>,
    private val listener: VenueListener

) : RecyclerView.Adapter<VenueAdapter.MainHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.card_venue,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val venue = venues[holder.adapterPosition]
        holder.bind(venue, listener)

    }

    override fun getItemCount(): Int = venues.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(venue: VenueModel, listener: VenueListener) {
            var visited = if (venue.visited) "Yes" else "Not Yet"
            var formattedLat = String.format("%.5f", venue.lat)
            var formattedLng = String.format("%.5f", venue.lng)
            itemView.venueTitle.text = venue.name
            itemView.tag = venue.id
            itemView.description.text = venue.description
            itemView.location.text= "Location: ${formattedLat}, ${formattedLng}"
            itemView.visited?.text= "Visited: $visited"
            if (venue.image1.length > 20) {
                 Glide.with(itemView.context).load(venue.image1).into(itemView.venueImage);
            }
            else  itemView.venueImage.setImageResource(itemView.context.getResources().getIdentifier(venue.image1, "drawable", itemView.context.packageName))
            itemView.setOnClickListener { listener.onVenueClick(venue) }
        }

    }




}