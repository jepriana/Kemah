package com.ca214.kemah.models

import android.os.Parcel
import android.os.Parcelable

data class Campground(
    val name: String?,
    val location: String?,
    val address: String?,
    val price: Int,
    val imageUrl: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(location)
        parcel.writeString(address)
        parcel.writeInt(price)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Campground> {
        override fun createFromParcel(parcel: Parcel): Campground {
            return Campground(parcel)
        }

        override fun newArray(size: Int): Array<Campground?> {
            return arrayOfNulls(size)
        }
    }
}
