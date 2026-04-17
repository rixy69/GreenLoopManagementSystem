DROP DATABASE IF EXISTS greenloop;
CREATE DATABASE greenloop;
USE greenloop;

CREATE TABLE greenloop.customers
(
    customer_id int auto_increment PRIMARY KEY,
    name        varchar(100) not null,
    address     text null,
    mobile      varchar(20) null,
    email       varchar(100) null
);

CREATE TABLE greenloop.jobtype
(
    job_type_id int auto_increment PRIMARY KEY,
    type_name   varchar(50) not null
);

CREATE TABLE greenloop.orders
(
    order_id            int auto_increment PRIMARY KEY,
    customer_id         int null,
    order_date          date null,
    status              varchar(50) null,
    total_price         decimal(10, 2) null,
    is_repair           tinyint(1) null,
    repair_service_fee  decimal(10, 2) null,
    is_repaint          tinyint(1) null,
    repaint_service_fee decimal(10, 2) null,
    constraint orders_ibfk_1
        foreign key (customer_id) references greenloop.customers (customer_id)
);

CREATE INDEX customer_id ON greenloop.orders (customer_id);

CREATE TABLE greenloop.ordertype
(
    order_type_id int auto_increment PRIMARY KEY,
    type_name     varchar(50) not null
);

CREATE TABLE greenloop.properties
(
    property_key varchar(50) not null,
    value        varchar(50) not null,
    type         varchar(50) not null,
    constraint unique_property_key_type unique (property_key, type)
);

CREATE TABLE greenloop.roles
(
    role_id   int auto_increment PRIMARY KEY,
    role_name varchar(50) not null,
    constraint role_name unique (role_name)
);

CREATE TABLE greenloop.employees
(
    employee_id int auto_increment PRIMARY KEY,
    title       varchar(100) null,
    first_name  varchar(100) not null,
    last_name   varchar(100) not null,
    username    varchar(100) not null,
    address     text null,
    mobile      varchar(20) null,
    email       varchar(100) null,
    password    varchar(100) not null,
    schedule    text null,
    role_id     int not null,
    constraint username unique (username),
    constraint employees_ibfk_1 foreign key (role_id) references greenloop.roles (role_id)
);

CREATE INDEX role_id ON greenloop.employees (role_id);

CREATE TABLE greenloop.jobs
(
    job_id          int auto_increment PRIMARY KEY,
    order_id        int not null,
    employee_id     int null,
    job_description text null,
    start_date      date null,
    end_date        date null,
    status          varchar(50) null,
    assigned_date   date null,
    job_type_id     int not null,
    constraint unique_order_jobtype unique (order_id, job_type_id),
    constraint jobs_ibfk_1 foreign key (order_id) references greenloop.orders (order_id) on delete cascade,
    constraint jobs_ibfk_2 foreign key (employee_id) references greenloop.employees (employee_id),
    constraint jobs_ibfk_3 foreign key (job_type_id) references greenloop.jobtype (job_type_id)
);

CREATE INDEX employee_id ON greenloop.jobs (employee_id);
CREATE INDEX job_type_id ON greenloop.jobs (job_type_id);

CREATE TABLE greenloop.suppliers
(
    supplier_id   int auto_increment PRIMARY KEY,
    name          varchar(100) not null,
    contact_name  varchar(100) null,
    contact_email varchar(100) null,
    contact_phone varchar(20) null,
    address       text null
);

CREATE TABLE greenloop.parts
(
    part_id     int auto_increment PRIMARY KEY,
    name        varchar(100) not null,
    description text null,
    price       decimal(10, 2) null,
    supplier_id int null,
    constraint parts_ibfk_1 foreign key (supplier_id) references greenloop.suppliers (supplier_id)
);

CREATE TABLE greenloop.inventory
(
    inventory_id int auto_increment PRIMARY KEY,
    part_id      int null,
    quantity     int null,
    location     varchar(100) null,
    constraint inventory_ibfk_1 foreign key (part_id) references greenloop.parts (part_id)
);

CREATE INDEX part_id ON greenloop.inventory (part_id);

CREATE TABLE greenloop.notifications
(
    notification_id int auto_increment PRIMARY KEY,
    part_id         int null,
    min_quantity    int null,
    notify          tinyint(1) null,
    part_name       varchar(100) null,
    constraint unique_part_id unique (part_id),
    constraint notification_ibfk_1 foreign key (part_id) references greenloop.parts (part_id)
);

CREATE INDEX part_id ON greenloop.notifications (part_id);

