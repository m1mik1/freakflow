// app/questions/page.tsx
'use client'

import React, { useState, useEffect } from 'react'
import { useSearchParams, usePathname, useRouter } from 'next/navigation'
import {
    listQuestions,
    getQuestionsInfo,
    QuestionSummaryResponse,
    PageResult,
    QuestionsInfoResponse,
} from '@/lib/api/questions'
import { QuestionCard } from '@/components/question/QuestionItem'
import { QuestionFilterBar } from '@/components/question/QuestionFilterBar'
import Pagination from '@/components/layout/Pagination'

const filters = [
    { label: 'Newest', sortBy: 'createdAt' },
    { label: 'Unanswered', sortBy: 'createdAt', filter: 'unanswered' },
    { label: 'Popular', sortBy: 'answersCount' },
]

function headingLabel(sortBy: string, filter: string): string {
    if (filter === 'unanswered') return 'Unanswered'
    if (sortBy === 'answersCount') return 'Popular'
    return 'Newest'
}

export default function QuestionsPage() {
    const sp = useSearchParams()
    const pathname = usePathname()
    const router = useRouter()

    // From URL
    const sortBy = sp.get('sortBy') ?? 'createdAt'
    const filter = sp.get('filter') ?? ''

    // Local state
    const [page, setPage] = useState(0)
    const [data, setData] = useState<PageResult<QuestionSummaryResponse> | null>(null)
    const [info, setInfo] = useState<QuestionsInfoResponse | null>(null)
    const [loading, setLoading] = useState(false)

    // Sync page with ?page=
    useEffect(() => {
        const qPage = parseInt(sp.get('page') || '0', 10)
        setPage(isNaN(qPage) ? 0 : qPage)
    }, [sp])

    // Fetch page of questions on sortBy, filter or page change
    useEffect(() => {
        setLoading(true)
        listQuestions(page, 10, sortBy, filter)
            .then(res => setData(res))
            .catch(console.error)
            .finally(() => setLoading(false))
    }, [page, sortBy, filter])

    // Fetch overall questions info once
    useEffect(() => {
        getQuestionsInfo()
            .then(setInfo)
            .catch(console.error)
    }, [])

    // Page change handler
    function onChangePage(newPage: number) {
        const params = new URLSearchParams(sp.toString())
        params.set('page', String(newPage))
        router.push(`${pathname}?${params.toString()}`)
    }

    return (
        <div className="p-2 space-y-6">
            {/* Heading */}
            <h1 className="text-3xl font-bold text-lime-600">
                {headingLabel(sortBy, filter)} questions
            </h1>

            {/* Global counts & filter */}
            {/* Global counts & filter */}
            <div className="flex items-center justify-between mb-6">
                <span className="text-lg text-gray-700">
                    {info ? (
                        filter === 'unanswered'
                            // When “Unanswered” filter is active, only show unanswered count
                            ? `${info.unansweredCount.toLocaleString('uk-UA')} unanswered`
                            // Otherwise (Newest or Popular), show total questions
                            : `${info.questionsCount.toLocaleString('uk-UA')} questions`
                    ) : (
                        'Loading…'
                    )}
                </span>
                <QuestionFilterBar />
            </div>


            {/* List */}
            {loading && !data ? (
                <div className="space-y-4">
                    {Array.from({ length: 5 }).map((_, i) => (
                        <div key={i} className="h-24 w-full rounded-xl bg-gray-100 animate-pulse" />
                    ))}
                </div>
            ) : (
                <div className="space-y-4">
                    {data?.content.map(q => (
                        <QuestionCard key={q.id} {...q} />
                    ))}
                </div>
            )}

            {/* Pagination */}
            {data && data.totalPages > 1 && (
                <Pagination
                    currentPage={page}
                    totalPages={data.totalPages}
                    hrefFor={p => {
                        const params = new URLSearchParams(sp.toString())
                        params.set('page', String(p))
                        return `${pathname}?${params.toString()}`
                    }}
                />
            )}
        </div>
    )
}
