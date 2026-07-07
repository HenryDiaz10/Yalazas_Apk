package com.example.wireframe_1

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private var suppressNavSelection = false

    private val fragmentLifecycleCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
            if (f == supportFragmentManager.findFragmentById(R.id.fragmentContainer)) {
                syncBottomNavState()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val main = findViewById<View>(R.id.main)
        bottomNav = findViewById(R.id.bottomNav)
        ViewCompat.setOnApplyWindowInsetsListener(main) { v: View, insets: WindowInsetsCompat ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(left = bars.left, right = bars.right, top = bars.top)
            bottomNav.updatePadding(bottom = bars.bottom)
            insets
        }

        supportFragmentManager.registerFragmentLifecycleCallbacks(
            fragmentLifecycleCallbacks,
            false,
        )

        bottomNav.setOnItemSelectedListener { item: MenuItem ->
            if (suppressNavSelection) return@setOnItemSelectedListener true
            when (item.itemId) {
                R.id.nav_home -> showRootFragment(HomeFragment())
                R.id.nav_menu -> showRootFragment(MenuFragment())
                R.id.nav_community -> showRootFragment(CommunityFragment())
                R.id.nav_contact -> showRootFragment(ContactFragment())
                else -> return@setOnItemSelectedListener false
            }
            true
        }

        if (savedInstanceState == null) {
            showRootFragment(HomeFragment())
        }
    }

    override fun onDestroy() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentLifecycleCallbacks)
        super.onDestroy()
    }

    private fun showRootFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        supportFragmentManager.commitNow {
            replace(R.id.fragmentContainer, fragment)
        }
        syncBottomNavState()
    }

    private fun syncBottomNavState() {
        val current = supportFragmentManager.findFragmentById(R.id.fragmentContainer) ?: return

        val navItemId = when (current) {
            is MenuFragment, is ComboConfiguratorFragment -> R.id.nav_menu
            is CommunityFragment -> R.id.nav_community
            is ContactFragment -> R.id.nav_contact
            else -> R.id.nav_home
        }

        val colorRes = when (current) {
            is ComboConfiguratorFragment -> R.color.wf_gold_nav
            is MenuFragment -> R.color.wf_lime_nav
            is CommunityFragment -> R.color.wf_orange_nav
            is ContactFragment -> R.color.yalaza_pink
            is HomeFragment -> R.color.wf_magenta_nav
            else -> R.color.wf_magenta_nav
        }

        if (bottomNav.selectedItemId != navItemId) {
            suppressNavSelection = true
            bottomNav.selectedItemId = navItemId
            suppressNavSelection = false
        }
        bottomNav.setBackgroundColor(ContextCompat.getColor(this, colorRes))
    }

    fun openWhatsAppFor(message: String) {
        val phone = getString(R.string.whatsapp_phone_e164)
        val url = "https://wa.me/$phone?text=${Uri.encode(message)}"
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        try {
            startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            // Ignore when no app can handle this URL.
        }
    }

    fun navigateToMenu() {
        suppressNavSelection = true
        bottomNav.selectedItemId = R.id.nav_menu
        suppressNavSelection = false
        showRootFragment(MenuFragment())
    }

    fun openInstagram() {
        try {
            startActivity(
                Intent(Intent.ACTION_VIEW, "https://www.instagram.com/yalazas_pe/".toUri()),
            )
        } catch (_: ActivityNotFoundException) {
            // Ignore when no app can handle this URL.
        }
    }

    fun openMap() {
        val lat = -13.061851
        val lng = -76.352075
        val label = Uri.encode("Yalaza - Av Ramos 309")
        val uri = "geo:$lat,$lng?q=$lat,$lng($label)".toUri()
        val intent = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            // Fallback to browser if no map app
            val webUri = "https://www.google.com/maps/search/?api=1&query=$lat,$lng".toUri()
            startActivity(Intent(Intent.ACTION_VIEW, webUri))
        }
    }
}
