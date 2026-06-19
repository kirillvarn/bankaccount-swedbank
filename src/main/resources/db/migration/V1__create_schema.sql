CREATE TABLE users (
  id UUID PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE exchanges (
    id UUID PRIMARY KEY,
    from_currency CHAR(3) NOT NULL,
    to_currency CHAR(3) NOT NULL,
    exchange_rate DECIMAL(19,6) NOT NULL,
    balance_before DECIMAL(19,4) NOT NULL,
    balance_after DECIMAL(19,4) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE accounts (
  id UUID PRIMARY KEY,
  user_id UUID NOT NULL,
  currency VARCHAR(3) NOT NULL,
  balance DECIMAL(19,4) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id),
  UNIQUE(user_id, currency)
);

CREATE TABLE account_transactions (
  id UUID PRIMARY KEY,
  account_id UUID NOT NULL,
  exchange_id UUID,
  transaction_type VARCHAR(3) NOT NULL,
  amount DECIMAL(19,4) NOT NULL,
  currency VARCHAR(3) NOT NULL,
  balance_before DECIMAL(19,4) NOT NULL,
  balance_after DECIMAL(19,4) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_exch FOREIGN KEY (exchange_id) REFERENCES exchanges(id),
  CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES accounts(id)
);

insert into users (id, username, created_at) values ('123e4567-e89b-12d3-a456-426614174000', 'User 1', now());
insert into users (id, username, created_at) values ('123e4567-e89b-12d3-a456-426614174001', 'User 2', now());
