# LAB 6 – Xác thực Active Directory và chính sách truy cập trên pfSense

| Thông tin | |
|---|---|
| **Họ và tên** | Hà Chí Hân |
| **Mã số sinh viên** | 1150080014 |
| **Lớp** | 11_ĐH_CNPM1 |
| **Học phần** | An toàn và bảo mật hệ thống thông tin |
| **Kênh YouTube (video thực hành)** | https://www.youtube.com/@chihan0403 |

## Mục tiêu

- Dựng pfSense riêng cho Lab 6 với bốn interface WAN, MGMT, LAN, DMZ.
- Tích hợp pfSense với Active Directory (`lab6.test`) qua LDAP bằng service account đặc quyền thấp.
- Cấu hình Captive Portal trên LAN xác thực người dùng domain `sv_a`, `sv_b`.
- Tạo Alias, Schedule và thực hiện 7 kịch bản chính sách: chặn site theo máy, chặn nhóm site, cho phép theo khung giờ, giới hạn dịch vụ trong giờ hành chính, cô lập DMZ, so sánh Block/Reject, đọc Firewall Log và State Table.

## Môi trường thực hành

Tài liệu viết cho VirtualBox; bài làm dùng **VMware Workstation 17.5.2**, các mạng được ánh xạ tương đương và **không dùng máy thật** trong mô hình:

| Vùng | VirtualBox (theo đề) | VMware (bài làm) | Dải địa chỉ |
|---|---|---|---|
| WAN | NAT Network `LAB6-WAN` | VMnet8 – NAT | DHCP 192.168.10.x |
| MGMT | Host-Only | LAN Segment `lab6-mgmt` | 192.168.56.0/24 |
| LAN | Internal `lab6-lan` | LAN Segment `lab6-lan` | 10.6.0.0/24 |
| DMZ | Internal `lab6-dmz` | LAN Segment `lab6-dmz` | 172.16.6.0/24 |

| Máy | Hệ điều hành | Địa chỉ IP |
|---|---|---|
| pfSense-LAB6 | pfSense CE 2.7.2-RELEASE | WAN DHCP · MGMT 192.168.56.254 · LAN 10.6.0.1 · DMZ 172.16.6.1 |
| Máy quản trị | Kali Linux 2026.2 (thay máy thật) | 192.168.56.1 |
| DC01 | Windows Server 2025 – AD DS + DNS `lab6.test` | 10.6.0.10 |
| Client-A / Client-B | Kali Linux 2026.2 (linked clone) | 10.6.0.21 / 10.6.0.22 |
| DMZ-WEB | Ubuntu Server 26.04.1 + Nginx | 172.16.6.10 / .20 / .30 / .40 |

**Khác biệt so với đề và lý do:** máy thật chỉ có 13,9 GB RAM (đề khuyến nghị 16–24 GB) nên Client-A/B dùng Kali thay Windows 11 – Captive Portal chỉ cần trình duyệt và policy khớp theo IP; máy quản trị là VM Kali để toàn bộ mô hình nằm trong mạng ảo cô lập; Windows Server 2025 và Ubuntu 26.04.1 là phiên bản có sẵn.

## Nội dung đã thực hiện

| Mục | Nội dung | Kết quả |
|---|---|---|
| Môi trường | pfSense 4 interface, DC01, 2 client, DMZ-WEB 4 IP | ✅ Hoàn thành |
| LDAP | Authentication Server `LAB6-AD`, test sv_a / sv_b | ✅ Xác thực thành công |
| Captive Portal | Zone `LAB6_USERS` trên LAN, backend LAB6-AD | ✅ sv_a, sv_b đăng nhập được |
| Alias / Schedule | 8 alias, LUNCH_WINDOW, WORK_HOURS, TEST_NOW | ✅ Hoàn thành |
| Kịch bản 1 | Client-A bị chặn JOB_SITE | ✅ Đúng dự đoán |
| Kịch bản 2 | Cấm RESTRICTED_SITE cho A và B | ✅ Đúng dự đoán |
| Kịch bản 3 | Client-B chỉ được SOCIAL_SITE trong giờ nghỉ | ✅ Đúng dự đoán |
| Kịch bản 4 | Chỉ HTTP/HTTPS trong giờ hành chính | ✅ Đúng dự đoán |
| Kịch bản 5 | Cô lập DMZ khỏi LAN | ✅ Đúng dự đoán |
| Kịch bản 6–7 | Block vs Reject, Firewall Log, State Table | ✅ Đúng dự đoán |
| Câu hỏi | 12 câu hỏi phân tích | ✅ Đã trả lời trong báo cáo |

## Cấu trúc thư mục

| File | Nội dung |
|---|---|
| `Lab6_11_ĐH_CNPM1_1150080014_HaChiHan.docx` | Báo cáo thực hành |
| `LAB6_pfSense_AD_Access_Policies_2026.docx` | Tài liệu hướng dẫn của giảng viên |

## Lưu ý

- File `config.xml` của pfSense chứa mật khẩu bind LDAP và mật khẩu quản trị (dạng băm) nên không đưa lên repo công khai; nộp riêng theo yêu cầu của giảng viên.
- Cấu hình LDAP TCP/389 không mã hóa chỉ dùng trong mạng lab cô lập; triển khai thật cần LDAPS với chứng chỉ phù hợp.
- File máy ảo và bộ cài không đưa lên repo. Mật khẩu các máy ảo không ghi trong repo hay báo cáo.
