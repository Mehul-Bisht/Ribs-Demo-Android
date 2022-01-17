package com.example.ribs_demo_android.ribs.root.home.catalogue

import io.reactivex.Observable

/**
 * Created by Mehul Bisht on 05-01-2022
 */

interface DataStream {
    fun data(): Observable<String>
}