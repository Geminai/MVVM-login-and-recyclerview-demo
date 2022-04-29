package com.dvl.sigma.ui.account

import android.app.Activity
import android.content.ContentUris
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dvl.sigma.BuildConfig
import com.dvl.sigma.R
import com.dvl.sigma.data.models.responses.Response
import com.dvl.sigma.databinding.FragmentMyAccountBinding
import com.dvl.sigma.ui.home.HomeActivity
import com.dvl.sigma.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MyAccountFragment : Fragment(), View.OnClickListener {

    private val TAG = "MyAccountFragment"

    lateinit var binding: FragmentMyAccountBinding

    val viewModel: MyAccountViewModel by viewModels()

    lateinit var homeActivity: HomeActivity

    //Our constants
    private val OPERATION_CAPTURE_PHOTO = 1
    private val OPERATION_CHOOSE_PHOTO = 2

    private var mUri: Uri? = null

    lateinit var filePath: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_my_account, container, false
        )

        homeActivity = activity as HomeActivity

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewmodel = viewModel//attach your viewModel to xml

        initUI()

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun initUI() {
        binding.ivProfilePic.setOnClickListener(this)
        binding.btnUpdate.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.ivProfilePic) {
            selectImage()
        }

        if (view?.id == R.id.btnUpdate) {
            homeActivity.showProgress()
            viewModel.updateMyAccount(File(filePath))
                .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    when (it) {
                        is Response.Loading -> {
                            Log.e(TAG, "Loading...")

                        }
                        is Response.Success -> {
                            Log.e(TAG, "Success...")
                            Toast.makeText(requireContext(), it.data?.message, Toast.LENGTH_SHORT)
                                .show()
                            homeActivity.hideProgress()

                        }
                        is Response.Error -> {
                            Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT)
                                .show()
                            homeActivity.hideProgress()
                            Log.e(TAG, "Error == ${it.errorMessage}")
                        }
                    }
                })
        }
    }

    private fun capturePhoto() {
        val capturedImage = File(activity?.externalCacheDir, "My_Captured_Photo.jpg")
        if (capturedImage.exists()) {
            capturedImage.delete()
        }
        capturedImage.createNewFile()
        mUri = if (Build.VERSION.SDK_INT >= 24) {
            FileProvider.getUriForFile(
                requireContext(),  BuildConfig.APPLICATION_ID + ".provider",
                capturedImage
            )
        } else {
            Uri.fromFile(capturedImage)
        }

        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)
        startActivityForResult(intent, OPERATION_CAPTURE_PHOTO)
    }

    private fun openGallery() {
        val intent = Intent("android.intent.action.GET_CONTENT")
        intent.type = "image/*"
        startActivityForResult(intent, OPERATION_CHOOSE_PHOTO)
    }

    private fun renderImage(imagePath: String?) {
        if (imagePath != null) {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            binding.ivProfilePic?.setImageBitmap(bitmap)
        }

    }

    private fun getImagePath(uri: Uri, selection: String?): String {
        var path: String? = null
        val cursor = activity?.contentResolver?.query(uri, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA).toInt())
            }
            cursor.close()
        }
        return path!!
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantedResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantedResults)
        when (requestCode) {
            1 ->
                if (grantedResults.isNotEmpty() && grantedResults.get(0) ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    openGallery()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            OPERATION_CAPTURE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    val bitmap = BitmapFactory.decodeStream(
                        activity?.contentResolver?.openInputStream(mUri!!)
                    )

                    filePath = mUri?.path.toString()

                    binding.ivProfilePic.setImageBitmap(bitmap)
                }
            OPERATION_CHOOSE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    filePath = handleImage(data)
                }
        }
    }

    fun selectImage() {

        val items = arrayOf("Camera", "Gallery")
        val builder = AlertDialog.Builder(requireContext())

        builder.setItems(items) { dialog, index ->

            if (index == 0) {
                capturePhoto()
            } else {
                openGallery()
            }
        }

        builder.create()

        builder.show()

    }

    private fun handleImage(data: Intent?): String {
        var imagePath: String? = null
        val uri = data!!.data
        //DocumentsContract defines the contract between a documents provider and the platform.
        if (DocumentsContract.isDocumentUri(requireContext(), uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            if ("com.android.providers.media.documents" == uri?.authority) {
                val id = docId.split(":")[1]
                val selsetion = MediaStore.Images.Media._ID + "=" + id
                imagePath = getImagePath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    selsetion
                )
            } else if ("com.android.providers.downloads.documents" == uri?.authority) {
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse(
                        "content://downloads/public_downloads"
                    ), java.lang.Long.valueOf(docId)
                )
                imagePath = getImagePath(contentUri, null)
            }
        } else if ("content".equals(uri?.scheme, ignoreCase = true)) {

            imagePath = getImagePath(uri!!, null)

        } else if ("file".equals(uri?.scheme, ignoreCase = true)) {
            imagePath = uri?.path
        }
        renderImage(imagePath)
        return imagePath.toString()
    }

}