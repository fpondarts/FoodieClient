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
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fpondarts.foodie.data.repository.DeliveryRepository
import com.fpondarts.foodie.model.OfferItem
import kotlinx.android.synthetic.main.fragment_offers.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import org.kodein.di.android.x.kodein


class OffersFragment : Fragment(), OnOfferItemClickListener, KodeinAware {


    override val kodein by kodein()

    val repository: DeliveryRepository by instance()

    override fun onAcceptClick(offer_id:Long,order_id:Long) {

        progress_bar.visibility = View.VISIBLE

        repository.acceptOffer(offer_id).observe(this@OffersFragment, Observer {
            it?.let {
                if (it){
                    val bundle = Bundle().apply {
                        this.putLong("offer_id",offer_id)
                        this.putLong("order_id",order_id)
                    }
                    Toast.makeText(activity,"Oferta aceptada",Toast.LENGTH_SHORT).show()
//                    findNavController().navigate(R.id.action_offersFragment_to_workingFragment
//                        ,bundle
//                        ,NavOptions.Builder().setPopUpTo(R.id.offersFragment,true).build())
                }
                progress_bar.visibility = View.GONE
            }
        })
    }


    override fun onRejectClick(offer_id:Long,order_id: Long) {
        progress_bar.visibility = View.VISIBLE
        repository.rejectOffer(offer_id).observe(this@OffersFragment, Observer {
            it?.let {
                if (it){
                    val adapter = offers_recycler_view.adapter as OfferAdapter
                    adapter.map.remove(offer_id)
                    adapter.notifyDataSetChanged()
                    Toast.makeText(activity,"La oferta fue rechazada",Toast.LENGTH_SHORT).show()
                }
                progress_bar.visibility = View.GONE
            }
        })

    }

    override fun onViewClick(offer_id:Long,order_id: Long) {
        val bundle = Bundle().apply {
            this.putLong("offer_id",offer_id)
            this.putLong("order_id",order_id)
        }
        findNavController().navigate(R.id.action_offersFragment_to_singleOfferFragment,bundle)
    }

    companion object {
        fun newInstance() = OffersFragment()
    }

    override fun onStop() {
        super.onStop()
        Log.d("Offers","Stopppedd")
        mHandler = null
        timeUpdateHandler = null
    }

    override fun onResume() {
        super.onResume()
        Log.d("Offers","Resumed")
        useHandler()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(com.fpondarts.foodie.R.layout.fragment_offers, container, false)

        repository.isWorking.observe(this,Observer{
            if (it){
                val bundle = bundleOf("order_id" to repository.current_order)
                val navOptions = NavOptions.Builder().setPopUpTo(R.id.offersFragment,true).build()
                findNavController().navigate(R.id.action_offersFragment_to_workingFragment,bundle,navOptions)
            }

        })

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        offers_recycler_view?.apply{
            layoutManager = LinearLayoutManager(activity)
            adapter = OfferAdapter(HashMap<Long,OfferItem>(),this@OffersFragment)
        }
    }

    var mHandler:Handler? = null

    var timeUpdateHandler:Handler? = null

    fun useHandler() {
        mHandler = Handler()
        mHandler!!.postDelayed(mRunnable, 5000)
        timeUpdateHandler = Handler()
        timeUpdateHandler!!.postDelayed(mUpdateTime,1000)
    }



    private val mRunnable = object : Runnable {

        override fun run() {
            offers_recycler_view?.let {

                var ld = this@OffersFragment.repository.getCurrentOffers()
                ld.observe(this@OffersFragment, Observer {
                    it?.let {
                        val itemMap = HashMap<Long, OfferItem>()
                        val adapter = offers_recycler_view.adapter as OfferAdapter
                        for (offer in it) {
                            val item = OfferItem(
                                offer.created_at_seconds!!,
                                offer.delivery_price,
                                offer.id,
                                offer.order_id
                            )
                            if (item.remainingSeconds < 0) {
                                adapter.map.remove(offer.id)
                            } else {
                                adapter.map.getOrPut(offer.id, { item })
                            }
                        }
                        offers_recycler_view.adapter!!.notifyDataSetChanged()
                        ld.removeObservers(this@OffersFragment)
                    }
                })
            }
            mHandler?.postDelayed(this, 5000)
        }
    }

    private val mUpdateTime = object : Runnable {

        override fun run(){
            this@OffersFragment

            offers_recycler_view?.let{
                val adapter = offers_recycler_view?.adapter as OfferAdapter
                for (item in adapter.map){
                    item.value.remainingSeconds -= 1
                    if (item.value.remainingSeconds < 0 ){
                        adapter.map.remove(item.key)
                    }
                }
                adapter.notifyDataSetChanged()

                timeUpdateHandler?.postDelayed(this,1000)
            }

        }

    }

}
