// src/components/tag/TagHoverCard.tsx
'use client'

import { PropsWithChildren, useCallback, useState } from 'react'
import {
  HoverCard,
  HoverCardTrigger,
  HoverCardContent,
} from '@/components/ui/hover-card'
import { Skeleton } from '@/components/ui/skeleton'
import { getTag, TagResponse } from '@/lib/api/tags'

export interface TagHoverCardProps {
  name: string
  preloadDescription?: string
}

export default function TagHoverCard ({
  name,
  preloadDescription,
  children,
}: PropsWithChildren<TagHoverCardProps>) {
  const [data, setData]   = useState<TagResponse | null>(null)
  const [loading, setLoad] = useState(false)

  /** загружаем данные только при первом наведении */
  const fetchTag = useCallback(async () => {
    if (data || loading) return
    try {
      setLoad(true)
      setData(await getTag(name))
    } finally {
      setLoad(false)
    }
  }, [data, loading, name])

  const desc      = data?.description ?? preloadDescription
  const questions = data?.questions

  return (
    <HoverCard openDelay={120} closeDelay={80}>
      {/* trigger */}
      <HoverCardTrigger asChild onMouseEnter={fetchTag}>
        {children}
      </HoverCardTrigger>

      {/* pop-up */}
      <HoverCardContent side="top" className="w-80 rounded-xl border p-5 shadow-xl">
        {loading && !data && <Skeleton className="h-24 w-full rounded-lg" />}

        {(data || desc) && (
          <>
            {/* название тега — крупным текстом */}
            <h3 className="text-2xl font-semibold text-lime-600">{name}</h3>

            {desc && (
              <p className="mt-3 text-sm leading-relaxed text-gray-700">
                {desc}
              </p>
            )}

            {questions !== undefined && (
              <div className="mt-4 flex items-center gap-2 text-xs text-gray-500">
                <span>{questions} question{questions !== 1 && 's'} tagged</span>
                <span className="h-px flex-1 bg-gray-200" />
              </div>
            )}
          </>
        )}
      </HoverCardContent>
    </HoverCard>
  )
}
