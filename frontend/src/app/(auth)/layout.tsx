// src/app/(auth)/layout.tsx
'use client';

import Header from '@/components/layout/Header';
import Footer from '@/components/layout/Footer';

export default function AuthLayout({ children }: { children: React.ReactNode }) {
  return (
    <div className="flex flex-col min-h-screen">
      <Header />

      {/* заполняет всё свободное пространство */}
      <main className="flex-1 flex items-center justify-center px-4">
        {children}
      </main>

      <Footer />
    </div>
  );
}
