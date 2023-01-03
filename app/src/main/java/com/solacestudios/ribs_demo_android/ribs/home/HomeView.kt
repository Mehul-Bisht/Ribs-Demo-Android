package com.solacestudios.ribs_demo_android.ribs.home

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

/**
 * Top level view for {@link RootBuilder.RootScope}.
 */
class HomeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle), HomeInteractor.Presenter
