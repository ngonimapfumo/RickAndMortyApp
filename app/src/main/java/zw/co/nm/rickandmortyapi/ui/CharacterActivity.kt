package zw.co.nm.rickandmortyapi.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import zw.co.nm.rickandmortyapi.R
import zw.co.nm.rickandmortyapi.databinding.ActivityCharacterBinding
import zw.co.nm.rickandmortyapi.models.responses.GetSingleCharacterResponse
import zw.co.nm.rickandmortyapi.network.Cache
import zw.co.nm.rickandmortyapi.viewmodels.GetCharacterViewModel

class CharacterActivity : AppCompatActivity() {
    lateinit var activityCharacterBinding: ActivityCharacterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCharacterBinding = ActivityCharacterBinding.inflate(layoutInflater)
        setContentView(activityCharacterBinding.root)
        val id = intent.getStringExtra("id")
        val getCharacterViewModel = ViewModelProvider(this)[GetCharacterViewModel::class.java]

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    getCharacterViewModel.getCharacter(Integer.parseInt(id!!)).collect(::response)
                }
            }

        }

    }

    private fun response(res: GetSingleCharacterResponse?) {
        activityCharacterBinding.statusTxt.setTextColor(
            if (res?.status!! == "Alive") {
                ContextCompat.getColor(
                    this@CharacterActivity,
                    R.color.green
                )
            } else {
                ContextCompat.getColor(
                    this@CharacterActivity,
                    R.color.red
                )
            }
        )

        activityCharacterBinding.nameTextView.text = res.name
        activityCharacterBinding.specieTxt.text = res.species
        activityCharacterBinding.typeTxt.text = res.type
        activityCharacterBinding.locationTxt.text = res.location.name
        activityCharacterBinding.statusTxt.text = res.status
        Picasso.get().load(res.image).into(activityCharacterBinding.characterImg)

        res.let {
            Cache.characterMap[res.id] = it
        }


    }
}