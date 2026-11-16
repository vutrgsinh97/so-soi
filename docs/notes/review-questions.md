## Ngày 0: Data model

### 1. Một tờ nhiều thẻ, một thẻ nhiều tờ: cất ở đâu?
  <details>
    <summary>Đáp án</summary>
      Đây là quan hệ N-N, dùng bảng trung gian
      Tên gọi: mapping table/Association table/Bridge table/Junction table
  </details>

### 2. Một user nhiều tờ: cột nối nằm bảng nào?
  <details>
    <summary>Đáp án</summary>
    Quan hệ 1-N: ticket giữ key
  </details>

### 3. Cột ngày nên là LocalDate hay LocalDateTime?
  <details>
    <summary>Đáp án</summary>
    Nên là LocalDate vì đã có createAt, dùng unique chặn tạo trùng, optimistic locking cho PUt/PATCH
  </details>

### 4. Code kiểm rồi, sao vẫn cần database chặn trùng?
  <details>
    <summary>Đáp án</summary>
    Vì có thể 2 hay nhiều requests cùng lên 1 lúc (concurrent requests) thì code không check được nên phải cho database tự chặn theo cột
  </details>

### 5. Unique với optimistic locking khác nhau chỗ nào?
  <details>
    <summary>Đáp án</summary>
    unique: ràng buộc duy nhất, đảm bảo 1 giá trị nào đó không TẠO 2 lần trongdatabase;
    optimistic locking: Chống ghi đè dự liệu khi SỬA
  </details>

### 6. Thẻ dùng chung hay mỗi người một bộ?
  <details>
    <summary>Đáp án</summary>
    Mỗi người 1 bộ. Thêm cột userId vào TAG, unique (userId, name).
    `Cà phê` và `cà phê` thì khác nhau nhỉ :))
  </details>