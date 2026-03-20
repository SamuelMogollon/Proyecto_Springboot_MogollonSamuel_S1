const API = 'http://172.16.41.128:8080';

// Verificar que haya token, si no redirigir al login
if (!localStorage.getItem('token')) {
    window.location.href = '../index.html';
}

// Obtener token del localStorage
function getToken() {
    return localStorage.getItem('token');
}

// Cerrar sesión
function doLogout() {
    localStorage.removeItem('token');
    window.location.href = '../index.html';
}

// Abrir sidebar en mobile
function openSidebar() {
    document.getElementById('sidebar').classList.add('open');
    document.getElementById('overlay').classList.add('show');
}

// Cerrar sidebar en mobile
function closeSidebar() {
    document.getElementById('sidebar').classList.remove('open');
    document.getElementById('overlay').classList.remove('show');
}

// Función genérica para hacer peticiones al backend con token JWT
async function apiFetch(endpoint) {
    const r = await fetch(`${API}${endpoint}`, {
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        }
    });
    // Si el token expiró, redirigir al login
    if (r.status === 401 || r.status === 403) {
        doLogout();
        return null;
    }
    return r.json();
}

// Formatear fecha a formato colombiano
function fmtFecha(str) {
    if (!str) return '—';
    const d = new Date(str);
    return d.toLocaleDateString('es-CO', { day: '2-digit', month: 'short', year: 'numeric' })
        + ' ' + d.toLocaleTimeString('es-CO', { hour: '2-digit', minute: '2-digit' });
}

// Badge de color según tipo de movimiento
function badgeTipo(tipo) {
    const map = {
        ENTRADA: 'badge-entrada',
        SALIDA: 'badge-salida',
        TRANSFERENCIA: 'badge-transferencia'
    };
    return `<span class="badge ${map[tipo] || ''}">${tipo}</span>`;
}

// Cargar fecha actual en el topbar
function loadDate() {
    const d = new Date();
    document.getElementById('dateStr').textContent =
        d.toLocaleDateString('es-CO', {
            weekday: 'long', day: 'numeric',
            month: 'long', year: 'numeric'
        });
}

// Cargar todos los datos del dashboard
async function loadDashboard() {

    // Hacer todas las peticiones en paralelo para mayor velocidad
    const [bodegas, productos, movimientos, usuarios, reporte] = await Promise.all([
        apiFetch('/api/bodegas'),
        apiFetch('/api/productos'),
        apiFetch('/api/movimientos'),
        apiFetch('/api/usuarios'),
        apiFetch('/api/reportes/resumen')
    ]);

    // Cards de resumen
    if (bodegas) document.getElementById('totalBodegas').textContent = bodegas.length;
    if (productos) document.getElementById('totalProductos').textContent = productos.length;
    if (movimientos) document.getElementById('totalMovimientos').textContent = movimientos.length;
    if (usuarios) document.getElementById('totalUsuarios').textContent = usuarios.length;

    // Stock por bodega — barras animadas
    if (reporte && reporte.stockTotalPorBodega) {
        const entries = Object.entries(reporte.stockTotalPorBodega);
        const max = Math.max(...entries.map(([, v]) => v)) || 1;
        document.getElementById('stockBodegas').innerHTML = entries.length
            ? entries.map(([k, v]) => `
                <div class="bar-item">
                    <span class="bar-label">${k}</span>
                    <div class="bar-track">
                        <div class="bar-fill" style="width:${Math.round(v / max * 100)}%"></div>
                    </div>
                    <span class="bar-val">${v}</span>
                </div>`).join('')
            : '<div class="empty">Sin datos</div>';
    }

    // Productos más movidos
    if (reporte && reporte.productosMasMovidos) {
        const entries = Object.entries(reporte.productosMasMovidos);
        document.getElementById('productosMasMovidos').innerHTML = entries.length
            ? entries.map(([k, v]) => `
                <div class="prod-item">
                    <div>
                        <div class="prod-name">${k}</div>
                        <div class="prod-cat">Unidades movidas</div>
                    </div>
                    <div class="prod-stock">
                        <div class="prod-stock-n">${v}</div>
                        <div class="prod-stock-l">unidades</div>
                    </div>
                </div>`).join('')
            : '<div class="empty">Sin movimientos registrados</div>';
    }

    // Últimos movimientos — mostrar los 8 más recientes
    if (movimientos && movimientos.length) {
        const ultimos = movimientos.slice(-8).reverse();
        document.getElementById('tablaMovimientos').innerHTML = ultimos.map(m => `
            <tr>
                <td style="color:var(--muted);font-size:12px">#${m.id}</td>
                <td>${badgeTipo(m.tipo)}</td>
                <td>${m.bodegaOrigen?.nombre || '—'}</td>
                <td>${m.bodegaDestino?.nombre || '—'}</td>
                <td>${m.usuario?.nombre || '—'}</td>
                <td style="font-size:12px;color:var(--muted)">${fmtFecha(m.fecha)}</td>
            </tr>`).join('');
    } else {
        document.getElementById('tablaMovimientos').innerHTML =
            '<tr><td colspan="6" class="empty">Sin movimientos registrados</td></tr>';
    }
}

// Cargar productos con stock bajo
async function loadStockBajo() {
    const data = await apiFetch('/api/productos/stock-bajo');
    if (!data || !data.length) {
        document.getElementById('stockBajo').innerHTML =
            '<div class="empty">✓ Todos los productos tienen stock suficiente</div>';
        return;
    }
    document.getElementById('stockBajo').innerHTML = `
        <div class="table-wrap">
            <table>
                <thead>
                    <tr>
                        <th>Producto</th>
                        <th>Categoría</th>
                        <th>Precio</th>
                        <th>Stock total</th>
                    </tr>
                </thead>
                <tbody>
                    ${data.map(p => `
                        <tr>
                            <td>${p.nombre}</td>
                            <td><span class="badge" style="background:var(--off);color:var(--muted)">${p.categoria}</span></td>
                            <td>$${Number(p.precio).toLocaleString('es-CO')}</td>
                            <td><span class="stock-low" style="font-weight:500">${p.stockTotal} uds</span></td>
                        </tr>`).join('')}
                </tbody>
            </table>
        </div>`;
}

// Inicializar dashboard
loadDate();
loadDashboard();
loadStockBajo();