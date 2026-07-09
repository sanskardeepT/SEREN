import type { Metadata } from "next";
import { Inter, Fraunces } from "next/font/google";
import "./globals.css";

const inter = Inter({
  subsets: ["latin"],
  variable: "--font-body",
  display: "swap",
});

const fraunces = Fraunces({
  subsets: ["latin"],
  variable: "--font-display",
  display: "swap",
  style: ["normal", "italic"],
  weight: ["300", "400", "600", "700"],
});

export const metadata: Metadata = {
  title: "SEREN — Every Child is a Star",
  description:
    "Helping schools and families identify hidden learning and developmental differences earlier — so every child gets the chance they deserve.",
  keywords: [
    "learning differences",
    "child development",
    "early identification",
    "dyslexia",
    "ADHD",
    "autism",
    "schools",
    "education",
    "NEP 2020",
    "special education",
  ],
  authors: [{ name: "SEREN" }],
  openGraph: {
    title: "SEREN — Every Child is a Star",
    description:
      "Helping schools and families identify hidden learning and developmental differences earlier — so every child gets the chance they deserve.",
    type: "website",
    locale: "en_IN",
    siteName: "SEREN",
  },
  twitter: {
    card: "summary_large_image",
    title: "SEREN — Every Child is a Star",
    description:
      "Helping schools and families identify hidden learning and developmental differences earlier — so every child gets the chance they deserve.",
  },
  robots: {
    index: true,
    follow: true,
  },
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" className={`${inter.variable} ${fraunces.variable}`}>
      <head>
        <script
          type="application/ld+json"
          dangerouslySetInnerHTML={{
            __html: JSON.stringify({
              "@context": "https://schema.org",
              "@type": "Organization",
              name: "SEREN",
              description:
                "Helping schools and families identify hidden learning and developmental differences earlier.",
              url: "https://seren.in",
            }),
          }}
        />
      </head>
      <body>{children}</body>
    </html>
  );
}
