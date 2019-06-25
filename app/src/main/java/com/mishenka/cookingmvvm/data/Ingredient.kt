package com.mishenka.cookingmvvm.data

import android.os.Parcel
import android.os.Parcelable
import com.beust.klaxon.Json

data class Ingredient (
        var text : String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString()
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(text)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Ingredient> {
        override fun createFromParcel(source: Parcel): Ingredient {
            return Ingredient(source)
        }

        override fun newArray(size: Int): Array<Ingredient?> {
            return arrayOfNulls(size)
        }
    }
}