package com.example.uniphoto.base.kodein

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

abstract class KodeinActivity : AppCompatActivity(), CoroutineScope by MainScope(), KodeinAware {

    final override val kodein by closestKodein()

    private val viewModelFactory by instance<KodeinViewModelFactory>()

    protected fun <T : KodeinViewModel> provide(viewModelClass: Class<T>): T {
        return ViewModelProvider(this, viewModelFactory).get(viewModelClass)
    }

    fun <T> LiveData<T>.observe(
        lifecycleOwner: LifecycleOwner,
        observer: (item: T?) -> Unit
    ) = observe(lifecycleOwner, Observer(observer))

    fun <T> LiveData<T>.observeNonNull(
        lifecycleOwner: LifecycleOwner,
        observer: (item: T) -> Unit
    ) = observe(lifecycleOwner, Observer { it?.let(observer) })

    fun <T> LiveData<T>.observe(observer: (item: T?) -> Unit) =
        observe(this@KodeinActivity, Observer(observer))

    fun <T> LiveData<T>.observeNonNull(observer: (item: T) -> Unit) =
        observeNonNull(this@KodeinActivity, observer)
}