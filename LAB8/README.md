# LAB 8 – Local DNS Attack (DNS Spoofing & Cache Poisoning)

| Thông tin | |
|---|---|
| Họ và tên | Hà Chí Hân |
| Mã số sinh viên | 1150080014 |
| Lớp | 11_ĐH_CNPM1 |
| Học phần | An toàn và bảo mật hệ thống thông tin |
| Kênh YouTube (video thực hành) | https://www.youtube.com/@chihan0403 |

## Mục tiêu

- Dựng DNS-SERVER chạy BIND9 làm resolver đệ quy/cache riêng cho mạng con cô lập DNSLAB 10.9.0.0/24 phía sau pfSense, kế thừa Lab 7.
- Thực hiện ba kịch bản của bài Local DNS Attack (SEED Labs): sửa /etc/hosts, Direct DNS Spoofing (tấn công thẳng vào Victim), DNS Cache Poisoning (đầu độc cache resolver).
- Bắt gói bằng Wireshark minh chứng Transaction ID, UDP port, bản ghi A giả mạo; khôi phục môi trường và phân tích biện pháp phòng thủ (DNS egress control, segmentation, DNSSEC, DNS Cookie).

## Môi trường thực hành

Kế thừa pfSense và DMZ-WEB của Lab 6/7 trên VMware Workstation 17.5.2 (tài liệu gốc viết cho VirtualBox), bổ sung interface DNSLAB và các máy mới:

| Máy | Hệ điều hành | Địa chỉ IP |
|---|---|---|
| pfSense-LAB6 | pfSense CE 2.7.2, thêm interface DNSLAB (OPT2) | DNSLAB 10.9.0.1/24, MGMT/LAN/DMZ giữ nguyên Lab 6/7 |
| DNS-SERVER (mới) | Ubuntu Server 24.04.5, BIND9 recursive/cache cho lab9.test | 10.9.0.10 |
| VICTIM (mới) | Ubuntu Server 24.04.5 | 10.9.0.100 |
| ATTACKER | Kali Linux 2026.2 (dùng lại, thêm NIC thứ 3), Scapy + Wireshark | 10.9.0.200 |
| APP-TEST/AUTH-DNS (mới) | Ubuntu Server 24.04.5, DNS authoritative mô phỏng, phản hồi chậm 1,5s | 172.16.6.20 |
| DMZ-WEB | Kế thừa Lab 6/7, web hợp lệ | 172.16.6.10 |

Khác biệt so với đề và lý do:
- Tài liệu gốc gọi bài là "LAB 9", kế thừa pfSense/APP-TEST từ một "LAB 8" trước đó; trên máy này không có lab đó nên bài được đánh số Lab 8, kế thừa trực tiếp Lab 7, và APP-TEST/AUTH-DNS được dựng mới trong chính bài này.
- Toàn bộ bước VirtualBox (Internal Network, Adapter, Promiscuous Mode = Allow VMs) được quy đổi sang VMware: LAN Segment mới dns-lab, thêm network adapter, bật promiscuous trong guest bằng ip link.

## Nội dung đã thực hiện (kết quả dự đoán)

| Mục | Nội dung | Kết quả dự đoán |
|---|---|---|
| pfSense DNSLAB | Interface OPT2 10.9.0.1/24, ruleset 5 dòng cô lập | Đúng kỳ vọng |
| DNS-SERVER | BIND9: named.conf.options/local, zone thuận/nghịch lab9.test | named-checkconf/checkzone thành công |
| APP-TEST/AUTH-DNS | auth_dns_delayed.py, delay 1,5s, www.external.lab9.test -> 172.16.6.10 | Đúng kỳ vọng |
| Baseline | www.external.lab9.test -> 172.16.6.10 trước khi tấn công | Đúng kỳ vọng |
| Task 1 – Hosts file | /etc/hosts -> 10.9.0.200; dig trực tiếp vẫn -> 172.16.6.10 | Đúng kỳ vọng |
| Task 2 – Direct DNS Spoofing | Victim nhận 10.9.0.200 khi phản hồi giả thắng cuộc đua | Đúng kỳ vọng |
| Task 3 – DNS Cache Poisoning | Cache DNS-SERVER giữ 10.9.0.200 trong TTL 120s | Đúng kỳ vọng |
| Khôi phục | rndc flush, kết quả trở lại 172.16.6.10 | Đúng kỳ vọng |
| Câu hỏi | 10 câu hỏi phân tích | Đã trả lời trong báo cáo |

Báo cáo được viết trước khi thực hành thật trên VM (quy trình giống Lab 6/7: viết báo cáo với kết quả dự đoán trước, thực hành sau). Làm xong mục nào mà kết quả thực tế khác dự đoán thì cập nhật lại báo cáo trước khi nộp chính thức. Hướng dẫn từng bước lệnh cụ thể nằm ở `D:\AnToanVaBaoMatThongTin\Lab8_ChuanBi_VMware.md` (không nằm trong repo).

## Cấu trúc thư mục

| File | Nội dung |
|---|---|
| Lab8_11_ĐH_CNPM1_1150080014_HaChiHan.docx | Báo cáo thực hành |
| LAB8_Local_DNS_Attack_pfSense_2026.docx | Tài liệu hướng dẫn của giảng viên |

## Lưu ý

- Mọi script giả mạo DNS (dns_spoof_lab9.py, auth_dns_delayed.py) chỉ chạy trong mạng DNSLAB cô lập 10.9.0.0/24, không chạy trên mạng thật.
- Chưa lưu ảnh minh chứng và file .pcapng vào thư mục này, sẽ bổ sung sau khi thực hành thật trên VM.
- config.xml của pfSense và mật khẩu các máy ảo không đưa lên repo công khai.
