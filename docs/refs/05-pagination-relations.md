# Phân trang, quan hệ giữa bảng và lỗi N+1

Dùng cho: Ngày 6 (phân trang, lọc theo khoảng ngày), Ngày 8 (thẻ, N-N, N+1). Mapping `@ManyToOne`, `mappedBy` chi tiết xem `02-jpa-persistence.md`, mục 4.

## 1. Phân trang và sắp xếp

**Phân trang** là chia danh sách lớn thành từng trang nhỏ, thay vì trả về 10.000 dòng một lần.

- `Pageable` chứa 3 thông tin: trang số mấy (`page`, bắt đầu từ 0), mỗi trang bao nhiêu dòng (`size`), sắp xếp theo gì (`sort`).
- Khai báo `Pageable pageable` làm tham số trong Controller, Spring tự đọc từ URL như `?page=0&size=10&sort=day,desc`.
- Truyền `pageable` xuống Repository, ví dụ `findByProfileId(profileId, pageable)`.
- Kết quả trả về là `Page<T>`: có danh sách dữ liệu, tổng số phần tử, tổng số trang.
- `Slice<T>` nhẹ hơn `Page` vì không chạy thêm câu đếm tổng, hợp với kiểu cuộn vô hạn.

## 2. Quan hệ giữa bảng

Một Profile có nhiều Ticket, đó là quan hệ **một-nhiều**.

- Phía Ticket (bên "nhiều"): `@ManyToOne` trỏ đến `Profile`. Bảng `ticket` có cột khoá ngoại `profile_id`. Bên này gọi là **owning side**, bên giữ khoá ngoại.
- Phía Profile (bên "một"): `@OneToMany(mappedBy = "profile")` để có danh sách ticket. `mappedBy` nghĩa là "bên kia đã quản lý khoá ngoại rồi".
- **Lazy loading**: dữ liệu liên quan chưa được tải ngay, chỉ tải khi thật sự gọi tới, ví dụ `ticket.getProfile().getFirstName()`.

## 3. Lỗi N+1

N+1 sinh ra từ lazy loading: lấy 1 câu query ra 20 ticket, rồi lặp qua từng ticket để đọc tên profile (hoặc đọc danh sách thẻ). Mỗi lần đọc lại chạy thêm 1 query. Tổng cộng 1 + 20 = 21 query, trong khi lẽ ra chỉ cần 1.

- Cách phát hiện: bật `spring.jpa.show-sql: true` và đếm số câu SQL in ra.
- Cách sửa: dùng `JOIN FETCH` trong `@Query` hoặc `@EntityGraph` để lấy ticket kèm dữ liệu liên quan trong **một** câu query.

```java
@Query("select t from Ticket t join fetch t.profile")
List<Ticket> findAllWithProfile();
```

Docs: [Pagination and Sorting using Spring Data JPA](https://www.baeldung.com/spring-data-jpa-pagination-sorting) · [Many-To-Many Relationship in JPA](https://www.baeldung.com/jpa-many-to-many) · [N+1 query problem](https://vladmihalcea.com/n-plus-1-query-problem/).
