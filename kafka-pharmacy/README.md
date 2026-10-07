# 🏥 Hệ Thống Quản Lý Kafka Nhà Thuốc (Pharmacy System)

Hệ thống kiến trúc Event-Driven kết hợp **Apache Kafka (KRaft mode)** chạy trên **Docker** và dịch vụ xử lý đơn hàng **Spring Boot (`pharmacy-service`)**.

---

## 🏗️ Kiến Trúc & Nguyên Lý Hoạt Động

### 1. Luồng dữ liệu (Data Flow)
```
[Client / Postman] 
       │ (HTTP POST /api/orders/checkout)
       ▼
[Pharmacy Service (Spring Boot :8081)]
       │
       ▼ (KafkaTemplate: serialize Object -> JSON)
[Kafka Broker (Docker :9094)]
       │
       ├───> Topic `medicine-stock-events` (3 Partitions - Xử lý song song kho/đơn hàng)
       ├───> Topic `medicine-price-updates` (1 Partition - Đảm bảo thứ tự giá)
       └───> Topic `pharmacy-notifications` (2 Partitions - Cân bằng tải thông báo)
       │
       ▼
[Consumers / Downstream Services / CLI Consumer]
```

### 2. Thiết kế Topic & Partition theo nghiệp vụ
| Tên Topic | Số Partitions | Mục đích & Nguyên lý |
| :--- | :---: | :--- |
| `medicine-stock-events` | **3** | Lưu trữ các sự kiện xuất/nhập/bán thuốc. 3 partition giúp các consumer xử lý song song, tăng throughput (thông lượng) xử lý tồn kho. Key là `medicineId` giúp các sự kiện cùng loại thuốc luôn vào cùng partition. |
| `medicine-price-updates` | **1** | Cập nhật bảng giá thuốc. Duy nhất 1 partition để đảm bảo thứ tự thời gian nghiêm ngặt (FIFO), tránh tình trạng ghi đè sai giá cũ - giá mới. |
| `pharmacy-notifications` | **2** | Gửi thông báo SMS/Email đến khách hàng và dược sĩ. 2 partition giúp phân tải xử lý gửi thông báo đồng thời. |

---

## 🚀 Hướng Dẫn Vận Hành & Chạy Hệ Thống

### Bước 1: Khởi động Kafka Broker (Docker KRaft Mode)
Mở PowerShell tại thư mục gốc của project:
```powershell
docker compose up -d
```
- **Port 9092**: Dành cho giao tiếp nội bộ giữa các container trong Docker network.
- **Port 9094**: Cổng external exposed ra ngoài cho Spring Boot (`localhost:9094`).

---

### Bước 2: Tạo các Topic nghiệp vụ
Chạy script PowerShell tự động khởi tạo 3 Topic:
```powershell
.\scripts\01-create-topics.ps1
```

---

### Bước 3: Kiểm tra cấu hình & phân bổ Partition
Kiểm tra danh sách topic và chi tiết các partition (Leader, Replicas, ISR):
```powershell
.\scripts\02-describe-topics.ps1
```

---

### Bước 4: Khởi động Spring Boot Service (`pharmacy-service`)

#### Cách 1: Chạy qua Maven Wrapper
Mở cửa sổ PowerShell mới:
```powershell
cd pharmacy-service
.\mvnw.cmd spring-boot:run
```

#### Cách 2: Chạy trực tiếp từ IntelliJ IDEA
- Mở class `com.example.pharmacyservice.PharmacyServiceApplication`
- Nhấn nút **Run** (icon tam giác màu xanh ▶️).
- Service sẽ chạy tại địa chỉ: `http://localhost:8081`

---

### Bước 5: Kiểm Thử Hoạt Động

#### 1. Lắng nghe tin nhắn (Consumer)
Mở một cửa sổ PowerShell mới để chạy script lắng nghe dữ liệu từ Topic `medicine-stock-events`:
```powershell
.\scripts\04-consume-sample.ps1 -Topic medicine-stock-events
```

#### 2. Gửi yêu cầu đặt/bán thuốc qua REST API (Producer)
Mở một cửa sổ PowerShell khác và thực hiện gọi API checkout:
```powershell
Invoke-RestMethod -Uri "http://localhost:8081/api/orders/checkout" `
  -Method POST `
  -Headers @{"Content-Type"="application/json"} `
  -Body '{"medicineId": "MED-PARACETAMOL-500", "quantity": 10}'
```

**Kết quả trả về:**
```json
{
  "orderId": "ORD-1728345678901",
  "medicineId": "MED-PARACETAMOL-500",
  "quantity": 10,
  "status": "CREATED",
  "eventTimestamp": "2026-10-07T22:45:00.000+07:00"
}
```
Đồng thời tại cửa sổ Consumer ở bước trên, bạn sẽ thấy tin nhắn JSON tương ứng xuất hiện ngay lập tức.

#### 3. Gửi dữ liệu mẫu trực tiếp vào Kafka qua Script CLI
```powershell
.\scripts\03-produce-sample.ps1
```

---

### 🛑 Dừng hệ thống khi kết thúc
```powershell
docker compose down
```