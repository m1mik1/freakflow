// components/search/SearchHelper.tsx
'use client'

import { useRef, useEffect } from 'react'
import { Button } from '@/components/ui/button'

const HELPERS = [
  { label: '[tags]', description: 'search within a tag' },
  { label: 'user:12345', description: 'search by author' },
  { label: 'answers:0', description: 'unanswered questions' },
  { label: 'isaccepted:yes', description: 'only accepted answers' },
]

export function SearchHelper({
  open,
  onClose,
}: {
  open: boolean
  onClose: () => void
}) {
  const ref = useRef<HTMLDivElement>(null)

  // close on outside click
  useEffect(() => {
    function onClick(e: MouseEvent) {
      if (ref.current && !ref.current.contains(e.target as Node)) {
        onClose()
      }
    }
    if (open) document.addEventListener('mousedown', onClick)
    return () => document.removeEventListener('mousedown', onClick)
  }, [open, onClose])

  if (!open) return null

  return (
    <div
      ref={ref}
      className="absolute z-20 mt-1 w-full bg-white border border-gray-200 rounded-lg shadow-lg"
    >
      {/* 2 columns of static helper items */}
      <div className="grid grid-cols-2 gap-1 p-2">
        {HELPERS.map((h, i) => (
          <div key={i} className="p-2 rounded">
            <div className="font-mono text-sm">{h.label}</div>
            <div className="text-gray-500 text-xs">{h.description}</div>
          </div>
        ))}
      </div>

      {/* footer: left-aligned Add question button */}
      <div className="border-t border-gray-100 p-4 text-left">
        <Button
          className="bg-lime-500 hover:bg-lime-600 text-white"
          onClick={() => (window.location.href = '/questions/ask')}
        >
          Add question
        </Button>
      </div>
    </div>
  )
}
