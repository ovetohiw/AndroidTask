package com.example.androidtesttask.DataBase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidtesttask.R

class MyAdapter(listMain:java.util.ArrayList<String>) : RecyclerView.Adapter<MyAdapter.MyHolder>() {
    var listArray = listMain


    class MyHolder(itemView : View) :RecyclerView.ViewHolder(itemView){
        val binHistory : TextView = itemView.findViewById(R.id.tvBIN)

            fun setData(bin:String){
                binHistory.text = "BIN/IIN: $bin"
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyHolder(inflater.inflate(R.layout.item_list_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return listArray.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.setData(listArray.get(position))
    }

    fun updateAdapter(listItems: List<String>){

        listArray.clear()
        listArray.addAll(listItems)
        notifyDataSetChanged()
    }

}