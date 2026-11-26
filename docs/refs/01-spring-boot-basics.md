# Spring Boot cơ bản

Dùng cho: Ngày 1 (khởi tạo project, `GET /ping`). Phần Lombok và Maven dùng từ Ngày 2.

## 1. Spring Boot chạy thế nào

Hình dung Spring Boot như một **quản lý nhà hàng**. Anh chỉ viết các "món" (class), còn nó tự lo tạo đối tượng, nối chúng với nhau và mở cửa đón khách (chạy server Tomcat ở cổng 8080).

- `@SpringBootApplication`: đánh dấu điểm bắt đầu. Spring sẽ quét toàn bộ package bên dưới để tìm các class có annotation.
- `@RestController`: class này nhận request HTTP và trả về **dữ liệu** (JSON), không trả về trang HTML.
- `@GetMapping("/ping")`: khi có request `GET /ping` thì gọi hàm này.
- Trả về một object Java, Spring tự chuyển nó thành JSON. Không cần tự viết code chuyển đổi.
- `application.yml`: file cấu hình (cổng, database, log...). Lợi ích là đổi cấu hình mà không phải sửa code.

Hình dạng một controller (ví dụ của demo):

```java
@GetMapping("/hello")
public Map<String, String> hello() {
    return Map.of("message", "Hello");
}
```

## 2. Lombok và Maven

Lombok giảm boilerplate code bằng cách xử lý annotation trong quá trình biên dịch.

| Annotation | Chức năng | Khi dùng với Entity |
|---|---|---|
| `@Getter` | Sinh getter | Nên dùng. |
| `@Setter` | Sinh setter | Dùng chọn lọc, tránh cho phép sửa ID/version/timestamp tùy ý. |
| `@NoArgsConstructor` | Constructor không tham số | JPA cần constructor không tham số, `public` hoặc `protected`. |
| `@AllArgsConstructor` | Constructor cho tất cả field | Không bắt buộc; có thể đưa cả field tự sinh vào constructor. |
| `@Builder` | Khởi tạo theo Builder Pattern | Tùy nhu cầu; vẫn bảo đảm constructor JPA. |
| `@ToString` | Sinh `toString()` | Cẩn thận với quan hệ, lazy loading và dữ liệu nhạy cảm. |
| `@Data` | Getter, setter, equals/hashCode, toString… | Không nên dùng mặc định cho Entity. |

Tên đúng là `@AllArgsConstructor`, không phải `@AllArgument`.

### Thêm dependency trong pom.xml

Chèn bên trong `<dependencies>` đang có (không tạo một block `<dependencies>` thứ hai):

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <scope>provided</scope>
</dependency>
```

Project hiện có `spring-boot-starter-parent` và `<java.version>21</java.version>`. Nếu parent quản lý phiên bản Lombok thì không cần khai báo `<version>`. Kiểm tra phiên bản thực tế bằng Maven thay vì giả định số phiên bản:

```bash
./mvnw dependency:tree -Dincludes=org.projectlombok:lombok
./mvnw -v
```

`provided`: thư viện có trên compile/test classpath, không thuộc runtime classpath thông thường của Maven. Lombok chủ yếu thực hiện công việc lúc compile.

### Vì sao đôi khi cần cấu hình build?

- `dependencies`: Project cần thư viện nào?
- `build/plugins`: Project được biên dịch, kiểm thử, đóng gói như thế nào?
- `annotationProcessorPaths`: Java Compiler tìm annotation processor ở đâu?

Với JDK 21, project không dùng `module-info.java` và annotation processing không bị vô hiệu hóa, thông thường chỉ cần dependency. Từ JDK 23, hoặc khi compile theo Java modules từ JDK 9, cần kích hoạt/cấu hình processor tường minh theo hướng dẫn Lombok. `<java.version>21</java.version>` không đảm bảo Maven thật sự chạy trên JDK 21, vì vậy kiểm tra `./mvnw -v`.

Nếu cần, thêm plugin này vào `<build><plugins>` hiện có, giữ nguyên `spring-boot-maven-plugin`:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <annotationProcessorPaths>
            <path>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```

Nếu không có dependency management phù hợp thì khai báo version tương ứng ở dependency và processor path. Khi project có thêm processor (ví dụ MapStruct), nhớ giữ cả các processor cần thiết trong cấu hình.

### VSCode không suggest Lombok

1. Cài Extension Pack for Java / Language Support for Java by Red Hat.
2. Mở đúng thư mục chứa `pom.xml`; đợi Java import Maven project.
3. Chạy `./mvnw clean compile` và kiểm tra lỗi.
4. Mở Command Palette (`Cmd + Shift + P`) → thử `Java: Reload Projects` (nếu có).
5. Nếu chưa được: `Java: Clean Java Language Server Workspace` → khởi động lại.
6. Kiểm tra settings:

```json
{
  "java.jdt.ls.lombokSupport.enabled": true,
  "java.configuration.updateBuildConfiguration": "automatic"
}
```

Nếu Maven compile thành công nhưng IDE vẫn báo lỗi, khả năng cao là vấn đề import project/Java Language Server. Nếu compile lỗi, xử lý dependency hoặc processor trước. VSCode Java đã tích hợp hỗ trợ Lombok, thông thường không cần extension Lombok riêng.

Docs: [Lombok — Maven Setup](https://projectlombok.org/setup/maven) · [Lombok Features](https://projectlombok.org/features/) · [Java Extension by Red Hat](https://marketplace.visualstudio.com/items?itemName=redhat.java).

