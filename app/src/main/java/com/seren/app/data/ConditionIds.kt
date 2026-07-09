package com.seren.app.data

/**
 * Identifiers and metadata for Batch 1 and Batch 2 conditions.
 * Using string constants instead of enums enables smooth additions of subsequent batches
 * without Room schema migrations.
 */
object ConditionIds {
    // === Batch 1 Conditions ===
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

    // === Batch 2 Conditions ===
    const val SOCIAL_ANXIETY = "social_anxiety"
    const val GAD = "gad"
    const val SELECTIVE_MUTISM = "selective_mutism"
    const val TEST_ANXIETY = "test_anxiety"
    const val SEPARATION_ANXIETY = "separation_anxiety"
    const val SCHOOL_PHOBIA = "school_phobia"
    const val DEPRESSION = "depression"
    const val EMOTIONAL_DYSREGULATION = "emotional_dysregulation"
    const val EXECUTIVE_FUNCTION = "executive_function"
    const val WORKING_MEMORY = "working_memory"

    // === Batch 3 Conditions ===
    const val PROCESSING_SPEED = "processing_speed"
    const val VOICE_DISORDER = "voice_disorder"
    const val EXPRESSIVE_LANGUAGE = "expressive_language"
    const val RECEPTIVE_LANGUAGE = "receptive_language"
    const val PHONOLOGICAL_DISORDER = "phonological_disorder"
    const val APRAXIA_OF_SPEECH = "apraxia_of_speech"
    const val NON_VERBAL_LD = "non_verbal_ld"
    const val TWICE_EXCEPTIONAL = "twice_exceptional"
    const val ADULT_DYSLEXIA = "adult_dyslexia"
    const val ADULT_ADHD = "adult_adhd"

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

    val BATCH_2 = listOf(
        SOCIAL_ANXIETY,
        GAD,
        SELECTIVE_MUTISM,
        TEST_ANXIETY,
        SEPARATION_ANXIETY,
        SCHOOL_PHOBIA,
        DEPRESSION,
        EMOTIONAL_DYSREGULATION,
        EXECUTIVE_FUNCTION,
        WORKING_MEMORY
    )

    val BATCH_3 = listOf(
        PROCESSING_SPEED,
        VOICE_DISORDER,
        EXPRESSIVE_LANGUAGE,
        RECEPTIVE_LANGUAGE,
        PHONOLOGICAL_DISORDER,
        APRAXIA_OF_SPEECH,
        NON_VERBAL_LD,
        TWICE_EXCEPTIONAL,
        ADULT_DYSLEXIA,
        ADULT_ADHD
    )

    val ALL = BATCH_1 + BATCH_2 + BATCH_3

    /**
     * Only conditions with at least one clinically defensible signal source
     * from an existing task screen. Used by ScreeningViewModel for scoring.
     * Conditions without a dedicated task are excluded to avoid "confidently wrong" scores.
     */
    val ACTIVE = listOf(
        // HandwritingTask + ReadingGazeTask
        DYSLEXIA,
        // HandwritingTask
        DYSGRAPHIA,
        // NumberTask
        DYSCALCULIA,
        // AttentionTask (CPT Go/No-Go)
        ADHD_INATTENTIVE,
        ADHD_HYPERACTIVE,
        ADHD_COMBINED,
        // PhonologicalTask + SpeechFluencyTask
        STUTTERING,
        CLUTTERING,
        // PhonologicalTask (RAN)
        ANOMIA,
        APD,
        // === Batch 2 ===
        SOCIAL_ANXIETY,
        GAD,
        SELECTIVE_MUTISM,
        TEST_ANXIETY,
        SEPARATION_ANXIETY,
        SCHOOL_PHOBIA,
        DEPRESSION,
        EMOTIONAL_DYSREGULATION,
        EXECUTIVE_FUNCTION,
        WORKING_MEMORY,

        // NumberTask + AttentionTask (RT-derived)
        PROCESSING_SPEED,
        // ReadingGazeTask (adult variant)
        ADULT_DYSLEXIA,
        // AttentionTask (adult variant)
        ADULT_ADHD,
        // PhonologicalTask (speech sound production)
        PHONOLOGICAL_DISORDER,
        // PhonologicalTask + SpeechFluencyTask (verbal output quality)
        EXPRESSIVE_LANGUAGE,
        // SpeechFluencyTask (acoustic quality)
        VOICE_DISORDER,
        // SpeechFluencyTask (motor speech planning)
        APRAXIA_OF_SPEECH,
        // Batch 3 missing integrations
        RECEPTIVE_LANGUAGE,
        NON_VERBAL_LD,
        TWICE_EXCEPTIONAL
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
        
        SOCIAL_ANXIETY -> "Social Anxiety Disorder"
        GAD -> "Generalised Anxiety Disorder"
        SELECTIVE_MUTISM -> "Selective Mutism"
        TEST_ANXIETY -> "Exam & Test Anxiety"
        SEPARATION_ANXIETY -> "Separation Anxiety Disorder"
        SCHOOL_PHOBIA -> "Specific Phobia (School)"
        DEPRESSION -> "Childhood Depression (Masked)"
        EMOTIONAL_DYSREGULATION -> "Emotional Dysregulation"
        EXECUTIVE_FUNCTION -> "Executive Function Deficit"
        WORKING_MEMORY -> "Working Memory Deficit"
        
        PROCESSING_SPEED -> "Processing Speed Disorder"
        VOICE_DISORDER -> "Voice Disorder"
        EXPRESSIVE_LANGUAGE -> "Expressive Language Disorder"
        RECEPTIVE_LANGUAGE -> "Receptive Language Disorder"
        PHONOLOGICAL_DISORDER -> "Phonological Disorder"
        APRAXIA_OF_SPEECH -> "Childhood Apraxia of Speech"
        NON_VERBAL_LD -> "Non-Verbal Learning Disability"
        TWICE_EXCEPTIONAL -> "Twice-Exceptional Profile (2e)"
        ADULT_DYSLEXIA -> "Adult Dyslexia"
        ADULT_ADHD -> "Adult ADHD"
        
        else -> id.replaceFirstChar { it.uppercase() }
    }

