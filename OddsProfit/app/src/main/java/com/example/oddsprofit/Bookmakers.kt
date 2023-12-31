package com.example.oddsprofit

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Bookmakers(
  @SerializedName("key") var key: String? = null,
  @SerializedName("title") var title: String? = null,
  @SerializedName("last_update") var lastUpdate: String? = null,
  @SerializedName("markets") var markets: ArrayList<Markets> = arrayListOf()
) : Parcelable {

  constructor(parcel: Parcel) : this(
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    arrayListOf<Markets>().apply {
      parcel.readList(this, Bookmakers::class.java.classLoader)
    }
  )

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(key)
    parcel.writeString(title)
    parcel.writeString(lastUpdate)
    parcel.writeList(markets)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<Bookmakers> {
    override fun createFromParcel(parcel: Parcel): Bookmakers {
      return Bookmakers(parcel)
    }

    override fun newArray(size: Int): Array<Bookmakers?> {
      return arrayOfNulls(size)
    }
  }
}



