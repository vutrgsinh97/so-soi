# Lộ trình Sổ Sói

App nhật ký nhỏ: mỗi ngày một **tờ** (ăn gì, uống gì, tâm trạng, một câu ngắn), gắn **thẻ**, xem lại và lọc. Đánh dấu `[x]` khi xong việc.

## Luật chơi

- Tối thiểu **30 phút code tay mỗi ngày**. Hứng thì làm thêm. Một "ngày" trong lộ trình có thể kéo dài hai, ba buổi, không sao.
- Không để AI viết code. Được đọc docs và Google.
- Bí quá 30 phút: được hỏi AI, nhưng chỉ hỏi khái niệm, không xin code.
- Làm xong mới nhờ review.
- Gõ tay, không copy-paste. Mỗi buổi commit ít nhất một lần.
- Đọc docs chính thức trước, Baeldung sau.

## Học kèm: Markdown, handoff, tiếng Anh

Mục tiêu: đọc hiểu file của thợ, và tự viết handoff cho thợ đọc.

- **Cuối mỗi buổi**: viết `docs/notes/YYYY-MM-DD.md` bằng **tiếng Anh**, tối đa 10 dòng, đúng 3 mục: `## Done`, `## Next`, `## Stuck`. Đây là handoff cho chính mình buổi sau.
- **Cuối mỗi tuần** (ngày 7, 14): viết `docs/notes/handoff-week-N.md` bằng tiếng Anh, như thể giao project cho một người lạ: đang ở đâu, chạy thế nào, còn gì dở, cần quyết gì.
- **Mỗi tuần một lần**: lấy một file report thật của thợ, đọc, rồi tóm lại 5 dòng bằng lời của mình. Gia sư so với bản gốc.
- Markdown học dần qua chính mấy file này: heading, list, bảng, code block, link, `<details>`, Mermaid.
- Gia sư review cả note: chỗ nào khó hiểu, sai ngữ pháp, câu nào nói ngắn hơn được.

### Terminal thay cho GUI

- Git, Docker, Postgres, gọi API: **chỉ dùng terminal**. Không mở Docker Desktop, không dùng nút Git trong IDE, không dùng Postman.
  - Git: `git status`, `git add -p`, `git commit`, `git log --oneline`, `git diff`
  - Docker: `docker compose up -d`, `ps`, `logs -f`, `exec`, `down`
  - Postgres: `psql`, `\dt`, `\d`, `\q`
  - API: `curl` (kèm `-i` để xem status, `-H` để gửi header, `-d` để gửi body)
- Gặp lệnh mới thì tự ghi vào `docs/notes/terminal.md`: lệnh, nó làm gì, một ví dụ. Sổ tay của chính Lang, không chép của ai.
- Lỗi thì đọc thông báo lỗi trong terminal trước, rồi mới hỏi.

Docs:

