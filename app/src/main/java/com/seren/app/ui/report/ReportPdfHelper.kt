package com.seren.app.ui.report

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.seren.app.data.ConditionIds
import com.seren.app.data.model.ConditionScore
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ReportPdfHelper {

    fun generateAndSharePdf(
        context: Context,
        sessionId: Long,
        completedAt: Long,
        scores: List<ConditionScore>
    ) {
        try {
            val pdfDocument = PdfDocument()
            
            // Standard A4 width = 595, height = 842
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas
            
            val paint = Paint()
            paint.isAntiAlias = true
            
            // Draw Header Accent Bar
            paint.color = Color.parseColor("#0D2A6E") // Deep Blue
            canvas.drawRect(0f, 0f, 595f, 40f, paint)
            
            // Draw SEREN Title
            paint.color = Color.WHITE
            paint.textSize = 20f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("SEREN — Every Child is a Star", 30f, 26f, paint)
            
            // Draw Document Sub-title
            paint.color = Color.parseColor("#222222")
            paint.textSize = 24f
            canvas.drawText("Developmental Screening Report", 30f, 85f, paint)
            
            // Date formatting
            val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
            val dateStr = dateFormat.format(Date(completedAt))
            
            paint.textSize = 10f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            paint.color = Color.parseColor("#555555")
            canvas.drawText("Session ID: #$sessionId  |  Date: $dateStr", 30f, 105f, paint)
            
            // Draw Divider Line
            paint.color = Color.parseColor("#DDDDDD")
            canvas.drawLine(30f, 115f, 565f, 115f, paint)
            
            // Draw Disclaimer Panel
            paint.color = Color.parseColor("#F5F5F5")
            canvas.drawRoundRect(30f, 125f, 565f, 185f, 8f, 8f, paint)
            
            paint.color = Color.parseColor("#1565C0") // Navy accent
            canvas.drawRect(30f, 125f, 35f, 185f, paint)
            
            paint.color = Color.parseColor("#333333")
            paint.textSize = 9f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
            canvas.drawText("Disclaimer: This report indicates developmental risk indicators observed during dynamic, game-based", 45f, 142f, paint)
            canvas.drawText("screening modalities (touch, gaze speed, attention latency, voice disfluency). It does NOT constitute", 45f, 156f, paint)
            canvas.drawText("a diagnostic evaluation. Please review with a qualified pediatrician or developmental therapist.", 45f, 170f, paint)
            
            // List of active scoring conditions (only high/moderate indicators to avoid spamming)
            val highRiskScores = scores.filter { it.riskScore >= 30f }
                .sortedByDescending { it.riskScore }
            
            var yPos = 220f
            
            paint.textSize = 14f
            paint.color = Color.parseColor("#0D2A6E")
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("Flagged Indicators & Risk Classification", 30f, yPos, paint)
            
            yPos += 20f
            
            // Draw Table Header
            paint.color = Color.parseColor("#0D2A6E")
            canvas.drawRect(30f, yPos, 565f, yPos + 22f, paint)
            
            paint.color = Color.WHITE
            paint.textSize = 10f
            canvas.drawText("Condition Name", 40f, yPos + 15f, paint)
            canvas.drawText("Risk Score", 320f, yPos + 15f, paint)
            canvas.drawText("Confidence", 420f, yPos + 15f, paint)
            canvas.drawText("Category", 490f, yPos + 15f, paint)
            
            yPos += 22f
            
            if (highRiskScores.isEmpty()) {
                paint.color = Color.parseColor("#333333")
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                paint.textSize = 11f
                canvas.drawText("No elevated indicators detected. All monitored profiles are within standard ranges.", 40f, yPos + 25f, paint)
            } else {
                highRiskScores.forEach { score ->
                    // Alternate background highlight
                    paint.color = Color.parseColor("#FAFAFA")
                    canvas.drawRect(30f, yPos, 565f, yPos + 28f, paint)
                    
                    paint.color = Color.parseColor("#222222")
                    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    val displayName = ConditionIds.getDisplayName(score.conditionId)
                    val truncatedName = if (displayName.length > 38) displayName.take(35) + "..." else displayName
                    canvas.drawText(truncatedName, 40f, yPos + 18f, paint)
                    
                    val scorePct = score.riskScore.toInt()
                    val riskColorStr = when {
                        scorePct < 30 -> "#1B5E20" // Green
                        scorePct < 60 -> "#1565C0" // Blue
                        else -> "#E65100"          // Amber
                    }
                    
                    paint.color = Color.parseColor(riskColorStr)
                    canvas.drawText("$scorePct%", 320f, yPos + 18f, paint)
                    
                    paint.color = Color.parseColor("#555555")
                    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                    canvas.drawText(score.confidenceLevel.uppercase(), 420f, yPos + 18f, paint)
                    
                    val cat = ConditionIds.getCategory(score.conditionId)
                    val shortCat = if (cat.length > 12) cat.take(10) + ".." else cat
                    canvas.drawText(shortCat, 490f, yPos + 18f, paint)
                    
                    // Bottom border line
                    paint.color = Color.parseColor("#EEEEEE")
                    canvas.drawLine(30f, yPos + 28f, 565f, yPos + 28f, paint)
                    
                    yPos += 28f
                }
            }
            
            // Draw Recovery Exercises Recommendations Panel
            yPos += 30f
            paint.textSize = 14f
            paint.color = Color.parseColor("#0D2A6E")
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("Customized Recovery & Practice Activities", 30f, yPos, paint)
            
            yPos += 15f
            paint.color = Color.parseColor("#DDDDDD")
            canvas.drawLine(30f, yPos, 565f, yPos, paint)
            
            yPos += 15f
            
            // Match custom practice tasks based on risk scores to help them recover
            val recTasks = mutableListOf<Pair<String, String>>()
            val hasReadingRisk = highRiskScores.any { 
                it.conditionId == ConditionIds.DYSLEXIA_PHONOLOGICAL || it.conditionId == ConditionIds.DYSLEXIA_SURFACE || it.conditionId == ConditionIds.DYSLEXIA_MIXED
            }
            val hasAttentionRisk = highRiskScores.any { 
                it.conditionId == ConditionIds.ADHD_INATTENTIVE || it.conditionId == ConditionIds.ADHD_HYPERACTIVE || it.conditionId == ConditionIds.ADHD_COMBINED 
            }
            val hasAnxietyRisk = highRiskScores.any {
                it.conditionId == ConditionIds.SOCIAL_ANXIETY || it.conditionId == ConditionIds.GAD || it.conditionId == ConditionIds.TEST_ANXIETY
            }
            
            if (hasReadingRisk) {
                recTasks.add("Phoneme Segmentation" to "Drag and connect phonological items daily to decode reading patterns.")
                recTasks.add("Letter-Formation Copying" to "Practice visual-motor copying to calibrate fine motor delays.")
            }
            if (hasAttentionRisk) {
                recTasks.add("Breathing Focus Space" to "Expand focus calibration using rhythmic breathing circles (10 mins/day).")
            }
            if (hasAnxietyRisk) {
                recTasks.add("Brave Explorer Scenarios" to "Graduated micro-exposure pathways to build classroom participation.")
            }
            
            if (recTasks.isEmpty()) {
                recTasks.add("Cognitive Brain Gym" to "Daily memory matrix sequencing and spatial Corsi span builder.")
                recTasks.add("Focus Circle Pacing" to "CPT target-inhibition exercises to sustain visual processing focus.")
            }
            
            recTasks.forEach { (taskName, desc) ->
                paint.color = Color.parseColor("#222222")
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                paint.textSize = 10f
                canvas.drawText("•  $taskName:", 40f, yPos, paint)
                
                paint.color = Color.parseColor("#555555")
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                canvas.drawText(desc, 150f, yPos, paint)
                
                yPos += 20f
            }
            
            // Draw Footer Info
            paint.color = Color.parseColor("#888888")
            paint.textSize = 8f
            canvas.drawText("SEREN Assessment Engine v0.3.0-audit-fixes  |  Confidential Medical Screening Document", 130f, 800f, paint)
            
            pdfDocument.finishPage(page)
            
            // Save to Cache directory
            val pdfFile = File(context.cacheDir, "SEREN_Report_${sessionId}.pdf")
            val outputStream = FileOutputStream(pdfFile)
            pdfDocument.writeTo(outputStream)
            pdfDocument.close()
            outputStream.close()
            
            // Fire Intent to share on WhatsApp!
            val authority = "${context.packageName}.provider"
            val uri: Uri = FileProvider.getUriForFile(context, authority, pdfFile)
            
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, "Hello! Here is the SEREN developmental screening report for Session #$sessionId.")
                setPackage("com.whatsapp")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            
            try {
                sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(sendIntent)
            } catch (ex: Exception) {
                // Fallback to general sharing chooser if WhatsApp package is missing
                val chooser = Intent.createChooser(sendIntent, "Share report via:")
                chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(chooser)
            }
            
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to build or share report: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }
}
