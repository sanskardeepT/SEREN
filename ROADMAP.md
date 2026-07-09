# SEREN — Project Roadmap

> **Architecture**: All batches reuse the same 8-module ensemble (DrawNet, GazeNet, PhonNet, NumNet, AttentNet, EmotNet, SpatialNet, FusionNet). Each batch adds conditions and fine-tuning data to existing modules — no new infrastructure per batch.

---

## Batch 1 — Strongest Dataset Backing
**Status**: ✅ Completed & Integrated

| # | Condition | Category | Primary Modules |
|---|---|---|---|
| 1 | Dyslexia | Learning Disorders | DrawNet, GazeNet, PhonNet |
| 2 | Dysgraphia | Learning Disorders | DrawNet, PhonNet |
| 3 | Dyscalculia | Learning Disorders | NumNet, SpatialNet |
| 4 | ADHD — Inattentive | Attention & Processing | AttentNet, GazeNet |
| 5 | ADHD — Hyperactive/Impulsive | Attention & Processing | AttentNet |
| 6 | ADHD — Combined | Attention & Processing | AttentNet, FusionNet |
| 7 | Stuttering | Speech & Language | PhonNet |
| 8 | Cluttering | Speech & Language | PhonNet |
| 9 | Word-Finding Difficulty (Anomia) | Speech & Language | PhonNet |
| 10 | Auditory Processing Disorder | Attention & Processing | PhonNet, AttentNet |

---

## Batch 2 — Anxiety, Emotional & Executive Function
**Status**: ✅ Completed & Integrated

| # | Condition | Category |
|---|---|---|
| 1 | Social Anxiety | Anxiety Disorders |
| 2 | Generalised Anxiety Disorder (GAD) | Anxiety Disorders |
| 3 | Selective Mutism | Anxiety Disorders |
| 4 | Test / Performance Anxiety | Anxiety Disorders |
| 5 | Separation Anxiety | Anxiety Disorders |
| 6 | Specific Phobia (School) | Anxiety Disorders |
| 7 | Childhood Depression (Masked) | Emotional & Behavioural |
| 8 | Emotional Dysregulation | Emotional & Behavioural |
| 9 | Executive Function Deficit | Attention & Processing |
| 10 | Working Memory Deficit | Attention & Processing |

---

## Batch 3 ✦ CURRENT — Additional Speech/Language, Adult Variants & Processing
**Status**: ⏳ Pending

| # | Condition | Category |
|---|---|---|
| 1 | Processing Speed Disorder | Attention & Processing |
| 2 | Voice Disorder | Speech & Language |
| 3 | Expressive Language Disorder | Speech & Language |
| 4 | Receptive Language Disorder | Speech & Language |
| 5 | Phonological Disorder | Speech & Language |
| 6 | Childhood Apraxia of Speech | Speech & Language |
| 7 | Non-Verbal Learning Disability | Learning Disorders |
| 8 | Twice-Exceptional Profile (2e) | Silent Profiles |
| 9 | Adult Dyslexia | Learning Disorders |
| 10 | Adult ADHD | Attention & Processing |

---

## Batch 4 — Motor, Visual-Motor, Adult Extensions & Math Subtypes
**Status**: ⏳ Pending

| # | Condition | Category |
|---|---|---|
| 1 | Dev. Coordination Disorder / Dyspraxia | Learning Disorders |
| 2 | Visual-Motor Integration | Sensory & Motor |
| 3 | Adult Word-Finding | Speech & Language |
| 4 | Adult Processing Speed | Attention & Processing |
| 5 | Reading Fluency Lag | Learning Disorders |
| 6 | Orthographic Processing Deficit | Learning Disorders |
| 7 | RAN Deficit | Learning Disorders |
| 8 | Spelling Disorder | Learning Disorders |
| 9 | Place Value Confusion | Learning Disorders |
| 10 | Fraction/Ratio Deficit | Learning Disorders |

---

## Batch 5 — Masking, Social Communication & Sensory (Phase 3 conditions, partial dataset support via ABIDE/ChildVox)
**Status**: ⏳ Pending

| # | Condition | Category |
|---|---|---|
| 1 | Masking and Camouflaging | Silent Profiles |
| 2 | High-Functioning Autism (Masked) | Silent Profiles |
| 3 | Social Communication Disorder | Silent Profiles |
| 4 | Pragmatic Language Disorder | Speech & Language |
| 5 | Theory of Mind Deficit | Silent Profiles |
| 6 | Sensory Processing Disorder | Sensory & Motor |
| 7 | Sensory Defensiveness | Sensory & Motor |
| 8 | Vestibular Processing Difficulty | Sensory & Motor |
| 9 | Proprioceptive Processing Difficulty | Sensory & Motor |
| 10 | Trauma-Based Silence | Emotional & Behavioural |

---

## Batch 6+ — Insecurities & Confidence Deficits + Remaining Silent Profiles
**Status**: ⏳ Pending

> ⚠️ **No dedicated public dataset exists for these conditions** (per Dataset Blueprint Section 6.7). These get built as validated-questionnaire + NLP self-report modules on EmotNet, not new trained sub-models. All 21 Insecurities + remaining Silent Profiles conditions go here, split across 3–4 batches of 10.

### Insecurities & Confidence Deficits (21 conditions):
Body Image Insecurity • Imposter Syndrome • Rejection Sensitivity • Workplace/Career Insecurity • Relationship Insecurity • FOMO/Social Comparison Anxiety • Digital/Online Social Insecurity • Academic Trauma • Stuttering-Linked Confidence Deficit • Decision Paralysis • Financial Confidence Insecurity • Leadership Avoidance Pattern • Career-Stagnation Confidence Collapse • Deep-Rooted Shyness • Voice and Expression Insecurity • Social Belonging Insecurity • Performance/Achievement Insecurity • Anger as Hidden Insecurity • Family-Origin Insecurities • Perfectionism-Driven Self-Criticism • Public Speaking Phobia

### Remaining Silent Profiles:
Pathological Demand Avoidance • Gifted Underachievement • Introversion-Driven Academic Suppression • Invisible Struggle (Composite Burden) • Alexithymia • Highly Sensitive Person Overwhelm • Chronic People-Pleasing / Fawn Response

---

## Architecture Reference

All batches share:
- **DrawNet** — Touchscreen stroke CNN (handwriting, drawing)
- **GazeNet** — LSTM on eye-movement sequences (MediaPipe Iris)
- **PhonNet** — Wav2Vec2 fine-tuned + latency regressor (speech/audio)
- **NumNet** — Random Forest on RT features (number tasks)
- **AttentNet** — XGBoost on behavioural features (attention tasks)
- **EmotNet** — Logistic Regression + Rule Engine (questionnaire + NLP)
- **SpatialNet** — SVM on accuracy-latency features (pattern tasks)
- **FusionNet** — Weighted ensemble combining all sub-model outputs → 0–100 risk score
