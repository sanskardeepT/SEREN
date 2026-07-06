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
