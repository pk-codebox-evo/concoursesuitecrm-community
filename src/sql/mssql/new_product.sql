-- PostgreSQL Table Creation
-- @created    March 18, 2004
-- @version    $Id$
-- This schema represents a product catalog.  A product catalog is a
-- classification and definition of products that a company can sell.
-- REQUIRES: new_cdb.sql
-- REQUIRES: new_project.sql

-- Currency table, could extend to exchange rates
-- Example: USD
CREATE TABLE lookup_currency (
  code INT IDENTITY PRIMARY KEY,
  description VARCHAR(300) NOT NULL,
  default_item BIT DEFAULT 0,
  level INTEGER DEFAULT 0,
  enabled BIT DEFAULT 1
);

-- Each category can be associated with a type
-- Example: Publication, Target Audience

CREATE TABLE lookup_product_category_type (
  code INT IDENTITY PRIMARY KEY,
  description VARCHAR(300) NOT NULL,
  default_item BIT DEFAULT 0,
  level INTEGER DEFAULT 0,
  enabled BIT DEFAULT 1
);

-- A category groups one or more products
-- Example: Name of a publication, Name of a target audience
CREATE TABLE product_category (
  category_id INT IDENTITY PRIMARY KEY,
  parent_id INTEGER REFERENCES product_category(category_id),
  category_name VARCHAR(255) NOT NULL,
  abbreviation VARCHAR(30),
  short_description TEXT,
  long_description TEXT,
  type_id INTEGER REFERENCES lookup_product_category_type(code),
  -- images
  thumbnail_image_id INTEGER REFERENCES project_files(item_id),
  small_image_id INTEGER REFERENCES project_files(item_id),
  large_image_id INTEGER REFERENCES project_files(item_id),
  -- record status
  list_order INTEGER DEFAULT 10,
  enteredby INT NOT NULL REFERENCES access(user_id),
  entered DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  modifiedby INT NOT NULL REFERENCES access(user_id),
  modified DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  start_date DATETIME DEFAULT NULL,
  expiration_date DATETIME DEFAULT NULL,
  enabled BIT NOT NULL DEFAULT 1
);

-- Each category can be associated with multiple categories
CREATE TABLE product_category_map (
  id INT IDENTITY PRIMARY KEY,
  category1_id INTEGER NOT NULL REFERENCES product_category(category_id),
  category2_id INTEGER NOT NULL REFERENCES product_category(category_id)
);

-- Each product can be associated with a type
-- Example: Ad, Design Ad, Subscription
CREATE TABLE lookup_product_type (
  code INT IDENTITY PRIMARY KEY,
  description VARCHAR(300) NOT NULL,
  default_item BIT DEFAULT 0,
  level INTEGER DEFAULT 0,
  enabled BIT DEFAULT 1
);

-- Each product can have a format
-- Example: Physical, Digital
CREATE TABLE lookup_product_format (
  code INT IDENTITY PRIMARY KEY,
  description VARCHAR(300) NOT NULL,
  default_item BIT DEFAULT 0,
  level INTEGER DEFAULT 0,
  enabled BIT DEFAULT 1
);

-- Each product can have a shipping code for shipping company
CREATE TABLE lookup_product_shipping (
  code INT IDENTITY PRIMARY KEY,
  description VARCHAR(300) NOT NULL,
  default_item BIT DEFAULT 0,
  level INTEGER DEFAULT 0,
  enabled BIT DEFAULT 1
);

-- Each product can have an estimated shipping time
-- Example: Ships within 24 hours
CREATE TABLE lookup_product_ship_time (
  code INT IDENTITY PRIMARY KEY,
  description VARCHAR(300) NOT NULL,
  default_item BIT DEFAULT 0,
  level INTEGER DEFAULT 0,
  enabled BIT DEFAULT 1
);

-- Each product can have a tax code
CREATE TABLE lookup_product_tax (
  code INT IDENTITY PRIMARY KEY,
  description VARCHAR(300) NOT NULL,
  default_item BIT DEFAULT 0,
  level INTEGER DEFAULT 0,
  enabled BIT DEFAULT 1
);

-- Each price is either fixed or recurring, if recurring then when?
-- Example: Monthly, Weekly, Yearly, etc.
CREATE TABLE lookup_recurring_type (
  code INT IDENTITY PRIMARY KEY,
  description VARCHAR(300) NOT NULL,
  default_item BIT DEFAULT 0,
  level INTEGER DEFAULT 0,
  enabled BIT DEFAULT 1
);

