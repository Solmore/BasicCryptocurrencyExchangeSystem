INSERT INTO users (username, password, roles) VALUES
                                                  ('user1@example.com', 'Z2Jnamtibmdram5iZ2tuYmdrbmJna2puYmdram5iZ2JqbmdrYmpnbmJsa25nbGtibmRrZmpuYmRramZuYmxkZ2JkbmdkYmxrbmdsYm5naw==', 'USER'),
                                                  ('user2@example.com', 'Z2Jnamtibmdram5iZ2tuYmdrbmJna2puYmdram5iZ2JqbmdrYmpnbmJsa25nbGtibmRrZmpuYmRramZuYmxkZ2JkbmdkYmxrbmdsYm5naw==', 'USER'),
                                                  ('user3@example.com', 'Z2Jnamtibmdram5iZ2tuYmdrbmJna2puYmdram5iZ2JqbmdrYmpnbmJsa25nbGtibmRrZmpuYmRramZuYmxkZ2JkbmdkYmxrbmdsYm5naw==', 'USER');
INSERT INTO crypto_currencies (code, name) VALUES
                                               ('BTC', 'Bitcoin'),
                                               ('ETH', 'Ethereum'),
                                               ('USDT', 'Tether');
INSERT INTO fiat_currencies (code, name, symbol) VALUES
                                                     ('USD', 'United States Dollar', '$'),
                                                     ('EUR', 'Euro', '€');
INSERT INTO crypto_prices (crypto_id, fiat_id, price) VALUES
                                                          (1, 1, 45000.00), -- BTC to USD
                                                          (1, 2, 38000.00), -- BTC to EUR
                                                          (2, 1, 3000.00),  -- ETH to USD
                                                          (2, 2, 2500.00),  -- ETH to EUR
                                                          (3, 1, 1.00),     -- USDT to USD
                                                          (3, 2, 0.85);     -- USDT to EUR
INSERT INTO wallets (user_id, currency, balance) VALUES
                                                               (1, 'BTC', 0.50), -- User1 holds 0.5 BTC
                                                               (1, 'ETH', 0.00),    -- User1 holds 0 ETH
                                                               (1, 'USDT', 0.00),    -- User1 holds 0 USDT
                                                               (1, 'USD', 1000.00),    -- User1 holds 1000 USD
                                                               (1, 'EUR', 0.00),    -- User1 holds 0 EUR
                                                               (2, 'BTC', 0.00), -- User2 holds 0 BTC
                                                               (2, 'ETH', 10.00), -- User2 holds 10 ETH
                                                               (2, 'USDT', 0.00),    -- User2 holds 0 USDT
                                                               (2, 'USD', 0.00),    -- User2 holds 0 USD
                                                               (2, 'EUR', 500.00),     -- User2 holds 500 EUR
                                                               (2, 'BTC', 0.00), -- User3 holds 0 BTC
                                                               (2, 'ETH', 0.00), -- User3 holds 0 ETH
                                                               (3, 'USDT', 10.00), -- User3 holds 10 USDT
                                                               (3, 'USD', 0.00),    -- User3 holds 0 USD
                                                               (3, 'EUR', 0.00);     -- User3 holds 0 EUR

INSERT INTO transactions (wallet_id, user_id, type, amount) VALUES
                                                                (1, 1, 'DEPOSIT', 0.50),  -- User1 deposited 0.5 BTC
                                                                (2, 1, 'DEPOSIT', 1000.00),     -- User1 deposited 1000 USD
                                                                (3, 2, 'DEPOSIT', 10.00), -- User2 deposited 10 ETH
                                                                (4, 2, 'DEPOSIT', 500.00),      -- User2 deposited 500 EUR
                                                                (5, 3, 'DEPOSIT', 100.00); -- User3 deposited 100 USDT
INSERT INTO orders (user_id, crypto_id, fiat_id, type, amount, price, status) VALUES
                                                                                  (1, 1, 1, 'BUY', 0.10, 45000.00, 'OPEN'),  -- User1 placed a BUY order for 0.1 BTC at $45,000
                                                                                  (2, 1, 2, 'SELL', 0.05, 38000.00, 'PARTIALLY_FILLED'),  -- User2 placed a SELL order for 0.05 BTC at €38,000
                                                                                  (3, 3, 1, 'BUY', 50.00, 1.00, 'OPEN');     -- User3 placed a BUY order for 50 USDT at $1.00
