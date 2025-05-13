'use client';

import { useState, useEffect } from 'react';
import Link from 'next/link';
import { usePathname, useRouter, useSearchParams } from 'next/navigation';
import {
  Menu,
  X,
  Home,
  Hash,
  Tag,
  MessageSquare,
  MessageCircle,
  BookOpen,
  Users,
  Briefcase,
  User,
  Search,
  Plus,
} from 'lucide-react';
import {
  Sheet,
  SheetTrigger,
  SheetContent,
  SheetHeader,
  SheetTitle,
  SheetClose,
} from '@/components/ui/sheet';
import {
  Dialog,
  DialogTrigger,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import LoginForm from '@/components/auth/login/LoginForm';

const links = [
  { href: '/about', label: 'About' },
  { href: '/features', label: 'Features' },
  { href: '/contact', label: 'Contact' },
];

function NavLinks({ vertical = false }: { vertical?: boolean }) {
  return (
    <nav className={vertical ? 'flex flex-col space-y-2' : 'flex items-center space-x-4'}>
      {links.map((link) => (
        <Link key={link.href} href={link.href} passHref>
          <Button
            variant="ghost"
            className="text-lime-600 rounded px-3 py-2 hover:bg-lime-50 hover:text-lime-700 transition"
          >
            {link.label}
          </Button>
        </Link>
      ))}
    </nav>
  );
}

export default function Header() {
  const path = usePathname() || '/';
  const isRegister = path === '/register';
  const router = useRouter();
  const params = useSearchParams();
  const [loginOpen, setLoginOpen] = useState(false);

  useEffect(() => {
    if (params.get('login') === 'true') {
      setLoginOpen(true);
    }
  }, [params]);

  const handleLoginOpenChange = (open: boolean) => {
    setLoginOpen(open);
   if (!open) {
      // строим новый queryString без login
    const sp = new URLSearchParams(Array.from(params.entries()));
 sp.delete('login');
   const newQs = sp.toString();
     const newUrl = window.location.pathname + (newQs ? `?${newQs}` : '');
      router.replace(newUrl, { scroll: false });
   }
  };

  return (
    <header className="fixed top-0 left-0 right-0 z-50 bg-white border-b border-lime-300">
      {!isRegister ? (
        <div className="flex items-center justify-between bg-white px-6 py-3 rounded-2xl">
          <div className="flex items-center space-x-6">
            <Link href="/">
              <span className="text-2xl font-bold text-lime-600">FreakFlow</span>
            </Link>
            <NavLinks />
          </div>

          <div className="flex-1 max-w-lg mx-8">
            <Input
              placeholder="Search..."
              className="w-full bg-white rounded-full px-5 py-3 ring-1 ring-lime-300 placeholder-lime-400 focus:outline-none focus:ring-2 focus:ring-lime-400 transition"
            />
          </div>

          <div className="flex items-center space-x-4">
            <Dialog open={loginOpen}
  onOpenChange={handleLoginOpenChange}>
              <DialogTrigger asChild>
                <Button
                  variant="outline"
                  className="flex items-center space-x-2 bg-white rounded-full px-4 py-2 text-lime-600 ring-1 ring-lime-300 hover:bg-lime-50 hover:border-lime-400 hover:text-lime-700 transition"
                >
                  <User className="w-5 h-5 text-lime-600" />
                  <span className="text-sm">Log in</span>
                </Button>
              </DialogTrigger>
              <DialogContent>
                <DialogHeader>
                  <DialogTitle className="text-center font-semibold text-xl mb-2">
                    Увійти в FreakFlow
                  </DialogTitle>
                </DialogHeader>
                <LoginForm
                  onSuccess={() => {
                    setLoginOpen(false);
                    router.push('/');
                  }}
                />
              </DialogContent>
            </Dialog>

            <Link href="/register">
              <Button className="flex items-center space-x-2 bg-lime-400/90 text-white rounded-full px-4 py-2 hover:bg-lime-300 transition">
                <Plus className="w-5 h-5" />
                <span className="text-sm">Sign Up</span>
              </Button>
            </Link>
          </div>
        </div>
      ) : (
        <div className="relative flex items-center justify-center px-6 py-2">
          <Sheet>
            <SheetTrigger asChild>
              <Button variant="ghost" className="p-0 text-lime-600 absolute left-6">
                <Menu className="w-6 h-6" />
              </Button>
            </SheetTrigger>
            <SheetContent side="left" className="w-64 max-w-full overflow-y-auto">
              <SheetHeader>
                <SheetTitle>Навигация</SheetTitle>
                <SheetClose asChild>
                  <Button
                    variant="ghost"
                    className="absolute top-3 right-3 p-0"
                  >
                    <X className="w-5 h-5 text-gray-500" />
                  </Button>
                </SheetClose>
              </SheetHeader>

              <div className="mt-4">
                <NavLinks vertical />
              </div>

              <div className="mt-4 px-4">
                <Input
                  placeholder="Search..."
                  className="w-full rounded-md border-lime-300 focus:ring-lime-300"
                />
              </div>
            </SheetContent>
          </Sheet>

          <Link href="/" className="mx-4">
            <span className="text-2xl font-bold text-lime-600">FreakFlow</span>
          </Link>

          <div className="hidden md:flex items-center gap-4">
            <NavLinks />
          </div>

          <div className="hidden lg:block ml-6">
            <Input
              placeholder="Search..."
              className="w-64 rounded-full border-lime-300 focus:ring-lime-300"
            />
          </div>
        </div>
      )}
    </header>
  );
}