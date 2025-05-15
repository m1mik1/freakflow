'use client'

import { useEffect, useState } from 'react'
import { QuestionCard } from './QuestionItem'
import { Skeleton } from '@/components/ui/skeleton'
import {
  QuestionSummaryResponse,
  listQuestions,
} from '@/lib/api/questions'
import { useSearchParams, useRouter } from 'next/navigation'

export function QuestionList() {
  const searchParams = useSearchParams()
  const page = 0
  const size = 10
  const sortBy = searchParams.get('sortBy') || undefined
  const filter = searchParams.get('filter') || undefined

  const [questions, setQuestions] = useState<QuestionSummaryResponse[] | null>(null)

  useEffect(() => {
    listQuestions(page, size, sortBy, filter)
      .then(res => setQuestions(res.content))
      .catch(err => {
        console.error('Не удалось загрузить вопросы', err)
        setQuestions([])
      })
  }, [sortBy, filter])

  if (!questions) {
    return (
      <div className="space-y-4">
        {Array.from({ length: 5 }).map((_, i) => (
          <Skeleton key={i} className="h-24 w-full rounded-xl" />
        ))}
      </div>
    )
  }

  return (
    <div className="space-y-4">
      {questions.map(q => (
        <QuestionCard key={q.id} {...q} />
      ))}
    </div>
  )
}