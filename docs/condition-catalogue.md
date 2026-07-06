# SEREN — Condition Catalogue (Batch 1)

> **Scope**: 10 conditions with strongest dataset backing. This is the first build batch.
> **Source**: SEREN Technical Research Paper v3.0 (Sections 2, 3) + Dataset Blueprint (Sections 4, 7)

---

## 1. Dyslexia

| Field | Detail |
|---|---|
| **Category** | Learning Disorders |
| **Prevalence** | 5–20% globally; 11.2% in Indian studies |
| **Target Accuracy** | 95–99% |
| **AI Modules** | **DrawNet** (letter-reversal CNN), **GazeNet** (reading regressions), **PhonNet** (RAN + phoneme tasks), **FusionNet** (final ensemble) |

**Neuroscience Basis**: Phonological processing deficit with reduced activation in the left-hemisphere reading network (temporoparietal junction, visual word form area, Broca's area). Heritability estimated at 50–60%. Associated genes: DYX1C1, DCDC2, KIAA0319.

**Manifestations**:
- Reading significantly below grade level despite adequate instruction
- Letter reversals (b/d, p/q) persisting beyond expected age
- Slow, laborious oral reading with omissions and substitutions
- Difficulty with phoneme blending, rhyme, and syllable segmentation
- Inconsistent spelling violating common phonetic rules
- Strong listening comprehension vs. markedly weaker reading comprehension
- Adults: slow professional reading, spelling errors, avoidance of writing-heavy tasks

**Detection Signals**:
- Webcam eye-tracking: fixation duration, regression frequency, saccade amplitude
- RAN latency across letters, digits, objects, colours
- Phoneme segmentation accuracy and response time
- Handwriting CNN: letter-reversal rate, stroke-direction pattern
- Oral reading fluency (words-per-minute via on-device speech recognition)
- Sight-word recognition latency on timed flashcard tasks
- Rhyme-detection accuracy on audio discrimination task

---

## 2. Dysgraphia

| Field | Detail |
|---|---|
| **Category** | Learning Disorders |
| **Prevalence** | 5–20% globally; 12.5% in Indian studies |
| **Target Accuracy** | 95–99% |
| **AI Modules** | **DrawNet** (stroke kinematics CNN), **PhonNet** (spelling audio for phonological subtype), **FusionNet** |

**Neuroscience Basis**: Three subtypes — phonological (grapheme-phoneme mapping), surface (irregular spelling patterns), and motor (overlapping with coordination difficulty). Neural correlates overlap both the dyslexia network and fine-motor coordination pathways.

**Manifestations**:
- Illegible handwriting inconsistent with effort and general ability
- Cramped pencil grip and visible fatigue during writing tasks
- Inconsistent letter sizing, spacing, and baseline alignment
- Difficulty organising ideas in writing despite clear verbal expression
- Writing speed well below grade-level expectations
- Avoidance of writing tasks; frequently incomplete written work

**Detection Signals**:
- Touchscreen letter-formation CNN: stroke velocity, acceleration, jerk coefficient
- Stylus pressure proxy from capacitive touch-area data
- Letter-height variability and inter-letter spacing consistency
- Writing speed (characters/minute) against age-normed benchmarks
- Baseline-drift detection across a multi-sentence copying task

---

## 3. Dyscalculia

| Field | Detail |
|---|---|
| **Category** | Learning Disorders |
| **Prevalence** | 5–7% globally; 10.5% in Indian studies |
| **Target Accuracy** | 88–93% |
| **AI Modules** | **NumNet** (number comparison RT + subitizing), **SpatialNet** (spatial tasks for access-deficit subtype), **FusionNet** |

**Neuroscience Basis**: Impaired core "number sense" linked to atypical intraparietal sulcus activation and a right-hemisphere parietal network deficit. Distinct from maths anxiety, though the two co-occur in a substantial minority.

**Manifestations**:
- Cannot automatically recall basic arithmetic facts despite repeated practice
- Difficulty with place value and number magnitude comparison
- Finger-counting persisting well beyond developmentally expected age
- Confusion with mathematical symbols and operations
- Poor comprehension of time, money, and measurement concepts
- Elevated maths anxiety including physical symptoms before maths tasks

**Detection Signals**:
- Dot enumeration (subitizing) accuracy and speed for arrays of 2–8 dots
- Number-magnitude comparison latency
- Arithmetic fact-retrieval speed under timed conditions
- Number-line estimation deviation score
- Approximate Number System (ANS) task accuracy
- Place-value understanding task using concrete-representational-abstract sequencing

---

## 4. ADHD — Inattentive Type

| Field | Detail |
|---|---|
| **Category** | Attention & Processing |
| **Prevalence** | 7.2% globally (all ADHD presentations) |
| **Target Accuracy** | 90–95% |
| **AI Modules** | **AttentNet** (CPT miss rate, vigilance decrement, RT variability), **GazeNet** (gaze deviation during instructions), **FusionNet** |

**Neuroscience Basis**: Reduced prefrontal cortex, basal ganglia, and cerebellar volume; dopaminergic/noradrenergic dysregulation. Heritability ~75–80%. Most under-identified in girls and high-ability children.

**Manifestations**:
- Difficulty sustaining attention; frequently drifts off mid-task
- Fails to follow through on multi-step instructions; work left incomplete
- Loses materials and appears forgetful in daily routines
- Easily distracted by external and internal stimuli
- Struggles with task organisation and realistic time estimation
- Adults: chronic disorganisation, procrastination, missed deadlines

**Detection Signals**:
- CPT miss rate over sustained multi-minute attention task
- Vigilance-decrement index (performance drop across session)
- Eye-gaze deviation during instruction screens
- Omission-error rate on target-detection task
- Session interruption frequency (device backgrounded, app exited)
- Response-inconsistency index (SD of reaction time across trials)

---

## 5. ADHD — Hyperactive/Impulsive Type

| Field | Detail |
|---|---|
| **Category** | Attention & Processing |
| **Prevalence** | 7.2% globally (all presentations); this is the externally visible subtype |
| **Target Accuracy** | 88–93% |
| **AI Modules** | **AttentNet** (commission errors, response inhibition), **FusionNet** |

**Neuroscience Basis**: Same prefrontal/basal-ganglia/cerebellar circuit as Inattentive, but with more pronounced motor-inhibition deficits. Higher male-to-female ratio at initial diagnosis (though this partly reflects referral bias).

**Manifestations**:
- Fidgeting, squirming, inability to stay seated
- Running/climbing in inappropriate situations
- Difficulty waiting turn; interrupting others
- Talking excessively; blurting answers before questions finish
- Acting without thinking; difficulty with impulse control

**Detection Signals**:
- Commission-error rate on Go/No-Go task (false alarms / no-go trials)
- Response inhibition latency
- Accelerometer fidgeting intensity and restlessness index
- Tap frequency and swipe irregularity on touchscreen
- Session interruption and re-entry patterns

---

## 6. ADHD — Combined Type

| Field | Detail |
|---|---|
| **Category** | Attention & Processing |
| **Prevalence** | ~50% of all ADHD cases |
| **Target Accuracy** | 90–95% |
| **AI Modules** | **AttentNet** (full attention battery), **GazeNet** (gaze deviation), **FusionNet** (joint feature fusion) |

**Neuroscience Basis**: Meets criteria for both Inattentive and Hyperactive/Impulsive presentations simultaneously. Same neural circuitry affected; broadest behavioural footprint of the three ADHD subtypes.

**Manifestations**:
- All Inattentive symptoms (sustained attention difficulty, forgetfulness, disorganisation)
- All Hyperactive/Impulsive symptoms (fidgeting, impulsivity, difficulty waiting)
- Most functionally impairing subtype due to combined deficits

**Detection Signals**:
- Combined battery: CPT miss rate + commission errors + gaze deviation + RT variability
- Vigilance decrement + fidgeting index + session interruptions
- Full attention battery from AttentNet
- Cross-validated via GazeNet gaze-off-task percentage

---

## 7. Stuttering

| Field | Detail |
|---|---|
| **Category** | Speech & Language |
| **Prevalence** | ~1% globally |
| **Target Accuracy** | 90–95% |
| **AI Modules** | **PhonNet** (disfluency audio classifier, spectral analysis), **FusionNet** |

**Neuroscience Basis**: Neurological timing disruption in speech motor planning implicating basal ganglia-thalamo-cortical circuitry. Heritability ~70%. Roughly 80% of childhood cases resolve spontaneously; the remainder persist into professional adulthood.

**Manifestations**:
- Syllable or sound repetitions
- Sound prolongations
- Speech blocks (airflow/voicing stops entirely)
- Secondary behaviours: eye blinking, head jerks, lip tension
- Avoidance vocabulary — choosing easier words to avoid feared sounds
- Anticipatory anxiety before speaking situations

**Detection Signals**:
- Syllable-repetition detection via custom disfluency audio classifier
- Sound-prolongation duration via spectral analysis
- Block detection: intra-word silence exceeding defined threshold
- Secondary-behaviour detection: facial landmark tracking correlated with speech timing
- Avoidance-vocabulary flagging via lexical complexity analysis
- Fluency variance between low-pressure and simulated-audience conditions
- Anticipatory-anxiety proxy: response delay before high-frequency trigger consonants

---

## 8. Cluttering

| Field | Detail |
|---|---|
| **Category** | Speech & Language |
| **Prevalence** | ~0.5% globally |
| **Target Accuracy** | 82–88% |
| **AI Modules** | **PhonNet** (syllable rate + intelligibility analysis), **FusionNet** |

**Neuroscience Basis**: Distinct from stuttering — involves excessively rapid and/or irregular speech rate with breakdowns in speech clarity. Often co-occurs with ADHD and language formulation difficulties. Less researched than stuttering.

**Manifestations**:
- Excessively fast speaking rate
- Irregular speech rhythm and pacing
- Excessive disfluencies (particularly revisions and interjections, less repetitions)
- Reduced speech intelligibility under pressure
- Poor self-monitoring of speech output
- Often unaware of the difficulty (unlike stuttering where awareness is high)

**Detection Signals**:
- Syllables-per-minute rate analysis (abnormally high)
- Intelligibility score under timed speaking conditions
- Revision and interjection frequency (distinct from stuttering's repetition pattern)
- Speech rate variability across utterances
- FluencyBank rate-analysis features

---

## 9. Word-Finding Difficulty (Anomia)

| Field | Detail |
|---|---|
| **Category** | Speech & Language |
| **Prevalence** | 5–7% globally |
| **Target Accuracy** | 85–90% |
| **AI Modules** | **PhonNet** (verbal fluency scoring, pause rate), **FusionNet** |

**Neuroscience Basis**: Difficulty retrieving known words during spontaneous speech. Can be developmental (childhood) or acquired. Linked to left temporal lobe lexical-retrieval networks. Commonly co-occurs with dyslexia.

**Manifestations**:
- Frequent tip-of-the-tongue experiences
- Circumlocution (talking around a word they can't retrieve)
- Excessive use of filler words and nonspecific terms ("thing", "stuff")
- Pauses mid-sentence while searching for words
- Vocabulary appears smaller than actual knowledge base

**Detection Signals**:
- Verbal fluency: items generated per 60 seconds on category/letter tasks
- Pause rate and duration during spontaneous speech
- Circumlocution detection via NLP
- Naming latency on confrontation naming tasks
- Comparison of receptive vocabulary (comprehension) vs. expressive output

---

## 10. Auditory Processing Disorder (APD)

| Field | Detail |
|---|---|
| **Category** | Attention & Processing |
| **Prevalence** | 2–5% globally |
| **Target Accuracy** | 82–88% |
| **AI Modules** | **PhonNet** (target-in-noise accuracy, phoneme discrimination), **AttentNet** (auditory attention), **FusionNet** |

**Neuroscience Basis**: Difficulty processing auditory information despite normal hearing sensitivity. Affects how the brain interprets sound, particularly speech in noisy environments. Often confused with ADHD or language disorders.

**Manifestations**:
- Difficulty understanding speech in noisy environments
- Frequently asking for repetition ("What?")
- Difficulty following multi-step verbal instructions
- Problems distinguishing similar-sounding words
- Better performance with visual vs. auditory information
- Delayed response to verbal information

**Detection Signals**:
- Target-in-noise accuracy degradation (stepped signal-to-noise ratio)
- Phoneme pair discrimination reaction time
- Dichotic listening task performance
- Auditory figure-ground task accuracy
- Performance differential: visual vs. auditory task presentation
- Response latency specifically on verbally-presented instructions

---
---

# SEREN — Condition Catalogue (Batch 2)

> **Scope**: 10 conditions (Anxiety, Emotional & Executive Function).
> **Source**: SEREN Technical Research Paper v3.0 SECTION 2

---

## 11. Social Anxiety Disorder (SAD)

| Field | Detail |
|---|---|
| **Category** | Anxiety Disorders |
| **Prevalence** | 7–13% lifetime |
| **Target Accuracy** | 87–92% |
| **AI Modules** | **GazeNet** (gaze aversion), **PhonNet** (tremor analysis), **EmotNet** (avoidance logs), **FusionNet** |

**Neuroscience Basis**: Fear of negative social evaluation associated with right amygdala hyperactivation and reduced prefrontal control. Avoidance behaviors compound over time and peak in prevalence during adolescence.

**Manifestations**:
- Intense fear of being watched, judged, or embarrassed in public
- Complete avoidance of oral participation (freezes when called upon)
- Shaking, blushing, or voice tremor when speaking
- Avoidance of group tasks, presentations, or shared activities
- Adults: avoidance of networking, job interviews, and phone calls

**Detection Signals**:
- Gaze-aversion frequency during simulated social tasks (via GazeNet webcam eye-tracking)
- Voice-tremor detection via acoustic jitter and shimmer analysis under pressure
- Response-delay differential between solo and group task conditions
- Avoidance-behavior scoring on social-scenario decision tasks
- Camera-based heart-rate proxy (rPPG) during social tasks

---

## 12. Generalised Anxiety Disorder (GAD)

| Field | Detail |
|---|---|
| **Category** | Anxiety Disorders |
| **Prevalence** | 6.5% children; 3.7% adults |
| **Target Accuracy** | 85–90% |
| **AI Modules** | **EmotNet** (CBT self-talk NLP), **AttentNet** (perfectionism action count), **FusionNet** |

**Neuroscience Basis**: Associated with dysregulation of the hypothalamic-pituitary-adrenal (HPA) axis and reduced prefrontal control over amygdala-driven worry circuitry.

**Manifestations**:
- Excessive worry spanning multiple domains: school, family, health, and future
- Concentration difficulties; mind going blank during tasks
- Physical complaints (headaches/stomach aches) before high-stakes tasks
- Perfectionism with excessive time spent re-checking work
- Repeated reassurance-seeking from parents or teachers

**Detection Signals**:
- Worry-language frequency detected via NLP analysis of task-related self-talk
- Perfectionism score derived from erase/redo action counts on touchscreen tasks
- Task-abandonment rate above a defined threshold, indicating failure-avoidance
- Response-latency elongation on low-difficulty items, indicating anxious overthinking
- Performance delta between timed and self-paced task versions

---

## 13. Selective Mutism (SM)

| Field | Detail |
|---|---|
| **Category** | Anxiety Disorders |
| **Prevalence** | 0.47–0.76% globally |
| **Target Accuracy** | 82–88% |
| **AI Modules** | **PhonNet** (whisper detection, latency), **AttentNet** (freeze response), **FusionNet** |

**Neuroscience Basis**: Consistent failure to speak in specific social settings (such as school) despite typical speech at home; anxiety-based rather than oppositional in origin.

**Manifestations**:
- Speaks normally at home; mute or near-mute at school
- Communicates through gestures, pointing, or writing in public settings
- Visible distress or freezing when expected to speak
- Frequently misidentified by adults as shyness or stubborness

**Detection Signals**:
- Speech-initiation latency during prompted speaking tasks
- Whisper-versus-normal-voice detection across session contexts
- Freeze-response detection: motor-activity pause (via sensors) when speech is expected
- Speech-latency differential between familiar and unfamiliar interaction partners

---

## 14. Exam / Test Anxiety

| Field | Detail |
|---|---|
| **Category** | Anxiety Disorders |
| **Prevalence** | 10–40% of students |
| **Target Accuracy** | 85–90% |
| **AI Modules** | **AttentNet** (latency inflation under pressure), **EmotNet** (post-task self-talk), **FusionNet** |

**Neuroscience Basis**: Cognitive interference model; task-irrelevant thoughts (worry about failure) consume working memory capacity, degrading performance under time-pressure.

**Manifestations**:
- Significant performance drop in timed testing vs. relaxed practice settings
- Blanking out during tests despite knowing the material
- Physical anxiety symptoms (sweating, rapid breathing) under evaluation
- Rapid guessing or early abandonment of difficult test items

**Detection Signals**:
- Cognitive latency inflation specifically under simulated test timers
- High-frequency answer validation/modification attempts before submitting
- Performance delta between timed and self-paced versions of identical cognitive tasks
- Camera-based heart-rate proxy (rPPG) elevation during timed segments

---

## 15. Separation Anxiety Disorder

| Field | Detail |
|---|---|
| **Category** | Anxiety Disorders |
| **Prevalence** | 4–5% of children |
| **Target Accuracy** | 78–84% |
| **AI Modules** | **EmotNet** (separation distress index), **FusionNet** |

**Neuroscience Basis**: Hyperactive threat-detection circuitry (amygdala) and atypical regulation of cortisol response when separated from attachment figures.

**Manifestations**:
- Extreme distress when separating from parents or home
- Persistent worry about harm coming to attachment figures
- Refusal to go to school or sleep alone due to separation fears
- Somatic complaints (nausea, stomach aches) on mornings of separation

**Detection Signals**:
- Parent-separation distress indexing through child self-reports
- Somatic symptom logging frequency
- Session logs correlating attendance/participation drops with separation events

---

## 16. Specific Phobia (School)

| Field | Detail |
|---|---|
| **Category** | Anxiety Disorders |
| **Prevalence** | 2–5% of children (School Refusal subclass) |
| **Target Accuracy** | 80–86% |
| **AI Modules** | **EmotNet** (avoidance scaling), **FusionNet** |

**Neuroscience Basis**: Amygdala-mediated fear conditioning to school-related stimuli (bullies, sensory overload, failure) leading to active avoidance patterns.

**Manifestations**:
- Intense dread of going to school resulting in tantrums or begging to stay home
- Severe somatic symptoms (headaches, stomach aches) before school
- Chronic absenteeism or school refusal
- Elevated anxiety levels on Sunday evenings or end of holidays

**Detection Signals**:
- Avoidance patterns mapping across simulated school-scenario tasks
- Attendance pattern correlation log analysis
- Self-reported dread ratings on school-themed visual tasks

---

## 17. Childhood Depression (Masked)

| Field | Detail |
|---|---|
| **Category** | Emotional & Behavioural |
| **Prevalence** | 2–3% of children |
| **Target Accuracy** | 80–86% |
| **AI Modules** | **PhonNet** (psychomotor speech slowing), **EmotNet** (sentiment), **AttentNet** (energy drop), **FusionNet** |

**Neuroscience Basis**: Dysphoric mood and cognitive slowing associated with HPA-axis dysregulation and reduced prefrontal cortical volume. Often masked in children as simple quietness.

**Manifestations**:
- Persistent low energy and reduced interest in previously enjoyed games
- Frequent negative self-talk ("I can't do anything right")
- Psychomotor slowing (slowed physical movement and speech)
- Somatic complaints without medical explanation

**Detection Signals**:
- Energy-level drop across the session, measured as performance deterioration over time
- Sentiment analysis of self-referential language for negative content (via NLP)
- Anhedonia proxy: reduced engagement or speed in reward-based game tasks
- Psychomotor slowing: elevation across all response-latency measures
- Future-negative language patterns detected via speech transcripts

---

## 18. Emotional Dysregulation

| Field | Detail |
|---|---|
| **Category** | Emotional & Behavioural |
| **Prevalence** | ~8% globally |
| **Target Accuracy** | 82–87% |
| **AI Modules** | **AttentNet** (frustration taps), **EmotNet** (self-regulation questionnaire), **FusionNet** |

**Neuroscience Basis**: Atypical prefrontal-amygdala structural connectivity, leading to difficulty modulating intense emotional states.

**Manifestations**:
- Frequent, intense temper outbursts disproportionate to the trigger
- Rapid mood shifts (happy to extremely angry or crying in seconds)
- Low frustration tolerance; gives up or throws objects when a task is hard
- Difficulty calming down once upset

**Detection Signals**:
- Frustration indices: rapid random tapping or high-pressure touches on error screens
- Recovery latency: time to resume typical response speeds after a simulated failure trigger
- Sensor-detected device shaking or rapid movements during difficult task segments

---

## 19. Executive Function Deficit

| Field | Detail |
|---|---|
| **Category** | Attention & Processing |
| **Prevalence** | ~10% school and working population |
| **Target Accuracy** | 80–87% |
| **AI Modules** | **NumNet** (star sorting task switch costs), **SpatialNet** (planning maze paths), **FusionNet** |

**Neuroscience Basis**: Prefrontal cortex dysfunction and atypical connectivity in frontoparietal networks regulating cognitive control, task switching, and planning.

**Manifestations**:
- Chronic disorganization and loss of school/work materials
- Difficulty starting, planning, and prioritizing multi-step tasks
- Struggle to switch attention between different tasks or rules
- Poor time estimation; frequently late or runs out of time

**Detection Signals**:
- Switch-cost reaction delay on star sorting tasks (cognitive flexibility)
- Planning efficiency: move-count and pause durations on maze/organization tasks
- Working memory updates accuracy on n-back task steps
- Task execution sequencing errors

---

## 20. Working Memory Deficit

| Field | Detail |
|---|---|
| **Category** | Attention & Processing |
| **Prevalence** | ~10% of school population |
| **Target Accuracy** | 78–85% |
| **AI Modules** | **SpatialNet** (Corsi block sequence recall), **PhonNet** (verbal digit span), **FusionNet** |

**Neuroscience Basis**: Reduced activation and capacity limit in the dorsolateral prefrontal cortex and parietal regions responsible for short-term information storage and manipulation.

**Manifestations**:
- Forgets instructions immediately after hearing them
- Loses place in the middle of a task (e.g., forgets what step they were on)
- Difficulty holding words/numbers in mind while working (like mental arithmetic)
- Slow learning progress due to fast cognitive overload

**Detection Signals**:
- Spatial recall span: maximum correct sequence length on Corsi block tasks
- Verbal digit span: maximum correct length of numbers recalled forward/backward
- Error rate on n-back working memory tasks
- Latency decay: performance drop as delay intervals increase

