package com.masaibar.roomflowablesample

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity
class Time(
        @PrimaryKey(autoGenerate = true) var id: Long = 0,
        @NonNull var time: Long
)