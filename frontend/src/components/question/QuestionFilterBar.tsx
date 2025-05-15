'use client'

import { Menubar, MenubarMenu, MenubarTrigger } from '@/components/ui/menubar'
import { usePathname, useRouter, useSearchParams } from 'next/navigation'

type Item = { label: string; sortBy: string; filter?: string }

const items: Item[] = [
  { label: 'Newest', sortBy: 'createdAt' },
  { label: 'Unanswered', sortBy: 'createdAt', filter: 'unanswered' },
  { label: 'Popular', sortBy: 'answersCount' },
]

export function QuestionFilterBar() {
  const router = useRouter()
  const pathname = usePathname()
  const searchParams = useSearchParams()

  const currentSort = searchParams.get('sortBy') ?? 'createdAt'
  const currentFilter = searchParams.get('filter') ?? ''

  const setParams = (it: Item) => {
    const params = new URLSearchParams(searchParams.toString())
    params.set('sortBy', it.sortBy)
    it.filter ? params.set('filter', it.filter) : params.delete('filter')
    router.push(`${pathname}?${params.toString()}`)
  }

  return (
    <Menubar className="inline-flex rounded-lg border border-lime-200 bg-white">
      {items.map(it => {
        const active =
          currentSort === it.sortBy &&
          (it.filter ? currentFilter === it.filter : currentFilter === '')
        return (
          <MenubarMenu key={it.label}>
            <MenubarTrigger
              onClick={() => setParams(it)}
              className={active ? 'bg-lime-100 text-lime-800 font-semibold' : 'hover:bg-gray-50'}
            >
              {it.label}
            </MenubarTrigger>
          </MenubarMenu>
        )
      })}
    </Menubar>
  )
}