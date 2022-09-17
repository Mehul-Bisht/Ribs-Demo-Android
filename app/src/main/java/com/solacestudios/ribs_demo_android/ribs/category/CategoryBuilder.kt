package com.solacestudios.ribs_demo_android.ribs.category

import android.view.LayoutInflater
import android.view.ViewGroup
import com.solacestudios.ribs_demo_android.R
import com.uber.rib.core.ViewBuilder

/**
 * Builder for the {@link CategoryScope}.
 *
 * TODO describe this scope's responsibility as a whole.
 */
class CategoryBuilder(dependency: CategoryComponent.ParentComponent) :
    ViewBuilder<CategoryView, CategoryRouter, CategoryComponent.ParentComponent>(dependency) {

    fun build(parentViewGroup: ViewGroup): CategoryRouter {
        val view = createView(parentViewGroup)
        val interactor = CategoryInteractor()
        val component = DaggerCategoryComponent.builder()
            .parentComponent(dependency)
            .view(view)
            .interactor(interactor)
            .build()
        return component.getCategoryRouter()
    }
    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): CategoryView {
        // TODO: Inflate a new view using the provided inflater, or create a new view programatically using the
        // provided context from the parentViewGroup.
        return inflater.inflate(R.layout.category_rib, parentViewGroup, false) as CategoryView
    }

}
