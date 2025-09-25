CREATE TABLE if not exists pos_items (
  OID int(11) not null auto_increment primary key,
  name varchar(200),
  item_number varchar(200),
  category varchar(200),
  status_item int(1),
  dt_registered datetime,
  rfid_number varchar(300),
  checkin_dt datetime,
  checkout_dt datetime,
  employee_checked_out varchar(200),
  employee_checked_in varchar(200)
) engine=innoDB default charset=utf8mb3;
