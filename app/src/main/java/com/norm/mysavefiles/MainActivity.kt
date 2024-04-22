package com.norm.mysavefiles

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.norm.mysavefiles.ui.theme.MySaveFilesTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mediaStoreUtil = MediaStoreUtil(this)

        setContent {
            MySaveFilesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Button(
                            onClick = {
                                val bmp = mediaStoreUtil.getRawBmpFile(R.raw.img)

                                CoroutineScope(Dispatchers.IO).launch {
                                    mediaStoreUtil.saveImage(bmp)
                                }

                                Toast.makeText(
                                    applicationContext,
                                    "Saved image",
                                    Toast.LENGTH_SHORT,
                                ).show()
                                openFolderByType("image")
                            }
                        ) {
                            Text(
                                text = "Save image"
                            )
                        }
                        Button(
                            onClick = {
                                val file = mediaStoreUtil.getRawVideoFile(R.raw.video)

                                CoroutineScope(Dispatchers.IO).launch {
                                    mediaStoreUtil.saveVideo(file)
                                }
                                Toast.makeText(
                                    applicationContext,
                                    "Saved video",
                                    Toast.LENGTH_SHORT,
                                ).show()
                                openFolderByType("video")
                            }
                        ) {
                            Text(
                                text = "Save video"
                            )
                        }
                        Button(
                            onClick = {
                                val file = mediaStoreUtil.getRawAudioFile(R.raw.music)

                                CoroutineScope(Dispatchers.IO).launch {
                                    mediaStoreUtil.saveAudio(file)
                                }
                                Toast.makeText(
                                    applicationContext,
                                    "Saved audio",
                                    Toast.LENGTH_SHORT,
                                ).show()
                                openFolderByType("audio")
                            }
                        ) {
                            Text(
                                text = "Save audio"
                            )
                        }
                    }
                }
            }
        }
    }

    fun openFolderByType(typeFile: String) {
        val folderIntent = Intent(Intent.ACTION_VIEW).apply {
            type = "${typeFile}/*"
        }
        try {
            startActivity(folderIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                applicationContext,
                "no activity can handle the intent",
                Toast.LENGTH_SHORT,
            ).show()
            openFolderByType("*")
        }
    }
}
