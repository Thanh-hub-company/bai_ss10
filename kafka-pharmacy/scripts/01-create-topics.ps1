Write-Host "==================================================" -ForegroundColor Cyan
Write-Host "  BAT DAU TAO CAC TOPIC HE THONG NHA THUOC       " -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan

# 1. Topic Nhap/Xuat kho (3 Partitions - Toi uu xu ly song song)
Write-Host "`n[1/3] Tao topic: medicine-stock-events (3 partitions)..." -ForegroundColor Yellow
docker exec -i pharmacy-kafka-broker /opt/kafka/bin/kafka-topics.sh `
  --bootstrap-server localhost:9092 `
  --create `
  --topic medicine-stock-events `
  --partitions 3 `
  --replication-factor 1

# 2. Topic Cap nhat gia (1 Partition - Dam bao thu tu tuyet doi)
Write-Host "`n[2/3] Tao topic: medicine-price-updates (1 partition)..." -ForegroundColor Yellow
docker exec -i pharmacy-kafka-broker /opt/kafka/bin/kafka-topics.sh `
  --bootstrap-server localhost:9092 `
  --create `
  --topic medicine-price-updates `
  --partitions 1 `
  --replication-factor 1

# 3. Topic Thong bao (2 Partitions - Can bang tai)
Write-Host "`n[3/3] Tao topic: pharmacy-notifications (2 partitions)..." -ForegroundColor Yellow
docker exec -i pharmacy-kafka-broker /opt/kafka/bin/kafka-topics.sh `
  --bootstrap-server localhost:9092 `
  --create `
  --topic pharmacy-notifications `
  --partitions 2 `
  --replication-factor 1

Write-Host "`n==================================================" -ForegroundColor Green
Write-Host "  DA HOAN TAT TAO TOAN BO TOPIC!                  " -ForegroundColor Green
Write-Host "==================================================" -ForegroundColor Green