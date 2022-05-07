package com.example.startupper.data

import android.content.res.Resources
import com.example.startupper.R
import com.example.startupper.model.notificationModel

class dataSource {
    fun loadNotificationModel(): List<notificationModel>{
        val notificationArray : Array<Int> = arrayOf(R.array.notificationList)
        val list : MutableList<notificationModel> = mutableListOf()
        for(n in notificationArray.indices){
            list.add(notificationModel(notificationArray[n]))
        }
        return  list
    }
}