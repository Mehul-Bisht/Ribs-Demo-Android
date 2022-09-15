package com.example.ribs_demo_android.ribs.root.home.catalogue

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

/**
 * Created by Mehul Bisht on 05-01-2022
 */

class MutableDataStream(data: String): DataStream {

    private val behaviorRelay = PublishSubject.create<String>()

    init {
        behaviorRelay.onNext(data)
    }

    fun setData(data: String) {
        behaviorRelay.onNext(data)
    }

    override fun data(): Observable<String> {
        return behaviorRelay
    }
}