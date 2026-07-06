# SEREN — Dataset Map (Batch 1)

> **Scope**: Datasets for Batch 1's 10 conditions, pulled directly from Dataset Blueprint PDF Sections 6 and 7.
> **Rule**: Only datasets explicitly named in the PDF are listed. Nothing invented.

---

## Dyslexia Datasets

| Dataset | Link | License | SEREN Use |
|---|---|---|---|
| **ETDD70** | zenodo.org/records/13332134 | CC BY 4.0 | GazeNet pre-training: fixation duration, regression rate (70 children, ages 9–10, eye-tracking) |
| **Dyslexia Handwriting (Kaggle)** | kaggle.com/datasets/drizasazanitaisa/dyslexia-handwriting-dataset | Open (Kaggle) | DrawNet: letter-reversal CNN pre-training (138,500 labeled images: 78k normal + 52k reversal + 8k corrected) |
| **Synthetic Dyslexia HW (YOLO)** | zenodo.org/records/14852659 | CC BY 4.0 | DrawNet: augmented reversal-pattern detection training (130k images) |
| **Dyslexia Prediction (Kaggle)** | kaggle.com/datasets/luzrello/dyslexia | CC BY 4.0 | FusionNet: cognitive risk-factor weighting (535 participants, gamified test features) |
| **CopCo (Danish ET+Dyslexia)** | via arxiv.org/pdf/2602.19598 | Research | GazeNet: cross-linguistic reading-regression pattern reference (57 participants) |
| **OpenNeuro ds003126** | openneuro.org/datasets/ds003126 | Open Access | Reference validation only — MRI/fMRI ground truth, not on-device inference |

---

## Dysgraphia Datasets

| Dataset | Link | License | SEREN Use |
|---|---|---|---|
| **IAM Handwriting Database** | fki.inf.unibe.ch/databases/iam-handwriting-database | Free registration | DrawNet: baseline stroke segmentation and character-level features (657 writers) |
| **ICDAR Datasets (2013/2019)** | tc11.cvc.uab.es/datasets | Research | DrawNet: large-scale handwriting CNN pre-training, multi-language competitions |
| **CASIA Handwriting Database** | nlpr.ia.ac.cn/databases/handwriting | Research | DrawNet: non-Latin stroke-pattern transfer learning |
| **Devanagari Character Dataset** | kaggle.com/datasets/suvooo/hindi-character-recognition | CC BY | **Critical**: Hindi-script letter-formation norms — mandatory pre-training step before Hindi-medium deployment |
| **Dysgraphia AI Review (PLOS ONE 2025)** | doi.org/10.1371/journal.pone.0328722 | Open Access | Dataset discovery reference — systematic index of dysgraphia AI datasets |

---

## Dyscalculia Datasets

| Dataset | Link | License | SEREN Use |
|---|---|---|---|
| **PISA Math Dataset** | pisa.oecd.org | OECD Open | NumNet: cross-cultural math-difficulty normative benchmark (79 countries) |
| **Number Sense Screener Norms** | doi.org/10.1007/s11145-009-9168-7 | Research | NumNet: age-stratified normative baseline for dot-counting/subitizing tasks |

---

## ADHD & Attention Datasets (covers Inattentive, Hyperactive/Impulsive, Combined, and APD)

| Dataset | Link | License | SEREN Use |
|---|---|---|---|
| **IEEE EEG ADHD Children** | ieee-dataport.org/open-access/eeg-data-adhd-control-children | CC BY-NC-ND 4.0 | AttentNet: theta/beta-ratio feature reference (61 ADHD + 60 control children, ages 7–12) |
| **Mendeley EEG ADHD Adults** | doi.org/10.17632/6k4g25fhzg.1 | CC BY 4.0 | AttentNet: adult ADHD signal reference for Phase 2 adaptation (79 adults, 4 recording states) |
| **ADHD-200 (fMRI)** | fcon_1000.projects.nitrc.org/indi/adhd200 | Open Access | Research reference only — SEREN uses behavioural proxies, not fMRI (973 participants) |
| **Reddit Mental-Health Text** | github.com/amurark/mental-health-detection | Open | EmotNet: ADHD/anxiety/depression language-pattern validation from labeled Reddit posts |

---

## Speech, Fluency & Stuttering Datasets (covers Stuttering, Cluttering, Word-Finding, APD)

| Dataset | Link | License | SEREN Use |
|---|---|---|---|
| **UCLASS** | uclass.org.uk (free registration) | Free research | PhonNet: stuttering acoustic-feature training (139 monologues, 81 speakers ages 5–47) |
| **FluencyBank** | fluency.talkbank.org | CC BY-NC-SA 3.0 | PhonNet: fluency-disorder feature extraction, expert-annotated disfluency labels (48 speakers, ~25 hrs) |
| **SEP-28k** | github.com/apple/ml-stuttering-events-dataset | CC BY-NC 4.0 | PhonNet: largest open stuttering dataset — disfluency-event classifier training (28,177 clips) |
| **KSoF (Kassel State of Fluency)** | arxiv.org/abs/2203.05383 | Research | PhonNet: fine-grained disfluency-type classification, 6 categories (5,597 clips, 37 speakers) |
| **LibriStutter** | github.com/Kourkounakis/LibriStutter | Open | PhonNet: synthetic data augmentation for low-resource stuttering training |
| **TORGO (Dysarthria)** | cs.toronto.edu/~complingweb/data/TORGO | Free research | PhonNet: speech-production-disorder baseline (negative-class reference) |
| **AI4Bharat IndicSUPERB** | github.com/AI4Bharat/indic-superb | CC BY 4.0 | **Critical**: Hindi/Marathi/Telugu speech and ASR base for all Indian-language phonological tasks |
| **ChildVox Benchmark** | arxiv.org/abs/2605.29257 | Research | PhonNet: children's speech baseline across 17 child-centered audio datasets, 20+ sub-tasks |
| **CHILDES** | childes.talkbank.org | CC BY-NC-SA 3.0 | PhonNet: child speech/language development norms, multiple languages incl. limited Hindi |

