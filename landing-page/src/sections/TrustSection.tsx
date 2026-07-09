"use client";

import { useRef } from "react";
import { motion, useInView } from "framer-motion";
import SectionHeading from "@/components/SectionHeading";
import { timelineSteps } from "@/lib/constants";

export default function TrustSection() {
  const ref = useRef(null);
  const isInView = useInView(ref, { once: true, margin: "-80px" });

  return (
    <section id="trust" className="relative gradient-cosmic overflow-hidden" aria-label="Our journey">
      {/* Ambient glow */}
      <div className="absolute bottom-0 left-1/2 -translate-x-1/2 w-[500px] h-[300px]"
        style={{
          background: "radial-gradient(ellipse, rgba(212, 168, 83, 0.06) 0%, transparent 70%)",
          filter: "blur(40px)",
        }}
      />

      <div className="section-padding">
        <div className="section-container relative z-10" ref={ref}>
          <SectionHeading
            eyebrow="Our Journey"
            title="Transparent progress"
            subtitle="We believe in openness. Here's where we are on our journey to help every child."
            light
          />

          {/* Timeline */}
          <div className="relative mt-16">
            {/* Desktop horizontal timeline */}
            <div className="hidden md:block">
              {/* Line */}
              <div className="relative h-px w-full mb-16">
                <div className="absolute inset-0 bg-white/10" />
                <motion.div
                  initial={{ scaleX: 0 }}
                  animate={isInView ? { scaleX: 1 } : {}}
                  transition={{ duration: 1.5, delay: 0.3, ease: [0.22, 1, 0.36, 1] }}
                  className="absolute inset-y-0 left-0 origin-left"
                  style={{
                    width: "70%",
                    background: "linear-gradient(90deg, var(--color-gold), var(--color-cyan))",
                  }}
                />

                {/* Nodes */}
                {timelineSteps.map((step, i) => (
                  <motion.div
                    key={step.title}
                    initial={{ opacity: 0, scale: 0 }}
                    animate={isInView ? { opacity: 1, scale: 1 } : {}}
                    transition={{ duration: 0.5, delay: 0.4 + i * 0.15, ease: [0.22, 1, 0.36, 1] }}
                    className="absolute -top-2.5"
                    style={{ left: `${(i / (timelineSteps.length - 1)) * 100}%`, transform: "translateX(-50%)" }}
                  >
                    <div
                      className={`w-5 h-5 rounded-full border-2 ${
                        step.current
                          ? "animate-pulse-glow border-cyan bg-cyan"
                          : step.completed
                            ? "border-gold bg-gold"
                            : "border-white/20 bg-midnight-light"
                      }`}
                    />
                  </motion.div>
                ))}
              </div>

              {/* Labels */}
              <div className="grid grid-cols-5 gap-4">
                {timelineSteps.map((step, i) => (
                  <motion.div
                    key={step.title}
                    initial={{ opacity: 0, y: 20 }}
                    animate={isInView ? { opacity: 1, y: 0 } : {}}
                    transition={{ duration: 0.6, delay: 0.5 + i * 0.12 }}
                    className="text-center"
                  >
                    <h4
                      className={`text-sm font-semibold mb-1 ${
                        step.current ? "text-cyan" : step.completed ? "text-gold" : "text-white/40"
                      }`}
                      style={{ fontFamily: "var(--font-heading)" }}
                    >
                      {step.title}
                    </h4>
                    <p className="text-white/30 text-xs leading-relaxed">
                      {step.description}
                    </p>
                    {step.current && (
                      <span className="inline-block mt-2 text-[0.6rem] tracking-[0.15em] uppercase text-cyan/60 font-medium">
                        We are here
                      </span>
                    )}
                  </motion.div>
                ))}
              </div>
            </div>

            {/* Mobile vertical timeline */}
            <div className="md:hidden space-y-0">
              {timelineSteps.map((step, i) => (
                <motion.div
                  key={step.title}
                  initial={{ opacity: 0, x: -20 }}
                  animate={isInView ? { opacity: 1, x: 0 } : {}}
                  transition={{ duration: 0.5, delay: 0.2 + i * 0.1 }}
                  className="flex gap-5"
                >
                  {/* Vertical line & node */}
                  <div className="flex flex-col items-center">
                    <div
                      className={`w-4 h-4 rounded-full flex-shrink-0 border-2 ${
                        step.current
                          ? "animate-pulse-glow border-cyan bg-cyan"
                          : step.completed
                            ? "border-gold bg-gold"
                            : "border-white/20 bg-midnight-light"
                      }`}
                    />
                    {i < timelineSteps.length - 1 && (
                      <div
                        className="w-px flex-1 min-h-[60px]"
                        style={{
                          background: step.completed
                            ? "linear-gradient(180deg, var(--color-gold), var(--color-gold))"
                            : "rgba(255, 255, 255, 0.08)",
                        }}
                      />
                    )}
                  </div>

                  {/* Content */}
                  <div className="pb-8">
                    <h4
                      className={`text-sm font-semibold mb-1 ${
                        step.current ? "text-cyan" : step.completed ? "text-gold" : "text-white/40"
                      }`}
                      style={{ fontFamily: "var(--font-heading)" }}
                    >
                      {step.title}
                    </h4>
                    <p className="text-white/30 text-xs leading-relaxed">
                      {step.description}
                    </p>
                    {step.current && (
                      <span className="inline-block mt-2 text-[0.6rem] tracking-[0.15em] uppercase text-cyan/60 font-medium">
                        We are here
                      </span>
                    )}
                  </div>
                </motion.div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
