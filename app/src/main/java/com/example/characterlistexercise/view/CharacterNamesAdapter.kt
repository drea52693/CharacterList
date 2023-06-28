package com.example.characterlistexercise.view


import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.characterlistexercise.R
import com.example.characterlistexercise.model.CharacterModel

class CharacterNamesAdapter(private val characterInfoList: List<CharacterModel>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<CharacterNamesAdapter.CharacterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.character_item_layout,
            parent, false
        )
        return CharacterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val currentItem = characterInfoList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return characterInfoList.size
    }

    inner class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val nameTextView: TextView = itemView.findViewById(R.id.name_textview)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(character: CharacterModel) {
            nameTextView.text = character.title
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val clickedItem = characterInfoList[position]
                listener.onItemClick(clickedItem)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(character: CharacterModel)
    }
}