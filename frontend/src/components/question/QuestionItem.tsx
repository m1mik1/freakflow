// components/question/QuestionCard.tsx
'use client'

import Link from 'next/link'
import { formatTimeAgo } from '@/lib/formatTimeAgo'
import { TagPill } from '@/components/tag/TagPill'
import type { QuestionSummaryResponse } from '@/lib/api/questions'

export function QuestionCard(props: QuestionSummaryResponse) {
    const {
        slug,
        title,
        body,
        tags,
        answersCount,
        votesCount,
        createdAt,
        author,
    } = props

    return (
        <div className="
               border-b border-lime-200
      first:border-t first:border-lime-200
 last:border-b-0
      px-6 py-4">
            <div className="flex items-start gap-6">
                {/* Stats */}
                <div className="w-20 flex-shrink-0 text-right text-sm text-gray-500 space-y-1">
                    <div className='mt-0.5'>
                        <span className="text-gray-500">{votesCount}</span>
                        <span className="ml-1">votes</span>
                    </div>
                    <div className='mt-3'>
                        <span className="text-gray-500 ">{answersCount}</span>
                        <span className="ml-1">answers</span>
                    </div>
                </div>

                {/* Main content */}
                <div className="flex-1">
                    {/* Title: no underline strip, just color change + pointer */}
                    <Link
                        href={`/questions/${slug}`}
                        className="
              text-lg font-semibold text-lime-600
              cursor-pointer
              hover:text-lime-800
              transition-colors
            "
                    >
                        {title}
                    </Link>

                    {/* Excerpt */}
                    <p
                        className="text-sm text-gray-600 mt-1 overflow-hidden"
                        style={{
                            display: '-webkit-box',
                            WebkitLineClamp: 2,
                            WebkitBoxOrient: 'vertical',
                        }}
                    >
                        {body || 'No description yet…'}
                    </p>

                    {/* Tags & time */}
                    <div className="mt-3 flex items-center justify-between">
                        <div className="flex flex-wrap gap-2">
                            {tags.map(tag => (
                                <TagPill key={tag} name={tag} />
                            ))}
                        </div>
                        {/* Вариант: "asked by alice 5 minutes ago" */}
                        <div className="text-sm">
                            <span className="text-sm text-gray-500 whitespace-nowrap">
                                asked by{' '}
                                <Link
                                    href={`/users/${author}`}
                                    className="font-medium text-lime-600 hover:text-lime-800"
                                >
                                    {author}
                                </Link>{' '}
                                • {formatTimeAgo(createdAt)}
                            </span>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    )
}
