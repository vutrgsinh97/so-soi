# Flyway, profile, logging, Docker và Swagger

Dùng cho: Ngày 13 (Flyway, profile cấu hình, logging), Ngày 14 (Docker, tài liệu API).

## 1. Flyway

**Flyway** là công cụ quản lý thay đổi database bằng các file SQL có đánh số phiên bản, giống Git cho schema.

- Đặt file trong `src/main/resources/db/migration`, tên theo dạng `V1__init.sql`, `V2__add_priority.sql` (hai dấu gạch dưới).
- Khi app khởi động, Flyway tự chạy những file chưa chạy và ghi lại vào bảng `flyway_schema_history`.
- **Không sửa file đã chạy**. Muốn thay đổi thì tạo file mới với số phiên bản cao hơn.
- Khi đã dùng Flyway thì đặt `spring.jpa.hibernate.ddl-auto: validate` để Hibernate chỉ kiểm tra chứ không tự sửa bảng. (Các giá trị `ddl-auto` khác: `02-jpa-persistence.md`, mục 10.)

## 2. Profile cấu hình

**Profile** cho phép có nhiều bộ cấu hình cho các môi trường khác nhau. Đây là *Spring profile*, khác bảng `profile` của Sổ Sói.

- `application.yml` chứa cấu hình chung. `application-dev.yml` và `application-prod.yml` chứa phần riêng.
- Chọn profile bằng `spring.profiles.active=dev`, hoặc biến môi trường `SPRING_PROFILES_ACTIVE=prod` khi chạy Docker.

## 3. Logging

- Các mức log từ chi tiết đến nghiêm trọng: `TRACE` < `DEBUG` < `INFO` < `WARN` < `ERROR`.
- Chỉnh mức log theo package: `logging.level.com.example.sosoi: DEBUG`.
- Mặc định Spring Boot chỉ ghi log ra console. Muốn ghi ra file thì đặt `logging.file.name`.
- Không log mật khẩu, token hay nội dung `short_description`.

## 4. Docker

**Docker** đóng gói app cùng môi trường chạy (Java) thành một **image**, chạy ở đâu cũng giống nhau.

- **Dockerfile**: công thức tạo image, ví dụ lấy image có Java, copy file `.jar` vào, chạy `java -jar`.
- **Image** là bản đóng gói. **Container** là image đang chạy.
- Nên chạy app bằng user thường, không dùng user root, để an toàn hơn.

## 5. Docker Compose

Chạy nhiều container cùng lúc bằng một file `docker-compose.yml`:

- Mỗi thành phần là một **service**, ví dụ `app` và `db`.
- Trong Compose, app kết nối database bằng **tên service** (`jdbc:postgresql://db:5432/sosoi`), không dùng `localhost`.
- Một lệnh `docker compose up` chạy cả hệ thống.

## 6. Swagger / OpenAPI

Tạo trang tài liệu API tự động từ code.

- Chỉ cần thêm dependency `springdoc-openapi-starter-webmvc-ui`, không cần cấu hình gì thêm.
- Mở `/swagger-ui.html` để xem danh sách endpoint và bấm thử gọi API ngay trên trình duyệt.
- Nhớ cho phép `/swagger-ui/**` và `/v3/api-docs/**` đi qua Spring Security, nếu không sẽ bị chặn 401.

Docs: [Database Initialization](https://docs.spring.io/spring-boot/how-to/data-initialization.html) · [Profiles](https://docs.spring.io/spring-boot/reference/features/profiles.html) · [Spring Boot with Docker](https://spring.io/guides/gs/spring-boot-docker/) · [Docker: Multi-stage builds](https://docs.docker.com/build/building/multi-stage/) · [springdoc-openapi Getting Started](https://springdoc.org/v2/getting-started.html).
