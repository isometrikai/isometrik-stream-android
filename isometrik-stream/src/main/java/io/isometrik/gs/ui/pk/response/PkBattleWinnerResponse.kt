package io.isometrik.gs.ui.pk.response;

import com.google.gson.annotations.SerializedName


data class PkBattleWinnerResponse(

    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: PkResultData? = PkResultData()

)


data class PkResultData(

    @SerializedName("winnerId") var winnerId: String? = null,
    @SerializedName("winnerStreamUserId") var winnerStreamUserId: String? = null,
    @SerializedName("percentageOfCoinsFirstUser") var percentageOfCoinsFirstUser: Double? = null,
    @SerializedName("percentageOfCoinsSecondUser") var percentageOfCoinsSecondUser: Double? = null,
    @SerializedName("totalCoinsFirstUser") var totalCoinsFirstUser: Double? = null,
    @SerializedName("totalCoinsSecondUser") var totalCoinsSecondUser: Double? = null

)