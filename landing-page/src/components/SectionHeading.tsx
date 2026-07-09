"use client";

import { motion } from "framer-motion";
import { useInView } from "framer-motion";
import { useRef } from "react";

interface SectionHeadingProps {
  eyebrow?: string;
  title: string;
  subtitle?: string;
  light?: boolean;
  centered?: boolean;
}

export default function SectionHeading({
  eyebrow,
  title,
  subtitle,
  light = false,
  centered = true,
}: SectionHeadingProps) {
  const ref = useRef(null);
  const isInView = useInView(ref, { once: true, margin: "-100px" });

  return (
    <div ref={ref} className={`mb-16 md:mb-20 ${centered ? "text-center" : ""}`}>
      {eyebrow && (
        <motion.p
          initial={{ opacity: 0, y: 20 }}
          animate={isInView ? { opacity: 1, y: 0 } : {}}
          transition={{ duration: 0.6 }}
          className={`text-xs tracking-[0.3em] uppercase font-medium mb-4 ${
            light ? "text-white/40" : "text-text-secondary/60"
          }`}
          style={{ fontFamily: "var(--font-body)" }}
        >
          {eyebrow}
        </motion.p>
      )}
      <motion.h2
        initial={{ opacity: 0, y: 30 }}
        animate={isInView ? { opacity: 1, y: 0 } : {}}
        transition={{ duration: 0.7, delay: 0.1 }}
        className={`text-3xl md:text-4xl lg:text-5xl font-semibold leading-tight ${
          light ? "text-white" : "text-text-primary"
        }`}
        style={{ fontFamily: "var(--font-heading)" }}
      >
        {title}
      </motion.h2>
      {subtitle && (
        <motion.p
          initial={{ opacity: 0, y: 20 }}
          animate={isInView ? { opacity: 1, y: 0 } : {}}
          transition={{ duration: 0.6, delay: 0.25 }}
          className={`mt-5 text-base md:text-lg max-w-2xl leading-relaxed ${
            centered ? "mx-auto" : ""
          } ${light ? "text-white/55" : "text-text-muted"}`}
          style={{ fontFamily: "var(--font-body)" }}
        >
          {subtitle}
        </motion.p>
      )}
    </div>
  );
}
