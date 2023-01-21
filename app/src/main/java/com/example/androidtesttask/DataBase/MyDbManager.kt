package com.example.androidtesttask.DataBase

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class MyDbManager (context: Context) {
    val myDbHelper = MyDbHelper(context)
    var db : SQLiteDatabase? = null

    fun openDB(){
        db = myDbHelper.writableDatabase
    }
    fun insertToDb(bin : String){
        val values = ContentValues().apply{
            put(MyDbNameClass.COLUMN_NAME_BIN, bin)
        }
        db?.insert(MyDbNameClass.TABLE_NAME,null, values)
    }
    @SuppressLint("Range")
    fun readDbData() : ArrayList<String>{
        val dataList = ArrayList<String>()
        val cursor = db?.query(MyDbNameClass.TABLE_NAME, null, null, null,
            null , null, null)
        while (cursor?.moveToNext()!!){
            val dataText = cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_BIN))
            dataList.add(dataText.toString())
        }
        cursor.close()
        return dataList
    }
    fun closeDb(){
        myDbHelper.close()
    }
}