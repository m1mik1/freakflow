// src/components/tag/TagItem.tsx
'use client'

import { TagResponse } from '@/lib/api/tags'
import { TagPill } from './TagPill'

interface TagItemProps {
  tag: TagResponse
}

export default function TagItem ({ tag }: TagItemProps) {
  return (
    <div
      className="
        rounded-lg border border-gray-200 bg-white p-4
        shadow-sm transition-shadow hover:shadow-md
      "
    >
      {/* Tag pill with hover-card */}
      <TagPill 
        name={tag.name} 
        description={tag.description} 
        page="0" 
        size="20" 
      />

      {/* Описание в списке (обрезаем до 3 строк) */}
      {tag.description && (
        <p
          className="
            mt-3 text-sm leading-relaxed text-gray-700 line-clamp-3"
          title={tag.description}
        >
          {tag.description}
        </p>
      )}

      {/* счётчик вопросов */}
      <span className="mt-3 inline-block text-xs text-gray-500">
        {tag.questions} question{tag.questions !== 1 && 's'}
      </span>
    </div>
  )
}