-- The details of a product are listed
-- If this product is to replace another product, then the parent_id is set
-- Example: 1/16 Page Ad
CREATE TABLE product_catalog (
  product_id INT IDENTITY PRIMARY KEY,
  parent_id INTEGER REFERENCES product_catalog(product_id),
  product_name VARCHAR(255) NOT NULL,
  abbreviation VARCHAR(30),
  short_description TEXT,
  long_description TEXT,
  type_id INTEGER REFERENCES lookup_product_type(code),
  special_notes VARCHAR(255),
  -- ecommerce
  sku VARCHAR(40),
  in_stock BIT NOT NULL DEFAULT 1,
  format_id INTEGER REFERENCES lookup_product_format(code),
  shipping_id INTEGER REFERENCES lookup_product_shipping(code),
  estimated_ship_time INTEGER REFERENCES lookup_product_ship_time(code),
  -- images
  thumbnail_image_id INTEGER REFERENCES project_files(item_id),
  small_image_id INTEGER REFERENCES project_files(item_id),
  large_image_id INTEGER REFERENCES project_files(item_id),
  -- record status
  list_order INTEGER DEFAULT 10,
  enteredby INT NOT NULL references access(user_id),
  entered DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  modifiedby INT NOT NULL references access(user_id),
  modified DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  start_date DATETIME DEFAULT NULL,
  expiration_date DATETIME DEFAULT NULL,
  enabled BIT NOT NULL DEFAULT 1
);

-- Each product can have a price, which can change over time
CREATE TABLE product_catalog_pricing (
  price_id INT IDENTITY PRIMARY KEY,
  product_id INTEGER REFERENCES product_catalog(product_id),
  -- pricing
  tax_id INTEGER REFERENCES lookup_product_tax(code),
  msrp_currency INTEGER REFERENCES lookup_currency(code),
  msrp_amount FLOAT NOT NULL DEFAULT 0,
  price_currency INTEGER REFERENCES lookup_currency(code),
  price_amount FLOAT NOT NULL DEFAULT 0,
  recurring_currency INTEGER REFERENCES lookup_currency(code),
  recurring_amount FLOAT NOT NULL DEFAULT 0,
  recurring_type INTEGER REFERENCES lookup_recurring_type(code),
  enteredby INT NOT NULL references access(user_id),
  entered DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  modifiedby INT NOT NULL references access(user_id),
  modified DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  start_date DATETIME DEFAULT NULL,
  expiration_date DATETIME DEFAULT NULL
);

-- A package can consist of products with discounted prices
CREATE TABLE package (
  package_id INT IDENTITY PRIMARY KEY,
  category_id INT REFERENCES product_category(category_id),
  package_name VARCHAR(255) NOT NULL,
  abbreviation VARCHAR(30),
  short_description TEXT,
  long_description TEXT,
  --images
  thumbnail_image_id INTEGER REFERENCES project_files(item_id),
  small_image_id INTEGER REFERENCES project_files(item_id),
  large_image_id INTEGER REFERENCES project_files(item_id),
  -- record status
  list_order INTEGER DEFAULT 10,
  enteredby INT NOT NULL REFERENCES access(user_id),
  entered DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  modifiedby INT NOT NULL REFERENCES access(user_id),
  modified DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  start_date DATETIME DEFAULT NULL,
  expiration_date DATETIME DEFAULT NULL,
  enabled BIT NOT NULL DEFAULT 1
);

-- Each package consists of one or more products
CREATE TABLE package_products_map (
  id INT IDENTITY PRIMARY KEY,
  package_id INT NOT NULL REFERENCES package(package_id),
  product_id INT NOT NULL REFERENCES product_catalog(product_id),
  description TEXT,
  msrp_currency INTEGER REFERENCES lookup_currency(code),
  msrp_amount FLOAT NOT NULL DEFAULT 0,
  price_currency INTEGER REFERENCES lookup_currency(code),
  price_amount FLOAT NOT NULL DEFAULT 0,
  enteredby INT NOT NULL REFERENCES access(user_id),
  entered DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  modifiedby INT NOT NULL REFERENCES access(user_id),
  modified DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  start_date DATETIME DEFAULT NULL,
  expiration_date DATETIME DEFAULT NULL
);

-- Each product can be associated with multiple categories
CREATE TABLE product_catalog_category_map (
  id INT IDENTITY PRIMARY KEY,
  product_id INTEGER NOT NULL REFERENCES product_catalog(product_id),
  category_id INTEGER NOT NULL REFERENCES product_category(category_id)
);

