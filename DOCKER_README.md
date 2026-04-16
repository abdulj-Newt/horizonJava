# Customer Service Docker Setup

## Overview
This Docker setup includes:
- Oracle XE 21c database container
- Spring Boot customer service application
- Automatic schema and stored procedure initialization

## Prerequisites
- Docker and Docker Compose installed
- Ports 1521 (Oracle) and 8083 (App) available

## Quick Start

### Build and Run
```bash
docker-compose up --build
```

### Run in detached mode
```bash
docker-compose up -d
```

### Stop containers
```bash
docker-compose down
```

### Stop and remove volumes
```bash
docker-compose down -v
```

## Database Connection
- Host: localhost
- Port: 1521
- SID: XE
- Username: system
- Password: oracle

## Application
- URL: http://localhost:8083
- The application will wait for the database to be healthy before starting

## Schema Initialization
The following SQL files are automatically executed on first run:
1. dbschema.sql - Creates tables and sequences
2. schema.sql - Creates TRANSFER_MONEY procedure
3. add-money-procedure.sql - Creates ADD_MONEY procedure
4. bill-pay-procedure.sql - Creates PAY_BILL procedure

## Troubleshooting

### Check logs
```bash
docker-compose logs -f customer-service
docker-compose logs -f oracle-db
```

### Restart application only
```bash
docker-compose restart customer-service
```

### Connect to Oracle container
```bash
docker exec -it customer-service-oracle sqlplus system/oracle@XE
```