CREATE TABLE greenloop.order_part
(
    order_part_id    int auto_increment PRIMARY KEY,
    sales_date       date null,
    part_id          int null,
    part_description text null,
    supplier_id      int null,
    quantity         int null,
    order_type_id    int null,
    price            decimal(10, 2) null,
    order_id         int null,
    constraint unique_order_part unique (order_id, part_id),
    constraint sales_fk_order_id foreign key (order_id) references greenloop.orders (order_id) on delete cascade,
    constraint sales_fk_order_type_id foreign key (order_type_id) references greenloop.ordertype (order_type_id),
    constraint sales_fk_product_id foreign key (part_id) references greenloop.parts (part_id),
    constraint sales_fk_supplier_id foreign key (supplier_id) references greenloop.suppliers (supplier_id)
);

CREATE INDEX supplier_id ON greenloop.parts (supplier_id);

INSERT IGNORE INTO greenloop.roles (role_name) VALUES ('Admin'), ('Employee'), ('Manager');

INSERT IGNORE INTO greenloop.employees (title, first_name, last_name, username, address, mobile, email, password, schedule, role_id)
VALUES
('Mr', 'Rixy', 'Admin', 'rixy1', 'N/A', '0000000000', 'rixy1@example.com', 'Rixy@8248', 'Mon-Fri', (SELECT role_id FROM greenloop.roles WHERE role_name = 'Admin')),
('Mr', 'Rixy', 'Employee', 'rixy2', 'N/A', '0000000000', 'rixy2@example.com', 'Rixy@8248', 'Mon-Fri', (SELECT role_id FROM greenloop.roles WHERE role_name = 'Employee')),
('Mr', 'Rixy', 'Manager', 'rixy3', 'N/A', '0000000000', 'rixy3@example.com', 'Rixy@8248', 'Mon-Fri', (SELECT role_id FROM greenloop.roles WHERE role_name = 'Manager')),
('Mr', 'Niru', 'Admin', 'Niru1', 'N/A', '0000000000', 'Niru1@example.com', 'Niru123', 'Mon-Fri', (SELECT role_id FROM greenloop.roles WHERE role_name = 'Admin')),
('Mr', 'Niru', 'Employee', 'Niru2', 'N/A', '0000000000', 'Niru2@example.com', 'Niru123', 'Mon-Fri', (SELECT role_id FROM greenloop.roles WHERE role_name = 'Employee')),
('Mr', 'Niru', 'Manager', 'Niru3', 'N/A', '0000000000', 'Niru3@example.com', 'Niru123', 'Mon-Fri', (SELECT role_id FROM greenloop.roles WHERE role_name = 'Manager')),
('Mr', 'Thanushika', 'Admin', 'Thanushika1', 'N/A', '0000000000', 'Thanushika1@example.com', 'Niru123', 'Mon-Fri', (SELECT role_id FROM greenloop.roles WHERE role_name = 'Admin')),
('Mr', 'Thanushika', 'Employee', 'Thanushika2', 'N/A', '0000000000', 'Thanushika2@example.com', 'Niru123', 'Mon-Fri', (SELECT role_id FROM greenloop.roles WHERE role_name = 'Employee')),
('Mr', 'Thanushika', 'Manager', 'Thanushika3', 'N/A', '0000000000', 'Thanushika3@example.com', 'Niru123', 'Mon-Fri', (SELECT role_id FROM greenloop.roles WHERE role_name = 'Manager')),
('Mr', 'Kajabavan', 'Admin', 'Kajabavan1', 'N/A', '0000000000', 'Kajabavan1@example.com', 'Niru123', 'Mon-Fri', (SELECT role_id FROM greenloop.roles WHERE role_name = 'Admin')),
('Mr', 'Kajabavan', 'Employee', 'Kajabavan2', 'N/A', '0000000000', 'Kajabavan2@example.com', 'Niru123', 'Mon-Fri', (SELECT role_id FROM greenloop.roles WHERE role_name = 'Employee')),
('Mr', 'Kajabavan', 'Manager', 'Kajabavan3', 'N/A', '0000000000', 'Kajabavan3@example.com', 'Niru123', 'Mon-Fri', (SELECT role_id FROM greenloop.roles WHERE role_name = 'Manager')),
('Mr', 'Maaran', 'Admin', 'Maaran1', 'N/A', '0000000000', 'Maaran1@example.com', 'Niru123', 'Mon-Fri', (SELECT role_id FROM greenloop.roles WHERE role_name = 'Admin')),
('Mr', 'Maaran', 'Employee', 'Maaran2', 'N/A', '0000000000', 'Maaran2@example.com', 'Niru123', 'Mon-Fri', (SELECT role_id FROM greenloop.roles WHERE role_name = 'Employee')),
('Mr', 'Maaran', 'Manager', 'Maaran3', 'N/A', '0000000000', 'Maaran3@example.com', 'Niru123', 'Mon-Fri', (SELECT role_id FROM greenloop.roles WHERE role_name = 'Manager')),
('Mr', 'Denu', 'Admin', 'Denu1', 'N/A', '0000000000', 'Denu1@example.com', 'Niru123', 'Mon-Fri', (SELECT role_id FROM greenloop.roles WHERE role_name = 'Admin')),
('Mr', 'Denu', 'Employee', 'Denu2', 'N/A', '0000000000', 'Denu2@example.com', 'Niru123', 'Mon-Fri', (SELECT role_id FROM greenloop.roles WHERE role_name = 'Employee')),
('Mr', 'Denu', 'Manager', 'Denu3', 'N/A', '0000000000', 'Denu3@example.com', 'Niru123', 'Mon-Fri', (SELECT role_id FROM greenloop.roles WHERE role_name = 'Manager'));

