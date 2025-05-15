// components/ui/GenericPagination.tsx
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

export interface GenericPaginationProps {
  /** 0-based */
  currentPage: number;
  totalPages: number;
  /** возвращает URL для страницы */
  hrefFor: (page: number) => string;
}

export default function GenericPagination({
  currentPage,
  totalPages,
  hrefFor,
}: GenericPaginationProps) {
  const pages: (number | 'e')[] = [];
  for (let i = 0; i < totalPages; i++) {
    const near = Math.abs(i - currentPage) <= 1;
    const edge = i === 0 || i === totalPages - 1;
    if (edge || near) {
      pages.push(i);
    } else if (
      (i === 1 && currentPage > 2) ||
      (i === totalPages - 2 && currentPage < totalPages - 3)
    ) {
      pages.push('e');
    }
  }

  return (
    <Pagination>
      <PaginationContent className="ml-auto gap-1">
        <PaginationItem>
          <PaginationPrevious
            href={hrefFor(Math.max(0, currentPage - 1))}
            aria-disabled={currentPage === 0}
            className="border px-3 py-1 text-lime-600 hover:bg-lime-100 aria-disabled:pointer-events-none aria-disabled:opacity-50"
          />
        </PaginationItem>

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
                className={`border px-3 py-1 ${
                  p === currentPage
                    ? 'bg-lime-600 text-white'
                    : 'text-lime-600 hover:bg-lime-100'
                }`}
              >
                {p + 1}
              </PaginationLink>
            </PaginationItem>
          )
        )}

        <PaginationItem>
          <PaginationNext
            href={hrefFor(Math.min(totalPages - 1, currentPage + 1))}
            aria-disabled={currentPage === totalPages - 1}
            className="border px-3 py-1 text-lime-600 hover:bg-lime-100 aria-disabled:pointer-events-none aria-disabled:opacity-50"
          />
        </PaginationItem>
      </PaginationContent>
    </Pagination>
  );
}
