# Kiến thức tra cứu

Kiến thức tra cứu, do Claude soạn. Anh đọc, không cần sửa.

| File | Chủ đề | Ngày trong roadmap |
| --- | --- | --- |
| [00-data-modeling.md](00-data-modeling.md) | Thiết kế dữ liệu, kiểu dữ liệu, constraint, ERD Mermaid | Ngày 0 |
| [01-spring-boot-basics.md](01-spring-boot-basics.md) | Spring Boot chạy thế nào, Lombok, Maven | Ngày 1 |
| [02-jpa-persistence.md](02-jpa-persistence.md) | JPA, Entity, Repository, timestamp, `@Version`, unique tổ hợp | Ngày 2, 5 |
| [03-layers-dto.md](03-layers-dto.md) | Controller/Service/Repository, DTO, REST, tự hỏi trước khi làm endpoint | Ngày 3 |
| [04-validation-errors.md](04-validation-errors.md) | Validation, `@RestControllerAdvice` | Ngày 4 |
| [05-pagination-relations.md](05-pagination-relations.md) | Phân trang, quan hệ bảng, N+1 | Ngày 6, 8 |
| [06-security-jwt.md](06-security-jwt.md) | Spring Security, BCrypt, JWT, phân quyền | Ngày 10, 11 |
| [07-testing.md](07-testing.md) | Mockito, `@WebMvcTest`, Testcontainers | Ngày 12 |
| [08-flyway-docker.md](08-flyway-docker.md) | Flyway, profile, logging, Docker, Swagger | Ngày 13, 14 |
| [09-terminal-docker-sql.md](09-terminal-docker-sql.md) | Lệnh Docker Compose, psql, SQL cơ bản | Ngày 1, 2, 5, 6, 9 |
| [10-java-http-basics.md](10-java-http-basics.md) | Java cơ bản, HTTP, curl | Ngày 1–6, 10–12 |
| [mistakes.md](mistakes.md) | Lỗi hay gặp, ghi sau mỗi lần review | mọi ngày |
| [review-questions.md](review-questions.md) | Câu hỏi ôn tập, ghi sau mỗi lần review | mọi ngày |

## Bảng thuật ngữ

Gặp từ lạ thì tra ở đây trước.

| Thuật ngữ | Nghĩa đơn giản |
| --- | --- |
| Annotation | Nhãn bắt đầu bằng `@`, gắn lên class hoặc hàm để Spring biết cần xử lý nó thế nào |
| Bean | Object do Spring tạo và quản lý giúp bạn |
| Dependency Injection (DI) | Spring tự đưa object cần dùng vào class của bạn qua constructor, bạn không tự `new` |
| Auto-configuration | Spring Boot thấy thư viện nào trong project thì tự cấu hình thư viện đó |
| Starter | Gói dependency đã ghép sẵn, ví dụ `spring-boot-starter-web` |
| ORM | Kỹ thuật ánh xạ bảng database thành class Java |
| Entity | Class Java tương ứng với một bảng |
| DTO | Class chỉ dùng để nhận và trả dữ liệu qua API |
| Lazy / Eager loading | Tải dữ liệu liên quan khi cần dùng / tải ngay từ đầu |
| Owning side | Bên trong quan hệ giữ cột khoá ngoại |
| Idempotent | Gọi nhiều lần cho cùng kết quả như gọi một lần, ví dụ `GET`, `PUT`, `DELETE` |
| Filter | Đoạn code chạy trước Controller, có thể chặn request |
| Authentication | Xác thực: bạn là ai |
| Authorization | Phân quyền: bạn được làm gì |
| Bearer token | Token gửi trong header `Authorization: Bearer ...` |
| Claim | Một mẩu thông tin trong JWT, ví dụ `sub`, `exp` |
| Salt | Chuỗi ngẫu nhiên trộn vào mật khẩu trước khi băm |
| Mock | Object giả dùng trong test |
| Migration | File thay đổi cấu trúc database theo phiên bản |
| Profile | Bộ cấu hình cho một môi trường, ví dụ dev hoặc prod |
| Image / Container | Bản đóng gói app / bản đóng gói đang chạy |
