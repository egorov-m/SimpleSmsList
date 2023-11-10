package com.example.simplesmslist.sms_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.simplesmslist.data.SmsMessage
import com.example.simplesmslist.databinding.SmsListItemBinding
import java.text.DateFormat.getDateTimeInstance

class SmsListAdapter :
    RecyclerView.Adapter<SmsListAdapter.SmsListViewHolder>() {

    private val messagesList = mutableListOf<SmsMessage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsListViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = SmsListItemBinding.inflate(inflater, parent, false)
        return SmsListViewHolder(binding)
    }

    override fun getItemCount(): Int = messagesList.size

    override fun onBindViewHolder(holder: SmsListViewHolder, position: Int) {
        holder.onBind(messagesList[position])
    }

    fun submitList(list: List<SmsMessage>) = with(this.messagesList) {
        clear()
        addAll(list)
        notifyDataSetChanged()
    }

    inner class SmsListViewHolder(
        private val binding: SmsListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: SmsMessage) = with(binding) {
            smsAvatar.text = item.address.first().toString()
            smsMessage.text = item.content
            smsDate.text = getDateTimeInstance().format(item.date)
        }
    }
}