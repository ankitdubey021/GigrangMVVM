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
import com.ankitdubey021.gigrangmvvm.utils.*
import com.ankitdubey021.gigrangmvvm.data.Category
import com.ankitdubey021.gigrangmvvm.data.Developer
import com.ankitdubey021.gigrangmvvm.data.DeveloperList
import com.ankitdubey021.gigrangmvvm.databinding.FragmentHomeBinding
import com.ankitdubey021.gigrangmvvm.extensions.getColorRes
import com.ankitdubey021.gigrangmvvm.extensions.toast
import com.ankitdubey021.gigrangmvvm.extensions.xOnScrollListener
import com.ankitdubey021.gigrangmvvm.networking.State
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.ResponseBody
import org.json.JSONObject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private val homeViewModel by viewModels<HomeViewModel>()
    private var categoryList = mutableListOf<Category>()
    private var developerList = mutableListOf<Developer>()
    private var selectedChip : Chip? = null
    private var selectedCategoryId : String? = null
    private var page = 0
    private var shouldLoadMore = true
    private val homeAdapter : HomeAdapter  by lazy {
        HomeAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater,container,false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        page = 0
        developerList.clear()
        categoryList.clear()
        fetchCategories()
        observeCategories()
        observeDevelopers()

        binding.rvCandidates.apply {
            adapter = homeAdapter
            xOnScrollListener {
                if(shouldLoadMore) {
                    page++
                    fetchDevelopers()
                }
            }
        }
        homeAdapter.daos = developerList
    }

    private fun fetchCategories(){
        homeViewModel.fetchCategories()
    }

    private fun observeCategories() {

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

    private fun observeDevelopers() {
        homeViewModel.developersLiveData.observe(viewLifecycleOwner, Observer {state->
            when(state){
                is State.Loading-> ProgressBarUtils.showProgressDialog(requireContext())

                is State.Success-> {
                    ProgressBarUtils.removeProgressDialog()
                    renderDevelopersData(state.data.string())
                }

                is State.Error -> renderErrorState(state)
            }
        })
    }

    private fun renderDevelopersData(res : String) {
        if(res.isEmpty()) return

        val jsonRes = JSONObject(res)
        val list = Gson().fromJson(jsonRes.getJSONObject("data").toString(), DeveloperList::class.java)
        developerList.addAll(list.data)
        homeAdapter.notifyDataSetChanged()

        shouldLoadMore =
            jsonRes.getJSONObject("data").getInt("total") != developerList.size

    }

    private fun renderErrorState(state: State.Error<ResponseBody>) {
        ProgressBarUtils.removeProgressDialog()
        requireContext().toast(state.message.string())

    }


    private fun renderCategories(data: String) {
        if(data.isEmpty())return

            categoryList.add(Category(0, "All", null))

            val jsonObj = JSONObject(data)
            val categoryArray = jsonObj.getJSONArray("data")
            for (x in 0 until categoryArray.length())
                categoryList.add(
                    Gson().fromJson(
                        categoryArray.getJSONObject(x).toString(),
                        Category::class.java
                    )
                )

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

        developerList.clear()
        page=0
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