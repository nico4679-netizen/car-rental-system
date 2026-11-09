-- Seed data: 12 cars, 12 customers, 12 rentals (loaded after Hibernate creates the schema)

INSERT INTO cars (car_id, make, model, car_year, license_plate, daily_rate, is_available) VALUES
  ('af48fca7-3892-5ba9-9ba2-f3ac47bca30f', 'Toyota', 'Camry', 2024, 'ABC-1234', 55.00, FALSE),
  ('afb33dc9-90b9-5d67-bb9f-df4025b92c85', 'Honda', 'Civic', 2023, 'CIV-2023', 48.00, FALSE),
  ('ed926203-b8fa-5e99-a1a4-08c08e7a905b', 'Ford', 'Mustang', 2024, 'MUS-7788', 95.00, FALSE),
  ('e2f60bb7-cc5d-5f23-8009-c433c03f300f', 'Tesla', 'Model 3', 2025, 'TES-0003', 110.00, FALSE),
  ('7483bb87-7cda-57c2-b0d6-bdfd85cb7473', 'Hyundai', 'Elantra', 2022, 'HYU-2200', 42.00, FALSE),
  ('1f1e2c74-bf75-559f-adaa-60089d5cd1c6', 'Nissan', 'Rogue', 2023, 'NIS-5566', 65.00, TRUE),
  ('f5ea67c6-ba2c-5aa1-9013-08c304ef874e', 'Chevrolet', 'Malibu', 2021, 'CHE-2121', 45.00, TRUE),
  ('ea9b3b18-08b2-5160-8553-a9af2f078ba6', 'BMW', 'X3', 2024, 'BMW-0303', 120.00, TRUE),
  ('b420ff42-f53c-5314-acc7-0f07c78b16b6', 'Kia', 'Sportage', 2023, 'KIA-9090', 60.00, TRUE),
  ('3fb6af85-0f07-551c-86f9-3c815f2a5b85', 'Mazda', 'CX-5', 2022, 'MAZ-5050', 58.00, TRUE),
  ('4ff7eddb-e68a-5f40-88f0-1b062ecd2535', 'Volkswagen', 'Golf', 2021, 'VWG-2112', 40.00, TRUE),
  ('e29b8af0-8f7f-5dad-9738-bb723891c3f2', 'Jeep', 'Wrangler', 2024, 'JEP-4444', 88.00, TRUE);

INSERT INTO customers (customer_id, first_name, last_name, email, phone_number, driver_license_number) VALUES
  ('7650a268-4eb3-51ea-ab03-4f6c2ff80501', 'John', 'Doe', 'john.doe@example.com', '514-555-0199', 'DL-98765432'),
  ('d4d57dd1-7371-56f6-8d63-85a901ae446b', 'Alice', 'Tremblay', 'alice.tremblay@example.com', '514-555-0101', 'DL-10000001'),
  ('305bb4b9-0eda-5bf8-bdb5-680d8b561552', 'Marc', 'Gagnon', 'marc.gagnon@example.com', '438-555-0102', 'DL-10000002'),
  ('27607da3-de1d-591b-ae17-4f5f5059a234', 'Sophie', 'Roy', 'sophie.roy@example.com', '450-555-0103', 'DL-10000003'),
  ('735917a3-feac-526a-9a39-22c7d718ebe0', 'David', 'Lee', 'david.lee@example.com', '514-555-0104', 'DL-10000004'),
  ('df167053-b2ae-5e35-acab-94610e4e53b4', 'Emma', 'Bouchard', 'emma.bouchard@example.com', '438-555-0105', 'DL-10000005'),
  ('5c4bb0c7-cc8e-57e7-891e-654e29df3493', 'Lucas', 'Martin', 'lucas.martin@example.com', '514-555-0106', 'DL-10000006'),
  ('00f1003b-b47b-5059-b3a3-1e108a7c6fe2', 'Olivia', 'Pelletier', 'olivia.pelletier@example.com', '450-555-0107', 'DL-10000007'),
  ('f18071bf-b358-5a49-9fd3-8961f9293a7f', 'Noah', 'Fortin', 'noah.fortin@example.com', '514-555-0108', 'DL-10000008'),
  ('7b1e1858-dadb-5f6d-aeb0-94f7b9f7b7e4', 'Chloe', 'Gauthier', 'chloe.gauthier@example.com', '438-555-0109', 'DL-10000009'),
  ('485eb799-b28c-58ae-a146-0500ce149d3a', 'Liam', 'Morin', 'liam.morin@example.com', '514-555-0110', 'DL-10000010'),
  ('f2f5ff3b-0767-54f4-8b6d-3eb6173b78c1', 'Mia', 'Lavoie', 'mia.lavoie@example.com', '450-555-0111', 'DL-10000011');

INSERT INTO rentals (rental_id, car_id, customer_id, start_date, end_date, total_cost, status) VALUES
  ('5768d73d-33ae-577a-b617-454ab79b9630', 1, 1, '2026-11-05', '2026-11-12', 385.00, 'ACTIVE'),
  ('03962983-a794-51ad-97d3-38f45c42d26e', 2, 2, '2026-11-07', '2026-11-10', 144.00, 'ACTIVE'),
  ('1596cf79-15e4-5084-82d9-39fad0a4855d', 3, 3, '2026-11-08', '2026-11-15', 665.00, 'ACTIVE'),
  ('1fc07241-55b7-5f19-b009-89c45361fe70', 4, 4, '2026-11-09', '2026-11-13', 440.00, 'ACTIVE'),
  ('4d6430fb-cc99-551f-bcd4-f035dd1eb2e3', 5, 5, '2026-11-06', '2026-11-11', 210.00, 'ACTIVE'),
  ('b3c21b56-4dc2-5031-a163-bdab0efaffe7', 1, 6, '2026-10-01', '2026-10-05', 220.00, 'COMPLETED'),
  ('40c5877e-a520-5b07-8c60-39b4e0227570', 6, 7, '2026-10-10', '2026-10-14', 260.00, 'COMPLETED'),
  ('0f9fb136-005b-57a7-bffa-24eda5df4a00', 7, 8, '2026-10-15', '2026-10-20', 225.00, 'COMPLETED'),
  ('e54819a2-1722-5d2b-9c1d-8c41216ccf0b', 8, 9, '2026-09-20', '2026-09-25', 600.00, 'COMPLETED'),
  ('e65bd3ab-53db-5fd5-a74a-8125c7eeb4da', 9, 10, '2026-09-01', '2026-09-04', 180.00, 'COMPLETED'),
  ('dcfcdcb7-0e8b-5aaf-80aa-ece95d144f99', 10, 1, '2026-10-25', '2026-10-28', 174.00, 'CANCELLED'),
  ('3ff39cf4-5803-5275-a143-38f8363c0a9e', 2, 11, '2026-10-01', '2026-10-06', 240.00, 'COMPLETED');
