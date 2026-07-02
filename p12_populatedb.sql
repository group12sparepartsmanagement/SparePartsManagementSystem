USE aspms;

INSERT INTO role (rid, rname) VALUES
(1, 'Admin'),
(2, 'Staff'),
(3, 'Customer');

INSERT INTO user (uid, rid, uname, password, address) VALUES
(1, 1, 'admin_priya',      'Priya@2026',   'ASPMS HQ, MG Road, Pune, MH'),
(2, 1, 'admin_rajesh',     'Rajesh@2026',  'ASPMS HQ, MG Road, Pune, MH'),
(3, 2, 'staff_karan',      'Karan@2026',   'ASPMS Warehouse Desk, MG Road, Pune, MH'),
(4, 2, 'staff_neha',       'Neha@2026',    'ASPMS Warehouse Desk, MG Road, Pune, MH'),
(5, 2, 'staff_vikram',     'Vikram@2026',  'ASPMS Warehouse Desk, MG Road, Pune, MH'),
(6, 3, 'rahul_sharma',     'Rahul@123',    '12 Green Park, New Delhi, DL'),
(7, 3, 'anita_verma',      'Anita@123',    '45 Koregaon Park, Pune, MH'),
(8, 3, 'suresh_gowda',     'Suresh@123',   '9 Indiranagar, Bengaluru, KA'),
(9, 3, 'meera_iyer',       'Meera@123',    '78 T Nagar, Chennai, TN'),
(10, 3, 'kabir_khan',      'Kabir@123',    '23 Bandra West, Mumbai, MH');

INSERT INTO category (cat_id, cat_name) VALUES
(1, 'Two Wheelers'),
(2, 'Three Wheelers'),
(3, 'Four Wheelers');

INSERT INTO sub_category (subcat_id, cat_id, subcat_name) VALUES
(1, 1, 'Engine Parts'),
(2, 1, 'Braking System'),
(3, 1, 'Electrical System'),
(4, 2, 'Engine Parts'),
(5, 2, 'Braking System'),
(6, 2, 'Electrical System'),
(7, 3, 'Engine Parts'),
(8, 3, 'Braking System'),
(9, 3, 'Electrical System'),
(10, 3, 'Suspension & Steering');

INSERT INTO parts (part_id, subcat_id, description) VALUES
(1, 1,  'Piston kit for 100cc-150cc two-wheeler engines'),
(2, 2,  'Front disc brake pad set for two-wheelers'),
(3, 3,  '12V 5Ah maintenance-free two-wheeler battery'),
(4, 4,  'Piston ring set for three-wheeler diesel engines'),
(5, 5,  'Rear brake shoe set for three-wheelers'),
(6, 6,  '12V 35Ah battery for auto-rickshaws'),
(7, 7,  'Standard piston set, compatible with 1.2L-1.5L petrol engines'),
(8, 8,  'Ceramic front brake pad set, sedans'),
(9, 9,  'Maintenance-free 12V 65Ah battery, SUVs and trucks'),
(10, 10, 'Gas-filled front shock absorber, pair');

INSERT INTO company (comp_id, comp_name) VALUES
(1, 'Bosch India Ltd'),
(2, 'Exide Industries'),
(3, 'Minda Corporation'),
(4, 'Mahindra Genuine Parts'),
(5, 'MRF Auto Components');

INSERT INTO company_part (cpid, comp_id, part_id, sell_price, cost_price, mfg_dt, qty) VALUES
(1,  1, 1,   850.00,  650.00, '2026-01-10 09:00:00', 60),
(2,  1, 7,  2450.00, 1950.00, '2026-01-15 09:00:00', 40),
(3,  2, 3,  1450.00, 1100.00, '2026-02-05 09:00:00', 100),
(4,  2, 6,  3200.00, 2600.00, '2026-02-08 09:00:00', 45),
(5,  2, 9,  8100.00, 6700.00, '2026-02-12 09:00:00', 18),
(6,  3, 2,   480.00,  350.00, '2026-01-25 09:00:00', 150),
(7,  3, 5,   650.00,  480.00, '2026-01-28 09:00:00', 80),
(8,  4, 8,  1850.00, 1400.00, '2026-02-01 09:00:00', 60),
(9,  4, 10, 5600.00, 4600.00, '2026-02-20 09:00:00', 20),
(10, 5, 4,  2650.00, 2100.00, '2026-01-18 09:00:00', 32);

INSERT INTO enquiry (enq_no, uid, cpid, qty, date, description) VALUES
(1,  6,  1, 2, '2026-04-01 10:15:00', 'Piston kit enquiry for Honda Activa'),
(2,  7,  6, 1, '2026-04-02 11:30:00', 'Front brake pad set for Bajaj Pulsar'),
(3,  8,  3, 1, '2026-04-03 14:00:00', 'Battery enquiry for TVS Jupiter'),
(4,  9,  2, 2, '2026-04-04 09:45:00', 'Piston set enquiry for Maruti Swift'),
(5,  10, 8, 1, '2026-04-05 16:20:00', 'Front brake pads for Hyundai i20'),
(6,  6,  7, 1, '2026-04-06 12:00:00', 'Brake shoe enquiry for auto-rickshaw'),
(7,  7,  4, 1, '2026-04-07 10:00:00', 'Battery enquiry for auto-rickshaw'),
(8,  8,  10, 1, '2026-04-08 15:10:00', 'Piston ring enquiry for three-wheeler diesel'),
(9,  9,  7, 1, '2026-04-09 13:25:00', 'Brake shoe enquiry for e-rickshaw fleet'),
(10, 10, 5, 1, '2026-04-10 17:05:00', 'Battery enquiry for Mahindra Bolero');

INSERT INTO bill (bill_no, uid, date, total_amt, mode_of_payment) VALUES
(1, 6,  '2026-04-01 10:40:00', 7300.00, 'Cash on Delivery'),
(2, 7,  '2026-04-02 11:50:00', 3680.00, 'Offline - Bank Transfer'),
(3, 8,  '2026-04-03 14:20:00', 4100.00, 'Cash on Delivery'),
(4, 9,  '2026-04-04 10:05:00', 5550.00, 'Cash on Delivery'),
(5, 10, '2026-04-05 16:40:00', 9950.00, 'Offline - Bank Transfer');

INSERT INTO invoice (inv_no, uid, date, enq_no, cpid, qty, amount, bill_no) VALUES
(1,  6,  '2026-04-01 10:40:00', 1,    1,  2, 1700.00, 1),
(2,  6,  '2026-04-01 10:45:00', NULL, 9,  1, 5600.00, 1),
(3,  7,  '2026-04-02 11:50:00', 2,    6,  1,  480.00, 2),
(4,  7,  '2026-04-02 11:55:00', 7,    4,  1, 3200.00, 2),
(5,  8,  '2026-04-03 14:20:00', 3,    3,  1, 1450.00, 3),
(6,  8,  '2026-04-03 14:25:00', 8,    10, 1, 2650.00, 3),
(7,  9,  '2026-04-04 10:05:00', 4,    2,  2, 4900.00, 4),
(8,  9,  '2026-04-04 10:10:00', 9,    7,  1,  650.00, 4),
(9,  10, '2026-04-05 16:40:00', 5,    8,  1, 1850.00, 5),
(10, 10, '2026-04-05 16:45:00', 10,   5,  1, 8100.00, 5);
