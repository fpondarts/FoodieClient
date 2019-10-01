package com.fpondarts.foodie.ui.home.ui.home.current_order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fpondarts.foodie.data.repository.Repository
import com.fpondarts.foodie.model.OrderItem
import com.fpondarts.foodie.util.Coroutines

class CurrentOrderViewModel(val repository: Repository) : ViewModel() {

    val liveOrder = MutableLiveData<ArrayList<Pair<String?,OrderItem>>>().apply {
        value = ArrayList<Pair<String?,OrderItem>>()
    }

    init{
        Coroutines.main{
            val order = repository.currentOrder
            val items = ArrayList<Pair<String?,OrderItem>>()
            order?.items?.values?.forEach {
                val name = repository.getItemName(it.id)
                items.add(Pair<String?,OrderItem>(name,it))
            }
            liveOrder.value = items
        }

    }

}
