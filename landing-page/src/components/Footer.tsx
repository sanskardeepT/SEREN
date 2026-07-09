"use client";

import { Mail, Phone } from "lucide-react";

export default function Footer() {
  return (
    <footer className="relative gradient-cosmic py-16 px-6 border-t border-white/5">
      <div className="max-w-7xl mx-auto">
        <div className="flex flex-col md:flex-row items-center justify-between gap-8">
          {/* Logo */}
          <div className="flex flex-col items-center md:items-start">
            <span
              className="text-xl font-bold tracking-[0.2em] text-white"
              style={{ fontFamily: "var(--font-heading)" }}
            >
              SEREN
            </span>
            <span className="tagline text-white/35" style={{ fontSize: '0.55rem', letterSpacing: '0.2em' }}>
              Every Child is a Star
            </span>
          </div>

          {/* Contact */}
          <div className="flex flex-col sm:flex-row items-center gap-6 text-sm text-white/40">
            <a
              href="mailto:sanskardeepbtalikote19@gmail.com"
              className="flex items-center gap-2 hover:text-white/70 transition-colors duration-300"
            >
              <Mail size={14} />
              <span>sanskardeepbtalikote19@gmail.com</span>
            </a>
            <a
              href="tel:+919403910943"
              className="flex items-center gap-2 hover:text-white/70 transition-colors duration-300"
            >
              <Phone size={14} />
              <span>9403910943</span>
            </a>
          </div>
        </div>

        {/* Divider */}
        <div className="w-full h-px bg-white/5 my-10" />

        {/* Bottom */}
        <div className="flex flex-col md:flex-row items-center justify-between gap-4">
          <p className="text-xs text-white/25">
            © {new Date().getFullYear()} SEREN. All rights reserved.
          </p>
          <p className="text-xs text-white/20 italic">
            Every child deserves to be understood before being judged.
          </p>
        </div>
      </div>
    </footer>
  );
}
