package com.example.characterlistexercise.view

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.characterlistexercise.R
import com.example.characterlistexercise.model.CharacterModel
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        getBundle()
    }

    private fun getBundle() {
        val bundle: Bundle? = intent.getBundleExtra("bundle")
        if (bundle != null) {
            val character: CharacterModel? = bundle.getParcelable("character")
            if (character != null) {
                // Use the character object to populate the detail view
                val imageView: ImageView = findViewById(R.id.character_image_imageview)
                val titleTextView: TextView = findViewById(R.id.character_title_textview)
                val descriptionTextView: TextView =
                    findViewById(R.id.character_description_textview)

                // Set the character's image, title, and description in the respective views
                if (!character.imageUrl.isNullOrEmpty())
                    Picasso.get().load(MainActivity.BASE_URL + character.imageUrl).resize(350, 550).into(imageView)
                titleTextView.text = character.title
                descriptionTextView.text = character.description
            }
        }
    }
}
