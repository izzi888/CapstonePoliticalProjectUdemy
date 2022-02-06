package com.example.android.politicalpreparedness.election

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.base.BaseFragment
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.util.Const.FIRST_TIME_FLOW
import com.example.android.politicalpreparedness.util.getSharedPref
import org.koin.androidx.viewmodel.ext.android.viewModel

class ElectionsFragment : BaseFragment() {

    override val _viewModel: ElectionsViewModel by viewModel()

    private lateinit var binding: FragmentElectionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_election, container, false)

        //TODO: Add ViewModel values and create ViewModel
        binding.viewModel = _viewModel
        binding.lifecycleOwner = this

        setupRecyclerView(binding)

        checkFirstTimeUserFlow()

        setupViewListeners(binding)


        //TODO: Add binding values

        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters

        //TODO: Populate recycler adapters

        return binding.root
    }

    private fun setupViewListeners(binding: FragmentElectionBinding) {
        binding.srlUpcoming.setOnRefreshListener {
            _viewModel.updateElectionsData()
        }
    }

    private fun checkFirstTimeUserFlow() {
        activity?.getSharedPref()?.let { pref ->
            if (pref.getBoolean(FIRST_TIME_FLOW, true)) {
                _viewModel.forceUpdateElectionsData()
                pref.edit {
                    putBoolean(FIRST_TIME_FLOW, false)
                    commit()
                    apply()
                }
            } else {
                _viewModel.updateElectionsData()
            }
        } ?: _viewModel.updateElectionsData()
    }

    private fun setupRecyclerView(binding: FragmentElectionBinding) {
        binding.rvUpcoming.adapter =
            ElectionListAdapter(clickListener = ElectionListener {
                _viewModel.navigateToVoterInfo(it)
            })

        binding.rvSaved.adapter =
            ElectionListAdapter(clickListener = ElectionListener {
                _viewModel.navigateToVoterInfo(it)
            })
    }
}
