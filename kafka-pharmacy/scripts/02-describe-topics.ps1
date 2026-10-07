Write-Host "==================================================" -ForegroundColor Cyan
Write-Host "  DANH SACH TAT CA CAC TOPIC HIEN CO             " -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan
docker exec -i pharmacy-kafka-broker /opt/kafka/bin/kafka-topics.sh `
  --bootstrap-server localhost:9092 `
  --list

Write-Host "`n==================================================" -ForegroundColor Cyan
Write-Host "  CHI TIET PHAN BO PARTITION CUA TUNG TOPIC       " -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan
docker exec -i pharmacy-kafka-broker /opt/kafka/bin/kafka-topics.sh `
  --bootstrap-server localhost:9092 `
  --describe