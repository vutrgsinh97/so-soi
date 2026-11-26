# Validation và xử lý lỗi

Dùng cho: Ngày 4 (validation, `GlobalExceptionHandler`). Validation ở mức Entity và database constraint xem `02-jpa-persistence.md`, mục 5.

## 1. Validation

**Validation** là kiểm tra dữ liệu client gửi lên có hợp lệ không, trước khi xử lý.

- Gắn ràng buộc lên field của DTO: `@NotBlank` (không rỗng), `@Size(max = 280)`, `@Email`...
- Thêm `@Valid` trước tham số `@RequestBody`. Nếu dữ liệu sai, Spring ném `MethodArgumentNotValidException`.
- Khác nhau: `@NotNull` cho phép chuỗi rỗng, `@NotEmpty` cấm rỗng nhưng cho phép toàn dấu cách, `@NotBlank` cấm cả hai. Với text, thường dùng `@NotBlank`.

## 2. Xử lý lỗi tập trung

Thay vì `try-catch` ở mọi Controller, viết **một class** gắn `@RestControllerAdvice`. Mọi exception ném ra từ bất kỳ Controller nào đều chạy về đây.

- Mỗi loại lỗi có một hàm `@ExceptionHandler` riêng, ví dụ `TicketNotFoundException` trả 404, lỗi validation trả 400.
- Nhờ vậy mọi lỗi trả về cùng một định dạng JSON, frontend xử lý dễ hơn.

Hình dạng một handler:

```java
@ExceptionHandler(TicketNotFoundException.class)
@ResponseStatus(HttpStatus.NOT_FOUND)
public Map<String, String> handleNotFound(TicketNotFoundException ex) { ... }
```

Docs: [Validation in Spring Boot](https://www.baeldung.com/spring-boot-bean-validation) · [Error Handling for REST with Spring](https://www.baeldung.com/exception-handling-for-rest-with-spring).
