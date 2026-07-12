package com.seren.app.data

/**
 * Identifiers and metadata for the complete 84 conditions.
 * Aligned with the SEREN Master Research & Technical Blueprint PDF.
 */
object ConditionIds {
    // === 84 Conditions Constants ===
    const val DYSLEXIA_PHONOLOGICAL = "dyslexia_phonological"
    const val DYSLEXIA_SURFACE = "dyslexia_surface"
    const val DYSLEXIA_MIXED = "dyslexia_mixed"
    const val DYSCALCULIA_CORE = "dyscalculia_core"
    const val DYSCALCULIA_ACCESS = "dyscalculia_access"
    const val DYSGRAPHIA_PHONOLOGICAL = "dysgraphia_phonological"
    const val DYSGRAPHIA_MOTOR = "dysgraphia_motor"
    const val SLD_DYSLEXIA_DYSCALCULIA = "sld_dyslexia_dyscalculia"
    const val SLD_DYSLEXIA_DYSGRAPHIA = "sld_dyslexia_dysgraphia"
    const val ADHD_INATTENTIVE = "adhd_inattentive"
    const val ADHD_HYPERACTIVE = "adhd_hyperactive"
    const val ADHD_COMBINED = "adhd_combined"
    const val ADHD_ADULT = "adhd_adult"
    const val ADHD_DYSLEXIA_COMORBID = "adhd_dyslexia_comorbid"
    const val PROCESSING_SPEED = "processing_speed"
    const val WORKING_MEMORY = "working_memory"
    const val DCD = "dcd"
    const val DYSPRAXIA = "dyspraxia"
    const val VMI = "vmi"
    const val STUTTERING_REPETITIONS = "stuttering_repetitions"
    const val STUTTERING_PROLONGATIONS = "stuttering_prolongations"
    const val STUTTERING_INTERJECTIONS = "stuttering_interjections"
    const val CLUTTERING = "cluttering"
    const val DYSARTHRIA = "dysarthria"
    const val APRAXIA_OF_SPEECH = "apraxia_of_speech"
    const val EXPRESSIVE_LANGUAGE = "expressive_language"
    const val RECEPTIVE_LANGUAGE = "receptive_language"
    const val ANOMIA = "anomia"
    const val PHONOLOGICAL_DISORDER = "phonological_disorder"
    const val APD = "apd"
    const val VOICE_DISORDER = "voice_disorder"
    const val GAD = "gad"
    const val SOCIAL_ANXIETY = "social_anxiety"
    const val SEPARATION_ANXIETY = "separation_anxiety"
    const val TEST_ANXIETY = "test_anxiety"
    const val SELECTIVE_MUTISM = "selective_mutism"
    const val SCHOOL_PHOBIA = "school_phobia"
    const val DEPRESSION = "depression"
    const val SOCIAL_WITHDRAWAL = "social_withdrawal"
    const val INTROVERSION_SUPPRESSION = "introversion_suppression"
    const val HOME_STRESS = "home_stress"
    const val BULLYING_VICTIMISATION = "bullying_victimisation"
    const val TRAUMA_RESPONSE = "trauma_response"
    const val ASD_SOCIAL = "asd_social"
    const val ASD_COMMUNICATION = "asd_communication"
    const val ASD_SENSORY = "asd_sensory"
    const val TWICE_EXCEPTIONAL = "twice_exceptional"
    const val HIDDEN_SPATIAL = "hidden_spatial"
    const val HIDDEN_PATTERN = "hidden_pattern"
    const val KINESTHETIC_INTEL = "kinesthetic_intel"
    const val VERBAL_IQ_SUPPRESSED = "verbal_iq_suppressed"
    const val MATHEMATICAL_ANXIETY = "mathematical_anxiety"
    const val FACT_RETRIEVAL = "fact_retrieval"
    const val PLACE_VALUE_CONFUSION = "place_value_confusion"
    const val FRACTION_RATIO_DEFICIT = "fraction_ratio_deficit"
    const val READING_COMPREHENSION = "reading_comprehension"
    const val RAN_DEFICIT = "ran_deficit"
    const val ORTHOGRAPHIC_PROCESSING = "orthographic_processing"
    const val READING_FLUENCY = "reading_fluency"
    const val SPELLING_DISORDER = "spelling_disorder"
    const val EXECUTIVE_PLANNING = "executive_planning"
    const val RESPONSE_INHIBITION = "response_inhibition"
    const val COGNITIVE_FLEXIBILITY = "cognitive_flexibility"
    const val VERBAL_WORKING_MEMORY = "verbal_working_memory"
    const val VISUAL_SPATIAL_MEM = "visual_spatial_mem"
    const val AUDITORY_DISCRIMINATION = "auditory_discrimination"
    const val VISUAL_DISCRIMINATION = "visual_discrimination"
    const val TACTILE_PROCESSING = "tactile_processing"
    const val PRAGMATIC_LANGUAGE = "pragmatic_language"
    const val THEORY_OF_MIND = "theory_of_mind"
    const val NON_VERBAL_LD = "non_verbal_ld"
    const val EMOTIONAL_DYSREGULATION = "emotional_dysregulation"
    const val LOW_FRUSTRATION_TOLERANCE = "low_frustration_tolerance"
    const val IMPULSIVITY_NON_ADHD = "impulsivity_non_adhd"
    const val PERFECTIONISM = "perfectionism"
    const val FINE_MOTOR_DELAY = "fine_motor_delay"
    const val GROSS_MOTOR_DELAY = "gross_motor_delay"
    const val HANDEDNESS_CONFUSION = "handedness_confusion"
    const val ADULT_DYSLEXIA = "adult_dyslexia"
    const val ADULT_DYSCALCULIA = "adult_dyscalculia"
    const val ADULT_ADHD = "adult_adhd"
    const val ADULT_ANOMIA = "adult_anomia"
    const val ADULT_READING_COMPREHENSION = "adult_reading_comprehension"
    const val ADULT_PROCESSING_SPEED = "adult_processing_speed"

