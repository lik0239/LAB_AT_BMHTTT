# LAB 5 – Thiết lập mô hình tường lửa pfSense

| Thông tin | |
|---|---|
| **Họ và tên** | Hà Chí Hân |
| **Mã số sinh viên** | 1150080014 |
| **Lớp** | 11_ĐH_CNPM1 |
| **Học phần** | An toàn và bảo mật hệ thống thông tin |
| **Kênh YouTube (video thực hành)** | https://www.youtube.com/@chihan0403 |

> **Trạng thái:** đang thực hiện – đã kiểm tra bộ cài pfSense, tạo mạng LAN ảo và cài Windows Server cho Domain Controller; cấu hình pfSense và các tình huống sẽ được cập nhật sau khi hoàn tất thực hành.

## Mục tiêu

- Cài đặt pfSense CE 2.7.2 làm tường lửa cho mô hình ba vùng WAN / LAN / DMZ.
- Dựng Domain Controller (AD DS + DNS) trong LAN, máy chủ Web IIS trong DMZ và máy LAN-Test.
- Cấu hình Outbound NAT, thay luật "Default allow LAN to any" bằng luật tự tạo.
- Thực hiện các tình huống lọc gói: chặn ICMP / cho phép DNS–HTTP–HTTPS, giới hạn theo IP nguồn, cô lập DMZ, Port Forward, ghi log luật Block.

## Môi trường thực hành

Tài liệu viết cho VirtualBox; bài làm dùng **VMware Workstation 17.5.2** nên các mạng được ánh xạ tương đương:

| Vùng | VirtualBox (theo đề) | VMware (bài làm) | Dải địa chỉ |
|---|---|---|---|
| WAN | Bridged | VMnet8 – NAT | DHCP |
| LAN | Host-only | VMnet2 – Host-only, không DHCP, không nối card máy thật | 10.0.0.0/8 |
| DMZ | Internal Network `dmz-net` | LAN Segment `dmz-net` | 172.16.0.0/16 |

| Máy | Hệ điều hành | Địa chỉ IP |
|---|---|---|
| pfSense | pfSense CE 2.7.2-RELEASE (amd64) | LAN 10.0.0.1/8 · DMZ (OPT1) 172.16.0.1/16 · WAN DHCP |
| DC01 | Windows Server 2025 Standard Evaluation (Desktop Experience) – AD DS, DNS `vietnam.local` | 10.0.0.2/8 |
| DMZ-WEB | Windows Server 2025 + IIS | 172.16.0.2/16 |
| LAN-Test | Ubuntu Server 26.04.1 (dùng lại VM của Lab 1) | 10.0.0.3/8 |
| Máy quản trị | Kali Linux 2026.2 (thay cho máy thật) | 10.0.0.100/8 |

**Khác biệt so với đề và lý do:**
- Đề ghi Windows Server 2019/2022 và Ubuntu 22.04/24.04; bài làm dùng Windows Server 2025 và Ubuntu Server 26.04.1 có sẵn.
- Máy quản trị WebGUI là VM Kali (thêm card VMnet2 và VMnet8) thay cho máy thật, vì vậy VMnet2 không nối card máy thật và WAN dùng VMnet8 NAT thay cho Bridged.
- DMZ-WEB được clone từ snapshot Windows Server sạch (chưa promote, chưa join domain) rồi chạy Sysprep để tạo SID mới – không clone Domain Controller.

## Nội dung đã thực hiện

| Mục | Nội dung | Kết quả |
|---|---|---|
| Bộ cài | Kiểm tra SHA-256 `pfSense-CE-2.7.2-RELEASE-amd64.iso.gz` trước khi giải nén | ✅ Khớp `883fb7bc…cd9e4` |
| Mạng ảo | Tạo VMnet2 10.0.0.0/8 (Host-only, tắt DHCP) | ✅ Hoàn thành |
| Domain Controller | Cài Windows Server 2025 + VMware Tools, snapshot nền | ✅ Đã cài – *cấu hình AD DS cập nhật sau* |
| DMZ-WEB | Clone + Sysprep, IIS | *(cập nhật sau)* |
| LAN-Test | Ubuntu Server 10.0.0.3/8 | *(cập nhật sau)* |
| pfSense | Cài đặt, gán WAN/LAN/OPT1, WebGUI, Outbound NAT, luật LAN | *(cập nhật sau)* |
| Tình huống 1 | Block ICMP, Pass DNS 53, Pass HTTP/HTTPS | *(cập nhật sau)* |
| Tình huống 2 | Chỉ cho phép 10.0.0.2, chặn phần còn lại của LAN | *(cập nhật sau)* |
| Tình huống 3 | Cô lập DMZ khỏi LAN | *(cập nhật sau)* |
| Tình huống 4 | Port Forward WAN:8080 → 172.16.0.2:80 | *(cập nhật sau)* |
| Tình huống 5 | Ghi log luật Block, xem Status → System Logs → Firewall | *(cập nhật sau)* |
| Câu hỏi | 6 câu hỏi phần D | *(cập nhật sau)* |

## Cấu trúc thư mục

| File / thư mục | Nội dung |
|---|---|
| `Lab5_11_ĐH_CNPM1_1150080014_HaChiHan.docx` | Báo cáo thực hành *(sẽ bổ sung)* |
| `Lab3-pfSense_SAMPLE_STYLE_SPLIT_FIGURES_v6_FINAL_TEXT_ONLY.docx` | Tài liệu hướng dẫn của giảng viên (tên gốc trong gói) |
| `SHA256SUMS.txt` | Mã băm SHA-256 chính thức của bộ cài pfSense |
| `images/` | Ảnh chụp màn hình minh chứng *(sẽ bổ sung)* |

## Lưu ý

- Bộ cài pfSense (.iso / .iso.gz) và file máy ảo không đưa lên repo do dung lượng lớn; tải tại https://atxfiles.netgate.com/mirror/downloads/ và kiểm tra SHA-256 trước khi dùng.
- pfSense 2.7.2 chỉ dùng trong mạng lab ảo cô lập, không dùng làm tường lửa thật.
- Mật khẩu các máy ảo không ghi trong repo hay báo cáo.
