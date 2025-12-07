# Frontend (React + Vite)

새로운 업로드/확장자 관리용 프런트엔드입니다.

## 실행 방법
```bash
cd frontend
npm install
npm run dev    # 로컬 개발 서버
npm run build  # 프로덕션 번들 생성
```

## 주요 기능
- 백엔드 `/api/extension/` 목록을 불러와 허용 확장자를 표시
- 확장자 추가(POST `/api/extension/`)
- 선택한 파일의 확장자가 허용 목록에 있는지 클라이언트에서 즉시 검증

업로드 실제 전송은 백엔드 업로드 엔드포인트에 `fetch`/`FormData`를 추가해 연결하면 됩니다.
