package com.fpondarts.foodie.ui.auth2

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI

import com.fpondarts.foodie.R
import com.fpondarts.foodie.databinding.FragmentSignUpBinding
import com.fpondarts.foodie.ui.HomeActivity
import com.fpondarts.foodie.ui.delivery.DeliveryHomeActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_sign_up.*

import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.util.*

class SignUpFragment : Fragment(),KodeinAware {


    private val RC_SIGN_IN = 123

    override val kodein by kodein()

    companion object {
        fun newInstance() = SignUpFragment()
    }

    private var progressDialog : ProgressDialog? = null

    private val factory:AuthViewModelFactory by instance()

    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSignUpBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_sign_up,container,false)

        viewModel = ViewModelProviders.of(this,factory).get(SignUpViewModel::class.java)
        binding.viewModel = viewModel

        FirebaseAuth.getInstance().signOut()

        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        googleSignUpButton.setOnClickListener(View.OnClickListener {
            handleAuth()
        })

        signUpButton.setOnClickListener(View.OnClickListener {
            progressDialog = ProgressDialog.show(activity,"Creando nuevo usuario","Espere")
            if (viewModel.email.value.isNullOrBlank() || viewModel.password.value.isNullOrBlank() || viewModel.name.value.isNullOrBlank()) {
                Toast.makeText(activity, "Los campos son obligatorios", Toast.LENGTH_LONG).show()
                progressDialog?.dismiss()
                return@OnClickListener
            }
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(viewModel.email.value!!,viewModel.password.value!!)
                .addOnCompleteListener(OnCompleteListener { task->
                    if (task.isSuccessful){
                        val bundle = bundleOf("name" to viewModel.name.value
                            ,"email" to viewModel.email.value
                            ,"password" to viewModel.password.value
                            ,"uid" to FirebaseAuth.getInstance().currentUser!!.uid)
                        findNavController().navigate(R.id.action_signUpFragment_to_registerDataFragment,bundle)
                    } else {
                        Toast.makeText(activity,task.exception?.message,Toast.LENGTH_SHORT).show()
                    }
                    progressDialog?.dismiss()
                })


        })

    }

     fun handleAuth() {
        val providers = Arrays.asList(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                val auth = FirebaseAuth.getInstance()
                val user = auth.currentUser
                if (user!!.metadata!!.creationTimestamp <= System.currentTimeMillis() - 100000){
                    Toast.makeText(activity,"Ya existe un usuario asociado a esa cuenta",Toast.LENGTH_SHORT).show()
                } else {
                    val bundle = bundleOf("name" to user.displayName
                        ,"uid" to user.uid
                        ,"email" to user.email
                        ,"phone" to user.phoneNumber
                        ,"photo" to user.photoUrl.toString())
                    findNavController().navigate(R.id.action_signUpFragment_to_registerDataFragment,bundle)
                }
            } else {
                Toast.makeText(activity, "Hubo un error en el ingreso", Toast.LENGTH_LONG).show()
            }
        }

    }





}
