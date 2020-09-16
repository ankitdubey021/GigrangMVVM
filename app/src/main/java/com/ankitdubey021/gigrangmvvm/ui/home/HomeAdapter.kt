package com.ankitdubey021.gigrangmvvm.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ankitdubey021.gigrangmvvm.R
import com.ankitdubey021.gigrangmvvm.commons.utils.MyStringUtils
import com.ankitdubey021.gigrangmvvm.data.Developer
import com.ankitdubey021.gigrangmvvm.databinding.RecyclerviewHomeFeedBinding

class HomeAdapter :
    RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {

    var daos: List<Developer> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val withDataBinding: RecyclerviewHomeFeedBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            MyViewHolder.LAYOUT,
            parent,
            false)
        return MyViewHolder(withDataBinding)

    }

    override fun getItemCount() = daos.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.dao = daos[position]
            it.stringUtils = MyStringUtils.Companion
        }
    }

    class MyViewHolder(val viewDataBinding: RecyclerviewHomeFeedBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.recyclerview_home_feed
        }
    }
}