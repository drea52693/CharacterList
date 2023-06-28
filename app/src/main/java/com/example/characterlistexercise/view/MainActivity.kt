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
import com.example.characterlistexercise.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.characterlistexercise.network.CharacterResponse
import com.example.characterlistexercise.viewmodel.MainViewModel
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity(), CharacterNamesAdapter.OnItemClickListener {
    private lateinit var characterAdapter: CharacterNamesAdapter
    private lateinit var characterDetail: CharacterResponse
    private lateinit var characterList: List<String>
    private lateinit var filteredCharacterList: MutableList<String>
    private lateinit var characterInfoList: MutableList<CharacterModel>
    private lateinit var mainViewModel: MainViewModel
    private var isTablet: Boolean = false
    private val base_url = "https://duckduckgo.com"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkIfTablet()

        val recyclerView: RecyclerView = if (isTablet){
            findViewById(R.id.recyclerView)
        } else {
            findViewById(R.id.main_recyclerview)
        }
        //val recyclerView: RecyclerView = findViewById(R.id.main_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)



        characterList = mutableListOf() // Initialize with an empty list
        filteredCharacterList = mutableListOf()
        characterInfoList = mutableListOf()

        characterAdapter = CharacterNamesAdapter(characterInfoList, this)
        recyclerView.adapter = characterAdapter

        fetchDataFromApi() // Fetch character data from API
    }

    fun checkIfTablet(){

        if (resources.getBoolean(R.bool.isTablet)) {
            // Replace the placeholder view with the tablet-specific layout
            val tabletLayoutPlaceholder: FrameLayout = findViewById(R.id.tabletLayoutPlaceholder)
            val tabletLayout = layoutInflater.inflate(R.layout.activity_character_list_tablet, null)
            tabletLayoutPlaceholder.visibility = View.VISIBLE
            tabletLayoutPlaceholder.addView(tabletLayout)
            isTablet = true
        } else {
            // Continue with the default phone layout
            // ...
        }
    }


    var characterNames: List<String> = listOf()

    private fun fetchDataFromApi() {
        // Make an API call to fetch character data using your chosen method (e.g., Retrofit)
        // Populate the characterList with the response data and assign it to filteredCharacterList as well
        // You'll need to handle any network operations and update the adapter accordingly
        val call: Call<CharacterResponse> = ApiClient.apiService.getCharacters()
        call.enqueue(object : Callback<CharacterResponse> {
            override fun onResponse(
                call: Call<CharacterResponse>,
                response: Response<CharacterResponse>
            ) {
                if (response.isSuccessful) {
                    val characterResponse: CharacterResponse? = response.body()
                    if (characterResponse != null) {
                        characterDetail = characterResponse
                        characterNames = filterResponseForCharacterNames(characterResponse)

                        for (characterInfo in characterResponse.relatedTopics){
                            var title = extractNameFromUrl(characterInfo.firstUrl)?.replace("_", " ")
                            var imageUrl = characterInfo.icon.url
                            var description = characterInfo.text

                            characterList = characterNames
                            characterInfoList.add(CharacterModel(title = title, imageUrl = imageUrl, description = description))
                        }
                        filteredCharacterList.addAll(characterNames)
                        characterAdapter.notifyDataSetChanged()
                    }
                } else {
                    // Handle API error response
                }
            }

            override fun onFailure(call: Call<CharacterResponse>, t: Throwable) {
                // Handle network or other errors
                Log.d("MainActivity", "API Call failed " + t)
            }
        })
    }

    fun filterResponseForCharacterNames(characterResponse: CharacterResponse): List<String> {
        var listOfNames = mutableListOf<String>()
        for (characterInfo in characterResponse.relatedTopics) {
            extractNameFromUrl(characterInfo.firstUrl)?.let {
                listOfNames.add(it.replace("_", " "))
            }
        }
        return listOfNames
    }

    private fun extractNameFromUrl(url: String): String? {
        val parts = url.split("/")
        return if (parts.size >= 2) {
            parts[parts.size - 1]
        } else {
            null
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
                filterCharacterList(newText)
                return true
            }
        })

        return true
    }

    private fun filterCharacterList(query: String) {
        filteredCharacterList.clear()

        if (query.isEmpty()) {
            filteredCharacterList.addAll(characterList)

        } else {
            val searchQuery = query.toLowerCase().trim()

            filteredCharacterList.addAll(characterList.filter { character ->
                character.toLowerCase().contains(searchQuery)
            })
        }
        characterAdapter.notifyDataSetChanged()
    }

    override fun onItemClick(character: CharacterModel) {
        // Handle item click event, navigate to the detail view activity, and pass the selected character's data
        if (isTablet){
            val imageView: ImageView = findViewById(R.id.characterimageView)
            val titleTextView: TextView = findViewById(R.id.titleTextView)
            val descriptionTextView: TextView =
                findViewById(R.id.descriptionTextView)

            if (!character.imageUrl.isNullOrEmpty())
                Picasso.get().load(base_url + character.imageUrl).resize(350, 550).into(imageView)
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
}