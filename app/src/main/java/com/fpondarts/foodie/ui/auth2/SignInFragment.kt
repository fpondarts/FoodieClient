package com.fpondarts.foodie.ui.auth2

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.fpondarts.foodie.R

import com.fpondarts.foodie.databinding.FragmentSignInBinding
import com.fpondarts.foodie.ui.HomeActivity
import com.fpondarts.foodie.ui.delivery.DeliveryHomeActivity
import com.fpondarts.foodie.util.exception.IncompleteDataException
import com.google.firebase.auth.FirebaseAuth
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.util.*
import com.google.firebase.auth.GetTokenResult
import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.android.synthetic.main.fragment_sign_in.*


class SignInFragment : Fragment(), KodeinAware, GoogleAuthHandler {

    private val RC_SIGN_IN = 123

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

        val binding : FragmentSignInBinding = DataBindingUtil.inflate(inflater,
            com.fpondarts.foodie.R.layout.fragment_sign_in,container,false)

        viewModel = ViewModelProviders.of(this,factory).get(SignInViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SignInViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        toSignUpButton.setOnClickListener(View.OnClickListener {
            navController.navigate(com.fpondarts.foodie.R.id.action_signInFragment_to_signUpFragment)
        })

        signInButton.setOnClickListener(View.OnClickListener {
                progress_bar.visibility = View.VISIBLE
                if (viewModel.userName.value.isNullOrBlank() || viewModel.password.value.isNullOrBlank()){
                    Toast.makeText(activity,"Los campos son obligatorios",Toast.LENGTH_SHORT).show()
                    progress_bar.visibility = View.GONE
                    return@OnClickListener
                }else {
                    try {
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(
                            viewModel.userName.value!!,
                            viewModel.password.value!!
                        )
                            .addOnCompleteListener(OnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    viewModel.signIn().observe(this, Observer {
                                        it?.let {
                                            if (it.role == "usuario") {
                                                val intent =
                                                    Intent(activity, HomeActivity::class.java)
                                                intent.putExtra("token", it.token)
                                                intent.putExtra("id",it.user_id)
                                                startActivity(intent)
                                                activity!!.finish()
                                            } else if (it.role == "delivery") {
                                                val intent = Intent(
                                                    activity,
                                                    DeliveryHomeActivity::class.java
                                                )
                                                intent.putExtra("token", it.token)
                                                startActivity(intent)
                                                activity!!.finish()
                                            }
                                        }
                                        progress_bar.visibility = View.GONE
                                    })
                                } else {
                                    Toast.makeText(
                                        activity,
                                        task.exception?.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    progress_bar.visibility = View.GONE
                                }
                            })

                    } catch (e: IncompleteDataException) {
                        Toast.makeText(
                            activity,
                            "Usuario y contraseña son obligatorios",
                            Toast.LENGTH_SHORT
                        ).show()
                        progress_bar.visibility = View.GONE

                    }
                }
        })



    }

    override fun handleAuth() {
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
                user?.getIdToken(true)
                    ?.addOnCompleteListener(OnCompleteListener<GetTokenResult> { task ->
                        if (task.isSuccessful) {
                            val idToken = task.result!!.token
                            idToken?.let{
                                viewModel?.tokenSignIn(it).observe(this,Observer{
                                    it?.let{
                                        if (it.role == "usuario"){
                                            val intent = Intent(activity, HomeActivity::class.java)
                                            intent.putExtra("token",it.token)
                                            startActivity(intent)
                                            activity!!.finish()
                                        } else if (it.role == "delivery"){
                                            val intent = Intent(activity,DeliveryHomeActivity::class.java)
                                            intent.putExtra("token",it.token)
                                            startActivity(intent)
                                            activity!!.finish()
                                        }
                                    }
                                })
                            }

                        } else {
                            Toast.makeText(activity,"No pudo obtenerse el token de firebase",Toast.LENGTH_LONG).show()
                        }
                    })
            } else {
                Toast.makeText(activity, "Hubo un error en el ingreso", Toast.LENGTH_LONG).show()
            }
        }

    }


}
