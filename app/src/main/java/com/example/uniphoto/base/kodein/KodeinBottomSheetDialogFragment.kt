package com.example.uniphoto.base.kodein

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.uniphoto.R
import com.example.uniphoto.base.extensions.createLogger
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import org.kodein.di.KodeinAware
import org.kodein.di.direct
import org.kodein.di.generic.instance
import kotlin.properties.Delegates
import kotlin.coroutines.CoroutineContext

abstract class KodeinBottomSheetDialogFragment : BottomSheetDialogFragment(), CoroutineScope by MainScope() {

    private var viewModelFactory: KodeinViewModelFactory by Delegates.notNull()

    val kodeinActivity get() = requireActivity() as KodeinActivity

    override val coroutineContext: CoroutineContext = Dispatchers.IO + SupervisorJob()

    lateinit var baseContainer: ViewGroup

    private val L = createLogger()

    @CallSuper
    override fun onAttach(context: Context) {
        super.onAttach(context)

        val kodein = (activity as KodeinAware).kodein
        viewModelFactory = kodein.direct.instance()
    }

    override fun onStart() {
        super.onStart()
    }

    abstract fun inflateContentView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedInstanceState: Bundle?
    ): View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        baseContainer = inflater.inflate(R.layout.bottom_sheet_base, container, false) as ViewGroup
        L.e("BOTTOM SHEET")

        baseContainer.addView(inflateContentView(inflater, baseContainer, savedInstanceState))

        return baseContainer
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