package com.fpondarts.foodie.ui.delivery.delivered

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.repository.DeliveryRepository
import com.fpondarts.foodie.ui.my_orders.OnMyOrderClickListener
import kotlinx.android.synthetic.main.fragment_delivered_orders.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DeliveredOrdersFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 */
class DeliveredOrdersFragment : Fragment(), KodeinAware, OnMyOrderClickListener {


    override fun onActiveOrderClick(
        active: Boolean,
        orderId: Long,
        shopId: Long?,
        deliveryId: Long?
    ) {
        
    }

    override val kodein by kodein()

    val repository : DeliveryRepository by instance()

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_delivered_orders,container,false)
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        delivered_recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
        }

        repository.getDeliveredByMe().observe(this, Observer {
            it?.let{
                if (! it.isNullOrEmpty() ){
                    delivered_recycler_view.adapter = DeliveredOrdersAdapter(it,this)
                }
            }
        })

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

}
