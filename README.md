# Wallet Service

Сервис для управления цифровыми кошельками с поддержкой высокой конкурентной нагрузки.

## Основные возможности

- Обработка операций пополнения и снятия средств
- Поддержка высокой конкурентной нагрузки (1000 RPS на кошелек)
- Отказоустойчивость и обработка ошибок
- Масштабируемость через Docker

## API Endpoints

### Операции с кошельком
POST /api/v1/wallet
```json
{
    "walletId": "UUID",
    "operationType": "DEPOSIT/WITHDRAW",
    "amount": 1000.00
}
```

### Получение баланса
GET /api/v1/wallets/{WALLET_UUID}

## Требования

- Java 17
- Docker и Docker Compose
- PostgreSQL 13+

## Запуск приложения

1. Клонировать репозиторий:
```bash
git clone https://github.com/Naifff/Wallet-Service-with-REST-API-and-Docker.git
```

2. Запустить приложение через Docker Compose:
```bash
docker-compose up -d
```

## Конфигурация

Основные параметры можно настроить через переменные окружения:

- `DB_HOST` - хост базы данных
- `DB_PORT` - порт базы данных
- `DB_POOL_SIZE` - размер пула соединений
- `SERVER_MAX_THREADS` - максимальное количество потоков

## Мониторинг

Сервис предоставляет метрики в формате Prometheus по адресу:
/actuator/prometheus

## Тестирование

Запуск тестов:
```bash
./mvnw test
```

## Безопасность

- Валидация входных данных
- Защита от конкурентных конфликтов
- Аудит операций

## Производительность

- Оптимизированные индексы базы данных
- Кэширование частых запросов
- Батчинг операций
- Очереди для пиковых нагрузок