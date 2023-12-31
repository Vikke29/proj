package com.example.oddsprofit

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class SportModel (
    @SerializedName("sportName"     ) var sportName     : String?               = null,
    @SerializedName("listOfLeagues"            ) var listOfLeagues           : MutableList<LeaguesResponseDataClass>?         = null,
    @SerializedName("isExpanded"     ) var isExpanded     : Boolean?               = null

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.createTypedArrayList(LeaguesResponseDataClass),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(sportName)
        parcel.writeTypedList(listOfLeagues)
        parcel.writeValue(isExpanded)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SportModel> {
        override fun createFromParcel(parcel: Parcel): SportModel {
            return SportModel(parcel)
        }

        override fun newArray(size: Int): Array<SportModel?> {
            return arrayOfNulls(size)
        }
    }
}