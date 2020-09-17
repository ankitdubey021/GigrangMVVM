package com.ankitdubey021.gigrangmvvm.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankitdubey021.gigrangmvvm.commons.utils.State
import com.ankitdubey021.gigrangmvvm.data.repository.HomeRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class HomeViewModel @ViewModelInject constructor(
    private val homeRepository: HomeRepository) : ViewModel()
{
    private val _postsLiveData = MutableLiveData<State<ResponseBody>>()
    private val _categoryLiveData = MutableLiveData<State<ResponseBody>>()

    val developersLiveData: LiveData<State<ResponseBody>> get() = _postsLiveData
    val categoryLiveData: LiveData<State<ResponseBody>> get() = _categoryLiveData

    fun fetchCategories(){
        viewModelScope.launch {
            homeRepository.fetchCategories().collect{
                _categoryLiveData.value = it
            }
        }
    }

    fun fetchDevelopers(categoryId : String?, page : Int){
        viewModelScope.launch {
            homeRepository.getDevelopers(categoryId,page,10).collect{
                _postsLiveData.value = it
            }
        }
    }

}