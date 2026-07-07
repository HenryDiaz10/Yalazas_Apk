package com.example.wireframe_1

import android.graphics.Matrix
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

object FeaturedCardsHelper {

    data class FeaturedItem(
        @StringRes val titleRes: Int,
        @StringRes val taglineRes: Int,
        @StringRes val descriptionRes: Int,
        @StringRes val priceRes: Int,
        @StringRes val whatsappRes: Int,
        @ColorRes val accentColorRes: Int,
        @DrawableRes val imageRes: Int,
    )

    val items: List<FeaturedItem> = listOf(
        FeaturedItem(
            titleRes = R.string.featured_acvichadas_title,
            taglineRes = R.string.featured_acvichadas_tagline,
            descriptionRes = R.string.featured_acvichadas_desc,
            priceRes = R.string.featured_acvichadas_price,
            whatsappRes = R.string.whatsapp_featured_acvichadas,
            accentColorRes = R.color.yalaza_lime,
            imageRes = R.drawable.img_featured_acvichadas,
        ),
        FeaturedItem(
            titleRes = R.string.featured_bbq_title,
            taglineRes = R.string.featured_bbq_tagline,
            descriptionRes = R.string.featured_bbq_desc,
            priceRes = R.string.featured_bbq_price,
            whatsappRes = R.string.whatsapp_featured_bbq,
            accentColorRes = R.color.yalaza_yellow,
            imageRes = R.drawable.img_featured_bbq,
        ),
        FeaturedItem(
            titleRes = R.string.featured_crispy_title,
            taglineRes = R.string.featured_crispy_tagline,
            descriptionRes = R.string.featured_crispy_desc,
            priceRes = R.string.featured_crispy_price,
            whatsappRes = R.string.whatsapp_featured_crispy,
            accentColorRes = R.color.yalaza_pink,
            imageRes = R.drawable.img_featured_crispy,
        ),
    )

    fun bindAll(
        inflater: LayoutInflater,
        container: LinearLayout,
        activity: MainActivity,
        query: String = "",
    ) {
        container.removeAllViews()
        val context = container.context

        items.filter { item ->
            query.isEmpty() ||
                context.getString(item.titleRes).contains(query, ignoreCase = true) ||
                context.getString(item.taglineRes).contains(query, ignoreCase = true) ||
                context.getString(item.descriptionRes).contains(query, ignoreCase = true)
        }.forEach { item ->
            val cardRoot = inflater.inflate(R.layout.item_featured_card, container, false)
            val card = cardRoot as MaterialCardView
            val accent = ContextCompat.getColor(context, item.accentColorRes)

            card.strokeColor = accent

            val image = cardRoot.findViewById<ImageView>(R.id.featuredImage)
            image.contentDescription = context.getString(item.titleRes)
            image.setImageResource(item.imageRes)
            image.scaleType = ImageView.ScaleType.CENTER_CROP
            image.imageMatrix = Matrix()

            cardRoot.findViewById<TextView>(R.id.featuredTitle).apply {
                text = context.getString(item.titleRes)
                setTextColor(accent)
            }
            cardRoot.findViewById<TextView>(R.id.featuredTagline).apply {
                text = context.getString(item.taglineRes)
                setTextColor(accent)
            }
            cardRoot.findViewById<TextView>(R.id.featuredDescription).text =
                context.getString(item.descriptionRes)
            cardRoot.findViewById<TextView>(R.id.featuredPrice).apply {
                text = context.getString(item.priceRes)
                setTextColor(accent)
            }
            cardRoot.findViewById<MaterialButton>(R.id.btnFeaturedWhatsApp).setOnClickListener {
                activity.openWhatsAppFor(context.getString(item.whatsappRes))
            }

            container.addView(cardRoot)
        }
    }
}
