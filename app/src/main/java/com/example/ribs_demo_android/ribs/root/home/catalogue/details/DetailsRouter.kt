package com.example.ribs_demo_android.ribs.root.home.catalogue.details

import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link DetailsBuilder.DetailsScope}.
 *
 * TODO describe the possible child configurations of this scope.
 */
class DetailsRouter(
    view: DetailsView,
    interactor: DetailsInteractor,
    component: DetailsBuilder.Component
) : ViewRouter<DetailsView, DetailsInteractor>(view, interactor, component)
