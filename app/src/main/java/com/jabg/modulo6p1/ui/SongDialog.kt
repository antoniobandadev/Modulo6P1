package com.jabg.modulo6p1.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.jabg.modulo6p1.R
import com.jabg.modulo6p1.application.SongsDBApp
import com.jabg.modulo6p1.data.SongRepository
import com.jabg.modulo6p1.data.db.model.SongEntity
import com.jabg.modulo6p1.databinding.SongDialogBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.IOException


class SongDialog(
    private val newSong : Boolean = true,
    private var song: SongEntity = SongEntity(
        title = "",
        artist = "",
        album = "",
        genre = "",
        year = 0,
        durationSec = 0
    ),
    private val updateUI : () -> Unit,
    private val message : (String, String) -> Unit

) : DialogFragment() {

    private var _binding: SongDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var dialog: Dialog

    private var saveButton : Button? = null

    private lateinit var repository: SongRepository

    private var genreVal = song.genre

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        _binding = SongDialogBinding.inflate(requireActivity().layoutInflater)

        binding.apply {
            tietTitle.setText(song.title)
            tietArtist.setText(song.artist)
            tieAlbum.setText(song.album)
            if (song.year != 0){
                tietYear.setText(song.year.toString())
            }
            if (song.durationSec != 0){
                tietDuration.setText(song.durationSec.toString())
            }
        }


        val genres = resources.getStringArray(R.array.Genres)
        val autoCompleteTextView = binding.spinGenre
        val adapter = ArrayAdapter(requireContext(), R.layout.drop_down_genre, genres)
        autoCompleteTextView.setAdapter(adapter)
        autoCompleteTextView.setText(genreVal, false)

        binding.spinGenre.setOnItemClickListener { parent, view, position, id ->
            genreVal = parent.getItemAtPosition(position).toString()
            saveButton?.isEnabled = validateFields()
        }

        dialog = if (newSong) {
            buildDialog(getString(R.string.save), getString(R.string.cancel),{
                //Save
                binding.apply {
                    song.title = tietTitle.text.toString().trim()
                    song.artist = tietArtist.text.toString().trim()
                    song.album = tieAlbum.text.toString().trim()
                    song.genre = genreVal
                    song.year = tietYear.text.toString().trim().toInt()
                    song.durationSec = tietDuration.text.toString().trim().toInt()
                }

                try {

                    lifecycleScope.launch {
                        val result = async {
                            repository.insertSong(song)
                        }
                        result.await()
                        message(getString(R.string.o_save_song),getString(R.string.ok))
                        updateUI()
                    }

                }catch (e : IOException){
                    e.printStackTrace()
                    message(getString(R.string.e_save_song),getString(R.string.error))
                }catch (e: Exception){
                    e.printStackTrace()
                }

            },{
                //Cancel

            })
        }else {

            //Update
            buildDialog(getString(R.string.update), getString(R.string.delete),{
                binding.apply {
                    song.title = tietTitle.text.toString().trim()
                    song.artist = tietArtist.text.toString().trim()
                    song.album = tieAlbum.text.toString().trim()
                    song.genre = genreVal
                    song.year = tietYear.text.toString().trim().toInt()
                    song.durationSec = tietDuration.text.toString().trim().toInt()
                }

                try {

                    lifecycleScope.launch {
                        val result = async {
                            repository.updateSong(song)
                        }
                        result.await()
                        message(getString(R.string.o_update_song),getString(R.string.ok))
                        updateUI()
                    }

                }catch (e : IOException){
                    e.printStackTrace()
                    message(getString(R.string.e_update_song),getString(R.string.error))
                }catch (e: Exception){
                    e.printStackTrace()
                }

            },{

                //Delete
                val context = requireContext()

                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.confirmation))
                    .setMessage(getString(R.string.delete_confirmation, song.title))
                    .setPositiveButton(getString(R.string.accept)) { _, _ ->
                        try {
                            lifecycleScope.launch {

                                val result = async {
                                    repository.deleteSong(song)
                                }

                                result.await()

                                message(context.getString(R.string.o_delete_song), context.getString(R.string.ok))

                                updateUI()

                            }

                        } catch (e: IOException) {

                            e.printStackTrace()
                            message(context.getString(R.string.e_delete_song), context.getString(R.string.error))

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    .setNegativeButton(context.getString(R.string.cancel)) { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                    .create()
                    .show()
            })
        }

        return dialog
    }


    override fun onStart() {
        super.onStart()

        repository = (requireContext().applicationContext as SongsDBApp).repository

        saveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        saveButton?.isEnabled = false

        binding.apply {
            setupTextWatcher(
                tietTitle,
                tietArtist,
                tieAlbum,
                tietYear,
                tietDuration
            )
        }

    }


    private fun buildDialog(
        btn1Text: String,
        btn2Text: String,
        positiveButton: () -> Unit,
        negativeButton: () -> Unit,
    ): Dialog =
        AlertDialog.Builder(requireContext()).setView(binding.root)
            .setTitle(getString(R.string.my_song))
            .setPositiveButton(btn1Text) { _, _ ->
                //Click positive
                positiveButton()
            }
            .setNegativeButton(btn2Text) { _, _ ->
                //Click negative
                negativeButton()
            }
            .create()



    private fun validateFields(): Boolean =
        binding.tietTitle.text.toString().trim().isNotEmpty()
                && binding.tietArtist.text.toString().trim().isNotEmpty()
                && binding.tieAlbum.text.toString().trim().isNotEmpty()
                && binding.tietYear.text.toString().trim().isNotEmpty()
                && binding.tietDuration.text.toString().trim().isNotEmpty()
                && genreVal.trim().isNotEmpty()


    private fun setupTextWatcher(vararg textFields: TextInputEditText) {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                saveButton?.isEnabled = validateFields()
                validateFieldsLen()

            }
        }

        textFields.forEach { it.addTextChangedListener(textWatcher) }
    }

    private fun validateFieldsLen(){
        binding.apply {
            val title = tietTitle.text.toString().trim()
            if(title.length in 1..2 && title.isNotEmpty()){
                tilTitle.error = getString(R.string.field_len)
                saveButton?.isEnabled = false
            }else{
                tilTitle.error = null
                tilTitle.isErrorEnabled = false
            }

            val artist = tietArtist.text.toString().trim()
            if(artist.length in 1..2 && artist.isNotEmpty()){
                tilArtist.error = getString(R.string.field_len)
                saveButton?.isEnabled = false
            }else{
                tilArtist.error = null
                tilArtist.isErrorEnabled = false
            }

            val album = tieAlbum.text.toString().trim()
            if(album.length in 1..2 && album.isNotEmpty()){
                tilAlbum.error = getString(R.string.field_len)
                saveButton?.isEnabled = false
            }else{
                tilAlbum.error = null
                tilAlbum.isErrorEnabled = false
            }

            val year = tietYear.text.toString().trim()
            if(year.toIntOrNull() !in 1500..2025 && year.length > 0){
                tilYear.error = getString(R.string.year_valid)
                saveButton?.isEnabled = false
            }else{
                tilYear.error = null
                tilYear.isErrorEnabled = false
            }

            val duration = tietDuration.text.toString().trim()
            if(duration.toIntOrNull() !in 60 .. 6000 && duration.length > 0){
                tilDuration.error = getString(R.string.duration_valid)
                saveButton?.isEnabled = false
            }else{
                tilDuration.error = null
                tilDuration.isErrorEnabled = false
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
