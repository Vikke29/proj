package com.example.oddsprofit

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class LeaguesResponseDataClass (

    @SerializedName("key"           ) var key          : String?  = null,
    @SerializedName("group"         ) var group        : String?  = null,
    @SerializedName("title"         ) var title        : String?  = null,
    @SerializedName("description"   ) var description  : String?  = null,
    @SerializedName("active"        ) var active       : Boolean? = null,
    @SerializedName("has_outrights" ) var hasOutrights : Boolean? = null

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(key)
        parcel.writeString(group)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeValue(active)
        parcel.writeValue(hasOutrights)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LeaguesResponseDataClass> {
        override fun createFromParcel(parcel: Parcel): LeaguesResponseDataClass {
            return LeaguesResponseDataClass(parcel)
        }

        override fun newArray(size: Int): Array<LeaguesResponseDataClass?> {
            return arrayOfNulls(size)
        }
    }
}
