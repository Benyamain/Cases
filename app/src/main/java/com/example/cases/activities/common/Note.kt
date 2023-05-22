package com.example.cases.activities.common

import java.io.Serializable

interface Note : Serializable {
    val id: Int?
    val title: String?
    val databaseCase: String?
    val date: String?
}
