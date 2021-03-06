package com.example.uniphoto.base.extensions

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Context.isPermissionGranted(permission: String) =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

fun Context.startActivityIfExist(intent: Intent) = (intent.resolveActivity(packageManager) != null)
    .also { isActivityExist -> if (isActivityExist) startActivity(intent) }

fun Context.getColorCompat(@ColorRes colorRes: Int) =
    ContextCompat.getColor(this, colorRes)