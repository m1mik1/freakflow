// src/config/navItems.ts
import {
    Home,
    ListChecks,
    Tag,
    Bookmark,
    MessageSquare,
    Users
  } from 'lucide-react';
  
  export interface NavItem {
    title: string;
    href: string;
    icon: React.ComponentType<any>;
  }
  
  export const navItems: NavItem[] = [
    { title: 'Home',      href: '/',           icon: Home },
    { title: 'Questions', href: '/questions',  icon: ListChecks },
    { title: 'Tags',      href: '/tags',       icon: Tag },
    { title: 'Saves',     href: '/saves',      icon: Bookmark },
    { title: 'Chat',      href: '/chat',       icon: MessageSquare },
    { title: 'Users',     href: '/users',      icon: Users },
  ];
  