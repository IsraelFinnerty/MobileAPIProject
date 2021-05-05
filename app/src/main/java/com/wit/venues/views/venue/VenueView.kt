package com.wit.venues.views.venue

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RatingBar
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.activity_venue.*
import com.wit.venues.R
import com.wit.venues.views.login.LoginView
import com.wit.venues.models.VenueModel
import com.wit.venues.views.BaseView
import kotlinx.android.synthetic.main.activity_venue.description
import kotlinx.android.synthetic.main.activity_venue.venueImage
import org.jetbrains.anko.*
import retrofit2.Callback

class VenueView :  BaseView(), AnkoLogger{

    var venue = VenueModel()
    lateinit var presenter: VenuePresenter
    lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venue)
        info("Venue Activity started..")

        drawerLayout = findViewById(R.id.drawer_layout_venue)

        init(toolbarAdd)

        toolbarAdd.setNavigationIcon(R.drawable.ic_baseline_menu_24)

        presenter = initPresenter( VenuePresenter(this)) as VenuePresenter
        info("Venue Activity started..")

        button_visited.setOnCheckedChangeListener { _, isChecked ->
            presenter.doCheckVisited(isChecked)
                    }

        button_fav.setOnCheckedChangeListener { _, isChecked ->
            presenter.doCheckFav(isChecked)
        }

        ratingBar.setOnRatingBarChangeListener(object : RatingBar.OnRatingBarChangeListener {
            override fun onRatingChanged(ratignBar: RatingBar?, rating: Float, fromUser: Boolean) {
                presenter.doCheckRating(rating)
            } })

        btnAdd.setOnClickListener() {
            presenter.doAddOrSave(venueName.text.toString(), description.text.toString(), notes.text.toString(), date_visited.year, date_visited.month, date_visited.dayOfMonth   )
                    }

        chooseImage.setOnClickListener { presenter.doSelectImage1() }
        chooseImage2.setOnClickListener { presenter.doSelectImage2() }
        chooseImage3.setOnClickListener { presenter.doSelectImage3() }
        chooseImage4.setOnClickListener { presenter.doSelectImage4() }

        val check = drawerLayout.isDrawerOpen(GravityCompat.START)
        toolbarAdd.setNavigationOnClickListener {
            if (!check) drawerLayout.openDrawer(GravityCompat.START)
            else  drawerLayout.closeDrawer(GravityCompat.START)
        }

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync {
            map = it
            presenter.doConfigureMap(map)
            it.setOnMapClickListener { presenter.doSetLocation() }
        }


        nav_view_venue.setNavigationItemSelectedListener { menuItem -> navDrawer(menuItem) }

        bottom_navigation1.setOnNavigationItemSelectedListener { item ->  bottomNavigation(item)  }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (intent.hasExtra("venue_edit")) {
            menuInflater.inflate(R.menu.menu_venue, menu)
        }
        else menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.item_cancel -> finish()
            R.id.item_delete -> {
                presenter.doDelete()
                          }
            R.id.item_logout -> startActivity<LoginView>()
            R.id.item_save ->  presenter.doAddOrSave(venueName.text.toString(), description.text.toString(), notes.text.toString(), date_visited.year, date_visited.month, date_visited.dayOfMonth   )

        }
        return super.onOptionsItemSelected(item)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            presenter.doActivityResult(requestCode, resultCode, data)
        }
    }

    override fun showVenue(venue: VenueModel){
        if (venueName.text.isEmpty()) venueName.setText(venue.name)
        if (description.text.isEmpty()) description.setText(venue.description)
        if (notes.text.isEmpty())notes.setText(venue.notes)
        button_fav.setChecked(venue.fav)
        button_visited.setChecked(venue.visited)
        date_visited.updateDate(venue.dateVisitedYear, venue.dateVisitedMonth, venue.dateVisitedDay)
        btnAdd.setText(R.string.button_editVenue)
        ratingBar.setRating(venue.rating)

        if (venue.visited) {
            date_visited.setVisibility(View.VISIBLE)
            date_title.setVisibility(View.VISIBLE)
        }

        if (venue.image1 != "") { chooseImage.setText(R.string.button_changeImage)
            if (venue.image1.length > 20) { Glide.with(this).load(venue.image1).into(venueImage)
            } else venueImage.setImageResource(this.getResources().getIdentifier(venue.image1, "drawable", this.packageName))
        }

        if (venue.image2 != "") { chooseImage2.setText(R.string.button_changeImage2)
            if (venue.image2.length > 20) { Glide.with(this).load(venue.image2).into(venueImage2)
            } else venueImage2.setImageResource(this.getResources().getIdentifier(venue.image2, "drawable", this.packageName))
        }

        if (venue.image3 != "") { chooseImage3.setText(R.string.button_changeImage3)
            if (venue.image3.length > 20) {Glide.with(this).load(venue.image3).into(venueImage3)
            } else venueImage4.setImageResource(this.getResources().getIdentifier(venue.image3, "drawable", this.packageName))
        }

        if (venue.image4 != "") { chooseImage4.setText(R.string.button_changeImage4)
            if (venue.image4.length > 20) { Glide.with(this).load(venue.image4).into(venueImage4)
            } else venueImage4.setImageResource(this.getResources().getIdentifier(venue.image4, "drawable", this.packageName))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        presenter.doResartLocationUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}


