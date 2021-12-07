package org.techtown.datas

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class Petdata(

    @DocumentId val documentId: String = "",

    val writerUid: String = "",
    val writerName: String = "",
    val writerContact: String = "",

    val pictures: List<String> = ArrayList(),
    val title: String = "",
    val race: String = "",
    val age: String = "",
    val sex: String = "",
    val date: Date = Date(),
    val area: String = "",
    val location: String = "",
    val content: String = "",
    val type: Int = 0,
    val activation: Boolean = true,
    @ServerTimestamp val timestamp: Date? = null
): Parcelable
