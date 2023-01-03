package com.solacestudios.ribs_demo_android.network

import com.solacestudios.ribs_demo_android.util.Resource
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

fun <T> createResult(apiObserver: Observable<T>): Observable<Resource<T>> {
  return Observable.create { emitter ->
    emitter.onNext(Resource.Loading())

    apiObserver
      .subscribeOn(Schedulers.io())
      .subscribe ({
        if (!emitter.isDisposed) {
          emitter.onNext(Resource.Success(it))
        }
      }){emitter.onNext(Resource.Error(it.message.toString()))}
  }
}