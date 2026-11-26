# Spring Security, BCrypt, JWT và phân quyền

Dùng cho: Ngày 10 (đăng ký, BCrypt), Ngày 11 (JWT, "sổ của ai người nấy thấy").

## 1. Spring Security hoạt động thế nào

Hình dung Spring Security như **dãy bảo vệ đứng ở cổng**, trước khi request vào tới Controller. Mỗi bảo vệ (gọi là **Filter**) kiểm tra một việc, ví dụ đọc token, kiểm tra đăng nhập, chặn request lạ.

- **Filter**: đoạn code chạy trước Controller. Nó có thể cho request đi tiếp hoặc chặn lại ngay.
- **SecurityFilterChain**: danh sách các filter, và là nơi cấu hình đường dẫn nào cần đăng nhập, đường dẫn nào công khai (ví dụ `/auth/**` công khai).
- **Authentication** (xác thực) = "bạn là ai?". **Authorization** (phân quyền) = "bạn được làm gì?".
- **UserDetailsService**: interface tự viết để Spring biết cách tìm user trong database theo username.
- **SecurityContext**: nơi lưu thông tin user đã đăng nhập trong suốt một request. Controller có thể lấy user hiện tại từ đây.

## 2. Mã hoá mật khẩu bằng BCrypt

- Không bao giờ lưu mật khẩu dạng chữ thường. BCrypt biến mật khẩu thành chuỗi băm, không thể dịch ngược.
- BCrypt tự thêm **salt** (chuỗi ngẫu nhiên) nên cùng một mật khẩu băm hai lần sẽ ra hai kết quả khác nhau. Điều này chống được việc tra bảng mật khẩu có sẵn.
- Khi đăng nhập, không "giải mã" mà dùng `passwordEncoder.matches(raw, hash)` để so khớp.
- Chuỗi băm BCrypt dài 60 ký tự, nên cột `password_hash` trong database phải đủ dài.

## 3. JWT

**JWT** (JSON Web Token) giống như **vé vào cổng có đóng dấu**. Server phát vé khi đăng nhập thành công, sau đó client đưa vé mỗi lần gọi API. Server chỉ cần kiểm tra con dấu, không cần lưu session.

JWT gồm 3 phần, ngăn cách bởi dấu chấm `xxxxx.yyyyy.zzzzz`:

1. **Header**: thuật toán ký, ví dụ HS256.
2. **Payload**: thông tin (gọi là **claims**), ví dụ `sub` (account id), `exp` (thời điểm hết hạn), `roles`.
3. **Signature**: chữ ký tạo từ header, payload và secret key. Ai sửa payload thì chữ ký sẽ không khớp nữa.

Điều quan trọng nhất: JWT chỉ được **ký**, không được **mã hoá**. Ai cũng đọc được payload (thử dán vào jwt.io). Vì vậy **không bao giờ để mật khẩu hay dữ liệu nhạy cảm trong payload**.

Luồng hoạt động:

1. Client gửi username và password tới `POST /auth/login`.
2. Server kiểm tra đúng thì tạo JWT, ký bằng secret key, trả về cho client.
3. Client gửi kèm header `Authorization: Bearer <token>` ở mọi request sau.
4. Filter của Spring Security kiểm tra chữ ký và hạn dùng. Hợp lệ thì cho đi tiếp, sai thì trả 401.

Spring Security có sẵn phần **Resource Server** để tự kiểm tra JWT, không cần tự viết filter đọc token. Chỉ cần cung cấp `JwtDecoder` (để kiểm tra) và dùng `JwtEncoder` (để tạo token lúc login). Nên để access token sống ngắn, ví dụ 15–60 phút.

## 4. Phân quyền

- **Role** là nhóm quyền, ví dụ `USER`, `ADMIN`. Spring quy ước role có tiền tố `ROLE_`, ví dụ `ROLE_ADMIN`.
- Phân quyền theo URL: cấu hình trong `SecurityFilterChain`, ví dụ `/admin/**` chỉ cho `ADMIN`.
- Phân quyền theo hàm (**method security**): bật `@EnableMethodSecurity`, rồi gắn lên hàm Service hoặc Controller.
- `@PreAuthorize("hasRole('ADMIN')")`: kiểm tra **trước** khi chạy hàm. Không đủ quyền thì trả 403.
- Lưu ý: Spring Boot **không tự bật** method security, phải tự thêm `@EnableMethodSecurity`.

Với yêu cầu "account chỉ sửa tờ của profile mình", cách dễ hiểu nhất là kiểm tra trong Service: lấy account hiện tại, so với chủ của profile chứa tờ đó, không khớp thì ném exception. Demo gốc trả 403; Sổ Sói (roadmap Ngày 11) chọn trả 404 để không lộ là tờ đó có tồn tại.

Docs: [Spring Security Architecture](https://docs.spring.io/spring-security/reference/servlet/architecture.html) · [Password Encoding with BCrypt](https://www.baeldung.com/spring-security-registration-password-encoding-bcrypt) · [Introduction to JSON Web Tokens](https://jwt.io/introduction) · [OAuth 2.0 Resource Server JWT](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html).
