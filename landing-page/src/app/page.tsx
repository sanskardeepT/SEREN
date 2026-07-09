"use client";

import { useState } from "react";
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";
import WaitlistModal from "@/components/WaitlistModal";
import HeroSection from "@/sections/HeroSection";
import StorytellingSection from "@/sections/StorytellingSection";
import SocialImpactSection from "@/sections/SocialImpactSection";
import ResearchSection from "@/sections/ResearchSection";
import NEPSection from "@/sections/NEPSection";
import ConstellationSection from "@/sections/ConstellationSection";
import TributeSection from "@/sections/TributeSection";
import AudienceSection from "@/sections/AudienceSection";
import BusinessModelSection from "@/sections/BusinessModelSection";
import TrustSection from "@/sections/TrustSection";
import TeamSection from "@/sections/TeamSection";
import ContactSection from "@/sections/ContactSection";

export default function Home() {
  const [waitlistOpen, setWaitlistOpen] = useState(false);

  return (
    <>
      <Navbar />
      <WaitlistModal isOpen={waitlistOpen} onClose={() => setWaitlistOpen(false)} />

      <main>
        <HeroSection onOpenWaitlist={() => setWaitlistOpen(true)} />
        <StorytellingSection />
        <SocialImpactSection />
        <ResearchSection />
        <NEPSection />
        <ConstellationSection />
        <TributeSection />
        <AudienceSection />
        <BusinessModelSection />
        <TrustSection />
        <TeamSection />
        <ContactSection />
      </main>

      <Footer />
    </>
  );
}
