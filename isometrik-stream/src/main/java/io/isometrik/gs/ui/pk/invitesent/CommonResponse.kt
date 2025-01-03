package io.isometrik.gs.ui.pk.invitesent

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * <h1>BlockUserResponse</h1>
 *
 * @author 3embed
 * @since 4/9/2018
 * @version 1.0
 */
class CommonResponse {
    @SerializedName("code")
    @Expose
    var code: Int? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

}
