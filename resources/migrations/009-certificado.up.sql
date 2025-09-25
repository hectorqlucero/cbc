create table if not exists certificado (
  id int NOT NULL AUTO_INCREMENT primary key,
  carrera_id int,
  imagen varchar(255),
  descripcion text,
  foreign key(carrera_id) references carrera(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
