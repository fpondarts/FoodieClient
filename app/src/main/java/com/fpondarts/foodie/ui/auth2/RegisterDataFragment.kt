package com.fpondarts.foodie.ui.auth2

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController

import com.fpondarts.foodie.R
import com.fpondarts.foodie.databinding.FragmentRegisterDataBinding
import com.fpondarts.foodie.util.exception.IncompleteDataException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.fragment_register_data.*

import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class RegisterDataFragment : DialogFragment(), KodeinAware {

    companion object {
        fun newInstance() = RegisterDataFragment()
    }

    override val kodein by kodein()

    val factory:AuthViewModelFactory by instance()

    private lateinit var viewModel: RegisterDataViewModel

    lateinit var navController: NavController

    private val ROLES = arrayOf("User", "Delivery")

    private var name:String? = null
    private var email:String? = null
    private var photoUri:String? = null
    private var password:String? = null
    private var uid:String? = null

    private var uploading = false

    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_GALLERY = 2

    var localPhotoPath:String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProviders.of(this,factory).get(RegisterDataViewModel::class.java)
        val binding: FragmentRegisterDataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_register_data,container,false)
        binding.viewModel = viewModel

        val adapter = ArrayAdapter<String>(context!!,android.R.layout.simple_spinner_item,ROLES)

        spinnerRoles.adapter = adapter
        spinnerRoles.setSelection(0)

        viewModel.name = arguments!!.getString("name")
        viewModel.email = arguments!!.getString("email")
        arguments!!.getString("photo")?.let{
            viewModel.photo = it
            Picasso.get().load(it).into(imageViewPhoto)
        }
        viewModel.uid = arguments!!.getString("uid")
        arguments!!.getString("password")?.let{
            viewModel.password = it
        }


        val callback = activity!!.onBackPressedDispatcher.addCallback(this) {
            deleteFbUser()
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        signUpButton.setOnClickListener(View.OnClickListener {
            if (!uploading) {
                try {
                    viewModel.signUpUser().observe(this, Observer {
                        it?.let {
                            if (it) {
                                navController.navigate(
                                    R.id.action_registerDataFragment_to_signInFragment, null,
                                    NavOptions.Builder().setPopUpTo(
                                        R.id.signInFragment,
                                        true
                                    ).build()
                                )
                            }
                        }
                    })
                } catch (e: IncompleteDataException) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(activity,"La imagen de perfil se esta cargando, espere",Toast.LENGTH_LONG).show()
            }
        })

        buttonCancelSignUp.setOnClickListener(View.OnClickListener {
            deleteFbUser()
        })


        imageViewPhoto.setOnClickListener(View.OnClickListener {
            onCreateDialog(savedInstanceState).show()
        })

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Elige un método")
                .setItems(R.array.picture_methods ,
                    DialogInterface.OnClickListener { dialog, which ->
                        if (which == 0){
                            onCamara()
                        } else if (which==1){
                            onGallery()
                        }
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")    }


    fun onCamara(){
        dispatchTakePictureIntent()
    }

    fun onGallery(){
               //Create an Intent with action as ACTION_PICK
       val intent = Intent(Intent.ACTION_PICK);
       // Sets the type as image/*. This ensures only components of type image are selected
       intent.setType("image/*");
       //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
       val mimeTypes = arrayOf("image/jpeg", "image/png")
       intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);

       startActivityForResult(intent,REQUEST_GALLERY);

    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {

                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }

                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        context!!,
                        "com.fpondarts.foodie.fileprovider",
                        it
                    )
                    Log.d("TAG",photoURI.toString())
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> uploadPhoto()
                REQUEST_GALLERY -> {
                    val uri = data?.data
                    localPhotoPath = uri?.path
                    uploadPhoto()
                }
                else -> return
            }
        }
    }

    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            localPhotoPath = absolutePath
        }
    }

    fun uploadPhoto(){

        var file = Uri.fromFile(File(localPhotoPath))

        val storage = FirebaseStorage.getInstance()
        val ref = storage.reference
        val imageRef = ref.child("images/${viewModel.uid}/${file.lastPathSegment}")

        val uploadTask = imageRef.putFile(file)
        uploading = true
        uploadTask.addOnFailureListener{
            Toast.makeText(activity,"No pudo cargarse la imagen de perfil",Toast.LENGTH_SHORT).show()
            uploading = false
        }.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                uploading = false
                viewModel.photo = it.toString()
                Picasso.get().load(viewModel.photo).into(imageViewPhoto)
            }
        }
    }

    fun deleteFbUser(){
        val user = FirebaseAuth.getInstance().currentUser

        user?.delete()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navController.navigate(R.id.action_registerDataFragment_to_signInFragment,null,
                        NavOptions.Builder().setPopUpTo(R.id.signInFragment,true).build())
                } else {
                    Toast.makeText(activity,"No se puede cancelar",Toast.LENGTH_SHORT).show()
                }
            }
    }


}
