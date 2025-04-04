package com.jabg.modulo6p1.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jabg.modulo6p1.data.db.model.SongEntity
import com.jabg.modulo6p1.databinding.SongElementBinding

class SongAdapter (
  private val onSongClick : (SongEntity) -> Unit
) : RecyclerView.Adapter<SongViewHolder>(){

    private var songs : List<SongEntity> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = SongElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding)
    }

    override fun getItemCount(): Int  = songs.size

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]

        holder.bind(song)

        holder.itemView.setOnClickListener{
            //Click element
            onSongClick(song)
        }
    }

    fun updateList (list: MutableList<SongEntity>){
        songs = list
        notifyDataSetChanged()
    }


}