package com.example.uniphoto.base.utils

import android.annotation.SuppressLint
import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import com.example.uniphoto.Application
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object Utils {
    private const val ANDROID_KEY_STORE = "AndroidKeyStore"
    private const val ALIAS_VALUE = "alias value for key store"
    private const val TRANSFORMATION_ALGORITHM = "AES/GCM/NoPadding"
    private const val IV_SEPARATOR = "]"
    private val appContext get() = Application.applicationContext()

    @SuppressLint("NewApi")
    private fun getSecretKey(): SecretKey? {

        val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
        keyStore.load(null)

        if (keyStore.containsAlias(ALIAS_VALUE)) {
            val secretKeyEntry = keyStore.getEntry(ALIAS_VALUE, null) as KeyStore.SecretKeyEntry
            return secretKeyEntry.secretKey
        } else {
            try {
                val generator: KeyGenerator =
                    KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)
                val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                        ALIAS_VALUE,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                    )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build()

                generator.init(keyGenParameterSpec)
                return generator.generateKey()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return null
    }

    fun encodeData(inputString: String): String {
        if (inputString.isEmpty()) {
            return inputString
        }

        var data: String = inputString

        if (data.length % 4 != 0) {
            var zeroString = ""
            for(i in 1..4 - data.length % 4)
                zeroString += "0"
            data = zeroString + inputString
        }

        var encodedBytes = ""
        try {
            val cipher: Cipher = Cipher.getInstance(TRANSFORMATION_ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
            encodedBytes = Base64.encodeToString(cipher.iv, Base64.DEFAULT) + IV_SEPARATOR
            val dataBytes = cipher.doFinal(Base64.decode(data, Base64.DEFAULT))
            encodedBytes += Base64.encodeToString(dataBytes, Base64.DEFAULT)
        } catch (e: java.lang.Exception) {

        }

        return encodedBytes
    }

    fun decodeData(encodedString: String): String {
        var decodedString = ""
        val split = encodedString.split(IV_SEPARATOR.toRegex())

        if (split.getOrNull(1).isNullOrEmpty()) {
            return "0"
        }

        try {
            val cipher = Cipher.getInstance(TRANSFORMATION_ALGORITHM)
            val spec = GCMParameterSpec(128, Base64.decode(split[0], Base64.DEFAULT))

            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec)
            val decodedBytes = cipher.doFinal(Base64.decode(split[1], Base64.DEFAULT))
            decodedString = Base64.encodeToString(decodedBytes, Base64.DEFAULT)
        } catch (e: java.lang.Exception) {
        }

        while (decodedString.isNotEmpty() && decodedString[0] == '0') {
            decodedString = decodedString.replaceFirst("0","")
        }

        return decodedString.trim()
    }

    @JvmStatic
    fun getTokenFromSharedPref(): String {
        var token: String

        val pref = appContext.getSharedPreferences(
            Constants.APP_PREFERENCES,
            Context.MODE_PRIVATE
        )

        token = pref.getString(Constants.APP_PREFERENCES_TOKEN, "").toString()

        return if (token.isEmpty()) {
            ""
        } else {
            decodeData(token)
        }
    }

    @JvmStatic
    fun putTokenInSharedPref(token: String) {
        val pref = appContext.getSharedPreferences(
            Constants.APP_PREFERENCES,
            Context.MODE_PRIVATE
        )

        val editor = pref.edit()
        editor.putString(Constants.APP_PREFERENCES_TOKEN, encodeData(token))
        editor.apply()
    }

    @JvmStatic
    fun removeTokenFromSharedPref() {
        val pref = appContext.getSharedPreferences(
            Constants.APP_PREFERENCES,
            Context.MODE_PRIVATE
        )

        val editor = pref.edit()
        editor.remove(Constants.APP_PREFERENCES_TOKEN)
        editor.apply()
    }

    @JvmStatic
    fun clearSharedPreferences() {
        val pref =
            appContext.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE)

        val editor = pref.edit()
        editor.clear()
        editor.apply()
    }

}

