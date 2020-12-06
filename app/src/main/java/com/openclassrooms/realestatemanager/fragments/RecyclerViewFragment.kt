package com.openclassrooms.realestatemanager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapters.EstateListAdapter
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.viewModels.EstateViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecyclerViewFragment(val valuesToDisplay : MutableList<Estate>?) : Fragment() {
    lateinit var recyclerview : RecyclerView
    lateinit var adapter: EstateListAdapter
    private val estateViewModel by viewModel<EstateViewModel>()
    private val values : MutableList<Estate>? = valuesToDisplay

    companion object {
        fun newInstance() : RecyclerViewFragment{
            return RecyclerViewFragment(null)
        }
        fun newSearchInstance(value : MutableList<Estate>?): RecyclerViewFragment {
            return RecyclerViewFragment(value)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.estate_recyclerview,container,false)
        if (values == null) {
            adapter = EstateListAdapter(view.context)

            estateViewModel.allEstates.observe(requireActivity()) {
                recyclerview = view.findViewById(R.id.estate_list)
                val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
                adapter?.setEstateList(it)
                recyclerview.adapter = adapter
                recyclerview.layoutManager = layoutManager
                recyclerview.scrollBy(0, 0)
            }
            return view
        }
        else{
            adapter = EstateListAdapter(view.context)
            recyclerview = view.findViewById(R.id.estate_list)
            val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
            adapter?.setEstateList(values)
            recyclerview.adapter = adapter
            recyclerview.layoutManager = layoutManager
            recyclerview.scrollBy(0, 0)
            return view
        }
    }
}