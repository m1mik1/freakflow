// src/app/(app)/search/page.tsx
'use client'

import { useSearchParams, useRouter, usePathname } from 'next/navigation'
import { useEffect, useState } from 'react'
import Pagination from '@/components/layout/Pagination'
import { TagPill } from '@/components/tag/TagPill'
import { QuestionFilterBar } from '@/components/question/QuestionFilterBar'
import { QuestionCard } from '@/components/question/QuestionItem'
import { searchQuestions, QuestionSearchResponse } from '@/lib/api/questions'

export default function SearchPage() {
  const params   = useSearchParams()
  const pathname = usePathname()
  const router   = useRouter()

  const q    = params.get('q') ?? ''
  const page = Number(params.get('page') ?? '0')
  const size = 20

  // detect [tag] anywhere
  const tagRegex = /\[([^\]]+)\]/
  const match    = q.match(tagRegex)
  const hasTag   = Boolean(match)
  const tagName  = match?.[1] ?? ''
  const restText = q.replace(tagRegex, '').trim()
  const isPureTagSearch = hasTag && restText === ''

  const [data,    setData]    = useState<QuestionSearchResponse | null>(null)
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    setLoading(true)
    searchQuestions(q, page, size)
      .then(res => setData(res))
      .catch(console.error)
      .finally(() => setLoading(false))
  }, [q, page])

  const hrefFor = (p: number) => {
    const sp = new URLSearchParams(Array.from(params.entries()))
    sp.set('q', q)
    sp.set('page', String(p))
    return `${pathname}?${sp.toString()}`
  }

  if (loading) return <div className="p-6 text-center">Loading…</div>
  if (!data)    return <div className="p-6 text-center">No data</div>

  return (
    <div className="p-2 space-y-6">
      {/* Tag banner */}
      {data.tagBanner && (
        <div className=" ">
          <h1 className="text-3xl font-bold text-lime-600 mb-2">
            [{data.tagBanner.name}]
          </h1>
          {data.tagBanner.description && (
            <p className="text-base text-gray-800 leading-relaxed">
              {data.tagBanner.description}
            </p>
          )}
        </div>
      )}

      {/* Header only when not pure tag search */}
      {!isPureTagSearch && (
        <div className="flex flex-col md:flex-row md:items-center justify-between">
          <div>
            <h1 className="text-3xl font-bold text-lime-600 mb-4">
              Search Results
            </h1>
            <p className="mt-1 text-gray-700">
              Results for {restText || 'all'}
              {hasTag && (
                <> tagged with <TagPill name={tagName} page="0" size="20" /></>
              )}
            </p>
            <p className="text-gray-600">Search options not deleted</p>
          </div>
        </div>
      )}

      {/* Count & filter */}
      <div className="flex flex-col lg:flex-row lg:items-center justify-between gap-4">
        <p className="text-xl font-medium text-gray-900">
          {data.questions.totalElements.toLocaleString()} results
        </p>
        <QuestionFilterBar />
      </div>

      {/* Question list */}
      <div className="space-y-4">
        {data.questions.content.map(item => (
          <QuestionCard key={item.id} {...item} />
        ))}
      </div>

      {/* Pagination */}
      {data.questions.totalPages > 1 && (
        <Pagination currentPage={page} totalPages={data.questions.totalPages} hrefFor={hrefFor} />
      )}
    </div>
  )
}
