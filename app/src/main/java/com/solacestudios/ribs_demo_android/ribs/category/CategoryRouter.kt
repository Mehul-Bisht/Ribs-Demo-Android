package com.solacestudios.ribs_demo_android.ribs.category

import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link CategoryBuilder.CategoryScope}.
 *
 * TODO describe the possible child configurations of this scope.
 */
class CategoryRouter(
    view: CategoryView,
    interactor: CategoryInteractor,
    component: CategoryBuilder.Component
) : ViewRouter<CategoryView, CategoryInteractor>(view, interactor, component)
