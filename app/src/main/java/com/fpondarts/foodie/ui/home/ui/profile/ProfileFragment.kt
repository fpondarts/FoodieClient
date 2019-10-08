package com.fpondarts.foodie.ui.home.ui.profile

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.fpondarts.foodie.R
import com.fpondarts.foodie.databinding.FragmentProfileBinding
import com.fpondarts.foodie.ui.auth.FoodieViewModelFactory
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.nav_header_home.*
import org.kodein.di.KodeinAware
import org.kodein.di.Kodein
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ProfileFragment : DialogFragment(), KodeinAware {


    override val kodein by kodein()

    val factory: FoodieViewModelFactory by instance()

    var viewModel:ProfileViewModel? = null

    val REQUEST_IMAGE_CAPTURE = 1


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentProfileBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile,container,false)
        val viewModel = ViewModelProviders.of(this,factory).get(ProfileViewModel::class.java)
        binding.viewModel = viewModel
        this.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profilePic.setOnClickListener(
            View.OnClickListener {
                onCreateDialog(savedInstanceState).show()
            }
        )

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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            setPic()
        }
    }

    var currentPhotoPath:String? = null

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
            currentPhotoPath = absolutePath
        }
    }

    private fun setPic() {

        val targetW: Int = profilePic.width
        val targetH: Int = profilePic.height

        val bmOptions = BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true

            val photoW: Int = outWidth
            val photoH: Int = outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int = Math.min(photoW / targetW, photoH / targetH)

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
            inPurgeable = true
        }

        BitmapFactory.decodeFile(currentPhotoPath, bmOptions)?.also { bitmap ->
            profilePic.setImageBitmap(bitmap)
        }

    }


    fun onGallery(){}
}

