package mb.com.onthego

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties

data class Entry(
    val id: String? = "",
    val amount: String,
    val category: String = "",
    val date: String = "")
