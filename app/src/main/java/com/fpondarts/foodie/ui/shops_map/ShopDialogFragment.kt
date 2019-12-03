package com.fpondarts.foodie.ui.shops_map


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.repository.UserRepository
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_rating.*
import kotlinx.android.synthetic.main.card_shop.*
import kotlinx.android.synthetic.main.dialog_shop.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


/**
 * A simple [Fragment] subclass.
 */
class ShopDialogFragment : DialogFragment(), KodeinAware {


    companion object{
        fun createInstance(id:Long): ShopDialogFragment{

            val f = ShopDialogFragment()

            val args = Bundle()
            args.putLong("shop_id",id)

            f.arguments = args

            return f
        }
    }

    override val kodein by kodein()
    val repository: UserRepository by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.dialog_shop,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val shop_id = arguments!!.getLong("shop_id")

        val listener = targetFragment as ShopNavigator

        repository.getShop(shop_id).observe(this,
            Observer {
                it?.let{
                    Picasso.get().load(it.photoUrl).resize(64,64).into(shopPic)
                    tv_shop_name.text = it.name
                    tv_shop_address.text = it.address

                    rating_title.text = "Rating"
                    rating.rating = it.rating

                    reviews_tv.text = it.reviews.toString()
                }
            }
        )

        button_back.setOnClickListener{
            dismiss()
        }

        button_begin_order.setOnClickListener{
            listener.onNavigateToShop(shop_id)
            dismiss()
        }
        

    }


}
