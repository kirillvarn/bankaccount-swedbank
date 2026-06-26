# Starting the application


```bash
$ make build
```
Builds the docker image and start docker compose services.

Rename example.env -> .env before building.

To request this application endpoint you need a valid JWT signed using the token under /certs.
To sign a JWT use jwtsigner utility:

```bash
$ jwtsigner[.bat] -c src\main\resources\certs\private.pem username
```

And use with curl.
For instance, to add a transaction:

```bash
curl --request POST \
  --url http://localhost:8080/api/v1/transactions \
  --header 'authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJjMTkwOTU5Ni1kYTlhLTQ1NWEtYWNhMy1hOTJmZTQ0MDFiYmUiLCJ1c2VyX2lkIjoiYzE5MDk1OTYtZGE5YS00NTVhLWFjYTMtYTkyZmU0NDAxYmJlIiwiaWF0IjoxNzgyNDYxODk3LCJleHAiOjE3ODI1NDgyOTd9.pvF4pAG6ISC6S7d03opabhPQg26EQQaVVMkG6wmC5KKIm74e1R0Ilh0hK3nHMYm7QPek1sIyN12pdhIe7ffEhIDhTtDaRdjYrRmUggHsXdEewhAgl1Wwi8_uPKCrgaUQ6N-9XxDPR9BFEZwufE0GkWn_7VrK0X-BVbzFDZe1u69SAOyJ4XxIaTQkTK1YPN8xS1kFmjBFwHBRSh3HUhbuuJ6XYAUnVPyg7bgYC4gHsdu5EyAFPpb64yCPt_JQKIQe3s01Mr-eVI5EqSPn_LKlAP1c508p6YT2aq70hKFblcYQTFxcnJDgjB62zTSZQn5VxXD3-oWYbyiadEja4gv3Fw' \
  --header 'content-type: application/json' \
  --data '{
  "currency": "EUR",
  "accountId": "4fd34087-4eba-4763-8198-64b015956c17",
  "amount": 5,
  "transactionType": "DEB"
}'
```