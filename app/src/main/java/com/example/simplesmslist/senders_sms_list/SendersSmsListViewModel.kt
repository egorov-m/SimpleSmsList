package com.example.simplesmslist.senders_sms_list

import android.content.ContentResolver
import android.provider.Telephony
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simplesmslist.data.SmsData
import com.example.simplesmslist.data.SmsMessage
import java.util.Date

class SendersSmsListViewModel: ViewModel() {

    private val _smsDataResponse: MutableLiveData<List<SmsData>> by lazy {
        MutableLiveData<List<SmsData>>()
    }
    val smsDataResponse: LiveData<List<SmsData>>
        get() = _smsDataResponse

    fun smsDataRequest(contentResolver: ContentResolver) {

        val cursor = contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        val result = SenderSmsDataList(mutableListOf())

        if (cursor?.moveToFirst() == true) {
            do {
                val address = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                val body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE))

                result.list.add(address to (date to body))
            } while (cursor.moveToNext())
        }

        cursor?.close()

        _smsDataResponse.postValue(result.getData())
    }
}

@JvmInline
value class SenderSmsDataList(
    val list: MutableList<Pair<String, Pair<String, String>>>
) {
    fun getData(): List<SmsData> =
        list
            .groupBy { it.first }
            .map { it ->
                val messages = it.value.map {
                    SmsMessage(it.first, Date(it.second.first.toLong()), it.second.second )
                }
                SmsData(it.key, messages)
            }
}
