// src/app/(auth)/layout.tsx
'use client';

import Header from '@/components/layout/Header';
import Footer from '@/components/layout/Footer';
import { Toaster } from '@/components/ui/sonner';

export default function AuthLayout({ children }: { children: React.ReactNode }) {
  return (
    <div className="flex flex-col min-h-screen">
      <Header />

      {/* заполняет всё свободное пространство */}
      <main className="flex-1 flex items-center justify-center px-4">
        {children}
        <Toaster
              position="top-center"
              richColors
              toastOptions={{
                className: 'bg-white text-lime-600',
                style: { backgroundColor: '#ffffff', color: '#5EA500' },
                descriptionClassName: 'text-lime-600',
              }}
            />
      </main>

      <Footer />
    </div>
  );
}
