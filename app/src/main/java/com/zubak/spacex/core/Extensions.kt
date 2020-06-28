package com.zubak.spacex.core

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import java.security.MessageDigest
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun ByteArray.toHex(): String {
    return joinToString("") { "%02x".format(it) }
}

fun String.sha1(): String {
    val sha1 = MessageDigest.getInstance("SHA-1")
    return sha1.digest(this.toByteArray()).toHex()
}

val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return if (tag.length <= 23) tag else tag.substring(0, 23)
    }

fun timeParser(timestamp: String): String {
    val parseDF: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
    val parsedDate: Date = parseDF.parse(timestamp)?: Date()
    val outputDF: DateFormat = SimpleDateFormat.getDateTimeInstance()

    return outputDF.format(parsedDate)
}
