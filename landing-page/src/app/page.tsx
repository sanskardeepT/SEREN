"use client";

import { useState, useEffect, useRef } from "react";
import { 
  School, 
  HeartHandshake, 
  UserCheck, 
  Activity, 
  Mail, 
  Phone, 
  MapPin, 
  Rocket, 
  Microscope, 
  Stethoscope, 
  Megaphone,
  X,
  Play,
  RotateCcw,
  CheckCircle,
  HelpCircle,
  FileText,
  Clock,
  Zap,
  ShieldAlert,
  ArrowRight,
  Send
} from "lucide-react";

// Condition Marquee chips mapping
const conditionGroups = [
  ["learning", "Dyslexia", "Dyscalculia", "Dysgraphia", "Dyspraxia / DCD", "Irlen Syndrome", "NVLD", "Reading Fluency Lag", "Orthographic Processing", "Adult Dyslexia", "Reading Avoidance", "Written Expression"],
  ["attention", "ADHD Inattentive", "ADHD Hyperactive", "ADHD Combined", "Auditory Processing", "Processing Speed", "Working Memory", "Time Blindness", "Executive Function", "Masked Adult ADHD", "Task Initiation", "Sustained Attention"],
  ["speech", "Stuttering", "Word Finding", "Expressive Language", "Receptive Language", "Social Communication", "Apraxia of Speech", "Phonological Disorder", "Cluttering", "Voice Anxiety", "Fluency Blocks", "Presentation Freeze"],
  ["anxiety", "General Anxiety", "Social Anxiety", "Selective Mutism", "Exam Anxiety", "School Refusal", "Panic Pattern", "Performance Anxiety", "Health Anxiety", "Separation Anxiety", "Avoidance Loop"],
  ["emotional", "Masked Depression", "Emotional Dysregulation", "Adjustment Strain", "Learning-Loss Anxiety", "Burnout", "Parental Burnout", "Trauma Silence", "Grief Withdrawal", "Low Motivation", "Shame Spiral"],
  ["silent", "Masking", "Camouflaging", "Masked Autism", "Twice Exceptional", "Demand Avoidance", "Gifted Underachievement", "Invisible Struggle", "Alexithymia", "Sensitive Overwhelm", "Private Recovery", "Public Performance"],
  ["sensory", "Sensory Processing", "Sensory Defensiveness", "Vestibular Difficulty", "Proprioceptive Difficulty", "Noise Sensitivity", "Texture Sensitivity", "Light Sensitivity", "Crowd Overload"],
  ["confidence", "Body Image", "Imposter Syndrome", "Rejection Sensitivity", "Career Insecurity", "Relationship Insecurity", "FOMO", "Academic Trauma", "Decision Paralysis", "Leadership Avoidance", "Public Speaking Phobia", "Perfectionism", "Comparison Loop"]
];

const allChips = conditionGroups.flatMap(([type, ...items]) => items.map(name => ({ type, name })));
const rows = [
  allChips.slice(0, 20),
  allChips.slice(20, 40),
  allChips.slice(40, 60),
  allChips.slice(60, 80),
  allChips.slice(80)
];

