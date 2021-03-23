package com.example.uniphoto.base.kodein

import android.content.Context
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import org.kodein.di.KodeinAware
import org.kodein.di.direct
import org.kodein.di.generic.instance
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates

abstract class KodeinFragment<T : KodeinViewModel> : Fragment(), FragmentScene, CoroutineScope by MainScope() {
    protected abstract val viewModel: T
    protected inline fun <reified T : KodeinViewModel> viewModel(type: Class<T>): Lazy<T> = lazy { provide(type) }
    inline fun <reified T> Fragment.castChild(): T? = child as? T

    inline fun <reified T> Fragment.castParent(): T? = parent as? T
    val Fragment.parent: Any?
        get() = parentFragment ?: activity

    override val self: Fragment
        get() = this

    val Fragment.child: Any?
        get() = childFragmentManager.fragments.firstOrNull() ?: activity

    private var viewModelFactory: KodeinViewModelFactory by Delegates.notNull()

    val kodeinActivity get() = requireActivity() as KodeinActivity

    override val coroutineContext: CoroutineContext = Dispatchers.IO + SupervisorJob()

    @CallSuper
    override fun onAttach(context: Context) {
        super.onAttach(context)

        val kodein = (activity as KodeinAware).kodein
        viewModelFactory = kodein.direct.instance()
    }

    protected fun <T : KodeinViewModel> provide(viewModelClass: Class<T>): T {
        return ViewModelProvider(this, viewModelFactory).get(viewModelClass)
    }

    protected fun <T : KodeinViewModel> provideGlobal(viewModelClass: Class<T>): T {
        return ViewModelProvider(requireActivity(), viewModelFactory).get(viewModelClass)
    }

    fun <T> LiveData<T>.observe(observer: (item: T?) -> Unit) =
        observe(getSuitableLifecycleOwner(), Observer(observer))

    private fun getSuitableLifecycleOwner() =
        if (view != null) viewLifecycleOwner else this

    fun navigate(id: Int, args: Bundle? = null, navOptions: NavOptions? = null) {
        try {
            findNavController().navigate(id, args, navOptions)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}
