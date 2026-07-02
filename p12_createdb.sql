DROP DATABASE IF EXISTS aspms;
CREATE DATABASE aspms;
USE aspms;

CREATE TABLE role (
    rid     INT AUTO_INCREMENT PRIMARY KEY,
    rname   VARCHAR(50) NOT NULL,
    CONSTRAINT uq_role_rname UNIQUE (rname)
);

CREATE TABLE user (
    uid       INT AUTO_INCREMENT PRIMARY KEY,
    rid       INT NOT NULL,
    uname     VARCHAR(100) NOT NULL,
    password  VARCHAR(255) NOT NULL,
    address   TEXT,
    CONSTRAINT fk_user_role FOREIGN KEY (rid) REFERENCES role(rid)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE category (
    cat_id    INT AUTO_INCREMENT PRIMARY KEY,
    cat_name  VARCHAR(100) NOT NULL,
    CONSTRAINT uq_category_name UNIQUE (cat_name)
);

CREATE TABLE sub_category (
    subcat_id     INT AUTO_INCREMENT PRIMARY KEY,
    cat_id        INT NOT NULL,
    subcat_name   VARCHAR(100) NOT NULL,
    CONSTRAINT fk_subcategory_category FOREIGN KEY (cat_id) REFERENCES category(cat_id)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE parts (
    part_id       INT AUTO_INCREMENT PRIMARY KEY,
    subcat_id     INT NOT NULL,
    description   TEXT,
    CONSTRAINT fk_parts_subcategory FOREIGN KEY (subcat_id) REFERENCES sub_category(subcat_id)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE company (
    comp_id     INT AUTO_INCREMENT PRIMARY KEY,
    comp_name   VARCHAR(150) NOT NULL
);

CREATE TABLE company_part (
    cpid         INT AUTO_INCREMENT PRIMARY KEY,
    comp_id      INT NOT NULL,
    part_id      INT NOT NULL,
    sell_price   DECIMAL(10,2) NOT NULL,
    cost_price   DECIMAL(10,2) NOT NULL,
    mfg_dt       DATETIME,
    qty          INT DEFAULT 0,
    CONSTRAINT fk_companypart_company FOREIGN KEY (comp_id) REFERENCES company(comp_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_companypart_part FOREIGN KEY (part_id) REFERENCES parts(part_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT chk_companypart_sellprice CHECK (sell_price >= 0),
    CONSTRAINT chk_companypart_costprice CHECK (cost_price >= 0),
    CONSTRAINT chk_companypart_qty CHECK (qty >= 0)
);

CREATE TABLE enquiry (
    enq_no        INT AUTO_INCREMENT PRIMARY KEY,
    uid           INT NOT NULL,
    cpid          INT NOT NULL,
    qty           INT NOT NULL,
    date          DATETIME DEFAULT CURRENT_TIMESTAMP,
    description   TEXT,
    CONSTRAINT fk_enquiry_user FOREIGN KEY (uid) REFERENCES user(uid)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_enquiry_companypart FOREIGN KEY (cpid) REFERENCES company_part(cpid)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT chk_enquiry_qty CHECK (qty > 0)
);  

CREATE TABLE bill (
    bill_no           INT AUTO_INCREMENT PRIMARY KEY,
    uid               INT NOT NULL,
    date              DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_amt         DECIMAL(12,2) NOT NULL,
    mode_of_payment   VARCHAR(50),
    CONSTRAINT fk_bill_user FOREIGN KEY (uid) REFERENCES user(uid)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT chk_bill_totalamt CHECK (total_amt >= 0)
);  

CREATE TABLE invoice (
    inv_no    INT AUTO_INCREMENT PRIMARY KEY,
    uid       INT NOT NULL,
    date      DATETIME DEFAULT CURRENT_TIMESTAMP,
    enq_no    INT NULL,
    cpid      INT NOT NULL,
    qty       INT NOT NULL,
    amount    DECIMAL(12,2) NOT NULL,
    bill_no   INT NOT NULL,
    CONSTRAINT fk_invoice_user FOREIGN KEY (uid) REFERENCES user(uid)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_invoice_enquiry FOREIGN KEY (enq_no) REFERENCES enquiry(enq_no)
        ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT fk_invoice_companypart FOREIGN KEY (cpid) REFERENCES company_part(cpid)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_invoice_bill FOREIGN KEY (bill_no) REFERENCES bill(bill_no)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT chk_invoice_qty CHECK (qty > 0),
    CONSTRAINT chk_invoice_amount CHECK (amount >= 0)
);


