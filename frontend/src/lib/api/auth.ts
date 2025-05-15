import type { RegisterFormValues } from '../formSchemas'
import { api } from './api'

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
  