-- Each configuration of an option has a result type that maps to the application
-- Example: Integer, Double, Text, Timestamp
CREATE TABLE lookup_product_conf_result (
  code INT IDENTITY PRIMARY KEY,
  description VARCHAR(300) NOT NULL,
  default_item BIT DEFAULT 0,
  level INTEGER DEFAULT 0,
  enabled BIT DEFAULT 1
);

-- A configurator allows options to be displayed, validated
-- Example: A color configurator with 4 Options: None, B&W, Spot Color, Process Color
-- PROBLEM: How is the configured item price indicated?
CREATE TABLE product_option_configurator (
  configurator_id INT IDENTITY PRIMARY KEY,
  short_description TEXT,
  long_description TEXT,
  class_name VARCHAR(255),
  result_type INTEGER NOT NULL REFERENCES lookup_product_conf_result(code)
);

-- An option library allows products to be further customized by 
-- specifying what the option is and if/how it can be configured.
-- If this option is to replace another, then the parent_id is set
CREATE TABLE product_option (
  option_id INT IDENTITY PRIMARY KEY,
  configurator_id INTEGER NOT NULL REFERENCES product_option_configurator(configurator_id),
  parent_id INTEGER REFERENCES product_option(option_id),
  short_description TEXT,
  long_description TEXT,
  allow_customer_configure BIT NOT NULL DEFAULT 0,
  allow_user_configure BIT NOT NULL DEFAULT 0,
  required BIT NOT NULL DEFAULT 0,
  -- record information
  start_date DATETIME DEFAULT NULL,
  end_date DATETIME DEFAULT NULL,
  enabled BIT DEFAULT 1
);

-- Each option will have it's configurator defined when the option is created
CREATE TABLE product_option_values (
  value_id INT IDENTITY PRIMARY KEY,
  option_id INTEGER NOT NULL REFERENCES product_option(option_id),
  result_id INTEGER NOT NULL,
  description TEXT,
  msrp_currency INTEGER REFERENCES lookup_currency(code),
  msrp_amount FLOAT NOT NULL DEFAULT 0,
  price_currency INTEGER REFERENCES lookup_currency(code),
  price_amount FLOAT NOT NULL DEFAULT 0,
  recurring_currency INTEGER REFERENCES lookup_currency(code),
  recurring_amount FLOAT NOT NULL DEFAULT 0,
  recurring_type INTEGER REFERENCES lookup_recurring_type(code)
);
CREATE UNIQUE INDEX idx_pr_opt_val ON product_option_values (option_id, result_id);

-- Each product can be associated with multiple configurable options
CREATE TABLE product_option_map (
  product_option_id INT IDENTITY PRIMARY KEY,
  product_id INTEGER NOT NULL REFERENCES product_catalog(product_id),
  option_id INTEGER NOT NULL REFERENCES product_option(option_id),
  value_id INTEGER NOT NULL REFERENCES product_option_values(value_id)
);

-- For each option, there are structure fields specified by the configurator
-- which determine the option's structure
CREATE TABLE product_option_boolean (
  product_option_id INTEGER NOT NULL REFERENCES product_option(option_id),
  value BIT NOT NULL
);

CREATE TABLE product_option_float (
  product_option_id INTEGER NOT NULL REFERENCES product_option(option_id),
  value FLOAT NOT NULL
);

CREATE TABLE product_option_timestamp (
  product_option_id INTEGER NOT NULL REFERENCES product_option(option_id),
  value DATETIME NOT NULL
);

CREATE TABLE product_option_integer (
  product_option_id INTEGER NOT NULL REFERENCES product_option(option_id),
  value INTEGER NOT NULL
);

CREATE TABLE product_option_text (
  product_option_id INTEGER NOT NULL REFERENCES product_option(option_id),
  value TEXT NOT NULL
);

-- Each product can be associated with multiple keywords
-- Example: printer, printing, laser
CREATE TABLE lookup_product_keyword (
  code INT IDENTITY PRIMARY KEY,
  description VARCHAR(300) NOT NULL,
  default_item BIT DEFAULT 0,
  level INTEGER DEFAULT 0,
  enabled BIT DEFAULT 1
);

CREATE TABLE product_keyword_map (
  product_id INTEGER NOT NULL REFERENCES product_catalog(product_id),
  keyword_id INTEGER NOT NULL REFERENCES lookup_product_keyword(code)
);
CREATE UNIQUE INDEX idx_pr_key_map ON product_keyword_map (product_id, keyword_id);

-- Each product_category can have "custom folders"
-- TABLES ALREADY EXIST, MUST CREATE A CONSTANT ID

