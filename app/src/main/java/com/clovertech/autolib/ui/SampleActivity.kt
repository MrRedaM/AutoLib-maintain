package com.clovertech.autolib.ui


import android.app.SearchManager
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clovertech.autolib.R
import com.clovertech.autolib.ui.menu.DrawerAdapter
import com.clovertech.autolib.ui.menu.DrawerItem
import com.clovertech.autolib.ui.menu.SimpleItem
import com.clovertech.autolib.ui.menu.SpaceItem
import com.clovertech.autolib.ui.panne.PanneFragment
import com.clovertech.autolib.ui.settings.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yarolegovich.slidingrootnav.SlidingRootNav
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class SampleActivity : AppCompatActivity(), DrawerAdapter.OnItemSelectedListener {
    private lateinit var screenTitles: Array<String>
    private lateinit var screenIcons: Array<Drawable?>
    private lateinit var menuItems: Array<Int>
    private lateinit var adapter:DrawerAdapter


    private var slidingRootNav: SlidingRootNav? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBottomBar()

        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        this.setSupportActionBar(toolbar);
        toolbar.setTitle("")
        toolbar.setBackgroundColor( getResources().getColor(R.color.dirtyWhite))

        screenIcons = loadScreenIcons()
        screenTitles = loadScreenTitles()
        menuItems =arrayOf(R.id.navigation_home, R.id.navigation_notifications, R.id.navigation_calendar,-1,-1, R.id.navigation_userProfil)


        adapter = DrawerAdapter(
            Arrays.asList(
                createItemFor(POS_ACCUEIL).setChecked(true),
                createItemFor(POS_NOTIF),
                createItemFor(POS_CALENDAR),
                createItemFor(POS_PANNE),
                createItemFor(POS_SETTINGS),
                createItemFor(POS_PROFIL),
                SpaceItem(48),
                createItemFor(POS_LOGOUT)
            ) as List<DrawerItem<DrawerAdapter.ViewHolder>>?
        )

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        /*listener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            val fragment: Fragment
            when (item.itemId) {
                R.id.navigation_userProfil -> {
                    supportActionBar?.title = "Profile"
                    fragment = UserProfilFragment()
                    showFragment(fragment)
                    adapter.setSelected(POS_PROFIL)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_home -> {
                    supportActionBar?.title = "Accueil"
                    fragment = HomeFragment()
                    showFragment(fragment)
                    adapter.setSelected(POS_ACCUEIL)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    supportActionBar?.title = "Notifications"
                    fragment = NotificationsFragment()
                    showFragment(fragment)
                    adapter.setSelected(POS_NOTIF)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_calendar -> {
                    supportActionBar?.title = "Calendrier"
                    fragment = DashboardFragment()
                    showFragment(fragment)
                    adapter.setSelected(POS_CALENDAR)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }*/

        navView.setSelectedItemId(R.id.navigation_home);


        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home,
            R.id.navigation_notifications,
            R.id.navigation_calendar,
            R.id.navigation_userProfil,
        ))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        slidingRootNav = SlidingRootNavBuilder(this)
            .withToolbarMenuToggle(toolbar)
            .withMenuOpened(false)
            .withContentClickableWhenMenuOpened(false)
            .withSavedState(savedInstanceState)
            .withMenuLayout(R.layout.menu_left_drawer)
            .inject()


        adapter.setListener(this)
        val list = findViewById<RecyclerView>(R.id.list)
        list.isNestedScrollingEnabled = false
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter
        adapter.setSelected(POS_ACCUEIL)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_nav_menu, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }
        return true
    }


    override fun onItemSelected(position: Int) {

        if (position == POS_LOGOUT) {
            finish()
        }
        else if(position == POS_PANNE){
            slidingRootNav!!.closeMenu()
            val selectedScreen: Fragment = PanneFragment()
            showFragment(selectedScreen)

        }
        else if(position == POS_SETTINGS){
            slidingRootNav!!.closeMenu()
            val selectedScreen: Fragment = SettingsFragment()
            showFragment(selectedScreen)

        }
        else{
            slidingRootNav!!.closeMenu()
            val bottomNavigationView: BottomNavigationView = findViewById(R.id.nav_view)
            bottomNavigationView.setSelectedItemId(menuItems[position]);
        }

    }


    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }

    private fun createItemFor(position: Int): DrawerItem<SimpleItem.ViewHolder> {

        return SimpleItem(screenIcons[position], screenTitles[position])
            .withIconTint(color(R.color.textColorSecondary))
            .withTextTint(color(R.color.textColorSecondary))
            .withSelectedIconTint(color(R.color.textColorSecondary))
            .withSelectedTextTint(color(R.color.textColorSecondary))
            //.withSelectedIconTint(color(R.color.colorAccent))
            //.withSelectedTextTint(color(R.color.colorAccent))
    }

    private fun loadScreenTitles(): Array<String> {

        return resources.getStringArray(R.array.ld_activityScreenTitles)
    }


    private fun loadScreenIcons(): Array<Drawable?> {
        val ta = resources.obtainTypedArray(R.array.ld_activityScreenIcons)
        val icons = arrayOfNulls<Drawable>(ta.length())
        for (i in 0 until ta.length()) {
            val id = ta.getResourceId(i, 0)
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id)
            }
        }
        ta.recycle()
        return icons
    }

    @ColorInt
    private fun color(@ColorRes res: Int): Int {
        return ContextCompat.getColor(this, res)
    }

    companion object {
        private const val POS_ACCUEIL = 0
        private const val POS_NOTIF= 1
        private const val POS_CALENDAR = 2
        private const val POS_PANNE = 3
        private const val POS_SETTINGS = 4
        private const val POS_PROFIL = 5
        private const val POS_LOGOUT = 7
    }
    private fun initBottomBar() {
        nav_view.enableItemShiftingMode(false)
        nav_view.enableAnimation(true)
    }
}