INSERT IGNORE INTO greenloop.ordertype (type_name)
VALUES ('Retail Sale'), ('Bulk Order'), ('Replacement');

INSERT IGNORE INTO greenloop.jobtype (type_name)
VALUES ('Packing'), ('Dispatch'), ('Delivery');

INSERT IGNORE INTO greenloop.properties (property_key, value, type)
VALUES
('mail.transport.protocol', 'smtp', 'email'),
('username', 'your-email@example.com', 'email'),
('password', 'your-app-password', 'email'),
('mail.smtp.host', 'smtp.gmail.com', 'email'),
('mail.smtp.port', '587', 'email'),
('mail.smtp.auth', 'true', 'email'),
('mail.debug', 'true', 'email'),
('mail.smtp.starttls.enable', 'true', 'email');

INSERT IGNORE INTO greenloop.customers (name, address, mobile, email)
VALUES
('FreshMart Retail', '12 Green Avenue, Colombo', '0771234567', 'orders@freshmart.lk'),
('EcoNeeds Store', '45 Lake Road, Kandy', '0712345678', 'purchasing@econeeds.lk'),
('Nature Basket', '88 Temple Street, Galle', '0763456789', 'admin@naturebasket.lk');

INSERT IGNORE INTO greenloop.suppliers (name, contact_name, contact_email, contact_phone, address)
VALUES
('EcoPack Lanka', 'Saman Perera', 'sales@ecopack.lk', '0112345678', '101 Industrial Park, Colombo'),
('BioWrap Suppliers', 'Nadeesha Silva', 'hello@biowrap.lk', '0812233445', '23 Green Valley, Kandy'),
('ReBox Distributors', 'Kasun Fernando', 'orders@rebox.lk', '0913344556', '17 Coastal Road, Galle');

INSERT IGNORE INTO greenloop.parts (name, description, price, supplier_id)
VALUES
('Recycled Kraft Box - Small', 'Small recycled kraft carton box for eco-friendly retail packaging', 120.00, (SELECT supplier_id FROM greenloop.suppliers WHERE name = 'EcoPack Lanka')),
('Compostable Carry Bag', 'Compostable carry bag for sustainable takeaway and retail usage', 45.00, (SELECT supplier_id FROM greenloop.suppliers WHERE name = 'BioWrap Suppliers')),
('Biodegradable Bubble Wrap', 'Protective biodegradable wrap for fragile deliveries', 250.00, (SELECT supplier_id FROM greenloop.suppliers WHERE name = 'ReBox Distributors'));

INSERT IGNORE INTO greenloop.inventory (part_id, quantity, location)
VALUES
((SELECT part_id FROM greenloop.parts WHERE name = 'Recycled Kraft Box - Small'), 150, 'Warehouse A - Shelf 01'),
((SELECT part_id FROM greenloop.parts WHERE name = 'Compostable Carry Bag'), 420, 'Warehouse A - Shelf 03'),
((SELECT part_id FROM greenloop.parts WHERE name = 'Biodegradable Bubble Wrap'), 60, 'Warehouse B - Rack 02');

INSERT IGNORE INTO greenloop.notifications (part_id, min_quantity, notify, part_name)
VALUES
((SELECT part_id FROM greenloop.parts WHERE name = 'Recycled Kraft Box - Small'), 40, 1, 'Recycled Kraft Box - Small'),
((SELECT part_id FROM greenloop.parts WHERE name = 'Compostable Carry Bag'), 120, 1, 'Compostable Carry Bag'),
((SELECT part_id FROM greenloop.parts WHERE name = 'Biodegradable Bubble Wrap'), 20, 1, 'Biodegradable Bubble Wrap');

