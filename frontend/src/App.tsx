import { useEffect, useMemo, useState } from 'react'
import type { FormEvent } from 'react'
import './App.css'
import { createExtension, getExtensions, updateExtension } from './api'
import type { Extension } from './api'

const BUILT_IN_EXTENSIONS = ['bat', 'cmd', 'com', 'cpl', 'exe', 'src', 'js']
const CUSTOM_MAX = 200

function App() {
  const [extensions, setExtensions] = useState<Extension[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [actionMessage, setActionMessage] = useState<string | null>(null)
  const [newExtension, setNewExtension] = useState('')

  useEffect(() => {
    loadExtensions()
  }, [])

  const builtInExtensions = useMemo(
    () =>
      BUILT_IN_EXTENSIONS.map((ext) => {
        const match = extensions.find((item) => item.extension === ext)
        return { extension: ext, enabled: match?.enabled ?? false }
      }),
    [extensions]
  )

  const customExtensions = useMemo(
    () => extensions.filter((ext) => !BUILT_IN_EXTENSIONS.includes(ext.extension)),
    [extensions]
  )

  async function loadExtensions() {
    try {
      setLoading(true)
      setError(null)
      setActionMessage(null)
      const list = await getExtensions()
      setExtensions(list)
    } catch (err) {
      setError(err instanceof Error ? err.message : '확장자 목록을 불러오지 못했습니다.')
    } finally {
      setLoading(false)
    }
  }

  async function handleAddExtension(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    const ext = newExtension.replace(/\s+/g, '').replace(/^\.+/, '').toLowerCase()
    if (!ext) {
      setError('추가할 확장자를 입력하세요.')
      return
    }
    if (ext.length > 15) {
      setError('길이가 15자를 넘으면 입력할 수 없습니다.')
      return
    }
    if (customExtensions.length >= CUSTOM_MAX) {
      setError(`커스텀 확장자는 최대 ${CUSTOM_MAX}개까지 추가할 수 있습니다.`)
      return
    }
    try {
      setError(null)
      setActionMessage(null)
      const res = await createExtension(ext)
      if (!res.success) {
        setError(res.error?.message ?? '등록에 실패했습니다.')
        return
      }
      setNewExtension('')
      await loadExtensions()
      setActionMessage(`.${ext} 커스텀 확장자가 추가되었습니다.`)
    } catch (err) {
      setError(err instanceof Error ? err.message : '등록에 실패했습니다.')
    }
  }

  async function toggleBuiltIn(ext: string, enabled: boolean) {
    try {
      setActionMessage(null)
      const res = await updateExtension(ext, enabled)
      if (!res.success) {
        setError(res.error?.message ?? '변경을 적용하지 못했습니다.')
        return
      }
      setExtensions((prev) =>
        prev.map((item) => (item.extension === ext ? { ...item, enabled } : item))
      )
      setActionMessage(`.${ext} 확장자 설정이 ${enabled ? '허용' : '차단'}되었습니다.`)
    } catch (err) {
      setError(err instanceof Error ? err.message : '변경 중 오류가 발생했습니다.')
    }
  }

  async function removeCustom(ext: string) {
    try {
      setActionMessage(null)
      const res = await updateExtension(ext, false)
      if (!res.success) {
        setError(res.error?.message ?? '삭제에 실패했습니다.')
        return
      }
      setExtensions((prev) => prev.filter((item) => item.extension !== ext))
      setActionMessage(`.${ext} 커스텀 확장자를 비활성화했습니다.`)
    } catch (err) {
      setError(err instanceof Error ? err.message : '삭제 중 오류가 발생했습니다.')
    }
  }

  return (
    <div className="page">
      <header className="hero">
        <div>
          <p className="eyebrow">Secure upload</p>
          <h1>허용된 확장자만 업로드</h1>
          <p className="lede">
            고정/커스텀 확장자를 관리하고, 허용 여부에 따라 업로드를 제어합니다.
          </p>
        </div>
      </header>

      <main className="grid">
        <section className="card">
          <header className="card__header">
            <div>
              <p className="eyebrow">Step 1</p>
              <h2>고정 확장자</h2>
            </div>
            <button className="link-button" onClick={loadExtensions} disabled={loading}>
              다시 불러오기
            </button>
          </header>
          {loading ? (
            <p className="muted">불러오는 중...</p>
          ) : (
            <ul className="list">
              {builtInExtensions.map((ext) => (
                <li key={ext.extension} className="list__item list__item--checkbox">
                  <label>
                    <input
                      type="checkbox"
                      checked={ext.enabled}
                      onChange={(e) => toggleBuiltIn(ext.extension, e.target.checked)}
                    />
                    <span>.{ext.extension}</span>
                  </label>
                </li>
              ))}
            </ul>
          )}
        </section>

        <section className="card">
          <header className="card__header">
            <div>
              <p className="eyebrow">Step 2</p>
              <h2>커스텀 확장자</h2>
            </div>
            <span className="chip chip--accent">
              {customExtensions.length} / {CUSTOM_MAX}
            </span>
          </header>
          <form className="form" onSubmit={handleAddExtension}>
            <div className="field">
              <label htmlFor="extension">확장자 추가</label>
              <div className="field__input">
                <span className="prefix">.</span>
                <input
                  id="extension"
                  name="extension"
                  value={newExtension}
                  onChange={(e) => setNewExtension(e.target.value)}
                  placeholder="pdf"
                  autoComplete="off"
                />
              </div>
            </div>
            <button type="submit" className="primary" disabled={customExtensions.length >= CUSTOM_MAX}>
              추가하기
            </button>
          </form>
          {customExtensions.length > 0 ? (
            <ul className="tag-list">
              {customExtensions.map((ext) => (
                <li key={ext.extension} className="tag">
                  <span>.{ext.extension}</span>
                  <button type="button" onClick={() => removeCustom(ext.extension)} aria-label="삭제">
                    ×
                  </button>
                </li>
              ))}
            </ul>
          ) : (
            <p className="muted">등록된 커스텀 확장자가 없습니다.</p>
          )}
          <p className="hint">
            입력 시 공백은 제거되고 소문자로 저장됩니다. 추가는 응답 success가 true일 때만 반영됩니다.
          </p>
        </section>
      </main>
      {(error || actionMessage) && (
        <div className="feedback">
          {error && <p className="error">{error}</p>}
          {actionMessage && !error && <p className="info">{actionMessage}</p>}
        </div>
      )}
    </div>
  )
}

export default App
