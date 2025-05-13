'use client'

import { useState } from 'react'
import { LifeBuoy } from 'lucide-react'
import {
  Dialog,
  DialogTrigger,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from '@/components/ui/dialog'

export default function HelpButton() {
  const [open, setOpen] = useState(false)

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <button
          className="fixed bottom-6 right-6 z-50 p-3 bg-lime-500 text-white rounded-full shadow-inner hover:bg-lime-600 transition"
          aria-label="Help"
        >
          <LifeBuoy className="w-6 h-6" />
        </button>
      </DialogTrigger>
      <DialogContent className="max-w-sm rounded-2xl p-6">
        <DialogHeader>
          <DialogTitle>Підтримка</DialogTitle>
          <DialogDescription className="mb-4">
            Якщо у вас виникли питання або потрібна допомога, напишіть нам на email:
          </DialogDescription>
        </DialogHeader>
        <p className="text-sm">
          <a
            href="mailto:rostikpastusenko2@gmail.com"
            className="text-lime-600 hover:underline"
          >
            rostikpastusenko2@gmail.com
          </a>
        </p>
      </DialogContent>
    </Dialog>
  )
}
