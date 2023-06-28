package com.example.characterlistexercise.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.characterlistexercise.R
import com.example.characterlistexercise.model.CharacterModel
import com.example.characterlistexercise.viewmodel.MainViewModel
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), CharacterNamesAdapter.OnItemClickListener {

    private val viewModel: MainViewModel by viewModel()
    private var isTablet: Boolean = false
    private lateinit var characterAdapter: CharacterNamesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkIfTablet()

        val recyclerView: RecyclerView = if (isTablet){
            findViewById(R.id.recyclerView)
        } else {
            findViewById(R.id.main_recyclerview)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        characterAdapter = CharacterNamesAdapter(viewModel.characterInfoList, this)
        recyclerView.adapter = characterAdapter

        viewModel.fetchDataFromApi()
    }

    private fun checkIfTablet(){
        if (resources.getBoolean(R.bool.isTablet)) {
            val tabletLayoutPlaceholder: FrameLayout = findViewById(R.id.tabletLayoutPlaceholder)
            val tabletLayout = layoutInflater.inflate(R.layout.activity_character_list_tablet, null)
            tabletLayoutPlaceholder.visibility = View.VISIBLE
            tabletLayoutPlaceholder.addView(tabletLayout)
            isTablet = true
        } else {
            // Continue with the default phone layout
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_character_list, menu)
        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // TODO Fix this

                viewModel.filterCharacterList(newText)
                return true
            }
        })

        return true
    }

    override fun onItemClick(character: CharacterModel) {
        // Handle item click event, navigate to the detail view activity, and pass the selected character's data
        if (isTablet){
            val imageView: ImageView = findViewById(R.id.characterimageView)
            val titleTextView: TextView = findViewById(R.id.titleTextView)
            val descriptionTextView: TextView =
                findViewById(R.id.descriptionTextView)

            if (!character.imageUrl.isNullOrEmpty())
                Picasso.get().load(BASE_URL + character.imageUrl).resize(350, 550).into(imageView)
            titleTextView.text = character.title
            descriptionTextView.text = character.description

        } else {
            val bundle = Bundle()
            bundle.putParcelable("character", character)

            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("bundle", bundle)

            startActivity(intent)
        }
    }

    companion object {
        const val BASE_URL = "https://duckduckgo.com"
    }
}