# Chia tầng và DTO

Dùng cho: Ngày 3 (CRUD theo tầng).

## 1. Ba tầng

Code được chia thành 3 tầng, mỗi tầng một việc:

1. **Controller**: nhận request, đọc tham số, trả response. Không chứa logic nghiệp vụ.
2. **Service**: chứa logic nghiệp vụ, ví dụ "mỗi profile chỉ có một tờ một ngày".
3. **Repository**: chỉ đọc và ghi database.

## 2. DTO

**DTO** (Data Transfer Object) là class dùng riêng để nhận và trả dữ liệu qua API. Vì sao không trả thẳng Entity?

- Tránh lộ dữ liệu nhạy cảm, ví dụ trả cả field `passwordHash` của Account.
- Tránh lỗi vòng lặp JSON khi hai Entity tham chiếu lẫn nhau.
- Đổi cấu trúc database không làm vỡ API mà frontend đang dùng.

Thường có 2 loại: `TicketRequest` (dữ liệu client gửi lên) và `TicketResponse` (dữ liệu trả về). Với Java 17+, dùng `record` cho DTO rất gọn.

## 3. Quy ước REST cơ bản

- `GET /tickets`: lấy danh sách.
- `GET /tickets/{id}`: lấy một cái.
- `POST /tickets`: tạo mới, trả 201.
- `PUT /tickets/{id}`: cập nhật.
- `DELETE /tickets/{id}`: xoá, trả 204.

Docs: [Building REST services with Spring](https://spring.io/guides/tutorials/rest/) · [Entity vs DTO](https://www.baeldung.com/java-entity-vs-dto).

## 4. Tự hỏi trước khi làm endpoint

Chọn cái nào trước:
1. Cái nào **tạo** dữ liệu, cái nào **cần** dữ liệu có sẵn? Làm cái tạo trước.
2. Làm xong kiểm bằng gì (`curl`, `psql`)? Chưa trả lời được thì chưa phải bước tiếp theo.
3. Bước nhỏ nhất chạy được: đi trọn một đường request → DB → response, không làm rộng cả một tầng.

Làm một endpoint:
4. Vào là gì, ra là gì: method, URL, body, status trả về.
5. Mỗi field của Entity do ai lo: client gửi, server tự tính, hay DB/Hibernate tự sinh?
6. Mỗi tầng đổi dữ liệu thế nào: DTO → Entity → lưu → Entity → Response DTO.
7. Cái gì có thể sai (không thấy, trùng, sai dữ liệu)? Ghi ra trước, xử lý sau.
