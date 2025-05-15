'use client';

import Link from 'next/link';
import TagHoverCard from '@/components/tag/TagHoverCard';

interface TagPillProps {
  name: string;
  description?: string;
  /** defaults to '0' */
  page?: string;
  /** defaults to '20' */
  size?: string;
}

export function TagPill({ name, description = '', page = '0', size = '20' }: TagPillProps) {
    const rawQuery = `[${name}]`;


  return (
    <TagHoverCard name={name} preloadDescription={description}>
      <Link
        href={{
          pathname: '/search',
          query: { q: rawQuery, page, size },
        }}
        className="
          inline-block rounded-full bg-lime-50 px-3 py-1
          text-sm font-semibold text-lime-600 hover:bg-lime-100
        "
      >
        {name}
      </Link>
    </TagHoverCard>
  );
}
