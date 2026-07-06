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

## Multimodal & General Datasets (used across Batch 1)

| Dataset | Link | License | SEREN Use |
|---|---|---|---|
| **AI4Bharat IndicNLP Corpus** | github.com/ai4bharat/indic-nlp-library | MIT License | PhonNet: Indian-language tokenization and phoneme-mapping base, 12 languages |
| **IIT Bombay Hindi Corpus** | cfilt.iitb.ac.in/iitb_parallel | CC BY-NC | PhonNet: Hindi language-model base for phonological task generation (1.49M sentence pairs) |

---

## Condition → Dataset → Module Quick Reference (Batch 1)

| # | Condition | Module(s) | Primary Datasets | Detection Modality |
|---|---|---|---|---|
| 1 | Dyslexia (Phonological) | GazeNet + PhonNet | ETDD70, FluencyBank | Eye-tracking regressions + phonological task RT |
| 2 | Dyslexia (Surface) | DrawNet + PhonNet | Dyslexia HW (Kaggle), UCLASS | Handwriting CNN + reading speed |
| 3 | Dyslexia (Deep/Mixed) | All 3 modules | ETDD70 + HW datasets + FluencyBank | Full multimodal fusion |
| 4 | Dyscalculia (Core) | NumNet | NSS Norms, PISA Math | Number comparison RT + subitizing accuracy |
| 5 | Dyscalculia (Access deficit) | NumNet + SpatialNet | PISA Math subsets | Symbolic processing + spatial tasks |
| 6 | Dysgraphia (Phonological) | DrawNet + PhonNet | IAM, Devanagari (Kaggle) | Handwriting kinematics + spelling audio |
| 7 | Dysgraphia (Motor) | DrawNet | IAM, CASIA, ICDAR | Stroke velocity, pressure, jerk CNN |
| 8 | ADHD — Inattentive | AttentNet + GazeNet | IEEE EEG ADHD, ADHD-200 | Sustained attention task + gaze deviation |
| 9 | ADHD — Hyperactive/Impulsive | AttentNet | IEEE EEG ADHD, KSoF | Response inhibition + commission errors |
| 10 | ADHD — Combined | AttentNet + FusionNet | IEEE EEG + ADHD-200 + Mendeley EEG | Full attention battery |
| 11 | Stuttering (Repetitions) | PhonNet | UCLASS, FluencyBank, SEP-28k | Audio: repetition event detection |
| 12 | Stuttering (Prolongations) | PhonNet | UCLASS, KSoF, LibriStutter | Audio: prolongation acoustic feature |
| 13 | Stuttering (Interjections) | PhonNet | FluencyBank, LibriStutter | Audio: interjection token detection |
| 14 | Cluttering (Fast Speech) | PhonNet | FluencyBank (rate analysis) | Audio: syllable rate + intelligibility |
| 15 | Word-Finding Difficulty | PhonNet | FluencyBank + fluency norms | Verbal fluency: items/60s + pause rate |
| 16 | Auditory Processing Disorder | PhonNet | ChildVox (noise-added), SCAN-3 norms | Target-in-noise accuracy degradation |
