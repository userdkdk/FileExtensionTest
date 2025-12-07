import { useEffect, useMemo, useState } from 'react'
import type { ChangeEvent, FormEvent } from 'react'
import './App.css'
import { createExtension, getExtensions } from './api'
import type { Extension } from './api'

const apiBase = import.meta.env.VITE_API_BASE_URL ?? ''

function App() {
  const [extensions, setExtensions] = useState<Extension[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [newExtension, setNewExtension] = useState('')
  const [file, setFile] = useState<File | null>(null)
  const [uploadMessage, setUploadMessage] = useState<string | null>(null)

  useEffect(() => {
    loadExtensions()
  }, [])

  const allowedExtensions = useMemo(
    () => extensions.filter((ext) => ext.enabled ?? true).map((ext) => ext.extension),
    [extensions]
  )

  async function loadExtensions() {
    try {
      setLoading(true)
      setError(null)
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
    const ext = newExtension.trim()
    if (!ext) {
      setError('추가할 확장자를 입력하세요.')
      return
    }
    try {
      setError(null)
      await createExtension(ext)
      setNewExtension('')
      await loadExtensions()
      setUploadMessage(`.${ext} 확장자가 등록되었습니다.`)
    } catch (err) {
      setError(err instanceof Error ? err.message : '등록에 실패했습니다.')
    }
  }

  function handleFileChange(event: ChangeEvent<HTMLInputElement>) {
    const selected = event.target.files?.[0]
    setFile(selected ?? null)
    setUploadMessage(null)
    if (!selected) return

    const parts = selected.name.split('.')
    const ext = parts.length > 1 ? parts.pop()?.toLowerCase() : undefined
    if (!ext) {
      setUploadMessage('파일 확장자를 확인할 수 없습니다.')
      return
    }

    if (allowedExtensions.includes(ext)) {
      setUploadMessage(`.${ext} 파일은 업로드 가능합니다.`)
    } else {
      setUploadMessage(`.${ext} 파일은 허용되지 않았습니다.`)
    }
  }

  return (
    <div className="page">
      <header className="hero">
        <div>
          <p className="eyebrow">Secure upload</p>
          <h1>허용된 확장자만 업로드</h1>
          <p className="lede">
            백엔드의 확장자 목록을 불러와 클라이언트에서 1차 검증 후 업로드합니다. 환경에 따라 자동으로
            다른 API 주소를 사용합니다.
          </p>
        </div>
        <div className="badge">
          <span>API Base</span>
          <strong>{apiBase || '미설정'}</strong>
        </div>
      </header>

      <main className="grid">
        <section className="card">
          <header className="card__header">
            <div>
              <p className="eyebrow">Step 1</p>
              <h2>파일 선택</h2>
            </div>
            <span className="chip chip--accent">{allowedExtensions.length}개 허용됨</span>
          </header>
          <label className="upload-box">
            <input type="file" onChange={handleFileChange} />
            <div>
              <p className="upload-title">파일을 선택해 확장자를 확인하세요</p>
              <p className="upload-hint">허용된 확장자인지 즉시 확인합니다.</p>
            </div>
          </label>
          {file && (
            <div className="file-row">
              <div>
                <p className="file-name">{file.name}</p>
                <p className="file-size">{(file.size / 1024).toFixed(1)} KB</p>
              </div>
              <span className="chip">{file.name.split('.').pop()?.toLowerCase()}</span>
            </div>
          )}
          {uploadMessage && <p className="info">{uploadMessage}</p>}
        </section>

        <section className="card">
          <header className="card__header">
            <div>
              <p className="eyebrow">Step 2</p>
              <h2>허용 확장자 목록</h2>
            </div>
            <button className="link-button" onClick={loadExtensions} disabled={loading}>
              다시 불러오기
            </button>
          </header>
          {loading ? (
            <p className="muted">불러오는 중...</p>
          ) : error ? (
            <p className="error">{error}</p>
          ) : extensions.length === 0 ? (
            <p className="muted">등록된 확장자가 없습니다.</p>
          ) : (
            <ul className="list">
              {extensions.map((ext) => (
                <li key={ext.extension} className="list__item">
                  <div>
                    <p className="ext-name">.{ext.extension}</p>
                    {ext.builtIn && <p className="badge-inline">기본 제공</p>}
                  </div>
                  <span className={`chip ${ext.enabled === false ? 'chip--off' : 'chip--on'}`}>
                    {ext.enabled === false ? '비활성' : '활성'}
                  </span>
                </li>
              ))}
            </ul>
          )}
        </section>

        <section className="card">
          <header className="card__header">
            <div>
              <p className="eyebrow">Step 3</p>
              <h2>확장자 추가</h2>
            </div>
          </header>
          <form className="form" onSubmit={handleAddExtension}>
            <div className="field">
              <label htmlFor="extension">확장자</label>
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
            <button type="submit" className="primary">
              추가하기
            </button>
          </form>
          <p className="hint">
            입력 시 공백은 제거되고 소문자로 저장됩니다. 실제 업로드 API 호출은 백엔드 업로드 엔드포인트와
            연결해 사용하세요.
          </p>
        </section>
      </main>
    </div>
  )
}

export default App
