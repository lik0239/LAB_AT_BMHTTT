# LAB 8 – Local DNS Attack (DNS Spoofing & Cache Poisoning)

| Thông tin | |
|---|---|
| **Họ và tên** | Hà Chí Hân |
| **Mã số sinh viên** | 1150080014 |
| **Lớp** | 11_ĐH_CNPM1 |
| **Học phần** | An toàn và bảo mật hệ thống thông tin |
| **Kênh YouTube (video thực hành)** | https://www.youtube.com/@chihan0403 |

## Mục tiêu

- Dựng DNS-SERVER chạy BIND9 làm resolver đệ quy/cache riêng cho một mạng con cô lập phía sau pfSense.
- Thực hiện ba kịch bản của bài Local DNS Attack (SEED Labs): sửa `/etc/hosts`, Direct DNS Spoofing, DNS Cache Poisoning.
- Bắt gói bằng Wireshark minh chứng transaction ID, UDP port, bản ghi A giả mạo; khôi phục môi trường và phân tích biện pháp phòng thủ.

## Trạng thái hiện tại

**Đang chuẩn bị môi trường, chưa thực hành.** Thư mục này mới có tài liệu phân tích và kế hoạch, chưa có máy ảo nào được dựng riêng cho Lab 8 và chưa có ảnh minh chứng.

## Ghi chú quan trọng

- Tài liệu hướng dẫn gốc viết theo thuật ngữ VirtualBox (Internal Network, Promiscuous Mode); môi trường thực tế trên máy này dùng **VMware Workstation 17.5.2** kế thừa từ Lab 5-7 (`C:\VM\LAB6-PFSENSE`, `LAB6-DMZWEB`, `LAB7-WEBPROXY`), nên các bước mạng sẽ được quy đổi sang VMware (vSwitch Host-only riêng, Allow Promiscuous ở cấp vmnet) thay vì cài VirtualBox riêng.
- Ổ D còn trống thấp (~31,5 GB); các máy ảo mới của Lab 8 (DNS-SERVER, VICTIM) sẽ dựng tại `C:\VM` như các lab trước, không dựng trên ổ D.
- Mọi script giả mạo DNS chỉ chạy trong mạng lab cô lập, không áp dụng ngoài môi trường VM.

## Cấu trúc thư mục

| File | Nội dung |
|---|---|
| `Lab8_PhanTich_MoiTruong_11_ĐH_CNPM1_1150080014_HaChiHan.docx` | Phân tích môi trường máy và kế hoạch thực hiện (chưa phải báo cáo kết quả) |
| `LAB8_Local_DNS_Attack_pfSense_2026.docx` | Tài liệu hướng dẫn của giảng viên |

## Việc còn lại

- Thêm vSwitch Host-only riêng và NIC mới cho pfSense.
- Dựng DNS-SERVER (Ubuntu Server 24.04 + BIND9) và VICTIM (Ubuntu 24.04) tại `C:\VM`; dùng lại VM Kali hiện có cho vai trò Attacker.
- Thực hiện ba kịch bản tấn công, chụp đủ ảnh minh chứng, trả lời 10 câu hỏi phân tích.
- Viết báo cáo chính thức `Lab8_11_ĐH_CNPM1_1150080014_HaChiHan.docx` và cập nhật README này sau khi hoàn thành.
