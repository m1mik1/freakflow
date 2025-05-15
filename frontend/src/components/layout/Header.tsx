'use client';

import { useState, useEffect, useRef } from 'react';
import Link from 'next/link';
import { usePathname, useRouter, useSearchParams } from 'next/navigation';
import {
  Menu,
  X,
  User,
  Search,
  Plus
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
import LoginForm from '../auth/login/LoginForm';
import { SearchHelper } from '@/components/layout/SearchHelper'

// Main navigation links
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

  // Controlled search input
  const [searchValue, setSearchValue] = useState('');
  const [helperOpen, setHelperOpen] = useState(false);
  const inputRef = useRef<HTMLInputElement>(null);

  // fill from URL
  useEffect(() => {
    setSearchValue(params.get('q') || '');
  }, [params]);

  // listen for “use” clicks inside helper
  useEffect(() => {
    function onHelperSelect(e: CustomEvent<string>) {
      setSearchValue(e.detail);
      setHelperOpen(false);
      // return focus to input
      inputRef.current?.focus();
    }
    window.addEventListener('helper-select', onHelperSelect as any);
    return () => window.removeEventListener('helper-select', onHelperSelect as any);
  }, []);

  const onSearch = () => {
    const sp = new URLSearchParams(Array.from(params.entries()));
    if (searchValue) sp.set('q', searchValue);
    else sp.delete('q');
    sp.set('page', '0');
    sp.set('size', '20');
    setHelperOpen(false); 
    router.push(`/search?${sp.toString()}`);
  };

  // Login dialog
  const [loginOpen, setLoginOpen] = useState(false);
  useEffect(() => {
    if (params.get('login') === 'true') setLoginOpen(true);
  }, [params]);
  const handleLoginOpenChange = (open: boolean) => {
    setLoginOpen(open);
    if (!open) {
      const sp = new URLSearchParams(Array.from(params.entries()));
      sp.delete('login');
      const newQs = sp.toString();
      router.replace(window.location.pathname + (newQs ? `?${newQs}` : ''), { scroll: false });
    }
  };

  return (
    <header className="fixed top-0 left-0 right-0 z-50 bg-white border-b border-lime-200">
      {!isRegister ? (
        <div className="flex items-center justify-between px-6 py-3 bg-white rounded-2xl">
          {/* logo + nav */}
          <div className="flex items-center space-x-6">
            <Link href="/" className="flex items-center space-x-2">
              <img src="/logo-icon.svg" className="w-8 h-8" alt="FreakFlow logo" />
              <span className="text-2xl font-bold text-lime-600">FreakFlow</span>
            </Link>
            <NavLinks />
          </div>

          {/* Search Input + helper */}
          <div className="flex-1 max-w-lg mx-8 relative">
            <Search className="absolute left-4 top-1/2 -translate-y-1/2 text-lime-400" />
            <Input
              ref={inputRef}
              value={searchValue}
              onChange={e => setSearchValue(e.target.value)}
              onFocus={() => setHelperOpen(true)}
              onKeyDown={e => {
                if (e.key === 'Enter') onSearch();
                // Escape closes helper
                if (e.key === 'Escape') setHelperOpen(false);
              }}
              placeholder="Search..."
              className="w-full pl-11 pr-4 bg-white rounded-full ring-1 ring-lime-300 focus:ring-2 focus:ring-lime-400 transition"
            />
            <SearchHelper open={helperOpen} onClose={() => setHelperOpen(false)} />
          </div>

          {/* Auth buttons */}
          <div className="flex items-center space-x-4">
            <Dialog open={loginOpen} onOpenChange={handleLoginOpenChange}>
              <DialogTrigger asChild>
                <Button variant="outline" className="flex items-center space-x-2 px-4 py-2 rounded-full text-lime-600 ring-1 ring-lime-300 hover:bg-lime-50 transition">
                  <User className="w-5 h-5" />
                  <span className="text-sm">Log in</span>
                </Button>
              </DialogTrigger>
              <DialogContent>
                <DialogHeader>
                  <DialogTitle className="text-center text-xl font-semibold mb-2">Log In to FreakFlow</DialogTitle>
                </DialogHeader>
                <LoginForm onSuccess={() => { setLoginOpen(false); router.push('/'); }} />
              </DialogContent>
            </Dialog>
            <Link href="/register">
              <Button className="flex items-center space-x-2 px-4 py-2 bg-lime-400/90 text-white rounded-full hover:bg-lime-300 transition">
                <Plus className="w-5 h-5" />
                <span className="text-sm">Sign Up</span>
              </Button>
            </Link>
          </div>
        </div>
      ) : (
        <div className="relative flex items-center justify-center px-6 py-2 bg-white">
          {/* Mobile nav sheet */}
          <Sheet>
            <SheetTrigger asChild>
              <Button variant="ghost" className="p-0 text-lime-600 absolute left-6">
                <Menu className="w-6 h-6" />
              </Button>
            </SheetTrigger>
            <SheetContent side="left" className="w-64 overflow-y-auto">
              <SheetHeader>
                <SheetTitle>Navigation</SheetTitle>
                <SheetClose asChild>
                  <Button variant="ghost" className="absolute top-3 right-3 p-0">
                    <X className="w-5 h-5 text-gray-500" />
                  </Button>
                </SheetClose>
              </SheetHeader>
              <div className="mt-4 px-4">
                <NavLinks vertical />
              </div>
            </SheetContent>
          </Sheet>

          <div className="flex items-center space-x-6">
            <Link
              href="/"
              className="flex items-center space-x-2 mx-4"
            >
              <img
                src="/logo-icon.svg"
                className="w-8 h-8"
                alt="FreakFlow logo"
              />
              <span className="text-2xl font-bold text-lime-600">
                FreakFlow
              </span>
            </Link>
          </div>

          <div className="hidden md:flex items-center gap-4">
            <NavLinks />
          </div>

          {/* Search on register page too */}
          <div className="hidden lg:block ml-6">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-lime-400" />
              <Input
                value={searchValue}
                onChange={e => setSearchValue(e.target.value)}
                onKeyDown={e => e.key === 'Enter' && onSearch()}
                placeholder="Search..."
                className="w-64 pl-10 pr-4 bg-white rounded-full border border-lime-300 focus:ring-2 focus:ring-lime-400 transition"
              />
            </div>
          </div>
        </div>
      )}
    </header>
  );
}
