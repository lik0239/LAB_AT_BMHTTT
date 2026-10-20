# LAB 7 – Xây dựng chính sách Web Protection trên mô hình pfSense + Web Proxy

| Thông tin | |
|---|---|
| **Họ và tên** | Hà Chí Hân |
| **Mã số sinh viên** | 1150080014 |
| **Lớp** | 11_ĐH_CNPM1 |
| **Học phần** | An toàn và bảo mật hệ thống thông tin |
| **Kênh YouTube (video thực hành)** | https://www.youtube.com/@chihan0403 |

## Mục tiêu

- Bổ sung tầng Web Protection cho mô hình Lab 6: máy **WEB-PROXY** chạy E2Guardian → Squid → ClamAV.
- Dùng pfSense ép Client-A/B chỉ ra web qua proxy, chặn truy cập trực tiếp TCP/80, 443 để chống bypass.
- Xây dựng chính sách web tương đương bài Sophos gốc: hard block, cảnh báo + temporary bypass, whitelist, khung giờ 60 phút, quét virus, giới hạn 100 MB, trang chặn tùy biến.

## Môi trường thực hành

Kế thừa môi trường Lab 6 trên **VMware Workstation 17.5.2**, bổ sung WEB-PROXY:

| Máy | Hệ điều hành | Địa chỉ IP |
|---|---|---|
| pfSense-LAB6 | pfSense CE 2.7.2 | MGMT 192.168.56.254 · LAN 10.6.0.1 · DMZ 172.16.6.1 |
| DC01 | Windows Server 2025 – AD/DNS `lab6.test` + zone `lab7.test` | 10.6.0.10 |
| Client-A / Client-B | Kali Linux 2026.2 (Firefox) | 10.6.0.21 / 10.6.0.22 |
| **WEB-PROXY** (mới) | Ubuntu Server 24.04.5 LTS – E2Guardian 5.3.5, Squid 6, ClamAV | 10.6.0.30 |
| DMZ-WEB | Ubuntu Server 26.04.1 + Nginx (13 site `*.lab7.test`) | 172.16.6.10, 172.16.6.50 |

Luồng web: `Client → E2Guardian 10.6.0.30:8080 → Squid 127.0.0.1:3128 → pfSense → DMZ-WEB / Internet`.

**Khác biệt so với đề và lý do:**
- `entertainment.lab7.test` và `games.lab7.test` được tách sang IP phụ **172.16.6.50**. Nếu mọi site cùng 172.16.6.10 thì pfSense (lớp 3/4) không phân biệt được site giải trí, nên Schedule 60 phút không áp dụng được.
- Thêm rule cho **DC01 → pfSense TCP/UDP 53**, vì rule explicit deny cuối ruleset chặn cả DNS forwarder của DC01.
- Captive Portal của Lab 6 được tắt trong Lab 7.

## Nội dung đã thực hiện

| Mục | Nội dung | Kết quả |
|---|---|---|
| WEB-PROXY | Squid chỉ nghe localhost, E2Guardian :8080, clamav-daemon | ✅ Hoàn thành |
| DMZ-WEB + DNS | 13 virtual host Nginx, zone `lab7.test` trên DC01 | ✅ Hoàn thành |
| pfSense | 5 alias, schedule `LAB7_ENT_60M`, ruleset 10 rule ép proxy | ✅ Chống bypass thành công |
| KB1 Baseline | normal được phép qua proxy; tắt proxy bị chặn | ✅ Đúng kỳ vọng |
| KB2–3, KB8 Hard block | china, mideast, jobs, blockedgame | ✅ Chặn, không bypass |
| KB4, KB7 Warning | weapons, politics, religion, streaming | ✅ Cảnh báo, bypass 5 phút |
| KB5 Education | education được phép, không cảnh báo | ✅ Đúng kỳ vọng |
| KB6 Schedule | entertainment/games chỉ trong khung 60 phút | ✅ Đúng kỳ vọng |
| KB9 Antivirus | EICAR bị ClamAV chặn | ✅ Đúng kỳ vọng |
| KB10 Size | 100MB.bin tải được, 110MB.bin bị từ chối | ✅ Đúng kỳ vọng |
| HTTPS inspection | Phần mở rộng | ⏸ Chưa thực hiện |
| Câu hỏi | 10 câu hỏi phân tích | ✅ Đã trả lời trong báo cáo |

## Cấu trúc thư mục

| File | Nội dung |
|---|---|
| `Lab7_11_ĐH_CNPM1_1150080014_HaChiHan.docx` | Báo cáo thực hành |
| `LAB7_pfSense_Web_Protection_2026.docx` | Tài liệu hướng dẫn của giảng viên |

## Lưu ý

- Không cài Squid/SquidGuard trên pfSense (package đã bị Netgate đánh dấu deprecated).
- File `eicar.com.txt` là chuỗi kiểm thử antivirus chuẩn, không phải mã độc; chỉ tồn tại trên DMZ-WEB của lab.
- Private key CA (nếu làm HTTPS inspection) và `config.xml` của pfSense không đưa lên repo công khai.
- File máy ảo, bộ cài và mật khẩu các máy ảo không đưa lên repo.
