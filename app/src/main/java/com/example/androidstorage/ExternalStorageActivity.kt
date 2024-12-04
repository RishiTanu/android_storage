package com.example.androidstorage

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.Display
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.androidstorage.databinding.ActivityExternalStorageBinding
import java.security.Permission

class ExternalStorageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExternalStorageBinding
    private lateinit var sharedPhotoAdapter: SharedPhotoAdapter

    private var readPermissionGrander = false
    private var writePermissionGrander = false
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExternalStorageBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    private fun updateOrRequestPermission(){
        val hasReadPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED

        val hasWritePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )==PackageManager.PERMISSION_GRANTED

        val minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        readPermissionGrander = hasReadPermission
        writePermissionGrander = hasWritePermission || minSdk29

        val permissionReq = mutableListOf<String>()
        if(!writePermissionGrander){
            permissionReq.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if(!readPermissionGrander){
            permissionReq.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if(permissionReq.isNotEmpty()){
            permissionLauncher.launch(permissionReq.toTypedArray())
        }

    }

}