package com.zuper.todo.model

import com.google.gson.annotations.SerializedName

data class Data (

 var title       : String?  = null,
 var author      : String?  = null,
 var tag         : String?  = null,
 @SerializedName("is_completed")
 var isCompleted : Boolean? = null,
 var priority    : String?  = null,
 var id          : Int?     = null

)