# Sổ lỗi

Ký hiệu: ✅ đã sửa · ⏳ chưa sửa

## Ngày 1

### 1. ⏳ Chưa `git init`

- **Lỗi**: thư mục `so-soi/` chưa phải git repo (`git status` báo `not a git repository`). Roadmap Ngày 1 mục đầu tiên.
- **Vì sao không nên**: không có commit thì không quay lại được bản chạy được gần nhất, không xem `git diff` để biết mình vừa đổi gì. Giống viết nhật ký bằng bút chì rồi tẩy liên tục: hôm nay sai, không còn bản hôm qua để so.
- **Cách thay thế**: `git init` ở gốc `so-soi/`, commit đầu tiên. `sosoi/.gitignore` của start.spring.io đã bỏ qua `target/`. Kiểm lại bằng `git status` trước khi commit, đừng để `target/` lọt vào.
- Docs: [git init](https://git-scm.com/docs/git-init)

### 2. ✅ `/ping` trả `pong`, yêu cầu là `ok`

- **Lỗi**: [GreetingController.java:13](../../sosoi/src/main/java/com/example/sosoi/GreetingController.java#L13) trả `{"status":"pong"}`. Roadmap ghi `{ "status": "ok" }`.
- **Vì sao không nên**: endpoint kiểu này thường để máy khác gọi kiểm tra (health check). Máy so chữ chính xác, lệch một chữ là báo app chết. Giống hẹn mật khẩu "trăng" mà tới cửa lại nói "trời".
- **Cách thay thế**: đổi giá trị trả về cho đúng yêu cầu.

### 3. ✅ Mật khẩu database viết thẳng trong file

> Đã chuyển sang `.env`. Còn một việc lúc `git init`: thêm `.env` vào `.gitignore` ở gốc `so-soi/` (`sosoi/.gitignore` không phủ tới gốc).

- **Lỗi**: [docker-compose.yml:7-8](../../docker/docker-compose.yml#L7-L8) và [application.yaml:6-7](../../sosoi/src/main/resources/application.yaml#L6-L7) đều ghi `admin`/`admin`.
- **Vì sao không nên**: chạy trên máy mình thì chưa sao, nhưng commit lên git là mật khẩu nằm trong lịch sử mãi mãi, xoá file cũng không mất. Giống dán mật khẩu wifi lên cửa nhà: ở trong nhà thì tiện, ai đi ngang cũng đọc được.
- **Cách thay thế**:

| Cách | Dùng khi nào |
| --- | --- |
| File `.env` cạnh compose, trong compose ghi `${POSTGRES_PASSWORD}`, thêm `.env` vào `.gitignore` | Docker, máy cá nhân |
| `application.yaml` ghi `${DB_PASSWORD}`, giá trị lấy từ biến môi trường | Spring, mọi môi trường |
| Giữ `admin` nhưng ghi rõ "chỉ dùng local" | Tạm thời, tới Ngày 13 tách `application-dev.yml` / `application-prod.yml` |

- Docs: [Compose: environment variables](https://docs.docker.com/compose/how-tos/environment-variables/), [Spring Boot: externalized configuration](https://docs.spring.io/spring-boot/reference/features/external-config.html)

### 4. ✅ Cổng Postgres mở cho mọi mạng

- **Lỗi**: [docker-compose.yml:11](../../docker/docker-compose.yml#L11) `"5432:5432"` mở cổng trên mọi card mạng của máy, kể cả wifi quán cà phê.
- **Vì sao không nên**: người cùng mạng gõ đúng IP máy anh là thử đăng nhập được, mà mật khẩu đang là `admin`. Giống để cửa nhà mở ra hẻm chứ không chỉ ra phòng khách.
- **Cách thay thế**: `"127.0.0.1:5432:5432"`, chỉ máy mình gọi được.
- Docs: [Compose: ports](https://docs.docker.com/reference/compose-file/services/#ports)

> Lần sửa 1: [docker-compose.yml:13](../../docker-compose.yml#L13) đổi thành `"${DB_HOST}:5432"`, nhưng `.env` chưa có `DB_HOST`, nên compose báo biến rỗng. Container đang chạy vẫn là bản cũ (`0.0.0.0:5432`), vì chưa `up -d` lại. Biến này giữ cả IP lẫn cổng mà tên là `HOST` thì dễ hiểu lầm; viết thẳng `"127.0.0.1:5432:5432"` là đủ, IP này không phải bí mật.

### 6. ✅ `env_file` trỏ sai chỗ

- **Lỗi**: [docker-compose.yml:6-7](../../docker-compose.yml#L6-L7) ghi `../.env`. Đường dẫn tính từ thư mục chứa compose, nên nó tìm `learning/.env`, không có. `docker compose config` báo `env file ... not found`, lần `up` tới sẽ hỏng.
- **Vì sao không nên**: dời file mà quên đổi đường dẫn. Giống chuyển nhà mà vẫn ghi địa chỉ cũ trên thư.
- **Cách thay thế**:

| Cách | Dùng khi nào |
| --- | --- |
| Bỏ hẳn `env_file`. Compose tự đọc `.env` **cạnh compose** để điền `${DB_USER}`, `${DB_PASSWORD}` | Như bây giờ: chỉ cần biến để điền vào compose |
| Giữ `env_file`, sửa thành `.env` | Muốn đẩy **mọi** biến trong file vào container |

- Docs: [Compose: environment variables](https://docs.docker.com/compose/how-tos/environment-variables/)

> Lần sửa 1: compose dời về `docker/`, nên `../.env` giờ trỏ đúng `so-soi/.env`, container nhận được biến. Nhưng `env_file` chỉ **đẩy biến vào container**. Còn `${DB_USER}`, `${DB_PASSWORD}`, `${DB_HOST}` trong file compose thì compose điền bằng `.env` **cạnh compose** (`docker/.env`, không có), nên cả ba thành rỗng. Thêm nữa `.env` đang ghi `BD_HOST` (đảo chữ), không phải `DB_HOST`.
>
> | Cách | Dùng khi nào |
> | --- | --- |
> | Chạy `docker compose --env-file ../.env up -d` | Giữ một file `.env` ở gốc, dùng chung cho Spring và compose |
> | Dời compose về gốc `so-soi/`, cạnh `.env` | Muốn gõ lệnh ngắn |
>
> Lần sửa 2: compose về lại gốc, nhưng `env_file` vẫn là `../.env` (lại trỏ ra `learning/`), `.env` vẫn chưa có `DB_HOST`. Dời compose thì tên project đổi theo thư mục (`docker` → `so-soi`), volume mới là `so-soi_pgdata`, trống; dữ liệu cũ nằm ở `docker_pgdata`.

### 5. ✅ Tên class không khớp việc nó làm

- **Lỗi**: [GreetingController.java:7](../../sosoi/src/main/java/com/example/sosoi/GreetingController.java#L7) tên là `Greeting` (chép từ guide) nhưng chỉ làm `/ping`.
- **Vì sao không nên**: người đọc (hay thợ) tìm chỗ xử lý `/ping` sẽ không nghĩ tới `Greeting`. Giống dán nhãn "muối" lên hũ đường.
- **Cách thay thế**: `PingController` hoặc `HealthController`. Nhớ đổi cả tên file.

## English

### 7. ⏳ Mục `Done` dùng động từ nguyên mẫu, chưa chia quá khứ

- **Lỗi**: [log.md:10](../notes/log.md#L10) `insert data ... and use returning`.
- **Vì sao không nên**: `Done` kể việc đã xong, nên động từ phải ở past simple. Nếu để nguyên mẫu, câu đọc ra giống một mệnh lệnh ("hãy insert"), người đọc dễ hiểu nhầm là việc còn phải làm. Giống tờ giấy dán tủ lạnh ghi "mua sữa": không biết là đã mua hay còn phải mua.
- **Cách thay thế**: `Inserted ... with psql and used RETURNING`. Dùng `with psql`, không dùng `by psql`.

| Mục | Dạng động từ |
| --- | --- |
| `Done` | past simple: `Inserted`, `Learned` |
| `Next` | nguyên mẫu (mệnh lệnh): `Write`, `Add` |

### 8. ⏳ Tự ghép tiền tố phủ định cho từ không có dạng đó

- **Lỗi**: [log.md:14-15](../notes/log.md#L14-L15) dùng `unknow`, `un-catch`.
- **Vì sao không nên**: tiếng Anh không cho gắn `un-` vào động từ nào cũng được. `unknow` là sai chính tả (chỉ có tính từ `unknown`), còn `un-catch` không phải là một từ. Giống tự chế từ "bất ăn" thay cho "không ăn".
- **Cách thay thế**: muốn phủ định một hành động thì dùng `don't` / `didn't`: `I don't know ...`, `I didn't get ...`. Hoặc dùng tính từ có sẵn: `... is still unclear to me`.

### 9. ⏳ Danh từ đếm được sau số nhiều chưa thêm `-s`

- **Lỗi**: [log.md:12](../notes/log.md#L12) `5 api`.
- **Vì sao không nên**: sau số lớn hơn 1 thì danh từ phải ở số nhiều. Viết tắt cũng vậy: `APIs`. Giống ghi "5 quả táo" mà lại vẽ ra có một quả.
- **Cách thay thế**: `5 endpoints` hoặc `5 APIs`. Với Ngày 3 thì `endpoints` chính xác hơn, vì cả 5 cái đều thuộc một API `/tickets`.
