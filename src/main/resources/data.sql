-- create users
INSERT INTO users (email_address, signup_date, last_signin_date, password_hash, user_role)
VALUES ('ad@test.com', '2025-12-26', now(), 'hashed', 'Admin'),
('piet@test.com', '2025-12-26', now(), 'hashed', 'Customer'),
('arjan@test.com', '2025-12-21', now(), 'hashed', 'Service');


--create customers
INSERT INTO customers (first_name, last_name, address, zip_code, city, country, telephone_number, email_address, user_id, newsletter_signup_date)
VALUES ('Ad', 'Karsten', 'Hoofdstraat 43', '7742LR', 'Den Haag', 'Nederland', '06-12345678', 'ad@test.com', null, null),
       ('Tommy', 'Jansen', 'Kerkstraat 12', '2513 KL', 'Alphen a/d Rijn', 'Nederland', '06-12345678', 'piet@test.com', null, null),
       ('Arjan', 'van dijk', 'Langestraat 23', '5623 KD', 'Eindhoven', 'Nederland', '06-12345678', 'arjan@test.com',
       (SELECT user_id
            FROM users
            WHERE email_address = 'arjan@test.com'), null
       );


--create vehicles
INSERT INTO vehicles (license_plate, vin_number, make, model, paint_color, body_style, trailer_hitch, cost_price, sale_price, customer_id)
VALUES ('G-053-NF','DAG01316403540G', 'Mazda', 'CX-3', 'Candy Red', 'Hatchback', false,24125, 29995, null),
       ('J-153-HX','AXD01316D03H40G', 'Toyota', 'Corolla', 'Sky-blue Metallic', 'Sedan', false,26099, 32770, null),
       ('GZ-00-HG','AXD01316D03H40G', 'Nissan', 'Sunny 2.0', 'Yellow', '4 doors', true,15999, 17995,
       (SELECT customer_id
            FROM customers
            WHERE first_name = 'Ad'
              AND last_name = 'Karsten')
       );


--create parts
INSERT INTO parts (item_number, item_description, qty_on_hand, qty_on_order, unit_cost, unit_price)
VALUES ('FR-0011','FRAM OIL Filter - generic', 25, 0, 14.95, 29.99),
       ('BR-002-FR','Brake pads front- Nissan', '2', '25', 19.95, 29.95),
       ('BR-003-RR','Brake pads rear - Nissan', '23', '0', '26.80', 39.75),
       ('TB-003-NISSAN','Timing belt - Nissan 2001-2008', '3', '0', '158.80', 201.75),
       ('LBR-HOURLY','Uur loon - werkplaats', '10000', '0', '89.25', 124.95);


--create appointments
INSERT INTO appointments (appointment_date, reason_for_visit, vehicle_id, created_by_user_id)
    SELECT
        now(),
        'Kleine OH-beurt',
        v.vehicle_id,
        u.user_id
    FROM vehicles v
        JOIN customers c ON c.customer_id = v.customer_id
        JOIN users u ON u.email_address = 'ad@test.com'
    WHERE c.first_name = 'Ad'
        AND c.last_name  = 'Karsten'
        AND v.license_plate = 'GZ-00-HG';