- [GitHub: Basic writing and formatting syntax](https://docs.github.com/en/get-started/writing-on-github/getting-started-with-writing-and-formatting-on-github/basic-writing-and-formatting-syntax)
- [Mermaid: Flowchart](https://mermaid.js.org/syntax/flowchart.html)

## Yêu cầu

App làm được những việc này. Thiết kế dữ liệu thế nào là bài của Lang.

- Mỗi ngày ghi một **tờ**: hôm đó ăn gì, uống gì, tâm trạng, một câu ngắn.
- Mỗi profile chỉ có **một** tờ cho một ngày.
- Gắn **thẻ** vào tờ, ví dụ `cà phê`, `trượt bánh`. Một tờ có nhiều thẻ, một thẻ dùng ở nhiều tờ. Mỗi profile một bộ thẻ riêng.
- Xem lại theo khoảng ngày, lọc theo thẻ hoặc tâm trạng.
- Có đăng ký, đăng nhập. Một account có nhiều profile. Ai chỉ thấy sổ của người đó.
- Ví dụ:
```
  Ngày:       13/11
  Ăn:         (Lang ăn gì thì điền)
  Uống:       cà phê sữa tươi
  Tâm trạng:  ★★★★☆  (4)
  Một câu:    Nghỉ làm, giặt cả thau đồ, bị nha sĩ khám lưỡi.
  Thẻ:        [nghỉ] [việc nhà] [adrenaline]
```

#### Ngày 0: Tự thiết kế dữ liệu

- [x] Vẽ sơ đồ ER bằng Mermaid (`erDiagram`) trong `docs/notes/data-model.md`: có những bảng nào, mỗi bảng có cột gì, kiểu gì, quan hệ ra sao
- [x] Ghi bằng tiếng Anh vài dòng: vì sao chọn vậy, chỗ nào còn phân vân
- [x] Nhờ gia sư review. Gia sư chỉ hỏi và chỉ chỗ hở, không đưa sơ đồ đúng

Kết quả: `ACCOUNT` 1-N `PROFILE`, `PROFILE` 1-N `TICKET`, `PROFILE` 1-N `TAG`, `TICKET` N-N `TAG` qua `TAG_TICKET`. Từ Ngày 2 trở đi, mọi bảng làm theo sơ đồ này.

Docs:

- [Mermaid: Entity Relationship Diagrams](https://mermaid.js.org/syntax/entityRelationshipDiagram.html)

## Phần 1: Backend (14 ngày)

### Tuần 1: Nền tảng

#### Ngày 1: Khởi tạo

- [x] `git init` ngay từ đầu, commit đầu tiên chỉ có project rỗng
- [x] Tạo project trên start.spring.io (Java 21, Maven, Spring Web, Spring Data JPA, PostgreSQL Driver, Validation)
- [x] Chạy Postgres bằng `docker-compose.yml` (chưa có app), có **volume** để tắt container không mất dữ liệu
- [x] Vào `psql` bằng `docker compose exec`, gõ `\l`, `\dt` cho quen
- [x] Cấu hình datasource trong `application.yml`
- [x] `GET /ping` trả JSON `{ "status": "ok" }`

Chưa có migration ở ngày này. Bảng do JPA tạo tạm (`ddl-auto: update`) tới Ngày 13 mới chuyển sang Flyway (bắt đầu từ `V1`, không có `V0`).

Docs:

- [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
- [Postgres Docker image](https://hub.docker.com/_/postgres/)

#### Ngày 2: Entity Account, Profile, Ticket

- [x] Entity `Account`, `Profile`, `Ticket` theo sơ đồ Ngày 0 (chưa có `Tag`), cùng Repository
- [x] `Profile` → `Account` và `Ticket` → `Profile` bằng `@ManyToOne`, bên giữ FK là bên "nhiều"
- [x] `created_at`, `updated_at` tự gán khi lưu và khi sửa
- [ ] Chưa có đăng ký: tự `INSERT` một account và một profile bằng tay trong `psql` để có `profile_id` mà dùng
- [x] Trước khi code: chốt `feeling` là chữ hay số 1 tới 5. Sơ đồ đang là `string`, yêu cầu ghi 1 tới 5; Ngày 4, 6, 9 cần số

Docs:

- [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa)

#### Ngày 3: CRUD theo tầng

- [ ] Tách Controller, Service, Repository
- [ ] 5 endpoint: `GET /tickets`, `GET /tickets/{id}`, `POST /tickets`, `PUT /tickets/{id}`, `DELETE /tickets/{id}`
- [ ] Tạm thời mọi tờ gắn với profile tạo tay ở Ngày 2
- [ ] DTO `TicketRequest` và `TicketResponse` bằng `record`, không trả Entity ra ngoài

Docs:

- [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
- [Entity vs DTO](https://www.baeldung.com/java-entity-vs-dto)

#### Ngày 4: Validation và lỗi

- [ ] `feeling` hợp lệ (theo cách đã chốt ở Ngày 2), `short_description` tối đa 280 ký tự, `day` không được ở tương lai
- [ ] `GlobalExceptionHandler` với `@RestControllerAdvice`
- [ ] 404 khi không thấy, 400 kèm danh sách lỗi khi dữ liệu sai

Docs:

- [Validation in Spring Boot](https://www.baeldung.com/spring-boot-bean-validation)
- [Error Handling for REST with Spring](https://www.baeldung.com/exception-handling-for-rest-with-spring)

#### Ngày 5: Một ngày một tờ

- [ ] `UNIQUE (profile_id, day)` cho `Ticket`
- [ ] Tạo trùng ngày thì trả **409 Conflict**, không để lộ lỗi SQL ra ngoài
- [ ] `@Version` cho `Ticket`. Hai lần `PUT` cùng một bản cũ thì lần sau trả **409**
- [ ] `GET /tickets/today`: lấy tờ hôm nay, chưa có thì 404
- [ ] Database: vào `psql` xem constraint vừa tạo (`\d ticket`), tự `INSERT` trùng bằng tay xem Postgres báo gì
- [ ] Ghi vào note bằng lời của mình: vì sao kiểm "đã có tờ chưa" trong Java là không đủ, và vì sao `version` không nằm trong unique

Docs:

- [Defining Unique Constraints in JPA](https://www.baeldung.com/jpa-unique-constraints)
- [PostgreSQL: Constraints](https://www.postgresql.org/docs/current/ddl-constraints.html)
- [Optimistic Locking in JPA](https://www.baeldung.com/jpa-optimistic-locking)

#### Ngày 6: Phân trang, lọc theo khoảng ngày

- [ ] `GET /tickets?from=2026-11-01&to=2026-11-30&page=0&size=10&sort=day,desc`
- [ ] Lọc tâm trạng tối thiểu: `?minFeeling=4` (nếu `feeling` là số)
- [ ] Trả DTO phân trang riêng, không trả thẳng `Page` của Spring
- [ ] Database: dùng `generate_series` trong `psql` tạo khoảng 10.000 tờ giả. Chạy `EXPLAIN ANALYZE` câu lọc theo khoảng ngày, thêm index, chạy lại, so thời gian

Docs:

- [Pagination and Sorting using Spring Data JPA](https://www.baeldung.com/spring-data-jpa-pagination-sorting)
- [PostgreSQL: Using EXPLAIN](https://www.postgresql.org/docs/current/using-explain.html)
- [PostgreSQL: Indexes](https://www.postgresql.org/docs/current/indexes.html)

#### Ngày 7: Ôn tập

- [ ] Project nháp mới, viết lại ngày 1–4 không nhìn code cũ
- [ ] Ghi chỗ nào còn phải tra
- [ ] Nhờ review cả tuần

### Tuần 2: Thẻ, đăng nhập, production-ready

#### Ngày 8: Thẻ và quan hệ nhiều-nhiều

- [ ] Entity `Tag` thuộc về `Profile` (`@ManyToOne`), `UNIQUE (profile_id, name)`
- [ ] `Ticket` N-N `Tag` qua bảng `tag_ticket`: `@ManyToMany` + `@JoinTable`
- [ ] Tạo tờ kèm danh sách tên thẻ; thẻ chưa có trong bộ của profile thì tạo mới, có rồi thì dùng lại
- [ ] `GET /tickets?tag=cà phê`
- [ ] Bật `show-sql`, tự gây N+1 khi lấy danh sách tờ kèm thẻ, rồi sửa

Docs:

- [Many-To-Many Relationship in JPA](https://www.baeldung.com/jpa-many-to-many)
- [N+1 query problem](https://vladmihalcea.com/n-plus-1-query-problem/)

#### Ngày 9: Thống kê

- [ ] `GET /stats?from=&to=`: số tờ, tâm trạng trung bình, top 5 thẻ dùng nhiều nhất
- [ ] Viết câu SQL thống kê **bằng tay trong `psql` trước** (`GROUP BY`, `JOIN`, `AVG`, `ORDER BY ... LIMIT`), chạy ra đúng rồi mới chuyển sang code
- [ ] Viết bằng `@Query` JPQL, không lấy hết về rồi đếm bằng Java

Docs:

- [Spring Data JPA @Query](https://www.baeldung.com/spring-data-jpa-query)

#### Ngày 10: Đăng ký, BCrypt

- [ ] `POST /auth/register`: tạo `Account` cùng profile đầu tiên, trong **một transaction**
- [ ] Mã hoá mật khẩu bằng BCrypt, lưu vào `password_hash`
- [ ] `username` trùng thì 409
- [ ] `POST /profiles`, `GET /profiles`: account tự tạo thêm và xem profile của mình

Docs:

- [Spring Security Architecture](https://docs.spring.io/spring-security/reference/servlet/architecture.html)
- [Password Encoding with BCrypt](https://www.baeldung.com/spring-security-registration-password-encoding-bcrypt)
- [Spring @Transactional](https://www.baeldung.com/transaction-configuration-with-jpa-and-spring)

#### Ngày 11: JWT và "sổ của ai người nấy thấy"

- [ ] `POST /auth/login` trả access token (token mang `account_id`)
- [ ] Chốt trước khi code: request đang làm cho profile nào? Ví dụ đường dẫn `/profiles/{profileId}/tickets`, hoặc header riêng. Ghi lý do vào note
- [ ] Mọi endpoint tờ, thẻ, thống kê cần token
- [ ] Chỉ đụng được profile thuộc account của mình. Đụng profile, tờ hay thẻ của người khác thì 404, không phải 403 (để khỏi lộ là nó có tồn tại)

Docs:

- [Introduction to JSON Web Tokens](https://jwt.io/introduction)
- [OAuth 2.0 Resource Server JWT](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html)

#### Ngày 12: Test

- [ ] Unit test `TicketService` với JUnit 5 và Mockito, có ca trùng ngày
- [ ] `@WebMvcTest` cho controller: 400, 404, 409
- [ ] Một test `@SpringBootTest` với Testcontainers: đăng ký, đăng nhập, tạo tờ, lấy lại

Docs:

- [Mockito and JUnit 5](https://www.baeldung.com/mockito-junit-5-extension)
- [Testing a Spring Boot REST API with Testcontainers](https://testcontainers.com/guides/testing-spring-boot-rest-api-using-testcontainers/)

#### Ngày 13: Flyway, profile cấu hình, logging

- [ ] Schema sang `V1__init.sql` (tự viết `CREATE TABLE` cho 5 bảng theo sơ đồ Ngày 0, kèm `NOT NULL`, FK, unique), `ddl-auto: validate`
- [ ] `application-dev.yml`, `application-prod.yml` (đây là *Spring profile*, khác bảng `profile` của mình)
- [ ] Không log mật khẩu, token hay nội dung `short_description`

Docs:

- [Database Initialization](https://docs.spring.io/spring-boot/how-to/data-initialization.html)
- [Profiles](https://docs.spring.io/spring-boot/reference/features/profiles.html)

#### Ngày 14: Docker và tài liệu API

- [ ] `Dockerfile` **multi-stage**: một stage build bằng Maven, một stage chỉ chạy `.jar`, chạy bằng user thường không phải root
- [ ] Thêm app vào `docker-compose.yml`, `healthcheck` cho Postgres, app chỉ khởi động khi database sẵn sàng
- [ ] So kích thước image trước và sau multi-stage (`docker images`)
- [ ] springdoc, mở `/swagger-ui.html`
- [ ] README: cách chạy, danh sách endpoint

Docs:

- [Spring Boot with Docker](https://spring.io/guides/gs/spring-boot-docker/)
- [Docker: Multi-stage builds](https://docs.docker.com/build/building/multi-stage/)
- [Compose: Control startup order](https://docs.docker.com/compose/how-tos/startup-order/)
- [springdoc-openapi Getting Started](https://springdoc.org/v2/getting-started.html)

## Phần 2: Frontend (10 ngày, soạn chi tiết khi xong Phần 1)

Code trong folder `web/`. Framework chưa chốt: Vite + React, Next.js hoặc Vue. Quyết trước Ngày 15; các mục dưới đây giữ nguyên ý, đổi tên công cụ theo framework đã chọn.

- [ ] Ngày 15: Tạo project, hiểu component, props, state
- [ ] Ngày 16: Trang đăng nhập, lưu token, gọi API có `Authorization`
- [ ] Ngày 17: Chọn profile, form ghi tờ hôm nay, validate phía client
- [ ] Ngày 18: Danh sách tờ có phân trang và lọc
- [ ] Ngày 19: TanStack Query để gọi API và cache
- [ ] Ngày 20: Router: trang hôm nay, lịch sử, thống kê
- [ ] Ngày 21: Trang thống kê, biểu đồ đơn giản
- [ ] Ngày 22: Giao diện trên điện thoại
- [ ] Ngày 23: Test component
- [ ] Ngày 24: Đóng gói, chạy chung với backend bằng `docker compose`

Docs:

- [React: Learn](https://react.dev/learn)
- [Vite: Getting Started](https://vite.dev/guide/)
- [Next.js: Docs](https://nextjs.org/docs)
- [Vue: Guide](https://vuejs.org/guide/introduction.html)
- [TanStack Query](https://tanstack.com/query/latest/docs/framework/react/overview)
