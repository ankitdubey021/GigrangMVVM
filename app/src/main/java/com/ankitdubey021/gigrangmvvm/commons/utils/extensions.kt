package com.ankitdubey021.gigrangmvvm.commons.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import java.util.regex.Matcher
import java.util.regex.Pattern

inline fun <reified T:Any> newIntent(context: Context): Intent = Intent(context,T::class.java)

inline fun <reified T : Any> Activity.launchActivity(
    requestCode:Int =-1,
    options: Bundle?=null,
    noinline init: Intent.() -> Unit = {}){

    val intent = newIntent<T>(this)
    intent.init()
    startActivityForResult(intent,requestCode,options)
}

fun Context.toast(msg : String){
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

fun View.hide(){
    this.visibility = View.GONE
}
fun View.show(){
    this.visibility = View.VISIBLE
}

fun Context.getColorRes(@ColorRes id: Int) = ContextCompat.getColor(applicationContext, id)

fun String.isValidEmail(): Boolean {
    if (this.startsWith(".") || this.startsWith("_") || this.contains("..") || this.contains("__")
        || this.contains("._") || this.contains("_.") || this.contains(".@") || this.contains("_@")) {
        return false
    }
    val pattern: Pattern
    val matcher: Matcher
    val emailPattern =
        "^[_A-Za-z0-9]+(\\.[_A-Za-z0-9]+)*@[A-Za-z0-9]+(\\.[A-Za-z]{2,}){1,}$"
    pattern = Pattern.compile(emailPattern)
    matcher = pattern.matcher(this)
    return matcher.matches()
}
