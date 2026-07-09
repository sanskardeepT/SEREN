"use client";

import { useRef } from "react";
import { motion, useInView } from "framer-motion";
import SectionHeading from "@/components/SectionHeading";
import { BookOpen, Heart, Shield, Users } from "lucide-react";

const pillars = [
  {
    icon: BookOpen,
    title: "Research-First Philosophy",
    description:
      "Every decision is informed by extensive literature review across education, psychology, and developmental science.",
  },
  {
    icon: Shield,
    title: "Medical Advisory Review",
    description:
      "Our approach has been reviewed and shaped by medical professionals who bring clinical perspective to every aspect.",
  },
  {
    icon: Heart,
    title: "Evidence-Informed Thinking",
    description:
      "We don't guess. Every element of the platform is grounded in published research and validated methodologies.",
  },
  {
    icon: Users,
    title: "Multidisciplinary Consultation",
    description:
      "Educators, psychologists, therapists, and researchers have contributed their expertise to shape the platform.",
  },
];

const accuracyMetrics = [
  {
    number: "95–99%",
    label: "Learning Disorders",
    sub: "Dyslexia, Dysgraphia",
  },
  {
    number: "90–95%",
    label: "ADHD",
    sub: "Detection accuracy",
  },
  {
    number: "85–93%",
    label: "Emotional Conditions",
    sub: "Anxiety & screening",
  },
];

const techBadges = [
  "TensorFlow Lite",
  "MediaPipe",
  "Whisper AI",
  "OpenCV",
  "On-Device ML",
  "12 Detection Modalities",
];

export default function ResearchSection() {
  const ref = useRef(null);
  const isInView = useInView(ref, { once: true, margin: "-100px" });

  return (
    <section id="research" className="relative gradient-cosmic overflow-hidden" aria-label="Research">
      {/* Ambient glow */}
      <div className="absolute top-0 left-1/2 -translate-x-1/2 w-[600px] h-[300px]"
        style={{
          background: "radial-gradient(ellipse, rgba(92, 224, 216, 0.06) 0%, transparent 70%)",
          filter: "blur(40px)",
        }}
      />

      <div className="section-padding">
        <div className="section-container relative z-10" ref={ref}>
          <SectionHeading
            eyebrow="Science & Credibility"
            title="Not built on hunches. Built on evidence."
            subtitle="60+ peer-reviewed studies. 12 detection modalities. Medical advisory from MBBS practitioners across India."
            light
          />

          {/* Pillars of Foundation */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6 lg:gap-8 mt-16">
            {pillars.map((pillar, i) => {
              const Icon = pillar.icon;
              return (
                <motion.div
                  key={i}
                  initial={{ opacity: 0, y: 30 }}
                  animate={isInView ? { opacity: 1, y: 0 } : {}}
                  transition={{ duration: 0.7, delay: 0.12 * i, ease: [0.22, 1, 0.36, 1] }}
                  className="glass-dark rounded-2xl p-8 md:p-10 card-hover group"
                >
                  <div
                    className="w-12 h-12 rounded-xl flex items-center justify-center mb-6 transition-all duration-500 group-hover:scale-110"
                    style={{
                      background: "linear-gradient(135deg, rgba(92, 224, 216, 0.1), rgba(212, 168, 83, 0.08))",
                      border: "1px solid rgba(92, 224, 216, 0.15)",
                    }}
                  >
                    <Icon size={22} className="text-cyan" />
                  </div>
                  <h3
                    className="text-lg font-semibold text-white mb-3"
                    style={{ fontFamily: "var(--font-heading)" }}
                  >
                    {pillar.title}
                  </h3>
                  <p className="text-white/45 text-sm leading-relaxed">
                    {pillar.description}
                  </p>
                </motion.div>
              );
            })}
          </div>

          {/* Accuracy Metrics Title */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={isInView ? { opacity: 1, y: 0 } : {}}
            transition={{ duration: 0.6, delay: 0.5 }}
            className="text-center mt-24 mb-12"
          >
            <h3 className="text-2xl font-semibold text-white font-serif" style={{ fontFamily: "var(--font-heading)" }}>
              Screening Accuracy
            </h3>
          </motion.div>

          {/* Accuracy Cards Grid */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            {accuracyMetrics.map((metric, i) => (
              <motion.div
                key={i}
                initial={{ opacity: 0, y: 20 }}
                animate={isInView ? { opacity: 1, y: 0 } : {}}
                transition={{ duration: 0.6, delay: 0.6 + i * 0.1 }}
                className="glass-dark rounded-xl p-8 text-center"
              >
                <div className="text-3xl md:text-4xl font-bold text-gold mb-2 font-serif">
                  {metric.number}
                </div>
                <div className="text-xs font-semibold uppercase tracking-wider text-white mb-1">
                  {metric.label}
                </div>
                <div className="text-xs text-white/40">
                  {metric.sub}
                </div>
              </motion.div>
            ))}
          </div>

          <motion.p
            initial={{ opacity: 0 }}
            animate={isInView ? { opacity: 1 } : {}}
            transition={{ duration: 0.6, delay: 0.9 }}
            className="text-center text-xs text-white/35 mt-6 italic"
          >
            SEREN is a screening tool, not a diagnostic tool. Positive results recommend professional follow-up.
          </motion.p>

          {/* Tech Badges */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={isInView ? { opacity: 1, y: 0 } : {}}
            transition={{ duration: 0.6, delay: 1 }}
            className="flex flex-wrap justify-center gap-3 mt-16"
          >
            {techBadges.map((badge, i) => (
              <span
                key={i}
                className="text-xs text-white/60 border border-white/10 px-4 py-2 rounded-lg bg-white/5"
              >
                {badge}
              </span>
            ))}
          </motion.div>

          {/* Disclosure note */}
          <motion.p
            initial={{ opacity: 0 }}
            animate={isInView ? { opacity: 1 } : {}}
            transition={{ duration: 0.8, delay: 1.1 }}
            className="mt-16 text-center text-white/20 text-xs max-w-lg mx-auto leading-relaxed"
          >
            {"The platform has been shaped using extensive literature review, educational insights, and multidisciplinary consultation. Proprietary methods remain confidential."}
          </motion.p>
        </div>
      </div>
    </section>
  );
}
