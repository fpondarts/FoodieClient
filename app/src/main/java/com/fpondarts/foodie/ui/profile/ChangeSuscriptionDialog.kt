package com.fpondarts.foodie.ui.profile


import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.repository.UserRepository
import kotlinx.android.synthetic.main.dialog_credit_card.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
/**
 * A simple [Fragment] subclass.
 */
class ChangeSuscriptionDialog : androidx.fragment.app.DialogFragment(), KodeinAware {

    override val kodein by kodein()

    val repository: UserRepository by instance()

    private var dialog: ProgressDialog? = null

    companion object {
        fun newInstance():ChangeSuscriptionDialog{
            val dialog = ChangeSuscriptionDialog()

            return dialog
        }
    }

    private lateinit var fragment: UserProfileFragment


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.dialog_credit_card,container,false)

        fragment = targetFragment!! as UserProfileFragment

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        suscribe_accept.setOnClickListener {
            val text = card_number_et.text.toString()
            if (text.isNullOrBlank() || text.length < 16){
                Toast.makeText(activity,"Formato de tarjeta inválido",Toast.LENGTH_LONG).show()
            } else {
                val cvv = card_cvv_et.text.toString()
                if (cvv.isNullOrBlank() || cvv.length < 3){
                    Toast.makeText(activity,"Codigo de seguridad con formato incorrecto",Toast.LENGTH_LONG).show()
                } else {
                    dialog = ProgressDialog.show(activity,"Procesando medio de pago","Espere")
                    repository.upgradeSuscription(text,cvv).observe(this, Observer {
                        it?.let{
                            if (it){
                                Toast.makeText(activity,"Suscripcion actualizada",Toast.LENGTH_LONG).show()
                                fragment.changeSuscriptionStatus()
                                dialog?.dismiss()
                            } else {
                                Toast.makeText(activity,"Medio de pago inválido",Toast.LENGTH_LONG).show()
                            }
                            dialog?.dismiss()

                        }
                    })
                }
            }
        }

        suscribe_cancel.setOnClickListener {
            dismiss()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        dialog?.dismiss()
    }


}
