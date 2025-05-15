// src/lib/api/questions.ts
import { api } from './api'  // ваш axios-инстанс

/** Минимальная структура одной карточки-вопроса */
export interface QuestionSummaryResponse {
  id: number
  title: string
  slug: string
  body: string
  answersCount: number
  votesCount: number
  createdAt: string
  tags: string[]
  author: string
}

/** Доп. данные по всем вопросам */
export interface QuestionsInfoResponse {
  questionsCount: number      // общее число вопросов
  unansweredCount: number     // число без ответа
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

export interface PageResult<T> {
  content: T[]
  page: number
  size: number
  totalElements: number
  totalPages: number
}

/**
 * Вызывает GET /api/questions/search?q=...&page=...&size=...
 */
export function searchQuestions(
  q: string,
  page = 0,
  size = 20
): Promise<QuestionSearchResponse> {
  // axios сам закодирует параметры, encodeURIComponent здесь не нужен
  return api
    .get<QuestionSearchResponse>('/api/questions/search', {
      params: { q, page, size },
    })
    .then(res => res.data)
}

/**
 * Вызывает GET /api/questions?page=...&size=...&sortBy=...&filter=...
 */
export function listQuestions(
  page: number = 0,
  size: number = 10,
  sortBy?: string,
  filter?: string
): Promise<PageResult<QuestionSummaryResponse>> {
  return api
    .get<PageResult<QuestionSummaryResponse>>('/api/questions', {
      params: { page, size, sortBy, filter },
    })
    .then(res => res.data)
}

/**
 * Новый: GET /api/questions/info
 */
export function getQuestionsInfo(): Promise<QuestionsInfoResponse> {
  return api
    .get<QuestionsInfoResponse>('/api/questions/info')
    .then(res => res.data)
}
