package com.seren.app.data

/**
 * Identifiers and metadata for Batch 1 conditions.
 * Using string constants instead of enums enables smooth additions of Batch 2+
 * without Room schema migrations.
 */
object ConditionIds {
    const val DYSLEXIA = "dyslexia"
    const val DYSGRAPHIA = "dysgraphia"
    const val DYSCALCULIA = "dyscalculia"
    const val ADHD_INATTENTIVE = "adhd_inattentive"
    const val ADHD_HYPERACTIVE = "adhd_hyperactive"
    const val ADHD_COMBINED = "adhd_combined"
    const val STUTTERING = "stuttering"
    const val CLUTTERING = "cluttering"
    const val ANOMIA = "anomia"
    const val APD = "apd"

    val BATCH_1 = listOf(
        DYSLEXIA,
        DYSGRAPHIA,
        DYSCALCULIA,
        ADHD_INATTENTIVE,
        ADHD_HYPERACTIVE,
        ADHD_COMBINED,
        STUTTERING,
        CLUTTERING,
        ANOMIA,
        APD
    )

    fun getDisplayName(id: String): String = when (id) {
        DYSLEXIA -> "Dyslexia"
        DYSGRAPHIA -> "Dysgraphia"
        DYSCALCULIA -> "Dyscalculia"
        ADHD_INATTENTIVE -> "ADHD – Inattentive Presentation"
        ADHD_HYPERACTIVE -> "ADHD – Hyperactive/Impulsive Presentation"
        ADHD_COMBINED -> "ADHD – Combined Presentation"
        STUTTERING -> "Stuttering"
        CLUTTERING -> "Cluttering"
        ANOMIA -> "Word-Finding Difficulty (Anomia)"
        APD -> "Auditory Processing Disorder"
        else -> id.replaceFirstChar { it.uppercase() }
    }

    fun getCategory(id: String): String = when (id) {
        DYSLEXIA, DYSGRAPHIA, DYSCALCULIA -> "Learning Differences"
        ADHD_INATTENTIVE, ADHD_HYPERACTIVE, ADHD_COMBINED, APD -> "Attention & Processing"
        STUTTERING, CLUTTERING, ANOMIA -> "Speech & Language"
        else -> "General"
    }

    fun getDescription(id: String): String = when (id) {
        DYSLEXIA -> "Struggle with phonological processing, slow reading, and visual decoding."
        DYSGRAPHIA -> "Difficulty in fine motor writing skills, consistency, and spatial letter formation."
        DYSCALCULIA -> "Difficulty grasping numerical values, basic calculations, and number sense."
        ADHD_INATTENTIVE -> "Sustained attention deficits, disorganization, and high distractibility."
        ADHD_HYPERACTIVE -> "Deficits in impulse control, response inhibition, and motor restlessness."
        ADHD_COMBINED -> "Co-occurring attentional drift and impulse/hyperactivity indicators."
        STUTTERING -> "Sound repetitions, speech blocks, or prolongations during verbal output."
        CLUTTERING -> "Extremely rapid speech, irregular rhythm, and speech clarity breakdowns."
        ANOMIA -> "Word-finding delays, word substitutions, and circumlocutions under low pressure."
        APD -> "Difficulty separating target speech from noise or processing verbal cues."
        else -> "Neurodevelopmental and cognitive screening profile."
    }
}
