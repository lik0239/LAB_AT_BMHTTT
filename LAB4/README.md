# LAB 4 – Khảo sát và đánh giá bề mặt mạng bằng Nmap

| Thông tin | |
|---|---|
| **Họ và tên** | Hà Chí Hân |
| **Mã số sinh viên** | 1150080014 |
| **Lớp** | 11_ĐH_CNPM1 |
| **Học phần** | An toàn và bảo mật hệ thống thông tin |
| **Kênh YouTube (video thực hành)** | https://www.youtube.com/@chihan0403 |

> **Trạng thái:** đang thực hiện – đã dựng môi trường, cài Nmap trên Windows và Kali; kết quả các lần quét sẽ được cập nhật sau khi hoàn tất thực hành.

## Mục tiêu

- Cài và kiểm tra Nmap trên Windows 11 và Kali Linux; hiểu vai trò của Npcap.
- Dựng mạng Host-only an toàn, xác định IP của máy quét và máy đích.
- Thực hiện host discovery, TCP/UDP scan, service/OS detection, NSE script phòng thủ.
- Giải thích các trạng thái open / closed / filtered, so sánh các kỹ thuật quét.
- Xuất kết quả làm hồ sơ bằng chứng; chứng minh hiệu quả hardening bằng quét trước/sau.

## Môi trường thực hành

Tài liệu viết cho VirtualBox (192.168.56.0/24); bài làm dùng VMware Workstation nên mạng Host-only tương đương là **VMnet1 – 192.168.222.0/24**, không có Internet khi quét.

| Thiết bị | Hệ điều hành / công cụ | IP |
|---|---|---|
| Máy thật | Windows 11, Nmap 7.991 + Npcap 1.88 + Zenmap | 192.168.222.1 |
| Máy quét | Kali Linux 2026.2, Nmap 7.99, Zenmap, xsltproc | 192.168.222.130 |
| Máy đích | Metasploitable 2 (Ubuntu 8.04, kernel 2.6.24) | 192.168.222.132 |
| Nền tảng | VMware Workstation 17.5.2 | VMnet1 Host-only |

## Nội dung đã thực hiện

| Mục | Nội dung | Kết quả |
|---|---|---|
| Cài đặt | Nmap trên Windows (kiểm tra SHA-256 + chữ ký số), Nmap/Zenmap/xsltproc trên Kali | ✅ Hoàn thành |
| Môi trường | Host-only VMnet1, Metasploitable 2 chỉ một card Host-only, snapshot Before-LAB4, lấy IP từng máy | ✅ Hoàn thành |
| Nhiệm vụ 1 | Host discovery `-sn` toàn dải /24 | *(cập nhật sau)* |
| Quét TCP | `-sT`, `-sS`, FIN/Xmas/NULL, ACK | *(cập nhật sau)* |
| Quét UDP | 20 cổng UDP phổ biến | *(cập nhật sau)* |
| Nhận diện | `-sV`, `-O`, `-A` | *(cập nhật sau)* |
| NSE | `smb-os-discovery`, `smb-vuln-ms17-010` | *(cập nhật sau)* |
| Xuất kết quả | `-oN`, `-oX`, `-oG` + grep, XML → HTML bằng xsltproc | *(cập nhật sau)* |
| Hardening | Quét before/after khi chặn Telnet (23) và FTP (21) bằng iptables trên máy đích | *(cập nhật sau)* |
| Bài tập bổ sung | `-p-`, CVE, decoy, `-oA`, bản đồ dịch vụ | *(cập nhật sau)* |

## Cấu trúc thư mục

| File / thư mục | Nội dung |
|---|---|
| `Lab4_11_ĐH_CNPM1_1150080014_HaChiHan.docx` | Báo cáo thực hành |
| `LAB4_Nmap_HuongDan_2026.docx` | Tài liệu hướng dẫn của giảng viên |
| `images/` | Ảnh chụp màn hình minh chứng |
| `evidence/` | Tệp kết quả Nmap (.txt, .xml, .gnmap, .html) |

## Lưu ý

- Chỉ quét các máy ảo của chính sinh viên trong mạng Host-only; không quét IP/tên miền bên ngoài.
- Metasploitable 2 là máy cố ý có lỗ hổng – tuyệt đối không nối Bridged/NAT khi đang chạy.
- NSE chỉ dùng để nhận diện dấu hiệu rủi ro, không khai thác lỗ hổng.
- Sau phần hardening đã xóa luật firewall và có thể khôi phục snapshot Before-LAB4.
