"use client";

import { useRef } from "react";
import { motion, useInView } from "framer-motion";
import SectionHeading from "@/components/SectionHeading";
import { audiences } from "@/lib/constants";
import {
  School,
  Heart,
  Sparkles,
  Microscope,
  BookOpen,
  Stethoscope,
} from "lucide-react";

const iconMap = {
  School,
  Heart,
  Sparkles,
  Microscope,
  BookOpen,
  Stethoscope,
};

export default function AudienceSection() {
  const ref = useRef(null);
  const isInView = useInView(ref, { once: true, margin: "-80px" });

  return (
    <section id="audience" className="relative section-padding overflow-hidden" aria-label="Who SEREN is for">
      {/* Ambient */}
      <div className="absolute inset-0"
        style={{
          background: "radial-gradient(ellipse 50% 30% at 70% 20%, rgba(212, 168, 83, 0.03) 0%, transparent 60%)",
        }}
      />

      <div className="section-container relative z-10" ref={ref}>
        <SectionHeading
          eyebrow="Built For Everyone"
          title="One platform. Every stakeholder."
          subtitle="SEREN was designed so that everyone involved in a child's growth feels heard, supported, and empowered."
        />

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 mt-16">
          {audiences.map((audience, i) => {
            const Icon = iconMap[audience.icon];
            return (
              <motion.div
                key={audience.title}
                initial={{ opacity: 0, y: 30 }}
                animate={isInView ? { opacity: 1, y: 0 } : {}}
                transition={{ duration: 0.6, delay: 0.1 * i, ease: [0.22, 1, 0.36, 1] }}
                className="glass-light rounded-2xl p-8 card-hover group relative overflow-hidden"
              >
                {/* Hover gradient */}
                <div
                  className="absolute inset-0 opacity-0 group-hover:opacity-100 transition-opacity duration-500 rounded-2xl"
                  style={{
                    background: "linear-gradient(135deg, rgba(92, 224, 216, 0.03), rgba(212, 168, 83, 0.03))",
                  }}
                />

                <div className="relative z-10">
                  <div
                    className="w-11 h-11 rounded-xl flex items-center justify-center mb-5 transition-transform duration-500 group-hover:scale-110"
                    style={{
                      background: "linear-gradient(135deg, rgba(212, 168, 83, 0.1), rgba(92, 224, 216, 0.08))",
                      border: "1px solid rgba(212, 168, 83, 0.12)",
                    }}
                  >
                    <Icon size={20} style={{ color: "var(--color-gold)" }} />
                  </div>

                  <h3
                    className="text-lg font-semibold text-text-primary mb-3"
                    style={{ fontFamily: "var(--font-heading)" }}
                  >
                    {audience.title}
                  </h3>

                  <p className="text-text-muted text-sm leading-relaxed">
                    {audience.description}
                  </p>
                </div>
              </motion.div>
            );
          })}
        </div>
      </div>
    </section>
  );
}
