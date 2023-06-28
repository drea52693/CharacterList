package com.example.characterlistexercise.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.characterlistexercise.network.ApiClient
import com.example.characterlistexercise.network.CharacterResponse
import com.example.characterlistexercise.model.CharacterModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var characterDetail: CharacterResponse
    private var characterList: List<String> = mutableListOf()
    private var filteredCharacterList: MutableList<String> = mutableListOf()
    var characterInfoList: MutableList<CharacterModel> = mutableListOf()
    var characterNames: List<String> = listOf()

    fun fetchDataFromApi() {
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

    fun filterCharacterList(query: String) {
        filteredCharacterList.clear()

        if (query.isEmpty()) {
            filteredCharacterList.addAll(characterList)

        } else {
            val searchQuery = query.toLowerCase().trim()

            filteredCharacterList.addAll(characterList.filter { character ->

                character.toLowerCase().contains(searchQuery)
            })
        }

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
}