## Public API 


GET  /api/v1/accounts/{id}  
```
body: {}  
params: { id: UUID }
```

GET  /api/v1/accounts/{id}/transactions  
```
body: {}  
params: { id: UUID }
```

POST /api/v1/transactions  
```
body: {
    type: TransactionType
    amount: BigDecimal
    accountId: String
}
```

POST /api/v1/exchanges  
```
body: {
    from: Currency  
    to: Currency
    accountId: String
    amount: BigDecimal
}
```
