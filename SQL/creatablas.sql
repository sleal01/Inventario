-- 
-- Estructura de tabla para la tabla 'tcategoria'
-- 
CREATE TABLE tcategoria (
  idcat INT NOT NULL AUTO_INCREMENT,
  descripcion VARCHAR(32) NOT NULL,
  PRIMARY KEY  (idcat)
);

-- 
-- Volcar la base de datos para la tabla 'tcategoria'
-- 

INSERT INTO tcategoria VALUES (1, 'Golosinas');
INSERT INTO tcategoria VALUES (2, 'Fritos');


-- 
-- Estructura de tabla para la tabla 'tproducto'
-- 

CREATE TABLE tproducto (
  idprod VARCHAR(8) NOT NULL,
  descripcion VARCHAR(32) NOT NULL,
  precioc FLOAT NOT NULL DEFAULT '0',
  preciov FLOAT NOT NULL DEFAULT '0',
  stock INT NOT NULL DEFAULT '0',
  idcat INT NOT NULL,
  PRIMARY KEY  (idprod)
);

-- 
-- Volcar la base de datos para la tabla 'tproducto'
-- 
INSERT INTO tproducto VALUES ('gol001', 'Dulce caramelo', 10, 25, 0, 1);
INSERT INTO tproducto VALUES ('gol002', 'Mazapan', 2, 4, 0, 1);
INSERT INTO tproducto VALUES ('fri001', 'Papitas Sabritas', 5, 10, 0, 2);
INSERT INTO tproducto VALUES ('fri002', 'Doritos Sabritas', 7, 14, 0, 2);

-- 
-- Estructura de tabla para la tabla 'tcliente'
-- 

CREATE TABLE tcliente (
  idcliente VARCHAR(8) NOT NULL,
  nombre VARCHAR(32) NOT NULL,
  dir VARCHAR(32) NOT NULL,
  telefono VARCHAR(12) DEFAULT NULL,
  PRIMARY KEY  (idcliente)
);

-- 
-- Volcar la base de datos para la tabla 'tcliente'
-- 
INSERT INTO tcliente VALUES ('12345678', 'Venta publico', '', '');


-- 
-- Estructura de tabla para la tabla 'tventa'
-- 

CREATE TABLE tventa (
  idventa VARCHAR(8) NOT NULL,
  fecha DATETIME NOT NULL,
  preciot FLOAT DEFAULT '0',
  detalle TEXT,
  idcliente VARCHAR(8) DEFAULT NULL,
  PRIMARY KEY  (idventa)
);

CREATE TABLE tcompra (
  idcompra VARCHAR(8) NOT NULL,
  fecha DATETIME NOT NULL,
  precioc FLOAT DEFAULT '0',
  detalle TEXT,
  idprov VARCHAR(8) DEFAULT NULL,
  PRIMARY KEY  (idcompra)
);

-- 
-- Estructura de tabla para la tabla 'tpv'
-- 

CREATE TABLE tpv (
  idprod VARCHAR(8) NOT NULL,
  idventa VARCHAR(8) NOT NULL,
  cantidad BIGINT(20) NOT NULL,
  precio FLOAT NOT NULL,
  PRIMARY KEY  (idprod,idventa)
);


-- 
-- Estructura de tabla de proveedores
-- 

CREATE TABLE tproveedor (
  idprov VARCHAR(8) NOT NULL,
  nombre VARCHAR(32) NOT NULL,
  dir VARCHAR(32) NOT NULL,
  telefono VARCHAR(12) DEFAULT NULL,
  PRIMARY KEY  (idprov)
);

-- 
-- Volcar la base de datos para la tabla 'tproveedor'
-- 
INSERT INTO tproveedor VALUES ('p01', 'Prov.Gral', '', '');

