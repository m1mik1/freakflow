'use client'

import { useState, useEffect, useMemo } from 'react'
import { useRouter } from 'next/navigation'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { motion } from 'framer-motion'
import { User, Mail, Lock, Eye, EyeOff, Info } from 'lucide-react'

import { registerSchema, RegisterFormValues } from '@/lib/formSchemas'
import { registerUser, verifyCode, resendCode } from '@/lib/api/auth'

import {
  Form,
  FormField,
  FormItem,
  FormLabel,
  FormControl,
  FormMessage,
} from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import {
  InputOTP,
  InputOTPGroup,
  InputOTPSeparator,
  InputOTPSlot,
} from '@/components/ui/input-otp'
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from '@/components/ui/dialog'
import { toast } from "sonner"



import { cn } from '@/lib/utils'
import { Progress } from '../../ui/progress'

export default function RegisterForm() {
  const router = useRouter()

  /* Local state */
  const [verifyOpen, setVerifyOpen] = useState(false)
  const [email, setEmail] = useState('')
  const [otp, setOtp] = useState('')
  const [error, setError] = useState<string | null>(null)
  const [cooldown, setCooldown] = useState(0)
  const [regCooldown, setRegCooldown] = useState(0)
  const [pending, setPending] = useState(false)
  const [showPwd, setShowPwd] = useState(false)
  const [showConfirm, setShowConfirm] = useState(false)
  const [resendLoading, setResendLoading] = useState(false)

  /* Tickers */
  useEffect(() => {
    if (!cooldown) return
    const id = setInterval(() => setCooldown(c => c - 1), 1000)
    return () => clearInterval(id)
  }, [cooldown])
  useEffect(() => {
    if (!regCooldown) return
    const id = setInterval(() => setRegCooldown(c => c - 1), 1000)
    return () => clearInterval(id)
  }, [regCooldown])
  useEffect(() => {
    if (regCooldown === 0 && !verifyOpen) setPending(false)
  }, [regCooldown, verifyOpen])

  /* React-hook-form */
  const form = useForm<RegisterFormValues>({
    resolver: zodResolver(registerSchema),
    defaultValues: { name: '', email: '', password: '', confirmPassword: '' },
    mode: 'onChange',              // пункт 5: валидация в реальном времени
    reValidateMode: 'onChange',
  })
  const { handleSubmit, watch, formState, setError: setFormError, reset } = form

  /* Password strength */
  const pwd = watch('password')
  const strength = useMemo(() => {
    const rules = [
      pwd.length >= 8,
      /[a-z]/.test(pwd) && /[A-Z]/.test(pwd),
      /\d/.test(pwd),
      /[^A-Za-z0-9]/.test(pwd),
    ]
    return rules.filter(Boolean).length
  }, [pwd])
  const barColor = ['bg-red-400', 'bg-orange-300', 'bg-lime-300', 'bg-lime-500'][Math.max(strength - 1, 0)]

  /* Handlers */
  const onSubmit = async (values: RegisterFormValues) => {
    try {
      await registerUser(values)
      setEmail(values.email)
      setVerifyOpen(true)
      setCooldown(60)
      setPending(true)
    } catch (err: any) {
      const status = err.response?.status
      const msg = err.response?.data?.error || err.response?.data?.message || 'Помилка'
      if ((status === 409 || status === 400) && msg.includes('Pending') && !pending) {
        setVerifyOpen(true); setCooldown(60); setPending(true)
        return
      }
      if (msg === 'Email already exists') {
        setFormError('email', { message: 'Електронну пошту вже використано' })
      } else if (msg === 'Name already exists') {
        setFormError('name', { message: 'Ім’я вже зайнято' })
      } else {

        setFormError('root', { message: 'Сталася помилка: ' + msg })
      }
    }
  }

  const onVerify = async () => {
    try {
      await verifyCode({ email, code: otp })
      toast.success('Акаунт підтверджено!')
      setTimeout(() => router.push('/?login=true'), 1200)
      reset()
    } catch (err: any) {
      setError(err.response?.data?.error ?? 'Невірний код')
    }
  }

  const onResend = async () => {
    setResendLoading(true)
    try {
      await resendCode({ email })
      setCooldown(60)
    } catch {
      setError('Не вдалося надіслати код')
    } finally {
      setResendLoading(false)
    }
  }

  return (

    <>
      <div className="relative overflow-visible">
        <div className="absolute -top-20 left-40 w-72 h-72 bg-lime-200 rounded-full opacity-15 pointer-events-none z-0" />
        <div className="absolute -bottom-10 right-50  w-62 h-62 bg-lime-200 rounded-full opacity-15 pointer-events-none z-0" />


        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="relative flex flex-col md:flex-row w-full max-w-4xl mx-auto bg-white rounded-xl mt-14 shadow-md overflow-hidden border-2 border-lime-300"
        >
          {/* Иллюстрация + полупрозрачный оверлей пункт 10 */}
          <div className="md:w-1/2 relative p-6 bg-lime-300    flex items-center justify-center">
            <div className="absolute inset-0 opacity-10  pointer-events-none" />
            <img
              src="/learning.svg"
              alt="Illustration"
              className="relative w-full object-contain rounded-xl"
            />
          </div>

          {/* Форма */}
          <div className="md:w-1/2 p-4">
            {/* Шаги */}
            <div className="flex justify-center space-x-2">
              <div className="mb-4">
                <div className="h-2 w-full rounded-full bg-lime-100 overflow-hidden">
                  <motion.div
                    className="h-full bg-lime-500"
                    initial={{ width: 0 }}
                    animate={{ width: '50%' }}
                    transition={{ duration: 0.5 }}
                  />
                </div>
                <p className="text-xs text-gray-500 mt-1 text-center">Крок 1 із 2</p>
              </div>
            </div>
            <h2 className="text-3xl font-bold text-gray-800 text-center mb-4">
              Реєстрація в FreakFlow
            </h2>

            <Form {...form}>
              <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
                {/* Name */}
                <FormField name="name" render={({ field }) => (
                  <FormItem>
                    <FormLabel>Ім'я</FormLabel>
                    <FormControl>
                      <motion.div whileFocus={{ scale: 1.02 }} className="relative">
                        <User className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" size={16} />
                        <Input {...field} placeholder="Ваше ім'я" className="pl-10" />
                      </motion.div>
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )} />

                {/* Email */}
                <FormField name="email" render={({ field }) => (
                  <FormItem>
                    <FormLabel>Email</FormLabel>
                    <FormControl>
                      <motion.div whileFocus={{ scale: 1.02 }} className="relative">
                        <Mail className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" size={16} />
                        <Input {...field} type="email" placeholder="you@example.com" className="pl-10" />
                      </motion.div>
                    </FormControl>
                    <FormMessage>
                      {field.value && !formState.errors.email && (
                        <span className="text-sm text-green-600">✓ Email валідний</span>
                      )}
                    </FormMessage>
                  </FormItem>
                )} />

                {/* Password */}
                <FormField name="password" render={({ field }) => (
                  <FormItem>
                    <FormLabel>Пароль</FormLabel>
                    <FormControl>
                      <motion.div whileFocus={{ scale: 1.02 }} className="relative">
                        <Lock className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" size={16} />
                        <Input
                          {...field}
                          type={showPwd ? 'text' : 'password'}
                          placeholder="Пароль"
                          className="pl-10 pr-10"
                        />
                        <button
                          type="button"
                          onClick={() => setShowPwd(p => !p)}
                          className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400"
                        >
                          {showPwd ? <EyeOff size={16} /> : <Eye size={16} />}
                        </button>
                      </motion.div>
                    </FormControl>
                    {/* шкала силы */}
                    {pwd && (
                      <div className="mt-1 h-2 w-full bg-gray-200 rounded-full overflow-hidden">
                        <div className={cn('h-full transition-all', barColor)} style={{ width: `${(strength / 4) * 100}%` }} />
                      </div>
                    )}
                    <FormMessage>
                      {field.value && strength >= 3 && (
                        <span className="text-sm text-green-600">✓ Достатньо складний</span>
                      )}
                    </FormMessage>
                  </FormItem>
                )} />

                {/* Confirm */}
                <FormField name="confirmPassword" render={({ field }) => (
                  <FormItem>
                    <FormLabel>Підтвердіть пароль</FormLabel>
                    <FormControl>
                      <motion.div whileFocus={{ scale: 1.02 }} className="relative">
                        <Lock className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" size={16} />
                        <Input
                          {...field}
                          type={showConfirm ? 'text' : 'password'}
                          placeholder="Підтвердіть пароль"
                          className="pl-10 pr-10"
                        />
                        <button
                          type="button"
                          onClick={() => setShowConfirm(c => !c)}
                          className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400"
                        >
                          {showConfirm ? <EyeOff size={16} /> : <Eye size={16} />}
                        </button>
                      </motion.div>
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )} />

                {/* Общая ошибка */}
                {formState.errors.root?.message && (
                  <p className="text-sm text-red-600 text-center">
                    {formState.errors.root.message}
                  </p>
                )}

                {/* Submit */}
                <Button
                  type="submit"
                  disabled={!formState.isValid || formState.isSubmitting || pending || regCooldown > 0}
                  className="w-full py-2 text-white font-semibold rounded-lg bg-lime-400 hover:bg-lime-500 transition"
                >
                  {formState.isSubmitting
                    ? 'Завантаження…'
                    : pending
                      ? verifyOpen
                        ? 'Очікуємо верифікацію…'
                        : `Повторити через ${regCooldown}s`
                      : 'Зареєструватися'}
                </Button>

                {/* Пункт 3: ссылка на логин */}
                <p className="text-center text-sm text-gray-600">
                  Уже є акаунт?{' '}
                  <a href="/?login=true" className="text-lime-600 hover:underline">
                    Увійти
                  </a>
                </p>
              </form>
            </Form>
          </div>
        </motion.div>
      </div>

      {/* Verification Dialog */}
      <Dialog
        open={verifyOpen}
        onOpenChange={(open) => {
          if (!open) {
            // запускаємо 15-сек таймер після закриття
            setRegCooldown(15)
          }
          setVerifyOpen(open)
        }}
      >
        <DialogContent className="max-w-md rounded-2xl p-8 shadow-xl">
          <div className="flex justify-center space-x-2 mb-1">
          <div >
                <div className="h-2 w-full rounded-full bg-lime-100 overflow-hidden">
                  <motion.div
                    className="h-full bg-lime-500"
                    initial={{ width: 0 }}
                    animate={{ width: '100%' }}
                    transition={{ duration: 0.5 }}
                  />
                </div>
                <p className="text-xs text-gray-500 mt-1 text-center">Крок 2 із 2</p>
              </div>
          </div>
          <DialogHeader className=" flex justify-center">
            <DialogTitle className="text-2xl text-center">Підтвердіть e-mail</DialogTitle>
            <DialogDescription className='text-center'>
              Ми надіслали код на <span className="font-medium">{email}</span>
            </DialogDescription>
          </DialogHeader>

          <div className="flex justify-center mb-4">
            <InputOTP
              maxLength={6}
              value={otp}
              onChange={setOtp}
              containerClassName="space-x-2"
            >
              <InputOTPGroup>
                <InputOTPSlot index={0} />
                <InputOTPSlot index={1} />
                <InputOTPSlot index={2} />
              </InputOTPGroup>
              <InputOTPSeparator />
              <InputOTPGroup>
                <InputOTPSlot index={3} />
                <InputOTPSlot index={4} />
                <InputOTPSlot index={5} />
              </InputOTPGroup>
            </InputOTP>
          </div>

          {error && (
            <p className="text-sm text-red-600 text-center mb-2">{error}</p>
          )}

          <Button
            type="button"
            onClick={onVerify}
            className="w-full mb-2 bg-gradient-to-r from-lime-300 to-lime-400 hover:opacity-90"
          >
            Підтвердити
          </Button>
          <Button
            type="button"
            variant="outline"
            disabled={cooldown > 0 || resendLoading}
            onClick={onResend}
            className="w-full"
          >
            {resendLoading
              ? 'Надсилаємо…'
              : cooldown > 0
                ? `Надіслати ще раз (${cooldown})`
                : 'Надіслати код ще раз'}
          </Button>
        </DialogContent>
      </Dialog>
    </>
  )
}
