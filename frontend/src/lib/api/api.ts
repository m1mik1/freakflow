// src/lib/api/auth.ts
import axios from 'axios'

// создаём экземпляр
export const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL,
})

// интерцептор, который автоматически добавит токен
api.interceptors.request.use((config) => {
  const token = typeof window !== 'undefined' && localStorage.getItem('accessToken')
  if (token && config.headers) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// и единственный catch-интерцептор для ошибок
api.interceptors.response.use(
  (res) => res,
  (err) => {
    // глобальная обработка 401, 403 и т.д.
    return Promise.reject(err)
  }
)

// аутентификация
