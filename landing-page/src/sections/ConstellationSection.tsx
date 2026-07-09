"use client";

import { useRef, useEffect, useState, useCallback } from "react";
import { motion, useInView } from "framer-motion";
import { conditions } from "@/lib/constants";

interface ConstellationStar {
  x: number;
  y: number;
  radius: number;
  label: string;
  baseOpacity: number;
  connections: number[];
  vx: number;
  vy: number;
}

export default function ConstellationSection() {
  const sectionRef = useRef<HTMLDivElement>(null);
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const starsRef = useRef<ConstellationStar[]>([]);
  const animRef = useRef<number>(0);
  const mouseRef = useRef({ x: -1000, y: -1000 });
  const hoveredRef = useRef<string | null>(null);
  const tooltipRef = useRef<HTMLDivElement>(null);
  const [isVisible, setIsVisible] = useState(false);
  const isInView = useInView(sectionRef, { once: true, margin: "-100px" });

  const initStars = useCallback((w: number, h: number) => {
    const padding = 60;
    const stars: ConstellationStar[] = conditions.map((label) => ({
      x: padding + Math.random() * (w - padding * 2),
      y: padding + Math.random() * (h - padding * 2),
      radius: 2 + Math.random() * 3,
      label,
      baseOpacity: 0.35 + Math.random() * 0.4,
      connections: [],
      vx: (Math.random() - 0.5) * 0.12,
      vy: (Math.random() - 0.5) * 0.12,
    }));

    // Create constellation connections (random pairs, limit to nearby stars)
    stars.forEach((star, i) => {
      const numConnections = Math.floor(Math.random() * 2) + 1;
      for (let c = 0; c < numConnections; c++) {
        const target = Math.floor(Math.random() * stars.length);
        if (target !== i) {
          const dx = stars[target].x - star.x;
          const dy = stars[target].y - star.y;
          const dist = Math.sqrt(dx * dx + dy * dy);
          if (dist < 220) {
            star.connections.push(target);
          }
        }
      }
    });

    starsRef.current = stars;
  }, []);

  useEffect(() => {
    const canvas = canvasRef.current;
    const section = sectionRef.current;
    if (!canvas || !section) return;

    const ctx = canvas.getContext("2d");
    if (!ctx) return;

    let w = 0;
    let h = 0;

    const resize = () => {
      const rect = section.getBoundingClientRect();
      const dpr = Math.min(window.devicePixelRatio || 1, 2);
      w = rect.width;
      h = 600;
      canvas.width = w * dpr;
      canvas.height = h * dpr;
      canvas.style.width = `${w}px`;
      canvas.style.height = `${h}px`;
      ctx.setTransform(dpr, 0, 0, dpr, 0, 0);
      if (starsRef.current.length === 0) {
        initStars(w, h);
      }
    };

    const handleMouseMove = (e: MouseEvent) => {
      const rect = canvas.getBoundingClientRect();
      mouseRef.current = { x: e.clientX - rect.left, y: e.clientY - rect.top };
    };

    const handleMouseLeave = () => {
      mouseRef.current = { x: -1000, y: -1000 };
      if (tooltipRef.current) {
        tooltipRef.current.style.opacity = "0";
      }
      hoveredRef.current = null;
    };

    // Use IntersectionObserver to only animate when visible
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          setIsVisible(entry.isIntersecting);
        });
      },
      { threshold: 0.1 }
    );
    observer.observe(canvas);

    resize();
    window.addEventListener("resize", resize);
    canvas.addEventListener("mousemove", handleMouseMove, { passive: true });
    canvas.addEventListener("mouseleave", handleMouseLeave);

    return () => {
      cancelAnimationFrame(animRef.current);
      window.removeEventListener("resize", resize);
      canvas.removeEventListener("mousemove", handleMouseMove);
      canvas.removeEventListener("mouseleave", handleMouseLeave);
      observer.disconnect();
    };
  }, [initStars]);

  // Separate effect for animation loop - only runs when visible
  useEffect(() => {
    const canvas = canvasRef.current;
    if (!canvas || !isVisible) {
      cancelAnimationFrame(animRef.current);
      return;
    }

    const ctx = canvas.getContext("2d");
    if (!ctx) return;

    const dpr = Math.min(window.devicePixelRatio || 1, 2);
    const w = canvas.width / dpr;
    const h = canvas.height / dpr;

    let frameCount = 0;

    const animate = (time: number) => {
      frameCount++;
      // Throttle to ~30fps to reduce CPU load
      if (frameCount % 2 !== 0) {
        animRef.current = requestAnimationFrame(animate);
        return;
      }

      ctx.clearRect(0, 0, w, h);

      const mx = mouseRef.current.x;
      const my = mouseRef.current.y;
      let closestDist = Infinity;
      let closestLabel = "";
      let closestX = 0;
      let closestY = 0;

      const stars = starsRef.current;

      // Draw connections
      ctx.lineWidth = 0.4;
      for (let i = 0; i < stars.length; i++) {
        const star = stars[i];
        for (let c = 0; c < star.connections.length; c++) {
          const target = stars[star.connections[c]];
          if (!target) continue;
          const dx = target.x - star.x;
          const dy = target.y - star.y;
          const dist = Math.sqrt(dx * dx + dy * dy);
          if (dist > 220) continue;

          const mouseDistStar = Math.sqrt((mx - star.x) ** 2 + (my - star.y) ** 2);
          const mouseDistTarget = Math.sqrt((mx - target.x) ** 2 + (my - target.y) ** 2);
          const mouseNear = Math.min(mouseDistStar, mouseDistTarget) < 120;

          ctx.beginPath();
          ctx.moveTo(star.x, star.y);
          ctx.lineTo(target.x, target.y);
          ctx.strokeStyle = mouseNear
            ? "rgba(92, 224, 216, 0.15)"
            : "rgba(255, 255, 255, 0.04)";
          ctx.lineWidth = mouseNear ? 0.8 : 0.4;
          ctx.stroke();
        }
      }

      // Draw stars
      const timeFactor = time * 0.002;
      for (let i = 0; i < stars.length; i++) {
        const star = stars[i];

        // Gentle drift
        star.x += star.vx;
        star.y += star.vy;
        if (star.x < 30 || star.x > w - 30) star.vx *= -1;
        if (star.y < 30 || star.y > h - 30) star.vy *= -1;

        const distToMouse = Math.sqrt((mx - star.x) ** 2 + (my - star.y) ** 2);
        const isNear = distToMouse < 100;
        const twinkle = Math.sin(timeFactor + star.x * 0.01) * 0.15;
        const opacity = isNear ? 1 : star.baseOpacity + twinkle;
        const radius = isNear ? star.radius * 1.8 : star.radius;

        if (distToMouse < closestDist && distToMouse < 60) {
          closestDist = distToMouse;
          closestLabel = star.label;
          closestX = star.x;
          closestY = star.y;
        }

        // Glow for nearby or large stars
        if (isNear || star.radius > 3.5) {
          const glowRadius = radius * 5;
          const gradient = ctx.createRadialGradient(
            star.x, star.y, 0,
            star.x, star.y, glowRadius
          );
          gradient.addColorStop(0, `rgba(92, 224, 216, ${opacity * 0.15})`);
          gradient.addColorStop(1, "transparent");
          ctx.beginPath();
          ctx.arc(star.x, star.y, glowRadius, 0, Math.PI * 2);
          ctx.fillStyle = gradient;
          ctx.fill();
        }

        // Core star
        ctx.beginPath();
        ctx.arc(star.x, star.y, radius, 0, Math.PI * 2);
        ctx.fillStyle = `rgba(255, 255, 255, ${Math.max(0.1, opacity)})`;
        ctx.fill();
      }

      // Update tooltip via DOM directly (no React state updates in animation loop)
      const tooltip = tooltipRef.current;
      if (tooltip) {
        if (closestLabel) {
          tooltip.textContent = closestLabel;
          tooltip.style.left = `${closestX}px`;
          tooltip.style.top = `${closestY - 45}px`;
          tooltip.style.opacity = "1";
          hoveredRef.current = closestLabel;
        } else if (hoveredRef.current) {
          tooltip.style.opacity = "0";
          hoveredRef.current = null;
        }
      }

      animRef.current = requestAnimationFrame(animate);
    };

    animRef.current = requestAnimationFrame(animate);

    return () => {
      cancelAnimationFrame(animRef.current);
    };
  }, [isVisible]);

  return (
    <section
      ref={sectionRef}
      className="relative gradient-cosmic overflow-hidden"
      aria-label="Conditions constellation"
    >
      {/* Header */}
      <div className="section-padding pb-8">
        <div className="section-container text-center relative z-10">
          <motion.p
            initial={{ opacity: 0 }}
            animate={isInView ? { opacity: 1 } : {}}
            transition={{ duration: 0.8 }}
            className="text-xs tracking-[0.3em] uppercase text-white/30 mb-4"
          >
            Understanding Every Difference
          </motion.p>
          <motion.h2
            initial={{ opacity: 0, y: 30 }}
            animate={isInView ? { opacity: 1, y: 0 } : {}}
            transition={{ duration: 0.8, delay: 0.1 }}
            className="text-3xl md:text-4xl lg:text-5xl font-semibold text-white mb-4"
            style={{ fontFamily: "var(--font-heading)" }}
          >
            Every challenge is a{" "}
            <span className="text-gradient-gold">star</span>{" "}
            in their constellation
          </motion.h2>
          <motion.p
            initial={{ opacity: 0 }}
            animate={isInView ? { opacity: 1 } : {}}
            transition={{ duration: 0.8, delay: 0.2 }}
            className="text-white/40 max-w-xl mx-auto"
          >
            Over 60 conditions and differences, each one a part of a child&apos;s unique story.
            Hover to explore.
          </motion.p>
        </div>
      </div>

      {/* Canvas container */}
      <div className="relative" style={{ height: "600px" }}>
        <canvas
          ref={canvasRef}
          className="absolute inset-0 cursor-crosshair"
        />

        {/* Tooltip — updated via ref, not state */}
        <div
          ref={tooltipRef}
          className="absolute pointer-events-none z-20"
          style={{
            opacity: 0,
            transform: "translateX(-50%)",
            transition: "opacity 0.15s ease",
          }}
        >
          <div
            className="px-4 py-2 rounded-xl text-sm font-medium text-white whitespace-nowrap"
            style={{
              background: "rgba(10, 22, 40, 0.9)",
              backdropFilter: "blur(20px)",
              border: "1px solid rgba(92, 224, 216, 0.2)",
              boxShadow: "0 8px 32px rgba(0, 0, 0, 0.3), 0 0 20px rgba(92, 224, 216, 0.1)",
              fontFamily: "var(--font-heading)",
            }}
          />
        </div>
      </div>

      {/* Bottom padding */}
      <div className="h-20" />
    </section>
  );
}
