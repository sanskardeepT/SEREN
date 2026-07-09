"use client";

import { motion, AnimatePresence } from "framer-motion";
import { X } from "lucide-react";

interface WaitlistModalProps {
  isOpen: boolean;
  onClose: () => void;
}

export default function WaitlistModal({ isOpen, onClose }: WaitlistModalProps) {
  return (
    <AnimatePresence>
      {isOpen && (
        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          exit={{ opacity: 0 }}
          transition={{ duration: 0.3 }}
          className="fixed inset-0 z-[100] flex items-center justify-center p-6"
          onClick={onClose}
          role="dialog"
          aria-modal="true"
          aria-label="Waitlist information"
        >
          {/* Backdrop */}
          <div className="absolute inset-0 bg-black/50 backdrop-blur-md" />

          {/* Modal */}
          <motion.div
            initial={{ scale: 0.9, opacity: 0, y: 20 }}
            animate={{ scale: 1, opacity: 1, y: 0 }}
            exit={{ scale: 0.95, opacity: 0, y: 10 }}
            transition={{ duration: 0.4, ease: [0.22, 1, 0.36, 1] }}
            onClick={(e) => e.stopPropagation()}
            className="relative w-full max-w-lg rounded-3xl overflow-hidden"
            style={{
              background: "rgba(10, 22, 40, 0.95)",
              backdropFilter: "blur(40px)",
              border: "1px solid rgba(255, 255, 255, 0.1)",
              boxShadow: "0 32px 80px rgba(0, 0, 0, 0.4), 0 0 120px rgba(92, 224, 216, 0.05)",
            }}
          >
            {/* Ambient glow */}
            <div
              className="absolute -top-20 -right-20 w-60 h-60 rounded-full"
              style={{
                background: "radial-gradient(circle, rgba(212, 168, 83, 0.12) 0%, transparent 70%)",
              }}
            />
            <div
              className="absolute -bottom-16 -left-16 w-48 h-48 rounded-full"
              style={{
                background: "radial-gradient(circle, rgba(92, 224, 216, 0.08) 0%, transparent 70%)",
              }}
            />

            {/* Close button */}
            <button
              onClick={onClose}
              className="absolute top-5 right-5 p-2 text-white/40 hover:text-white/80 transition-colors duration-300 rounded-full hover:bg-white/5"
              aria-label="Close modal"
            >
              <X size={18} />
            </button>

            {/* Content */}
            <div className="relative z-10 p-10 text-center">
              {/* Star icon */}
              <div className="inline-flex items-center justify-center w-16 h-16 rounded-full mb-8"
                style={{
                  background: "linear-gradient(135deg, rgba(212, 168, 83, 0.15), rgba(92, 224, 216, 0.1))",
                  border: "1px solid rgba(212, 168, 83, 0.2)",
                }}
              >
                <svg width="28" height="28" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path d="M12 2L14.09 8.26L21 9.27L16 14.14L17.18 21.02L12 17.77L6.82 21.02L8 14.14L3 9.27L9.91 8.26L12 2Z" 
                    fill="url(#star-gradient)" stroke="rgba(212, 168, 83, 0.4)" strokeWidth="0.5"/>
                  <defs>
                    <linearGradient id="star-gradient" x1="3" y1="2" x2="21" y2="21">
                      <stop offset="0%" stopColor="#d4a853" />
                      <stop offset="100%" stopColor="#5ce0d8" />
                    </linearGradient>
                  </defs>
                </svg>
              </div>

              <h3
                className="text-2xl font-semibold text-white mb-2"
                style={{ fontFamily: "var(--font-heading)" }}
              >
                Thank you for your interest
              </h3>

              <div className="w-12 h-px mx-auto my-6" style={{ background: "linear-gradient(90deg, transparent, var(--color-gold), transparent)" }} />

              <p className="text-white/60 leading-relaxed text-[0.95rem] mb-3">
                SEREN is currently in its final development phase.
              </p>
              <p className="text-white/60 leading-relaxed text-[0.95rem] mb-8">
                We expect to begin early access within the next two weeks.
                Leave your details and we&apos;ll notify you the moment invitations become available.
              </p>

              {/* Form */}
              <div className="space-y-4 mb-6">
                <input
                  type="text"
                  placeholder="Your Name"
                  className="w-full px-5 py-3.5 rounded-xl bg-white/5 border border-white/10 text-white placeholder:text-white/30 focus:outline-none focus:border-gold/40 focus:bg-white/8 transition-all duration-300 text-sm"
                  style={{ fontFamily: "var(--font-body)" }}
                />
                <input
                  type="email"
                  placeholder="Your Email"
                  className="w-full px-5 py-3.5 rounded-xl bg-white/5 border border-white/10 text-white placeholder:text-white/30 focus:outline-none focus:border-gold/40 focus:bg-white/8 transition-all duration-300 text-sm"
                  style={{ fontFamily: "var(--font-body)" }}
                />
              </div>

              <button
                onClick={onClose}
                className="w-full py-3.5 rounded-xl font-medium text-sm transition-all duration-400 hover:translate-y-[-1px]"
                style={{
                  background: "linear-gradient(135deg, var(--color-gold), var(--color-gold-light))",
                  color: "var(--color-midnight)",
                  fontFamily: "var(--font-heading)",
                  boxShadow: "0 4px 24px rgba(212, 168, 83, 0.25)",
                }}
              >
                Notify Me
              </button>

              <p className="text-white/25 text-xs mt-5">
                We respect your privacy. No spam, ever.
              </p>
            </div>
          </motion.div>
        </motion.div>
      )}
    </AnimatePresence>
  );
}
