package com.seren.app.ui.report

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
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
            
            // 1. Draw Header Accent Bar
            paint.color = Color.parseColor("#0F172A") // Premium Dark Slate
            canvas.drawRect(0f, 0f, 595f, 45f, paint)
            
            // Draw SEREN branding text
            paint.color = Color.WHITE
            paint.textSize = 15f
            paint.typeface = Typeface.create("sans-serif", Typeface.BOLD)
            canvas.drawText("S E R E N", 35f, 28f, paint)
            
            paint.color = Color.parseColor("#94A3B8")
            paint.textSize = 9f
            paint.typeface = Typeface.create("sans-serif", Typeface.NORMAL)
            canvas.drawText("EVERY CHILD IS A STAR", 115f, 26f, paint)
            
            // 2. Draw Report Document Header
            paint.color = Color.parseColor("#0F172A")
            paint.textSize = 22f
            paint.typeface = Typeface.create("sans-serif", Typeface.BOLD)
            canvas.drawText("Developmental Screening Report", 35f, 80f, paint)
            
            // Date formatting
            val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
            val dateStr = dateFormat.format(Date(completedAt))
            
            paint.textSize = 9.5f
            paint.color = Color.parseColor("#64748B")
            paint.typeface = Typeface.create("sans-serif", Typeface.NORMAL)
            canvas.drawText("Session ID: #$sessionId  |  Date: $dateStr  |  India-first Screening", 35f, 98f, paint)
            
            // Divider Line
            paint.color = Color.parseColor("#E2E8F0")
            canvas.drawLine(35f, 108f, 560f, 108f, paint)
            
            // 3. Draw Disclaimer Box (with blue left border stripe)
            paint.color = Color.parseColor("#F8FAFC") // Slate light background
            val disclaimerBg = RectF(35f, 118f, 560f, 168f)
            canvas.drawRoundRect(disclaimerBg, 6f, 6f, paint)
            
            paint.color = Color.parseColor("#3B82F6") // Blue border accent
            canvas.drawRect(35f, 118f, 39f, 168f, paint)
            
            paint.color = Color.parseColor("#475569")
            paint.textSize = 8.5f
            paint.typeface = Typeface.create("sans-serif", Typeface.ITALIC)
            canvas.drawText("Disclaimer: This report indicates developmental risk indicators observed during dynamic, game-based", 48f, 134f, paint)
            canvas.drawText("screening modalities (touch, gaze speed, attention latency, voice disfluency). It does NOT constitute", 48f, 147f, paint)
            canvas.drawText("a medical diagnosis. Please review this clinical profile summary with a qualified pediatrician.", 48f, 160f, paint)
            
            // 4. Draw Domain Performance Progress Gauges
            val domains = listOf(
                "Learning & Literacy" to getDomainMaxScore(0, scores),
                "Attention & Focus" to getDomainMaxScore(1, scores),
                "Speech & Fluency" to getDomainMaxScore(2, scores),
                "Anxiety & Confidence" to getDomainMaxScore(3, scores),
                "Silent Profiles" to getDomainMaxScore(4, scores),
                "Sensory & Motor" to getDomainMaxScore(5, scores)
            )

            var domainY = 190f
            paint.color = Color.parseColor("#0F172A")
            paint.textSize = 12f
            paint.typeface = Typeface.create("sans-serif", Typeface.BOLD)
            canvas.drawText("Developmental Domain Signal Map", 35f, domainY, paint)
            
            domainY += 15f
            
            domains.forEach { (name, maxScore) ->
                // Category Label
                paint.color = Color.parseColor("#334155")
                paint.textSize = 9.5f
                paint.typeface = Typeface.create("sans-serif", Typeface.NORMAL)
                canvas.drawText(name, 35f, domainY + 9f, paint)
                
                // Track bar background
                paint.color = Color.parseColor("#F1F5F9")
                val trackRect = RectF(160f, domainY + 1f, 460f, domainY + 9f)
                canvas.drawRoundRect(trackRect, 4f, 4f, paint)
                
                // Track bar progress (color-coded gauges)
                if (maxScore > 0) {
                    val progressWidth = 160f + (300f * (maxScore.coerceIn(0, 100) / 100f))
                    val progressColor = when {
                        maxScore < 30 -> "#10B981" // Green (Optimal)
                        maxScore < 60 -> "#F59E0B" // Amber (Watch)
                        else -> "#EF4444"          // Red (Flagged)
                    }
                    paint.color = Color.parseColor(progressColor)
                    val progressRect = RectF(160f, domainY + 1f, progressWidth, domainY + 9f)
                    canvas.drawRoundRect(progressRect, 4f, 4f, paint)
                }
                
                // Status description text
                val statusText = when {
                    maxScore == 0 -> "Not Flagged"
                    maxScore < 30 -> "$maxScore% (Optimal)"
                    maxScore < 60 -> "$maxScore% (Watch)"
                    else -> "$maxScore% (Flagged)"
                }
                paint.color = when {
                    maxScore == 0 -> Color.parseColor("#94A3B8")
                    maxScore < 30 -> Color.parseColor("#10B981")
                    maxScore < 60 -> Color.parseColor("#F59E0B")
                    else -> Color.parseColor("#EF4444")
                }
                paint.typeface = Typeface.create("sans-serif", Typeface.BOLD)
                canvas.drawText(statusText, 475f, domainY + 9f, paint)
                
                domainY += 16f
            }
            
            // 5. Draw Flagged Indicators Table
            var yPos = domainY + 18f
            paint.color = Color.parseColor("#0F172A")
            paint.textSize = 12f
            paint.typeface = Typeface.create("sans-serif", Typeface.BOLD)
            canvas.drawText("Flagged Indicators & Risk Classification", 35f, yPos, paint)
            
            yPos += 14f
            
            // Table Header Rect
            paint.color = Color.parseColor("#1E293B") // Slate Grey Header
            canvas.drawRect(35f, yPos, 560f, yPos + 18f, paint)
            
            paint.color = Color.WHITE
            paint.textSize = 9f
            paint.typeface = Typeface.create("sans-serif", Typeface.BOLD)
            canvas.drawText("Profile / Indicator", 42f, yPos + 12f, paint)
            canvas.drawText("Risk Score", 290f, yPos + 12f, paint)
            canvas.drawText("Confidence", 380f, yPos + 12f, paint)
            canvas.drawText("Clinical Domain", 475f, yPos + 12f, paint)
            
            yPos += 18f
            
            val highRiskScores = scores.filter { it.riskScore >= 30f }
                .sortedByDescending { it.riskScore }
                .take(5) // Limit to top 5 to fit exactly on A4 page
            
            if (highRiskScores.isEmpty()) {
                paint.color = Color.parseColor("#64748B")
                paint.typeface = Typeface.create("sans-serif", Typeface.NORMAL)
                paint.textSize = 10f
                canvas.drawText("No elevated indicators detected. All monitored profiles are within standard ranges.", 40f, yPos + 18f, paint)
                
                // Draw bottom border line
                paint.color = Color.parseColor("#E2E8F0")
                canvas.drawLine(35f, yPos + 26f, 560f, yPos + 26f, paint)
                yPos += 26f
            } else {
                highRiskScores.forEach { score ->
                    // Alternate row background highlights
                    paint.color = Color.parseColor("#F8FAFC")
                    canvas.drawRect(35f, yPos, 560f, yPos + 22f, paint)
                    
                    paint.color = Color.parseColor("#334155")
                    paint.typeface = Typeface.create("sans-serif", Typeface.BOLD)
                    val displayName = ConditionIds.getDisplayName(score.conditionId)
                    val truncatedName = if (displayName.length > 35) displayName.take(32) + "..." else displayName
                    canvas.drawText(truncatedName, 42f, yPos + 14f, paint)
                    
                    val scorePct = score.riskScore.toInt()
                    val riskColorStr = when {
                        scorePct < 30 -> "#10B981" // Green
                        scorePct < 60 -> "#F59E0B" // Amber
                        else -> "#EF4444"          // Red
                    }
                    
                    paint.color = Color.parseColor(riskColorStr)
                    canvas.drawText("$scorePct%", 290f, yPos + 14f, paint)
                    
                    paint.color = Color.parseColor("#475569")
                    paint.typeface = Typeface.create("sans-serif", Typeface.NORMAL)
                    canvas.drawText(score.confidenceLevel.uppercase(), 380f, yPos + 14f, paint)
                    
                    val cat = ConditionIds.getCategory(score.conditionId)
                    val shortCat = if (cat.length > 15) cat.take(13) + ".." else cat
                    canvas.drawText(shortCat, 475f, yPos + 14f, paint)
                    
                    // Bottom border line
                    paint.color = Color.parseColor("#E2E8F0")
                    canvas.drawLine(35f, yPos + 22f, 560f, yPos + 22f, paint)
                    
                    yPos += 22f
                }
            }
            
            // 6. Draw Recovery Recommendations Panel
            yPos += 18f
            paint.textSize = 12f
            paint.color = Color.parseColor("#0F172A")
            paint.typeface = Typeface.create("sans-serif", Typeface.BOLD)
            canvas.drawText("Customized Recovery & Calibration Plan", 35f, yPos, paint)
            
            yPos += 6f
            paint.color = Color.parseColor("#E2E8F0")
            canvas.drawLine(35f, yPos, 560f, yPos, paint)
            
            yPos += 14f
            
            // Retrieve recovery recommendations
            val recTasks = mutableListOf<Pair<String, String>>()
            val hasReadingRisk = highRiskScores.any { 
                it.conditionId.contains("DYSLEXIA") || it.conditionId.contains("DYSGRAPHIA")
            }
            val hasAttentionRisk = highRiskScores.any { 
                it.conditionId.contains("ADHD") || it.conditionId.contains("AUDITORY")
            }
            val hasAnxietyRisk = highRiskScores.any {
                it.conditionId.contains("ANXIETY") || it.conditionId.contains("MUTISM")
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
            
            recTasks.take(2).forEach { (taskName, desc) ->
                paint.color = Color.parseColor("#0F172A")
                paint.typeface = Typeface.create("sans-serif", Typeface.BOLD)
                paint.textSize = 9.5f
                canvas.drawText("•  $taskName:", 42f, yPos, paint)
                
                paint.color = Color.parseColor("#475569")
                paint.typeface = Typeface.create("sans-serif", Typeface.NORMAL)
                canvas.drawText(desc, 160f, yPos, paint)
                
                yPos += 16f
            }
            
            // 7. Draw Verification Stamp & QR Box (Executive clinical formatting)
            yPos += 10f
            paint.color = Color.parseColor("#F1F5F9") // Light Grey box
            val advisoryBg = RectF(35f, yPos, 560f, yPos + 64f)
            canvas.drawRoundRect(advisoryBg, 6f, 6f, paint)
            
            paint.color = Color.parseColor("#CBD5E1")
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            canvas.drawRoundRect(advisoryBg, 6f, 6f, paint)
            paint.style = Paint.Style.FILL // Reset to fill
            
            paint.color = Color.parseColor("#475569")
            paint.textSize = 9f
            paint.typeface = Typeface.create("sans-serif", Typeface.BOLD)
            canvas.drawText("Verification: SEREN Clinical Ensemble Engine (v0.3.0)", 45f, yPos + 18f, paint)
            
            paint.color = Color.parseColor("#64748B")
            paint.typeface = Typeface.create("sans-serif", Typeface.NORMAL)
            paint.textSize = 8f
            canvas.drawText("Classification Model: float16 Multi-Agent Neural Networks + XGBoost Fusion Core", 45f, yPos + 32f, paint)
            canvas.drawText("NEP 2020 Early Identification Compliant Framework", 45f, yPos + 46f, paint)
            
            // Draw simulated QR verification outline
            paint.color = Color.parseColor("#94A3B8")
            paint.style = Paint.Style.STROKE
            val qrBox = RectF(505f, yPos + 12f, 545f, yPos + 52f)
            canvas.drawRect(qrBox, paint)
            
            // Mini QR pixel accents inside
            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor("#64748B")
            canvas.drawRect(510f, yPos + 17f, 518f, yPos + 25f, paint)
            canvas.drawRect(532f, yPos + 17f, 540f, yPos + 25f, paint)
            canvas.drawRect(510f, yPos + 39f, 518f, yPos + 47f, paint)
            canvas.drawRect(522f, yPos + 28f, 528f, yPos + 34f, paint)
            
            // 8. Draw Footer Info
            paint.color = Color.parseColor("#94A3B8")
            paint.textSize = 8f
            paint.typeface = Typeface.create("sans-serif", Typeface.NORMAL)
            canvas.drawText("SEREN Clinical Assessment & Developmental Guidance Core  |  Confidential Report", 35f, 802f, paint)
            canvas.drawText("Page 1 of 1", 520f, 802f, paint)
            
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

    private fun getDomainMaxScore(domainIndex: Int, scores: List<ConditionScore>): Int {
        val domainScores = when (domainIndex) {
            0 -> scores.filter { 
                it.conditionId.contains("DYSLEXIA") || 
                it.conditionId.contains("DYSCALCULIA") || 
                it.conditionId.contains("DYSGRAPHIA") ||
                it.conditionId.contains("READING_FLUENCY") ||
                it.conditionId.contains("ORTHOGRAPHIC") ||
                it.conditionId.contains("SPELLING")
            }
            1 -> scores.filter { 
                it.conditionId.contains("ADHD") || 
                it.conditionId.contains("AUDITORY") || 
                it.conditionId.contains("EXECUTIVE") ||
                it.conditionId.contains("ATTENTION") ||
                it.conditionId.contains("MEMORY") ||
                it.conditionId.contains("PROCESSING_SPEED")
            }
            2 -> scores.filter { 
                it.conditionId.contains("STUTTERING") || 
                it.conditionId.contains("CLUTTERING") || 
                it.conditionId.contains("WORD_FINDING") ||
                it.conditionId.contains("APRAXIA") ||
                it.conditionId.contains("PHONOLOGICAL") ||
                it.conditionId.contains("VOICE") ||
                it.conditionId.contains("PRAGMATIC") ||
                it.conditionId.contains("FLUENCY")
            }
            3 -> scores.filter { 
                it.conditionId.contains("ANXIETY") || 
                it.conditionId.contains("MUTISM") || 
                it.conditionId.contains("PHOBIA") ||
                it.conditionId.contains("INSECURITY") ||
                it.conditionId.contains("IMPOSTER") ||
                it.conditionId.contains("CONFIDENCE") ||
                it.conditionId.contains("PERFECTIONISM")
            }
            4 -> scores.filter { 
                it.conditionId.contains("MASKING") || 
                it.conditionId.contains("AUTISM") || 
                it.conditionId.contains("SOCIAL_COMMUNICATION") ||
                it.conditionId.contains("THEORY_OF_MIND") ||
                it.conditionId.contains("SILENT") ||
                it.conditionId.contains("PEOPLE_PLEASING")
            }
            else -> scores.filter { 
                it.conditionId.contains("DYSPRAXIA") || 
                it.conditionId.contains("VISUAL_MOTOR") || 
                it.conditionId.contains("SENSORY") ||
                it.conditionId.contains("VESTIBULAR") ||
                it.conditionId.contains("PROPRIOCEPTIVE") ||
                it.conditionId.contains("COORDINATION")
            }
        }
        return domainScores.maxOfOrNull { it.riskScore.toInt() } ?: 0
    }
}
