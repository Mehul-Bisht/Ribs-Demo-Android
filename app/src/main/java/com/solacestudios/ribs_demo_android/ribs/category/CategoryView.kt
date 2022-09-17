package com.solacestudios.ribs_demo_android.ribs.category

import android.content.Context
import android.util.AttributeSet
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.forEachIndexed
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.solacestudios.ribs_demo_android.R
import com.solacestudios.ribs_demo_android.models.Catalogue
import com.solacestudios.ribs_demo_android.ribs.category.adapter.CategoryAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

/**
 * Top level view for {@link CategoryBuilder.CategoryScope}.
 */
class CategoryView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RelativeLayout(context, attrs, defStyle), CategoryInteractor.Presenter {
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var tvHome: TextView
    private lateinit var chipGroup: ChipGroup
    private lateinit var progressBar: ProgressBar
    private val chipTaps = PublishSubject.create<String>()

    private val adapter: CategoryAdapter = CategoryAdapter {}

    fun initViewIds() {
        this.categoryRecyclerView = findViewById(R.id.rvCategory)
        this.categoryRecyclerView.adapter = adapter
        this.tvHome = findViewById(R.id.home)
        this.chipGroup = findViewById(R.id.chipgroup)
        this.progressBar = findViewById(R.id.progressbar_category)

        this.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            group.forEachIndexed { index, view ->
                if (view.id == checkedId) {
                    val chip = group[index] as Chip
                    val query = chip.text.toString().toLowerCase()
                    chipTaps.onNext(query)
                }
            }
        }
    }

    override fun setup(items: List<Catalogue>) {
        adapter.setItems(items)
    }

    override fun toggle(): Observable<Boolean> {
        return tvHome.clicks().map { true }
    }

    override fun getChip(): Observable<String> {
        return chipTaps
    }

    override fun updateProgressbarState(isVisible: Boolean) {
        findViewById<ProgressBar>(R.id.progressbar_category).isVisible = isVisible
    }
}
