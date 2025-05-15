// src/lib/api/tags.ts
import { api } from "./api";

/**
 * Ответ от сервера по одному тегу
 */
export interface TagResponse {
    id: number
    name: string
    description?: string
    questions: number         // ← новое поле
  }

/**
 * Страница результата с пагинацией
 */
export interface PageResult<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

/**
 * Получить список тегов с сервера
 * @param page номер страницы (начинается с 0)
 * @param size размер страницы
 */
export function listTags(
  page: number = 0,
  size: number = 10
): Promise<PageResult<TagResponse>> {
  return api
    .get<PageResult<TagResponse>>('/api/tags/page', {
      params: { page, size },
    })
    .then(res => res.data);
}

/**
 * Получить информацию о теге по его имени
 * @param name имя тега
 */
export function getTag(
  name: string
): Promise<TagResponse> {
  return api
    .get<TagResponse>(`/api/tags/${encodeURIComponent(name)}`)
    .then(res => res.data);
}


const BIG_SIZE = 1000 

export async function listAllTags(): Promise<TagResponse[]> {
    const { content } = await listTags(0, BIG_SIZE)
    return content
  }