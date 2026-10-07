Write-Host "==================================================" -ForegroundColor Cyan
Write-Host "  GUI DU LIEU MAU VAO CAC TOPIC                   " -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan

# 1. Gửi dữ liệu Nhập kho
Write-Host "`n[1] Gui tin nhan vao medicine-stock-events..." -ForegroundColor Yellow
'{"item_id": "PARACETAMOL-500", "type": "IMPORT", "quantity": 1000, "timestamp": "2026-10-07T10:00:00Z"}' | `
docker exec -i pharmacy-kafka-broker /opt/kafka/bin/kafka-console-producer.sh `
  --bootstrap-server localhost:9092 `
  --topic medicine-stock-events

# 2. Gửi dữ liệu Cập nhật giá
Write-Host "`n[2] Gui tin nhan vao medicine-price-updates..." -ForegroundColor Yellow
'{"item_id": "PARACETAMOL-500", "old_price": 1500, "new_price": 1800, "timestamp": "2026-10-07T10:05:00Z"}' | `
docker exec -i pharmacy-kafka-broker /opt/kafka/bin/kafka-console-producer.sh `
  --bootstrap-server localhost:9092 `
  --topic medicine-price-updates

# 3. Gửi dữ liệu Thông báo
Write-Host "`n[3] Gui tin nhan vao pharmacy-notifications..." -ForegroundColor Yellow
'{"user_id": "CUST-001", "message": "Don hang thuoc #12345 da duoc xac nhan", "channel": "SMS"}' | `
docker exec -i pharmacy-kafka-broker /opt/kafka/bin/kafka-console-producer.sh `
  --bootstrap-server localhost:9092 `
  --topic pharmacy-notifications

Write-Host "`n==================================================" -ForegroundColor Green
Write-Host "  DA GUI XONG DU LIEU MAU!                        " -ForegroundColor Green
Write-Host "==================================================" -ForegroundColor Green