// src/components/register/RegisterForm.tsx
'use client'

import { useState } from 'react'
import { useRouter } from 'next/navigation'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { motion, AnimatePresence } from 'framer-motion'
import { User, Mail, Lock, HelpCircle, MessageCircle, ShieldCheck, Users } from 'lucide-react'

import { registerSchema, RegisterFormValues } from '@/lib/formSchemas'
import { registerUser, verifyCode } from '@/lib/api'

import { Form, FormField, FormItem, FormLabel, FormControl, FormMessage } from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { InputOTP, InputOTPGroup, InputOTPSeparator, InputOTPSlot } from '@/components/ui/input-otp'

export default function RegisterForm() {
  const router = useRouter()
  const [step, setStep] = useState<'form' | 'verify'>('form')
  const [email, setEmail] = useState('')
  const [otp, setOtp] = useState('')
  const [error, setError] = useState<string | null>(null)

  const form = useForm<RegisterFormValues>({
    resolver: zodResolver(registerSchema),
    defaultValues: { name: '', email: '', password: '', confirmPassword: '' },
    mode: 'onBlur',
    reValidateMode: 'onChange',
  })
  const { handleSubmit, formState } = form

  async function onSubmit(values: RegisterFormValues) {
    try {
      await registerUser(values)
      setEmail(values.email)
      setStep('verify')
      setError(null)
    } catch (err: any) {
      setError(err.response?.data?.message || 'Помилка реєстрації')
    }
  }

  async function onVerify() {
    try {
      await verifyCode({ email, code: otp })
      router.push('/')
    } catch (err: any) {
      setError(err.response?.data?.message || 'Невірний код')
    }
  }

  const formView = (
    <div className="flex flex-col md:flex-row w-11/12 max-w-4xl mx-auto mt-10 bg-white rounded-xl shadow-md overflow-hidden">
      <div className="md:w-1/2 p-6 bg-gradient-to-b from-lime-300 to-lime-400 text-white flex flex-col justify-center">
        <img src="/learning.svg" alt="Illustration" className="mb-6 w-full object-contain rounded" />
      </div>
      <div className="md:w-1/2 p-6">
        <div className="flex justify-center mb-6 space-x-2">
          <span className={`w-3 h-3 rounded-full ${step==='form'?'bg-lime-400':'bg-gray-300'}`}/>
          <span className={`w-3 h-3 rounded-full ${step==='verify'?'bg-lime-400':'bg-gray-300'}`}/>
        </div>
        <h2 className="text-3xl font-bold text-gray-800 text-center mb-4">Реєстрація в FreakFlow</h2>
        <Form {...form}>
          <form noValidate onSubmit={handleSubmit(onSubmit)} className="space-y-5">
            {/* Name */}
            <FormField name="name" render={({ field }) => (
              <FormItem>
                <FormLabel className="text-gray-700">Ім'я</FormLabel>
                <FormControl>
                  <div className="relative">
                    <User className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" size={16}/>
                    <Input {...field} placeholder="Ваше ім'я" className="pl-10 focus:ring-2 focus:ring-lime-300"/>
                  </div>
                </FormControl>
                <FormMessage/>
              </FormItem>
            )}/>
            {/* Email */}
            <FormField name="email" render={({ field }) => (
              <FormItem>
                <FormLabel className="text-gray-700">Email</FormLabel>
                <FormControl>
                  <div className="relative">
                    <Mail className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" size={16}/>
                    <Input {...field} type="email" placeholder="you@example.com" className="pl-10 focus:ring-2 focus:ring-lime-300"/>
                  </div>
                </FormControl>
                <FormMessage/>
              </FormItem>
            )}/>
            {/* Password */}
            <FormField name="password" render={({ field }) => (
              <FormItem>
                <FormLabel className="text-gray-700">Пароль <span className="ml-1 relative text-gray-400"><HelpCircle size={14}/><span className="absolute top-full left-1/2 -translate-x-1/2 mt-1 w-40 p-2 text-xs text-white bg-black rounded opacity-0 hover:opacity-100 transition-opacity">Мінімум 8 символів: велика/мала літера, цифра, спецсимвол</span></span></FormLabel>
                <FormControl>
                  <div className="relative">
                    <Lock className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" size={16}/>
                    <Input {...field} type="password" placeholder="Пароль" className="pl-10 focus:ring-2 focus:ring-lime-300"/>
                  </div>
                </FormControl>
                <FormMessage/>
              </FormItem>
            )}/>
            {/* Confirm */}
            <FormField name="confirmPassword" render={({ field }) => (
              <FormItem>
                <FormLabel className="text-gray-700">Підтвердіть пароль</FormLabel>
                <FormControl>
                  <div className="relative">
                    <Lock className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" size={16}/>
                    <Input {...field} type="password" placeholder="Підтвердіть пароль" className="pl-10 focus:ring-2 focus:ring-lime-300"/>
                  </div>
                </FormControl>
                <FormMessage/>
              </FormItem>
            )}/>
            {error && <p className="text-sm text-red-600 text-center">{error}</p>}
            <Button type="submit" disabled={formState.isSubmitting} className="w-full py-2 text-white font-semibold rounded-lg bg-gradient-to-r from-lime-300 to-lime-400 hover:opacity-90">{formState.isSubmitting?'Завантаження…':'Зареєструватися'}</Button>
          </form>
        </Form>
      </div>
    </div>
  )

  // OTP screen full width
  const verifyView = (
    <div className="w-11/12 max-w-md mx-auto mt-10 p-6 bg-white rounded-xl shadow-md">
      <h2 className="text-3xl font-bold text-gray-800 text-center mb-4">Підтвердження коду</h2>
      <p className="text-center text-gray-600 mb-4">Введіть код з листа на {email}</p>
      <div className="flex justify-center mb-4">
        <InputOTP maxLength={6} value={otp} onChange={setOtp} containerClassName="space-x-2">
          <InputOTPGroup>
            <InputOTPSlot index={0}/>
            <InputOTPSlot index={1}/>
            <InputOTPSlot index={2}/>
          </InputOTPGroup>
          <InputOTPSeparator/>
          <InputOTPGroup>
            <InputOTPSlot index={3}/>
            <InputOTPSlot index={4}/>
            <InputOTPSlot index={5}/>
          </InputOTPGroup>
        </InputOTP>
      </div>
      {error && <p className="text-sm text-red-600 text-center mb-4">{error}</p>}
      <Button onClick={onVerify} className="w-full py-2 text-white font-semibold rounded-lg bg-gradient-to-r from-lime-300 to-lime-400 hover:opacity-90">Підтвердити</Button>
    </div>
  )

  return step === 'form' ? formView : verifyView
}
