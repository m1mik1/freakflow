// src/components/layout/AppSidebar.tsx
'use client';

import Link from 'next/link';
import { usePathname } from 'next/navigation';
import {
  SidebarContent,
  SidebarGroup,
  SidebarGroupContent,
  SidebarGroupLabel,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from '@/components/ui/sidebar';
import { navItems } from '@/config/navItems';

export default function AppSidebar() {
  const path = usePathname();
  return (
    <aside className="hidden md:block relative top-0 left-0 z-40  w-64 overflow-y-auto border-r border-lime-200 bg-white">
      <div className="sticky top-16">
        <SidebarContent className="p-2 space-y-4">
          <SidebarGroup>
            <SidebarGroupLabel className="text-sm font-semibold text-gray-500">
              Навігація
            </SidebarGroupLabel>
            <SidebarGroupContent>
              <SidebarMenu className="space-y-2">
                {navItems.map(({ href, icon: Icon, title }) => {
                  const active = path === href;
                  return (
                    <SidebarMenuItem key={href}>
                      <SidebarMenuButton asChild>
                        <Link
                          href={href}
                          className={`flex items-center gap-3 px-4 py-2 rounded-lg transition ${
                            active
                              ? 'bg-lime-100 text-lime-800 font-medium'
                              : 'hover:bg-lime-50 hover:text-lime-600'
                          }`}
                        >
                          <Icon className="h-5 w-5" />
                          <span>{title}</span>
                        </Link>
                      </SidebarMenuButton>
                    </SidebarMenuItem>
                  );
                })}
              </SidebarMenu>
            </SidebarGroupContent>
          </SidebarGroup>
        </SidebarContent>
      </div>
    </aside>
  );
}