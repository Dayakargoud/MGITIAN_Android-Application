package com.dayakar.mgitian.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dayakar.mgitian.R

class MainRecycerView_Adapter(internal var items: List<String>,internal var lecturers: List<String>,
                              internal var times: List<String>,internal var locations: List<String>,internal var mContext: Context) : RecyclerView.Adapter<MainRecycerView_Adapter.ItemHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.single_list_item2, parent, false)
        return ItemHolder(v)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.mtext.text = items[position]
        holder.lectur.text=lecturers[position]
        holder.time.text=times[position]
        holder.location.text=locations[position]


    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var mtext: TextView
        internal var lectur:TextView
        internal var  time:TextView
        internal var  location:TextView



        init {
            mtext = itemView.findViewById(R.id.sub_name)
            lectur=itemView.findViewById(R.id.lecturer_name)
            time=itemView.findViewById(R.id.eventDetails_timings)
            location=itemView.findViewById(R.id.location)
        }
    }
}
