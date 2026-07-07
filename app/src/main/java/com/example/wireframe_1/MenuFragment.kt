package com.example.wireframe_1

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class MenuFragment : Fragment(R.layout.fragment_menu) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activity = activity as? MainActivity ?: return

        view.findViewById<FloatingActionButton>(R.id.fabFilter).setOnClickListener { v ->
            Snackbar.make(v, R.string.menu_filter_toast, Snackbar.LENGTH_SHORT).show()
        }
        view.findViewById<View>(R.id.btnOpenCombo).setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                replace(R.id.fragmentContainer, ComboConfiguratorFragment())
                addToBackStack("combo")
            }
        }

        val featuredList = view.findViewById<LinearLayout>(R.id.menuFeaturedList)
        FeaturedCardsHelper.bindAll(layoutInflater, featuredList, activity)

        view.findViewById<TextInputEditText>(R.id.inputSearchMenu).addTextChangedListener { text ->
            FeaturedCardsHelper.bindAll(layoutInflater, featuredList, activity, text?.toString() ?: "")
        }
    }
}
