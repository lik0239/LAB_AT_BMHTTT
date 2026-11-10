# LAB 10 – Mã hóa đối xứng cổ điển (Java Swing / Maven / SQL Server)

- **Họ và tên:** Hà Chí Hân
- **MSSV:** 1150080014
- **Lớp:** 11_ĐH_CNPM1
- **Học phần:** An toàn và bảo mật hệ thống thông tin
- **Kênh YouTube (video thực hành):** https://www.youtube.com/@chihan0403

> Ghi chú: tài liệu hướng dẫn gốc (`LAB10_HuongDan.docx`) đặt tiêu đề nội dung là "LAB 1" theo mẫu của tác giả; sinh viên nộp bài này theo số **Lab 10** (tên file báo cáo và thư mục). Tên project, package, database giữ nguyên theo tài liệu (`LAB1_MaHoaCoDien`, `week_01`) để khớp với video.

## Nội dung đã làm

1. **Sáu thuật toán mã hóa đối xứng cổ điển**, mỗi thuật toán một lớp + một form:
   - Caesar, Vigenère, Rail Fence, Playfair, Hoán vị cột (Transposition), Autokey.
2. **Chương trình tổng hợp** (`frm_TongHop`): chọn thuật toán bằng JComboBox, mã hóa/giải mã trên một giao diện, ghi **lịch sử** xuống Microsoft SQL Server (bảng `dbo.history`) và xem lại bằng `JTable` (`frm_LichSu`).
3. **Kết nối SQL Server** (`frm_KetNoiSQL`): nhập thông tin kết nối lúc chạy; truy vấn dùng `PreparedStatement`; tài khoản chỉ được cấp quyền tối thiểu trên một bảng.
4. **Phân tích (tấn công) mật mã** (`frm_TanCong`): vét cạn khóa Caesar, phân tích tần suất chữ cái, known plaintext suy ra khóa Vigenère.
5. **Menu chính** (`frm_Menu`): mở từng bài; là lớp chạy chính (F6).

## Cấu trúc thư mục

```
LAB10/
├── LAB1_MaHoaCoDien/              # Project NetBeans/Maven (mở trực tiếp bằng NetBeans)
│   ├── pom.xml
│   ├── src/main/java/week_01/     # 11 form + 6 lớp thuật toán + tiện ích/CSDL/phân tích
│   └── 03_SQL_SERVER/             # 3 script .sql (tạo CSDL, tạo login, kiểm tra dữ liệu)
├── Lab10_11_ĐH_CNPM1_1150080014_HaChiHan.docx   # Báo cáo
└── README.md
```

## Cách chạy

1. Mở `LAB1_MaHoaCoDien` bằng Apache NetBeans (hoặc build bằng Maven).
2. (Phần nâng cao) Cài SQL Server Express + SSMS, bật TCP/IP cổng 14333, Mixed Mode; chạy lần lượt các script trong `03_SQL_SERVER/`.
3. Bấm **Run Project (F6)** → mở `frm_Menu`.

## Kết quả kiểm thử (trùng từng ký tự với bộ chuẩn)

| Thuật toán | Plaintext | Key | Ciphertext |
|---|---|---|---|
| Caesar | HELLO | 3 | KHOOR |
| Vigenère | HELLO | KEY | RIJVS |
| Rail Fence | HELLO WORLD | 3 | HOREL OLLWD |
| Playfair | HELLOWORLD | KEYWORD | GYIZSCOKCFBU |
| Transposition | BAOMAT | 2,3,1 | AAOTBM |
| Autokey | HELLO | KEY | RIJSS |

Toàn bộ mã nguồn đã biên dịch sạch (JDK 17, `mvn compile` thành công) và vượt 12 vector kiểm thử tự động.

## Lưu ý

- Mật khẩu kết nối SQL Server **không** lưu trong mã nguồn hay README; chương trình nhập lúc chạy và giữ ở `char[]`. Mật khẩu tài khoản `lab1_user` nằm trong script `02_Tao_Login_MAU.sql` do tài liệu cung cấp và **chỉ dùng cho lab trên máy cá nhân** – nếu đẩy repo Public nên cân nhắc thay bằng mật khẩu khác.
- Giải mã Playfair còn ký tự đệm `X` (ví dụ `HELXLOWORLDX`) là **đúng** thuật toán.
