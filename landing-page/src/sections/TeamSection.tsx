"use client";

import { useRef } from "react";
import { motion, useInView } from "framer-motion";
import SectionHeading from "@/components/SectionHeading";
import { teamData } from "@/lib/constants";

type TeamGroup = {
  title: string;
  members: string[];
};

const teamGroups: TeamGroup[] = [
  teamData.founder,
  teamData.coFounders,
  teamData.research,
  teamData.medicalAdvisory,
  teamData.creatorTeam,
];

export default function TeamSection() {
  const ref = useRef(null);
  const isInView = useInView(ref, { once: true, margin: "-60px" });

  return (
    <section id="team" className="relative section-padding overflow-hidden" aria-label="Our team">
      {/* Ambient */}
      <div className="absolute inset-0"
        style={{
          background: "radial-gradient(ellipse 40% 30% at 20% 80%, rgba(92, 224, 216, 0.03) 0%, transparent 60%)",
        }}
      />

      <div className="section-container relative z-10" ref={ref}>
        <SectionHeading
          eyebrow="The People"
          title="Behind SEREN"
          subtitle="A multidisciplinary team united by a single belief: every child deserves to be understood."
        />

        <div className="space-y-16 mt-16">
          {teamGroups.map((group, gi) => (
            <motion.div
              key={group.title}
              initial={{ opacity: 0, y: 20 }}
              animate={isInView ? { opacity: 1, y: 0 } : {}}
              transition={{ duration: 0.6, delay: 0.1 * gi }}
            >
              {/* Group title */}
              <div className="flex items-center gap-4 mb-6">
                <h3
                  className="text-sm font-semibold tracking-[0.15em] uppercase"
                  style={{
                    fontFamily: "var(--font-heading)",
                    color: gi === 0 ? "var(--color-gold)" : "var(--color-text-secondary)",
                  }}
                >
                  {group.title}
                </h3>
                <div className="flex-1 h-px bg-text-primary/5" />
              </div>

              {/* Members grid */}
              <div className={`grid gap-4 ${
                group.members.length === 1
                  ? "grid-cols-1 max-w-xs"
                  : group.members.length <= 3
                    ? "grid-cols-1 sm:grid-cols-3"
                    : "grid-cols-2 sm:grid-cols-3 lg:grid-cols-5"
              }`}>
                {group.members.map((member, mi) => (
                  <motion.div
                    key={member}
                    initial={{ opacity: 0, scale: 0.95 }}
                    animate={isInView ? { opacity: 1, scale: 1 } : {}}
                    transition={{ duration: 0.4, delay: 0.1 * gi + 0.05 * mi }}
                    className={`glass-light rounded-xl px-5 py-4 text-center card-hover ${
                      gi === 0 ? "border border-gold/10" : ""
                    }`}
                  >
                    {/* Initials avatar */}
                    <div
                      className="w-10 h-10 rounded-full mx-auto mb-3 flex items-center justify-center text-xs font-semibold"
                      style={{
                        background: gi === 0
                          ? "linear-gradient(135deg, rgba(212, 168, 83, 0.15), rgba(212, 168, 83, 0.05))"
                          : gi === 3
                            ? "linear-gradient(135deg, rgba(92, 224, 216, 0.12), rgba(92, 224, 216, 0.04))"
                            : "rgba(10, 22, 40, 0.05)",
                        color: gi === 0
                          ? "var(--color-gold)"
                          : gi === 3
                            ? "var(--color-cyan)"
                            : "var(--color-text-secondary)",
                        fontFamily: "var(--font-heading)",
                      }}
                    >
                      {member.startsWith("@")
                        ? "@"
                        : member.split(" ").map(n => n[0]).join("").slice(0, 2).toUpperCase()
                      }
                    </div>

                    <p
                      className="text-sm font-medium text-text-primary"
                      style={{ fontFamily: "var(--font-heading)" }}
                    >
                      {member}
                    </p>
                  </motion.div>
                ))}
              </div>

              {/* Medical advisory note */}
              {group.title === "Medical Advisory" && (
                <p className="mt-4 text-xs text-text-muted/40 italic">
                  Advisory role — providing clinical perspective and review. Does not constitute endorsement.
                </p>
              )}
            </motion.div>
          ))}
        </div>
      </div>
    </section>
  );
}
