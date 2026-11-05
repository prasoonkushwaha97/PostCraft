package com.example.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import com.example.ui.theme.CivicSaffron
import java.io.File

private const val TAG = "CameraCaptureDialog"

@Composable
fun CameraCaptureDialog(
    onPhotoCaptured: (Uri) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
        if (!granted) {
            Toast.makeText(context, "Camera permission is required to capture photos", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .testTag("camera_capture_dialog"),
            color = Color.Black
        ) {
            if (!hasCameraPermission) {
                // Permission Denied / Rationale View
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = null,
                            tint = CivicSaffron,
                            modifier = Modifier.size(64.dp)
                        )
                        Text(
                            text = "Camera Permission Required",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "GramAwaz needs camera access so citizen journalists can capture live ground reality evidence directly in the field.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.LightGray,
                            textAlign = TextAlign.Center
                        )
                        Button(
                            onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                            colors = ButtonDefaults.buttonColors(containerColor = CivicSaffron),
                            modifier = Modifier.testTag("request_camera_permission_button")
                        ) {
                            Text("Grant Permission")
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White
                            )
                        }
                    }
                }
            } else {
                // Live CameraX Preview & Controls
                var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
                var camera: Camera? by remember { mutableStateOf(null) }
                var lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }
                var isTorchOn by remember { mutableStateOf(false) }
                var isCapturing by remember { mutableStateOf(false) }

                Box(modifier = Modifier.fillMaxSize()) {
                    // Preview View
                    AndroidView(
                        factory = { ctx ->
                            val previewView = PreviewView(ctx).apply {
                                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                            }
                            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                            cameraProviderFuture.addListener({
                                val cameraProvider = cameraProviderFuture.get()

                                val preview = Preview.Builder().build().also {
                                    it.setSurfaceProvider(previewView.surfaceProvider)
                                }

                                val capture = ImageCapture.Builder()
                                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                                    .build()

                                val cameraSelector = CameraSelector.Builder()
                                    .requireLensFacing(lensFacing)
                                    .build()

                                try {
                                    cameraProvider.unbindAll()
                                    camera = cameraProvider.bindToLifecycle(
                                        lifecycleOwner,
                                        cameraSelector,
                                        preview,
                                        capture
                                    )
                                    imageCapture = capture
                                } catch (e: Exception) {
                                    Log.e(TAG, "Use case binding failed", e)
                                }
                            }, ContextCompat.getMainExecutor(ctx))
                            previewView
                        },
                        modifier = Modifier.fillMaxSize(),
                        update = { previewView ->
                            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                            cameraProviderFuture.addListener({
                                val cameraProvider = cameraProviderFuture.get()
                                val preview = Preview.Builder().build().also {
                                    it.setSurfaceProvider(previewView.surfaceProvider)
                                }
                                val capture = ImageCapture.Builder()
                                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                                    .build()
                                val cameraSelector = CameraSelector.Builder()
                                    .requireLensFacing(lensFacing)
                                    .build()
                                try {
                                    cameraProvider.unbindAll()
                                    camera = cameraProvider.bindToLifecycle(
                                        lifecycleOwner,
                                        cameraSelector,
                                        preview,
                                        capture
                                    )
                                    imageCapture = capture
                                } catch (e: Exception) {
                                    Log.e(TAG, "Rebind camera failed", e)
                                }
                            }, ContextCompat.getMainExecutor(context))
                        }
                    )

                    // Top Bar Overlay
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 24.dp)
                            .align(Alignment.TopCenter),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                                .testTag("close_camera_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Camera",
                                tint = Color.White
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color.Black.copy(alpha = 0.6f)
                        ) {
                            Text(
                                text = "Ground Evidence Camera",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        // Torch Toggle
                        IconButton(
                            onClick = {
                                isTorchOn = !isTorchOn
                                camera?.cameraControl?.enableTorch(isTorchOn)
                            },
                            modifier = Modifier
                                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                                .testTag("toggle_torch_button")
                        ) {
                            Icon(
                                imageVector = if (isTorchOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                                contentDescription = "Toggle Torch",
                                tint = if (isTorchOn) CivicSaffron else Color.White
                            )
                        }
                    }

                    // Bottom Controls Overlay
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 36.dp, start = 32.dp, end = 32.dp)
                            .align(Alignment.BottomCenter),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.size(48.dp))

                        // Large Shutter Button
                        Box(
                            modifier = Modifier
                                .size(78.dp)
                                .clip(CircleShape)
                                .border(4.dp, Color.White, CircleShape)
                                .background(if (isCapturing) Color.Gray else Color.White.copy(alpha = 0.9f))
                                .clickable(enabled = !isCapturing && imageCapture != null) {
                                    isCapturing = true
                                    val photoFile = File(
                                        context.cacheDir,
                                        "ground_proof_${System.currentTimeMillis()}.jpg"
                                    )
                                    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                                    imageCapture?.takePicture(
                                        outputOptions,
                                        ContextCompat.getMainExecutor(context),
                                        object : ImageCapture.OnImageSavedCallback {
                                            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                                isCapturing = false
                                                val savedUri = Uri.fromFile(photoFile)
                                                Toast.makeText(context, "Ground evidence photo captured!", Toast.LENGTH_SHORT).show()
                                                onPhotoCaptured(savedUri)
                                                onDismiss()
                                            }

                                            override fun onError(exception: ImageCaptureException) {
                                                isCapturing = false
                                                Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
                                                Toast.makeText(context, "Capture failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    )
                                }
                                .testTag("shutter_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isCapturing) {
                                CircularProgressIndicator(
                                    color = CivicSaffron,
                                    modifier = Modifier.size(36.dp),
                                    strokeWidth = 3.dp
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(62.dp)
                                        .clip(CircleShape)
                                        .background(CivicSaffron)
                                )
                            }
                        }

                        // Lens Switch (Back <-> Front)
                        IconButton(
                            onClick = {
                                lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
                                    CameraSelector.LENS_FACING_FRONT
                                } else {
                                    CameraSelector.LENS_FACING_BACK
                                }
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                                .testTag("switch_lens_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Cameraswitch,
                                contentDescription = "Switch Camera Lens",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
