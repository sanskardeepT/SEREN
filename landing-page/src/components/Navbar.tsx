"use client";

import { useState, useEffect } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { navLinks } from "@/lib/constants";
import { Menu, X } from "lucide-react";

export default function Navbar() {
  const [scrolled, setScrolled] = useState(false);
  const [mobileOpen, setMobileOpen] = useState(false);

  useEffect(() => {
    const handleScroll = () => setScrolled(window.scrollY > 50);
    window.addEventListener("scroll", handleScroll, { passive: true });
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  useEffect(() => {
    if (mobileOpen) {
      document.body.style.overflow = "hidden";
    } else {
      document.body.style.overflow = "";
    }
    return () => { document.body.style.overflow = ""; };
  }, [mobileOpen]);

  return (
    <>
      <motion.nav
        initial={{ y: -100, opacity: 0 }}
        animate={{ y: 0, opacity: 1 }}
        transition={{ duration: 0.8, ease: [0.22, 1, 0.36, 1], delay: 0.5 }}
        className={`fixed top-0 left-0 right-0 z-50 transition-all duration-500 ${
          scrolled
            ? "py-3"
            : "py-5"
        }`}
        role="navigation"
        aria-label="Main navigation"
      >
        <div className="mx-auto max-w-7xl px-6">
          <div
            className={`flex items-center justify-between rounded-2xl px-6 py-3 transition-all duration-500 ${
              scrolled
                ? "glass-dark shadow-lg shadow-black/10"
                : ""
            }`}
          >
            {/* Logo */}
            <a href="#" className="flex flex-col items-start group" aria-label="SEREN - Home">
              <span
                className="text-xl font-bold tracking-[0.2em] text-white"
                style={{ fontFamily: "var(--font-heading)" }}
              >
                SEREN
              </span>
              <span className="tagline text-white/50 group-hover:text-gold transition-colors duration-300" style={{ fontSize: '0.55rem', letterSpacing: '0.2em' }}>
                Every Child is a Star
              </span>
            </a>

            {/* Desktop Links */}
            <div className="hidden md:flex items-center gap-1">
              {navLinks.map((link) => (
                <a
                  key={link.href}
                  href={link.href}
                  className="px-4 py-2 text-sm text-white/60 hover:text-white transition-colors duration-300 rounded-lg hover:bg-white/5"
                  style={{ fontFamily: "var(--font-body)" }}
                >
                  {link.label}
                </a>
              ))}
            </div>

            {/* Mobile Menu Button */}
            <button
              onClick={() => setMobileOpen(!mobileOpen)}
              className="md:hidden p-2 text-white/70 hover:text-white transition-colors"
              aria-label={mobileOpen ? "Close menu" : "Open menu"}
              aria-expanded={mobileOpen}
            >
              {mobileOpen ? <X size={22} /> : <Menu size={22} />}
            </button>
          </div>
        </div>
      </motion.nav>

      {/* Mobile Menu */}
      <AnimatePresence>
        {mobileOpen && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            transition={{ duration: 0.3 }}
            className="fixed inset-0 z-40 md:hidden"
          >
            <div
              className="absolute inset-0 bg-black/60 backdrop-blur-sm"
              onClick={() => setMobileOpen(false)}
            />
            <motion.div
              initial={{ x: "100%" }}
              animate={{ x: 0 }}
              exit={{ x: "100%" }}
              transition={{ duration: 0.4, ease: [0.22, 1, 0.36, 1] }}
              className="absolute right-0 top-0 bottom-0 w-72 gradient-cosmic flex flex-col justify-center px-8"
            >
              <div className="flex flex-col gap-2">
                {navLinks.map((link, i) => (
                  <motion.a
                    key={link.href}
                    href={link.href}
                    onClick={() => setMobileOpen(false)}
                    initial={{ opacity: 0, x: 30 }}
                    animate={{ opacity: 1, x: 0 }}
                    transition={{ delay: 0.1 + i * 0.05 }}
                    className="py-3 px-4 text-lg text-white/70 hover:text-white hover:bg-white/5 rounded-xl transition-all duration-300"
                    style={{ fontFamily: "var(--font-heading)" }}
                  >
                    {link.label}
                  </motion.a>
                ))}
              </div>
            </motion.div>
          </motion.div>
        )}
      </AnimatePresence>
    </>
  );
}
