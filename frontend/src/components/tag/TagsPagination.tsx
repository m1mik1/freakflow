'use client';

import {
  Pagination,
  PaginationContent,
  PaginationEllipsis,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
} from '@/components/ui/pagination';

interface TagsPaginationProps {
  /** 0-based index, который приходит с бэкенда */
  currentPage: number;
  totalPages: number;
  /** навигация вперёд/назад и по номеру страницы */
  hrefFor: (page: number) => string;
}

export default function TagsPagination({
  currentPage,
  totalPages,
  hrefFor,
}: TagsPaginationProps) {
  /* формируем массив номеров + 'ellipsis' */
  const pages: (number | 'e')[] = [];
  for (let i = 0; i < totalPages; i++) {
    const near = Math.abs(i - currentPage) <= 1;        // текущая ±1
    const edge = i === 0 || i === totalPages - 1;       // крайние
    if (edge || near) pages.push(i);
    else if (
      (i === 1 && currentPage > 2) ||
      (i === totalPages - 2 && currentPage < totalPages - 3)
    ) pages.push('e');
  }

  return (
    <Pagination>
      <PaginationContent className="ml-auto  gap-1">
        {/* Prev */}
        <PaginationItem>
          <PaginationPrevious
            href={hrefFor(Math.max(0, currentPage - 1))}
            aria-disabled={currentPage === 0}
            className="border px-3 py-1 text-lime-600 hover:bg-lime-100
                       aria-disabled:pointer-events-none aria-disabled:opacity-50"
          />
        </PaginationItem>

        {/* numbers / ellipsis */}
        {pages.map((p, idx) =>
          p === 'e' ? (
            <PaginationItem key={`e-${idx}`}>
              <PaginationEllipsis />
            </PaginationItem>
          ) : (
            <PaginationItem key={p}>
              <PaginationLink
                href={hrefFor(p)}
                isActive={p === currentPage}
                aria-current={p === currentPage ? 'page' : undefined}
                className={`
                  border px-3 py-1
                  ${p === currentPage
                    ? 'bg-lime-600 text-white'
                    : 'text-lime-600 hover:bg-lime-100'}
                `}
              >
                {p + 1}
              </PaginationLink>
            </PaginationItem>
          )
        )}

        {/* Next */}
        <PaginationItem>
          <PaginationNext
            href={hrefFor(Math.min(totalPages - 1, currentPage + 1))}
            aria-disabled={currentPage === totalPages - 1}
            className="border px-3 py-1 text-lime-600 hover:bg-lime-100
                       aria-disabled:pointer-events-none aria-disabled:opacity-50"
          />
        </PaginationItem>
      </PaginationContent>
    </Pagination>
  );
}
