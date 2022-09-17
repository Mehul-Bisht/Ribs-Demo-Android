package com.solacestudios.ribs_demo_android.ribs.catalogue

import io.reactivex.rxjava3.core.Observable

/**
 * Created by Mehul Bisht on 05-01-2022
 */

interface DataStream {
    fun data(): Observable<String>
}