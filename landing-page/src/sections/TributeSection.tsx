"use client";

import { useRef } from "react";
import { motion, useInView } from "framer-motion";

export default function TributeSection() {
  const ref = useRef(null);
  const isInView = useInView(ref, { once: true, margin: "-100px" });

  return (
    <section
      id="tribute"
      className="relative section-padding overflow-hidden text-center"
      aria-label="Tribute to every child"
      style={{ background: "var(--color-midnight)" }}
    >
      <div className="section-container relative z-10" ref={ref}>
        {/* Star Icon */}
        <motion.div
          initial={{ opacity: 0, scale: 0.8 }}
          animate={isInView ? { opacity: 1, scale: 1 } : {}}
          transition={{ duration: 0.8, ease: "easeOut" }}
          className="mb-10 flex justify-center"
        >
          <svg
            className="w-12 h-12 text-gold animate-float-slow"
            viewBox="0 0 24 24"
            fill="none"
            style={{ filter: "drop-shadow(0 0 16px rgba(233,168,50,0.4))" }}
          >
            <path
              d="M12 2l2.8 6.2L22 9.3l-5.5 5 1.8 7.2L12 18l-6.3 3.5 1.8-7.2L2 9.3l7.2-1.1L12 2z"
              fill="currentColor"
            />
          </svg>
        </motion.div>

        {/* Quote */}
        <motion.blockquote
          initial={{ opacity: 0, y: 30 }}
          animate={isInView ? { opacity: 1, y: 0 } : {}}
          transition={{ duration: 0.8, delay: 0.2 }}
          className="text-2xl sm:text-3xl md:text-4xl lg:text-5xl font-light italic text-white max-w-4xl mx-auto leading-tight mb-4"
          style={{ fontFamily: "var(--font-heading)" }}
        >
          &ldquo;Every child is a star. Every adult deserves a second chance.&rdquo;
        </motion.blockquote>

        {/* Inspired By */}
        <motion.p
          initial={{ opacity: 0 }}
          animate={isInView ? { opacity: 1 } : {}}
          transition={{ duration: 0.8, delay: 0.5 }}
          className="text-gold text-sm tracking-wider uppercase mb-8"
        >
          Inspired by Taare Zameen Par (2007)
        </motion.p>

        {/* Body Text */}
        <motion.p
          initial={{ opacity: 0, y: 20 }}
          animate={isInView ? { opacity: 1, y: 0 } : {}}
          transition={{ duration: 0.8, delay: 0.7 }}
          className="text-white/60 text-base sm:text-lg max-w-2xl mx-auto leading-relaxed italic font-light"
          style={{ fontFamily: "var(--font-body)" }}
        >
          Ishaan was not alone then, and he is not alone now. SEREN exists because millions of
          Ishaans never got a teacher who saw them. We&apos;re building the tool that teacher needed
          — for every child who was called lazy, slow, or difficult, when they were simply
          unidentified.
        </motion.p>
      </div>
    </section>
  );
}
