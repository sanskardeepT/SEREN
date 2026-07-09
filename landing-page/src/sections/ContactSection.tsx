"use client";

import { useRef } from "react";
import { motion, useInView } from "framer-motion";
import { Mail, Phone } from "lucide-react";

export default function ContactSection() {
  const ref = useRef(null);
  const isInView = useInView(ref, { once: true, margin: "-60px" });

  return (
    <section id="contact" className="relative section-padding" aria-label="Contact us">
      <div className="section-container relative z-10" ref={ref}>
        <div className="max-w-xl mx-auto text-center">
          {/* Decorative line */}
          <motion.div
            initial={{ scaleX: 0 }}
            animate={isInView ? { scaleX: 1 } : {}}
            transition={{ duration: 1, ease: [0.22, 1, 0.36, 1] }}
            className="w-16 h-px mx-auto mb-12"
            style={{
              background: "linear-gradient(90deg, transparent, var(--color-gold), transparent)",
            }}
          />

          <motion.p
            initial={{ opacity: 0, y: 20 }}
            animate={isInView ? { opacity: 1, y: 0 } : {}}
            transition={{ duration: 0.6 }}
            className="text-xs tracking-[0.3em] uppercase text-text-muted/60 mb-4"
          >
            Get in Touch
          </motion.p>

          <motion.h2
            initial={{ opacity: 0, y: 30 }}
            animate={isInView ? { opacity: 1, y: 0 } : {}}
            transition={{ duration: 0.7, delay: 0.1 }}
            className="text-3xl md:text-4xl font-semibold text-text-primary mb-6"
            style={{ fontFamily: "var(--font-heading)" }}
          >
            Let&apos;s talk about children
          </motion.h2>

          <motion.p
            initial={{ opacity: 0 }}
            animate={isInView ? { opacity: 1 } : {}}
            transition={{ duration: 0.6, delay: 0.2 }}
            className="text-text-muted mb-12"
          >
            Whether you&apos;re a school, a parent, a researcher, or someone who simply cares
            — we&apos;d love to hear from you.
          </motion.p>

          {/* Contact cards */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={isInView ? { opacity: 1, y: 0 } : {}}
            transition={{ duration: 0.6, delay: 0.3 }}
            className="flex flex-col sm:flex-row items-stretch justify-center gap-4"
          >
            <a
              href="mailto:sanskardeepbtalikote19@gmail.com"
              className="glass-light rounded-2xl px-8 py-6 card-hover flex items-center gap-4 group"
            >
              <div
                className="w-10 h-10 rounded-xl flex items-center justify-center flex-shrink-0 transition-transform duration-500 group-hover:scale-110"
                style={{
                  background: "rgba(212, 168, 83, 0.08)",
                  border: "1px solid rgba(212, 168, 83, 0.1)",
                }}
              >
                <Mail size={18} style={{ color: "var(--color-gold)" }} />
              </div>
              <div className="text-left">
                <p className="text-xs text-text-muted/60 mb-0.5">Email</p>
                <p className="text-sm text-text-primary font-medium">
                  sanskardeepbtalikote19@gmail.com
                </p>
              </div>
            </a>

            <a
              href="tel:+919403910943"
              className="glass-light rounded-2xl px-8 py-6 card-hover flex items-center gap-4 group"
            >
              <div
                className="w-10 h-10 rounded-xl flex items-center justify-center flex-shrink-0 transition-transform duration-500 group-hover:scale-110"
                style={{
                  background: "rgba(92, 224, 216, 0.08)",
                  border: "1px solid rgba(92, 224, 216, 0.1)",
                }}
              >
                <Phone size={18} style={{ color: "var(--color-cyan)" }} />
              </div>
              <div className="text-left">
                <p className="text-xs text-text-muted/60 mb-0.5">Phone</p>
                <p className="text-sm text-text-primary font-medium">9403910943</p>
              </div>
            </a>
          </motion.div>
        </div>
      </div>
    </section>
  );
}