    fun getCategory(id: String): String = when (id) {
        DYSLEXIA, DYSGRAPHIA, DYSCALCULIA, NON_VERBAL_LD, ADULT_DYSLEXIA -> "Learning Differences"
        
        ADHD_INATTENTIVE, ADHD_HYPERACTIVE, ADHD_COMBINED, APD,
        EXECUTIVE_FUNCTION, WORKING_MEMORY, PROCESSING_SPEED, ADULT_ADHD -> "Attention & Processing"
        
        STUTTERING, CLUTTERING, ANOMIA, VOICE_DISORDER, EXPRESSIVE_LANGUAGE, 
        RECEPTIVE_LANGUAGE, PHONOLOGICAL_DISORDER, APRAXIA_OF_SPEECH -> "Speech & Language"
        
        SOCIAL_ANXIETY, GAD, SELECTIVE_MUTISM, TEST_ANXIETY, 
        SEPARATION_ANXIETY, SCHOOL_PHOBIA -> "Anxiety Disorders"
        
        DEPRESSION, EMOTIONAL_DYSREGULATION -> "Emotional & Behavioural"
        
        TWICE_EXCEPTIONAL -> "Silent Profiles"
        
        else -> "General"
    }

    fun getDescription(id: String): String = when (id) {
        DYSLEXIA -> "Struggle with phonological processing, slow reading, and visual decoding."
        DYSGRAPHIA -> "Difficulty in fine motor writing skills, consistency, and letter formation."
        DYSCALCULIA -> "Difficulty grasping numerical values, basic calculations, and number sense."
        ADHD_INATTENTIVE -> "Sustained attention deficits, disorganization, and high distractibility."
        ADHD_HYPERACTIVE -> "Deficits in impulse control, response inhibition, and motor restlessness."
        ADHD_COMBINED -> "Co-occurring attentional drift and impulse/hyperactivity indicators."
        STUTTERING -> "Sound repetitions, speech blocks, or prolongations during verbal output."
        CLUTTERING -> "Extremely rapid speech, irregular rhythm, and speech clarity breakdowns."
        ANOMIA -> "Word-finding delays, word substitutions, and circumlocutions under low pressure."
        APD -> "Difficulty separating target speech from noise or processing verbal cues."
        
        SOCIAL_ANXIETY -> "Intense worry regarding negative evaluation and gaze-aversion during social tasks."
        GAD -> "Excessive overthinking, performance fatigue, and worry-language patterns."
        SELECTIVE_MUTISM -> "Failure to speak in specific social settings despite talking normally at home."
        TEST_ANXIETY -> "Cognitive latency inflation under simulated evaluation or time pressure."
        SEPARATION_ANXIETY -> "Intense distress indicators when parting from parent or guardian figures."
        SCHOOL_PHOBIA -> "Anxious avoidance patterns relating directly to school activities and settings."
        DEPRESSION -> "Psychomotor slowing, persistent low energy, and future-negative lexical tokens."
        EMOTIONAL_DYSREGULATION -> "Rapid random touch responses and frustration patterns on error screens."
        EXECUTIVE_FUNCTION -> "Switch-cost delays and poor planning efficiency on sorting challenges."
        WORKING_MEMORY -> "Reduced spatial recall span during complex block sequence reproduction."
        
        PROCESSING_SPEED -> "Prolonged cognitive decision latency and slower manual action speed."
        VOICE_DISORDER -> "Acoustic instability, tremor, or pitch alterations during sustained vocalization."
        EXPRESSIVE_LANGUAGE -> "Difficulty formulating spoken thoughts, vocabulary limitations, and syntax errors."
        RECEPTIVE_LANGUAGE -> "Difficulty comprehending complex verbal instructions and semantic relationships."
        PHONOLOGICAL_DISORDER -> "Errors in producing speech sounds, substitutions, or cluster simplifications."
        APRAXIA_OF_SPEECH -> "Motor planning deficits causing inconsistent speech sound distortions and pauses."
        NON_VERBAL_LD -> "Deficits in spatial awareness, tactile drawing coordination, and visual-spatial reasoning."
        TWICE_EXCEPTIONAL -> "A combined high intellectual capacity with co-occurring learning or processing challenges."
        ADULT_DYSLEXIA -> "Visual decoding latency, adult phonological processing strategies, and slow reading speed."
        ADULT_ADHD -> "Adult-specific attentional drift, executive scheduling challenges, and impulsivity markers."
        
        else -> "Neurodevelopmental and cognitive screening profile."
    }
}
