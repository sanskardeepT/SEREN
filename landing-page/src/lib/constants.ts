// ─── Team Data ───────────────────────────────────────────────
export const teamData = {
  founder: {
    title: "Founder",
    members: ["Sanskardeep Talikote"],
  },
  coFounders: {
    title: "Co-Founders",
    members: ["Rekha Aiwale", "Aditya Paraskar", "Sanket Korde", "Abhishek Chavhan", "Ayema Ansari"],
  },
  research: {
    title: "Research Team",
    members: [
      "Megha T.",
      "Komal Aiwale",
      "Rekha Aiwale",
      "Risha H.",
      "Sanskardeep Talikote",
    ],
  },
  medicalAdvisory: {
    title: "Medical Advisory",
    members: ["Omkar Ghadge", "Dr. Manish Warke", "Shubham A.", "Asmitaa"],
  },
  creatorTeam: {
    title: "Creator & Awareness Team",
    members: [
      "@hanmant_yelgatte",
      "@med._insights",
      "@explore.wth.omey",
      "@feeling_fry",
      "@sanskardeeep",
    ],
  },
};

// ─── Conditions for Constellation ────────────────────────────
export const conditions: string[] = [
  "Dyslexia",
  "ADHD",
  "Dysgraphia",
  "Autism",
  "Speech Difficulties",
  "Learning Differences",
  "Processing Challenges",
  "Social Anxiety",
  "Selective Mutism",
  "Sensory Processing",
  "Communication",
  "Confidence",
  "Dyscalculia",
  "Auditory Processing",
  "Visual Processing",
  "Working Memory",
  "Executive Function",
  "Motor Coordination",
  "Developmental Delay",
  "Language Disorder",
  "Phonological Disorder",
  "Reading Comprehension",
  "Written Expression",
  "Emotional Regulation",
  "Attention Difficulties",
  "Hyperactivity",
  "Impulsivity",
  "Slow Processing Speed",
  "Nonverbal Learning",
  "Intellectual Giftedness",
  "Twice-Exceptional",
  "Oppositional Behavior",
  "Conduct Challenges",
  "Separation Anxiety",
  "Generalized Anxiety",
  "Childhood Depression",
  "Obsessive Patterns",
  "Tic Disorders",
  "Stuttering",
  "Apraxia of Speech",
  "Receptive Language",
  "Expressive Language",
  "Pragmatic Language",
  "Social Communication",
  "Echolalia",
  "Hyperlexia",
  "Math Reasoning",
  "Number Sense",
  "Spatial Awareness",
  "Time Management",
  "Organization Skills",
  "Self-Regulation",
  "Peer Relationships",
  "Emotional Awareness",
  "Sensory Sensitivity",
  "Proprioception",
  "Vestibular Processing",
  "Tactile Sensitivity",
  "Fine Motor Skills",
  "Gross Motor Skills",
  "Hand-Eye Coordination",
  "Visual-Motor Integration",
  "Memory Retention",
  "Cognitive Flexibility",
];

// ─── Stories for Storytelling Section ─────────────────────────
export const stories = [
  {
    label: "He was called lazy.",
    reveal: "He simply learned differently.",
  },
  {
    label: "Sometimes silence isn't attitude.",
    reveal: "Sometimes it's a child asking for help.",
  },
  {
    label: "The problem isn't every child.",
    reveal: "The problem is that we expect every child to learn the same way.",
  },
];

// ─── Target Audiences ────────────────────────────────────────
export const audiences = [
  {
    title: "Schools",
    description:
      "Identify learning differences earlier. Support every child before they fall behind. Build classrooms where every learner can thrive.",
    icon: "School" as const,
  },
  {
    title: "Parents",
    description:
      "Understand your child's unique way of learning. Replace uncertainty with clarity, and worry with confidence.",
    icon: "Heart" as const,
  },
  {
    title: "Children",
    description:
      "Every child deserves to feel seen. SEREN helps the world understand what makes each child extraordinary.",
    icon: "Sparkles" as const,
  },
  {
    title: "Researchers",
    description:
      "Contribute to a growing understanding of childhood development. Join a mission rooted in evidence and empathy.",
    icon: "Microscope" as const,
  },
  {
    title: "Educators",
    description:
      "Gain insights to personalise learning. Understand the children in your care beyond the classroom.",
    icon: "BookOpen" as const,
  },
  {
    title: "Healthcare Professionals",
    description:
      "Support early identification and holistic developmental understanding, informed by multidisciplinary research.",
    icon: "Stethoscope" as const,
  },
];

// ─── Trust Timeline ──────────────────────────────────────────
export const timelineSteps = [
  {
    title: "Research",
    description: "Extensive literature review and multidisciplinary consultation",
    completed: true,
  },
  {
    title: "Validation",
    description: "Medical advisory review and educational insight integration",
    completed: true,
  },
  {
    title: "Development",
    description: "Building the platform with care and precision",
    completed: true,
  },
  {
    title: "Early Access",
    description: "Opening doors to schools and families",
    completed: false,
    current: true,
  },
  {
    title: "Future Platform",
    description: "Expanding to healthcare, government, and research ecosystems",
    completed: false,
  },
];

// ─── Business Model ──────────────────────────────────────────
export const businessPillars = [
  {
    title: "Schools",
    subtitle: "Today",
    points: ["Institution partnerships", "Educational ecosystem integration", "Classroom-level insights"],
  },
  {
    title: "Parents",
    subtitle: "Today",
    points: ["Home support", "Progress understanding", "Early guidance"],
  },
  {
    title: "Future",
    subtitle: "Tomorrow",
    points: ["Healthcare ecosystem", "Government collaborations", "Research partnerships"],
  },
];

// ─── Navigation Links ────────────────────────────────────────
export const navLinks = [
  { label: "Mission", href: "#mission" },
  { label: "Research", href: "#research" },
  { label: "For You", href: "#audience" },
  { label: "Journey", href: "#trust" },
  { label: "Team", href: "#team" },
  { label: "Contact", href: "#contact" },
];
