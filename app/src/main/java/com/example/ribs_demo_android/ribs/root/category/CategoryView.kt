package com.example.ribs_demo_android.ribs.root.category

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.forEach
import androidx.core.view.forEachIndexed
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.ribs_demo_android.R
import com.example.ribs_demo_android.models.Catalogue
import com.example.ribs_demo_android.ribs.root.category.adapter.CategoryAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import io.reactivex.Observable
import io.reactivex.Observer

/**
 * Top level view for {@link CategoryBuilder.CategoryScope}.
 */
class CategoryView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RelativeLayout(context, attrs, defStyle), CategoryInteractor.CategoryPresenter {

    private var adapter: CategoryAdapter? = null

    override fun setup(items: List<Catalogue>) {
        adapter = CategoryAdapter(items) {

        }

        findViewById<RecyclerView>(R.id.rvCategory).adapter = adapter
    }

    override fun toggle(): Observable<Boolean> {
        return object : Observable<Boolean>() {
            override fun subscribeActual(observer: Observer<in Boolean>?) {
                findViewById<TextView>(R.id.home).setOnClickListener {
                    observer?.onNext(true)
                }
            }
        }
    }

    override fun getChip(): Observable<String> {
        return object : Observable<String>() {
            override fun subscribeActual(observer: Observer<in String>?) {
                findViewById<ChipGroup>(R.id.chipgroup).setOnCheckedChangeListener { group, checkedId ->
                    group.forEachIndexed { index, view ->
                        if (view.id == checkedId) {
                            val chip = group[index] as Chip
                            val query = chip.text.toString().toLowerCase()
                            observer?.onNext(query)
                        }
                    }
                }
            }
        }
    }

    override fun updateProgressbarState(isVisible: Boolean) {
        findViewById<ProgressBar>(R.id.progressbar_category).isVisible = isVisible
    }
}
