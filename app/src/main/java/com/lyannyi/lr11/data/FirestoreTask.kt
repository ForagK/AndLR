package com.lyannyi.lr11.data

import java.util.Date

data class FirestoreTask(
    val id: String = "",
    val name: String = "",
    val status: Boolean = false,
    val deadline: Date = Date()
)
