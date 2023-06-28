package com.example.characterlistexercise.model

import android.os.Parcel
import android.os.Parcelable

class CharacterModel(var title: String?, var imageUrl: String?, var description: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(imageUrl)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CharacterModel> {
        override fun createFromParcel(parcel: Parcel): CharacterModel {
            return CharacterModel(parcel)
        }

        override fun newArray(size: Int): Array<CharacterModel?> {
            return arrayOfNulls(size)
        }
    }
}