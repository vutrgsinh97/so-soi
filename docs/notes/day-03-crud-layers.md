# Spring Boot — Day 03: CRUD theo tầng

> Sườn do Nguyệt Nhi dựng. Phần kiến thức Lang tự điền. Không có code giải sẵn.
> Link nào 404 thì tìm lại trên trang docs chính, đừng lấy link lạ.

## 0. Mục tiêu và phạm vi

- [ ] Viết lại bằng lời của mình: hết Ngày 3, app làm được gì?
- Trong phạm vi: Controller, Service, Repository; 5 endpoint `/tickets`; DTO `TicketRequest`, `TicketResponse` bằng `record`.
- Ngoài phạm vi (để ngày sau): validation (Ngày 4), một ngày một tờ (Ngày 5), phân trang (Ngày 6), đăng nhập (Ngày 10–11).

Tự hỏi trước khi đọc:
- Ngày 2 đã có Entity + Repository. Vậy còn thiếu gì để người ngoài gọi được vào app?
> Answer: Chưa có api để FE, postman,...

## 1. Luồng một request đi qua app

- [ ] Vẽ luồng bằng Mermaid (`flowchart LR`): `curl` → Controller → Service → Repository → DB, rồi đường trả về.
- [x] Mỗi tầng: nó **được làm** gì, **không được làm** gì?

| Tầng | Được làm | Không được làm |
| --- | --- | --- |
| Controller | nhận HTTP request, tham số| xử lý logic, thao tác liên quan tới database, không quản lý transaction|
| Service | nhận tham số, xử lý logic, điều phối repository, quản lý transaction| không nhận HTTP request trực tiếp, không viết sql trực tiếp |
| Repository | các thao tác liên quan tới database | nghiệp vụ, điều phối, gọi service, http request trực tiếp|
> Thêm: transaction nằm ở Service và test service thì dễ hơn

Tự hỏi:
- Vì sao Controller không gọi thẳng Repository?
> có thể gọi thẳng nhưng không nên, sẽ bị rối sau này nghiệp vụ lẫn lộn, nếu sau này thêm api hoặc các api có logic nghiệp vụ liên quan thì không dùng lại đc.
- Logic "tờ này thuộc profile nào" nên nằm ở tầng nào?
> thuộc về service vì service biết đc tờ này thuộc profile nào mới điều phối được thao tác

