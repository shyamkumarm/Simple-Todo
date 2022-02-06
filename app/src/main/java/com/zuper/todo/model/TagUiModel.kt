package com.zuper.todo.model


sealed class TagUiModel {
data class TaskModel(var title       : String?  = null,
                 var author      : String?  = null,
                 var tag         : String,
                 var isCompleted : Boolean? = null,
                 var priority    : String?  = null,
                 var id          : Int?     = null) : TagUiModel() {
  constructor(user: Data) : this(user.title, user.author,user.tag!!,user.isCompleted,user.priority,user.id)
 }

 data class SeparatorBadgeModel(val badgeText: String) : TagUiModel()


}