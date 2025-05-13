// src/lib/api/questions.ts
import { api } from './auth'  // ваш axios-инстанс

/** Минимальная структура одной карточки-вопроса */
export interface QuestionSummaryResponse {
  id: number
  title: string
  slug: string
  answersCount: number
  createdAt: string
  tags: string[]
}

/** Ответ вашего searchCombined(...) на фронт */
export interface QuestionSearchResponse {
  tagBanner?: {
    id: number
    name: string
    description?: string
  }
  questions: {
    content: QuestionSummaryResponse[]
    page: number
    size: number
    totalElements: number
    totalPages: number
  }
}

/**
 * Вызывает GET /api/questions/search?q=...&page=...&size=...
 */


export function searchQuestions(
  q: string,
  page = 0,
  size = 20
): Promise<QuestionSearchResponse> {
  // *ручная* кодировка перед тем, как отдать axios
  const encodedQ = encodeURIComponent(q);
  return api
    .get<QuestionSearchResponse>('/api/questions/search', {
      params: { q: encodedQ, page, size },
    })
    .then(res => res.data);
}