    // === Insecurities Constants ===
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
    const val PUBLIC_SPEAKING_PHOBIA = "public_speaking_phobia"
    const val PDA = "pda"
    const val ALEXITHYMIA = "alexithymia"
    const val HSP_OVERWHELM = "hsp_overwhelm"
    const val FAWN_RESPONSE = "fawn_response"

    val ALL = listOf(
        DYSLEXIA_PHONOLOGICAL, DYSLEXIA_SURFACE, DYSLEXIA_MIXED, DYSCALCULIA_CORE, DYSCALCULIA_ACCESS,
        DYSGRAPHIA_PHONOLOGICAL, DYSGRAPHIA_MOTOR, SLD_DYSLEXIA_DYSCALCULIA, SLD_DYSLEXIA_DYSGRAPHIA,
        ADHD_INATTENTIVE, ADHD_HYPERACTIVE, ADHD_COMBINED, ADHD_ADULT, ADHD_DYSLEXIA_COMORBID,
        PROCESSING_SPEED, WORKING_MEMORY, DCD, DYSPRAXIA, VMI, STUTTERING_REPETITIONS,
        STUTTERING_PROLONGATIONS, STUTTERING_INTERJECTIONS, CLUTTERING, DYSARTHRIA, APRAXIA_OF_SPEECH,
        EXPRESSIVE_LANGUAGE, RECEPTIVE_LANGUAGE, ANOMIA, PHONOLOGICAL_DISORDER, APD, VOICE_DISORDER,
        GAD, SOCIAL_ANXIETY, SEPARATION_ANXIETY, TEST_ANXIETY, SELECTIVE_MUTISM, SCHOOL_PHOBIA,
        DEPRESSION, SOCIAL_WITHDRAWAL, INTROVERSION_SUPPRESSION, HOME_STRESS, BULLYING_VICTIMISATION,
        TRAUMA_RESPONSE, ASD_SOCIAL, ASD_COMMUNICATION, ASD_SENSORY, TWICE_EXCEPTIONAL,
        HIDDEN_SPATIAL, HIDDEN_PATTERN, KINESTHETIC_INTEL, VERBAL_IQ_SUPPRESSED, MATHEMATICAL_ANXIETY,
        FACT_RETRIEVAL, PLACE_VALUE_CONFUSION, FRACTION_RATIO_DEFICIT, READING_COMPREHENSION,
        RAN_DEFICIT, ORTHOGRAPHIC_PROCESSING, READING_FLUENCY, SPELLING_DISORDER, EXECUTIVE_PLANNING,
        RESPONSE_INHIBITION, COGNITIVE_FLEXIBILITY, VERBAL_WORKING_MEMORY, VISUAL_SPATIAL_MEM,
        AUDITORY_DISCRIMINATION, VISUAL_DISCRIMINATION, TACTILE_PROCESSING, PRAGMATIC_LANGUAGE,
        THEORY_OF_MIND, NON_VERBAL_LD, EMOTIONAL_DYSREGULATION, LOW_FRUSTRATION_TOLERANCE,
        IMPULSIVITY_NON_ADHD, PERFECTIONISM, FINE_MOTOR_DELAY, GROSS_MOTOR_DELAY, HANDEDNESS_CONFUSION,
        ADULT_DYSLEXIA, ADULT_DYSCALCULIA, ADULT_ADHD, ADULT_ANOMIA, ADULT_READING_COMPREHENSION,
        ADULT_PROCESSING_SPEED, BODY_IMAGE_INSECURITY, IMPOSTER_SYNDROME, REJECTION_SENSITIVITY,
        WORKPLACE_INSECURITY, RELATIONSHIP_INSECURITY, FOMO_ANXIETY, DIGITAL_INSECURITY,
        ACADEMIC_TRAUMA, STUTTERING_CONFIDENCE_DEFICIT, DECISION_PARALYSIS, FINANCIAL_INSECURITY,
        LEADERSHIP_AVOIDANCE, CAREER_STAGNATION, DEEP_ROOTED_SHYNESS, EXPRESSION_INSECURITY,
        SOCIAL_BELONGING_INSECURITY, PERFORMANCE_INSECURITY, ANGER_INSECURITY, FAMILY_ORIGIN_INSECURITY,
    )

