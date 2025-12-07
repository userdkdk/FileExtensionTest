export type ErrorPayload = {
  code: string
  message: string
}

export type CommonResponse<T> = {
  success: boolean
  data: T
  error?: ErrorPayload | null
  timestamp: string
}

export type Extension = {
  extension: string
  enabled?: boolean
  builtIn?: boolean
}

const baseUrl = (import.meta.env.VITE_API_BASE_URL ?? '').replace(/\/$/, '')

async function request<T>(path: string, init?: RequestInit) {
  if (!baseUrl) {
    throw new Error('API base URL가 설정되지 않았습니다. .env 파일을 확인하세요.')
  }
  const response = await fetch(`${baseUrl}${path}`, {
    headers: {
      'Content-Type': 'application/json',
      ...init?.headers,
    },
    ...init,
  })

  if (!response.ok) {
    const text = await response.text()
    throw new Error(text || `요청이 실패했습니다. (${response.status})`)
  }

  return (await response.json()) as CommonResponse<T>
}

export async function getExtensions() {
  const res = await request<Extension[]>('/api/extension')
  return res.data ?? []
}

export async function createExtension(extension: string) {
  await request<null>('/api/extension', {
    method: 'POST',
    body: JSON.stringify({ extension }),
  })
}
