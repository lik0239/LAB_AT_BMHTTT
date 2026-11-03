# LAB 9 – Remote DNS Cache Poisoning (mô phỏng Kaminsky) trên pfSense cô lập

| Thông tin | |
|---|---|
| Họ và tên | Hà Chí Hân |
| Mã số sinh viên | 1150080014 |
| Lớp | 11_ĐH_CNPM1 |
| Học phần | An toàn và bảo mật hệ thống thông tin |
| Kênh YouTube (video thực hành) | https://www.youtube.com/@chihan0403 |

## Mục tiêu

- Phân biệt Local DNS spoofing (Lab trước – attacker cùng mạng, nghe trộm được) với **Remote DNS cache poisoning kiểu Kaminsky** (attacker ở mạng khác, không nghe trộm được Transaction ID / cổng nguồn của resolver).
- Tách ATTACKER (Kali) sang mạng con riêng **ATTACKNET 10.10.0.0/24** phía sau pfSense, buộc attacker phải đoán TXID 16-bit thay vì sao chép.
- Tái lập cơ chế poisoning ở cấp **delegation (NS + glue A)** bằng một resolver mô phỏng có lỗ hổng chủ đích (cổng nguồn cố định 33333) chỉ trong pha tấn công; sau đó khôi phục BIND9 hiện đại và hardening.
- Chứng minh hậu quả bằng truy vấn DNS + trang web mô phỏng, phân tích biện pháp phòng thủ (recursion ACL, source-port randomization, DNSSEC, bailiwick checking, firewall segmentation).

## Môi trường thực hành

Kế thừa pfSense-LAB6 và DMZ-WEB của Lab 6/7, và mạng DNSLAB + máy APP-TEST/AUTH-DNS đã dựng ở **Lab 8** (bài Local DNS Attack) trên VMware Workstation 17.5.2. Tài liệu gốc viết cho VirtualBox và gọi tiền thân là "LAB 9"; trên máy sinh viên mạng đó chính là Lab 8, nên bài này là **Lab 9** của sinh viên (lệch 1 số với tài liệu gốc). Bộ tài nguyên/script của học phần đặt tên theo "lab10" (LAB10_Resources, example.lab10.test, *_lab10.py) nên giữ nguyên các tên literal đó khi chạy lệnh.

| Máy | Hệ điều hành | Địa chỉ IP | Ghi chú |
|---|---|---|---|
| pfSense-LAB6 | pfSense CE 2.7.2 | DNSLAB 10.9.0.1/24, ATTACKNET 10.10.0.1/24 (OPT3 mới) | ATTACKNET không Upstream Gateway |
| DNS-SERVER | Ubuntu Server 24.04.5, BIND9 | 10.9.0.10 | Baseline BIND9; attack phase chạy `vuln_resolver_lab10.py` |
| VICTIM | Ubuntu Server 24.04.5 | 10.9.0.100 | Kế thừa Lab 8, dùng DNS 10.9.0.10 |
| ATTACKER | Kali Linux 2026.2 | 10.10.0.200 | Chuyển khỏi DNSLAB; evil DNS + fake web + kaminsky script; promiscuous = tắt |
| AUTH-DNS/RELAY | Ubuntu Server 24.04.5 (APP-TEST) | 172.16.6.20 | `auth_dns_relay_lab10.py`: authoritative hợp lệ + race harness UDP/5300 |
| DMZ-WEB | Kế thừa Lab 6/7/8 | 172.16.6.10 | Đích thật của `www.example.lab10.test` |
| HOST MGMT | Máy thật | 192.168.56.1 | Quản trị WebGUI pfSense (192.168.56.254) |

Domain thử nghiệm: `example.lab10.test` (RFC 2606). Mọi script giới hạn cứng vào 10.9.0.0/24, 10.10.0.0/24, 172.16.6.0/24; chỉ chạy trong môi trường ảo cô lập.

## Nội dung đã thực hiện (kết quả dự đoán)

| Mục | Nội dung | Kết quả dự đoán |
|---|---|---|
| pfSense ATTACKNET | OPT3 10.10.0.1/24, không Upstream Gateway, ma trận 12 rule | Đúng kỳ vọng |
| Kali remote | 10.10.0.200, gỡ card DNSLAB, promiscuous tắt | Không sniff được 10.9.0.10 ↔ 172.16.6.20 |
| Baseline | `www.example.lab10.test` → 172.16.6.10 | Đúng kỳ vọng |
| DNS-SERVER | BIND9 baseline + `vuln_resolver_lab10.py` trong attack phase | named-checkconf OK |
| AUTH-DNS/RELAY | `auth_dns_relay_lab10.py`, race window 3s, UDP/5300 chỉ nhận 10.10.0.200 | `[PENDING]` rồi `[HIT]` |
| Task 1 – Kaminsky | 16 batch × 4096 TXID → forged delegation | `[CACHE]` → `www` = 10.10.0.200, Fake Site |
| Task 2 – Phân tích | NS/glue in-bailiwick, so sánh trước/sau | Đúng kỳ vọng |
| Hardening | Khôi phục BIND9, recursion ACL, source-port random | Kali bị REFUSED, cổng nguồn không còn 33333 |
| Câu hỏi | 12 câu hỏi phân tích | Đã trả lời trong báo cáo |

Báo cáo được viết với kết quả dự đoán trước khi thực hành thật trên VM (quy trình giống Lab 6/7/8). Làm xong mục nào mà kết quả thực tế khác dự đoán thì cập nhật lại báo cáo trước khi nộp chính thức. Hướng dẫn từng bước lệnh cụ thể nằm ở `D:\AnToanVaBaoMatThongTin\Lab9_ChuanBi_VMware.md` (không nằm trong repo).

## Cấu trúc thư mục

```
LAB10/
├── Lab9_11_ĐH_CNPM1_1150080014_HaChiHan.docx   # Báo cáo thực hành
├── LAB10_HuongDan.docx                          # Tài liệu hướng dẫn gốc của học phần
└── README.md
```

## Lưu ý an toàn

- Chỉ thực hiện trong môi trường ảo cô lập; không chạy script trên Wi-Fi, mạng trường, doanh nghiệp, Internet hay hệ thống của người khác.
- Race relay UDP/5300 là giao diện chỉ dùng trong lab (biết trước TXID để tránh flood 65.536 gói) — không phản ánh độ khó/khả thi của tấn công Internet thực tế.
- Sau buổi lab: dừng resolver mô phỏng, khởi động lại BIND9, disable/xóa rule UDP/5300, tắt interface ATTACKNET hoặc khôi phục snapshot `LAB9_START_FROM_LAB8`.
