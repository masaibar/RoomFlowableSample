package com.masaibar.roomflowablesample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_view.apply {
            this.addItemDecoration(
                    DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL)
            )
            this.layoutManager = LinearLayoutManager(this@MainActivity)
        }

        val db = AppDatabase.create(this)

        db.getTimeDao().selectAll().subscribeOn(
                Schedulers.io()
        ).observeOn(
                AndroidSchedulers.mainThread()
        ).subscribeBy(
                onError = { t ->
                    Log.d("!!!", "times error, ${t.message}")
                },
                onComplete = {
                    Log.d("!!!", "times complete")
                },
                onNext = { times ->
                    Log.d("!!!", "times onNext")
                    if (recycler_view.adapter == null) {
                        recycler_view.adapter = TimeAdapter(
                                this,
                                times
                        )
                    } else {
                        val adapter = recycler_view.adapter as TimeAdapter
                        adapter.update(times)

                        //最下部までスクロールさせる
                        recycler_view.post {
                            val itemCount = adapter.itemCount
                            recycler_view.smoothScrollToPosition(if (itemCount > 0) itemCount - 1 else 0)
                        }

                    }
                }
        ).addTo(disposable)

        button_insert.setOnClickListener {
            Single.fromCallable {
                db.getTimeDao().insert(
                        Time(time = Date().time)
                )
            }.subscribeOn(
                    Schedulers.io()
            ).subscribeBy(
                    onError = { t ->
                        Log.d("!!!", "insert error ${t.message}")
                    },
                    onSuccess = { id ->
                        Log.d("!!!", "insert success, id = $id")
                    }

            ).addTo(disposable)
        }

        button_clear_all.setOnClickListener {
            Completable.fromAction {
                db.getTimeDao().deleteAll()
            }.subscribeOn(
                    Schedulers.io()
            ).subscribeBy(
                    onError = { t ->
                        Log.d("!!!", "delete error ${t.message}")
                    }
            ).addTo(disposable)
        }
    }

    override fun onDestroy() {
        disposable.clear()
        super.onDestroy()
    }
}
