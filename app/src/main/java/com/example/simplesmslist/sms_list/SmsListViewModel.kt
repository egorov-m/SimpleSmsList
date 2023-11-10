package com.example.simplesmslist.sms_list

import android.content.ContentResolver
import android.provider.Telephony
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simplesmslist.data.SmsMessage
import java.util.Date

class SmsListViewModel : ViewModel() {
    private val _smsDataResponse: MutableLiveData<List<SmsMessage>> by lazy {
        MutableLiveData<List<SmsMessage>>()
    }
    val smsDataResponse: LiveData<List<SmsMessage>>
        get() = _smsDataResponse

    fun smsDataRequest(contentResolver: ContentResolver, sender: String) {

        val cursor = contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        val result = SmsDataList(mutableListOf())

        if (cursor?.moveToFirst() == true) {
            do {
                val address = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                val body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE))

                result.list.add(address to (date to body))
            } while (cursor.moveToNext())
        }

        cursor?.close()

        _smsDataResponse.postValue(result.getData(sender))
    }
}

@JvmInline
value class SmsDataList(
    val list: MutableList<Pair<String, Pair<String, String>>>
) {
    fun getData(sender: String): List<SmsMessage> =
        list.filter { it.first == sender }.map {
            SmsMessage(sender, Date(it.second.first.toLong()), it.second.second)
        }
}