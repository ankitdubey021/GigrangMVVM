package com.ankitdubey021.gigrangmvvm.ui.home
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.ankitdubey021.gigrangmvvm.R
import com.ankitdubey021.gigrangmvvm.commons.utils.ProgressBarUtils
import com.ankitdubey021.gigrangmvvm.commons.utils.State
import com.ankitdubey021.gigrangmvvm.commons.utils.getColorRes
import com.ankitdubey021.gigrangmvvm.commons.utils.toast
import com.ankitdubey021.gigrangmvvm.data.Category
import com.ankitdubey021.gigrangmvvm.databinding.FragmentHomeBinding
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.ResponseBody
import org.json.JSONObject
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment() {

    lateinit var binding : FragmentHomeBinding
    private val homeViewModel by viewModels<HomeViewModel>()
    private var categoryList = mutableListOf<Category>()
    private var selectedChip : Chip? = null
    private var selectedCategoryId : String? = null
    private var page = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater,container,false)
        fetchCategories()
        observerDevelopers()
        return binding.root
    }

    private fun fetchCategories() {
        homeViewModel.categoryLiveData.observe(viewLifecycleOwner, Observer {state->
            when(state){
                is State.Loading-> ProgressBarUtils.showProgressDialog(requireContext())

                is State.Success-> {
                    ProgressBarUtils.removeProgressDialog()
                    renderCategories(state.data.string())
                }

                is State.Error -> renderErrorState(state)
            }
        })
    }

    private fun fetchDevelopers(){
        homeViewModel.fetchDevelopers(selectedCategoryId,page)
    }

    private fun observerDevelopers() {
        homeViewModel.developersLiveData.observe(viewLifecycleOwner, Observer {state->
            when(state){
                is State.Loading-> ProgressBarUtils.showProgressDialog(requireContext())

                is State.Success-> {
                    ProgressBarUtils.removeProgressDialog()
                    Timber.e(state.data.string())
                }

                is State.Error -> renderErrorState(state)
            }
        })
    }

    private fun renderErrorState(state: State.Error<ResponseBody>) {
        ProgressBarUtils.removeProgressDialog()
        requireContext().toast(state.message.string())
    }


    private fun renderCategories(data: String) {

        categoryList.add(Category(0,"All",null))

        val jsonObj = JSONObject(data)
        val categoryArray = jsonObj.getJSONArray("data")
        for(x in 0 until categoryArray.length())
            categoryList.add(Gson().fromJson(categoryArray.getJSONObject(x).toString(),Category::class.java))

        categoryList.forEach { addCategoryChip(it) }
    }


    private fun addCategoryChip(category: Category) {
        val chip = Chip(requireContext())
        chip.apply {
            text=category.title
            id = category.id
        }
        setChipClickListener(chip)

        if(category.title=="All") checkChip(chip)

        binding.cg.addView(chip)
    }

    private fun setChipClickListener(chip: Chip) {
        chip.setOnClickListener {
            if(selectedChip==chip) {
                if(selectedChip!=null && selectedChip!!.tag.toString()!="All") {
                    uncheckSelectedChip()
                    checkChip(binding.cg[0] as Chip)
                }
            }
            else {
                uncheckSelectedChip()
                checkChip(chip)
            }
        }
    }

    private fun checkChip(chip: Chip) {
        selectedChip = chip
        chip.tag = chip.text
        chip.setChipBackgroundColorResource(R.color.colorAccent)
        chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.whiteLight))

        selectedCategoryId = if(chip.text=="All") null else chip.id.toString()

        fetchDevelopers()
    }

    private fun uncheckSelectedChip(){
        selectedChip?.let {
            it.tag = ""
            it.setChipBackgroundColorResource(R.color.colorGrey)
            it.setTextColor(requireContext().getColorRes(R.color.colorBlack))
            selectedChip=null
        }
    }
}