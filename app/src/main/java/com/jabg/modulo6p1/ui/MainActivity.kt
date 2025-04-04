package com.jabg.modulo6p1.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jabg.modulo6p1.R
import com.jabg.modulo6p1.application.SongsDBApp
import com.jabg.modulo6p1.data.SongRepository
import com.jabg.modulo6p1.data.db.model.SongEntity
import com.jabg.modulo6p1.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var songs :MutableList<SongEntity> = mutableListOf()

    private lateinit var repository: SongRepository

    private lateinit var songAdapter: SongAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

       repository = (application as SongsDBApp).repository

        songAdapter = SongAdapter { songSelector ->
            val dialog = SongDialog (
                newSong = false,
                song = songSelector,
                updateUI = {
                    updateUI()
                },
                message = { text, type ->
                    message(text, type)
                }
            )
            dialog.show(supportFragmentManager, "dialogUpDelSong")
        }

        binding.apply {
            rvSongs.layoutManager = LinearLayoutManager(this@MainActivity)
            rvSongs.adapter = songAdapter
        }
        updateUI()
    }

    private fun updateUI(){
        lifecycleScope.launch {
            songs = repository.getAllSongs()
            binding.tvNoSongs.visibility=
                if (songs.isNotEmpty()) View.INVISIBLE else View.VISIBLE

            songAdapter.updateList(songs)
        }
    }

    fun addSong(view: View) {
        val dialog = SongDialog(
            updateUI = {
                updateUI()
            },
            message = {text, type ->
                message(text, type)
            }
        )
        dialog.show(supportFragmentManager, "dialogNewSong")
    }

    private fun message(text: String, type: String) {
        if (type == getString(R.string.ok))
        {
            Snackbar.make(binding.main, text, Snackbar.LENGTH_SHORT)
                .setTextColor(getColor(R.color.white))
                .setBackgroundTint(getColor(R.color.green))
                .show()
        }else{
            Snackbar.make(binding.main, text, Snackbar.LENGTH_SHORT)
                .setTextColor(getColor(R.color.white))
                .setBackgroundTint(getColor(R.color.red))
                .show()
        }
    }


}