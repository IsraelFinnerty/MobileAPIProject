package com.wit.venues.models

interface VenueStore {
    fun findAll(): List<VenueModel>
    fun findById(id:Long) : VenueModel?
    fun create(venue: VenueModel)
    fun update(venue: VenueModel)
    fun delete(venue: VenueModel)
    fun clear()
    fun seed()
}