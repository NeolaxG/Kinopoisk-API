package com.cinema.search.cinemasearch.api

import android.os.Parcel
import android.os.Parcelable

data class Movie(
    val name: String,
    val description: String,
    val year: Int,
    val posterUrl: String,
    val rating: Rating
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readParcelable(Rating::class.java.classLoader) ?: Rating(0.0, 0.0, 0.0, 0.0)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeInt(year)
        parcel.writeString(posterUrl)
        parcel.writeString(rating.toString())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }
}
