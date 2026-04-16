# Customer Service Application - API Documentation

## Application Status
✅ **Application is running successfully on port 8083**

## Base URL
```
http://localhost:8083
```

## Available API Endpoints

### Health Check
- **GET** `/api/customers/healthCheck`
  - Returns: `{"status":"OK"}`

### Customer Endpoints
- **GET** `/api/customers` - Get all customers
- **GET** `/api/customers/{id}` - Get customer by ID
- **GET** `/api/customers/{customerId}/account` - Get account by customer ID

### Account Endpoints
- **GET** `/api/accounts?username={username}` - Get account by username
- **GET** `/api/accounts/customer/{customerId}` - Get account by customer ID
- **GET** `/api/accounts/{accountId}/balance` - Get account balance
- **POST** `/api/accounts/{accountId}/deposit` - Deposit money
  - Body: `{"amount": 100.00}`
- **POST** `/api/accounts/{accountId}/withdraw` - Withdraw money
  - Body: `{"amount": 50.00}`
- **POST** `/api/accounts/add-money` - Add money to account
  - Body: `{"customerId": 1, "amount": 100.00}`

### Transaction Endpoints
- **GET** `/api/transactions/customer/{customerId}` - Get all transactions for a customer
- **POST** `/api/transactions` - Transfer money between customers
  - Body: `{"fromCustomerId": 1, "toCustomerId": 2, "amount": 50.00}`
- **POST** `/api/transactions/plsql` - Transfer money using PL/SQL stored procedure
  - Body: `{"fromCustomerId": 1, "toCustomerId": 2, "amount": 50.00}`
- **POST** `/api/transactions/add-money` - Add money using PL/SQL
  - Body: `{"customerId": 1, "amount": 100.00}`
- **POST** `/api/transactions/add-money/plsql` - Add money using PL/SQL stored procedure
  - Body: `{"customerId": 1, "amount": 100.00}`
- **POST** `/api/transactions/bill-pay` - Pay bill
  - Body: `{"customerId": 1, "billType": "ELECTRICITY", "accountNumber": "ACC123", "amount": 75.00}`
- **POST** `/api/transactions/bill-pay/plsql` - Pay bill using PL/SQL stored procedure
  - Body: `{"customerId": 1, "billType": "ELECTRICITY", "accountNumber": "ACC123", "amount": 75.00}`

## Database Verification
✅ Tables created successfully in APPUSER schema:
- CUSTOMERS
- ACCOUNTS
- TRANSACTIONS

✅ Stored Procedures created:
- TRANSFER_MONEY
- ADD_MONEY
- PAY_BILL

## Testing the Application

### 1. Health Check
```bash
curl http://localhost:8083/api/customers/healthCheck
```

### 2. Get All Customers
```bash
curl http://localhost:8083/api/customers
```

### 3. Transfer Money (Example)
```bash
curl -X POST http://localhost:8083/api/transactions/plsql \
  -H "Content-Type: application/json" \
  -d '{"fromCustomerId": 1, "toCustomerId": 2, "amount": 50.00}'
```

### 4. Add Money (Example)
```bash
curl -X POST http://localhost:8083/api/transactions/add-money/plsql \
  -H "Content-Type: application/json" \
  -d '{"customerId": 1, "amount": 100.00}'
```

### 5. Pay Bill (Example)
```bash
curl -X POST http://localhost:8083/api/transactions/bill-pay/plsql \
  -H "Content-Type: application/json" \
  -d '{"customerId": 1, "billType": "ELECTRICITY", "accountNumber": "ACC123", "amount": 75.00}'
```

## Container Status
- **Oracle Database**: Running on port 1521 (FREEPDB1)
- **Customer Service App**: Running on port 8083

## Notes
- All endpoints support CORS for `http://localhost:5173` (frontend)
- Amount validation: All transaction amounts must be > 0
- Database: Oracle Free 23c with APPUSER schema
