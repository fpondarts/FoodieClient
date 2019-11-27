package com.fpondarts.foodie.ui.favours.favourOffers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fpondarts.foodie.model.OfferItem
import com.fpondarts.foodie.ui.delivery.offers.OfferItemViewHolder
import com.fpondarts.foodie.ui.delivery.offers.OnOfferItemClickListener

class FavourOfferAdapter (val map: HashMap<Long, OfferItem>, val listener: OnOfferItemClickListener):
    RecyclerView.Adapter<OfferItemViewHolder>()
{

    val list = map.values

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return OfferItemViewHolder(inflater,parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: OfferItemViewHolder, position: Int) {
        val item: OfferItem = list.elementAt(position)
        holder.bind(item, listener)
    }
}