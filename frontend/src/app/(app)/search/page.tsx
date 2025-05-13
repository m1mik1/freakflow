// src/app/(app)/search/page.tsx
'use client'

import { useSearchParams, useRouter } from 'next/navigation'
import { useEffect, useState } from 'react'
import { searchQuestions, QuestionSearchResponse } from '@/lib/api/questions'
import Pagination from '@/components/Pagination'

export default function SearchPage() {
  const params = useSearchParams()
  const router = useRouter()

  // читаем ?q=... и ?page=...
  const q = params.get('q') ?? ''
  const page = Number(params.get('page') ?? '0')
  const size = 20

  const [data, setData] = useState<QuestionSearchResponse | null>(null)
  const [loading, setLoading] = useState(false)

  // при изменении q или page — делаем запрос
  useEffect(() => {
    setLoading(true)
    searchQuestions(q, page, size)
      .then((d) => setData(d))
      .catch((err) => console.error(err))
      .finally(() => setLoading(false))
  }, [q, page])

  const onPageChange = (newPage: number) => {
    // меняем query-параметр «page»
    router.push(`/search?q=${encodeURIComponent(q)}&page=${newPage}`)
  }

  if (loading) {
    return <div className="p-6 text-center">Loading…</div>
  }
  if (!data) {
    return <div className="p-6 text-center">No data</div>
  }

  return (
    <div className="p-6 space-y-4">
      <h1 className="text-2xl font-bold">
        {q ? `Search results for «${q}»` : 'All questions'}
      </h1>

      {/* Опциональный баннер по единственному тегу */}
      {data.tagBanner && (
        <div className="p-4 bg-lime-50 border-l-4 border-lime-400 rounded">
          <h2 className="font-semibold text-lime-600">
            Тег: {data.tagBanner.name}
          </h2>
          <p className="text-gray-700">{data.tagBanner.description}</p>
        </div>
      )}

      {/* Список превью вопросов */}
      <ul className="space-y-4">
        {data.questions.content.map((q) => (
          <li key={q.id} className="p-4 border rounded hover:shadow">
            <a href={`/questions/${q.id}/${q.slug}`} className="text-lg font-semibold text-blue-600 hover:underline">
              {q.title}
            </a>
            <div className="mt-1 text-sm text-gray-500">
              {q.answersCount} answer(s) • {new Date(q.createdAt).toLocaleDateString()}
            </div>
            <div className="mt-2 flex flex-wrap gap-2">
              {q.tags.map((t) => (
                <span key={t} className="px-2 py-0.5 bg-gray-100 rounded text-xs">
                  {t}
                </span>
              ))}
            </div>
          </li>
        ))}
      </ul>

      {/* Пагинация */}
      <Pagination
        page={data.questions.page}
        totalPages={data.questions.totalPages}
        onChange={onPageChange}
      />
    </div>
  )
}
