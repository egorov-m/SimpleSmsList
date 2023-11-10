package com.example.simplesmslist.sms_list

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.simplesmslist.R
import com.example.simplesmslist.databinding.FragmentSmsListBinding
import com.example.simplesmslist.requirePermission

class SmsListFragment : Fragment(R.layout.fragment_sms_list) {
    private val args: SmsListFragmentArgs by navArgs()
    private val binding : FragmentSmsListBinding by viewBinding()
    private val viewModel: SmsListViewModel by viewModels()
    private val adapter = SmsListAdapter()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.smsDataResponse.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.senderTitle.text = args.smsSender

        initializeRecycler()
        showFailureMessage(false)
        requireSmsPermission()
    }

    private fun requireSmsPermission() {
        requirePermission(
            permission = Manifest.permission.READ_SMS,
            successDelegate = {
                viewModel.smsDataRequest(requireContext().contentResolver, args.smsSender)
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
        binding.recyclerViewSendersSmsList.visibility = if (isShown) View.GONE else View.VISIBLE
    }

    private fun initializeRecycler() = with(binding.recyclerViewSendersSmsList) {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = this@SmsListFragment.adapter
        addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = SmsListFragment()
    }
}