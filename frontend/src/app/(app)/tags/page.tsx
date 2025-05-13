// src/app/(app)/tags/page.tsx
'use client'

import { Search } from 'lucide-react'
import { useRouter, useSearchParams } from 'next/navigation'
import { useEffect, useState } from 'react'

import {
  listTags,
  listAllTags,            // 👈 новый импорт
  PageResult,
  TagResponse,
} from '@/lib/api/tags'
import TagsList from '@/components/tag/TagList'
import TagsPagination from '@/components/tag/TagsPagination'
import { Input } from '@/components/ui/input'

export default function TagsPage() {
  /* ─── URL-state ────────────────────────────────────────────── */
  const params       = useSearchParams()
  const router       = useRouter()
  const currentPage  = Number(params.get('page') ?? '0')     // 0-based
  const size         = 12

  /* ─── data-state ───────────────────────────────────────────── */
  const [data,       setData]   = useState<PageResult<TagResponse> | null>(null)
  const [allTags,    setAll]    = useState<TagResponse[] | null>(null)
  const [filter,     setFilter] = useState('')

  /* ─── когда фильтра нет — обычная пагинация ──────────────── */
  useEffect(() => {
    if (filter) return                                    // пропускаем
    listTags(currentPage, size)
      .then(setData)
      .catch(console.error)
  }, [currentPage, size, filter])

  /* ─── когда фильтр есть — грузим «всё» один раз ──────────── */
  useEffect(() => {
    if (!filter || allTags) return                        // уже есть
    listAllTags()
      .then(setAll)
      .catch(console.error)
  }, [filter, allTags])

  /* ─── фильтрация ─────────────────────────────────────────── */
  const normalized = filter.toLowerCase().trim()

  const visible = normalized
    ? (allTags ?? []).filter(t => t.name.toLowerCase().includes(normalized))
    : data?.content ?? []

  /* ─── helper для href пагинации ──────────────────────────── */
  const hrefFor = (page: number) => {
    const qp = new URLSearchParams(Array.from(params.entries()))
    qp.set('page', String(page))
    return `/tags?${qp.toString()}`
  }

  /* ─── loader ─────────────────────────────────────────────── */
  if (!data && !allTags) {
    return <div className="p-6 text-center">Loading tags…</div>
  }

  /* ─── UI ─────────────────────────────────────────────────── */
  return (
    <div className="space-y-6 p-2">
      <h1 className="text-3xl font-bold text-lime-600">Tags</h1>

      <p className="max-w-2xl text-gray-700">
        A tag is a keyword or label that categorizes your question with other,
        similar questions. Using the right tags makes it easier for others to
        find and answer your question.
      </p>

      {/* поиск */}
      <div className="relative max-w-md">
        <Search className="absolute left-4 top-1/2 -translate-y-1/2 text-lime-600" />
        <Input
          value={filter}
          onChange={e => setFilter(e.target.value)}
          placeholder="Filter by tag name"
          className="rounded-full border border-lime-600 py-2 pl-12 pr-4
                     focus:ring focus:ring-lime-300"
        />
      </div>

      {/* список */}
      {visible.length ? (
        <TagsList tags={visible} />
      ) : (
        <p className="text-center text-gray-500">No tags found.</p>
      )}

      {/* пагинация показываем ТОЛЬКО когда фильтр пуст */}
      {!filter && data?.totalPages! > 1 && (
        <TagsPagination
          currentPage={currentPage}
          totalPages={data!.totalPages}
          hrefFor={hrefFor}
        />
      )}
    </div>
  )
}
