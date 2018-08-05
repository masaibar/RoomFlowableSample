package com.masaibar.roomflowablesample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

    }
}
