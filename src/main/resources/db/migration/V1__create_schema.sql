CREATE TABLE users (
  id UUID PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE accounts (
  id UUID PRIMARY KEY,
  user_id UUID NOT NULL,
  currency VARCHAR(3) NOT NULL,
  balance DECIMAL(19,4) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE transaction_type (
    id INTEGER PRIMARY KEY,
    t_type VARCHAR(4) NOT NULL
);


CREATE TABLE account_transactions (
  id UUID PRIMARY KEY,
  account_id UUID NOT NULL,
  transaction_type INTEGER NOT NULL,
  amount DECIMAL(19,4) NOT NULL,
  currency VARCHAR(3) NOT NULL,
  exchange_rate DECIMAL(19,6),
  balance_before DECIMAL(19,4) NOT NULL,
  balance_after DECIMAL(19,4) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_ttype FOREIGN KEY (transaction_type) REFERENCES transaction_type(id),
  CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES accounts(id)
);

INSERT INTO transaction_type (id, t_type) VALUES (1, 'EXCH');
INSERT INTO transaction_type (id, t_type) VALUES (2, 'DEB');
INSERT INTO transaction_type (id, t_type) VALUES (3, 'ADD');
