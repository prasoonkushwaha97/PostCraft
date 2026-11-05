package com.example.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

private const val TAG = "ShareUtil"

object ShareUtil {

    fun shareText(context: Context, text: String, subject: String = "GramAwaz Ground Report") {
        try {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, text)
            }
            context.startActivity(Intent.createChooser(intent, "Share via..."))
        } catch (e: Exception) {
            Log.e(TAG, "Failed to share text", e)
            Toast.makeText(context, "Could not launch share sheet", Toast.LENGTH_SHORT).show()
        }
    }

    fun shareBitmapWithText(
        context: Context,
        bitmap: Bitmap,
        captionText: String,
        title: String = "Share Ground Reality Report"
    ) {
        try {
            val cachePath = File(context.cacheDir, "images")
            cachePath.mkdirs()
            val imageFile = File(cachePath, "gramawaz_report_${System.currentTimeMillis()}.png")
            val stream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()

            val contentUri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                imageFile
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, contentUri)
                putExtra(Intent.EXTRA_TEXT, captionText)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(Intent.createChooser(shareIntent, title))
        } catch (e: Exception) {
            Log.e(TAG, "Failed to share image with text", e)
            Toast.makeText(context, "Sharing image failed: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveBitmapToDevice(context: Context, bitmap: Bitmap): Boolean {
        val filename = "GramAwaz_${System.currentTimeMillis()}.jpg"
        var outputStream: OutputStream? = null

        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/GramAwaz")
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }

                val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                if (uri != null) {
                    outputStream = context.contentResolver.openOutputStream(uri)
                    outputStream?.let { bitmap.compress(Bitmap.CompressFormat.JPEG, 95, it) }
                    values.clear()
                    values.put(MediaStore.Images.Media.IS_PENDING, 0)
                    context.contentResolver.update(uri, values, null, null)
                    Toast.makeText(context, "Card saved to Pictures/GramAwaz!", Toast.LENGTH_LONG).show()
                    true
                } else {
                    false
                }
            } else {
                val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/GramAwaz"
                val dir = File(imagesDir)
                if (!dir.exists()) dir.mkdirs()
                val file = File(dir, filename)
                outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)
                Toast.makeText(context, "Card saved to Pictures/GramAwaz!", Toast.LENGTH_LONG).show()
                true
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save bitmap", e)
            Toast.makeText(context, "Failed to save image: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            false
        } finally {
            outputStream?.close()
        }
    }

    /**
     * Generates a high-resolution, branded social quote and evidence card (1080 x 1350 - 4:5 aspect ratio)
     */
    fun createVisualQuoteCard(
        context: Context,
        village: String,
        district: String,
        claim: String,
        reality: String,
        proofBitmap: Bitmap?,
        eventBadge: String = ""
    ): Bitmap {
        val width = 1080
        val height = 1350
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Modern Zinc/Dark Background
        val bgPaint = Paint().apply { color = Color.parseColor("#09090B") }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        // Top Header Banner (Safety Orange #EA580C)
        val headerPaint = Paint().apply { color = Color.parseColor("#EA580C") }
        canvas.drawRect(0f, 0f, width.toFloat(), 150f, headerPaint)

        val headerTextPaint = Paint().apply {
            color = Color.WHITE
            textSize = 48f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
        canvas.drawText("ग्राम आवाज़ | साथी फाउंडेशन", 60f, 95f, headerTextPaint)

        val subheaderPaint = Paint().apply {
            color = Color.parseColor("#FFEDD5")
            textSize = 26f
            typeface = Typeface.DEFAULT
            isAntiAlias = true
        }
        val locText = "स्थान: ${if (village.isNotBlank()) village else "ग्रामीण क्षेत्र"}, $district"
        canvas.drawText(locText, 60f, 135f, subheaderPaint)

        var currentY = 175f

        // Event Banner if available
        if (eventBadge.isNotBlank()) {
            val eventRect = RectF(60f, currentY, (width - 60).toFloat(), currentY + 70f)
            val eventBgPaint = Paint().apply { color = Color.parseColor("#18181B") }
            val eventBorderPaint = Paint().apply {
                color = Color.parseColor("#F97316")
                style = Paint.Style.STROKE
                strokeWidth = 3f
                isAntiAlias = true
            }
            canvas.drawRoundRect(eventRect, 14f, 14f, eventBgPaint)
            canvas.drawRoundRect(eventRect, 14f, 14f, eventBorderPaint)

            val eventTextPaint = Paint().apply {
                color = Color.parseColor("#F97316")
                textSize = 26f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                isAntiAlias = true
            }
            val displayEvent = if (eventBadge.length > 55) eventBadge.take(52) + "..." else eventBadge
            canvas.drawText("📅 $displayEvent", 85f, currentY + 46f, eventTextPaint)
            currentY += 90f
        }

        // Photo or Illustration Area
        if (proofBitmap != null) {
            val photoHeight = if (eventBadge.isNotBlank()) 360 else 420
            val srcRect = Rect(0, 0, proofBitmap.width, proofBitmap.height)
            val dstRect = RectF(60f, currentY, (width - 60).toFloat(), currentY + photoHeight)
            canvas.drawBitmap(proofBitmap, srcRect, dstRect, Paint(Paint.FILTER_BITMAP_FLAG))
            currentY += photoHeight + 25f
        } else {
            val bannerHeight = if (eventBadge.isNotBlank()) 180f else 220f
            val bannerRect = RectF(60f, currentY, (width - 60).toFloat(), currentY + bannerHeight)
            val placeholderPaint = Paint().apply { color = Color.parseColor("#18181B") }
            val placeholderBorder = Paint().apply {
                color = Color.parseColor("#27272A")
                style = Paint.Style.STROKE
                strokeWidth = 2f
            }
            canvas.drawRoundRect(bannerRect, 16f, 16f, placeholderPaint)
            canvas.drawRoundRect(bannerRect, 16f, 16f, placeholderBorder)

            val bannerTextPaint = Paint().apply {
                color = Color.parseColor("#FAFAFA")
                textSize = 34f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                isAntiAlias = true
            }
            canvas.drawText("नागरिक पत्रकारिता एवं ग्रामीण सुशासन", 90f, currentY + 80f, bannerTextPaint)
            val bannerSubPaint = Paint().apply {
                color = Color.parseColor("#A1A1AA")
                textSize = 24f
                isAntiAlias = true
            }
            canvas.drawText("धरातल की सच्चाई vs कागज़ी विकास योजनाएं", 90f, currentY + 125f, bannerSubPaint)
            currentY += bannerHeight + 25f
        }

        // Section 1: Official Claim Card
        val claimCardRect = RectF(60f, currentY, (width - 60).toFloat(), currentY + 210f)
        val claimBg = Paint().apply { color = Color.parseColor("#18181B") }
        val claimBorder = Paint().apply {
            color = Color.parseColor("#1D4ED8")
            style = Paint.Style.STROKE
            strokeWidth = 2f
        }
        canvas.drawRoundRect(claimCardRect, 16f, 16f, claimBg)
        canvas.drawRoundRect(claimCardRect, 16f, 16f, claimBorder)

        val claimBadgePaint = Paint().apply { color = Color.parseColor("#2563EB") }
        canvas.drawRoundRect(RectF(85f, currentY + 20f, 320f, currentY + 62f), 8f, 8f, claimBadgePaint)

        val badgeTextPaint = Paint().apply {
            color = Color.WHITE
            textSize = 22f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
        canvas.drawText("📋 सरकारी दावा", 105f, currentY + 50f, badgeTextPaint)

        val claimTextPaint = Paint().apply {
            color = Color.parseColor("#E4E4E7")
            textSize = 28f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            isAntiAlias = true
        }
        val claimSample = if (claim.length > 95) claim.take(92) + "..." else claim
        canvas.drawText(claimSample, 85f, currentY + 115f, claimTextPaint)

        currentY += 235f

        // Section 2: Ground Reality Card
        val realityCardRect = RectF(60f, currentY, (width - 60).toFloat(), currentY + 250f)
        val realityBg = Paint().apply { color = Color.parseColor("#18181B") }
        val realityBorder = Paint().apply {
            color = Color.parseColor("#DC2626")
            style = Paint.Style.STROKE
            strokeWidth = 2.5f
        }
        canvas.drawRoundRect(realityCardRect, 16f, 16f, realityBg)
        canvas.drawRoundRect(realityCardRect, 16f, 16f, realityBorder)

        val realityBadgePaint = Paint().apply { color = Color.parseColor("#EF4444") }
        canvas.drawRoundRect(RectF(85f, currentY + 20f, 360f, currentY + 62f), 8f, 8f, realityBadgePaint)
        canvas.drawText("⚠️ ज़मीनी हकीकत", 105f, currentY + 50f, badgeTextPaint)

        val realityTextPaint = Paint().apply {
            color = Color.parseColor("#FAFAFA")
            textSize = 30f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
        val realitySample = if (reality.length > 105) reality.take(102) + "..." else reality
        canvas.drawText(realitySample, 85f, currentY + 115f, realityTextPaint)

        val callToActionPaint = Paint().apply {
            color = Color.parseColor("#F87171")
            textSize = 24f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
            isAntiAlias = true
        }
        canvas.drawText("जिम्मेदार प्रशासन व जनप्रतिनिधि जवाब दें!", 85f, currentY + 185f, callToActionPaint)

        // Bottom Footer (Deep Charcoal / Zinc)
        val footerPaint = Paint().apply { color = Color.parseColor("#18181B") }
        canvas.drawRect(0f, (height - 85).toFloat(), width.toFloat(), height.toFloat(), footerPaint)

        val footerTextPaint = Paint().apply {
            color = Color.parseColor("#A1A1AA")
            textSize = 22f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
        canvas.drawText("साथी फाउंडेशन | ग्राम आवाज़ #GroundReality #CitizenJournalism", 60f, (height - 35).toFloat(), footerTextPaint)

        return bitmap
    }
}
