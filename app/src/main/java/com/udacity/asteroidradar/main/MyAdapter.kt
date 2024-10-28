package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.ListIt5emBinding
import com.udacity.asteroidradar.model.Asteroid

class MyAdapter(var listener:ClickListener, var list:List<Asteroid>): RecyclerView.Adapter<MyAdapter.MyViewHolder>(  ) {
   inner class MyViewHolder(listIt5emBinding: ListIt5emBinding): RecyclerView.ViewHolder(listIt5emBinding.root) {
          var binding:ListIt5emBinding=listIt5emBinding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        var holder = MyViewHolder( ListIt5emBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        return holder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.item=list[position]
        holder.binding.root.setOnClickListener(){
            listener.onClick(position)
        }
    }

    override fun getItemCount(): Int {
       return list.size
    }
interface ClickListener{
    fun onClick( position:Int)
}
}