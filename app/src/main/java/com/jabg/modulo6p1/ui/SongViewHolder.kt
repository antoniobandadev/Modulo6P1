package com.jabg.modulo6p1.ui

import androidx.recyclerview.widget.RecyclerView
import com.jabg.modulo6p1.data.db.model.SongEntity
import com.jabg.modulo6p1.databinding.SongElementBinding

class SongViewHolder(private val binding: SongElementBinding)
    : RecyclerView.ViewHolder(binding.root){

    fun bind (song: SongEntity){
        binding.apply {

            binding.apply {
                val context = itemView.context
                val resourceImg = context.resources.getIdentifier(song.genre.lowercase(), "drawable", context.packageName)
                ivIcon.setImageResource(resourceImg)
                tvTitle.text = song.title
                tvArtist.text = song.artist
                tvAlbum.text = song.album
                tvYear.text = song.year.toString()
                tvDuration.text = song.durationSec.toString()
            }
        }
    }

}