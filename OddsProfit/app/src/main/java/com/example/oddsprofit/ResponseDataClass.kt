package com.example.oddsprofit

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ResponseDataClass (

  @SerializedName("id"            ) var id           : String?               = null,
  @SerializedName("sport_key"     ) var sportKey     : String?               = null,
  @SerializedName("sport_title"   ) var sportTitle   : String?               = null,
  @SerializedName("commence_time" ) var commenceTime : String?               = null,
  @SerializedName("home_team"     ) var homeTeam     : String?               = null,
  @SerializedName("away_team"     ) var awayTeam     : String?               = null,
  @SerializedName("bookmakers"    ) var bookmakers   : ArrayList<Bookmakers> = arrayListOf()

) : Parcelable {

  constructor(parcel: Parcel) : this(
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    parcel.readString(),
    arrayListOf<Bookmakers>().apply {
      parcel.readList(this, Bookmakers::class.java.classLoader)
    }
  )

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(id)
    parcel.writeString(sportKey)
    parcel.writeString(sportTitle)
    parcel.writeString(commenceTime)
    parcel.writeString(homeTeam)
    parcel.writeString(awayTeam)
    parcel.writeList(bookmakers)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<ResponseDataClass> {
    override fun createFromParcel(parcel: Parcel): ResponseDataClass {
      return ResponseDataClass(parcel)
    }

    override fun newArray(size: Int): Array<ResponseDataClass?> {
      return arrayOfNulls(size)
    }
  }
}