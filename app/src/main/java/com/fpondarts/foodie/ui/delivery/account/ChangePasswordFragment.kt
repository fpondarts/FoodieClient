package com.fpondarts.foodie.ui.delivery.account


import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer

import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.repository.DeliveryRepository
import com.fpondarts.foodie.data.repository.UserRepository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_change_password.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import org.kodein.di.android.x.kodein
/**
 * A simple [Fragment] subclass.
 */
class ChangePasswordFragment : DialogFragment(), KodeinAware {

    companion object {
        fun newInstance(delivery:Boolean):ChangePasswordFragment{
            val f = ChangePasswordFragment()
            val bundle = bundleOf("delivery" to delivery )
            f.arguments = bundle
            return f
        }
    }

    var progressDialog: ProgressDialog? = null

    override val kodein by kodein()

    var isDelivery:Boolean = false

    val del_repository : DeliveryRepository by instance()
    val user_repo: UserRepository by instance()

    var newPassword = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        isDelivery = arguments!!.getBoolean("delivery",false)
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        et_new_password.setText(newPassword)


        button_change_pass.setOnClickListener(View.OnClickListener {

            newPassword = et_new_password.text.toString()
            if (!newPassword.isNullOrEmpty()){
                progressDialog = ProgressDialog.show(
                    context
                    , "Cambiando contraseña",null
                )
                FirebaseAuth.getInstance().currentUser!!.updatePassword(newPassword)
                    .addOnCompleteListener(
                        OnCompleteListener {
                            if (it.isSuccessful) {
                                if (isDelivery){
                                    del_repository.changePassword(newPassword).observe(this, Observer {
                                        it?.let {
                                            Toast.makeText(activity, it.msg, Toast.LENGTH_SHORT).show()
                                            dismiss()
                                            progressDialog?.dismiss()
                                        }
                                    })
                                } else {
                                    user_repo.changePassword(newPassword).observe(this, Observer {
                                        it?.let {
                                            Toast.makeText(activity, it.msg, Toast.LENGTH_SHORT).show()
                                            dismiss()
                                            progressDialog?.dismiss()
                                        }
                                    })
                                }
                            } else {
                                Toast.makeText(activity,it.exception?.message,Toast.LENGTH_LONG).show()
                                progressDialog?.dismiss()
                            }

                    })
            } else {
                Toast.makeText(activity,"Ingrese una contrasena",Toast.LENGTH_SHORT).show()
            }

        })
    }


}