Docs:
- [Spring Framework — Annotated Controllers](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller.html)
- [Spring Guide — Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

## 2. Tổ chức folder cho Ngày 3

- Ngày 2 đang chia theo tầng (Package by Layer).
- [ ] Ngày 3 thêm những package / file nào? Liệt kê cây thư mục dự kiến.
- [ ] Ghi một dòng: giữ Package by Layer hay đổi sang Package by Feature, vì sao.

## 3. DTO và `record`
> Hệ thống quản lý gì -> Entity
> Dữ liệu gì được truyền qua lại -> DTO
> Biểu diễn (code) như thế nào -> record

- [x] DTO là gì? Khác Entity chỗ nào?
> DTO như 1 dạng dữ liệu thôi nhưng nó dùng để trao đổi 'thông tin' trong khi Entity là đối tượng chứa dữ liệu thật
- [x] Vì sao không trả Entity ra ngoài? (gợi ý tự tìm: lộ field, vòng lặp quan hệ, lazy loading, đổi DB thì đổi API)
> Entity là đối tượng thật và có các field nhạy cảm như passwordHash, bên cạnh đó còn có các fields không dùng tới. Dùng DTO để tránh lộ + chỉ truyền những fields cần thiết
- [x] `record` trong Java là gì, khác `class` chỗ nào? Có setter không?
> Điều dùng để khai báo đối tượng, record là 1 class đặc biệt, không có 1 setter. Ví dụ như class là 1 tờ giấy ghi số lượng order, có thể thay đổi, còn record thì như biên lai thu tiền thôi, mún tạo 1 giao dịch khác thì phải tạo thêm 1 biên lai khác
- [x] `TicketRequest` có những field nào? `TicketResponse` có những field nào? Field nào chỉ có ở một bên, vì sao?

> Thêm: vòng lặp quan hệ (quan hệ hai chiều thì JSON bị gì?) và lazy loading (lỗi LazyInitializationException)
> Circular Reference	Dữ liệu tham chiếu qua lại, đọc mãi không hết.
> Lazy Loading	Khi nào cần dữ liệu thì mới tải.
> LazyInitializationException	Muốn tải dữ liệu nhưng Session đã đóng.

| Field | Entity `Ticket` | `TicketRequest` | `TicketResponse` |
| --- | --- | --- | --- |
| id | | | |
| profile | | | |
| created_at / updated_at | | | |
| version | | | |
| (các field nội dung) | | | |

Docs:
- [Java — Record Classes](https://docs.oracle.com/en/java/javase/21/language/records.html)
- [Baeldung — Entity vs DTO](https://www.baeldung.com/java-entity-vs-dto)

## 4. Mapping Entity ↔ DTO

- [ ] Ai làm việc chuyển đổi: Controller, Service, hay một chỗ riêng (mapper)? Chọn và ghi lý do.
- [ ] Khi `PUT`, cập nhật Entity cũ hay tạo Entity mới? Liên quan gì tới `@Version` ở Ngày 2?

## 5. Năm endpoint

| Method | Path | Body vào | Trả ra | Status thành công | Status khi không tìm thấy |
| --- | --- | --- | --- | --- | --- |
| GET | `/tickets` | | | | |
| GET | `/tickets/{id}` | | | | |
| POST | `/tickets` | | | | |
| PUT | `/tickets/{id}` | | | | |
| DELETE | `/tickets/{id}` | | | | |

Tự hỏi:
- `POST` thành công nên trả 200 hay 201? Có header `Location` không?
- `DELETE` thành công trả gì?
- Không tìm thấy id thì Ngày 3 xử lý tạm thế nào? (xử lý lỗi đẹp là Ngày 4)
- `@PathVariable`, `@RequestBody`, `ResponseEntity` dùng khi nào?

Docs:
- [Spring Framework — ResponseEntity](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods/responseentity.html)
- [MDN — HTTP response status codes](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status)

## 6. Service và `@Transactional`

- [ ] `@Transactional` làm gì? Đặt ở Service hay Controller?
- [ ] `readOnly = true` dùng cho method nào?
- [ ] Tạm gắn mọi tờ với profile tạo tay ở Ngày 2: lấy `profile_id` đó từ đâu cho gọn (hằng số, config)? Ghi lý do, và ghi luôn "Ngày 11 sẽ thay bằng JWT".

Docs:
- [Spring Framework — Using @Transactional](https://docs.spring.io/spring-framework/reference/data-access/transaction/declarative/annotations.html)

## 7. Chuẩn bị dữ liệu: INSERT tay bằng psql (ô còn lại của Ngày 2)

- [ ] Lệnh vào container và mở `psql`.
- [ ] Xem cấu trúc bảng `account`, `profile` (`\d`), cột nào `NOT NULL`, có default không.
- [ ] Thứ tự insert và vì sao.
- [ ] Ghi các lệnh mới vào `docs/notes/terminal.md`.

## 8. Thử bằng curl

- [ ] Viết 5 lệnh `curl` cho 5 endpoint (có `-i`, `-H "Content-Type: application/json"`, `-d`).
- [ ] Ghi lại status + body thật mình nhận được.
- [ ] Thử một ca sai: id không tồn tại, body thiếu field. App trả gì? (để dành sửa ở Ngày 4)

Docs:
- [curl — man page](https://curl.se/docs/manpage.html)

## 9. Lỗi đã gặp

| Lỗi | Thông báo | Nguyên nhân | Cách sửa |
| --- | --- | --- | --- |
| | | | |

## 10. Tự kiểm tra cuối buổi

- [ ] Giải thích cho một người lạ trong 3 câu: vì sao tách 3 tầng.
- [ ] Không nhìn code: kể đường đi của `POST /tickets` từ lúc `curl` tới lúc có dòng trong DB.
- [ ] Commit (ít nhất một lần) + note tiếng Anh `docs/notes/YYYY-MM-DD.md`: `## Done`, `## Next`, `## Stuck`.
