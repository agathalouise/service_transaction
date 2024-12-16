-- ====================
-- Populate: wallet
-- ====================
INSERT INTO wallet (id, food_balance, meal_balance, culture_balance, transport_balance, cash_balance) VALUES (1, 100.00, 50.00, 30.00, 20.00, 200.00);
INSERT INTO wallet (id, food_balance, meal_balance, culture_balance, transport_balance, cash_balance) VALUES (2, 200.00, 100.00, 50.00, 40.00, 300.00);

-- ====================
-- Populate: account
-- ====================
INSERT INTO account (id, hashed_pin, hashed_cvv, wallet_id) VALUES (1, "$2a$10$p6easLW8cUxCMjtcFv.H/OCfc7PLBEvxfzIpbx6XY/jlhvDxGj4sC", "$2a$10$MSQil9A78dcRmm/12mBGv..TYqTzeSup.rwvArkHDja4WdUCKUax2", 1); -- PIN: 1111, CVV: 123
INSERT INTO account (id, hashed_pin, hashed_cvv, wallet_id) VALUES (2, "$2a$10$HnufSbt9B4LEVHfs.KCkjuABfkbPoncuoifKPFb.jR9GwPEKXniLK", "$2a$10$f3fsnynkQh0TcaieGLiEgOK.JXDqjvxdQkhAf9Cp4vn7pu4yOxa2i", 2); -- PIN: 2222, CVV: 456

-- ====================
-- Populate: transaction_card
-- ====================
INSERT INTO transaction_card (amount, mcc, merchant, created_date, wallet_id) VALUES (50.00, "5411", "SUPERMARKET A", "2024-12-01 10:30:00", 1);
INSERT INTO transaction_card (amount, mcc, merchant, created_date, wallet_id) VALUES (20.00, "5812", "CAFETERIA B", "2024-12-02 14:15:00", 1);
INSERT INTO transaction_card (amount, mcc, merchant, created_date, wallet_id) VALUES (100.00, "5411", "SUPERMARKET C", "2024-12-03 18:45:00", 2);
INSERT INTO transaction_card (amount, mcc, merchant, created_date, wallet_id) VALUES (15.00, "5811", "BAKERY D", "2024-12-04 08:20:00", 2);
