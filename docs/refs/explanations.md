# Giải thích dễ hiểu

Khái niệm chung (Spring Boot chạy thế nào, JPA, chia tầng, DTO, validation, phân trang, N+1, Security, JWT, test, Flyway, Docker) đã có ở `../demo/docs/explanations.md`. Đọc ở đó trước.

File này chỉ ghi những gì **riêng** của Sổ Sói. Gia sư thêm vào khi tới ngày tương ứng.

## Ngày 0: Nền tảng thiết kế dữ liệu

### 1. Bảng, dòng, cột

- **Bảng (table)**: một loại thứ cần lưu.
- **Dòng (row)**: một thứ cụ thể.
- **Cột (column)**: một thông tin của thứ đó. Mỗi cột có đúng một kiểu dữ liệu.

### 2. Một ô chỉ chứa một giá trị (atomic value)

Luật gốc của thiết kế quan hệ. Không lưu list trong một ô.

- **1-N**: FK nằm ở bảng bên N. Bảng bên 1 không có cột nào trỏ sang bảng bên N.
- **N-N**: tách ra bảng trung gian. Hai bảng chính không có cột nào trỏ sang nhau.
- **1-1**: FK nằm ở bảng phụ thuộc, kèm `UNIQUE`.

### 3. Kiểu dữ liệu

ERD vẽ cho database thì dùng kiểu **PostgreSQL**. Kiểu **Java** chỉ xuất hiện ở Entity.

| PostgreSQL | Java (Entity) | Dùng cho |
| --- | --- | --- |
| `uuid` | `UUID` | Khoá chính, khoá ngoại |
| `text` | `String` | Chữ, không giới hạn độ dài |
| `varchar(n)` | `String` | Chữ có giới hạn độ dài |
| `smallint` | `Short` | Số nhỏ (-32768 tới 32767) |
| `integer` | `Integer` / `int` | Số nguyên |
| `bigint` | `Long` / `long` | Số nguyên lớn |
| `boolean` | `Boolean` / `boolean` | Đúng / sai |
| `date` | `LocalDate` | Ngày, không có giờ |
| `timestamptz` | `Instant` / `OffsetDateTime` | Thời điểm, có múi giờ |
| `timestamp` | `LocalDateTime` | Thời điểm, **không** có múi giờ. Sổ Sói không dùng. |

- Kiểu wrapper (`Integer`, `Short`) nhận được `null`. Kiểu primitive (`int`, `short`) thì không. Cột cho phép `NULL` thì dùng wrapper.
- `@Version` dùng `Long` / `long` hoặc `Integer` / `int`.
- FK không phải là kiểu dữ liệu. FK là một cột kiểu `uuid` có gắn thêm ràng buộc `FOREIGN KEY`.

### 4. Ràng buộc (constraint)

Luật do database tự kiểm, code không lách qua được.

| Ràng buộc | Ý nghĩa |
| --- | --- |
| `NOT NULL` | Không được bỏ trống |
| `UNIQUE` | Không được trùng. Áp cho một cột, hoặc cho một tổ hợp cột (`UNIQUE (a, b)`): chỉ cặp giá trị không được trùng, từng cột riêng lẻ thì trùng được |
| `PRIMARY KEY` | `UNIQUE` + `NOT NULL`, dùng để xác định một dòng. Một bảng chỉ có một PK; PK có thể gồm nhiều cột (composite key) |
| `FOREIGN KEY` | Giá trị phải tồn tại ở bảng được tham chiếu |
| `CHECK` | Giá trị phải thoả một điều kiện |

Mermaid `erDiagram` chỉ đánh dấu được `PK`, `FK`, `UK` trên từng cột. Unique theo tổ hợp cột thì ghi bằng chữ dưới sơ đồ.

### 5. Mỗi cột một nghĩa

Hai cột cùng lưu một thông tin thì sớm muộn sẽ lệch nhau. Mỗi thông tin chỉ lưu ở một chỗ.

### 6. Mermaid `erDiagram`

- Thuộc tính viết theo thứ tự `kiểu tên [PK|FK|UK] ["ghi chú"]`.
- Tên cột trong PostgreSQL dùng `snake_case`.
- Đường nối viết liền, không có dấu cách: `A ||--o{ B : "nhãn"`.
- Mỗi đầu nối có hai ký tự. Ký tự **sát bảng** là số lượng tối đa (`|` một, `{` `}` nhiều). Ký tự **phía đường nối** là số lượng tối thiểu (`o` không, `|` một).

| Ký hiệu | Nghĩa |
| --- | --- |
| `\|\|` | Đúng một |
| `\|o` / `o\|` | Không hoặc một |
| `\|{` / `}\|` | Một hoặc nhiều |
| `o{` / `}o` | Không hoặc nhiều |
