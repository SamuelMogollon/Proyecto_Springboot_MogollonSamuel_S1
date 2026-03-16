-- Usuarios
INSERT IGNORE INTO usuario (nombre, email, password, rol) VALUES
('Samuel Mogollón', 'samuel@logitrack.com', '$2a$10$ZKMLO/NACwsrH0JCsyh3uOLgpkLVBXIvFPhJrA9YqDb3z7oFjGXrK', 'ADMIN');
-- Bodegas
INSERT IGNORE INTO bodega (nombre, ubicacion, capacidad, encargado_id) VALUES
('Bodega Bogotá', 'Bogotá, Colombia', 1000, 1),
('Bodega Medellín', 'Medellín, Colombia', 500, 2);

-- Productos
INSERT IGNORE INTO producto (nombre, categoria, precio) VALUES
('Laptop Dell', 'Electrónica', 2500000.00),
('Mouse Logitech', 'Electrónica', 85000.00),
('Silla Ergonómica', 'Mobiliario', 450000.00);