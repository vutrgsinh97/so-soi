## Day 2

### 1. kiến trúc cơ bản, đi theo thứ tự:
    1. Java object: đối tượng Java chứa dự liệu
    2. Repository: nơi tiếp nhận request sửa, xoá or truy vấn
    3. Hibernate: Ánh xã đối tượng Java (1) với bảng trong db và tạo câu lệnh SQL
    4. SQL: Ngôn ngữ để giao tiếp với db
    5. PostgreSQL: Thực thi SQL (4) và lưu trữ data thực tế

### 2. pom.xml:
- Có thể dùng lombok cho gọn
- Phần **build** không buộc
- Nhớ Java: Reload Project
  
### 3. Entity Timestamp Management (quản lý thời gian của entity)

|  | Phương pháp | Đặc điểm | Khi nào dùng? |
| :--- | :--- | :---: | ---: |
| Hibernate |@CreationTimestamp, @UpdateTimestamp | Tự động gán time, không cần cấu hình thêm | Dự án CRUD, chỉ cần ghi nhận time create/update |
| JPA Lifecycle | @PrePersist, @PreUpdate | Tự defined logic trước khi lưu/update entity | Cần tuỳ chỉnh logic, không muốn phụ thuộc vào Hibernate |
| Spring Data JPA Auditing | @CreatedDate, @LastModifiedDate | Tự động ghi nhận time, hỗ trợ ghi nhận người thực hiện, cần cấu hình thêm | Hệ thống ERP, CRM, HRM, cần quản lý ai tạo và ai chỉnh sửa dữ liệu |

### 4. Optimistic Locking && Unique constraint
```Java
@Table (
  uniqueConstraints = {
    @UniqueConstraint(
      name = "unique_profile_day",
      columnNames = {"profile_id", "day"}
    )
  }
)
```
=> Nói cho Hibernate: Trong bảng Ticket, tổ hợp (profile_id, day) không được phép trùng lặp

**Cách đọc code trên:
  - table:cấu hình bảng trong db
    - uniqueConstraints: Danh sách các ràng buộc không trùng lặp.
      - @UniqueConstraint: Định nghĩa một ràng buộc cụ thể.
        - name: Tên của ràng buộc.
        - columnNames: Danh sách các cột cần kiểm tra trùng lặp khi kết hợp với nhau.

### 5. Keyword
**ORM (Object-Relational Mapping)**: Ánh xạ đối tượng Java với database quan hệ.
**Schema Generation**: Tự động sinh cấu trúc database.
**DDL (Data Definition Language)**: SQL dùng để định nghĩa cấu trúc database.
**Database Migration**: Quản lý sự thay đổi cấu trúc database theo phiên bản.