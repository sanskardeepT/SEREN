"use client";

import { useRef } from "react";
import { motion, useInView } from "framer-motion";

export default function NEPSection() {
  const ref = useRef(null);
  const isInView = useInView(ref, { once: true, margin: "-100px" });

  return (
    <section className="relative section-padding overflow-hidden" aria-label="NEP 2020 alignment">
      <div className="section-container relative z-10" ref={ref}>
        <div className="max-w-3xl mx-auto text-center">
          {/* Decorative top element */}
          <motion.div
            initial={{ scaleX: 0 }}
            animate={isInView ? { scaleX: 1 } : {}}
            transition={{ duration: 1, ease: [0.22, 1, 0.36, 1] }}
            className="w-20 h-px mx-auto mb-12"
            style={{
              background: "linear-gradient(90deg, transparent, var(--color-gold), transparent)",
            }}
          />

          <motion.p
            initial={{ opacity: 0, y: 20 }}
            animate={isInView ? { opacity: 1, y: 0 } : {}}
            transition={{ duration: 0.6 }}
            className="text-xs tracking-[0.3em] uppercase text-text-muted/60 mb-8"
            style={{ fontFamily: "var(--font-body)" }}
          >
            Aligned with India&apos;s Vision
          </motion.p>

          <motion.h2
            initial={{ opacity: 0, y: 30 }}
            animate={isInView ? { opacity: 1, y: 0 } : {}}
            transition={{ duration: 0.7, delay: 0.1 }}
            className="text-2xl md:text-3xl lg:text-4xl font-semibold text-text-primary leading-tight mb-8"
            style={{ fontFamily: "var(--font-heading)" }}
          >
            National Education Policy{" "}
            <span className="text-gradient-gold">2020</span>
          </motion.h2>

          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={isInView ? { opacity: 1, y: 0 } : {}}
            transition={{ duration: 0.7, delay: 0.25 }}
            className="glass-light rounded-2xl p-8 md:p-12 text-left"
          >
            <p className="text-text-secondary leading-relaxed mb-6">
              India&apos;s National Education Policy encourages early identification and support
              for children with learning differences. It envisions a system where every child
              receives personalised attention and timely intervention.
            </p>
            <p className="text-text-secondary leading-relaxed">
              SEREN aligns with this vision by helping schools move toward earlier support
              and informed intervention — ensuring no child is left behind because their
              differences went unnoticed.
            </p>
          </motion.div>

          <motion.p
            initial={{ opacity: 0 }}
            animate={isInView ? { opacity: 1 } : {}}
            transition={{ duration: 0.6, delay: 0.5 }}
            className="mt-6 text-xs text-text-muted/40 italic"
          >
            SEREN is not affiliated with or endorsed by any government body.
            This represents alignment in vision, not official partnership.
          </motion.p>
        </div>
      </div>
    </section>
  );
}
