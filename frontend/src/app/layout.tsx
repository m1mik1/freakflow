// src/app/layout.tsx
import './globals.css'
import type { Metadata } from 'next'
import { Geist, Geist_Mono } from 'next/font/google'

const geistSans = Geist({ variable: '--font-geist-sans', subsets: ['latin'] })
const geistMono = Geist_Mono({ variable: '--font-geist-mono', subsets: ['latin'] })

export const metadata: Metadata = { title: 'FreakFlow' }

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="uk" className={`${geistSans.variable} ${geistMono.variable}`}>
      <body className="min-h-screen bg-white font-[var(--font-geist-sans)]">
        {children}
      </body>
    </html>
  )
}
