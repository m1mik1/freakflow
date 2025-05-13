// src/app/(app)/layout.tsx
'use client';

import ClientAuthProvider from '@/providers/ClientAuthProvider';
import Header from '@/components/layout/Header';
import AppSidebar from '@/components/layout/AppSidebar';
import Footer from '@/components/layout/Footer';
import HelpButton from '@/components/layout/HelpButton';
import { Toaster } from '@/components/ui/sonner';
import { SidebarProvider } from '@/components/ui/sidebar';

export default function AppLayout({ children }: { children: React.ReactNode }) {
  return (
    <ClientAuthProvider>
      <Header />

      {/*  
        Задаём grid: первая колонка 16rem (w-64), вторая — 1fr
        pt-16 чтобы не попадать под Header высотой 4rem
      */}
      <main className="pt-16 grid grid-cols-[16rem_1fr]">
        {/* 1) Fixed-styled Sidebar */}
        <SidebarProvider>
          <AppSidebar />
        </SidebarProvider>

        {/* 2) Контентная колонка */}
        <div className="overflow-y-auto">
          {/* Внутри ограничиваем максимальную ширину и центрируем */}
          <div className="container mx-auto px-4 py-6 flex flex-col gap-6">
            {/* Основной контент */}
            <div className="flex-1">{children}</div>

            {/* Помощник и тосты */}
            <HelpButton />
            <Toaster
              position="top-center"
              richColors
              toastOptions={{
                className: 'bg-white text-lime-600',
                style: { backgroundColor: '#ffffff', color: '#5EA500' },
                descriptionClassName: 'text-lime-600',
              }}
            />
          </div>
        </div>
      </main>

      <Footer />
    </ClientAuthProvider>
  );
}
