package com.fpondarts.foodie.ui.profile


import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment

import com.fpondarts.foodie.R
import com.fpondarts.foodie.data.repository.Repository
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_user_profile.*
import androidx.lifecycle.Observer
import com.fpondarts.foodie.ui.delivery.account.ChangePasswordFragment
import com.google.firebase.storage.FirebaseStorage

/**
 * A simple [Fragment] subclass.
 */

import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class UserProfileFragment : DialogFragment(), KodeinAware {

    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_GALLERY = 2
    var localPhotoPath:String? = null
    private var uploading = false
    var progressDialog : ProgressDialog? = null
    lateinit var uid: String
    var photoUrl: String?=null
    lateinit var imageView: ImageView

    override val kodein by kodein()
    val repository : Repository by instance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pic = user_card.findViewById<ImageView>(R.id.profilePic)
        imageView = pic
        pic.setOnClickListener(View.OnClickListener {
            onCreateDialog(savedInstanceState).show()
        })

        pic?.let{
            Log.d("PictureDim","W:${it.width}, H:${it.height}")
        }

        val name = user_card.findViewById<TextView>(R.id.tv_user_name)
        val email = user_card.findViewById<TextView>(R.id.tv_email)
        val phone = user_card.findViewById<TextView>(R.id.tv_phone)

        val points = points_card.findViewById<TextView>(R.id.points_tv)

        val suscription = suscription_card.findViewById<TextView>(R.id.suscription_tv)



        repository.refreshUser()
        repository.currentUser.observe(this, Observer {
            it?.let{

                uid = it.firebase_uid!!
                it.picture?.let{
                    photoUrl = it
                    Picasso.get().load(photoUrl)
                        .resize(80,80)
                        .rotate(270.0.toFloat()).into(imageView)
                }

                name.text = it.name
                phone.text = it.phone_number
                email.text = it.email

                points.text = it.favourPoints.toString()
                suscription.text = it.suscripcion.toString()
            }
        })

    }

    override fun onResume() {
        super.onResume()

        photoUrl?.let{
            Picasso.get().load(photoUrl)
                .resize(80,80)
                .rotate(270.0.toFloat()).into(imageView)
        }


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
                            Toast.makeText(activity,"No disponible",Toast.LENGTH_LONG).show()
                        }
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")    }


    fun onCamara(){
        dispatchTakePictureIntent()
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
                    Toast.makeText(activity,"back from gallery", Toast.LENGTH_SHORT).show()
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

        progressDialog = ProgressDialog.show(context,"Cargando foto","Cargando")

        var file = Uri.fromFile(File(localPhotoPath))

        val storage = FirebaseStorage.getInstance()
        val ref = storage.reference
        val imageRef = ref.child("images/${uid}/${file.lastPathSegment}")

        val uploadTask = imageRef.putFile(file)
        uploading = true
        uploadTask.addOnFailureListener{
            Toast.makeText(activity,"No pudo cargarse la imagen de perfil",Toast.LENGTH_SHORT).show()
            uploading = false
            progressDialog?.dismiss()
        }.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                uploading = false
                Picasso.get().load(it.toString()).resize(80,80).rotate(270.0.toFloat()).into(imageView)
                progressDialog?.dismiss()
                repository.updatePic(it.toString()).observe(this, Observer {
                    it?.msg?.let {
                        Toast.makeText(activity,it,Toast.LENGTH_LONG).show()
                        progressDialog?.dismiss()
                    }
                })
            }
        }
    }

    fun changePassword(){
        val passDialog = ChangePasswordFragment.newInstance(false)
        passDialog.show(fragmentManager!!,"ChangePassword")
    }
}
