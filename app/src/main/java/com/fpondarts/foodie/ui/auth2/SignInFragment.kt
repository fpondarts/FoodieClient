package com.fpondarts.foodie.ui.auth2

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

import com.fpondarts.foodie.R
import com.fpondarts.foodie.databinding.FragmentSignInBinding
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class SignInFragment : Fragment(), KodeinAware {

    companion object {
        fun newInstance() = SignInFragment()
    }

    override val kodein by kodein()

    private lateinit var viewModel: SignInViewModel
    private val factory : AuthViewModelFactory by instance()
    lateinit var navController: NavController



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : FragmentSignInBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_sign_in,container,false)

        viewModel = ViewModelProviders.of(this,factory).get(SignInViewModel::class.java)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SignInViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        signUpButton.setOnClickListener(View.OnClickListener {
            navController.navigate(R.id.action_signInFragment_to_signUpFragment)
        })

    }

}
