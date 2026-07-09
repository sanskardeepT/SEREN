"use client";

import { useRef } from "react";
import { motion, useInView } from "framer-motion";
import SectionHeading from "@/components/SectionHeading";
import { businessPillars } from "@/lib/constants";
import { Landmark, Home, Rocket } from "lucide-react";

const icons = [Landmark, Home, Rocket];

export default function BusinessModelSection() {
  const ref = useRef(null);
  const isInView = useInView(ref, { once: true, margin: "-80px" });

  return (
    <section className="relative section-padding overflow-hidden" aria-label="Our vision">
      <div className="section-container relative z-10" ref={ref}>
        <SectionHeading
          eyebrow="Our Vision"
          title="Building an ecosystem of understanding"
          subtitle="SEREN's vision extends beyond a single product — it's an evolving ecosystem designed to support every child, everywhere."
        />

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 lg:gap-8 mt-16">
          {businessPillars.map((pillar, i) => {
            const Icon = icons[i];
            return (
              <motion.div
                key={pillar.title}
                initial={{ opacity: 0, y: 30 }}
                animate={isInView ? { opacity: 1, y: 0 } : {}}
                transition={{ duration: 0.6, delay: 0.15 * i, ease: [0.22, 1, 0.36, 1] }}
                className="glass-light rounded-2xl p-8 card-hover group relative overflow-hidden"
              >
                {/* Accent top bar */}
                <div
                  className="absolute top-0 left-0 right-0 h-0.5"
                  style={{
                    background: i === 2
                      ? "linear-gradient(90deg, var(--color-cyan), var(--color-gold))"
                      : "linear-gradient(90deg, var(--color-gold), transparent)",
                    opacity: 0.5,
                  }}
                />

                <div className="relative z-10">
                  <div className="flex items-center gap-3 mb-6">
                    <div
                      className="w-10 h-10 rounded-lg flex items-center justify-center"
                      style={{
                        background: i === 2
                          ? "linear-gradient(135deg, rgba(92, 224, 216, 0.12), rgba(212, 168, 83, 0.08))"
                          : "rgba(212, 168, 83, 0.08)",
                        border: `1px solid ${i === 2 ? "rgba(92, 224, 216, 0.15)" : "rgba(212, 168, 83, 0.1)"}`,
                      }}
                    >
                      <Icon size={18} style={{ color: i === 2 ? "var(--color-cyan)" : "var(--color-gold)" }} />
                    </div>
                    <div>
                      <h3 className="text-lg font-semibold text-text-primary" style={{ fontFamily: "var(--font-heading)" }}>
                        {pillar.title}
                      </h3>
                      <span className="text-xs text-text-muted/60 tracking-wider uppercase">
                        {pillar.subtitle}
                      </span>
                    </div>
                  </div>

                  <ul className="space-y-3">
                    {pillar.points.map((point) => (
                      <li key={point} className="flex items-start gap-3 text-sm text-text-muted">
                        <span className="mt-1.5 w-1 h-1 rounded-full flex-shrink-0"
                          style={{ background: i === 2 ? "var(--color-cyan)" : "var(--color-gold)" }}
                        />
                        {point}
                      </li>
                    ))}
                  </ul>
                </div>
              </motion.div>
            );
          })}
        </div>
      </div>
    </section>
  );
}
