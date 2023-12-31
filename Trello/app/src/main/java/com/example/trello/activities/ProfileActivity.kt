package com.example.trello.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.trello.R
import com.example.trello.firebase.FireStoreClass
import com.example.trello.models.User
import com.example.trello.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException

class ProfileActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    private var mSelectImageFileURL: Uri? = null
    private var mProfileImageURL: String = ""
    private lateinit var mUserDetails: User
    companion object {
        private const val READ_STORAGE_PERMISSION_CODE = 1
        private const val PICK_IMAGE_REQUEST_CODE = 2
    }


    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                mSelectImageFileURL = data?.data
                val circleImageView: CircleImageView = findViewById(R.id.user_profile_image)

                try {
                    Glide
                        .with(this@ProfileActivity)
                        .load(mSelectImageFileURL)
                        .centerCrop()
                        .placeholder(R.drawable.ic_user_place_holder)
                        .into(circleImageView)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }else{
                Toast.makeText(this@ProfileActivity, "Voit halutessasi antaa oikeudet asetuksissa", Toast.LENGTH_LONG).show()
            }
        }

    private fun showImageChooser() {
        var galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(galleryIntent)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImageChooser()
            }else {

                Toast.makeText(this@ProfileActivity, "Et ole antanut oikeuksia käyttää mediaa. Voit antaa oikeudet asetuksista", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = Firebase.auth

        FireStoreClass().loadUserData(this@ProfileActivity)
        setupActionBar()

        val updateButton: Button = findViewById(R.id.btn_update_profile)
        updateButton.setOnClickListener {
            if(mSelectImageFileURL != null){
                updateUIData()
            }else{
                showProgressDialog(resources.getString(R.string.please_wait))
                updateUserProfileData()
            }
        }

        val circleImageView: CircleImageView = findViewById(R.id.user_profile_image)
        circleImageView.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_STORAGE_PERMISSION_CODE
                )
                //showImageChooser()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_STORAGE_PERMISSION_CODE
                )
            }
        }

    }

    private fun getFileExtension(url: Uri?): String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(url!!))
    }

    fun profileUpdateSuccess(){
        hideProgressDialog()

        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun updateUIData() {
        showProgressDialog(resources.getString(R.string.please_wait))
        if (mSelectImageFileURL != null) {
            val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
                "USER_IMAGE" + System.currentTimeMillis() + "." + getFileExtension(
                    mSelectImageFileURL
                )
            )
            sRef.putFile(mSelectImageFileURL!!).addOnSuccessListener {
                taskSnapShot ->
                run {
                    Log.e(
                        "FIREBASE Image URL",
                        taskSnapShot.metadata!!.reference!!.downloadUrl.toString()
                    )
                }
                taskSnapShot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                    uri ->
                    Log.e("Downloadable Image URL", uri.toString())
                    mProfileImageURL = uri.toString()
                    updateUserProfileData()
                }
            }
                .addOnFailureListener{
                    exception -> Toast.makeText(this, exception.message, Toast.LENGTH_LONG).show()

                }
            hideProgressDialog()

        }
    }




    fun updateUserProfileData(){
        val et_profile_name: AppCompatEditText = findViewById(R.id.et_profile_name)
        val et_profile_email: AppCompatEditText = findViewById(R.id.et_profile_email)
        val et_profile_mobile: AppCompatEditText = findViewById(R.id.et_profile_mobile_number)

        val userHashMap = HashMap<String, Any>()
        if (mProfileImageURL.isNotEmpty() && mProfileImageURL != mUserDetails.image){
            userHashMap[Constants.IMAGE] = mProfileImageURL
        }

        if (et_profile_name.text.toString() != mUserDetails.name){
            userHashMap[Constants.NAME] = et_profile_name.text.toString()

        }
        if (et_profile_mobile.text.toString() != mUserDetails.mobile.toString()){
            userHashMap[Constants.MOBILE] = et_profile_mobile.text.toString().toLong()
        }
        FireStoreClass().updateUserProfileData(this, userHashMap)

    }

    fun updateUIData(user: User) {
        val profile_circleImageView = findViewById<CircleImageView>(R.id.user_profile_image)
        val et_profile_name: AppCompatEditText = findViewById(R.id.et_profile_name)
        val et_profile_email: AppCompatEditText = findViewById(R.id.et_profile_email)
        val et_profile_mobile: AppCompatEditText = findViewById(R.id.et_profile_mobile_number)


        mUserDetails = user

        Glide
            .with(this@ProfileActivity)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(profile_circleImageView)
        et_profile_name.setText(user.name)
        et_profile_email.setText(user.email)
        et_profile_mobile.setText(user.mobile.toString())
    }

    private fun setupActionBar() {
        val toolbar_sign_up_activity = findViewById<Toolbar>(R.id.toolbar_profile_activity)
        setSupportActionBar(toolbar_sign_up_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_color_black_24dp)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Handle the back button click event here
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


}