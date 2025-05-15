// src/components/login/LoginForm.tsx
'use client';

import { useState } from 'react';
import { useForm } from 'react-hook-form';
import Link from 'next/link';
import { Mail, Lock, Eye, EyeOff } from 'lucide-react';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { loginUser } from '@/lib/api/auth';
import { api } from '@/lib/api/api'  
import { useRouter } from 'next/navigation'


interface LoginFormProps {
  onSuccess: () => void;
}

export default function LoginForm({ onSuccess }: LoginFormProps) {
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting, isValid },
  } = useForm<{ email: string; password: string }>({ mode: 'onChange' });
  const [serverError, setServerError] = useState<string | null>(null);
  const [showPassword, setShowPassword] = useState(false);

  const router = useRouter()

  const onSubmit = async (data: { email: string; password: string }) => {
    setServerError(null);
    try {
      const res = await loginUser(data)
      const token = res.data.accessToken 

      localStorage.setItem('accessToken', token)
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`
      
      onSuccess();
      router.push('/questions')
    } catch (err: any) {
      // Server error handling
      if (err.response) {
        if (err.response.status === 400) {
          setServerError('Invalid email or password');
        } else if (err.response.data?.message) {
          setServerError(err.response.data.message);
        } else {
          setServerError('An error occurred, please try again later');
        }
      } else {
        setServerError(err.message || 'Login failed');
      }
    }
  };
    
  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      className="space-y-6"
    >

      {/* Email */}
      <div className="relative">
        <Mail className="absolute left-3 top-1/2 -translate-y-1/2 text-lime-400" />
        <Input
          {...register('email', { required: 'Email is required' })}
          type="email"
          placeholder="you@example.com"
          className={`pl-10 ${errors.email ? 'border-red-500' : ''}`}
        />
        {errors.email && <p className="mt-1 text-red-500 text-sm">{errors.email.message}</p>}
      </div>

      {/* Password */}
      <div className="relative">
        <Lock className="absolute left-3 top-1/2 -translate-y-1/2 text-lime-400" />
        <Input
          {...register('password', { required: 'Password is required' })}
          type={showPassword ? 'text' : 'password'}
          placeholder="Password"
          className={`pl-10 pr-10 ${errors.password ? 'border-red-500' : ''}`}
        />
        {showPassword ? (
          <EyeOff
            className="absolute right-3 top-1/2 -translate-y-1/2 cursor-pointer text-gray-500"
            onClick={() => setShowPassword(false)}
          />
        ) : (
          <Eye
            className="absolute right-3 top-1/2 -translate-y-1/2 cursor-pointer text-gray-500"
            onClick={() => setShowPassword(true)}
          />
        )}
        {errors.password && <p className="mt-1 text-red-500 text-sm">{errors.password.message}</p>}
      </div>

      {/* Server Error */}
      {serverError && <p className="text-red-500 text-center text-sm">{serverError}</p>}

      {/* Submit */}
      <Button
        type="submit"
        disabled={!isValid || isSubmitting}
        className="w-full bg-lime-400 hover:bg-lime-300 text-white rounded-full py-2 disabled:opacity-50"
      >
        {isSubmitting ? 'Logging in...' : 'Log In'}
      </Button>

      {/* Register Link */}
      <p className="text-center text-sm">
        Don’t have an account?{' '}
        <Link href="/register" className="text-lime-600 font-medium hover:underline">
          Register
        </Link>
      </p>
    </form>
  );
}
