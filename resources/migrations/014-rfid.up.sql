create table if not exists rfid (
  id int not null auto_increment primary key,
  rfid text,
  numero text,
  hora timestamp default current_timestamp
  )
