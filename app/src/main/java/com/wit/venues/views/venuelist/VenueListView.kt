package com.wit.venues.views.venuelist


import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_venue_list.*
import com.wit.venues.R
import com.wit.venues.helpers.createLoader
import com.wit.venues.models.VenueModel
import com.wit.venues.models.VenueModelAPI
import com.wit.venues.views.BaseView


class VenueListView : BaseView(), VenueListener {

    lateinit var presenter: VenueListPresenter
    lateinit var loader : AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venue_list)

        drawerLayout = findViewById(R.id.drawer_layout)
        toolbar.title = title
        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24)

        setSupportActionBar(toolbar)

        presenter = initPresenter( VenueListPresenter(this)) as VenueListPresenter

        val check = drawerLayout.isDrawerOpen(GravityCompat.START)
        toolbar.setNavigationOnClickListener {
            if (!check) drawerLayout.openDrawer(GravityCompat.START)
            else  drawerLayout.closeDrawer(GravityCompat.START)
        }

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager


        nav_view.setNavigationItemSelectedListener { menuItem -> navDrawer(menuItem) }

        bottom_navigation.setOnNavigationItemSelectedListener { item -> bottomNavigation(item) }
      //  presenter.loader = createLoader(activity = FragmentActivity())
        presenter.getAllVenues()
       // presenter.loadVenues()
        presenter.doSwipeHandler()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.item_add -> presenter.doAddVenue()
            R.id.item_settings -> presenter.doShowSettings()
            R.id.item_logout -> basePresenter?.doLogout()
            R.id.item_fav -> presenter.doFav()
            R.id.item_map -> presenter.doMap()
            }
        return super.onOptionsItemSelected(item)
    }



    override fun onVenueClick(venue: VenueModel) {
        presenter.doEditVenue(venue)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.loadVenues()
        super.onActivityResult(requestCode, resultCode, data)
    }

   override fun showVenues (venues: List<VenueModel>) {
        recyclerView.adapter = VenueAdapter(venues, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }

   override fun showVenuesAPI (venues: List<VenueModelAPI>) {
        recyclerView.adapter = VenueAdapter(venues, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }


    override fun onResume() {
        super.onResume()
        presenter.getAllVenues()
    }




    override fun findById(id: Long): VenueModel? {
        return presenter.doFindById(id)
    }


}



