# Test: unit test và integration test

Dùng cho: Ngày 12 (test).

## 1. Unit test với Mockito

**Unit test** kiểm tra **một class riêng lẻ**, không chạy Spring, không đụng database, nên chạy rất nhanh.

- **Mock** là object giả. Ví dụ khi test `TicketService`, không dùng `TicketRepository` thật mà dùng bản giả để tự quy định nó trả về gì.
- `@ExtendWith(MockitoExtension.class)`: bật Mockito cho class test.
- `@Mock`: tạo object giả, ví dụ `TicketRepository`.
- `@InjectMocks`: tạo object thật cần test (`TicketService`) và tự gắn các mock vào.
- `when(repo.findById(id)).thenReturn(Optional.of(ticket))`: dặn mock trả về gì.
- `verify(repo).save(any())`: kiểm tra hàm có thật sự được gọi không.

Mỗi test theo 3 bước **Given – When – Then**: chuẩn bị dữ liệu, gọi hàm cần test, kiểm tra kết quả.

## 2. Integration test

**Integration test** kiểm tra nhiều phần chạy cùng nhau, có Spring thật và có thể có database thật.

- `@WebMvcTest(TicketController.class)`: chỉ khởi động **tầng web** (Controller, validation, xử lý lỗi), không có Service hay database thật. Service được thay bằng mock (`@MockitoBean`).
- **MockMvc**: giả lập gửi request HTTP vào Controller mà không cần mở server thật, rồi kiểm tra status và JSON trả về.
- `@SpringBootTest`: khởi động **toàn bộ ứng dụng**. Chậm hơn nhưng giống thực tế hơn.
- **Testcontainers**: tự bật một Postgres thật trong Docker khi chạy test và tự tắt khi xong. Test trên database thật đáng tin hơn H2.
- Từ Spring Boot 3.1, chỉ cần gắn `@ServiceConnection` lên container, Spring tự cấu hình kết nối.

## 3. Chọn loại test nào

- Logic trong Service: unit test.
- API trả đúng status và JSON (400, 404, 409): `@WebMvcTest`.
- Cả luồng thật (đăng ký, đăng nhập, tạo tờ, lấy lại): `@SpringBootTest` với Testcontainers.

Lưu ý từ bài Ngày 2: `BUILD SUCCESS` chỉ chứng minh code compile, không chứng minh mọi quy tắc database đúng. Kiểm tra concurrency cần hai transaction độc lập, xem `02-jpa-persistence.md`, mục 12.

Docs: [Mockito and JUnit 5](https://www.baeldung.com/mockito-junit-5-extension) · [Testing a Spring Boot REST API with Testcontainers](https://testcontainers.com/guides/testing-spring-boot-rest-api-using-testcontainers/).
