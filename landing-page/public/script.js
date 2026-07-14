window.addEventListener("DOMContentLoaded", () => {
  if ("scrollRestoration" in history) history.scrollRestoration = "manual";
  if (window.lucide) window.lucide.createIcons();

  initNav();
  initConditionField();
  initModals();
  initSignalCheck();
});

function initNav() {
  const topbar = document.querySelector(".topbar");
  const onScroll = () => {
    topbar.classList.toggle("scrolled", window.scrollY > 24);
  };
  onScroll();
  window.addEventListener("scroll", onScroll, { passive: true });
  if (location.hash) setTimeout(() => document.querySelector(location.hash)?.scrollIntoView({ block: "start" }), 80);
}

function initConditionField() {
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
  const conditions = conditionGroups.flatMap(([type, ...items]) => items.map((name) => ({ type, name })));
  const rows = [
    conditions.slice(0, 20),
    conditions.slice(20, 40),
    conditions.slice(40, 60),
    conditions.slice(60, 80),
    conditions.slice(80)
  ];
  const chipHtml = (row) => [...row, ...row].map((item) => `<span class="condition-chip ${item.type}">${item.name}</span>`).join("");

  document.querySelectorAll("[data-condition-track]").forEach((track, index) => (track.innerHTML = chipHtml(rows[index] || conditions)));
  document.querySelectorAll("[data-hero-track]").forEach((track, index) => (track.innerHTML = chipHtml(rows[index] || conditions)));
  document.querySelectorAll("[data-site-track]").forEach((track, index) => (track.innerHTML = chipHtml(rows[(index + 2) % rows.length] || conditions)));
}

function initModals() {
  const backdrop = document.getElementById("waitlistModal");
  const modal = backdrop.querySelector(".modal");
  const form = document.getElementById("waitlistForm");
  const title = document.getElementById("modalTitle");
  const text = document.getElementById("modalText");
  const badge = document.getElementById("modalBadge");
  const schoolField = document.getElementById("schoolField");

  const close = () => {
    backdrop.classList.remove("open");
    backdrop.setAttribute("aria-hidden", "true");
    modal.classList.remove("success");
    form.reset();
  };

  document.querySelectorAll("[data-waitlist]").forEach((button) => {
    button.addEventListener("click", () => {
      const school = button.dataset.waitlist === "school";
      badge.textContent = school ? "School partnership" : "Self-screening access";
      title.textContent = school ? "Pilot seats are opening." : "Your private check is almost ready.";
      text.textContent = school
        ? "Leave one contact. We are preparing school pilots and will notify you within the next two weeks."
        : "Leave one contact. We will notify you when SEREN self-screening opens in the next two weeks.";
      schoolField.style.display = school ? "grid" : "none";
      backdrop.classList.add("open");
      backdrop.setAttribute("aria-hidden", "false");
      setTimeout(() => form.elements.name?.focus(), 50);
    });
  });

  form.addEventListener("submit", (event) => {
    event.preventDefault();
    modal.classList.add("success");
  });
  document.getElementById("modalClose").addEventListener("click", close);
  backdrop.addEventListener("click", (event) => event.target === backdrop && close());
  window.addEventListener("keydown", (event) => event.key === "Escape" && close());
}