INSERT IGNORE INTO greenloop.orders (customer_id, order_date, status, total_price, is_repair, repair_service_fee, is_repaint, repaint_service_fee)
VALUES
((SELECT customer_id FROM greenloop.customers WHERE name = 'FreshMart Retail'), '2026-01-10', 'Pending', 4200.00, 0, 0.00, 0, 0.00),
((SELECT customer_id FROM greenloop.customers WHERE name = 'EcoNeeds Store'), '2026-01-18', 'Dispatched', 3150.00, 0, 0.00, 0, 0.00);

INSERT IGNORE INTO greenloop.order_part (sales_date, part_id, part_description, supplier_id, quantity, order_type_id, price, order_id)
VALUES
('2026-01-10',
 (SELECT part_id FROM greenloop.parts WHERE name = 'Recycled Kraft Box - Small'),
 'Small recycled kraft carton box for eco-friendly retail packaging',
 (SELECT supplier_id FROM greenloop.suppliers WHERE name = 'EcoPack Lanka'),
 20,
 (SELECT order_type_id FROM greenloop.ordertype WHERE type_name = 'Retail Sale'),
 120.00,
 (SELECT order_id FROM greenloop.orders WHERE customer_id = (SELECT customer_id FROM greenloop.customers WHERE name = 'FreshMart Retail') AND order_date = '2026-01-10')),
('2026-01-10',
 (SELECT part_id FROM greenloop.parts WHERE name = 'Compostable Carry Bag'),
 'Compostable carry bag for sustainable takeaway and retail usage',
 (SELECT supplier_id FROM greenloop.suppliers WHERE name = 'BioWrap Suppliers'),
 40,
 (SELECT order_type_id FROM greenloop.ordertype WHERE type_name = 'Retail Sale'),
 45.00,
 (SELECT order_id FROM greenloop.orders WHERE customer_id = (SELECT customer_id FROM greenloop.customers WHERE name = 'FreshMart Retail') AND order_date = '2026-01-10')),
('2026-01-18',
 (SELECT part_id FROM greenloop.parts WHERE name = 'Biodegradable Bubble Wrap'),
 'Protective biodegradable wrap for fragile deliveries',
 (SELECT supplier_id FROM greenloop.suppliers WHERE name = 'ReBox Distributors'),
 6,
 (SELECT order_type_id FROM greenloop.ordertype WHERE type_name = 'Bulk Order'),
 250.00,
 (SELECT order_id FROM greenloop.orders WHERE customer_id = (SELECT customer_id FROM greenloop.customers WHERE name = 'EcoNeeds Store') AND order_date = '2026-01-18')),
('2026-01-18',
 (SELECT part_id FROM greenloop.parts WHERE name = 'Compostable Carry Bag'),
 'Compostable carry bag for sustainable takeaway and retail usage',
 (SELECT supplier_id FROM greenloop.suppliers WHERE name = 'BioWrap Suppliers'),
 30,
 (SELECT order_type_id FROM greenloop.ordertype WHERE type_name = 'Bulk Order'),
 45.00,
 (SELECT order_id FROM greenloop.orders WHERE customer_id = (SELECT customer_id FROM greenloop.customers WHERE name = 'EcoNeeds Store') AND order_date = '2026-01-18'));

INSERT IGNORE INTO greenloop.jobs (order_id, employee_id, job_description, start_date, end_date, status, assigned_date, job_type_id)
VALUES
((SELECT order_id FROM greenloop.orders WHERE customer_id = (SELECT customer_id FROM greenloop.customers WHERE name = 'FreshMart Retail') AND order_date = '2026-01-10'),
 (SELECT employee_id FROM greenloop.employees WHERE username = 'rixy2'),
 'Prepare and pack the FreshMart Retail order.',
 '2026-01-10',
 '2026-01-11',
 'Completed',
 '2026-01-10',
 (SELECT job_type_id FROM greenloop.jobtype WHERE type_name = 'Packing')),
((SELECT order_id FROM greenloop.orders WHERE customer_id = (SELECT customer_id FROM greenloop.customers WHERE name = 'EcoNeeds Store') AND order_date = '2026-01-18'),
 (SELECT employee_id FROM greenloop.employees WHERE username = 'Niru2'),
 'Dispatch and deliver the EcoNeeds Store order.',
 '2026-01-18',
 '2026-01-19',
 'Assigned',
 '2026-01-18',
 (SELECT job_type_id FROM greenloop.jobtype WHERE type_name = 'Delivery'));
