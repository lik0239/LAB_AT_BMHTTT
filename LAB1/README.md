# LAB 1 – Bắt gói tin Telnet – SSH (Examining SSH & Telnet in Wireshark)

| Thông tin | |
|---|---|
| **Họ và tên** | Hà Chí Hân |
| **Mã số sinh viên** | 1150080014 |
| **Lớp** | 11_ĐH_CNPM1 |
| **Học phần** | An toàn và bảo mật hệ thống thông tin |
| **Kênh YouTube (video thực hành)** | https://www.youtube.com/@chihan0403 |

## Mục tiêu

- Giả lập mô hình mạng Client – Server – Attacker, triển khai dịch vụ Telnet và SSH trên Server.
- Dùng Wireshark bắt và phân tích gói tin của phiên Telnet và phiên SSH.
- So sánh mức độ an toàn của hai giao thức dựa trên kết quả thực nghiệm.

## Môi trường thực hành

Server được triển khai bằng Ubuntu Server 26.04.1 LTS theo phương án khuyến nghị trong tài liệu (thay cho Windows Server 2008 + Cygwin). Các máy nằm chung mạng Host-only **VMnet1 – 192.168.222.0/24** trên VMware Workstation 17.5.2, cô lập với Internet.

| Vai trò | Máy | Hệ điều hành | Phần mềm | IP |
|---|---|---|---|---|
| Client | Máy thật | Windows 11 | PuTTY 0.85, PuTTYgen, Wireshark 4.6.8 + Npcap 1.88 | 192.168.222.1 |
| Server | Máy ảo `lab1-server` | Ubuntu Server 26.04.1 LTS | inetutils-telnetd, inetutils-inetd, OpenSSH Server | 192.168.222.129 |
| Attacker | Máy ảo Kali | Kali Linux 2026.2 | Wireshark | 192.168.222.130 |

## Nội dung đã thực hiện

1. **Thiết lập môi trường:** tạo 2 máy ảo, chuyển card mạng sang VMnet1, kiểm tra kết nối bằng `ping` giữa các máy.
2. **Chuẩn bị Server:** tạo tài khoản thực hành `chihan` (mật khẩu = MSSV); cài và bật Telnet Server qua `inetd`; cài OpenSSH Server.
3. **Bắt gói Telnet:** Client đăng nhập Telnet (TCP/23) bằng PuTTY, Attacker bắt gói bằng Wireshark trên `eth0`, phân tích bằng `tcp.port == 23` và Follow TCP Stream; lặp lại với mật khẩu phức tạp (> 10 ký tự).
4. **Bắt gói SSH:** Client đăng nhập SSH (TCP/22), đối chiếu fingerprint khóa host trước khi chấp nhận, phân tích phiên bằng `tcp.port == 22`.
5. **Xác thực SSH bằng khóa công khai:** tạo cặp khóa bằng PuTTYgen, đưa khóa công khai vào `~/.ssh/authorized_keys`, đăng nhập không cần mật khẩu.
6. **Trả lời 11 câu hỏi** trong phần C của tài liệu.

## Kết quả

| Tiêu chí | Telnet (TCP/23) | SSH (TCP/22) |
|---|---|---|
| Username, mật khẩu | Đọc được nguyên văn | Không đọc được |
| Lệnh và kết quả lệnh | Đọc được toàn bộ | Không đọc được (Encrypted packet) |
| IP, cổng, thời điểm, kích thước gói | Quan sát được | Quan sát được |
| Thông tin còn lộ | Lời chào hệ thống (phiên bản Ubuntu, hostname) | Chuỗi phiên bản SSH, danh sách thuật toán, khóa host công khai |
| Xác thực Server | Không có | Có (khóa host ED25519, fingerprint) |

- Mật khẩu phức tạp vẫn hiển thị nguyên văn khi dùng Telnet: độ mạnh mật khẩu không thay thế được việc mã hóa kênh truyền.
- Máy Attacker (Kali) bắt được lưu lượng unicast giữa Client và Server trên mạng ảo VMnet1.
- Phiên SSH thỏa thuận trao đổi khóa lai hậu lượng tử (Wireshark ghi nhận PQ/T Hybrid Key Exchange); sau gói New Keys toàn bộ dữ liệu đã được mã hóa.

## Cấu trúc thư mục

| File | Nội dung |
|---|---|
| `Lab1_11_ĐH_CNPM1_1150080014_HaChiHan.docx` | Báo cáo thực hành (các bước, ảnh minh chứng, trả lời câu hỏi) |
| `Lab1 - Examining SSH  Telnet in Wireshark.docx` | Tài liệu hướng dẫn bài Lab |
| `README.md` | Mô tả bài Lab |

## Lưu ý để kiểm tra / chạy lại

Trên Ubuntu Server 26.04.1, dịch vụ Telnet sau khi cài mặc định bị vô hiệu hóa trong `/etc/inetd.conf` (dòng có tiền tố `#<off>#`). Các lệnh dựng lại Server:

```bash
sudo apt update
sudo apt install -y inetutils-telnetd inetutils-inetd openssh-server
sudo sed -i 's/^#<off># telnet/telnet/' /etc/inetd.conf
sudo systemctl restart inetutils-inetd
sudo systemctl enable --now ssh
sudo adduser chihan
ss -ltn | grep -E ':23|:22'      # phải thấy cổng 23 và 22 ở trạng thái LISTEN
```

- Wireshark: chọn đúng card mạng của mạng lab (`eth0` trên Kali, `VMware Network Adapter VMnet1` trên Windows), lọc bằng `tcp.port == 23` hoặc `tcp.port == 22`, xem nội dung phiên bằng **Follow → TCP Stream**.
- Chỉ bật Telnet trong mạng lab nội bộ; không mở TCP/23 ra Internet.
- Khóa bí mật SSH (`.ppk`) dùng cho phần demo không được đưa lên repository.
