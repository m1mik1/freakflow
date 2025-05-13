// src/providers/ClientAuthProvider.tsx
'use client'

import { useEffect } from 'react'
import { api } from '@/lib/api/auth'
import { useRouter } from 'next/navigation'

export default function ClientAuthProvider({ children }: { children: React.ReactNode }) {
  const router = useRouter()

  // при монтировании — восстанавливаем токен
  useEffect(() => {
    const token = localStorage.getItem('accessToken')
    if (token) {
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`
    }
  }, [])

  // интерсептор для 401
  useEffect(() => {
    const id = api.interceptors.response.use(
      res => res,
      err => {
        if (err.response?.status === 401) {
          localStorage.removeItem('accessToken')
          delete api.defaults.headers.common['Authorization']
          // редиректим на логин-модалку
          window.location.href = '/?login=true'
        }
        return Promise.reject(err)
      }
    )
    return () => api.interceptors.response.eject(id)
  }, [])

  return <>{children}</>
}
