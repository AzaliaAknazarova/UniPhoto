package com.example.uniphoto.ui.feed

import androidx.lifecycle.MutableLiveData
import com.example.uniphoto.base.kodein.KodeinViewModel
import com.example.uniphoto.model.dataClasses.ContentData
import com.example.uniphoto.model.repository.ContentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import java.lang.Exception

class FeedMainTabViewModel(kodein: Kodein): KodeinViewModel(kodein) {
    private val contentRepository by instance<ContentRepository>()

    var currentPage = 1
    var nextPageExist = false
    val feedItemsList = MutableLiveData<List<ContentData>>()

    fun init() {
        getContentPage(currentPage)
    }

    fun getContentPage(pageNumber: Int) {
        launch {
            try {
                val page = contentRepository.getFeedContentFiles(pageNumber)
                currentPage++
                nextPageExist = !page.nextPage.isNullOrEmpty()
                val newItemsList = (feedItemsList.value ?: emptyList()).toMutableList()
                newItemsList.addAll(page.content)
                withContext(Dispatchers.Main) {
                    feedItemsList.value = newItemsList
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }

    fun reachTheEndOfRecycler() {
        if (nextPageExist)
            getContentPage(currentPage)
    }

}