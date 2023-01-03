package com.solacestudios.ribs_demo_android.ribs.catalogue

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.solacestudios.ribs_demo_android.R
import com.solacestudios.ribs_demo_android.ribs.catalogue.adapter.CatalogueAdapter.CatalogueAdapter
import com.solacestudios.ribs_demo_android.models.Catalogue
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

/**
 * Top level view for {@link CatalogueScope}.
 */
class CatalogueView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle), CatalogueInteractor.Presenter {
    private lateinit var catalogueRecycler: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvFilters: TextView

    private lateinit var adapter: CatalogueAdapter
    private val itemTaps = PublishSubject.create<String>()
    private val filterTaps = PublishSubject.create<Boolean>()

    fun initViewIds() {
        this.catalogueRecycler = findViewById(R.id.RecyclerView)
        this.progressBar = findViewById(R.id.progressbar_catalogue)
        this.tvFilters = findViewById(R.id.filters)
        adapter = CatalogueAdapter { name ->
            Log.e(this.javaClass.name, "itemClicked: $name")
            itemTaps.onNext(name)
        }
        tvFilters.setOnClickListener { filterTaps.onNext(true) }
        catalogueRecycler.adapter = adapter
    }

    override fun setupUI(items: List<Catalogue>) {
        adapter.setItems(items)
    }

    override fun categoryTaps(): Observable<String> {
        return itemTaps
    }

    override fun updateProgressbarState(isVisible: Boolean) {
        progressBar.isVisible = isVisible
    }

    override fun onCategoryToggle(): Observable<Boolean> {
        return filterTaps
    }
}
