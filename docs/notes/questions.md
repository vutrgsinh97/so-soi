## Ngày 0: Data model

### 1.

```
1. List<Tag>, List<Ticket>. Trong Postgres, một ô chỉ chứa một giá trị. Vậy "một tờ có nhiều thẻ, một thẻ nằm ở nhiều tờ" thì cất ở đâu? Thử vẽ hai tờ với ba thẻ ra giấy, rồi tự hỏi: cái gì nối chúng với nhau?
-> chắc là thiếu rồi, ý ta là 1 ticket có nhiều tag và 1 tag có thể ở nhiều ticket.
   Nguyệt: Đúng cái cần có rồi, giờ tới chỗ cất. Điền tay thử: tờ A gắn `cà phê`, `nghỉ`; tờ B gắn `cà phê`, `việc nhà`. Điền cột `tag` của TICKET và `ticketId` của TAG xem kẹt ở đâu. Gợi ý: mỗi CẶP (tờ, thẻ) là một dòng riêng. Dòng đó nằm ở bảng nào?
   Nguyệt (lần 2): Câu này còn trống nè. Gợi ý thêm: nếu không nhét vào TICKET, cũng không nhét vào TAG được, thì có được phép tạo thêm một bảng thứ ba không? Bảng đó cần mấy cột?
   -> à há ta nhớ rồi, do quan hệ nhiều-nhiều nên phải có cột trung gian (mapping table/Association table/Bridge table/Junction table)
   Nguyệt: ✅ Đúng, là BẢNG trung gian (không phải cột). Lúc vẽ lại: bảng đó tên gì, có mấy cột, và một cặp (tờ, thẻ) có được xuất hiện hai lần không?

2. ACCOUNT trỏ tới USER, USER lại trỏ ngược tới ACCOUNT. Cần cả hai chiều không? Mà khoan, app này có cần tách hai bảng không, hay một bảng là đủ? Lang thấy tách ra thì được lợi gì?
-> ta nghĩ nên tách vì sẽ tiện hơn vì sau này mở rộng thì sao? 1 account có nhiều user mà, với lại ta cũng đang học mà :)) nàng thấy sao?
   Nguyệt: "Đang học" là lý do hợp lệ, em chịu. Giống Netflix: một tài khoản, nhiều profile. Trỏ một chiều (USER giữ `accountId`) là đúng. Còn phải chốt: tờ ghi thuộc về USER hay ACCOUNT?
   -> tờ ghi thuộc về USER (ta thêm ticket id vào USER rồi nha)
   Nguyệt: Chọn USER là đúng. Nhưng chỗ đặt cột thì ngược rồi. Lang ghi 30 tờ trong tháng 11: cột `ticketId` trong USER chứa được mấy tờ? Vậy cột nối nên nằm bên "một" (USER) hay bên "nhiều" (TICKET)? (Giống câu 2 hồi nãy: chỉ trỏ một chiều.)
   -> này là quan hệ 1:N, 1 user có nhiều ticket, nhưng 1 ticket chỉ thuộc về 1 user, nên TICKET phải nắm key 
   Nguyệt: ✅ Chuẩn. Nhớ xoá `ticketId` khỏi USER trong sơ đồ.

3. date: LocalDateTime. Lang ghi một tờ lúc 8 giờ sáng, rồi lỡ ghi thêm lúc 9 giờ tối. Database làm sao biết hai tờ đó cùng một ngày? Cột này nên giữ cả giờ, hay chỉ giữ ngày?
-> chổ này đã có code bắt lại rồi mà nhỉ? 1 ngày chỉ 1 ticket thôi mà
   Nguyệt: Code bắt là tốt nhưng chưa đủ. (a) Hai request tới cùng lúc, cả hai hỏi "có tờ chưa?", cả hai nghe "chưa", lưu luôn hai tờ. (b) Với LocalDateTime, code phải tự tính 8h sáng và 9h tối là cùng ngày, còn dính múi giờ. Giữ cả giờ thì được gì? Chỉ giữ ngày thì được gì?
   -> ta nghĩ LocalDateTime khi muốn xem chi tiết thì nên có ngày/giờ, nên ta nghĩ là phải có 2 filed nhỉ? ta search thì request đồng thời gọi là (concurrent requests), ta tìm thì có nhiều các như: database từ sinh timestampe, optimistic locking (dùng version gì đó), ta chịu nên nhờ cô Nha sĩ khám lưỡi
   Nguyệt: Hai field là đúng hướng! Mà Lang có sẵn rồi đó: `createAt` đã giữ ngày giờ chi tiết. Vậy cột `date` chỉ cần giữ "ngày nào" thôi. Trong Java thì kiểu nào chỉ có ngày, không có giờ?
   Về concurrent requests: Lang tra giỏi lắm. Nhưng optimistic locking (version) dùng khi hai người cùng SỬA một dòng ĐÃ CÓ. Ca của mình là hai request cùng TẠO mới, lúc đó chưa có dòng nào để gắn version. Nên cách đơn giản nhất ở đây là câu 4: để database chặn trùng. Request tới sau sẽ bị database từ chối, code bắt lỗi đó rồi trả 409.
   -> Chỉ có ngày: LocalDate, chỉ có giờ: LocalTime; nhưng có thể cho database tự chặn trùng và thêm optimistic locking để PUT cùng 1 dòng mà :)), ta nghĩ nên gộp cả 2 nhỉ? vậy ta chọn LocalDate + createAt thì databse tự chặn + optimistic locking cho PUT/PATCH
   Nguyệt: ✅ Đúng hết. Ý gộp cả hai là Lang tự nghĩ ra đó, giỏi: unique chặn TẠO trùng, optimistic locking chặn hai lần SỬA đè nhau. Locking để làm sau (thêm cột `version`), giờ chỉ cần ghi vào sơ đồ là được.

4. "Mỗi người chỉ một tờ một ngày". Luật này nằm ở đâu trong sơ đồ? Cột nào đi chung với nhau thì không được trùng?
-> cái này buộc trong sơ đồ à?
   Nguyệt: Có. Luật "không được trùng" là một phần thiết kế database, gọi là unique constraint. Database chặn giùm kể cả khi code quên (và chặn luôn ca (a) ở câu 3). Mermaid không vẽ được luật này, nên ghi bằng chữ dưới sơ đồ. Cột nào đi chung với nhau thì không được trùng?
   Nguyệt (lần 2): Gợi ý: "mỗi NGƯỜI chỉ một tờ một NGÀY". Hai chữ in hoa đó ứng với hai cột nào trong TICKET?
   -> Người: createBy, Ngày, date và localDateTime? nếu 1 người chỉ có 1 tờ trong 1 ngày thì cột ngày đúng ra phải là date thôi nhỉ
   Nguyệt: ✅ Đúng: (người, ngày) không được trùng, và cột ngày là LocalDate. Một chỗ nhỏ: `createdBy` là "ai bấm tạo" (để ghi vết), còn cột Lang nói ở câu 2 là "sổ của ai". Trong app này hai cái trùng nhau, nên chọn một tên cho rõ, ví dụ `userId`, rồi unique (userId, date).

5. Thẻ là của ai? Lang tạo thẻ cà phê, bạn Lang cũng tạo cà phê. Hai người dùng chung một thẻ, hay mỗi người một cái riêng? Không có đáp án đúng, nhưng Lang phải chọn một.
-> Cái này có 2 hướng:
 5.1. Một người tạo, dùng chung: Sẽ bắt thêm tên của thẻ, bắt trùng 100% (hoa, tường, tiếng Việt,...), cho dùng lại thẻ của người khác
 5.2. Mỗi user 1 loại thẻ, không dùng của nhau
   Nguyệt: Ra được hai hướng là tốt. Chọn một. Thêm câu cho dễ chọn: theo 5.1, bạn Lang đổi tên thẻ `cà phê` thành `cafe` thì tờ của Lang có đổi theo không? Ai được sửa, xoá thẻ dùng chung? Theo 5.2 thì TAG cần thêm cột gì?
   -> 5.1:đổi theo luôn nhỉ? (nhưng mà như vậy không ổn, vì tự nhiên thẻ mình bị đổi); 5.2: ta nghĩ theo hướng này nhưng mà lại bị phìm ra...khó quá.... *thè lưỡi*
   Nguyệt: Lang tự thấy cái dở của 5.1 rồi, giỏi. "Phình ra" thì tính thử: một người chừng 30 thẻ, 1.000 người là 30.000 dòng. Với Postgres thì nhỏ xíu, không đáng lo. Vậy chọn 5.2 nha. Còn 2 câu: TAG cần thêm cột gì để biết thẻ của ai? Và trong sổ của MỘT người, cột nào đi chung với nhau thì không được trùng (để Lang không có hai thẻ `cà phê`)?
   -> chốt: user mỗi người 1 bộ TAG riêng, không dùng chung; này là quan hệ 1-N nên TAG phải có thêm cột userId, còn vế sau thì ta sẽ nên dùng đặt thêm luật cho database không trùng, như cái ngày nhỉ?
   Nguyệt: ✅ Đúng, y như cái ngày: unique (userId, name). Câu thêm cho vui, không bắt buộc: `Cà phê` với `cà phê` có tính là trùng không?

7. Nguyệt: Bước cuối của Ngày 0. Vẽ lại sơ đồ Mermaid theo hết mấy câu ✅ ở trên (sửa cú pháp như câu 6, thêm bảng trung gian, nối bảng bằng `||--o{`). Dưới sơ đồ ghi các luật unique bằng chữ. Rồi viết 3-5 dòng tiếng Anh: why this design. Xong là tick Ngày 0.

6. Nguyệt: Lỗi Mermaid. Viết KIỂU trước, TÊN sau: `string username`, không phải `username string`. Kiểu là một chữ, không gạch nối: đổi `id`, `user-id`, `ticket-id` thành `uuid`. Cột trỏ sang bảng khác thì thêm `FK` cuối dòng.

```