package com.example.ribs_demo_android.ribs.root.home.catalogue

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ribs_demo_android.R
import com.example.ribs_demo_android.ribs.root.home.catalogue.adapter.CatalogueAdapter.CatalogueAdapter
import com.example.ribs_demo_android.models.Catalogue
import io.reactivex.Observable
import io.reactivex.Observer

/**
 * Top level view for {@link CatalogueBuilder.CatalogueScope}.
 */
class CatalogueView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle), CatalogueInteractor.CataloguePresenter {

    private var adapter: CatalogueAdapter? = null
    private var listener: ((String) -> Unit)? = null

    override fun setupUI(items: List<Catalogue>): Observable<String> {
        adapter = CatalogueAdapter(items) { name ->
            listener?.invoke(name)
        }
        findViewById<RecyclerView>(R.id.rv).adapter = adapter
        return object : Observable<String>() {
            override fun subscribeActual(observer: Observer<in String>?) {
                listener = { name ->
                    observer?.onNext(name)
                }
            }
        }
    }

    override fun updateProgressbarState(isVisible: Boolean) {
        findViewById<ProgressBar>(R.id.progressbar_catalogue).isVisible = isVisible
    }

    override fun onCategoryToggle(): Observable<Boolean> {
        return object : Observable<Boolean>() {
            override fun subscribeActual(observer: Observer<in Boolean>?) {
                findViewById<TextView>(R.id.filters).setOnClickListener {
                    observer?.onNext(true)
                }
            }
        }
    }
}
