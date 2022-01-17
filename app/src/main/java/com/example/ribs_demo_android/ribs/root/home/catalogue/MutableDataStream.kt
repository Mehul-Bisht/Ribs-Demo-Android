package com.example.ribs_demo_android.ribs.root.home.catalogue

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable

/**
 * Created by Mehul Bisht on 05-01-2022
 */

class MutableDataStream(data: String): DataStream {

    private val behaviorRelay: BehaviorRelay<String> = BehaviorRelay.create()

    init {
        behaviorRelay.accept(data)
    }

    fun setData(data: String) {
        behaviorRelay.accept(data)
    }

    override fun data(): Observable<String> {
        return behaviorRelay.hide()
    }
}