package com.masaibar.roomflowablesample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

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

            )
        }


        db.getTimeDao().selectAll().subscribeOn(
                Schedulers.io()
        ).observeOn(
                AndroidSchedulers.mainThread()
        ).subscribeBy(
                onError = { t->
                    Log.d("!!!", "times error, ${t.message}")
                },
                onComplete = {
                    Log.d("!!!", "times complete")
                },
                onNext = { times ->
                        recycler_view.adapter = TimeAdapter(
                                this,
                                times
                        )
                        val adapter = recycler_view.adapter as TimeAdapter
                        adapter.update(times)

                    times.forEach { time ->
                        Log.d("!!!", "time $time")
                    }
                }
        )
    }

}
