// src/lib/api/auth.ts
import axios from 'axios'
import type { RegisterFormValues } from '../formSchemas'

export interface AuthResponse {
  accessToken: string
  refreshToken?: string
}

export interface VerifyResponse {
  success: boolean
}

export interface ResendResponse {
  success: boolean
}

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
export function registerUser(data: RegisterFormValues) {
  return api.post<AuthResponse>('/api/auth/register', data)
}

export function verifyCode(data: { email: string; code: string }) {
  return api.post<VerifyResponse>('/api/auth/verify', data)
}

export function resendCode(data: { email: string }) {
  return api.post<ResendResponse>('/api/auth/verify/resend', data)
}

export function loginUser(data: { email: string; password: string }) {
  return api.post<AuthResponse>('/api/auth/login', data);
}
