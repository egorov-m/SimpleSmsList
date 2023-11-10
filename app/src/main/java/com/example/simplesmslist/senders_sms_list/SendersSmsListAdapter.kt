package com.example.simplesmslist.senders_sms_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.simplesmslist.data.SmsData
import com.example.simplesmslist.databinding.SendersSmsListItemBinding
import java.text.DateFormat.getDateInstance

class SendersSmsListAdapter(
    private val onItemClick: (SmsData) -> Unit
) : RecyclerView.Adapter<SendersSmsListAdapter.SenderSmsListViewHolder>() {

    private val smsList = mutableListOf<SmsData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SenderSmsListViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = SendersSmsListItemBinding.inflate(inflater, parent, false)
        return SenderSmsListViewHolder(binding, onItemClick)
    }

    override fun getItemCount(): Int = smsList.size

    override fun onBindViewHolder(holder: SenderSmsListViewHolder, position: Int) {
        holder.onBind(smsList[position])
    }

    fun submitList(list: List<SmsData>) = with(this.smsList) {
        clear()
        addAll(list)
        notifyDataSetChanged()
    }


    inner class SenderSmsListViewHolder(
        private val binding: SendersSmsListItemBinding,
        private val onItemClick: (SmsData) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: SmsData) = with(binding) {
            smsAvatar.text = item.address.first().toString()
            smsSender.text = item.address
            val msg = item.messages.first()
            smsMessage.text = msg.content
            smsDate.text = getDateInstance().format(msg.date)
            root.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}