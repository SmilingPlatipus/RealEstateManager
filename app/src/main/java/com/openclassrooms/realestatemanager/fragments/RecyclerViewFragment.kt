package com.openclassrooms.realestatemanager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapters.EstateListAdapter
import com.openclassrooms.realestatemanager.viewModels.EstateViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class RecyclerViewFragment : Fragment() {
    private lateinit var recyclerview : RecyclerView
    private lateinit var adapter: EstateListAdapter
    private val estateViewModel by viewModel<EstateViewModel>()

    companion object {
        fun newInstance() : RecyclerViewFragment{
            return RecyclerViewFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.estate_recyclerview,container,false)

        estateViewModel.allEstates.observe(requireActivity()) {
            adapter = EstateListAdapter(view.context)
            adapter.setEstateList(it)
            recyclerview = view.findViewById(R.id.estate_list)
            val layoutManager = LinearLayoutManager(view.context,LinearLayoutManager.HORIZONTAL,false)
            recyclerview.adapter = adapter
            recyclerview.layoutManager = layoutManager}


        return view
    }
}