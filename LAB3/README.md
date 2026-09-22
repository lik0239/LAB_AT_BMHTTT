# LAB 3 – Nhận diện và ứng phó các mối đe dọa đến an toàn thông tin

| Thông tin | |
|---|---|
| **Họ và tên** | Hà Chí Hân |
| **Mã số sinh viên** | 1150080014 |
| **Lớp** | 11_ĐH_CNPM1 |
| **Học phần** | An toàn và bảo mật hệ thống thông tin |
| **Kênh YouTube (video thực hành)** | https://www.youtube.com/@chihan0403 |

> **Trạng thái:** đang thực hiện. Báo cáo trong thư mục này là bản dựng khung đầy đủ theo đề bài; các mục ảnh chụp màn hình, số liệu đo được và kết luận PASS/FAIL sẽ được cập nhật sau khi hoàn tất thực hành trên máy ảo.

## Mục tiêu

- Phân biệt bốn khái niệm lỗ hổng (Vulnerability), mối đe dọa (Threat), rủi ro (Risk) và tấn công (Attack), gắn với tài sản cụ thể trên một máy trạm Windows.
- Nhận diện năm nhóm nguồn đe dọa: hành động vô ý, hành động cố ý, thảm họa tự nhiên, lỗi kỹ thuật và lỗi quản lý.
- Thu thập bằng chứng cho các nhóm kỹ thuật: mã độc, tấn công mật khẩu, keylogging, backdoor/persistence, DoS/DDoS, mail bombing, sniffing, man-in-the-middle, spoofing và social engineering/phishing.
- Thực hiện quy trình Baseline → Observe → Detect → Contain → Recover → Verify.

## Môi trường thực hành

Toàn bộ bài lab chạy trong một máy ảo Windows 11 đặt ở chế độ mạng **Host-only**, cô lập với mạng ngoài. Máy ảo được tạo snapshot sạch trước khi thực hành để khôi phục sau khi hoàn tất.

| Thành phần | Phiên bản |
|---|---|
| Nền tảng ảo hóa | VMware Workstation Pro *(cập nhật phiên bản thực tế)* |
| Máy ảo | Windows 11 25H2 x64 *(cập nhật OS build thực tế)* |
| Endpoint protection | Microsoft Defender Antivirus tích hợp Windows 11 |
| Shell | Windows PowerShell 5.1 |
| Sysmon / Autoruns / Process Explorer | Microsoft Sysinternals *(cập nhật phiên bản)* |
| Wireshark | *(cập nhật phiên bản)* + Npcap |
| Python | *(cập nhật phiên bản)* – chỉ dùng làm HTTP server cục bộ và tải thử trên 127.0.0.1 |

## Cách dựng môi trường

1. Tạo máy ảo Windows 11 25H2 x64 (tối thiểu 2 vCPU, 6 GB RAM, 64 GB đĩa), đặt Network Adapter = Host-only, cập nhật bản vá bảo mật, tạo snapshot sạch.
2. Tạo cấu trúc thư mục `C:\LAB3\{Evidence,Tools,Downloads,Assets}` và ghi mốc thời gian bắt đầu.
3. Kiểm tra SHA-256 gói `LAB3_Threats_Assets.zip` do giảng viên cung cấp rồi mới giải nén vào `C:\LAB3`.
4. Cài Python và Wireshark (giữ thành phần Npcap) bằng `winget`.
5. Tải Sysmon, Autoruns và Process Explorer trực tiếp từ `download.sysinternals.com`.

Chi tiết lệnh của từng bước nằm trong báo cáo.

## Các tình huống thực hành

| Tình huống | Nội dung | Kết quả |
|---|---|---|
| TH1 | Risk register (Asset → Vulnerability → Threat → Risk → Control) và phân loại 5 nguồn đe dọa | *(cập nhật sau)* |
| TH2 | Mã độc – kiểm chứng chu trình phát hiện bằng EICAR và Microsoft Defender | *(cập nhật sau)* |
| TH3 | Tấn công mật khẩu – audit logon, Event ID 4624/4625/4648, xoay vòng credential, phân tích keylogging | *(cập nhật sau)* |
| TH4 | Backdoor – persistence lành tính `LAB3_*`, listener 127.0.0.1:8080, truy vết bằng Sysmon/Autoruns/Process Explorer | *(cập nhật sau)* |
| TH5 | Sniffing / MITM / Spoofing – so sánh HTTP loopback và TLS/443 bằng Wireshark | *(cập nhật sau)* |
| TH6 | DoS / DDoS / Mail bombing – tải cục bộ giới hạn và phân tích dataset offline | *(cập nhật sau)* |
| TH7 | Social Engineering / Phishing – phân tích mẫu offline và phân loại 6 case | *(cập nhật sau)* |
| Cleanup | Gỡ artefact, so sánh Autoruns before/after, tính SHA-256 bằng chứng, revert snapshot | *(cập nhật sau)* |

## Cấu trúc thư mục

| File | Nội dung |
|---|---|
| `11_ĐH_CNPM1-LAB3_1150080014-HaChiHan.docx` | Báo cáo thực hành |
| `LAB3_CacMoiDeDoa_ATTT_2026.pdf` | Tài liệu hướng dẫn bài Lab |
| `README.md` | Mô tả bài Lab |

Sẽ bổ sung sau khi thực hành xong: các tệp output/log đã làm sạch và `evidence_sha256.csv`.

## Ranh giới an toàn của bài lab

- Mọi thao tác chỉ diễn ra trong máy ảo thuộc quyền quản lý, mạng Host-only.
- Không sửa `local_load_test.py` để trỏ tới mục tiêu khác ngoài `127.0.0.1:8080`.
- Không gửi email hàng loạt, không tạo DDoS, không thực hiện spoofing hoặc MITM chủ động ra ngoài máy ảo.
- Giữ Microsoft Defender và Tamper Protection bật; không tạo exclusion, không phục hồi tệp bị quarantine.
- Không nhập tài khoản thật, mật khẩu thật, token, cookie hay dữ liệu cá nhân vào máy ảo.
- Các domain `.example` và `.invalid` trong mẫu chỉ để đọc và phân tích, không truy cập.

## Lưu ý

- Ảnh chụp trong báo cáo phải lấy trực tiếp từ máy ảo của sinh viên, timestamp khớp với log thu được.
- Không đưa vào repository: installer, executable của Sysinternals/Wireshark/Python, tệp bị Defender quarantine, log chưa làm sạch, mật khẩu hoặc thông tin định danh hệ thống thật.
