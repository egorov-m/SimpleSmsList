package com.example.simplesmslist.senders_sms_list

import android.Manifest.permission.READ_SMS
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.simplesmslist.R
import com.example.simplesmslist.data.SmsData
import com.example.simplesmslist.databinding.FragmentSendersSmsListBinding
import com.example.simplesmslist.requirePermission

class SendersSmsListFragment : Fragment(R.layout.fragment_senders_sms_list) {
    private val binding : FragmentSendersSmsListBinding by viewBinding()
    private val viewModel: SendersSmsListViewModel by viewModels()
    private val adapter = SendersSmsListAdapter(
        ::onSmsClick,
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.smsDataResponse.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        initializeRecycler()
        showFailureMessage(false)
        requireSmsPermission()
    }

    private fun requireSmsPermission() {
        requirePermission(
            permission = READ_SMS,
            successDelegate = {
                viewModel.smsDataRequest(requireContext().contentResolver)
            },
            failureDelegate = {
                showFailureMessage(true)
            }
        )
    }

    private fun showFailureMessage(isShown: Boolean) {
        val visibility = if (isShown) View.VISIBLE else View.GONE
        binding.failedLoadSmsMessageTitle.visibility = visibility
        binding.failedLoadSmsMessageDescription.visibility = visibility
        binding.recyclerViewSendersList.visibility = if (isShown) View.GONE else View.VISIBLE
    }


    private fun onSmsClick(item: SmsData) {
        val direction = SendersSmsListFragmentDirections.actionSendersSmsListFragmentToSmsListFragment(
            smsSender = item.address
        )
        findNavController().navigate(direction)
    }

    private fun initializeRecycler() = with(binding.recyclerViewSendersList) {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = this@SendersSmsListFragment.adapter
        addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = SendersSmsListFragment()
    }
}