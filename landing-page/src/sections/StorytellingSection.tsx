"use client";

import { useRef } from "react";
import { motion, useInView } from "framer-motion";
import { stories } from "@/lib/constants";

export default function StorytellingSection() {
  return (
    <section id="mission" className="relative" aria-label="Stories of misunderstood children">
      {/* Top transition gradient */}
      <div className="h-32 bg-gradient-to-b from-midnight to-cream" />

      <div className="gradient-section">
        <div className="section-padding">
          <div className="section-container">
            {/* Eyebrow */}
            <motion.p
              initial={{ opacity: 0 }}
              whileInView={{ opacity: 1 }}
              viewport={{ once: true }}
              transition={{ duration: 0.8 }}
              className="text-center text-xs tracking-[0.3em] uppercase text-text-muted/60 mb-24"
              style={{ fontFamily: "var(--font-body)" }}
            >
              Stories that need to be heard
            </motion.p>

            {/* Stories */}
            <div className="space-y-32 md:space-y-40">
              {stories.map((story, i) => (
                <StoryCard key={i} story={story} index={i} />
              ))}
            </div>

            {/* Closing thought */}
            <ClosingThought />
          </div>
        </div>
      </div>
    </section>
  );
}

function StoryCard({ story, index }: { story: { label: string; reveal: string }; index: number }) {
  const ref = useRef(null);
  const isInView = useInView(ref, { once: true, margin: "-15%" });

  return (
    <div ref={ref} className="relative max-w-3xl mx-auto text-center">
      {/* Decorative line */}
      <motion.div
        initial={{ scaleX: 0 }}
        animate={isInView ? { scaleX: 1 } : {}}
        transition={{ duration: 1, delay: 0.2, ease: [0.22, 1, 0.36, 1] }}
        className="w-16 h-px mx-auto mb-12"
        style={{
          background: index % 2 === 0
            ? "linear-gradient(90deg, transparent, var(--color-gold), transparent)"
            : "linear-gradient(90deg, transparent, var(--color-cyan), transparent)",
        }}
      />

      {/* Label */}
      <motion.p
        initial={{ opacity: 0, y: 40 }}
        animate={isInView ? { opacity: 1, y: 0 } : {}}
        transition={{ duration: 0.8, delay: 0.1, ease: [0.22, 1, 0.36, 1] }}
        className="text-2xl sm:text-3xl md:text-4xl lg:text-5xl font-semibold text-text-primary leading-tight"
        style={{ fontFamily: "var(--font-heading)" }}
      >
        {story.label}
      </motion.p>

      {/* Reveal */}
      <motion.p
        initial={{ opacity: 0, y: 30 }}
        animate={isInView ? { opacity: 1, y: 0 } : {}}
        transition={{ duration: 0.8, delay: 0.5, ease: [0.22, 1, 0.36, 1] }}
        className="mt-6 text-xl sm:text-2xl md:text-3xl font-medium leading-tight"
        style={{
          fontFamily: "var(--font-heading)",
          color: index % 2 === 0 ? "var(--color-gold)" : "var(--color-cyan)",
        }}
      >
        {story.reveal}
      </motion.p>
    </div>
  );
}

function ClosingThought() {
  const ref = useRef(null);
  const isInView = useInView(ref, { once: true, margin: "-10%" });

  return (
    <motion.div
      ref={ref}
      initial={{ opacity: 0 }}
      animate={isInView ? { opacity: 1 } : {}}
      transition={{ duration: 1.2 }}
      className="mt-32 md:mt-40 pt-16 border-t border-text-primary/5 text-center"
    >
      <p className="text-base md:text-lg text-text-muted max-w-xl mx-auto leading-relaxed italic">
        &ldquo;SEREN exists because millions of children are misunderstood before they are understood.
        Some are called lazy, slow, silent, distracted. Yet many of them are simply living with
        differences that remain unidentified until it is already too late.&rdquo;
      </p>
    </motion.div>
  );
}
