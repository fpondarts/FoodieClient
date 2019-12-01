package com.fpondarts.foodie.ui.home.favours_map


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fpondarts.foodie.R

/**
 * A simple [Fragment] subclass.
 */
class FavourUsersMapFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.delivery_map_fragment, container, false)
    }


}