    val BATCH1_AND_2 = listOf(
        DYSLEXIA_PHONOLOGICAL, DYSLEXIA_SURFACE, DYSLEXIA_MIXED, DYSCALCULIA_CORE, DYSCALCULIA_ACCESS,
        DYSGRAPHIA_PHONOLOGICAL, DYSGRAPHIA_MOTOR, SLD_DYSLEXIA_DYSCALCULIA, SLD_DYSLEXIA_DYSGRAPHIA,
        ADHD_INATTENTIVE, ADHD_HYPERACTIVE, ADHD_COMBINED, ADHD_ADULT, ADHD_DYSLEXIA_COMORBID,
        PROCESSING_SPEED, WORKING_MEMORY, DCD, DYSPRAXIA, VMI, STUTTERING_REPETITIONS,
        STUTTERING_PROLONGATIONS, STUTTERING_INTERJECTIONS, CLUTTERING, DYSARTHRIA, APRAXIA_OF_SPEECH,
        EXPRESSIVE_LANGUAGE, RECEPTIVE_LANGUAGE, ANOMIA, PHONOLOGICAL_DISORDER, APD, VOICE_DISORDER,
        GAD, SOCIAL_ANXIETY, SEPARATION_ANXIETY, TEST_ANXIETY, SELECTIVE_MUTISM, SCHOOL_PHOBIA,
        DEPRESSION, SOCIAL_WITHDRAWAL, INTROVERSION_SUPPRESSION, HOME_STRESS, BULLYING_VICTIMISATION,
        TRAUMA_RESPONSE, ASD_SOCIAL, ASD_COMMUNICATION, ASD_SENSORY, TWICE_EXCEPTIONAL,
        HIDDEN_SPATIAL, HIDDEN_PATTERN, KINESTHETIC_INTEL, VERBAL_IQ_SUPPRESSED, MATHEMATICAL_ANXIETY,
        FACT_RETRIEVAL, PLACE_VALUE_CONFUSION, FRACTION_RATIO_DEFICIT, READING_COMPREHENSION,
        RAN_DEFICIT, ORTHOGRAPHIC_PROCESSING, READING_FLUENCY, SPELLING_DISORDER, EXECUTIVE_PLANNING,
        RESPONSE_INHIBITION, COGNITIVE_FLEXIBILITY, VERBAL_WORKING_MEMORY, VISUAL_SPATIAL_MEM,
        AUDITORY_DISCRIMINATION, VISUAL_DISCRIMINATION, TACTILE_PROCESSING, PRAGMATIC_LANGUAGE,
        THEORY_OF_MIND, NON_VERBAL_LD, EMOTIONAL_DYSREGULATION, LOW_FRUSTRATION_TOLERANCE,
        IMPULSIVITY_NON_ADHD, PERFECTIONISM, FINE_MOTOR_DELAY, GROSS_MOTOR_DELAY, HANDEDNESS_CONFUSION,
        ADULT_DYSLEXIA, ADULT_DYSCALCULIA, ADULT_ADHD, ADULT_ANOMIA, ADULT_READING_COMPREHENSION,
        ADULT_PROCESSING_SPEED
    )

