package com.personal.student_management.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

/**
 * Helper class for handling file operations, such as saving images to internal storage and deleting image files.
 *
 * @param context The application context.
 */
class FileHelper @Inject constructor(private val context: Context) {

    /**
     * Saves an image from the given Uri to the internal storage of the application.
     *
     * @param uri The Uri of the image to be saved.
     * @param filename The desired filename for the saved image.
     * @return The absolute path of the saved image file, or null if there was an error during the save operation.
     */
    fun saveImageToInternalStorage(uri: Uri, filename: String): String? {
        val inputStream = context.contentResolver.openInputStream(uri)
        val directory = File(context.filesDir, FOLDER_NAME)
        if (!directory.exists()) {
            directory.mkdir()
        }
        val file = File(directory, filename)
        val outputStream = FileOutputStream(file)

        return try {
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Deletes an image file from the internal storage.
     *
     * @param imagePath The absolute path of the image file to be deleted.
     */
    fun deleteImageFile(imagePath: String?) {
        if (!imagePath.isNullOrEmpty()) {
            val imageFile = File(imagePath)
            if (imageFile.exists()) {
                try {
                    imageFile.delete()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