---

## Anxiety & Emotional Datasets (covers SAD, GAD, Selective Mutism, Depression)

| Dataset | Link | License | SEREN Use |
|---|---|---|---|
| **DAIC-WOZ** | dcapswoz.ict.usc.edu | Free research | EmotNet: clinical interview transcripts & audio vectors (189 sessions) |
| **MPAD (Anxiety)** | doi.org/10.1016/j.bspc.2020.102390 | Research | EmotNet: heart-rate (rPPG) and galvanic skin response anxiety thresholds |
| **Reddit Mental-Health Text** | github.com/amurark/mental-health-detection | Open | EmotNet: worry/perfectionism lexical classifier fine-tuning |
| **CASME II (Micro-Expressions)** | fvh.ict.ac.cn/casme | Research | GazeNet: facial micro-expression parameters validation under social tasks |

---

## Executive & Memory Datasets (covers Executive Function, Working Memory)

| Dataset | Link | License | SEREN Use |
|---|---|---|---|
| **Corsi-Block Normative Spans** | doi.org/10.3389/fpsyg.2019.02882 | Research | SpatialNet: age-adapted z-scores for spatial recall sequences (2,000+ children) |
| **PISA Cognitive Battery** | oecd.org/pisa | OECD Open | NumNet: cognitive switch-cost parameters and time-efficiency baselines |

---

## Multimodal & General Datasets (used across Batch 1 + 2)

| Dataset | Link | License | SEREN Use |
|---|---|---|---|
| **AI4Bharat IndicNLP Corpus** | github.com/ai4bharat/indic-nlp-library | MIT License | PhonNet: Indian-language tokenization and phoneme-mapping base, 12 languages |
| **IIT Bombay Hindi Corpus** | cfilt.iitb.ac.in/iitb_parallel | CC BY-NC | PhonNet: Hindi language-model base for phonological task generation (1.49M sentence pairs) |

---

## Condition → Dataset → Module Quick Reference (Batch 1 + Batch 2)

| # | Condition | Module(s) | Primary Datasets | Detection Modality |
|---|---|---|---|---|
| 1 | Dyslexia | GazeNet + PhonNet | ETDD70, FluencyBank | Eye-tracking regressions + phonological task RT |
| 2 | Dyscalculia | NumNet | NSS Norms, PISA Math | Number comparison RT + subitizing accuracy |
| 3 | Dysgraphia | DrawNet | Dyslexia HW (Kaggle), IAM | Touchscreen stroke kinematics CNN |
| 4 | ADHD | AttentNet | IEEE EEG ADHD, ADHD-200 | Sustained attention task + Go/No-Go errors |
| 5 | Stuttering | PhonNet | UCLASS, FluencyBank, SEP-28k | Audio: repetition & block events detection |
| 6 | Cluttering | PhonNet | FluencyBank (rate analysis) | Audio: syllable rate + pacing irregularity |
| 7 | Word-Finding Difficulty | PhonNet | FluencyBank + fluency norms | Verbal fluency: items/60s + pause rate |
| 8 | Auditory Processing | PhonNet | ChildVox (noise-added), SCAN-3 | Target-in-noise accuracy degradation |
| 9 | Social Anxiety (SAD) | GazeNet + PhonNet | MPAD, CASME II | Gaze avoidance + voice-tremor analysis |
| 10 | Generalised Anxiety (GAD) | EmotNet | Reddit Mental-Health, DAIC-WOZ | Worry-language (NLP) + erase/redo counts |
| 11 | Selective Mutism (SM) | PhonNet + AttentNet | IndicSUPERB, CHILDES | Initiation latency + freeze-response pauses |
| 12 | Exam / Test Anxiety | AttentNet + EmotNet | PISA Cognitive Battery | Latency inflation under time pressure |
| 13 | Separation Anxiety | EmotNet | DAIC-WOZ (attachment subsets) | Parent-separation distress questionnaire |
| 14 | Specific Phobia (School) | EmotNet | Avoidance scaling norms | School-scenario dread scoring |
| 15 | Childhood Depression | PhonNet + EmotNet | DAIC-WOZ, Reddit NLP | Psychomotor speech slowing + negative sentiment |
| 16 | Emotional Dysregulation | AttentNet | Frustration touch metrics | Tapping frequency on error triggers |
| 17 | Executive Function | NumNet + SpatialNet | PISA Cognitive subsets | Star sorting task-switch cost delay |
| 18 | Working Memory | SpatialNet | Corsi-Block Spans Norms | Spatial recall block pattern sequences |
