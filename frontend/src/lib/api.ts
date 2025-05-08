// src/lib/api.ts
import axios from 'axios'
import type { RegisterFormValues } from './formSchemas'

export const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL, // в .env.local: NEXT_PUBLIC_API_URL=http://localhost:8080
})

export function registerUser(data: RegisterFormValues) {
  return api.post('/api/auth/register', data)
}

export function verifyCode(data: { email: string; code: string }) {
  return api.post('/api/auth/verify', data)
}
