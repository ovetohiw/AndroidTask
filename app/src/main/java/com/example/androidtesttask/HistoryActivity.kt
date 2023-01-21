package com.example.androidtesttask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidtesttask.DataBase.MyAdapter
import com.example.androidtesttask.DataBase.MyDbManager
import com.example.androidtesttask.DataBase.MyDbNameClass
import com.example.androidtesttask.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {

    private lateinit var bindingClass: ActivityHistoryBinding
    private val myDbManager = MyDbManager(this)
    val myAdapter = MyAdapter(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)

        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDB()
        fillAdapter()
    }

    private fun init() {
        val rcView = bindingClass.recyclerView
        rcView.layoutManager = LinearLayoutManager(this)
        rcView.adapter = myAdapter
    }

    private fun fillAdapter() {
        myAdapter.updateAdapter(myDbManager.readDbData().reversed())
    }

    fun onClickClearHistory(view: View) {
        myDbManager.db?.execSQL(MyDbNameClass.SQL_DELETE_TABLE)
        myDbManager.myDbHelper.onCreate(myDbManager.db)
        finish()
    }
}