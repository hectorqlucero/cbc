(ns sk.handlers.eventos.sql)

(def eventos-sql
  "
  SELECT
  id,
  imagen,
  DATE_FORMAT(fecha, '%e de %M %Y') as f_fecha,
  DAY(fecha) as day,
  CASE WHEN DAYNAME(fecha) = 'Sunday' THEN 'Domingo' WHEN DAYNAME(fecha) = 'Monday' THEN 'Lunes' WHEN DAYNAME(fecha) = 'Tuesday' THEN 'Martes' WHEN DAYNAME(fecha) = 'Wednesday' THEN 'Miercoles' WHEN DAYNAME(fecha) = 'Thursday' THEN 'Jueves' WHEN DAYNAME(fecha) = 'Friday' THEN 'Viernes' WHEN DAYNAME(fecha) = 'Saturday' THEN 'Sabado' END AS fecha_dow,
  DATE_FORMAT(fecha,'%m/%d/%Y') AS fecha,
  detalles as descripcion,
  titulo as descripcion_corta,
  lugar as punto_reunion,
  TIME_FORMAT(hora,'%h:%i %p') as hora,
  organiza as leader
  FROM eventos
  WHERE
  YEAR(fecha) = ?
  AND MONTH(fecha) = ?
  ORDER BY
  DAY(fecha),
  hora ")
