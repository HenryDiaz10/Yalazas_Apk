package com.example.wireframe_1

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment

class CommunityFragment : Fragment(R.layout.fragment_community) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val grid = view.findViewById<GridLayout>(R.id.gridCommunity)
        val handles = resources.getStringArray(R.array.community_handles)
        val inflater = layoutInflater

        grid.removeAllViews()

        for (i in 0 until minOf(12, handles.size)) {

            val cell = inflater.inflate(R.layout.item_community_repost, grid, false)

            val image = cell.findViewById<AppCompatImageView>(R.id.repostImage)
            val user = cell.findViewById<TextView>(R.id.repostUser)

            image.setImageResource(
                if (i % 2 == 0) R.drawable.img_alitas_combo else R.drawable.yalaza_logo
            )

            user.text = handles[i]

            val row = i / 3
            val col = i % 3

            val lp = GridLayout.LayoutParams(
                GridLayout.spec(row, 1, 1f),
                GridLayout.spec(col, 1, 1f)
            ).apply {
                width = 0
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                setMargins(6, 6, 6, 6)
            }

            cell.layoutParams = lp
            grid.addView(cell)
        }

        view.findViewById<View>(R.id.btnCommunityUpload).setOnClickListener {
            (activity as? MainActivity)?.openInstagram()
        }
    }
}