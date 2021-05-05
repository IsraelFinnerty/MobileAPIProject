package com.wit.venues.views

import android.content.Intent

import android.os.Parcelable
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.AnkoLogger
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.wit.venues.R
import com.wit.venues.views.map.VenueMapsView
import com.wit.venues.models.VenueModel
import com.wit.venues.models.Location
import com.wit.venues.models.VenueModelAPI
import com.wit.venues.views.venue.VenueView
import com.wit.venues.views.venuelist.VenueListView
import com.wit.venues.views.login.LoginView
import com.wit.venues.views.map.MapView
import com.wit.venues.views.settings.SettingsView
import kotlinx.android.synthetic.main.activity_venue_list.*
import javax.security.auth.callback.Callback

val IMAGE_REQUEST = 1
val LOCATION_REQUEST = 2

enum class VIEW {
    LOCATION, VENUE, MAPS, LIST, SETTINGS, FAVS, LOGIN, MAP
}

open abstract class BaseView() : AppCompatActivity(), AnkoLogger {

    var basePresenter: BasePresenter? = null
    lateinit var drawerLayout: DrawerLayout



    fun navigateTo(view: VIEW, code: Int = 0, key: String = "", value: Parcelable? = null) {
        var intent = Intent(this, VenueListView::class.java)
        when (view) {
            VIEW.LOCATION -> intent = Intent(this, MapView::class.java)
            VIEW.VENUE -> intent = Intent(this, VenueView::class.java)
            VIEW.MAPS -> intent = Intent(this, MapView::class.java)
            VIEW.LIST -> intent = Intent(this, VenueListView::class.java)
            VIEW.SETTINGS -> intent = Intent(this, SettingsView::class.java)
            VIEW.FAVS -> intent = Intent(this, VenueListView::class.java)
            VIEW.LOGIN -> intent = Intent(this, LoginView::class.java)
            VIEW.MAP -> intent = Intent(this, VenueMapsView::class.java )
        }
        if (key != "") {
            intent.putExtra(key, value)
        }
        startActivityForResult(intent, code)
    }

    fun bottomNavigation(item: MenuItem): Boolean {
               when(item.itemId) {
                R.id.list_bottom-> {
                    navigateTo(VIEW.LIST)
                    return true
                }
                R.id.fav_bottom-> {
                    navigateTo(VIEW.FAVS, 0, "Fav")
                    return true
                }
                R.id.settings_bottom-> {
                    navigateTo(VIEW.SETTINGS)
                    return true
                }
                R.id.map_bottom-> {
                    navigateTo(VIEW.MAP)
                    return true
                }
                R.id.logout_bottom-> {
                    basePresenter!!.doLogout()
                    return true
                   }
                else -> return false
            }
    }

    fun navDrawer(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_list -> {
                navigateTo(VIEW.LIST)
                return true
            }
            R.id.nav_fav-> {
                navigateTo(VIEW.FAVS, 0, "Fav")
                return true
            }
            R.id.nav_settings -> {
                navigateTo(VIEW.SETTINGS)
                drawerLayout.closeDrawer(GravityCompat.START)
                return true
            }
            R.id.nav_add -> {
                navigateTo(VIEW.VENUE)
                drawerLayout.closeDrawer(GravityCompat.START)
                return true
            }
            R.id.nav_map -> {
                navigateTo(VIEW.MAP)
                return true
            }
            R.id.nav_logout -> {
                basePresenter!!.doLogout()
                return true
            }
            else -> {
                if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawer(GravityCompat.START)
                }
                return false
            }
        }
    }

    fun initPresenter(presenter: BasePresenter): BasePresenter {
        basePresenter = presenter
        return presenter
    }

    fun init(toolbar: Toolbar) {
        toolbar.title = title
        setSupportActionBar(toolbar)
    }

    override fun onDestroy() {
        basePresenter?.onDestroy()
        super.onDestroy()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            basePresenter?.doActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        basePresenter?.doRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    open fun showVenue(venue: VenueModel) {}
    open fun showVenuesAPI(venues: List<VenueModelAPI>) {}
    open fun showVenues(venues: List<VenueModel>) {}
    open fun showProgress() {}
    open fun hideProgress() {}
    open fun findById(id: Long): VenueModel? {return VenueModel()}
    open fun showLocation(location : Location) {}
    }
