"use client";

import { useRef, useEffect, useState } from "react";
import { motion, useInView } from "framer-motion";
import SectionHeading from "@/components/SectionHeading";

const stats = [
  {
    value: "3.75 Cr",
    label: "children in India with undetected learning conditions",
  },
  {
    value: "Class 6",
    label: "average age of first identification — 3 to 4 years too late",
  },
  {
    value: "₹0",
    label: "cost to screen — because access shouldn't depend on income",
  },
];

export default function SocialImpactSection() {
  const ref = useRef(null);
  const isInView = useInView(ref, { once: true, margin: "-100px" });

  return (
    <section id="problem" className="relative section-padding overflow-hidden" aria-label="Social impact">
      {/* Ambient background */}
      <div className="absolute inset-0"
        style={{
          background: "radial-gradient(ellipse 60% 40% at 50% 50%, rgba(92, 224, 216, 0.03) 0%, transparent 60%)",
        }}
      />

      <div className="section-container relative z-10" ref={ref}>
        <SectionHeading
          eyebrow="The Problem"
          title="Ishaan was real. He's sitting in 25 million classrooms right now."
          subtitle="Taare Zameen Par showed India one boy. There are millions more. India already knows this story. SEREN makes sure it doesn't repeat. — Inspired by Taare Zameen Par (2007, dir. Aamir Khan)"
        />

        <div className="grid grid-cols-1 md:grid-cols-3 gap-8 lg:gap-12 mt-16">
          {stats.map((stat, i) => (
            <motion.div
              key={i}
              initial={{ opacity: 0, y: 30 }}
              animate={isInView ? { opacity: 1, y: 0 } : {}}
              transition={{ duration: 0.7, delay: 0.15 * i, ease: [0.22, 1, 0.36, 1] }}
              className="glass-light rounded-2xl p-8 md:p-10 card-hover text-center"
            >
              <p
                className="text-4xl md:text-5xl font-bold mb-4 text-gradient-gold"
                style={{ fontFamily: "var(--font-heading)" }}
              >
                {stat.value}
              </p>
              <p className="text-text-muted text-sm md:text-base leading-relaxed">
                {stat.label}
              </p>
            </motion.div>
          ))}
        </div>

        {/* Hope message */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={isInView ? { opacity: 1, y: 0 } : {}}
          transition={{ duration: 0.8, delay: 0.8 }}
          className="mt-16 text-center"
        >
          <p className="text-lg md:text-xl text-text-secondary font-medium" style={{ fontFamily: "var(--font-heading)" }}>
            The problem isn&apos;t awareness. It&apos;s infrastructure.
          </p>
          <p className="text-text-muted mt-2">
            Early understanding can change everything.
          </p>
        </motion.div>
      </div>
    </section>
  );
}
