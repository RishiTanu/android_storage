package com.example.androidstorage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.androidstorage.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var internalStorageAdapter: InternalStorageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        internalStorageAdapter = InternalStorageAdapter { internalStorage ->
            val isDeleteSuccessfully = deletePhotoFromInternalStorage(internalStorage.name)
            if (isDeleteSuccessfully) {
                loadPhotoFromInternalStorageIntoRecyclerView()
                Toast.makeText(this, "Delete image successfully", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, "Failed to delete image", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        val takePhoto = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
                val private = binding.switchPrivate.isChecked
                if (private) {
                    if (bitmap != null) {
                        val isSavedSuccessfully =
                            savePhotoInternalStorage(UUID.randomUUID().toString(), bitmap)
                        if (isSavedSuccessfully) {
                            loadPhotoFromInternalStorageIntoRecyclerView()
                            Toast.makeText(this, "Saved image successfully", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(this, "Failed to saved image", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }

        binding.btnTakePhoto.setOnClickListener {
            takePhoto.launch()
        }

        setUpInternalStorageRecyclerView()
        loadPhotoFromInternalStorageIntoRecyclerView()
    }

    private fun setUpInternalStorageRecyclerView() = binding.rvPrivatePhotos.apply {
        adapter = internalStorageAdapter
        layoutManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)
    }

    private fun loadPhotoFromInternalStorageIntoRecyclerView() {
        lifecycleScope.launch {
            val photos = loadPhotoFromInternalStorage()
            internalStorageAdapter.submitList(photos)
        }
    }

    private fun deletePhotoFromInternalStorage(fileName: String): Boolean {
        return try {
            deleteFile(fileName)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private suspend fun loadPhotoFromInternalStorage(): List<InternalStoragePhoto> {
        return withContext(Dispatchers.IO) {
            val files = filesDir.listFiles()
            files?.filter { it.isFile && it.canRead() && it.endsWith(".jpg") }?.map {
                val bytes = it.readBytes()
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                InternalStoragePhoto(it.name, bmp)
            } ?: listOf()
        }
    }

    private fun savePhotoInternalStorage(fileName: String, bmp: Bitmap): Boolean {
        return try {
            openFileOutput("$fileName.jpg", MODE_PRIVATE).use { stream ->
                if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                    throw IOException("Could not saved image")
                }
                return true
            }
        } catch (io: IOException) {
            io.printStackTrace()
            false
        }
    }
}