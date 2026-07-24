import Link from "next/link";
import { Shield, ArrowLeft, Lock, FileText, CheckCircle2 } from "lucide-react";

export const metadata = {
  title: "Privacy Policy | SEREN — On-Device Neurodevelopmental Screening",
  description:
    "Official Privacy Policy and Data Safety Statement for the SEREN neurodevelopmental and emotional screening platform.",
};

export default function PrivacyPage() {
  return (
    <div className="min-h-screen bg-[#070B14] text-white/90 selection:bg-cyan/30 selection:text-cyan">
      {/* Top Navbar */}
      <header className="border-b border-white/10 bg-[#070B14]/80 backdrop-blur-md sticky top-0 z-50">
        <div className="max-w-5xl mx-auto px-6 h-20 flex items-center justify-between">
          <Link
            href="/"
            className="flex items-center gap-2 text-sm text-white/60 hover:text-white transition-colors"
          >
            <ArrowLeft size={16} />
            Back to SEREN Home
          </Link>
          <div className="flex items-center gap-3">
            <Shield className="w-5 h-5 text-cyan" />
            <span className="font-semibold text-white tracking-wider text-sm uppercase">
              Data Sovereignty Guarantee
            </span>
          </div>
        </div>
      </header>

      {/* Main Content Area */}
      <main className="max-w-4xl mx-auto px-6 py-16">
        <div className="mb-12">
          <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full text-xs font-mono bg-cyan/10 border border-cyan/20 text-cyan mb-4">
            <Lock size={12} /> Google Play Data Safety Compliant
          </div>
          <h1
            className="text-4xl md:text-5xl font-bold text-white mb-4 tracking-tight"
            style={{ fontFamily: "var(--font-heading)" }}
          >
            Privacy Policy & Data Sovereignty
          </h1>
          <p className="text-white/60 text-lg">
            Effective Date: July 24, 2026 • Version 1.0.0
          </p>
        </div>

        {/* Highlights Banner */}
        <div className="p-6 rounded-2xl bg-white/[0.03] border border-white/10 mb-12 grid grid-cols-1 md:grid-cols-3 gap-6">
          <div className="flex gap-3">
            <CheckCircle2 className="w-5 h-5 text-cyan shrink-0 mt-1" />
            <div>
              <h4 className="font-medium text-white text-sm">100% On-Device</h4>
              <p className="text-xs text-white/50 mt-1">
                Zero data sent to external servers or cloud analytics.
              </p>
            </div>
          </div>
          <div className="flex gap-3">
            <CheckCircle2 className="w-5 h-5 text-cyan shrink-0 mt-1" />
            <div>
              <h4 className="font-medium text-white text-sm">SQLCipher Encrypted</h4>
              <p className="text-xs text-white/50 mt-1">
                Hardware-backed Android Keystore AES-256 database protection.
              </p>
            </div>
          </div>
          <div className="flex gap-3">
            <CheckCircle2 className="w-5 h-5 text-cyan shrink-0 mt-1" />
            <div>
              <h4 className="font-medium text-white text-sm">Offline First</h4>
              <p className="text-xs text-white/50 mt-1">
                Zero INTERNET permissions requested in the app manifest.
              </p>
            </div>
          </div>
        </div>

        {/* Section Blocks */}
        <div className="space-y-10 text-white/80 leading-relaxed text-sm md:text-base">
          <section className="space-y-3">
            <h2 className="text-xl font-semibold text-white flex items-center gap-2">
              <FileText className="w-5 h-5 text-cyan" /> 1. Overview & Data Philosophy
            </h2>
            <p>
              SEREN (&quot;we&quot;, &quot;our&quot;, or &quot;us&quot;) is committed to absolute data privacy and sovereignty. The SEREN mobile application operates as a <strong>100% offline-first, on-device screening engine</strong> designed to assist parents, educators, and clinicians in identifying potential neurodevelopmental and emotional indicators.
            </p>
            <p>
              Because we process sensitive child behavioral and assessment data, SEREN is architected to guarantee that <strong>no personally identifiable information (PII), audio recordings, drawing strokes, gaze metrics, or assessment scores ever leave your physical device.</strong>
            </p>
          </section>

          <section className="space-y-3">
            <h2 className="text-xl font-semibold text-white">2. Information Handled & On-Device Storage</h2>
            <p>During a screening session, the application collects and processes the following data types <em>strictly within local device RAM and encrypted storage</em>:</p>
            <ul className="list-disc list-inside space-y-2 pl-4 text-white/70">
              <li><strong>Motor & Handwriting Signals:</strong> Touch coordinates, stroke velocity, and drawing canvas samples (processed by DrawNet TFLite).</li>
              <li><strong>Speech & Audio Signals:</strong> Microphone audio snippets captured strictly during the vocal disfluency task (processed by PhonNet TFLite). Audio is analyzed immediately on-device and stored locally in encrypted Room database tables.</li>
              <li><strong>Behavioral Pacing & Reaction Times:</strong> Continuous Performance Task (CPT) tap timings and Corsi block sequence latencies.</li>
              <li><strong>Self-Report Questionnaire Endorsements:</strong> Likert ratings for anxiety, emotional, sensory, and executive function domains.</li>
            </ul>
          </section>

          <section className="space-y-3">
            <h2 className="text-xl font-semibold text-white">3. Technical Security & Encryption Standards</h2>
            <p>Local data security is maintained using clinical-grade hardware cryptographic primitives:</p>
            <ul className="list-disc list-inside space-y-2 pl-4 text-white/70">
              <li><strong>Database Passphrase:</strong> Generated using a 256-bit cryptographically secure random number generator and encrypted using AES-256-GCM backed by the <code>AndroidKeyStore</code>.</li>
              <li><strong>Database Layer:</strong> Encrypted using SQLCipher (256-bit AES encryption for SQLite).</li>
              <li><strong>Backup Prohibition:</strong> Cloud backups are explicitly disabled via <code>android:allowBackup=&quot;false&quot;</code> in the application manifest.</li>
            </ul>
          </section>

          <section className="space-y-3">
            <h2 className="text-xl font-semibold text-white">4. Zero Third-Party Sharing & Zero Tracking</h2>
            <p>
              SEREN contains <strong>no third-party SDKs, no advertising identifiers, no analytics trackers (e.g., Firebase, Mixpanel), and no network communication libraries.</strong> The application manifest explicitly omits the <code>android.permission.INTERNET</code> permission. We cannot sell, share, transmit, or monetize your data under any circumstances because we never possess or receive it.
            </p>
          </section>

          <section className="space-y-3">
            <h2 className="text-xl font-semibold text-white">5. Clinical Disclaimer & Regulatory Status</h2>
            <p>
              SEREN is a research-informed screening and early-indicator tool. It is <strong>not a medical diagnostic device</strong> and does not replace evaluation by a qualified physician, developmental pediatrician, or clinical psychologist. All screening output reports explicitly include this clinical disclaimer.
            </p>
          </section>

          <section className="space-y-3 border-t border-white/10 pt-8">
            <h2 className="text-xl font-semibold text-white">6. Contact & Legal Enquiries</h2>
            <p>
              If you have questions regarding SEREN&apos;s privacy architecture, clinical ground reality documentation, or legal compliance, please contact our medical advisory team at:
            </p>
            <p className="font-mono text-cyan text-sm">
              privacy@seren-app.org | research@seren-app.org
            </p>
          </section>
        </div>
      </main>

      {/* Simple Footer */}
      <footer className="border-t border-white/10 py-8 text-center text-xs text-white/40">
        <p>© 2026 SEREN Health Technologies. All rights reserved. Data Sovereignty Guaranteed.</p>
      </footer>
    </div>
  );
}
