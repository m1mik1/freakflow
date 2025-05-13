'use client';

import Link from 'next/link';

export default function Footer() {
  return (
    <footer className="bg-white border-t border-lime-200 py-3 text-sm text-gray-500">
      <div className="container mx-auto px-4 flex items-center justify-center space-x-1">
        <Link
          href="https://github.com/m1mik1/freakflow"
          target="_blank"
          rel="noreferrer"
          className="hover:text-lime-600 transition"
        >
          GitHub
        </Link>
        <span className="text-gray-400">·</span>
        <Link
          href="mailto:rostikpastusenko2@gmail.com"
          className="hover:text-lime-600 transition"
        >
          Підтримка
        </Link>
      </div>
      <p className="container mx-auto px-4 mt-1 text-center">© {new Date().getFullYear()} FreakFlow</p>
    </footer>
  );
}