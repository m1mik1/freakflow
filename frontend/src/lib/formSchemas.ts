// src/lib/formSchemas.ts
import { z } from 'zod'

export const registerSchema = z
  .object({
    
    name: z
      .string()
      .min(2, "Ім'я мінімум 2 символи")
      .regex(/^[A-Za-z]+$/, 'Нікнейм має містити лише латинські літери'),
    // Перевірка email
    email: z
      .string()
      .email('Невірний формат email'),
    // Надійний пароль: мінімум 8 символів, великі, малі літери, цифри та спецсимволи
    password: z
      .string()
      .min(8, 'Пароль мінімум 8 символів')
      .regex(
        /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).+$/,
        'Пароль повинен містити великі, малі літери, цифри та спеціальні символи'
      ),
    confirmPassword: z.string(),
  })
  // Перевіряємо, що пароль і підтвердження співпадають
  .refine((data) => data.password === data.confirmPassword, {
    message: 'Паролі не співпадають',
    path: ['confirmPassword'],
  })

export type RegisterFormValues = z.infer<typeof registerSchema>