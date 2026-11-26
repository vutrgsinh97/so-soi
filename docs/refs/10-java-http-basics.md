# Java và HTTP cơ bản

Dùng cho: Ngày 1–6, 10–12

Bảng tra do Claude soạn.

## 1. Java

**Class và object.** Class là bản vẽ, object là cái nhà xây từ bản vẽ đó. Một class tạo được nhiều object.

- **Field**: biến nằm trong class, giữ dữ liệu của object (ví dụ `title`, `pages` của một cuốn sách).
- **Constructor**: hàm đặc biệt, cùng tên class, chạy khi `new`. Dùng để gán giá trị đầu cho field.
- **Method**: hàm nằm trong class, làm một việc với dữ liệu của object.
- **`static`**: thuộc về class, không thuộc object nào. Gọi bằng tên class, không cần `new`.
- **Access modifier**: `private` chỉ class đó thấy, `public` ai cũng thấy. Field thường `private`, method cần gọi từ ngoài thì `public`.

```java
Book b = new Book("Sách A", 120);   // new gọi constructor
int n = b.getPages();               // gọi method
```

**Wrapper và primitive.** `int`, `long`, `boolean` là primitive: nhanh, không thể là `null`. `Integer`, `Long`, `Boolean` là wrapper: là object, được phép `null`. Cột database cho phép trống thì field Java nên dùng wrapper. Xem thêm [00-data-modeling.md, mục 3](00-data-modeling.md#3-kiểu-dữ-liệu).

**Interface và implementation.** Interface chỉ khai báo "làm được gì", class implementation viết "làm thế nào".

```java
interface Greeter { String greet(); }
class VnGreeter implements Greeter { public String greet() { return "Chào"; } }
```

Spring Data JPA khác thường ở chỗ: anh chỉ viết interface Repository, Spring tự sinh phần implementation.

**Generics.** Dấu `<T>` cho biết "chứa kiểu gì". `List<Book>` là danh sách các `Book`. `Optional<Book>` là hộp có thể chứa một `Book` hoặc rỗng, thay cho việc trả `null`.

```java
Optional<Book> found = repo.findById(1L);
Book b = found.orElseThrow();   // rỗng thì ném exception
```

**`record`.** Class ngắn gọn, chỉ giữ dữ liệu, field không đổi được. Java tự sinh constructor và getter.

```java
record BookDto(String title, int pages) {}
```

**Exception.** Khi có lỗi, code `throw` một exception. Chỗ khác `try/catch` để bắt và xử lý.

- **Unchecked** (kế thừa `RuntimeException`): không bắt buộc `try/catch`. Hay dùng cho lỗi nghiệp vụ, ví dụ "không tìm thấy".
- **Checked** (ví dụ `IOException`): trình biên dịch bắt buộc `try/catch` hoặc khai báo `throws`.
- Spring có thể gom exception ném ra từ Controller về một chỗ: xem [04-validation-errors.md, mục 2](04-validation-errors.md#2-xử-lý-lỗi-tập-trung).

```java
try { risky(); } catch (IllegalStateException e) { /* xử lý */ }
```

**Annotation.** Dấu `@` là nhãn dán lên class, field hoặc method để framework biết cách xử lý. Nhãn không tự chạy gì, framework đọc nhãn rồi mới hành động. Ví dụ `@GetMapping` nói "hàm này nhận `GET`". Xem bảng thuật ngữ trong [README.md](README.md).

## 2. HTTP

**Request** gồm 4 phần: method, path, headers, body.

```
POST /books HTTP/1.1
Content-Type: application/json
{"title": "Sách A", "pages": 120}
```

| Method | Dùng để | Idempotent? |
| --- | --- | --- |
| `GET` | Lấy dữ liệu | Có |
| `POST` | Tạo mới | Không (gọi hai lần tạo hai cái) |
| `PUT` | Cập nhật | Có |
| `DELETE` | Xoá | Có |

**Idempotent**: gọi nhiều lần cho kết quả cuối giống gọi một lần.

**Status code** là con số server trả về để báo kết quả:

| Nhóm | Nghĩa | Mã hay gặp |
| --- | --- | --- |
| 2xx | Thành công | `200` OK, `201` Created (tạo xong), `204` No Content (xong, không có body) |
| 4xx | Client gửi sai | `400` dữ liệu sai, `404` không thấy, `409` xung đột (ví dụ trùng) |
| 5xx | Server lỗi | `500` lỗi không lường trước |

**JSON** là chữ có cấu trúc. Object trong `{}`, danh sách trong `[]`, cặp `"khoá": giá trị`.

```json
{"title": "Sách A", "pages": 120, "tags": ["java", "học"]}
```

**Header** là thông tin kèm theo request hoặc response:

- `Content-Type: application/json`: body đang là JSON.
- `Authorization: Bearer <token>`: gửi token để chứng minh mình là ai (xem [06-security-jwt.md](06-security-jwt.md)).

**Path variable và query param:**

- Path variable: một phần của đường dẫn, chỉ định **cái nào**: `GET /books/5` (sách số 5).
- Query param: sau dấu `?`, lọc hoặc chia trang: `GET /books?page=0&size=10`.

## 3. curl

`curl` là lệnh gửi request HTTP từ terminal.

| Cờ | Làm gì |
| --- | --- |
| `-i` | In cả status và header của response |
| `-X <METHOD>` | Chọn method (`GET`, `POST`, `PUT`, `DELETE`) |
| `-H "..."` | Thêm header |
| `-d '...'` | Gửi body (có `-d` thì curl tự dùng `POST`) |

```bash
curl -i http://localhost:8080/books/1
curl -i -X POST http://localhost:8080/books -H "Content-Type: application/json" \
  -d '{"title": "Sách A", "pages": 120}'
```

Đọc dòng đầu của kết quả (ví dụ `HTTP/1.1 201`) để biết status trước khi đọc body.

## 4. Link

- [Java tutorial (dev.java)](https://dev.java/learn/)
- [MDN: HTTP overview](https://developer.mozilla.org/en-US/docs/Web/HTTP/Overview)
- [MDN: HTTP status codes](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status)
- [curl man page](https://curl.se/docs/manpage.html)
