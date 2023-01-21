package com.example.androidtesttask.DataBase

import android.provider.BaseColumns

object MyDbNameClass {

    const val TABLE_NAME = "my_table"
    const val COLUMN_NAME_BIN = "bin"

    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "ApplicationDb.db"

    const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
            "$COLUMN_NAME_BIN TEXT)"
    const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}