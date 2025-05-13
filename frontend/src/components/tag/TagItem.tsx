// src/components/tag/TagItem.tsx
'use client'

import Link from 'next/link'
import { TagResponse } from '@/lib/api/tags'
import TagHoverCard from './TagHoverCard'

interface TagItemProps {
  tag: TagResponse
}

export default function TagItem ({ tag }: TagItemProps) {
  const rawQ = `[${tag.name}]`

  return (
    <div
      className="
        rounded-lg border border-gray-200 bg-white p-4
        shadow-sm transition-shadow hover:shadow-md
      "
    >
      {/* 👉 Hover-card оборачивает только <Link>, остальная карточка не кликабельна */}
      <TagHoverCard name={tag.name} preloadDescription={tag.description}>
        <Link
          href={{ pathname: '/search', query: { q: rawQ, page: '0', size: '20' } }}
          className="
            inline-block rounded-full bg-lime-50 px-3 py-1
            text-sm font-semibold text-lime-600 hover:bg-lime-100
          "
        >
          {tag.name}
        </Link>
      </TagHoverCard>

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
