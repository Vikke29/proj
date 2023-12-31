package com.example.oddsprofit

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Markets (
  @SerializedName("key") var key: String? = null,
  @SerializedName("last_update") var lastUpdate: String? = null,
  @SerializedName("outcomes") var outcomes: ArrayList<Outcomes> = arrayListOf()
) : Parcelable {

  constructor(parcel: Parcel) : this(
    parcel.readString(),
    parcel.readString(),
    arrayListOf<Outcomes>().apply {
      parcel.readList(this, Outcomes::class.java.classLoader)
    }
  )

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(key)
    parcel.writeString(lastUpdate)
    parcel.writeList(outcomes)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<Markets> {
    override fun createFromParcel(parcel: Parcel): Markets {
      return Markets(parcel)
    }

    override fun newArray(size: Int): Array<Markets?> {
      return arrayOfNulls(size)
    }
  }
}
