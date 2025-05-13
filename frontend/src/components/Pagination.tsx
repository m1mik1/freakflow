'use client';

import React from 'react';

interface PaginationProps {
  page: number;
  totalPages: number;
  onChange: (newPage: number) => void;
}

export default function Pagination({ page, totalPages, onChange }: PaginationProps) {
  return (
    <div className="flex items-center justify-center space-x-2 mt-6">
      <button
        onClick={() => onChange(page - 1)}
        disabled={page === 0}
        className="px-4 py-2 border rounded disabled:opacity-50"
      >
        Previous
      </button>
      <span>
        Page {page + 1} of {totalPages}
      </span>
      <button
        onClick={() => onChange(page + 1)}
        disabled={page + 1 >= totalPages}
        className="px-4 py-2 border rounded disabled:opacity-50"
      >
        Next
      </button>
    </div>
  );
}