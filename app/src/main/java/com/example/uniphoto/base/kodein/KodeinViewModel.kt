package com.example.uniphoto.base.kodein

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.uniphoto.Application
import com.example.uniphoto.base.extensions.createLogger
import kotlinx.coroutines.*
import com.example.uniphoto.base.lifecycle.SingleLiveEvent
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import kotlin.coroutines.CoroutineContext

abstract class KodeinViewModel(override val kodein: Kodein) : ViewModel(), CoroutineScope,
    KodeinAware {

    private val L = createLogger()

    protected val context: Context = Application.applicationContext()
    override val coroutineContext: CoroutineContext = Dispatchers.IO + SupervisorJob()

    override fun onCleared() = coroutineContext.cancel()

    val showMessageRequest = SingleLiveEvent<String>()
    val showConfirmDialogRequest = SingleLiveEvent<ConfirmDialogArgs>()

    fun <T> MutableLiveData<T>.immutable() = this as LiveData<T>
//
//    fun withToken(tokenProvider: TokenProvider, callback: (String) -> Unit) =
//        launch {
//            tokenProvider.getToken { token ->
//                token?.let {
//                    callback.invoke(it)
//                }
//            }
//        }

    protected fun showMessage(message: String) {
        if (message.isNotEmpty())
            showMessageRequest.postValue(message)
    }

    protected fun showConfirmDialog(args: ConfirmDialogArgs) {
        showConfirmDialogRequest.postValue(args)
    }
}

data class ConfirmDialogArgs(
    val tag: String,
    val title: String?,
    val message: String?,
    val positiveButtonText: String,
    val negativeButtonText: String?,
    val cancelable: Boolean
)