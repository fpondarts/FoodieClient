package com.fpondarts.foodie.ui.delivery.offers

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.fpondarts.foodie.R
import androidx.core.os.HandlerCompat.postDelayed
import android.os.Handler
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fpondarts.foodie.model.OfferItem
import kotlinx.android.synthetic.main.fragment_offers.*


class OffersFragment : Fragment(), OnOfferItemClickListener {


    override fun onAcceptClick(offer_id:Long,order_id:Long) {

        progress_bar.visibility = View.VISIBLE
        viewModel.repository.acceptOffer(offer_id).observe(this@OffersFragment, Observer {
            it?.let {
                if (it){
                    Toast.makeText(activity,"Oferta aceptada",Toast.LENGTH_SHORT).show()
                    findNavController()
                }
                progress_bar.visibility = View.GONE
            }
        })
    }

    override fun onRejectClick(offer_id:Long,order_id: Long) {
        progress_bar.visibility = View.VISIBLE
        viewModel.repository.rejectOffer(offer_id).observe(this@OffersFragment, Observer {
            it?.let {
                if (it){
                    Toast.makeText(activity,"La oferta fue rechazada",Toast.LENGTH_SHORT).show()
                }
                progress_bar.visibility = View.GONE
            }
        })

    }

    override fun onViewClick(offer_id:Long,order_id: Long) {
        val bundle = bundleOf("offer_id" to offer_id)
        findNavController().navigate(R.id.action_offersFragment_to_singleOfferFragment,bundle)
    }

    companion object {
        fun newInstance() = OffersFragment()
    }

    private lateinit var viewModel: OffersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.fpondarts.foodie.R.layout.fragment_offers, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OffersViewModel::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        offers_recycler_view?.apply{
            layoutManager = LinearLayoutManager(activity)
        }

        useHandler()
    }

    var mHandler:Handler? = null

    fun useHandler() {
        mHandler = Handler()
        mHandler!!.postDelayed(mRunnable, 5000)
    }

    private val mRunnable = object : Runnable {

        override fun run() {

            this@OffersFragment.
            viewModel.getOffers().observe(this@OffersFragment, Observer {
                it?.let{
                    val itemList = ArrayList<OfferItem>()
                    for (offer in it) {
                        itemList.add(OfferItem(offer.created_at_seconds!!,offer.delivery_price,offer.id,offer.orderId))
                    }
                    offers_recycler_view.adapter = OfferAdapter(itemList,this@OffersFragment)
                    offers_recycler_view.adapter!!.notifyDataSetChanged()
                }
            })

            mHandler!!.postDelayed(this, 5000)
        }
    }

}
