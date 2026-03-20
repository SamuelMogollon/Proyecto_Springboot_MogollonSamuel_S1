const API = 'http://172.16.41.128:8080';

// Verificar autenticación
if (!localStorage.getItem('token')) {
    window.location.href = '../index.html';
}

// Obtener token
function getToken() {
    return localStorage.getItem('token');
}

// Cerrar sesión
function doLogout() {
    localStorage.removeItem('token');
    window.location.href = '../index.html';
}

// Sidebar mobile
function openSidebar() {
    document.getElementById('sidebar').classList.add('open');
    document.getElementById('overlay').classList.add('show');
}

function closeSidebar() {
    document.getElementById('sidebar').classList.remove('open');
    document.getElementById('overlay').classList.remove('show');
}

// Fetch con token JWT
async function apiFetch(endpoint, options = {}) {
    const r = await fetch(`${API}${endpoint}`, {
        ...options,
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json',
            ...options.headers
        }
    });
    if (r.status === 401 || r.status === 403) {
        doLogout();
        return null;
    }
    return r;
}

// Formatear fecha
function fmtFecha(str) {
    if (!str) return '—';
    const d = new Date(str);
    return d.toLocaleDateString('es-CO', { day: '2-digit', month: 'short', year: 'numeric' })
        + ' ' + d.toLocaleTimeString('es-CO', { hour: '2-digit', minute: '2-digit' });
}

// Badge tipo operación
function badgeTipo(tipo) {
    const map = {
        INSERT: 'badge-insert',
        UPDATE: 'badge-update',
        DELETE: 'badge-delete'
    };
    return `<span class="${map[tipo] || ''}">${tipo}</span>`;
}

// Filtrar por tipo de operación
async function filtrarPorTipo() {
    const tipo = document.getElementById('filtroTipo').value;
    document.getElementById('filtroUsuario').value = '';

    if (!tipo) {
        cargarAuditorias();
        return;
    }

    document.getElementById('tablaAuditorias').innerHTML =
        '<tr><td colspan="7" class="loading">Filtrando...</td></tr>';

    const r = await apiFetch(`/api/auditorias/tipo/${tipo}`);
    if (!r) return;
    const data = await r.json();

    document.getElementById('panelTitle').textContent = `Auditorías — ${tipo}`;
    document.getElementById('totalCount').textContent = `${data.length} registros`;
    renderTabla(data);
}

// Filtrar por usuario
async function filtrarPorUsuario() {
    const idUsuario = document.getElementById('filtroUsuario').value;
    document.getElementById('filtroTipo').value = '';

    if (!idUsuario) {
        cargarAuditorias();
        return;
    }

    document.getElementById('tablaAuditorias').innerHTML =
        '<tr><td colspan="7" class="loading">Filtrando...</td></tr>';

    const r = await apiFetch(`/api/auditorias/usuario/${idUsuario}`);
    if (!r) return;
    const data = await r.json();

    document.getElementById('panelTitle').textContent = 'Auditorías por usuario';
    document.getElementById('totalCount').textContent = `${data.length} registros`;
    renderTabla(data);
}

// Renderizar tabla
function renderTabla(data) {
    if (!data.length) {
        document.getElementById('tablaAuditorias').innerHTML =
            '<tr><td colspan="7" class="empty">No hay registros de auditoría</td></tr>';
        return;
    }

    document.getElementById('tablaAuditorias').innerHTML = data.map(a => `
        <tr>
            <td style="color:var(--muted);font-size:12px">#${a.id}</td>
            <td>${badgeTipo(a.tipoOperacion)}</td>
            <td style="font-weight:500">${a.entidadAfectada || '—'}</td>
            <td>${a.usuario?.nombre || '—'}</td>
            <td class="val-cell" title="${a.valoresAnteriores || ''}">${a.valoresAnteriores || '—'}</td>
            <td class="val-cell" title="${a.valoresNuevos || ''}">${a.valoresNuevos || '—'}</td>
            <td style="font-size:12px;color:var(--muted)">${fmtFecha(a.fechaHora)}</td>
        </tr>`).join('');
}

// Cargar usuarios en el select de filtro
async function cargarUsuariosFiltro() {
    const r = await apiFetch('/api/usuarios');
    if (!r) return;
    const usuarios = await r.json();
    const select = document.getElementById('filtroUsuario');
    usuarios.forEach(u => {
        select.innerHTML += `<option value="${u.id}">${u.nombre}</option>`;
    });
}

// Cargar todas las auditorías
async function cargarAuditorias() {
    document.getElementById('tablaAuditorias').innerHTML =
        '<tr><td colspan="7" class="loading">Cargando...</td></tr>';

    document.getElementById('panelTitle').textContent = 'Registro de auditorías';

    const r = await apiFetch('/api/auditorias');
    if (!r) return;
    const data = await r.json();

    document.getElementById('totalCount').textContent = `${data.length} registros`;
    renderTabla(data);
}

// Inicializar
cargarUsuariosFiltro();
cargarAuditorias();