    val INSECURITIES = listOf(
        BODY_IMAGE_INSECURITY, IMPOSTER_SYNDROME, REJECTION_SENSITIVITY, WORKPLACE_INSECURITY,
        RELATIONSHIP_INSECURITY, FOMO_ANXIETY, DIGITAL_INSECURITY, ACADEMIC_TRAUMA,
        STUTTERING_CONFIDENCE_DEFICIT, DECISION_PARALYSIS, FINANCIAL_INSECURITY, LEADERSHIP_AVOIDANCE,
        CAREER_STAGNATION, DEEP_ROOTED_SHYNESS, EXPRESSION_INSECURITY, SOCIAL_BELONGING_INSECURITY,
        PERFORMANCE_INSECURITY, ANGER_INSECURITY, FAMILY_ORIGIN_INSECURITY, SELF_CRITICISM,
        PUBLIC_SPEAKING_PHOBIA, PDA, ALEXITHYMIA, HSP_OVERWHELM, FAWN_RESPONSE
    )

    val ACTIVE = BATCH1_AND_2

    fun getDisplayName(id: String): String = when (id) {
        DYSLEXIA_PHONOLOGICAL -> "Dyslexia (Phonological)"
        DYSLEXIA_SURFACE -> "Dyslexia (Surface)"
        DYSLEXIA_MIXED -> "Dyslexia (Mixed)"
        DYSCALCULIA_CORE -> "Dyscalculia (Core)"
        DYSCALCULIA_ACCESS -> "Dyscalculia (Access)"
        DYSGRAPHIA_PHONOLOGICAL -> "Dysgraphia (Phonological)"
        DYSGRAPHIA_MOTOR -> "Dysgraphia (Motor)"
        SLD_DYSLEXIA_DYSCALCULIA -> "Mixed SLD (Dyslexia + Dyscalculia)"
        SLD_DYSLEXIA_DYSGRAPHIA -> "Mixed SLD (Dyslexia + Dysgraphia)"
        ADHD_INATTENTIVE -> "ADHD – Inattentive Presentation"
        ADHD_HYPERACTIVE -> "ADHD – Hyperactive/Impulsive Presentation"
        ADHD_COMBINED -> "ADHD – Combined Presentation"
        ADHD_ADULT -> "Adult ADHD"
        ADHD_DYSLEXIA_COMORBID -> "ADHD + Dyslexia Comorbidity"
        PROCESSING_SPEED -> "Processing Speed Disorder"
        WORKING_MEMORY -> "Working Memory Deficit"
        DCD -> "Developmental Coordination Disorder (DCD)"
        DYSPRAXIA -> "Dyspraxia (Planning)"
        VMI -> "Visual-Motor Integration Deficit"
        STUTTERING_REPETITIONS -> "Stuttering (Repetitions)"
        STUTTERING_PROLONGATIONS -> "Stuttering (Prolongations)"
        STUTTERING_INTERJECTIONS -> "Stuttering (Interjections)"
        CLUTTERING -> "Cluttering"
        DYSARTHRIA -> "Dysarthria"
        APRAXIA_OF_SPEECH -> "Childhood Apraxia of Speech"
        EXPRESSIVE_LANGUAGE -> "Expressive Language Disorder"
        RECEPTIVE_LANGUAGE -> "Receptive Language Disorder"
        ANOMIA -> "Word-Finding Difficulty (Anomia)"
        PHONOLOGICAL_DISORDER -> "Phonological Disorder"
        APD -> "Auditory Processing Disorder"
        VOICE_DISORDER -> "Voice Disorder"
        GAD -> "Generalised Anxiety Disorder (GAD)"
        SOCIAL_ANXIETY -> "Social Anxiety Disorder"
        SEPARATION_ANXIETY -> "Separation Anxiety Disorder"
        TEST_ANXIETY -> "Exam & Test Anxiety"
        SELECTIVE_MUTISM -> "Selective Mutism"
        SCHOOL_PHOBIA -> "Specific Phobia (School)"
        DEPRESSION -> "Childhood Depression Indicators"
        SOCIAL_WITHDRAWAL -> "Social Withdrawal"
        INTROVERSION_SUPPRESSION -> "Introversion-Driven Academic Suppression"
        HOME_STRESS -> "Home Environment Stress"
        BULLYING_VICTIMISATION -> "Bullying Victimisation"
        TRAUMA_RESPONSE -> "Trauma Response"
        ASD_SOCIAL -> "Autism Spectrum Disorder (Social)"
        ASD_COMMUNICATION -> "Autism Spectrum Disorder (Communication)"
        ASD_SENSORY -> "Autism Spectrum Disorder (Sensory)"
        TWICE_EXCEPTIONAL -> "Twice-Exceptional Profile (2e)"
        HIDDEN_SPATIAL -> "Hidden Spatial Intelligence"
        HIDDEN_PATTERN -> "Hidden Pattern Recognition"
        KINESTHETIC_INTEL -> "Kinesthetic Intelligence"
        VERBAL_IQ_SUPPRESSED -> "Verbal IQ Suppressed by Language Barrier"
        MATHEMATICAL_ANXIETY -> "Mathematical Anxiety"
        FACT_RETRIEVAL -> "Arithmetic Fact Retrieval (Isolated)"
        PLACE_VALUE_CONFUSION -> "Place Value Confusion"
        FRACTION_RATIO_DEFICIT -> "Fraction & Ratio Difficulty"
        READING_COMPREHENSION -> "Reading Comprehension (No Decoding Deficit)"
        RAN_DEFICIT -> "RAN Naming Deficit"
        ORTHOGRAPHIC_PROCESSING -> "Orthographic Processing Deficit"
        READING_FLUENCY -> "Reading Fluency (No Accuracy Deficit)"
        SPELLING_DISORDER -> "Spelling Disorder"
        EXECUTIVE_PLANNING -> "Executive Function: Planning"
        RESPONSE_INHIBITION -> "Response Inhibition Deficit"
        COGNITIVE_FLEXIBILITY -> "Cognitive Flexibility Deficit"
        VERBAL_WORKING_MEMORY -> "Verbal Working Memory"
        VISUAL_SPATIAL_MEM -> "Visual-Spatial Working Memory"
        AUDITORY_DISCRIMINATION -> "Auditory Discrimination"
        VISUAL_DISCRIMINATION -> "Visual Discrimination"
        TACTILE_PROCESSING -> "Tactile Processing"
        PRAGMATIC_LANGUAGE -> "Pragmatic Language Disorder"
        THEORY_OF_MIND -> "Theory of Mind Deficit"
        NON_VERBAL_LD -> "Non-Verbal Learning Disability"
        EMOTIONAL_DYSREGULATION -> "Emotional Dysregulation"
        LOW_FRUSTRATION_TOLERANCE -> "Low Frustration Tolerance"
        IMPULSIVITY_NON_ADHD -> "Impulsivity (Non-ADHD)"
        PERFECTIONISM -> "Perfectionism (Maladaptive)"
        FINE_MOTOR_DELAY -> "Fine Motor Delay"
        GROSS_MOTOR_DELAY -> "Gross Motor Delay"
        HANDEDNESS_CONFUSION -> "Handedness Confusion"
        ADULT_DYSLEXIA -> "Adult Dyslexia"
        ADULT_DYSCALCULIA -> "Adult Dyscalculia"
        ADULT_ADHD -> "Adult ADHD"
        ADULT_ANOMIA -> "Adult Word-Finding"
        ADULT_READING_COMPREHENSION -> "Reading Comprehension (Adult)"
        ADULT_PROCESSING_SPEED -> "Adult Processing Speed"
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
        PUBLIC_SPEAKING_PHOBIA -> "Public Speaking Phobia"
        PDA -> "Pathological Demand Avoidance"
        ALEXITHYMIA -> "Alexithymia"
        HSP_OVERWHELM -> "Highly Sensitive Person Overwhelm"
        FAWN_RESPONSE -> "Chronic People-Pleasing (Fawn Response)"
        else -> id.replaceFirstChar { it.uppercase() }
    }

