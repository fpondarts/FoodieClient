package com.fpondarts.foodie.ui.favours.favourOffers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fpondarts.foodie.model.OfferItem
import com.fpondarts.foodie.ui.delivery.offers.OfferItemViewHolder
import com.fpondarts.foodie.ui.delivery.offers.OnOfferItemClickListener

class FavourOfferAdapter (val map: HashMap<Long, OfferItem>, val listener: OnOfferItemClickListener):
    RecyclerView.Adapter<FavourOfferViewHolder>()
{

    val list = map.values

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavourOfferViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return FavourOfferViewHolder(inflater,parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: FavourOfferViewHolder, position: Int) {
        val item: OfferItem = list.elementAt(position)
        holder.bind(item, listener)
    }
}