package com.example.oddsprofit

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
data class Outcomes(
  @SerializedName("name") var name: String? = null,
  @SerializedName("price") var price: Double? = null,
  @SerializedName("point") var point: Double? = null
) : Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readString(),
    parcel.readValue(Double::class.java.classLoader) as? Double,
    parcel.readValue(Double::class.java.classLoader) as? Double
  )

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(name)
    parcel.writeValue(price)
    parcel.writeValue(point)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<Outcomes> {
    override fun createFromParcel(parcel: Parcel): Outcomes {
      return Outcomes(parcel)
    }

    override fun newArray(size: Int): Array<Outcomes?> {
      return arrayOfNulls(size)
    }
  }
}