    fun getCategory(id: String): String = when (id) {
        DYSLEXIA_PHONOLOGICAL, DYSLEXIA_SURFACE, DYSLEXIA_MIXED, DYSCALCULIA_CORE, DYSCALCULIA_ACCESS,
        DYSGRAPHIA_PHONOLOGICAL, DYSGRAPHIA_MOTOR, SLD_DYSLEXIA_DYSCALCULIA, SLD_DYSLEXIA_DYSGRAPHIA,
        NON_VERBAL_LD, ADULT_DYSLEXIA, ADULT_DYSCALCULIA, DCD, DYSPRAXIA, VMI, READING_FLUENCY,
        SPELLING_DISORDER, PLACE_VALUE_CONFUSION, FRACTION_RATIO_DEFICIT -> "Learning Differences"

        ADHD_INATTENTIVE, ADHD_HYPERACTIVE, ADHD_COMBINED, ADHD_ADULT, ADHD_DYSLEXIA_COMORBID, APD,
        WORKING_MEMORY, PROCESSING_SPEED, ADULT_ADHD, ADULT_PROCESSING_SPEED, EXECUTIVE_PLANNING,
        RESPONSE_INHIBITION, COGNITIVE_FLEXIBILITY, VERBAL_WORKING_MEMORY, VISUAL_SPATIAL_MEM,
        AUDITORY_DISCRIMINATION, VISUAL_DISCRIMINATION, TACTILE_PROCESSING -> "Attention & Processing"

        STUTTERING_REPETITIONS, STUTTERING_PROLONGATIONS, STUTTERING_INTERJECTIONS, CLUTTERING,
        DYSARTHRIA, APRAXIA_OF_SPEECH, EXPRESSIVE_LANGUAGE, RECEPTIVE_LANGUAGE, ANOMIA,
        PHONOLOGICAL_DISORDER, VOICE_DISORDER, PRAGMATIC_LANGUAGE, ADULT_ANOMIA -> "Speech & Language"

        GAD, SOCIAL_ANXIETY, SEPARATION_ANXIETY, TEST_ANXIETY, SELECTIVE_MUTISM, SCHOOL_PHOBIA,
        MATHEMATICAL_ANXIETY -> "Anxiety Disorders"

        DEPRESSION, EMOTIONAL_DYSREGULATION, LOW_FRUSTRATION_TOLERANCE, IMPULSIVITY_NON_ADHD,
        PERFECTIONISM, FINE_MOTOR_DELAY, GROSS_MOTOR_DELAY, HANDEDNESS_CONFUSION, SOCIAL_WITHDRAWAL,
        HOME_STRESS, BULLYING_VICTIMISATION, TRAUMA_RESPONSE -> "Emotional & Behavioural"

        TWICE_EXCEPTIONAL, ASD_SOCIAL, ASD_COMMUNICATION, ASD_SENSORY, HIDDEN_SPATIAL, HIDDEN_PATTERN,
        KINESTHETIC_INTEL, VERBAL_IQ_SUPPRESSED, THEORY_OF_MIND -> "Silent Profiles"

        BODY_IMAGE_INSECURITY, IMPOSTER_SYNDROME, REJECTION_SENSITIVITY, WORKPLACE_INSECURITY,
        RELATIONSHIP_INSECURITY, FOMO_ANXIETY, DIGITAL_INSECURITY, ACADEMIC_TRAUMA,
        STUTTERING_CONFIDENCE_DEFICIT, DECISION_PARALYSIS, FINANCIAL_INSECURITY, LEADERSHIP_AVOIDANCE,
        CAREER_STAGNATION, DEEP_ROOTED_SHYNESS, EXPRESSION_INSECURITY, SOCIAL_BELONGING_INSECURITY,
        PERFORMANCE_INSECURITY, ANGER_INSECURITY, FAMILY_ORIGIN_INSECURITY, SELF_CRITICISM,
        PUBLIC_SPEAKING_PHOBIA, PDA, ALEXITHYMIA, HSP_OVERWHELM, FAWN_RESPONSE -> "Insecurities & Confidence Deficits"

        else -> "General"
    }

