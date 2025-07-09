
![image](https://github.com/user-attachments/assets/47264196-2722-41b0-a47c-1e03b0f733d6)

## 소개

공공데이터 포털에서 수집한 **레시피 정보**를 기반으로, 사용자 CLI 환경에서 **냉장고 속 식재료를 관리하고**, 해당 재료로 만들 수 있는 **레시피를 추천**받을 수 있는 자바 기반 프로그램입니다.  
Oracle DB와 JDBC를 연동하여 데이터를 실시간으로 조회하고, **내부 DB에 레시피가 없을 경우 웹 검색으로 대체**하는 기능도 제공합니다.

---
## 시연영상

https://github.com/user-attachments/assets/be4bf7d9-a22f-43fc-8048-1ddf73ceea89



## 프로젝트 목적

- 공공데이터를 활용한 실용적인 프로그램 개발
- CLI 환경 기반의 데이터 실시간 조작 학습
- 자바와 데이터베이스 연동을 통한 백엔드 구조 이해
- DB + 웹 연동으로 사용자 경험 개선

---

## 기술 스택 및 시스템 구성

| 항목           | 기술 / 도구                                                                 |
|----------------|-----------------------------------------------------------------------------|
| 언어           | Java                                                                        |
| DB             | Oracle                                                                      |
| DB 연동        | JDBC (ojdbc10-19.3.0.0.jar)                                                 |
| 데이터 수집    | 농림축산식품부 레시피 API (공공데이터 포털)                                 |
| 데이터 처리    | XML → CSV 전처리 (Python 사용)                                              |
| 실행 환경      | CLI (Command Line Interface)                                                |
| 기타           | Docker 이미지 생성 및 배포 가능, 웹 검색 연동 기능 포함                     |

---

## 핵심 기능

### 사용자 기능
- 사용자 등록 / 삭제

### 냉장고 재료 관리
- 보유 재료 조회
- 재료 추가 / 삭제
- 유통기한 지난 재료 자동 폐기 → `DISCARDED` 테이블 이관

### 레시피 기반 기능
- 재료명 기반 가능한 레시피 추천
- 레시피명 기반 필요한 재료 조회
- 레시피 상세 정보 출력 (칼로리, 조리시간, 인분 수 등)
- ❗ **레시피 DB에 없을 경우, 키워드 기반 웹에서 자동 검색 및 결과 제공**

---

---
### 메뉴 예시

```
1. 사용자 등록
2. 냉장고 재료 조회
3. 재료 추가
4. 재료 삭제
5. 레시피 검색 (재료 기반)
6. 레시피에 필요한 재료 조회
7. 유통기한 지난 재료 자동 폐기
8. DB에 없는 레시피 → 웹에서 자동 검색

```

---

## 📦 테이블 요약
![image](https://github.com/user-attachments/assets/7d6ceabe-7bdf-4154-a2f3-6eb93233df73)

| 테이블명 | 설명 |
| --- | --- |
| `USER` | 사용자 정보 |
| `FOOD_INFO` | 식재료 메타데이터 |
| `FRIDGE` | 사용자별 냉장고 상태 (user_id, food_id 등) |
| `RECIPE` | 레시피 기본 정보 |
| `RECIPE_INGREDIENT` | 각 레시피의 필요 재료 |
| `DISCARDED` | 유통기한 초과로 폐기된 재료 이력 |

---

## 🌐 웹 검색 기능 상세

- **동작 흐름**:
    1. 사용자가 레시피명으로 검색
    2. 내부 DB에서 해당 이름의 레시피가 존재하지 않음
    3. 네이버 등에서 크롤링 또는 API 호출로 대체 검색
    4. 결과를 요약해 사용자에게 CLI로 출력
- **장점**:
    - 데이터 부족 보완
    - 실시간 정보 제공
    - 사용자 신뢰도 향상

---
---
## 🧪 트러블슈팅 (문제 해결)
🔸 CLI 한글 깨짐 문제 (인코딩 이슈)
![image](https://github.com/user-attachments/assets/3a37b6f4-b3b4-4527-abb5-1d35e4735d8d)


---

## 🛠 실행 방법

### CLI 실행 예시 (Powershell 기준)

```powershell
$jarFiles = Get-ChildItem -Filter *.jar | ForEach-Object { $_.Name }
$classpath = ".;first;" + ($jarFiles -join ";")
java -cp $classpath first.Main
```
![image](https://github.com/user-attachments/assets/40ab2c27-815e-45f0-b977-41bf935e5afc)


## 🐳 Docker 배포 예시

```bash
# 컨테이너 → 이미지 저장
docker commit <container_id> eunchai5781/for_us:1

# Docker Hub에 업로드
docker push eunchai5781/for_us:1

# 다른 환경에서 실행
docker pull eunchai5781/for_us:1
docker run -it eunchai5781/for_us:1

```

---

##  디버깅 팁

- Oracle 연결 실패 시 확인 사항:
    - JDBC 드라이버 경로 및 classpath 설정
    - Oracle 계정 및 포트 정보
    - DB 테이블 유무, SQL 문법 오류

---


## API 및 데이터 수집

- 공공데이터 포털에서 **레시피 기본정보** API를 통해 데이터를 수집
- XML 파싱 후 CSV로 저장한 뒤 Oracle DB에 적재

```python
response = requests.get(url)
root = ET.fromstring(response.content)
for row in root.findall('.//row'):
    writer.writerow([...])  # 주요 레시피 정보 추출

```
## 팀원 후기
![image](https://github.com/user-attachments/assets/530380b3-228a-4fe7-b2a2-2c45209b4a7d)


## 📌 한 줄 요약

> 공공데이터 기반의 레시피 정보를 Oracle DB에 저장하고, 자바 기반 CLI를 통해 냉장고 재료를 관리하며, 내부에 없는 레시피는 웹에서 자동 검색해 추천하는 실용적 프로그램입니다.
>
