package com.example.ribs_demo_android.ribs.root.home.catalogue.details

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.ribs_demo_android.R
import com.example.ribs_demo_android.models.CatalogueDetail
import io.reactivex.Observable
import io.reactivex.Observer

/**
 * Top level view for {@link DetailsBuilder.DetailsScope}.
 */
class DetailsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle), DetailsInteractor.DetailsPresenter {

    override fun onBack(): Observable<Boolean> {
        return object : Observable<Boolean>() {
            override fun subscribeActual(observer: Observer<in Boolean>?) {
                findViewById<ImageView>(R.id.back).setOnClickListener {
                    observer?.onNext(true)
                }
            }
        }
    }

    override fun setData(data: CatalogueDetail) {
        findViewById<TextView>(R.id.name).text = data.name
        findViewById<TextView>(R.id.rarity).text = data.rarity
        findViewById<TextView>(R.id.hp).text = data.hp
        findViewById<TextView>(R.id.type).text = data.type

        Glide.with(context)
            .load(data.imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(findViewById<ImageView>(R.id.icon_detail))
    }

    override fun updateProgressbarState(isVisible: Boolean) {
        findViewById<ProgressBar>(R.id.progressbar_details).isVisible = isVisible
        findViewById<ProgressBar>(R.id.name_label).isVisible = !isVisible
        findViewById<ProgressBar>(R.id.hp_label).isVisible = !isVisible
        findViewById<ProgressBar>(R.id.rarity_label).isVisible = !isVisible
        findViewById<ProgressBar>(R.id.type_label).isVisible = !isVisible
    }
}
