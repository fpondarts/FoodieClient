package com.fpondarts.foodie.ui.favours.favourOffers


import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.fpondarts.foodie.R
import com.fpondarts.foodie.model.OfferItem
import com.fpondarts.foodie.data.repository.Repository
import com.fpondarts.foodie.ui.delivery.offers.OfferAdapter
import com.fpondarts.foodie.ui.delivery.offers.OnOfferItemClickListener
import kotlinx.android.synthetic.main.fragment_favour_offers.*
import kotlinx.android.synthetic.main.fragment_offers.*
import kotlinx.android.synthetic.main.fragment_offers.offers_recycler_view
import kotlinx.android.synthetic.main.fragment_offers.progress_bar
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/**
 * A simple [Fragment] subclass.
 */
class FavourOffersFragment : Fragment(), KodeinAware, OnOfferItemClickListener, CompoundButton.OnCheckedChangeListener {


    override fun onAcceptClick(offer_id: Long, order_id: Long) {
        progress_bar.visibility = View.VISIBLE

        repository.acceptOffer(offer_id).observe(this@FavourOffersFragment, Observer {
            it?.let {
                if (it){
                    val bundle = Bundle().apply {
                        this.putLong("offer_id",offer_id)
                        this.putLong("order_id",order_id)
                    }
                    Toast.makeText(activity,"Oferta aceptada", Toast.LENGTH_SHORT).show()
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
        repository.rejectOffer(offer_id).observe(this@FavourOffersFragment, Observer {
            it?.let {
                if (it){
                    val adapter = offers_recycler_view.adapter as FavourOfferAdapter
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
        findNavController().navigate(R.id.action_favourOffersFragment_to_singleFavourOfferFragment,bundle)
    }

    override val kodein by kodein()

    private val repository: Repository by instance()

    private var make_favours: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        repository.isWorking.observe(this, Observer{
            if (it){
                val bundle = bundleOf("order_id" to repository.current_order)
                val navOptions = NavOptions.Builder().setPopUpTo(R.id.offersFragment,true).build()
                findNavController().navigate(R.id.action_favourOffersFragment_to_workingFavourFragment,bundle,navOptions)
            }

        })

        return inflater.inflate(R.layout.fragment_favour_offers, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStop() {
        super.onStop()
        Log.d("Offers","Stoppped")
        mHandler = null
        timeUpdateHandler = null
    }

    override fun onResume() {
        super.onResume()
        Log.d("Offers","Resumed")
        if (make_favours)
            useHandler()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        offers_recycler_view?.apply{
            layoutManager = LinearLayoutManager(activity)
            adapter = FavourOfferAdapter(HashMap<Long,OfferItem>(),this@FavourOffersFragment)
        }

        repository.make_favours.observe(this, Observer {
            if (it){
                make_favours = it
                useHandler()
            } else {
                mHandler = null
                timeUpdateHandler = null
                offers_recycler_view?.apply{
                    layoutManager = LinearLayoutManager(activity)
                    adapter = FavourOfferAdapter(HashMap<Long,OfferItem>(),this@FavourOffersFragment)
                }
            }
            take_favours_switch.isChecked = it
        })

        take_favours_switch.setOnClickListener(
            View.OnClickListener {
                val switch = it as Switch
                if (switch.isChecked){
                    repository.setTakingFavours(true).observe(this, Observer {
                        it?.let {
                            if (!it){
                                switch.isChecked = false
                            }
                        }
                    })
                } else {
                    repository.setTakingFavours(false).observe(this, Observer {
                        it?.let {
                            if (!it){
                                switch.isChecked = true
                            }
                        }
                    })
                }
            }
        )

        take_favours_switch.setOnCheckedChangeListener(this)
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        repository.setTakingFavours(p1)
    }

    var mHandler: Handler? = null

    var timeUpdateHandler: Handler? = null

    fun useHandler() {
        mHandler = Handler()
        mHandler!!.postDelayed(mRunnable, 5000)
        timeUpdateHandler = Handler()
        timeUpdateHandler!!.postDelayed(mUpdateTime,1000)
    }



    private val mRunnable = object : Runnable {

        override fun run() {
            offers_recycler_view?.let {

                var ld = this@FavourOffersFragment.repository.getCurrentOffers()
                ld.observe(this@FavourOffersFragment, Observer {
                    it?.let {
                        val itemMap = HashMap<Long, OfferItem>()
                        val adapter = offers_recycler_view.adapter as FavourOfferAdapter
                        for (offer in it) {
                            val item = OfferItem(
                                offer.created_at_seconds!!,
                                offer.points.toFloat(),
                                offer.user_id,
                                offer.order_id
                            )
                            if (item.remainingSeconds < 0) {
                                adapter.map.remove(offer.id)
                            } else {
                                adapter.map.getOrPut(offer.id, { item })
                            }
                        }
                        offers_recycler_view.adapter!!.notifyDataSetChanged()
                        ld.removeObservers(this@FavourOffersFragment)
                    }
                })
            }
            mHandler?.postDelayed(this, 5000)
        }
    }

    private val mUpdateTime = object : Runnable {

        override fun run(){
            offers_recycler_view?.let{
                val adapter = offers_recycler_view?.adapter as FavourOfferAdapter
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
