"use client";

import { motion } from "framer-motion";
import StarField from "@/components/StarField";

interface HeroSectionProps {
  onOpenWaitlist: () => void;
}

export default function HeroSection({ onOpenWaitlist }: HeroSectionProps) {
  return (
    <section
      id="hero"
      className="relative min-h-screen flex items-center justify-center overflow-hidden"
      aria-label="Hero"
    >
      {/* Background */}
      <div className="absolute inset-0 gradient-hero" />
      <StarField />

      {/* Ambient orbs */}
      <div className="absolute top-1/4 left-1/4 w-[500px] h-[500px] rounded-full animate-float-slow opacity-30"
        style={{
          background: "radial-gradient(circle, rgba(92, 224, 216, 0.06) 0%, transparent 70%)",
          filter: "blur(60px)",
        }}
      />
      <div className="absolute bottom-1/4 right-1/4 w-[400px] h-[400px] rounded-full animate-float opacity-25"
        style={{
          background: "radial-gradient(circle, rgba(212, 168, 83, 0.05) 0%, transparent 70%)",
          filter: "blur(50px)",
          animationDelay: "3s",
        }}
      />

      {/* Decorative line art — child with star */}
      <div className="absolute inset-0 z-[1] pointer-events-none overflow-hidden">
        {/* Left shooting star */}
        <svg className="absolute top-[15%] left-[8%] w-40 h-40 opacity-[0.07]" viewBox="0 0 160 160" fill="none">
          <path d="M80 10L88 50L120 30L95 60L130 70L95 80L120 110L88 90L80 130L72 90L40 110L65 80L30 70L65 60L40 30L72 50Z" 
            stroke="white" strokeWidth="0.8" className="animate-float-slow" style={{ animationDelay: "1s" }}/>
        </svg>
        {/* Right decorative circle constellation */}
        <svg className="absolute bottom-[20%] right-[10%] w-48 h-48 opacity-[0.05]" viewBox="0 0 200 200" fill="none">
          <circle cx="100" cy="100" r="60" stroke="white" strokeWidth="0.5" strokeDasharray="4 8"/>
          <circle cx="100" cy="100" r="80" stroke="white" strokeWidth="0.3" strokeDasharray="2 12"/>
          <circle cx="100" cy="40" r="3" fill="rgba(92, 224, 216, 0.3)"/>
          <circle cx="160" cy="100" r="2" fill="rgba(212, 168, 83, 0.3)"/>
          <circle cx="100" cy="160" r="2.5" fill="rgba(255, 255, 255, 0.2)"/>
          <circle cx="40" cy="100" r="2" fill="rgba(92, 224, 216, 0.2)"/>
        </svg>
        {/* Small child silhouette reaching for star */}
        <svg className="absolute bottom-[25%] left-[15%] w-32 h-32 opacity-[0.04] hidden lg:block" viewBox="0 0 120 120" fill="none">
          <path d="M60 95 L60 65 L45 80 M60 65 L75 80 M60 65 L60 45 M50 50 L60 45 L70 50 M55 40 Q60 30 65 40 Q60 35 55 40Z M58 25 L60 18 L62 25" 
            stroke="white" strokeWidth="1" strokeLinecap="round" strokeLinejoin="round"/>
          <circle cx="60" cy="18" r="3" fill="rgba(212, 168, 83, 0.3)"/>
        </svg>
      </div>

      {/* Content */}
      <div className="relative z-10 text-center px-6 max-w-4xl mx-auto">
        {/* Wordmark */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 1, delay: 0.3 }}
          className="mb-12"
        >
          <h1 className="sr-only">SEREN — Every Child is a Star</h1>
          <span
            className="text-4xl md:text-5xl font-bold tracking-[0.35em] text-white/90"
            style={{ fontFamily: "var(--font-heading)" }}
            aria-hidden="true"
          >
            SEREN
          </span>
          <p className="tagline text-white/35 mt-2" style={{ letterSpacing: '0.25em' }}>
            Every Child is a Star
          </p>
        </motion.div>

        {/* Headline */}
        <motion.h2
          initial={{ opacity: 0, y: 30 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 1, delay: 0.7 }}
          className="text-3xl sm:text-4xl md:text-5xl lg:text-6xl font-normal text-white leading-[1.15] mb-8 font-serif italic"
          style={{ fontFamily: "var(--font-heading)" }}
        >
          What if the child everyone called <span className="text-gradient-gold not-italic font-semibold">lazy</span>…
          <br className="hidden sm:block" />
          was a <span className="text-gradient-cyan not-italic font-semibold">star</span>?
        </motion.h2>

        {/* Subheading */}
        <motion.p
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.8, delay: 1.1 }}
          className="text-base sm:text-lg md:text-xl text-white/50 max-w-2xl mx-auto leading-relaxed mb-12"
          style={{ fontFamily: "var(--font-body)" }}
        >
          SEREN screens 60+ learning, emotional, and developmental conditions — using only a smartphone, in 20 minutes, fully offline. Built for India&apos;s 3.75 crore undetected children.
        </motion.p>

        {/* CTAs */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.8, delay: 1.4 }}
          className="flex flex-col sm:flex-row items-center justify-center gap-4"
        >
          <button onClick={onOpenWaitlist} className="btn-primary">
            <span>For Schools →</span>
          </button>
          <button onClick={onOpenWaitlist} className="btn-secondary">
            For Parents
          </button>
        </motion.div>
      </div>

      {/* Scroll indicator */}
      <motion.div
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ delay: 2.2, duration: 1 }}
        className="absolute bottom-10 left-1/2 -translate-x-1/2 flex flex-col items-center gap-3"
      >
        <span className="text-[0.65rem] text-white/25 tracking-[0.2em] uppercase">Scroll</span>
        <motion.div
          animate={{ y: [0, 8, 0] }}
          transition={{ duration: 2, repeat: Infinity, ease: "easeInOut" }}
          className="w-px h-8 bg-gradient-to-b from-white/20 to-transparent"
        />
      </motion.div>
    </section>
  );
}
