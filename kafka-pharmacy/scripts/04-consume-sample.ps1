param (
    [string]$Topic = "medicine-stock-events"
)

Write-Host "==================================================" -ForegroundColor Cyan
Write-Host "  DOC TIN NHAN TU TOPIC: $Topic" -ForegroundColor Cyan
Write-Host "  (Bam Ctrl+C de thoat)" -ForegroundColor Yellow
Write-Host "==================================================" -ForegroundColor Cyan

docker exec -i pharmacy-kafka-broker /opt/kafka/bin/kafka-console-consumer.sh `
  --bootstrap-server localhost:9092 `
  --topic $Topic `
  --from-beginning