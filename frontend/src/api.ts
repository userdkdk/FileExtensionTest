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

  const contentType = response.headers.get('content-type') ?? ''
  const isJson = contentType.includes('application/json')
  const payload = isJson ? await response.json() : await response.text()

  if (!response.ok) {
    // 서버가 JSON으로 에러 메시지를 내려줄 때 우선 사용
    if (isJson && payload?.error?.message) {
      throw new Error(payload.error.message)
    }
    if (typeof payload === 'string' && payload.trim()) {
      throw new Error(payload)
    }
    throw new Error(`요청이 실패했습니다. (${response.status})`)
  }

  return payload as CommonResponse<T>
}

export async function getExtensions() {
  const res = await request<Extension[]>('/api/extension')
  return res.data ?? []
}

export async function createExtension(extension: string) {
  const res = await request<null>('/api/extension', {
    method: 'POST',
    body: JSON.stringify({ extension }),
  })
  return res
}

export async function updateExtension(extension: string, enabled: boolean) {
  const res = await request<null>('/api/extension', {
    method: 'PATCH',
    body: JSON.stringify({ extension, enabled }),
  })
  return res
}
