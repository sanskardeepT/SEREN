"use client";

import { useEffect, useRef } from "react";

interface Star {
  x: number;
  y: number;
  radius: number;
  opacity: number;
  speed: number;
  twinkleSpeed: number;
  twinkleOffset: number;
}

export default function StarField({ className = "" }: { className?: string }) {
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const starsRef = useRef<Star[]>([]);
  const animRef = useRef<number>(0);

  useEffect(() => {
    const canvas = canvasRef.current;
    if (!canvas) return;

    const ctx = canvas.getContext("2d");
    if (!ctx) return;

    let w = 0;
    let h = 0;

    const resizeCanvas = () => {
      const dpr = Math.min(window.devicePixelRatio || 1, 2);
      w = window.innerWidth;
      h = window.innerHeight;
      canvas.width = w * dpr;
      canvas.height = h * dpr;
      canvas.style.width = `${w}px`;
      canvas.style.height = `${h}px`;
      // Reset transform before scaling to prevent accumulation
      ctx.setTransform(dpr, 0, 0, dpr, 0, 0);
      initStars();
    };

    const initStars = () => {
      const count = Math.min(Math.floor((w * h) / 8000), 120);
      starsRef.current = Array.from({ length: count }, () => ({
        x: Math.random() * w,
        y: Math.random() * h,
        radius: Math.random() * 1.5 + 0.3,
        opacity: Math.random() * 0.5 + 0.15,
        speed: Math.random() * 0.1 + 0.02,
        twinkleSpeed: Math.random() * 0.005 + 0.002,
        twinkleOffset: Math.random() * Math.PI * 2,
      }));
    };

    let frameCount = 0;

    const animate = (time: number) => {
      frameCount++;
      // Throttle to ~30fps
      if (frameCount % 2 !== 0) {
        animRef.current = requestAnimationFrame(animate);
        return;
      }

      ctx.clearRect(0, 0, w, h);

      const stars = starsRef.current;
      for (let i = 0; i < stars.length; i++) {
        const star = stars[i];
        const twinkle = Math.sin(time * star.twinkleSpeed + star.twinkleOffset);
        const opacity = star.opacity + twinkle * 0.2;
        const finalOpacity = Math.max(0.05, Math.min(0.9, opacity));

        ctx.beginPath();
        ctx.arc(star.x, star.y, star.radius, 0, Math.PI * 2);
        ctx.fillStyle = `rgba(255, 255, 255, ${finalOpacity})`;
        ctx.fill();

        // Subtle glow for larger stars only
        if (star.radius > 1.2) {
          ctx.beginPath();
          ctx.arc(star.x, star.y, star.radius * 3, 0, Math.PI * 2);
          ctx.fillStyle = `rgba(92, 224, 216, ${finalOpacity * 0.08})`;
          ctx.fill();
        }

        // Gentle drift
        star.y -= star.speed;
        if (star.y < -10) {
          star.y = h + 10;
          star.x = Math.random() * w;
        }
      }

      animRef.current = requestAnimationFrame(animate);
    };

    resizeCanvas();
    animRef.current = requestAnimationFrame(animate);
    window.addEventListener("resize", resizeCanvas);

    return () => {
      cancelAnimationFrame(animRef.current);
      window.removeEventListener("resize", resizeCanvas);
    };
  }, []);

  return (
    <canvas
      ref={canvasRef}
      className={`pointer-events-none ${className}`}
      style={{ position: "absolute", inset: 0, zIndex: 0 }}
      aria-hidden="true"
    />
  );
}
