package com.example.uniphoto.base.extensions

import android.os.Parcelable
import androidx.fragment.app.Fragment
import com.sredasolutions.gt_android.base.extensions.pack

fun <T : Fragment> T.withArguments(args: Parcelable) = apply { arguments = args.pack() }