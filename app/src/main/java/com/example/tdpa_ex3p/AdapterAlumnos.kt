package com.example.tdpa_ex3p

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tdpa_ex3p.databinding.ListItemsBinding
import android.view.LayoutInflater
import android.widget.TextView
import com.bumptech.glide.Glide

class AdapterAlumnos : RecyclerView.Adapter<AdapterAlumnos.ViewHolder>() {

    lateinit var context: Context
    lateinit var cursor: Cursor

    fun RecyclerViewAdapterAlumnos(context: Context, cursor: Cursor){
        this.context = context
        this.cursor = cursor
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.list_items, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        cursor.moveToPosition(position)

        holder.nombre.text = cursor.getString(1)
        holder.imagen = cursor.getString(5)
    }

    override fun getItemCount(): Int {
        if(cursor == null){
            return 0
        } else {
            return cursor.count
        }
    }

    inner class ViewHolder : RecyclerView.ViewHolder {
        val nombre:TextView
        var imagen:String = ""
        constructor(view: View) : super(view) {
            val bindingItemsRV = ListItemsBinding.bind(view)
            nombre = bindingItemsRV.alumnName
            val url: Uri = Uri.parse(imagen)
            Glide.with(context).load(url).into(bindingItemsRV.profilePic)
        }
    }


}