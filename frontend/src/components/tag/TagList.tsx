'use client';

import React from 'react';
import TagItem from './TagItem';
import { TagResponse } from '@/lib/api/tags';

interface TagsListProps {
  tags: TagResponse[];
}

export default function TagsList({ tags }: TagsListProps) {
  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
      {tags.map(tag => (
        <TagItem key={tag.id} tag={tag} />
      ))}
    </div>
  );
}