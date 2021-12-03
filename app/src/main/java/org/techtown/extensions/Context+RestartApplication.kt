package org.techtown.extensions

import android.content.Context
import android.content.Intent
import org.techtown.petfinder.SignInActivity


fun Context.restartApplication() {
    val intent = Intent(this, SignInActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION)

    startActivity(intent)

//    엑티비티 stack 방식으로 쌓일 때 관리해주는 역할 인듯
}