package com.wit.venues.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

data class VenueModelAPI ( var category: String ="") : Parcelable
                /*    pois: [{
      var name: String="",
     location: String,
     geo: {
           var lat: Number,
            long: Number
     },
    website: String,
    imageMain: String,
    image1: String,
    image2: String,
    image3: String,
    description: String,
    reviews: [{
        reviwer: String,
        review: String,
        dateOfVisit: String,
        rating: String
        */


@Parcelize
data class VenueModel(   var id: Long = 0,
                            var userId : String = "",
                            var fbId : String = "",
                            var name: String = "",
                            var description: String = "",
                            var image1: String = "",
                            var image2: String = "",
                            var image3: String = "",
                            var image4: String = "",
                            var visited: Boolean = false,
                            var fav: Boolean = false,
                            var rating: Float = 0f,
                            var notes: String = "",
                            var dateVisitedYear: Int = 2020,
                            var dateVisitedMonth: Int = 9,
                            var dateVisitedDay: Int = 26,
                            var lat : Double = 51.88,
                            var lng: Double = -8.49,
                            var zoom: Float = 8f) : Parcelable





@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable

@Parcelize
data class User    (var name: String = "",
                    var id: Long = 0,
                    var year: Int = 0,
                    var email: String = "",
                    var password: String = "",
                    var venues: MutableList<VenueModel> = mutableListOf() ): Parcelable