function initSignalCheck() {
  const domains = {
    learning: {
      label: "Learning & reading",
      short: "Learning friction",
      plain: "reading, spelling, writing, memory, or number work feels heavier than it looks",
      intervention: "SEREN would suggest a phonics plus fluency path, rapid naming practice, working-memory supports, and teacher-readable classroom accommodations."
    },
    attention: {
      label: "Attention & focus",
      short: "Attention drift",
      plain: "focus starts strong, then drops, jumps, or resets slowly",
      intervention: "SEREN would suggest task chunking, focus-reset routines, visual timers, executive-function scaffolds, and teacher or parent context checks."
    },
    speech: {
      label: "Speech & fluency",
      short: "Expression pressure",
      plain: "the answer is ready inside, but speaking, naming, or presenting gets blocked",
      intervention: "SEREN would suggest fluency-safe speaking practice, phonological checks, low-pressure voice tasks, and referral prompts when speech-language support is needed."
    },
    confidence: {
      label: "Anxiety & confidence",
      short: "Confidence pressure",
      plain: "performance, comparison, mistakes, or body/social pressure changes how safe you feel",
      intervention: "SEREN would suggest a graded confidence ladder, reflection prompts, safe exposure practice, parent or mentor check-ins, and escalation guidance for high distress."
    },
    masking: {
      label: "Silent masking",
      short: "Masking pattern",
      plain: "you look okay outside while spending extra energy hiding the hard part",
      intervention: "SEREN would suggest an energy map, private-to-public support plan, low-shame disclosure scripts, and context changes before burnout builds."
    }
  };

  const lensData = {
    teen: {
      label: "Teen signal set",
      subject: "Your",
      prompts: {
        learning: ["I reread a sentence and still guess.", "I know the answer but writing slows me down."],
        attention: ["I lose the thread even when I care.", "I miss instructions that others catch."],
        speech: ["I avoid answering aloud.", "My voice gets stuck when people watch."],
        confidence: ["A small mistake ruins my confidence.", "I compare myself and feel behind."],
        masking: ["People think I am fine because I hide it.", "I am more capable alone than in public."]
      }
    },
    adult: {
      label: "Adult signal set",
      subject: "Your",
      prompts: {
        learning: ["I avoid long emails or forms.", "I reread messages because meaning slips."],
        attention: ["Time disappears while I switch tasks.", "I look busy but finish late."],
        speech: ["I avoid calls or presentations.", "I rehearse simple sentences too much."],
        confidence: ["I feel like I fooled people into trusting me.", "Exposure feels unsafe."],
        masking: ["I built systems to hide the hard parts.", "Simple tasks cost more energy than people know."]
      }
    }
  };

  const sharedQuestions = [
    "This gets worse when I am watched.",
    "I avoid it until the last moment.",
    "I recover slowly after mistakes.",
    "I hide it from people close to me.",
    "I can do it better in private than in public.",
    "I feel tired after pretending it is easy.",
    "I need more time than people expect.",
    "I change my behaviour so nobody notices.",
    "Pressure makes the same task harder.",
    "I have built small tricks to survive this."
  ];

  const state = {
    lens: "teen",
    focus: "learning",
    answers: {},
    taskScores: {},
    taskIndex: 0,
    taskStarted: false,
    advanceTimer: null,
    reactionTimer: null,
    sequence: [],
    sequenceInput: [],
    namingStart: 0
  };

  const taskShell = document.getElementById("taskShell");
  const taskProgress = document.getElementById("taskProgress");
  const scoreStack = document.getElementById("scoreStack");
  const resultHeadline = document.getElementById("resultHeadline");
  const resultBody = document.getElementById("resultBody");
  const nextStepTitle = document.getElementById("nextStepTitle");
  const nextStepBody = document.getElementById("nextStepBody");
  const taskTitle = document.getElementById("taskTitle");
  const taskCountLabel = document.getElementById("taskCountLabel");
  const signalsPill = document.getElementById("signalsPill");
  const domainReport = document.getElementById("domainReport");

  const coreTasks = [
    { id: "cpt", title: "Catch the Signal", icon: "S", instruction: "Tap only when you see STAR. Ignore MOON.", steps: ["Start", "Watch", "Tap STAR"], render: renderCpt },
    { id: "gng", title: "Stop or Go", icon: "Go", instruction: "Tap GO. Do not tap STOP.", steps: ["Watch word", "Tap GO", "Hold on STOP"], render: renderGoNoGo },
    { id: "memory", title: "Memory Castle", icon: "4", instruction: "Watch the four-symbol pattern, then repeat it in order.", steps: ["Watch", "Wait", "Repeat"], render: renderMemory },
    { id: "naming", title: "Word Racer", icon: "Ab", instruction: "Read the row out loud or silently, then tap Done.", steps: ["Start timer", "Read row", "Tap Done"], render: renderNaming },
    { id: "rhyme", title: "Sound Match", icon: "Rh", instruction: "Choose the pair that sounds alike.", steps: ["Read pairs", "Hear the sound", "Choose one"], render: renderRhyme },
    { id: "dots", title: "Quick Count", icon: "8", instruction: "Count the dots quickly and choose the number.", steps: ["Look once", "Count fast", "Pick number"], render: renderDots },
    { id: "stroop", title: "Colour Trap", icon: "C", instruction: "Tap the ink colour, not the written word.", steps: ["Ignore word", "See ink", "Tap colour"], render: renderStroop },
    { id: "text", title: "Tell Me a Story", icon: "T", instruction: "Write one honest line about what happens under pressure.", steps: ["Think", "Write one line", "Capture"], render: renderStory },
    { id: "timed", title: "Two Speeds", icon: "2x", instruction: "Tap the matching letter b, then score the pressure round.", steps: ["Choose calmly", "Notice errors", "Score"], render: renderTimed },
    { id: "repeat", title: "Deja Vu", icon: "D", instruction: "Repeat the row and tap Done when finished.", steps: ["Start timer", "Read row", "Tap Done"], render: renderRepeat }
  ];

  const focusTasks = {
    learning: [
      { id: "sortStars", title: "Sort the Stars", icon: "*", instruction: "Choose the group that matches the sample pattern.", steps: ["Read sample", "Compare choices", "Pick match"], render: renderSortStars },
      { id: "mirrorPattern", title: "Mirror the Pattern", icon: "M", instruction: "Find the option that mirrors the sample.", steps: ["Read sample", "Flip it", "Choose mirror"], render: renderMirrorPattern }
    ],
    attention: [
      { id: "targetSweep", title: "Target Sweep", icon: "A", instruction: "Tap A, B, C in order while ignoring X.", steps: ["Find A", "Find B", "Find C"], render: renderTargetSweep },
      { id: "focusReset", title: "Focus Reset", icon: "R", instruction: "Wait for NOW, then tap once.", steps: ["Press Begin", "Wait", "Tap NOW"], render: renderFocusReset }
    ],
    speech: [
      { id: "braveExplorer", title: "Brave Explorer", icon: "V", instruction: "Write the sentence you would say if nobody judged your voice.", steps: ["Imagine safe room", "Write sentence", "Capture"], render: renderBraveExplorer },
      { id: "catchSound", title: "Catch the Sound", icon: "Sa", instruction: "Choose the word that starts like school.", steps: ["Hear school", "Compare starts", "Choose"], render: renderCatchSound }
    ],
    confidence: [
      { id: "emotionTown", title: "Emotion Town", icon: "E", instruction: "Write what you do first when comparison hits.", steps: ["Notice feeling", "Name action", "Capture"], render: renderEmotionTown },
      { id: "pressureDial", title: "Pressure Dial", icon: "P", instruction: "Choose the situation that changes your answer the most.", steps: ["Read options", "Pick trigger", "Record"], render: renderPressureDial }
    ],
    masking: [
      { id: "hiddenMask", title: "Hidden Mask", icon: "H", instruction: "Choose the line closest to your public-private gap.", steps: ["Read lines", "Pick closest", "Record"], render: renderHiddenMask },
      { id: "energyMap", title: "Energy Map", icon: "En", instruction: "Pick where the hidden energy goes.", steps: ["Scan options", "Pick cost", "Record"], render: renderEnergyMap }
    ]
  };

  document.querySelectorAll(".lens").forEach((button) => {
    button.addEventListener("click", () => {
      document.querySelectorAll(".lens").forEach((item) => item.classList.remove("active"));
      button.classList.add("active");
      state.lens = button.dataset.lens;
      state.taskIndex = 0;
      state.taskStarted = false;
      clearTimeout(state.advanceTimer);
      clearTimeout(state.reactionTimer);
      renderTask();
      updateResult();
    });
  });

  document.querySelectorAll(".focus").forEach((button) => {
    button.addEventListener("click", () => setFocus(button.dataset.focus));
  });

  document.getElementById("prevTask").addEventListener("click", () => {
    clearTimeout(state.advanceTimer);
    state.taskIndex = Math.max(0, state.taskIndex - 1);
    state.taskStarted = false;
    renderTask();
  });
  document.getElementById("nextTask").addEventListener("click", () => {
    clearTimeout(state.advanceTimer);
    const pack = getTaskPack();
    if (state.taskIndex >= pack.length - 1) {
      document.querySelector(".result-panel")?.scrollIntoView({ behavior: "smooth", block: "start" });
      return;
    }
    state.taskIndex += 1;
    state.taskStarted = false;
    renderTask();
  });
  document.getElementById("retakePack").addEventListener("click", () => {
    state.taskScores[state.focus] = {};
    state.taskIndex = 0;
    state.taskStarted = false;
    renderTask();
    updateResult();
  });
  document.getElementById("tryAnotherFocus").addEventListener("click", () => {
    const keys = Object.keys(domains);
    setFocus(keys[(keys.indexOf(state.focus) + 1) % keys.length]);
  });

  function setFocus(focus) {
    state.focus = focus;
    state.taskIndex = 0;
    state.taskStarted = false;
    document.querySelectorAll(".focus").forEach((item) => item.classList.toggle("active", item.dataset.focus === focus));
    renderTask();
    updateResult();
  }

  function getTaskPack() {
    return [...coreTasks, ...focusTasks[state.focus]];
  }

  function getFocusScores(focus = state.focus) {
    if (!state.taskScores[focus]) state.taskScores[focus] = {};
    return state.taskScores[focus];
  }

  function renderProgress() {
    const pack = getTaskPack();
    const scores = getFocusScores();
    taskProgress.style.gridTemplateColumns = `repeat(${pack.length}, minmax(0, 1fr))`;
    taskProgress.innerHTML = pack
      .map((task, index) => {
        const isDone = Boolean(scores[task.id]);
        const isCurrent = index === state.taskIndex;
        return `<span class="progress-dot ${isDone ? "done" : ""} ${isCurrent ? "current" : ""}" title="${escapeHtml(task.title)}">${isDone ? escapeHtml(task.icon) : index + 1}</span>`;
      })
      .join("");
  }

  function renderTask() {
    clearTimeout(state.reactionTimer);
    clearTimeout(state.advanceTimer);
    const pack = getTaskPack();
    const task = pack[state.taskIndex];
    taskTitle.textContent = task.title;
    taskCountLabel.textContent = `Activity ${state.taskIndex + 1} / ${pack.length}`;
    document.getElementById("nextTask").textContent = state.taskIndex === pack.length - 1 ? "View report" : "Next activity";
    renderProgress();
    if (!state.taskStarted) {
      const progress = ((state.taskIndex + 1) / pack.length) * 100;
      taskShell.innerHTML = `
        <div class="task-instruction task-stage">
          <div class="task-ring" style="--progress:${progress}"><span>${state.taskIndex + 1}</span><small>${pack.length}</small></div>
          <div>
            <span class="section-kicker">Activity ${state.taskIndex + 1}</span>
            <h4>${escapeHtml(task.title)}</h4>
            <p>${escapeHtml(task.instruction)}</p>
            <button class="lab-button" id="startTask">Start ${escapeHtml(task.title)}</button>
          </div>
        </div>`;
      document.getElementById("startTask").addEventListener("click", () => {
        state.taskStarted = true;
        renderTask();
      });
      return;
    }
    task.render(task);
    enhanceActiveTask(task);
    updateResult();
  }

  function saveTask(id, score, note) {
    getFocusScores()[id] = { score: clamp(Math.round(score), 0, 100), note };
    renderProgress();
    updateResult();
    const readout = document.getElementById("taskReadout");
    const phase = document.getElementById("taskPhase");
    const confirmation = document.getElementById("taskConfirmation");
    if (readout) readout.textContent = `Recorded: ${note}`;
    if (phase) phase.textContent = "Recorded";
    if (confirmation) {
      confirmation.textContent = `Got it - ${note}.`;
      confirmation.classList.add("show");
    }
    clearTimeout(state.advanceTimer);
    state.advanceTimer = setTimeout(() => {
      const pack = getTaskPack();
      if (state.taskIndex < pack.length - 1) {
        state.taskIndex += 1;
        state.taskStarted = false;
        renderTask();
      } else {
        document.querySelector(".result-panel")?.scrollIntoView({ behavior: "smooth", block: "start" });
      }
    }, 1250);
  }

  function setTaskPhase(text) {
    const phase = document.getElementById("taskPhase");
    if (phase) phase.textContent = text;
  }

  function enhanceActiveTask(task) {
    const card = taskShell.querySelector(".task-card");
    if (!card || card.dataset.enhanced === "true") return;
    const pack = getTaskPack();
    const progress = ((state.taskIndex + 1) / pack.length) * 100;
    const playArea = document.createElement("div");
    playArea.className = "task-play-area";
    while (card.firstChild) playArea.appendChild(card.firstChild);

    const head = document.createElement("div");
    head.className = "task-active-head";
    head.innerHTML = `
      <div class="mini-demo">${escapeHtml(task.icon)}</div>
      <div>
        <span class="section-kicker">Activity ${state.taskIndex + 1}</span>
        <h4>${escapeHtml(task.title)}</h4>
        <p class="task-main-instruction">${escapeHtml(task.instruction)}</p>
      </div>
      <div class="task-ring small" style="--progress:${progress}"><span>${state.taskIndex + 1}</span><small>${pack.length}</small></div>
      <button class="task-help" id="taskHelp" aria-label="Show task instructions">?</button>
    `;

    const strip = document.createElement("div");
    strip.className = "how-strip";
    strip.innerHTML = (task.steps || ["Read", "Try", "Record"])
      .map((step, index) => `<span>${index + 1}. ${escapeHtml(step)}</span>`)
      .join("");

    const phase = document.createElement("div");
    phase.className = "task-state";
    phase.id = "taskPhase";
    phase.textContent = "Ready";

    const confirmation = document.createElement("div");
    confirmation.className = "task-confirmation";
    confirmation.id = "taskConfirmation";

    card.append(head, strip, phase, playArea, confirmation);
    card.dataset.enhanced = "true";
    document.getElementById("taskHelp").addEventListener("click", () => {
      clearTimeout(state.advanceTimer);
      state.taskStarted = false;
      renderTask();
    });
  }

  function simpleButtons(items) {
    return `<div class="choice-grid">${items.map((item) => `<button data-value="${escapeAttr(item)}">${escapeHtml(item)}</button>`).join("")}</div>`;
  }

  function renderCpt() {
    let hits = 0;
    let falseTaps = 0;
    let total = 0;
    taskShell.innerHTML = `<article class="task-card"><span>Reaction signal</span><p>Tap only when you see STAR. Ignore MOON.</p><div class="reaction-box" id="cptBox">Ready</div><button class="lab-button" id="cptStart">Run 8 flashes</button><small class="task-readout" id="taskReadout">No sample yet.</small></article>`;
    document.getElementById("cptStart").onclick = () => {
      setTaskPhase("Watching");
      const box = document.getElementById("cptBox");
      const run = () => {
        if (total >= 8) {
          saveTask("cpt", hits * 12 - falseTaps * 18 + 16, `${hits}/8 targets, ${falseTaps} false taps`);
          return;
        }
        const target = Math.random() > 0.42;
        box.textContent = target ? "STAR" : "MOON";
        box.dataset.target = target ? "1" : "0";
        total += 1;
        state.reactionTimer = setTimeout(run, 760);
      };
      box.onclick = () => {
        setTaskPhase("Tap captured");
        if (box.dataset.target === "1") hits += 1;
        else falseTaps += 1;
        box.style.transform = "scale(0.98)";
        setTimeout(() => (box.style.transform = ""), 120);
      };
      run();
    };
  }

  function renderGoNoGo() {
    let correct = 0;
    let trials = 0;
    taskShell.innerHTML = `<article class="task-card"><span>Inhibition signal</span><p>Tap GO. Do not tap STOP.</p><div class="reaction-box" id="goBox">GO</div><small class="task-readout" id="taskReadout">Tap the card for GO trials.</small></article>`;
    const box = document.getElementById("goBox");
    const next = () => {
      if (trials >= 7) {
        saveTask("gng", (correct / 7) * 100, `${Math.max(0, correct)}/7 control matches`);
        return;
      }
      const go = Math.random() > 0.36;
      setTaskPhase(go ? "Tap GO" : "Hold back");
      box.textContent = go ? "GO" : "STOP";
      box.dataset.go = go ? "1" : "0";
      box.classList.toggle("ready", go);
      box.classList.toggle("waiting", !go);
      trials += 1;
      state.reactionTimer = setTimeout(() => {
        if (!go) correct += 1;
        next();
      }, 850);
    };
    box.onclick = () => {
      clearTimeout(state.reactionTimer);
      setTaskPhase("Tap captured");
      correct += box.dataset.go === "1" ? 1 : -1;
      next();
    };
    next();
  }

  function renderMemory() {
    const symbols = ["Sun", "Leaf", "Wave", "Star"];
    taskShell.innerHTML = `<article class="task-card"><span>Working memory</span><p>Watch four symbols. Repeat them.</p><div class="memory-board">${symbols.map((x) => `<button data-memory="${x}">${x}</button>`).join("")}</div><button class="lab-button" id="showMemory">Show pattern</button><small class="task-readout" id="taskReadout">Not started.</small></article>`;
    const buttons = [...taskShell.querySelectorAll("[data-memory]")];
    document.getElementById("showMemory").onclick = () => {
      setTaskPhase("Watching");
      state.sequence = Array.from({ length: 4 }, () => buttons[Math.floor(Math.random() * buttons.length)].dataset.memory);
      state.sequenceInput = [];
      state.sequence.forEach((name, index) => setTimeout(() => {
        const btn = taskShell.querySelector(`[data-memory="${name}"]`);
        btn.classList.add("flash");
        setTimeout(() => btn.classList.remove("flash"), 260);
      }, index * 520));
      document.getElementById("taskReadout").textContent = "Now tap the pattern back.";
      setTimeout(() => setTaskPhase("Your turn"), 2100);
    };
    buttons.forEach((btn) => {
      btn.onclick = () => {
        state.sequenceInput.push(btn.dataset.memory);
        setTaskPhase(`${state.sequenceInput.length}/4 tapped`);
        btn.classList.add("selected");
        setTimeout(() => btn.classList.remove("selected"), 160);
        if (state.sequenceInput.length === 4) {
          const correct = state.sequenceInput.filter((x, i) => x === state.sequence[i]).length;
          saveTask("memory", correct * 25, `${correct}/4 order match`);
        }
      };
    });
  }

  function renderNaming() {
    const items = ["b", "d", "p", "q", "6", "9", "m", "w", "a", "e"];
    taskShell.innerHTML = `<article class="task-card"><span>Processing speed</span><p>Read the row. Tap done when finished.</p><div class="naming-row">${items.map((x) => `<span>${x}</span>`).join("")}</div><button class="lab-button" id="startNaming">Start</button><button class="lab-button secondary" id="doneNaming">Done</button><small class="task-readout" id="taskReadout">Measures rapid naming proxy.</small></article>`;
    document.getElementById("startNaming").onclick = () => {
      state.namingStart = performance.now();
      document.getElementById("taskReadout").textContent = "Timer running.";
      setTaskPhase("Timer running");
    };
    document.getElementById("doneNaming").onclick = () => {
      const ms = performance.now() - state.namingStart;
      saveTask("naming", 100 - Math.max(0, (ms - 3600) / 75), `${Math.round(ms)} ms naming`);
    };
  }

  function renderRhyme() {
    taskShell.innerHTML = `<article class="task-card"><span>Phonological signal</span><p>Which pair rhymes?</p>${simpleButtons(["cat-hat", "sun-pen", "book-bike", "fish-road"])}<small class="task-readout" id="taskReadout">Tap one answer.</small></article>`;
    taskShell.querySelectorAll("[data-value]").forEach((b) => {
      b.onclick = () => {
        setTaskPhase("Choice recorded");
        saveTask("rhyme", b.dataset.value === "cat-hat" ? 100 : 35, `selected ${b.dataset.value}`);
      };
    });
  }

  function renderDots() {
    const count = 3 + Math.floor(Math.random() * 7);
    taskShell.innerHTML = `<article class="task-card"><span>Number sense</span><p>Count fast. How many dots?</p><div class="dot-flash">${Array.from({ length: count }).map(() => "<span></span>").join("")}</div>${simpleButtons([String(count - 1), String(count), String(count + 1), String(count + 2)])}<small class="task-readout" id="taskReadout">Subitizing / number sense proxy.</small></article>`;
    taskShell.querySelectorAll("[data-value]").forEach((b) => {
      b.onclick = () => {
        setTaskPhase("Choice recorded");
        saveTask("dots", Number(b.dataset.value) === count ? 100 : 40, `dot count ${b.dataset.value}/${count}`);
      };
    });
  }

  function renderStroop() {
    const colors = [{ n: "RUST", c: "var(--rust)" }, { n: "PLUM", c: "var(--plum)" }, { n: "AMBER", c: "var(--amber)" }, { n: "INK", c: "var(--ink)" }];
    const word = colors[Math.floor(Math.random() * colors.length)];
    const ink = colors.filter((item) => item.n !== word.n)[Math.floor(Math.random() * 3)];
    taskShell.innerHTML = `<article class="task-card"><span>Executive conflict</span><p>Tap the ink color, not the word.</p><div class="stroop-word" style="color:${ink.c}">${word.n}</div>${simpleButtons(colors.map((x) => x.n))}<small class="task-readout" id="taskReadout">Executive function proxy.</small></article>`;
    taskShell.querySelectorAll("[data-value]").forEach((b) => {
      b.onclick = () => {
        setTaskPhase("Choice recorded");
        saveTask("stroop", b.dataset.value === ink.n ? 100 : 30, `ink answer ${b.dataset.value}/${ink.n}`);
      };
    });
  }

  function renderStory() {
    taskShell.innerHTML = `<article class="task-card"><span>Language and pressure</span><p>Finish this honestly: When people watch me perform, I...</p><textarea class="text-task" id="freeText"></textarea><button class="lab-button" id="scoreText">Capture story signal</button><small class="task-readout" id="taskReadout">Looks for worry, avoidance, freeze, comparison, and shame words.</small></article>`;
    document.getElementById("scoreText").onclick = () => scoreTextTask("text", "freeText");
  }

  function renderTimed() {
    let calmHits = 0;
    let pressureErrors = 0;
    taskShell.innerHTML = `<article class="task-card"><span>Pressure delta</span><p>Tap the matching letter b. First calm, then timed.</p>${simpleButtons(["b", "d", "p", "q"])}<button class="lab-button" id="timedMode">Score pressure round</button><small class="task-readout" id="taskReadout">Performance drop under pressure.</small></article>`;
    taskShell.querySelectorAll("[data-value]").forEach((b) => {
      b.onclick = () => {
        if (b.dataset.value === "b") calmHits += 1;
        else pressureErrors += 1;
        document.getElementById("taskReadout").textContent = `Calm hits ${calmHits}, pressure errors ${pressureErrors}`;
        setTaskPhase("Sampling");
      };
    });
    document.getElementById("timedMode").onclick = () => saveTask("timed", 78 + calmHits * 5 - pressureErrors * 16, `${calmHits} calm hits, ${pressureErrors} pressure errors`);
  }

  function renderRepeat() {
    const items = ["b", "d", "p", "q", "6", "9", "m", "w"];
    let start = 0;
    taskShell.innerHTML = `<article class="task-card"><span>Consistency signal</span><p>Repeat this quick row. Tap done when finished.</p><div class="naming-row">${items.map((x) => `<span>${x}</span>`).join("")}</div><button class="lab-button" id="startRepeat">Start</button><button class="lab-button secondary" id="doneRepeat">Done</button><small class="task-readout" id="taskReadout">Compares with earlier naming if available.</small></article>`;
    document.getElementById("startRepeat").onclick = () => {
      start = performance.now();
      document.getElementById("taskReadout").textContent = "Timer running.";
      setTaskPhase("Timer running");
    };
    document.getElementById("doneRepeat").onclick = () => {
      const ms = performance.now() - start;
      const prior = getFocusScores().naming?.note?.match(/(\d+)/)?.[1];
      const variance = prior ? Math.abs(ms - Number(prior)) : 700;
      saveTask("repeat", 100 - Math.min(70, variance / 48), `${Math.round(ms)} ms repeat sample`);
    };
  }

  function renderSortStars() {
    taskShell.innerHTML = `<article class="task-card"><span>Visual sorting</span><p>Pick the group that matches: star, star, moon.</p>${simpleButtons(["star star moon", "star moon moon", "moon star sun", "sun star moon"])}<small class="task-readout" id="taskReadout">Pattern sorting proxy.</small></article>`;
    taskShell.querySelectorAll("[data-value]").forEach((b) => {
      b.onclick = () => {
        setTaskPhase("Choice recorded");
        saveTask("sortStars", b.dataset.value === "star star moon" ? 100 : 42, `pattern choice: ${b.dataset.value}`);
      };
    });
  }

  function renderMirrorPattern() {
    taskShell.innerHTML = `<article class="task-card"><span>Spatial pattern</span><p>Sample: b d 6 9. Which option feels mirrored?</p>${simpleButtons(["d b 9 6", "b d 9 6", "p q 6 9", "6 9 b d"])}<small class="task-readout" id="taskReadout">Letter reversal and spatial load proxy.</small></article>`;
    taskShell.querySelectorAll("[data-value]").forEach((b) => {
      b.onclick = () => {
        setTaskPhase("Choice recorded");
        saveTask("mirrorPattern", b.dataset.value === "d b 9 6" ? 100 : 38, `mirror choice: ${b.dataset.value}`);
      };
    });
  }

  function renderTargetSweep() {
    const order = [];
    taskShell.innerHTML = `<article class="task-card"><span>Sequential attention</span><p>Tap A, B, C in order. Ignore X.</p>${simpleButtons(["X", "B", "A", "C", "X", "B"])}<small class="task-readout" id="taskReadout">Sequence target sweep.</small></article>`;
    taskShell.querySelectorAll("[data-value]").forEach((b) => {
      b.onclick = () => {
        order.push(b.dataset.value);
        setTaskPhase(`${order.length}/3 tapped`);
        const joined = order.join("");
        if (order.length >= 3) saveTask("targetSweep", joined.startsWith("ABC") ? 100 : 36, `sequence ${joined}`);
      };
    });
  }

  function renderFocusReset() {
    let ready = false;
    taskShell.innerHTML = `<article class="task-card"><span>Reset timing</span><p>Wait. Tap only after the card says NOW.</p><div class="reaction-box waiting" id="resetBox">WAIT</div><button class="lab-button" id="resetStart">Begin</button><small class="task-readout" id="taskReadout">Impulse and reset proxy.</small></article>`;
    const box = document.getElementById("resetBox");
    document.getElementById("resetStart").onclick = () => {
      ready = false;
      setTaskPhase("Waiting");
      box.textContent = "WAIT";
      box.className = "reaction-box waiting";
      state.reactionTimer = setTimeout(() => {
        ready = true;
        box.textContent = "NOW";
        box.className = "reaction-box ready";
        setTaskPhase("Tap now");
      }, 900 + Math.random() * 1100);
    };
    box.onclick = () => saveTask("focusReset", ready ? 100 : 22, ready ? "waited for reset cue" : "early tap before cue");
  }

  function renderBraveExplorer() {
    taskShell.innerHTML = `<article class="task-card"><span>Expression safety</span><p>Write the sentence you would say if nobody judged your voice.</p><textarea class="text-task" id="braveText"></textarea><button class="lab-button" id="scoreBrave">Capture voice signal</button><small class="task-readout" id="taskReadout">Looks for avoidance, freeze, and safe-expression language.</small></article>`;
    document.getElementById("scoreBrave").onclick = () => scoreTextTask("braveExplorer", "braveText");
  }

  function renderCatchSound() {
    taskShell.innerHTML = `<article class="task-card"><span>First sound</span><p>Which word starts like school?</p>${simpleButtons(["scale", "chair", "full", "goal"])}<small class="task-readout" id="taskReadout">Sound discrimination proxy.</small></article>`;
    taskShell.querySelectorAll("[data-value]").forEach((b) => {
      b.onclick = () => {
        setTaskPhase("Choice recorded");
        saveTask("catchSound", b.dataset.value === "scale" ? 100 : 44, `sound choice: ${b.dataset.value}`);
      };
    });
  }

  function renderEmotionTown() {
    taskShell.innerHTML = `<article class="task-card"><span>Confidence language</span><p>When comparison hits, what do you do first?</p><textarea class="text-task" id="emotionText"></textarea><button class="lab-button" id="scoreEmotion">Map emotion signal</button><small class="task-readout" id="taskReadout">Looks for comparison, shutdown, avoidance, and self-blame.</small></article>`;
    document.getElementById("scoreEmotion").onclick = () => scoreTextTask("emotionTown", "emotionText");
  }

  function renderPressureDial() {
    taskShell.innerHTML = `<article class="task-card"><span>Performance safety</span><p>Which situation changes your answer the most?</p>${simpleButtons(["being watched", "time limit", "public marks", "new people"])}<small class="task-readout" id="taskReadout">Pressure context signal.</small></article>`;
    taskShell.querySelectorAll("[data-value]").forEach((b) => {
      const heavy = ["being watched", "public marks"].includes(b.dataset.value);
      b.onclick = () => {
        setTaskPhase("Choice recorded");
        saveTask("pressureDial", heavy ? 52 : 74, `pressure trigger: ${b.dataset.value}`);
      };
    });
  }

  function renderHiddenMask() {
    taskShell.innerHTML = `<article class="task-card"><span>Public-private gap</span><p>Choose the line that feels closest.</p>${simpleButtons(["I look calm but crash later", "I ask for help early", "I need scripts to seem normal", "I do better alone"])}<small class="task-readout" id="taskReadout">Masking and recovery-cost signal.</small></article>`;
    taskShell.querySelectorAll("[data-value]").forEach((b) => {
      const signal = ["I look calm but crash later", "I need scripts to seem normal", "I do better alone"].includes(b.dataset.value);
      b.onclick = () => {
        setTaskPhase("Choice recorded");
        saveTask("hiddenMask", signal ? 46 : 84, `masking line: ${b.dataset.value}`);
      };
    });
  }

  function renderEnergyMap() {
    taskShell.innerHTML = `<article class="task-card"><span>Energy audit</span><p>Where does the hidden energy go?</p>${simpleButtons(["starting", "explaining", "social acting", "recovering"])}<small class="task-readout" id="taskReadout">Hidden load and recovery signal.</small></article>`;
    taskShell.querySelectorAll("[data-value]").forEach((b) => {
      const intense = ["social acting", "recovering"].includes(b.dataset.value);
      b.onclick = () => {
        setTaskPhase("Choice recorded");
        saveTask("energyMap", intense ? 50 : 72, `energy cost: ${b.dataset.value}`);
      };
    });
  }

  function scoreTextTask(id, inputId) {
    const input = document.getElementById(inputId);
    const text = input.value.toLowerCase();
    const hits = ["can't", "avoid", "scared", "freeze", "fail", "stupid", "behind", "judge", "sorry", "perfect", "hide", "panic", "ashamed", "tired"].filter((w) => text.includes(w)).length;
    const lengthScore = Math.min(32, text.length / 5);
    setTaskPhase("Text captured");
    saveTask(id, Math.min(100, hits * 13 + lengthScore + (text.length > 18 ? 30 : 8)), `${hits} pressure words, ${text.length} chars`);
  }

  function updateResult() {
    const selected = computeFocusRisk(state.focus);
    const band = selected.risk > 66 ? "higher" : selected.risk > 36 ? "moderate" : "lighter";
    const signalsUsed = selected.answerCount + selected.taskCount;
    resultHeadline.textContent = `${domains[state.focus].short} looks ${band}.`;
    resultBody.textContent = `${lensData[state.lens].subject} map is built from how the activities behaved. The strongest sampled pattern is: ${domains[state.focus].plain}.`;
    signalsPill.textContent = `Signals used: ${signalsUsed} live inputs of 12 production modalities`;
    scoreStack.innerHTML = [
      ["Felt signals", selected.selfScore],
      ["Activity friction", selected.taskRisk],
      ["Composite confidence", selected.risk]
    ].map(([label, value]) => scoreRow(label, value)).join("");
    domainReport.innerHTML = Object.keys(domains)
      .map((focus) => {
        const data = computeFocusRisk(focus);
        const sampled = data.answerCount + data.taskCount > 0;
        const value = sampled ? data.risk : 0;
        return `<div class="domain-note">${scoreRow(domains[focus].label, value)}<p>${sampled ? domains[focus].plain : "Try this focus area to build a real signal here."}</p></div>`;
      })
      .join("");
    nextStepTitle.textContent = "Matched SEREN intervention";
    nextStepBody.textContent = domains[state.focus].intervention;
  }

  function computeFocusRisk(focus) {
    const keyPrefix = `${state.lens}-${focus}-`;
    const answerValues = Object.entries(state.answers).filter(([k]) => k.startsWith(keyPrefix)).map(([, v]) => v);
    const selfScore = answerValues.length ? (answerValues.reduce((a, b) => a + b, 0) / (answerValues.length * 3)) * 100 : 0;
    const taskValues = Object.values(getFocusScores(focus)).map((x) => 100 - x.score);
    const taskRisk = taskValues.length ? taskValues.reduce((a, b) => a + b, 0) / taskValues.length : 0;
    const risk = Math.round(selfScore * 0.52 + taskRisk * 0.48);
    return { answerCount: answerValues.length, taskCount: taskValues.length, selfScore, taskRisk, risk };
  }

  function scoreRow(label, value) {
    const width = clamp(Math.round(value), 4, 100);
    return `<div class="score-row"><b>${label} <em>${Math.round(value)}%</em></b><i><span style="width:${width}%"></span></i></div>`;
  }

  function escapeHtml(value) {
    return String(value).replace(/[&<>"']/g, (char) => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" })[char]);
  }

  function escapeAttr(value) {
    return escapeHtml(value).replace(/`/g, "&#96;");
  }

  function clamp(value, min, max) {
    return Math.max(min, Math.min(max, value));
  }

  renderTask();
  updateResult();
}
