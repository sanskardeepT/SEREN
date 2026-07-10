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

    // === Batch 4 Conditions ===
    const val DYSPRAXIA = "dyspraxia"
    const val VMI = "vmi"
    const val ADULT_ANOMIA = "adult_anomia"
    const val ADULT_PROCESSING_SPEED = "adult_processing_speed"
    const val READING_FLUENCY_LAG = "reading_fluency_lag"
    const val ORTHOGRAPHIC_DEFICIT = "orthographic_deficit"
    const val RAN_DEFICIT = "ran_deficit"
    const val SPELLING_DISORDER = "spelling_disorder"
    const val PLACE_VALUE_CONFUSION = "place_value_confusion"
    const val FRACTION_RATIO_DEFICIT = "fraction_ratio_deficit"

    // === Batch 5 Conditions ===
    const val MASKING = "masking"
    const val HFA_MASKED = "hfa_masked"
    const val SOCIAL_COMMUNICATION_DISORDER = "social_communication_disorder"
    const val PRAGMATIC_LANGUAGE = "pragmatic_language"
    const val THEORY_OF_MIND = "theory_of_mind"
    const val SENSORY_PROCESSING = "sensory_processing"
    const val SENSORY_DEFENSIVENESS = "sensory_defensiveness"
    const val VESTIBULAR_DIFFICULTY = "vestibular_difficulty"
    const val PROPRIOCEPTIVE_DIFFICULTY = "proprioceptive_difficulty"
    const val TRAUMA_SILENCE = "trauma_silence"

    // === Batch 6 Conditions ===
    const val BODY_IMAGE_INSECURITY = "body_image_insecurity"
    const val IMPOSTER_SYNDROME = "imposter_syndrome"
    const val REJECTION_SENSITIVITY = "rejection_sensitivity"
    const val WORKPLACE_INSECURITY = "workplace_insecurity"
    const val RELATIONSHIP_INSECURITY = "relationship_insecurity"
    const val FOMO_ANXIETY = "fomo_anxiety"
    const val DIGITAL_INSECURITY = "digital_insecurity"
    const val ACADEMIC_TRAUMA = "academic_trauma"
    const val STUTTERING_CONFIDENCE_DEFICIT = "stuttering_confidence_deficit"
    const val DECISION_PARALYSIS = "decision_paralysis"

    // === Batch 7 Conditions ===
    const val FINANCIAL_INSECURITY = "financial_insecurity"
    const val LEADERSHIP_AVOIDANCE = "leadership_avoidance"
    const val CAREER_STAGNATION = "career_stagnation"
    const val DEEP_ROOTED_SHYNESS = "deep_rooted_shyness"
    const val EXPRESSION_INSECURITY = "expression_insecurity"
    const val SOCIAL_BELONGING_INSECURITY = "social_belonging_insecurity"
    const val PERFORMANCE_INSECURITY = "performance_insecurity"
    const val ANGER_INSECURITY = "anger_insecurity"
    const val FAMILY_ORIGIN_INSECURITY = "family_origin_insecurity"
    const val SELF_CRITICISM = "self_criticism"

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

    val BATCH_4 = listOf(
        DYSPRAXIA,
        VMI,
        ADULT_ANOMIA,
        ADULT_PROCESSING_SPEED,
        READING_FLUENCY_LAG,
        ORTHOGRAPHIC_DEFICIT,
        RAN_DEFICIT,
        SPELLING_DISORDER,
        PLACE_VALUE_CONFUSION,
        FRACTION_RATIO_DEFICIT
    )

    val BATCH_5 = listOf(
        MASKING,
        HFA_MASKED,
        SOCIAL_COMMUNICATION_DISORDER,
        PRAGMATIC_LANGUAGE,
        THEORY_OF_MIND,
        SENSORY_PROCESSING,
        SENSORY_DEFENSIVENESS,
        VESTIBULAR_DIFFICULTY,
        PROPRIOCEPTIVE_DIFFICULTY,
        TRAUMA_SILENCE
    )

    val BATCH_6 = listOf(
        BODY_IMAGE_INSECURITY,
        IMPOSTER_SYNDROME,
        REJECTION_SENSITIVITY,
        WORKPLACE_INSECURITY,
        RELATIONSHIP_INSECURITY,
        FOMO_ANXIETY,
        DIGITAL_INSECURITY,
        ACADEMIC_TRAUMA,
        STUTTERING_CONFIDENCE_DEFICIT,
        DECISION_PARALYSIS
    )

    val BATCH_7 = listOf(
        FINANCIAL_INSECURITY,
        LEADERSHIP_AVOIDANCE,
        CAREER_STAGNATION,
        DEEP_ROOTED_SHYNESS,
        EXPRESSION_INSECURITY,
        SOCIAL_BELONGING_INSECURITY,
        PERFORMANCE_INSECURITY,
        ANGER_INSECURITY,
        FAMILY_ORIGIN_INSECURITY,
        SELF_CRITICISM
    )

    val ALL = BATCH_1 + BATCH_2 + BATCH_3 + BATCH_4 + BATCH_5 + BATCH_6 + BATCH_7

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
        TWICE_EXCEPTIONAL,
        // Batch 4 integrations
        DYSPRAXIA,
        VMI,
        ADULT_ANOMIA,
        ADULT_PROCESSING_SPEED,
        READING_FLUENCY_LAG,
        ORTHOGRAPHIC_DEFICIT,
        RAN_DEFICIT,
        SPELLING_DISORDER,
        PLACE_VALUE_CONFUSION,
        FRACTION_RATIO_DEFICIT,
        // Batch 5 integrations
        MASKING,
        HFA_MASKED,
        SOCIAL_COMMUNICATION_DISORDER,
        PRAGMATIC_LANGUAGE,
        THEORY_OF_MIND,
        SENSORY_PROCESSING,
        SENSORY_DEFENSIVENESS,
        VESTIBULAR_DIFFICULTY,
        PROPRIOCEPTIVE_DIFFICULTY,
        TRAUMA_SILENCE,
        // Batch 6 integrations
        BODY_IMAGE_INSECURITY,
        IMPOSTER_SYNDROME,
        REJECTION_SENSITIVITY,
        WORKPLACE_INSECURITY,
        RELATIONSHIP_INSECURITY,
        FOMO_ANXIETY,
        DIGITAL_INSECURITY,
        ACADEMIC_TRAUMA,
        STUTTERING_CONFIDENCE_DEFICIT,
        DECISION_PARALYSIS,
        // Batch 7 integrations
        FINANCIAL_INSECURITY,
        LEADERSHIP_AVOIDANCE,
        CAREER_STAGNATION,
        DEEP_ROOTED_SHYNESS,
        EXPRESSION_INSECURITY,
        SOCIAL_BELONGING_INSECURITY,
        PERFORMANCE_INSECURITY,
        ANGER_INSECURITY,
        FAMILY_ORIGIN_INSECURITY,
        SELF_CRITICISM
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
        
        DYSPRAXIA -> "Developmental Coordination Disorder (Dyspraxia)"
        VMI -> "Visual-Motor Integration Deficit"
        ADULT_ANOMIA -> "Adult Word-Finding Difficulty"
        ADULT_PROCESSING_SPEED -> "Adult Processing Speed Disorder"
        READING_FLUENCY_LAG -> "Reading Fluency Lag"
        ORTHOGRAPHIC_DEFICIT -> "Orthographic Processing Deficit"
        RAN_DEFICIT -> "RAN Naming Deficit"
        SPELLING_DISORDER -> "Spelling Disorder"
        PLACE_VALUE_CONFUSION -> "Place Value Confusion"
        FRACTION_RATIO_DEFICIT -> "Fraction & Ratio Difficulty"
        
        MASKING -> "Masking and Camouflaging Profile"
        HFA_MASKED -> "Masked High-Functioning Autism Profile"
        SOCIAL_COMMUNICATION_DISORDER -> "Social Communication Disorder"
        PRAGMATIC_LANGUAGE -> "Pragmatic Language Disorder"
        THEORY_OF_MIND -> "Theory of Mind Deficit"
        SENSORY_PROCESSING -> "Sensory Processing Disorder"
        SENSORY_DEFENSIVENESS -> "Sensory Defensiveness Profile"
        VESTIBULAR_DIFFICULTY -> "Vestibular Processing Difficulty"
        PROPRIOCEPTIVE_DIFFICULTY -> "Proprioceptive Processing Difficulty"
        TRAUMA_SILENCE -> "Trauma-Based Silence"
        
        BODY_IMAGE_INSECURITY -> "Body Image Insecurity"
        IMPOSTER_SYNDROME -> "Imposter Syndrome"
        REJECTION_SENSITIVITY -> "Rejection Sensitivity"
        WORKPLACE_INSECURITY -> "Workplace & Career Insecurity"
        RELATIONSHIP_INSECURITY -> "Relationship Insecurity"
        FOMO_ANXIETY -> "FOMO & Social Comparison Anxiety"
        DIGITAL_INSECURITY -> "Digital & Online Social Insecurity"
        ACADEMIC_TRAUMA -> "Academic Trauma"
        STUTTERING_CONFIDENCE_DEFICIT -> "Stuttering-Linked Confidence Deficit"
        DECISION_PARALYSIS -> "Decision Paralysis"
        
        FINANCIAL_INSECURITY -> "Financial Confidence Insecurity"
        LEADERSHIP_AVOIDANCE -> "Leadership Avoidance Pattern"
        CAREER_STAGNATION -> "Career-Stagnation Confidence Collapse"
        DEEP_ROOTED_SHYNESS -> "Deep-Rooted Shyness"
        EXPRESSION_INSECURITY -> "Voice and Expression Insecurity"
        SOCIAL_BELONGING_INSECURITY -> "Social Belonging Insecurity"
        PERFORMANCE_INSECURITY -> "Performance/Achievement Insecurity"
        ANGER_INSECURITY -> "Anger as Hidden Insecurity"
        FAMILY_ORIGIN_INSECURITY -> "Family-Origin Insecurity"
        SELF_CRITICISM -> "Perfectionism-Driven Self-Criticism"
        
        else -> id.replaceFirstChar { it.uppercase() }
    }

    fun getCategory(id: String): String = when (id) {
        DYSLEXIA, DYSGRAPHIA, DYSCALCULIA, NON_VERBAL_LD, ADULT_DYSLEXIA,
        DYSPRAXIA, READING_FLUENCY_LAG, ORTHOGRAPHIC_DEFICIT, SPELLING_DISORDER,
        PLACE_VALUE_CONFUSION, FRACTION_RATIO_DEFICIT -> "Learning Differences"
        
        ADHD_INATTENTIVE, ADHD_HYPERACTIVE, ADHD_COMBINED, APD,
        EXECUTIVE_FUNCTION, WORKING_MEMORY, PROCESSING_SPEED, ADULT_ADHD,
        ADULT_PROCESSING_SPEED -> "Attention & Processing"
        
        STUTTERING, CLUTTERING, ANOMIA, VOICE_DISORDER, EXPRESSIVE_LANGUAGE, 
        RECEPTIVE_LANGUAGE, PHONOLOGICAL_DISORDER, APRAXIA_OF_SPEECH,
        ADULT_ANOMIA, RAN_DEFICIT -> "Speech & Language"
        
        SOCIAL_ANXIETY, GAD, SELECTIVE_MUTISM, TEST_ANXIETY, 
        SEPARATION_ANXIETY, SCHOOL_PHOBIA -> "Anxiety Disorders"
        
        DEPRESSION, EMOTIONAL_DYSREGULATION, TRAUMA_SILENCE -> "Emotional & Behavioural"
        
        TWICE_EXCEPTIONAL, MASKING, HFA_MASKED, SOCIAL_COMMUNICATION_DISORDER, THEORY_OF_MIND -> "Silent Profiles"
        
        VMI, SENSORY_PROCESSING, SENSORY_DEFENSIVENESS, VESTIBULAR_DIFFICULTY, PROPRIOCEPTIVE_DIFFICULTY -> "Sensory & Motor"
        
        BODY_IMAGE_INSECURITY, IMPOSTER_SYNDROME, REJECTION_SENSITIVITY,
        WORKPLACE_INSECURITY, RELATIONSHIP_INSECURITY, FOMO_ANXIETY,
        DIGITAL_INSECURITY, ACADEMIC_TRAUMA, STUTTERING_CONFIDENCE_DEFICIT,
        DECISION_PARALYSIS -> "Insecurities & Confidence Deficits"
        
        FINANCIAL_INSECURITY, LEADERSHIP_AVOIDANCE, CAREER_STAGNATION,
        DEEP_ROOTED_SHYNESS, EXPRESSION_INSECURITY, SOCIAL_BELONGING_INSECURITY,
        PERFORMANCE_INSECURITY, ANGER_INSECURITY, FAMILY_ORIGIN_INSECURITY,
        SELF_CRITICISM -> "Insecurities & Confidence Deficits"
        
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
        
        DYSPRAXIA -> "Difficulty in fine motor writing, touch input coordination, and spatial planning."
        VMI -> "Difficulty translating visual inputs to manual drawing strokes or quick screen selections."
        ADULT_ANOMIA -> "Adult-onset word retrieval delays, speech hesitation, and naming latencies."
        ADULT_PROCESSING_SPEED -> "Prolonged decision latencies and slower reaction speeds in adult cohorts."
        READING_FLUENCY_LAG -> "Reduced reading pacing and fluency delays compared to age norms."
        ORTHOGRAPHIC_DEFICIT -> "Difficulty processing visual forms of words, leading to reading and spelling speed blocks."
        RAN_DEFICIT -> "Delayed automated vocal naming speed of visual stimuli."
        SPELLING_DISORDER -> "Consistent graphic spelling errors, omissions, and motor execution pauses."
        PLACE_VALUE_CONFUSION -> "Confusion of decimal/column representations and comparison latencies of large numbers."
        FRACTION_RATIO_DEFICIT -> "Difficulty estimating and comparing proportions, fractions, or relative amounts."
        
        MASKING -> "Cognitive camouflaging of clinical symptoms via excessive compensatory efforts."
        HFA_MASKED -> "Co-occurring masked autism spectrum indicators with social adaptation profiles."
        SOCIAL_COMMUNICATION_DISORDER -> "Pragmatic difficulties in social communication and conversational timing."
        PRAGMATIC_LANGUAGE -> "Difficulty understanding context-dependent language rules and conversation nuances."
        THEORY_OF_MIND -> "Difficulty processing perspective-taking cues and social intent markers."
        SENSORY_PROCESSING -> "Atypical responsiveness to sensory touch inputs and background stimulation."
        SENSORY_DEFENSIVENESS -> "Averse sensory reactions and visual/auditory distractibility."
        VESTIBULAR_DIFFICULTY -> "Balance, movement coordination, and tactile motor planning deficits."
        PROPRIOCEPTIVE_DIFFICULTY -> "Atypical touch feedback, writing stroke pressure, or spatial coordination."
        TRAUMA_SILENCE -> "Vocal avoidance, low volume, or situational silence linked to emotional stressors."
        
        BODY_IMAGE_INSECURITY -> "Worry or anxiety regarding physical appearance and social feedback."
        IMPOSTER_SYNDROME -> "Cognitive anxiety relating to competence, feeling like a fraud despite success."
        REJECTION_SENSITIVITY -> "Extreme sensitivity to negative evaluation, feedback, or social exclusion."
        WORKPLACE_INSECURITY -> "Anxiety regarding performance, career advancement, and job stability."
        RELATIONSHIP_INSECURITY -> "Interpersonal trust anxiety, attachment concern, and fear of abandonment."
        FOMO_ANXIETY -> "Social comparison anxiety and fear of missing out on peer group activities."
        DIGITAL_INSECURITY -> "Anxiety relating to online presence, social media evaluation, and digital interactions."
        ACADEMIC_TRAUMA -> "Performance fatigue, stress, or avoidance linked to academic evaluative tasks."
        STUTTERING_CONFIDENCE_DEFICIT -> "Reduced verbal confidence, speech avoidance, and expression anxiety."
        DECISION_PARALYSIS -> "Prolonged decision latencies, erasures, and difficulty choosing under low pressure."
        
        FINANCIAL_INSECURITY -> "Anxiety regarding financial stability, literacy, and planning capability."
        LEADERSHIP_AVOIDANCE -> "Avoidance of group facilitation, coordination, or decision-making roles."
        CAREER_STAGNATION -> "Confidence loss relating to professional growth, promotion, or workplace progress."
        DEEP_ROOTED_SHYNESS -> "Avoidance of voluntary social interaction and heightened interpersonal anxiety."
        EXPRESSION_INSECURITY -> "Anxiety regarding vocal presentation, public speaking, or self-expression."
        SOCIAL_BELONGING_INSECURITY -> "Feelings of alienation, exclusion, or fear of not fitting in with peers."
        PERFORMANCE_INSECURITY -> "Extreme performance anxiety and fear of failure during evaluative tasks."
        ANGER_INSECURITY -> "Repressed anger or irritability masking underlying vulnerability and insecurity."
        FAMILY_ORIGIN_INSECURITY -> "Trust issues, low self-worth, or anxiety stemming from family background."
        SELF_CRITICISM -> "Harsh self-evaluation, perfectionistic erasures, and self-blaming language."
        
        else -> "Neurodevelopmental and cognitive screening profile."
    }
}
