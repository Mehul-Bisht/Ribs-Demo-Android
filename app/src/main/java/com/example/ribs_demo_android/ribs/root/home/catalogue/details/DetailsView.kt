package com.example.ribs_demo_android.ribs.root.home.catalogue.details

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.ribs_demo_android.R
import com.example.ribs_demo_android.models.CatalogueDetail
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.core.Observable

/**
 * Top level view for {@link DetailsBuilder.DetailsScope}.
 */
class DetailsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle), DetailsInteractor.DetailsPresenter {
    private lateinit var tvName: TextView
    private lateinit var tvRarity: TextView
    private lateinit var tvHp: TextView
    private lateinit var tvType: TextView
    private lateinit var progressDetails: ProgressBar
    private lateinit var imgBack: ImageView

    fun initViewIds() {
        tvName = findViewById(R.id.name)
        tvRarity = findViewById(R.id.rarity)
        tvHp = findViewById(R.id.hp)
        tvType = findViewById(R.id.type)
        progressDetails = findViewById<ProgressBar>(R.id.progressbar_details)
        imgBack = findViewById(R.id.back)
    }

    override fun onBack(): Observable<Boolean> {
       return imgBack.clicks().map { true }
    }

    override fun setData(data: CatalogueDetail) {
        tvName.text = data.name
        tvRarity.text = data.rarity
        tvHp.text = data.hp
        tvType.text = data.type

        Glide.with(context)
            .load(data.imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(findViewById<ImageView>(R.id.icon_detail))
    }

    override fun updateProgressbarState(isVisible: Boolean) {
        progressDetails.isVisible = isVisible
    }
}
