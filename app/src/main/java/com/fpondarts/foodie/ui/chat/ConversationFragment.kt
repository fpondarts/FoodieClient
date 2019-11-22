package com.fpondarts.foodie.ui.chat

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.fpondarts.foodie.R
import com.fpondarts.foodie.ui.FoodieViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class ConversationFragment : Fragment(), KodeinAware {

    companion object {
        fun newInstance() = ConversationFragment()
    }

    override val kodein by kodein()

    val factory: FoodieViewModelFactory by instance()

    private lateinit var viewModel: ConversationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        return inflater.inflate(R.layout.fragment_conversation, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ConversationViewModel::class.java)

        val orderId = arguments!!.getLong("orderId")

    }

}