    fun getDescription(id: String): String = when (id) {
        DYSLEXIA_PHONOLOGICAL -> "Struggle with mapping sounds to letters and phonological processing deficits."
        DYSLEXIA_SURFACE -> "Difficulty processing visual forms of words and sight-word reading speed."
        DYSLEXIA_MIXED -> "Combined deficits in phonological processing and visual orthographic word decoding."
        DYSCALCULIA_CORE -> "Atypical number sense, subitizing, and basic arithmetic fact retrieval deficits."
        DYSCALCULIA_ACCESS -> "Difficulty mapping numerical symbols to spatial quantities."
        DYSGRAPHIA_PHONOLOGICAL -> "Difficulty spelling and mapping spoken words to written graphemes."
        DYSGRAPHIA_MOTOR -> "Fine-motor writing delay, poor finger stroke consistency and layout alignment."
        SLD_DYSLEXIA_DYSCALCULIA -> "Co-occurring reading processing and mathematical counting deficits."
        SLD_DYSLEXIA_DYSGRAPHIA -> "Co-occurring reading processing and motor copy-writing deficits."
        ADHD_INATTENTIVE -> "Sustained attention deficits, disorganization, and high distractibility."
        ADHD_HYPERACTIVE -> "Deficits in impulse control, response inhibition, and motor restlessness."
        ADHD_COMBINED -> "Co-occurring attentional drift and impulse/hyperactivity indicators."
        ADHD_ADULT -> "Adult attentional drift, executive management difficulty, and restlessness."
        ADHD_DYSLEXIA_COMORBID -> "Co-occurring attention deficit and phonological reading latency."
        PROCESSING_SPEED -> "Slower manual touch reaction speed and prolonged cognitive action times."
        WORKING_MEMORY -> "Reduced digit-span recall and spatial sequence memory spans."
        DCD -> "Motor clumsiness, fine coordination delay, and writing difficulty."
        DYSPRAXIA -> "Difficulty planning and executing motor task sequences."
        VMI -> "Difficulty translating visual shapes to manual copying/drawing actions."
        STUTTERING_REPETITIONS -> "Involuntary repetitions of syllables or sounds during vocal reading."
        STUTTERING_PROLONGATIONS -> "Involuntary prolongations of speech sounds during reading."
        STUTTERING_INTERJECTIONS -> "Frequent interjections and vocal breaks in spoken sentences."
        CLUTTERING -> "Extremely rapid speech, irregular rhythm, and speech clarity breakdowns."
        DYSARTHRIA -> "Slurred speech, poor articulation, and speech volume control issues."
        APRAXIA_OF_SPEECH -> "Speech motor planning delays causing sound distortions."
        EXPRESSIVE_LANGUAGE -> "Vocabulary limits, structural syntax errors, and verbal output difficulty."
        RECEPTIVE_LANGUAGE -> "Difficulty comprehending complex verbal instructions and instructions."
        ANOMIA -> "Word-finding delays and verbal naming pause durations."
        PHONOLOGICAL_DISORDER -> "Sound production errors and speech substitution patterns."
        APD -> "Auditory processing delays in identifying speech in background noise."
        VOICE_DISORDER -> "Acoustic voice tremor, pitch instability, or vocal fatigue."
        GAD -> "Excessive overthinking, performance fatigue, and worry-language patterns."
        SOCIAL_ANXIETY -> "Intense worry regarding social evaluation and gaze-aversion during tasks."
        SEPARATION_ANXIETY -> "Intense distress indicators when parting from parent or guardian figures."
        TEST_ANXIETY -> "Cognitive latency inflation under simulated evaluation or time pressure."
        SELECTIVE_MUTISM -> "Failure to speak in specific social settings despite talking normally at home."
        SCHOOL_PHOBIA -> "Anxious avoidance patterns relating directly to school activities."
        DEPRESSION -> "Psychomotor slowing, low energy, and negative self-talk patterns."
        SOCIAL_WITHDRAWAL -> "Avoidance of oral participation and performance withdrawal."
        INTROVERSION_SUPPRESSION -> "Introversion-driven silence or adaptation leading to underestimation of capacity."
        HOME_STRESS -> "Structured stress indicators and tension within the home environment."
        BULLYING_VICTIMISATION -> "High stress-abandonment spike and withdrawal patterns."
        TRAUMA_RESPONSE -> "Trauma response, startle response, and emotional shutdown."
        ASD_SOCIAL -> "Atypical social interaction patterns and communication adaptation profiles."
        ASD_COMMUNICATION -> "Pragmatic difficulties in conversational timing and adaptation."
        ASD_SENSORY -> "Atypical responsiveness to sensory touch inputs and background stimulation."
        TWICE_EXCEPTIONAL -> "Combined high intellectual capacity with co-occurring learning differences."
        HIDDEN_SPATIAL -> "Strong visual-spatial reasoning and matrix completion capabilities."
        HIDDEN_PATTERN -> "Rapid visual pattern sequence recognition and logic completion."
        KINESTHETIC_INTEL -> "High motor copy accuracy despite lower academic test metrics."
        VERBAL_IQ_SUPPRESSED -> "Speech comprehension vs. oral expression output gap."
        MATHEMATICAL_ANXIETY -> "Performance collapse specifically during timed math exercises."
        FACT_RETRIEVAL -> "Prolonged fact-retrieval times without visual magnitude deficits."
        PLACE_VALUE_CONFUSION -> "Confusion of decimal/column values and number comparison times."
        FRACTION_RATIO_DEFICIT -> "Difficulty estimating and comparing visual proportions."
        READING_COMPREHENSION -> "Difficulty answering text comprehension queries despite quick reading speeds."
        RAN_DEFICIT -> "Delayed naming speed of visual blocks and colors."
        ORTHOGRAPHIC_PROCESSING -> "Difficulty processing visual forms of words and letters."
        READING_FLUENCY -> "Reduced reading pacing compared to age-stratified norms."
        SPELLING_DISORDER -> "Consistent graphic spelling omissions, errors, and motor pauses."
        EXECUTIVE_PLANNING -> "Multi-step sequencing planning speed and task optimization limits."
        RESPONSE_INHIBITION -> "Go/No-Go commission error rate and inhibition control deficits."
        COGNITIVE_FLEXIBILITY -> "Task-switching latency switch cost delays."
        VERBAL_WORKING_MEMORY -> "Struggle repeating sentences or recalling auditory digit spans."
        VISUAL_SPATIAL_MEM -> "Spatial sequence block recall span limitations."
        AUDITORY_DISCRIMINATION -> "Phoneme pair discrimination response times."
        VISUAL_DISCRIMINATION -> "Shape comparison matching speed and fixation duration."
        TACTILE_PROCESSING -> "Atypical touchscreen pressure variance."
        PRAGMATIC_LANGUAGE -> "Difficulty understanding context-dependent language regulations."
        THEORY_OF_MIND -> "Difficulty processing perspective-taking and social cues."
        NON_VERBAL_LD -> "Deficits in spatial coordination and drawing alignment."
        EMOTIONAL_DYSREGULATION -> "Emotional dysregulation and task frustration indicators."
        LOW_FRUSTRATION_TOLERANCE -> "Error-triggered task abandonment rates."
        IMPULSIVITY_NON_ADHD -> "Inhibition control errors without attention drift."
        PERFECTIONISM -> "Frequent self-correction, erase counts, and latency on easy items."
        FINE_MOTOR_DELAY -> "Stroke velocity delay and writing coordination struggles."
        GROSS_MOTOR_DELAY -> "Motor coordination delay and spatial planning difficulty."
        HANDEDNESS_CONFUSION -> "Inconsistent hand stroke coordinates and writing direction."
        ADULT_DYSLEXIA -> "Visual decoding latency and slow professional reading pacing."
        ADULT_DYSCALCULIA -> "Adult math comparison and calculation delays."
        ADULT_ADHD -> "Adult attention drift, executive scheduling blocks, and impulsivity."
        ADULT_ANOMIA -> "Adult word-finding delays and conversation pauses."
        ADULT_READING_COMPREHENSION -> "Adult reading comprehension checks."
        ADULT_PROCESSING_SPEED -> "Adult decision latency and reaction speeds."
        BODY_IMAGE_INSECURITY -> "Worry regarding physical appearance and social feedback."
        IMPOSTER_SYNDROME -> "Insecurity relating to competence, feeling like a fraud despite success."
        REJECTION_SENSITIVITY -> "Extreme sensitivity to negative evaluation, feedback, or exclusion."
        WORKPLACE_INSECURITY -> "Anxiety regarding performance, career growth, and job stability."
        RELATIONSHIP_INSECURITY -> "Interpersonal trust anxiety and fear of abandonment."
        FOMO_ANXIETY -> "Social comparison anxiety and fear of missing out."
        DIGITAL_INSECURITY -> "Anxiety relating to online presence and social media evaluation."
        ACADEMIC_TRAUMA -> "Stress or avoidance linked to academic evaluative tasks."
        STUTTERING_CONFIDENCE_DEFICIT -> "Reduced verbal confidence and expression anxiety."
        DECISION_PARALYSIS -> "Prolonged decision latencies under choice options."
        FINANCIAL_INSECURITY -> "Anxiety regarding financial planning and stability."
        LEADERSHIP_AVOIDANCE -> "Avoidance of group facilitation or decision-making roles."
        CAREER_STAGNATION -> "Confidence loss relating to professional growth."
        DEEP_ROOTED_SHYNESS -> "Avoidance of voluntary social interaction and shyness."
        EXPRESSION_INSECURITY -> "Voice and self-expression insecurity."
        SOCIAL_BELONGING_INSECURITY -> "Feelings of alienation or fear of not fitting in."
        PERFORMANCE_INSECURITY -> "Extreme performance anxiety and fear of failure."
        ANGER_INSECURITY -> "Repressed anger or irritability masking underlying vulnerability."
        FAMILY_ORIGIN_INSECURITY -> "Trust issues or anxiety stemming from family background."
        SELF_CRITICISM -> "Harsh self-evaluation and perfectionistic self-criticism."
        PUBLIC_SPEAKING_PHOBIA -> "Severe communication anxiety relating to public speaking."
        PDA -> "Obsessive demand avoidance driven by an anxiety response."
        ALEXITHYMIA -> "Difficulty identifying, describing, and expressing own emotions."
        HSP_OVERWHELM -> "Hyper-responsiveness to sensory stimuli and environmental inputs."
        FAWN_RESPONSE -> "Chronic people-pleasing and boundary deficits."
        else -> id.replaceFirstChar { it.uppercase() }
    }
}
