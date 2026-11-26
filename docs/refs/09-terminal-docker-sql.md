# Terminal: Docker, psql, SQL cơ bản

Dùng cho: Ngày 1, 2, 5, 6, 9

Bảng tra do Claude soạn. Sổ tay riêng của anh vẫn là `docs/notes/terminal.md`.

## 1. Docker Compose

Chạy trong folder có `docker-compose.yml`. Service của project tên `db` (xem file compose).

| Lệnh | Làm gì | Ví dụ |
| --- | --- | --- |
| `docker compose up -d` | Tạo và chạy container nền (`-d` = detached) | `docker compose up -d` |
| `docker compose ps` | Xem container nào đang chạy | `docker compose ps` |
| `docker compose logs -f <service>` | Xem log liên tục, `Ctrl+C` để thoát | `docker compose logs -f db` |
| `docker compose exec <service> <lệnh>` | Chạy lệnh bên trong container đang chạy | `docker compose exec db bash` |
| `docker compose stop` | Tắt container, giữ nguyên container và dữ liệu | `docker compose stop` |
| `docker compose down` | Tắt và xoá container, **giữ volume** | `docker compose down` |
| `docker compose down -v` | Tắt, xoá container **và volume** | `docker compose down -v` |

**Cảnh báo:** `down -v` xoá volume, tức là **mất sạch dữ liệu** trong database. Chỉ dùng khi muốn làm lại từ đầu.

**Volume** là ổ đĩa riêng do Docker giữ, nằm ngoài container. Container bị xoá thì volume vẫn còn, nên `down` rồi `up` lại dữ liệu không mất. Ví dụ đời thường: container là căn phòng thuê, volume là cái tủ anh gửi ở kho riêng.

## 2. psql

`psql` là chương trình gõ lệnh vào Postgres. Mở nó **bên trong container**:

```bash
docker compose exec db psql -U <db_user> -d <db_name>
```

- `<db_user>`: lấy từ biến `DB_USER` (đặt trong file `.env`, compose đọc vào). Anh tự xem file đó, không ghi vào docs.
- `<db_name>`: đặt ở `POSTGRES_DB` trong `docker-compose.yml`.

Meta-command (lệnh của psql, bắt đầu bằng `\`):

| Lệnh | Làm gì | Ví dụ |
| --- | --- | --- |
| `\l` | Liệt kê database | `\l` |
| `\c <db>` | Chuyển sang database khác | `\c <db_name>` |
| `\dt` | Liệt kê bảng | `\dt` |
| `\d <table>` | Xem cột, kiểu, khoá, index của bảng | `\d book` |
| `\x` | Bật/tắt hiển thị dọc (dễ đọc khi hàng dài) | `\x` |
| `\?` | Danh sách mọi meta-command | `\?` |
| `\q` | Thoát psql | `\q` |

**Lưu ý:** câu SQL phải kết thúc bằng `;`. Meta-command thì **không** cần `;`.

## 3. SQL cơ bản

Ví dụ dùng hai bảng giả: `author(id, name)` và `book(id, title, pages, author_id)`.

**Đọc dữ liệu** (`SELECT`): chọn cột, lọc, sắp xếp, giới hạn số dòng.

```sql
SELECT title, pages FROM book WHERE pages > 200 ORDER BY pages DESC LIMIT 5;
```

**Thêm** (`INSERT ... RETURNING`): `RETURNING` trả luôn dòng vừa thêm, tiện để lấy `id`.

```sql
INSERT INTO author (name) VALUES ('An') RETURNING id;
INSERT INTO book (title, pages, author_id) VALUES ('Sách A', 120, 1);
```

**Sửa** (`UPDATE`):

```sql
UPDATE book SET pages = 150 WHERE id = 1;
```

**Xoá** (`DELETE`):

```sql
DELETE FROM book WHERE id = 1;
```

**Cảnh báo:** quên `WHERE` trong `UPDATE` hoặc `DELETE` là **ảnh hưởng mọi dòng** của bảng. Viết `WHERE` trước, kiểm tra bằng `SELECT` cùng điều kiện rồi mới sửa/xoá.

**Nối bảng** (`JOIN`): lấy cột của hai bảng qua khoá ngoại.

```sql
SELECT b.title, a.name FROM book b JOIN author a ON a.id = b.author_id;
```

**Gom nhóm** (`GROUP BY` + `COUNT`/`AVG`): thống kê theo từng nhóm.

```sql
SELECT a.name, COUNT(*) AS so_sach, AVG(b.pages) AS trung_binh_trang
FROM book b JOIN author a ON a.id = b.author_id GROUP BY a.name;
```

**Thử an toàn** (`BEGIN` / `ROLLBACK`): mọi thay đổi nằm trong transaction, chưa ghi thật. `ROLLBACK` huỷ, `COMMIT` ghi thật.

```sql
BEGIN;
DELETE FROM book WHERE pages < 100;   -- xem kết quả bằng SELECT
ROLLBACK;
```

Tạo dữ liệu Sổ Sói: xem `\d account`, `\d profile` trước, insert bảng cha trước.

## 4. Lỗi hay gặp

| Triệu chứng | Nguyên nhân | Cách xử lý |
| --- | --- | --- |
| Dấu nhắc đổi thành `-#` hoặc `-(#`, không thấy kết quả | Câu SQL thiếu `;` (hoặc thiếu đóng ngoặc/nháy), psql chờ gõ tiếp | Gõ `;` rồi Enter. Kẹt thì `Ctrl+C` để huỷ câu đang gõ |
| `relation "xyz" does not exist` | Sai tên bảng, sai database, hoặc bảng chưa được tạo | `\dt` xem có bảng chưa; `\c` đúng database |
| `violates foreign key constraint` | Insert dòng con khi dòng cha chưa tồn tại (hoặc xoá dòng cha còn dòng con) | Insert bảng cha trước; xoá dòng con trước |
| `duplicate key value violates unique constraint` | Trùng giá trị ở cột có `UNIQUE` hoặc khoá chính | Đổi giá trị, hoặc `SELECT` xem dòng đã có |
| `service "db" is not running` / `no such service` | Container chưa chạy, hoặc sai tên service | `docker compose ps`; chưa chạy thì `docker compose up -d` |

## 5. Link

- [Docker Compose CLI reference](https://docs.docker.com/reference/cli/docker/compose/)
- [psql](https://www.postgresql.org/docs/current/app-psql.html)
- [PostgreSQL tutorial: SQL](https://www.postgresql.org/docs/current/tutorial-sql.html)
