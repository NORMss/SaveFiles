package com.norm.mysavefiles

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class MediaStoreUtil(
    private val context: Context,
) {
    suspend fun saveImage(bitmap: Bitmap) {
        withContext(Dispatchers.IO) {
            val resolver = context.contentResolver
            val imageCollection = MediaStore.Images.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL_PRIMARY,
            )

            val timeMillis = System.currentTimeMillis()

            val imageContentValues = ContentValues().apply {
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES,
                )
                put(
                    MediaStore.Images.Media.DISPLAY_NAME,
                    "${timeMillis}_image" + ".jpg",
                )
                put(
                    MediaStore.Images.Media.MIME_TYPE,
                    "image/jpg",
                )
                put(
                    MediaStore.Images.Media.DATE_TAKEN,
                    timeMillis,
                )
                put(
                    MediaStore.Images.Media.IS_PENDING,
                    1,
                )
            }

            val imageMediaStoreUri = resolver.insert(
                imageCollection, imageContentValues,
            )

            imageMediaStoreUri?.let { uri ->
                try {
                    resolver.openOutputStream(uri)?.let { outputStream ->
                        bitmap.compress(
                            Bitmap.CompressFormat.JPEG, 100, outputStream,
                        )
                    }

                    imageContentValues.put(
                        MediaStore.MediaColumns.IS_PENDING,
                        0,
                    )

                    resolver.update(
                        uri, imageContentValues, null, null,
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    resolver.delete(uri, null, null)
                }
            }
        }
    }

    suspend fun saveVideo(file: File) {
        withContext(Dispatchers.IO) {
            val resolver = context.contentResolver
            val videoCollection = MediaStore.Video.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL_PRIMARY,
            )

            val timeMillis = System.currentTimeMillis()

            val videoContentValues = ContentValues().apply {
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_MOVIES,
                )
                put(
                    MediaStore.Video.Media.DISPLAY_NAME,
                    "${timeMillis}_video" + ".mp4",
                )
                put(
                    MediaStore.Video.Media.MIME_TYPE,
                    "video/mp4",
                )
                put(
                    MediaStore.Video.Media.DATE_ADDED,
                    timeMillis,
                )
                put(
                    MediaStore.Video.Media.IS_PENDING,
                    1,
                )
            }

            val videoMediaStoreUri = resolver.insert(
                videoCollection, videoContentValues,
            )

            videoMediaStoreUri?.let { uri ->
                try {
                    resolver.openOutputStream(uri)?.use { outputStream ->
                        resolver.openInputStream(
                            Uri.fromFile(file)
                        )?.use { inputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }

                    videoContentValues.put(
                        MediaStore.MediaColumns.IS_PENDING,
                        0,
                    )

                    resolver.update(
                        uri, videoContentValues, null, null,
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    resolver.delete(uri, null, null)
                }
            }
        }
    }

    suspend fun saveAudio(file: File) {
        withContext(Dispatchers.IO) {
            val resolver = context.contentResolver
            val audioCollection = MediaStore.Audio.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL_PRIMARY,
            )

            val timeMillis = System.currentTimeMillis()

            val audioContentValues = ContentValues().apply {
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_MUSIC,
                )
                put(
                    MediaStore.Audio.Media.DISPLAY_NAME,
                    "${timeMillis}_audio" + ".mp3",
//                    file.name
                )
                put(
                    MediaStore.Audio.Media.MIME_TYPE,
                    "audio/mpeg",
                )
                put(
                    MediaStore.Audio.Media.DATE_ADDED,
                    timeMillis,
                )
                put(
                    MediaStore.Audio.Media.IS_PENDING,
                    1,
                )
            }

            val audioMediaStoreUri = resolver.insert(
                audioCollection, audioContentValues,
            )

            audioMediaStoreUri?.let { uri ->
                try {
                    resolver.openOutputStream(uri)?.use { outputStream ->
                        resolver.openInputStream(
                            Uri.fromFile(file)
                        )?.use { inputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }

                    audioContentValues.put(
                        MediaStore.MediaColumns.IS_PENDING,
                        0,
                    )

                    resolver.update(
                        uri, audioContentValues, null, null,
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    resolver.delete(uri, null, null)
                }
            }
        }
    }

    fun getRawAudioFile(resourceId: Int): File {
        val inputStream = context.resources.openRawResource(resourceId)

        val audioFile = File.createTempFile(
            "temp_audio", ".mp3", context.cacheDir
        )
        audioFile.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }

        return audioFile
    }

    fun getRawVideoFile(resourceId: Int): File {
        val inputStream = context.resources.openRawResource(resourceId)

        val videoFile = File.createTempFile(
            "temp_video", ".mp4", context.cacheDir
        )
        videoFile.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }

        return videoFile
    }

    fun getRawBmpFile(resourceId: Int): Bitmap {
        val inputStream = context.resources.openRawResource(resourceId)

        return BitmapFactory.decodeStream(inputStream)
    }
}