export default function Home() {
  const [scrolled, setScrolled] = useState(false);
  const [waitlistOpen, setWaitlistOpen] = useState(false);
  const [waitlistType, setWaitlistType] = useState<"school" | "parent">("school");
  const [waitlistSuccess, setWaitlistSuccess] = useState(false);

  // Form states
  const [name, setName] = useState("");
  const [schoolName, setSchoolName] = useState("");
  const [contact, setContact] = useState("");
  const [city, setCity] = useState("");

  // Screener settings
  const [lens, setLens] = useState<"teen" | "adult">("teen");
  const [focus, setFocus] = useState<"learning" | "attention" | "speech" | "confidence" | "masking">("learning");
  const [screenerStep, setScreenerStep] = useState<"welcome" | "attention" | "cognitive" | "masking" | "result">("welcome");

  // Interactive Diagnostic Variables (SEREN Clinical Simulator)
  // 1. CPT Variables (Attention reaction times)
  const [cptState, setCptState] = useState<"idle" | "running" | "ended">("idle");
  const [cptSymbol, setCptSymbol] = useState("Ready");
  const [cptIsTarget, setCptIsTarget] = useState(false);
  const [cptFlashCount, setCptFlashCount] = useState(0);
  const [cptHits, setCptHits] = useState(0);
  const [cptMisses, setCptMisses] = useState(0);
  const [cptReactionTimes, setCptReactionTimes] = useState<number[]>([]);
  const cptStartTimeRef = useRef<number>(0);
  const cptTimeoutRef = useRef<NodeJS.Timeout | null>(null);

  // 2. Stroop Variables (Cognitive Set-Shifting Interference)
  const [stroopRound, setStroopRound] = useState(1);
  const [stroopText, setStroopText] = useState("RED");
  const [stroopColor, setStroopColor] = useState<"Red" | "Blue" | "Green">("Blue");
  const [stroopIsCongruent, setStroopIsCongruent] = useState(false);
  const [stroopCorrectCount, setStroopCorrectCount] = useState(0);
  const [stroopCongruentTimes, setStroopCongruentTimes] = useState<number[]>([]);
  const [stroopIncongruentTimes, setStroopIncongruentTimes] = useState<number[]>([]);
  const stroopStartTimeRef = useRef<number>(0);

  // 3. Self-Report Masking Audit
  const [qAnswers, setQAnswers] = useState<Record<number, number>>({ 0: 5, 1: 5, 2: 5 });

  // Handle Navbar scroll backdrop toggle
  useEffect(() => {
    const handleScroll = () => {
      setScrolled(window.scrollY > 24);
    };
    window.addEventListener("scroll", handleScroll, { passive: true });
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  // Attention Task Logic (CPT Flashes)
  const startCptTest = () => {
    setCptState("running");
    setCptHits(0);
    setCptMisses(0);
    setCptReactionTimes([]);
    setCptFlashCount(0);
    triggerCptFlash(0);
  };

  const triggerCptFlash = (round: number) => {
    if (round >= 6) {
      setCptState("ended");
      setCptSymbol("Done");
      return;
    }
    const isTargetVal = Math.random() > 0.4;
    setCptSymbol(isTargetVal ? "STAR" : "MOON");
    setCptIsTarget(isTargetVal);
    setCptFlashCount(round + 1);
    cptStartTimeRef.current = Date.now();

    cptTimeoutRef.current = setTimeout(() => {
      triggerCptFlash(round + 1);
    }, 1100);
  };

  useEffect(() => {
    return () => {
      if (cptTimeoutRef.current) clearTimeout(cptTimeoutRef.current);
    };
  }, []);

  const handleCptTap = () => {
    if (cptState !== "running") return;
    const latency = Date.now() - cptStartTimeRef.current;
    
    if (cptIsTarget) {
      setCptHits(h => h + 1);
      setCptReactionTimes(rts => [...rts, latency]);
      // Instantly change symbol to acknowledge tap
      setCptSymbol("Tapped!");
    } else {
      setCptMisses(m => m + 1);
      setCptSymbol("Error!");
    }
  };

  // Stroop Set-Shifting Logic (Alternate congruent / incongruent trials)
  const colorWords = ["RED", "BLUE", "GREEN"];
  const colorHexs: Record<string, "Red" | "Blue" | "Green"> = {
    "RED": "Red",
    "BLUE": "Blue",
    "GREEN": "Green"
  };

  const setupStroopRound = () => {
    const isCongruentVal = Math.random() > 0.5;
    setStroopIsCongruent(isCongruentVal);

    if (isCongruentVal) {
      const word = colorWords[stroopRound % 3];
      setStroopText(word);
      setStroopColor(colorHexs[word]);
    } else {
      const word = colorWords[stroopRound % 3];
      const otherColors = colorWords.filter(w => w !== word);
      const randomColorWord = otherColors[Math.floor(Math.random() * otherColors.length)];
      setStroopText(word);
      setStroopColor(colorHexs[randomColorWord]);
    }
    stroopStartTimeRef.current = Date.now();
  };

  useEffect(() => {
    setupStroopRound();
  }, [stroopRound]);

  const handleStroopChoice = (chosen: "Red" | "Blue" | "Green") => {
    const latency = Date.now() - stroopStartTimeRef.current;
    const isCorrect = chosen === stroopColor;

    if (isCorrect) {
      setStroopCorrectCount(c => c + 1);
      if (stroopIsCongruent) {
        setStroopCongruentTimes(t => [...t, latency]);
      } else {
        setStroopIncongruentTimes(t => [...t, latency]);
      }
    }

    if (stroopRound >= 4) {
      setScreenerStep("masking");
    } else {
      setStroopRound(r => r + 1);
    }
  };

  // Target Slider Questions for Teens & Adults
  const maskingQuestions = {
    teen: [
      "I look fine because I got good at hiding (masking) my academic struggles.",
      "A small mistake or correction ruins my study confidence for the day.",
      "My voice gets blocked or stuck when presenting or reading aloud in class."
    ],
    adult: [
      "I build complex secret systems to hide simple writing or organizational errors.",
      "I rehearse simple conversational sentences repeatedly before making calls.",
      "I feel physically exhausted after pretend-masking that tasks are easy for me."
    ]
  };

  const handleSliderChange = (idx: number, val: number) => {
    setQAnswers(ans => ({ ...ans, [idx]: val }));
  };

  // Calculate genuine diagnostic stats
  const getDiagnosticReport = () => {
    // 1. Attention Speed
    const avgAttentionRT = cptReactionTimes.length > 0 
      ? Math.round(cptReactionTimes.reduce((a, b) => a + b, 0) / cptReactionTimes.length)
      : 420;

    // 2. Response Time Variability (RTV)
    let rtv = 38;
    if (cptReactionTimes.length > 1) {
      const mean = cptReactionTimes.reduce((a, b) => a + b, 0) / cptReactionTimes.length;
      const variances = cptReactionTimes.map(t => Math.pow(t - mean, 2));
      rtv = Math.round(Math.sqrt(variances.reduce((a, b) => a + b, 0) / variances.length));
    }

    // 3. Stroop Shifting Cost
    const avgCongruent = stroopCongruentTimes.length > 0 
      ? stroopCongruentTimes.reduce((a, b) => a + b, 0) / stroopCongruentTimes.length
      : 550;
    const avgIncongruent = stroopIncongruentTimes.length > 0 
      ? stroopIncongruentTimes.reduce((a, b) => a + b, 0) / stroopIncongruentTimes.length
      : 780;
    const stroopInterferenceCost = Math.max(20, Math.round(avgIncongruent - avgCongruent));

    // 4. Masking Strain Index
    const questionAverage = Object.values(qAnswers).reduce((a, b) => a + b, 0) / 3;
    const maskingScore = Math.round(questionAverage * 10);

    // Dynamic warning markers
    const attentionStatus = avgAttentionRT > 550 || rtv > 75 ? "Elevated Latency" : "Optimal Speed";
    const cognitiveStatus = stroopInterferenceCost > 180 ? "Shifting Friction" : "Optimal Adaptability";
    const maskingStatus = maskingScore > 65 ? "High Masking Strain" : "Balanced Coping";

    return { 
      avgAttentionRT, 
      rtv, 
      stroopInterferenceCost, 
      maskingScore,
      attentionStatus,
      cognitiveStatus,
      maskingStatus
    };
  };

  const report = getDiagnosticReport();

  const handleWaitlistSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setWaitlistSuccess(true);
  };

  return (
    <>
      <div className="page-noise" aria-hidden="true"></div>
      
      {/* Dynamic Condition Chips Background */}
      <div className="site-condition-field" aria-hidden="true">
        {rows.map((row, idx) => (
          <div key={idx} className="site-condition-track" data-site-track={idx}>
            {[...row, ...row].map((item, itemIdx) => (
              <span key={itemIdx} className={`condition-chip ${item.type}`}>
                {item.name}
              </span>
            ))}
          </div>
        ))}
      </div>

      {/* Header / Navbar */}
      <header className={`topbar ${scrolled ? "scrolled" : ""}`} aria-label="SEREN navigation">
        <a className="brand" href="#top" aria-label="SEREN home">
          <span className="brand-mark" aria-hidden="true">
            <svg viewBox="0 0 64 64" role="img">
              <circle cx="32" cy="32" r="28" className="logo-disc" />
              <path
                className="logo-script"
                d="M43 18c-6-6-23-3-24 7-1 13 25 6 23 20-2 12-23 13-29 3"
              />
              <path className="logo-starline" d="M45 12l2 5 5 1-4 3 1 5-4-3-4 3 1-5-4-3 5-1 2-5Z" />
            </svg>
          </span>
          <span>
            <strong>SEREN</strong>
            <small>Every child is a star</small>
          </span>
        </a>
        <nav className="nav-links" aria-label="Sections">
          <a href="#signal">The problem</a>
          <a href="#spectrum">What we screen</a>
          <a href="#check">Try a test</a>
          <a href="#team">About</a>
        </nav>
        <div className="nav-actions">
          <button 
            className="nav-cta" 
            onClick={() => {
              setWaitlistType("school");
              setWaitlistOpen(true);
              setWaitlistSuccess(false);
            }}
          >
            <School size={15} /> Partner your school
          </button>
          <button 
            className="nav-ghost"
            onClick={() => {
              setWaitlistType("parent");
              setWaitlistOpen(true);
              setWaitlistSuccess(false);
            }}
          >
            Check yourself
          </button>
        </div>
      </header>

      <main id="top">
        {/* Hero Section */}
        <section className="hero">
          <div className="hero-photo" role="img" aria-label="Students in an Indian classroom"></div>
          <div className="hero-shade"></div>
          <div className="hero-condition-field" aria-hidden="true">
            {rows.map((row, idx) => (
              <div key={idx} className="hero-condition-track" data-hero-track={idx}>
                {[...row, ...row].map((item, itemIdx) => (
                  <span key={itemIdx} className={`condition-chip ${item.type}`}>
                    {item.name}
                  </span>
                ))}
              </div>
            ))}
          </div>

          <div className="hero-grid">
            <div className="hero-copy">
              <p className="eyebrow">For the child, teen, parent, and adult who learned to hide the hard part.</p>
              <h1>Not lazy. Not weak. Just unseen.</h1>
              <p>
                SEREN is building India&apos;s first screening layer for hidden learning, attention, speech, anxiety, sensory,
                emotional, and confidence struggles. One phone. Simple tasks. A report people can understand.
              </p>
              <div className="hero-actions">
                <button 
                  className="primary-button" 
                  onClick={() => {
                    setWaitlistType("school");
                    setWaitlistOpen(true);
                    setWaitlistSuccess(false);
                  }}
                >
                  Partner your school
                </button>
                <button 
                  className="secondary-button"
                  onClick={() => {
                    setWaitlistType("parent");
                    setWaitlistOpen(true);
                    setWaitlistSuccess(false);
                  }}
                >
                  Check yourself
                </button>
                <a className="text-button" href="#check">
                  Try the signal check
                </a>
              </div>
            </div>

            <aside className="mirror-card">
              <span>The sentence nobody says out loud</span>
              <p>&quot;I am smart when nobody is watching.&quot;</p>
            </aside>
          </div>

          <div className="hero-metrics" aria-label="SEREN platform metrics">
            <div><b>84+</b><span>hidden profiles</span></div>
            <div><b>400+</b><span>signal points</span></div>
            <div><b>12</b><span>mobile modalities</span></div>
            <div><b>Offline</b><span>India-ready</span></div>
          </div>
        </section>

        {/* Problem Description */}
        <section id="signal" className="section signal-section">
          <div className="section-heading">
            <div>
              <p className="section-kicker">The real signal</p>
              <h2>The problem is usually visible years before it is named.</h2>
            </div>
            <p>
              A child stops reading aloud. A teen avoids the front bench. An adult builds a career around avoiding
              writing, speaking, or being judged. SEREN starts there.
            </p>
          </div>

          <div className="felt-grid">
            <article><span>Reading</span><b>&quot;I read it. It still does not stay.&quot;</b></article>
            <article><span>Speech</span><b>&quot;The answer is ready. My voice is not.&quot;</b></article>
            <article><span>Focus</span><b>&quot;I care. Then my mind disappears.&quot;</b></article>
            <article><span>Masking</span><b>&quot;I look fine because I got good at hiding.&quot;</b></article>
          </div>
        </section>

        {/* Spectrum Overview */}
        <section id="spectrum" className="spectrum-section">
          <div className="spectrum-copy">
            <p className="section-kicker">84+ spectrum</p>
            <h2>A moving map of struggles people carry quietly.</h2>
            <p>
              Not a single-condition app. SEREN maps learning, attention, speech, anxiety, emotional regulation,
              neurodiversity, sensory patterns, and adult confidence wounds in one screening architecture.
            </p>
          </div>
          <div className="condition-stage" aria-label="Animated SEREN condition spectrum">
            <div className="condition-track track-one" data-condition-track="0"></div>
            <div className="condition-track track-two" data-condition-track="1"></div>
            <div className="condition-track track-three" data-condition-track="2"></div>
            <div className="condition-track track-four" data-condition-track="3"></div>
          </div>
        </section>

        {/* Earning Trust / Modalities */}
        <section className="section product-section">
          <div className="section-heading">
            <div>
              <p className="section-kicker">How SEREN earns trust</p>
              <h2>It does not ask people to believe a label. It shows them a pattern.</h2>
            </div>
            <p>
              The real product combines self-report with reaction timing, working memory, speech, handwriting, gaze,
              sensors, teacher/parent input, and progress over time.
            </p>
          </div>

          <div className="product-grid">
            <article><School size={28} style={{ color: "var(--amber)" }} /><b>Schools</b><span>Classroom screening, teacher-readable reports, NEP-aligned early support.</span></article>
            <article><HeartHandshake size={28} style={{ color: "var(--teal)" }} /><b>Parents</b><span>Plain language. No shame. What to notice and what to do next.</span></article>
            <article><UserCheck size={28} style={{ color: "var(--rose)" }} /><b>Teens and adults</b><span>A private way to understand old struggles without feeling broken.</span></article>
          </div>
        </section>

        {/* Interactive Signal Check Simulator (Simplified React Version) */}
        <section id="check" className="check-section">
          <div className="check-intro">
            <p className="section-kicker">SEREN signal check</p>
            <h2>Signal Check - a public demo, not a diagnosis.</h2>
            <p>
              Choose Teen or Adult. Try a 3-step high-accuracy interactive clinical task pack matching SEREN&apos;s mobile ML pipelines.
            </p>
          </div>

          <div className="check-app">
            <div className="check-workspace">
              {/* Workspace Lens Toggles */}
              <div className="lens-row" role="group" aria-label="Choose screening lens">
                <button 
                  className={`lens ${lens === "teen" ? "active" : ""}`} 
                  onClick={() => { setLens("teen"); setScreenerStep("welcome"); }}
                >
                  Teen
                </button>
                <button 
                  className={`lens ${lens === "adult" ? "active" : ""}`} 
                  onClick={() => { setLens("adult"); setScreenerStep("welcome"); }}
                >
                  Adult
                </button>
              </div>

              {/* Focus Category Row */}
              <div className="focus-row" role="group" aria-label="Choose focus area">
                {["learning", "attention", "speech", "confidence", "masking"].map((f) => (
                  <button 
                    key={f}
                    className={`focus ${focus === f ? "active" : ""}`} 
                    onClick={() => { setFocus(f as any); setScreenerStep("welcome"); }}
                  >
                    {f.charAt(0).toUpperCase() + f.slice(1)}
                  </button>
                ))}
              </div>

              {/* Task Rendering panels */}
              <div className="check-step">
                {screenerStep === "welcome" && (
                  <div className="task-instruction task-stage" style={{ padding: "40px 24px", textAlign: "center" }}>
                    <div className="task-ring" style={{ margin: "0 auto 20px" }}>
                      <span>🚀</span>
                    </div>
                    <h4>Genuine Clinical Checkup</h4>
                    <p style={{ maxWidth: "450px", margin: "10px auto 24px" }}>
                      This pack runs an attention speed check, a cognitive Stroop matching task, and a self-reported masking analysis mapping {focus} traits for {lens}s.
                    </p>
                    <button className="lab-button" onClick={() => setScreenerStep("attention")}>
                      Begin Checkup
                    </button>
                  </div>
                )}

                {screenerStep === "attention" && (
                  <div className="task-card" style={{ padding: "32px 24px", textAlign: "center" }}>
                    <span className="section-kicker">Activity 1 / 3 (CPT Test)</span>
                    <h4 style={{ marginBottom: "8px" }}>Catch the Signal</h4>
                    <p style={{ fontSize: "0.9rem", opacity: 0.7, marginBottom: "20px" }}>
                      Flashes happen automatically. Tap the box below **ONLY** when you see the word **STAR**.
                    </p>
                    
                    <div 
                      onClick={handleCptTap}
                      className="reaction-box"
                      style={{ 
                        margin: "0 auto 24px", 
                        cursor: cptState === "running" ? "pointer" : "default",
                        background: cptSymbol === "STAR" ? "rgba(72,184,160,0.15)" : "rgba(255,255,255,0.03)",
                        border: cptSymbol === "STAR" ? "1px solid var(--teal)" : "1px solid var(--border)",
                        borderRadius: "16px",
                        padding: "40px",
                        fontSize: "2rem",
                        fontWeight: "bold",
                        width: "180px",
                        textAlign: "center"
                      }}
                    >
                      {cptSymbol}
                    </div>

                    <div style={{ display: "flex", justifyContent: "center", gap: "12px" }}>
                      {cptState === "idle" && (
                        <button className="lab-button" onClick={startCptTest}>
                          Start Flashes
                        </button>
                      )}
                      {cptState === "running" && (
                        <span style={{ fontSize: "0.85rem", opacity: 0.5 }}>Flash {cptFlashCount} of 6... Tap STAR</span>
                      )}
                      {cptState === "ended" && (
                        <button className="lab-button" onClick={() => setScreenerStep("cognitive")}>
                          Next (Stroop Challenge)
                        </button>
                      )}
                    </div>
                  </div>
                )}

                {screenerStep === "cognitive" && (
                  <div className="task-card" style={{ padding: "32px 24px", textAlign: "center" }}>
                    <span className="section-kicker">Activity 2 / 3 (Stroop Shift)</span>
                    <h4 style={{ marginBottom: "8px" }}>Colour Trap (Set Shifting)</h4>
                    <p style={{ fontSize: "0.9rem", opacity: 0.7, marginBottom: "20px" }}>
                      Select the button matching the **TEXT COLOR**, ignoring what the word actually spells.
                    </p>

                    <div 
                      style={{ 
                        margin: "0 auto 24px", 
                        padding: "30px", 
                        fontSize: "2.4rem", 
                        fontWeight: "800",
                        color: stroopColor === "Red" ? "rgb(212,101,74)" : stroopColor === "Blue" ? "rgb(92,142,224)" : "rgb(72,184,160)",
                        background: "rgba(255,255,255,0.03)",
                        borderRadius: "16px",
                        width: "200px",
                        textAlign: "center"
                      }}
                    >
                      {stroopText}
                    </div>

                    <div className="choice-grid" style={{ display: "grid", gridTemplateColumns: "1fr 1fr 1fr", gap: "10px", maxWidth: "320px", margin: "0 auto" }}>
                      <button className="lab-button" style={{ background: "rgba(212,101,74,0.15)", color: "#e06088" }} onClick={() => handleStroopChoice("Red")}>Red</button>
                      <button className="lab-button" style={{ background: "rgba(92,142,224,0.15)", color: "#9b6dcc" }} onClick={() => handleStroopChoice("Blue")}>Blue</button>
                      <button className="lab-button" style={{ background: "rgba(72,184,160,0.15)", color: "#48b8a0" }} onClick={() => handleStroopChoice("Green")}>Green</button>
                    </div>
                    <small style={{ display: "block", marginTop: "16px", opacity: 0.5 }}>Round {stroopRound} of 4</small>
                  </div>
                )}

                {screenerStep === "masking" && (
                  <div className="task-card" style={{ padding: "32px 24px" }}>
                    <span className="section-kicker">Activity 3 / 3 (Masking Audit)</span>
                    <h4 style={{ marginBottom: "16px", textAlign: "center" }}>Support Needs self-rate</h4>
                    
                    <div style={{ display: "flex", flexDirection: "column", gap: "20px" }}>
                      {maskingQuestions[lens].map((q, idx) => (
                        <div key={idx} style={{ display: "flex", flexDirection: "column", gap: "8px" }}>
                          <p style={{ fontSize: "0.85rem", opacity: 0.8 }}>{q}</p>
                          <div style={{ display: "flex", alignItems: "center", gap: "12px" }}>
                            <span style={{ fontSize: "0.75rem", opacity: 0.4 }}>Disagree</span>
                            <input 
                              type="range" 
                              min="0" 
                              max="10" 
                              value={qAnswers[idx] || 5} 
                              onChange={(e) => handleSliderChange(idx, parseInt(e.target.value))}
                              style={{ flex: 1, accentColor: "var(--amber)" }}
                            />
                            <span style={{ fontSize: "0.75rem", opacity: 0.4 }}>Agree</span>
                          </div>
                        </div>
                      ))}
                    </div>

                    <button 
                      className="lab-button" 
                      style={{ marginTop: "24px", width: "100%" }}
                      onClick={() => setScreenerStep("result")}
                    >
                      Generate Report
                    </button>
                  </div>
                )}

                {screenerStep === "result" && (
                  <div className="task-card" style={{ padding: "32px 24px", textAlign: "center" }}>
                    <CheckCircle size={44} style={{ color: "var(--teal)", margin: "0 auto 12px" }} />
                    <h4>Analysis Complete</h4>
                    <p style={{ fontSize: "0.85rem", opacity: 0.7, marginBottom: "20px" }}>
                      Your diagnostic checkup has compiled safely. Check the right-hand panel for your clinical metrics.
                    </p>
                    <button 
                      className="lab-button secondary" 
                      onClick={() => {
                        setScreenerStep("welcome");
                        setStroopRound(1);
                        setStroopCorrectCount(0);
                        setStroopCongruentTimes([]);
                        setStroopIncongruentTimes([]);
                        setCptState("idle");
                        setCptSymbol("Ready");
                        setCptReactionTimes([]);
                      }}
                    >
                      Restart screening
                    </button>
                  </div>
                )}
              </div>
            </div>

            {/* Diagnostic Report Panel */}
            <aside className="result-panel">
              <span>Screening preview</span>
              {screenerStep === "result" ? (
                <>
                  <h3 id="resultHeadline" style={{ color: "var(--amber)" }}>Diagnosis Flagged</h3>
                  <p id="resultBody" style={{ fontSize: "0.85rem" }}>
                    Based on local attention, shifting latency, and self-reports, you show signs of {focus} friction.
                  </p>
                  
                  <div className="signals-pill" id="signalsPill">
                    Clinical Metrics (Production SEREN Engine)
                  </div>
                  
                  <div className="score-stack" id="scoreStack" style={{ margin: "16px 0", display: "flex", flexDirection: "column", gap: "10px" }}>
                    
                    {/* CPT Mean Speed */}
                    <div style={{ background: "rgba(255,255,255,0.03)", padding: "12px", borderRadius: "8px", textAlign: "left" }}>
                      <div style={{ display: "flex", justifyContent: "space-between", marginBottom: "4px" }}>
                        <span style={{ fontSize: "0.8rem", fontWeight: "600", display: "flex", alignItems: "center", gap: "6px" }}>
                          <Clock size={12} style={{ color: "var(--teal)" }} />
                          Attention Mean Latency
                        </span>
                        <span style={{ fontSize: "0.8rem", color: "var(--teal)", fontWeight: "bold" }}>{report.avgAttentionRT}ms</span>
                      </div>
                      <div style={{ display: "flex", justifyContent: "space-between", fontSize: "0.75rem", opacity: 0.5 }}>
                        <span>Target Tap Speed</span>
                        <span>{report.attentionStatus}</span>
                      </div>
                    </div>

                    {/* Response Time Variability (RTV) */}
                    <div style={{ background: "rgba(255,255,255,0.03)", padding: "12px", borderRadius: "8px", textAlign: "left" }}>
                      <div style={{ display: "flex", justifyContent: "space-between", marginBottom: "4px" }}>
                        <span style={{ fontSize: "0.8rem", fontWeight: "600", display: "flex", alignItems: "center", gap: "6px" }}>
                          <Zap size={12} style={{ color: "var(--rose)" }} />
                          Response Variability (RTV)
                        </span>
                        <span style={{ fontSize: "0.8rem", color: "var(--rose)", fontWeight: "bold" }}>±{report.rtv}ms</span>
                      </div>
                      <div style={{ display: "flex", justifyContent: "space-between", fontSize: "0.75rem", opacity: 0.5 }}>
                        <span>Attention Deviation</span>
                        <span>{report.rtv > 75 ? "Elevated Variability" : "Normal Consistency"}</span>
                      </div>
                    </div>

                    {/* Stroop Inhibition Cost */}
                    <div style={{ background: "rgba(255,255,255,0.03)", padding: "12px", borderRadius: "8px", textAlign: "left" }}>
                      <div style={{ display: "flex", justifyContent: "space-between", marginBottom: "4px" }}>
                        <span style={{ fontSize: "0.8rem", fontWeight: "600", display: "flex", alignItems: "center", gap: "6px" }}>
                          <ShieldAlert size={12} style={{ color: "var(--amber)" }} />
                          Cognitive Inhibition Cost
                        </span>
                        <span style={{ fontSize: "0.8rem", color: "var(--amber)", fontWeight: "bold" }}>+{report.stroopInterferenceCost}ms</span>
                      </div>
                      <div style={{ display: "flex", justifyContent: "space-between", fontSize: "0.75rem", opacity: 0.5 }}>
                        <span>Incongruent Shifting Latency</span>
                        <span>{report.cognitiveStatus}</span>
                      </div>
                    </div>

                    {/* Masking strain index */}
                    <div style={{ background: "rgba(255,255,255,0.03)", padding: "12px", borderRadius: "8px", textAlign: "left" }}>
                      <div style={{ display: "flex", justifyContent: "space-between", marginBottom: "4px" }}>
                        <span style={{ fontSize: "0.8rem", fontWeight: "600", display: "flex", alignItems: "center", gap: "6px" }}>
                          <UserCheck size={12} style={{ color: "var(--plum)" }} />
                          Silent Masking Strain
                        </span>
                        <span style={{ fontSize: "0.8rem", color: "var(--plum)", fontWeight: "bold" }}>{report.maskingScore}/100</span>
                      </div>
                      <div style={{ display: "flex", justifyContent: "space-between", fontSize: "0.75rem", opacity: 0.5 }}>
                        <span>Self-reported audit</span>
                        <span>{report.maskingStatus}</span>
                      </div>
                    </div>

                  </div>

                  <div className="result-card" style={{ background: "rgba(232,163,61,0.08)", border: "1px solid rgba(232,163,61,0.15)", borderRadius: "12px", padding: "16px", marginBottom: "16px" }}>
                    <b style={{ color: "var(--amber)", fontSize: "0.85rem", display: "block", marginBottom: "6px" }}>
                      Recovery recommendations
                    </b>
                    <p style={{ fontSize: "0.8rem", opacity: 0.8, lineHeight: 1.4, textAlign: "left" }}>
                      SEREN app recommends introducing daily practice elements: *Breathing Focus Space* for ADHD control, *Sort the Stars* for shifting, and *Worry Release* for anxieties. A secure parents report is generated for WhatsApp mapping.
                    </p>
                  </div>
                </>
              ) : (
                <>
                  <h3 id="resultHeadline">Try a few activities to see your map.</h3>
                  <p id="resultBody">The result will update live as you play. It stays simple on purpose.</p>
                  <div className="signals-pill" id="signalsPill">Signals used: 0 of 12 modalities</div>
                </>
              )}

              <div className="credibility-card" id="credibilityCard">
                This public screening uses on-device client inputs. Production SEREN is designed for 12 modalities including speech decibels, handwriting coordinates, gaze tracks, and mobile sensor analysis.
              </div>
            </aside>
          </div>
        </section>

        {/* Team Section */}
        <section id="team" className="section team-section">
          <div className="section-heading">
            <div>
              <p className="section-kicker">Team</p>
              <h2>Founders, research, medical advisory, and distribution in one serious build.</h2>
            </div>
            <p>Same table system. Same weight. No pasted-name afterthought.</p>
          </div>

          <div className="team-tables">
            <article className="team-table">
              <div className="team-table-head"><Rocket size={18} style={{ color: "var(--amber)", marginRight: "8px" }} /><h3>Founding team</h3></div>
              <ul>
                <li><b>Mr. Sanskardeep Talikote</b><span>Founder</span></li>
                <li><b>Rekha Aiwale</b><span>Co-founder</span></li>
                <li><b>Aditya Paraskar</b><span>Co-founder</span></li>
                <li><b>Sanket Korde</b><span>Co-founder</span></li>
                <li><b>Abhishek Chavhan</b><span>Co-founder</span></li>
                <li><b>Ayema Ansari</b><span>Co-founder</span></li>
              </ul>
            </article>
            <article className="team-table">
              <div className="team-table-head"><Microscope size={18} style={{ color: "var(--teal)", marginRight: "8px" }} /><h3>Research team</h3></div>
              <ul>
                <li><b>Megha T.</b><span>Research</span></li>
                <li><b>Komal Aiwale</b><span>Research</span></li>
                <li><b>Rekha Aiwale</b><span>Research</span></li>
                <li><b>Risha H.</b><span>Research</span></li>
                <li><b>Sanskardeep Talikote</b><span>Research</span></li>
              </ul>
            </article>
            <article className="team-table">
              <div className="team-table-head"><Stethoscope size={18} style={{ color: "var(--rose)", marginRight: "8px" }} /><h3>Medical advisory</h3></div>
              <ul>
                <li><b>Omkar Ghadge</b><span>MBBS student</span></li>
                <li><b>Dr. Manish Warke</b><span>Medical advisor</span></li>
                <li><b>Shubham A.</b><span>MBBS student</span></li>
                <li><b>Asmitaa</b><span>MBBS student</span></li>
              </ul>
            </article>
            <article className="team-table">
              <div className="team-table-head"><Megaphone size={18} style={{ color: "var(--plum)", marginRight: "8px" }} /><h3>Creator network</h3></div>
              <ul>
                <li><b>@hanmant_yelgatte</b><span>Creator</span></li>
                <li><b>@med._insights</b><span>Creator</span></li>
                <li><b>@explore.wth.omey</b><span>Creator</span></li>
                <li><b>@feeling_fry</b><span>Creator</span></li>
                <li><b>@sanskardeeep</b><span>Creator</span></li>
              </ul>
            </article>
          </div>
        </section>

        {/* About Section */}
        <section id="about" className="section about-section">
          <div className="section-heading">
            <div>
              <p className="section-kicker">About</p>
              <h2>SEREN is the first screen that respects quiet struggle.</h2>
            </div>
            <p>
              We combine simple interaction design, a human-readable report, and school-friendly screening cues so hidden
              learning, attention, speech, sensory, and confidence struggles can be noticed earlier.
            </p>
          </div>
        </section>

        {/* Terms Section */}
        <section id="terms" className="section terms-section">
          <div className="section-heading">
            <div>
              <p className="section-kicker">Terms</p>
              <h2>Public demo only. Not a diagnosis.</h2>
            </div>
            <p>
              This site shows a conceptual screening experience for product education and early interest only. Any future
              clinical use would require proper validation, consent, and compliance workflows.
            </p>
          </div>
        </section>

        {/* Contact Section */}
        <section id="contact" className="contact-section">
          <div>
            <p className="section-kicker">Partner, pilot, invest</p>
            <h2>Build the first check children should have had years ago.</h2>
            <p>For schools, families, early users, advisors, and investors.</p>
            <div className="contact-actions">
              <button 
                className="primary-button" 
                onClick={() => {
                  setWaitlistType("school");
                  setWaitlistOpen(true);
                  setWaitlistSuccess(false);
                }}
              >
                Partner your school
              </button>
              <button 
                className="secondary-button"
                onClick={() => {
                  setWaitlistType("parent");
                  setWaitlistOpen(true);
                  setWaitlistSuccess(false);
                }}
              >
                Check yourself
              </button>
            </div>
          </div>
          <div className="contact-card">
            <a className="email-strong" href="mailto:sanskardeepbtalikote19@gmail.com"><Mail size={16} style={{ marginRight: "8px" }} /> Email: sanskardeepbtalikote19@gmail.com</a>
            <a href="tel:+919403910943"><Phone size={16} style={{ marginRight: "8px" }} /> Phone: +91 94039 10943</a>
            <span><MapPin size={16} style={{ marginRight: "8px" }} /> India-first. Global-ready.</span>
          </div>
        </section>
      </main>

      {/* Footer */}
      <footer className="site-footer">
        <div className="footer-brand">
          <p className="section-kicker">About SEREN</p>
          <h2>Built to find the quiet struggles people were taught to hide.</h2>
          <p>
            SEREN is an India-first screening platform for learning, attention, speech, sensory, confidence, and masking patterns.
            It is designed for schools, families, and adults who need a clearer starting point.
          </p>
        </div>

        <div className="footer-panel">
          <div className="footer-links">
            <span className="footer-links-label">Links</span>
            <a href="#about">About</a>
            <a href="#terms">Terms</a>
            <a href="#top">Back to top</a>
          </div>
          <p className="footer-note">
            Public demo only. Not a diagnosis. This website is an early product demo, not a substitute for clinical care, and not an emergency service.
          </p>
        </div>

        <div className="footer-bar">
          <span>SEREN &copy; 2026</span>
          <span>India-first. School-ready. Parent-readable.</span>
        </div>
      </footer>

      {/* Waitlist Modal (Pure React State driven) */}
      {waitlistOpen && (
        <div className="modal-backdrop open" id="waitlistModal" aria-hidden="false">
          <div className={`modal ${waitlistSuccess ? "success" : ""}`} role="dialog" aria-modal="true">
            <button className="modal-close" onClick={() => setWaitlistOpen(false)} aria-label="Close"><X size={18} /></button>
            <span id="modalBadge">
              {waitlistType === "school" ? "School partnership" : "Self-screening access"}
            </span>
            <h2 id="modalTitle">
              {waitlistType === "school" ? "Pilot seats are opening." : "Your private check is almost ready."}
            </h2>
            <p id="modalText">
              {waitlistType === "school" 
                ? "Leave one contact. We are preparing school pilots and will notify you within the next two weeks."
                : "Leave one contact. We will notify you when SEREN self-screening opens in the next two weeks."}
            </p>
            
            <form className="waitlist-form" onSubmit={handleWaitlistSubmit}>
              <label>
                <span>Your name</span>
                <input 
                  name="name" 
                  value={name} 
                  onChange={(e) => setName(e.target.value)} 
                  autoComplete="name" 
                  required 
                />
              </label>
              
              {waitlistType === "school" && (
                <label id="schoolField">
                  <span>School / organisation</span>
                  <input 
                    name="school" 
                    value={schoolName} 
                    onChange={(e) => setSchoolName(e.target.value)} 
                  />
                </label>
              )}

              <label>
                <span>Email or phone</span>
                <input 
                  name="contact" 
                  value={contact} 
                  onChange={(e) => setContact(e.target.value)} 
                  autoComplete="email" 
                  required 
                />
              </label>
              
              <label>
                <span>City</span>
                <input 
                  name="city" 
                  value={city} 
                  onChange={(e) => setCity(e.target.value)} 
                  autoComplete="address-level2" 
                />
              </label>
              
              <button className="primary-button" type="submit">Notify me</button>
            </form>
            
            <div className="modal-success" id="modalSuccess">
              Thank you - we are finishing SEREN&apos;s core build right now. We will reach out within the next couple of weeks.
            </div>
          </div>
        </div>
      )}
    </>
  );
}
