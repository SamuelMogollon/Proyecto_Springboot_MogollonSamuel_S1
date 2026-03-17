const API = 'http://localhost:8080';

// Verificar autenticación
if (!localStorage.getItem('token')) {
    window.location.href = '../index.html';
}

// Variables de estado
let mostrandoFiltro = false;
let productosDisponibles = [];

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

// Badge tipo movimiento
function badgeTipo(tipo) {
    const map = {
        ENTRADA: 'badge-entrada',
        SALIDA: 'badge-salida',
        TRANSFERENCIA: 'badge-transferencia'
    };
    return `<span class="${map[tipo] || ''}">${tipo}</span>`;
}

// Toggle filtro de fechas
function toggleFiltroFechas() {
    mostrandoFiltro = !mostrandoFiltro;
    document.getElementById('filtroPanel').style.display = mostrandoFiltro ? 'block' : 'none';
}

// Limpiar filtro y cargar todos
function limpiarFiltro() {
    document.getElementById('inputInicio').value = '';
    document.getElementById('inputFin').value = '';
    document.getElementById('panelTitle').textContent = 'Movimientos registrados';
    cargarMovimientos();
}

// Buscar por rango de fechas
async function buscarPorFechas() {
    const inicio = document.getElementById('inputInicio').value;
    const fin = document.getElementById('inputFin').value;

    if (!inicio || !fin) {
        alert('Selecciona fecha inicio y fin.');
        return;
    }

    document.getElementById('tablaMovimientos').innerHTML =
        '<tr><td colspan="7" class="loading">Buscando...</td></tr>';

    const r = await apiFetch(`/api/movimientos/rango?inicio=${inicio}&fin=${fin}`);
    if (!r) return;
    const data = await r.json();

    document.getElementById('panelTitle').textContent = 'Resultados del filtro';
    document.getElementById('totalCount').textContent = `${data.length} movimientos`;
    renderTabla(data);
}

// Cambiar campos visibles según tipo de movimiento
function cambiarTipo() {
    const tipo = document.getElementById('inputTipo').value;
    const campoOrigen = document.getElementById('campoOrigen');
    const campoDestino = document.getElementById('campoDestino');

    // ENTRADA — solo destino
    // SALIDA — solo origen
    // TRANSFERENCIA — origen y destino
    campoOrigen.style.display = (tipo === 'SALIDA' || tipo === 'TRANSFERENCIA') ? 'block' : 'none';
    campoDestino.style.display = (tipo === 'ENTRADA' || tipo === 'TRANSFERENCIA') ? 'block' : 'none';
}

// Abrir modal
async function abrirModal() {
    document.getElementById('detallesContainer').innerHTML = '';
    document.getElementById('inputTipo').value = 'ENTRADA';
    document.getElementById('inputUsuario').innerHTML = '<option value="">Seleccionar usuario...</option>';
    document.getElementById('inputOrigen').innerHTML = '<option value="">Seleccionar bodega...</option>';
    document.getElementById('inputDestino').innerHTML = '<option value="">Seleccionar bodega...</option>';
    limpiarMensaje();
    cambiarTipo();

    // Cargar usuarios, bodegas y productos en paralelo
    const [ru, rb, rp] = await Promise.all([
        apiFetch('/api/usuarios'),
        apiFetch('/api/bodegas'),
        apiFetch('/api/productos')
    ]);

    if (ru) {
        const usuarios = await ru.json();
        usuarios.forEach(u => {
            document.getElementById('inputUsuario').innerHTML +=
                `<option value="${u.id}">${u.nombre}</option>`;
        });
    }

    if (rb) {
        const bodegas = await rb.json();
        bodegas.forEach(b => {
            document.getElementById('inputOrigen').innerHTML +=
                `<option value="${b.id}">${b.nombre}</option>`;
            document.getElementById('inputDestino').innerHTML +=
                `<option value="${b.id}">${b.nombre}</option>`;
        });
    }

    if (rp) {
        productosDisponibles = await rp.json();
    }

    // Agregar primer detalle por defecto
    agregarDetalle();

    document.getElementById('modalOverlay').classList.add('show');
    document.getElementById('modal').classList.add('show');
}

// Cerrar modal
function cerrarModal() {
    document.getElementById('modalOverlay').classList.remove('show');
    document.getElementById('modal').classList.remove('show');
}

// Limpiar mensaje
function limpiarMensaje() {
    const msg = document.getElementById('modalMsg');
    msg.className = 'modal-msg';
    msg.textContent = '';
}

// Mostrar mensaje
function mostrarMensaje(texto, tipo) {
    const msg = document.getElementById('modalMsg');
    msg.textContent = texto;
    msg.className = `modal-msg ${tipo} show`;
}

// Agregar fila de detalle
function agregarDetalle() {
    const container = document.getElementById('detallesContainer');
    const id = Date.now();

    const opciones = productosDisponibles.map(p =>
        `<option value="${p.id}">${p.nombre}</option>`
    ).join('');

    container.innerHTML += `
        <div class="detalle-row" id="detalle-${id}">
            <select>
                <option value="">Seleccionar producto...</option>
                ${opciones}
            </select>
            <input type="number" placeholder="Cantidad" min="1"/>
            <button class="btn-remove-detalle" onclick="eliminarDetalle('detalle-${id}')">✕</button>
        </div>`;
}

// Eliminar fila de detalle
function eliminarDetalle(id) {
    const el = document.getElementById(id);
    if (el) el.remove();
}

// Guardar movimiento
async function guardarMovimiento() {
    const tipo = document.getElementById('inputTipo').value;
    const idUsuario = document.getElementById('inputUsuario').value;
    const idOrigen = document.getElementById('inputOrigen').value;
    const idDestino = document.getElementById('inputDestino').value;
    const btn = document.getElementById('btnGuardar');

    // Validaciones básicas
    if (!idUsuario) {
        mostrarMensaje('Selecciona un usuario responsable.', 'err');
        return;
    }

    if ((tipo === 'SALIDA' || tipo === 'TRANSFERENCIA') && !idOrigen) {
        mostrarMensaje('Selecciona la bodega origen.', 'err');
        return;
    }

    if ((tipo === 'ENTRADA' || tipo === 'TRANSFERENCIA') && !idDestino) {
        mostrarMensaje('Selecciona la bodega destino.', 'err');
        return;
    }

    // Obtener detalles
    const filas = document.querySelectorAll('#detallesContainer .detalle-row');
    const detalles = [];

    for (const fila of filas) {
        const idProducto = fila.querySelector('select').value;
        const cantidad = fila.querySelector('input').value;

        if (!idProducto || !cantidad || cantidad < 1) {
            mostrarMensaje('Completa todos los productos y cantidades.', 'err');
            return;
        }

        detalles.push({
            idProducto: parseInt(idProducto),
            cantidad: parseInt(cantidad)
        });
    }

    if (!detalles.length) {
        mostrarMensaje('Agrega al menos un producto.', 'err');
        return;
    }

    btn.disabled = true;
    btn.textContent = 'Guardando...';

    const body = {
        tipo,
        idUsuario: parseInt(idUsuario),
        idBodegaOrigen: idOrigen ? parseInt(idOrigen) : null,
        idBodegaDestino: idDestino ? parseInt(idDestino) : null,
        detalles
    };

    try {
        const r = await apiFetch('/api/movimientos', {
            method: 'POST',
            body: JSON.stringify(body)
        });

        if (!r) return;

        if (!r.ok) {
            const d = await r.json();
            throw new Error(d.mensaje || 'Error al guardar');
        }

        mostrarMensaje('✓ Movimiento registrado correctamente', 'ok');

        setTimeout(() => {
            cerrarModal();
            cargarMovimientos();
        }, 1000);

    } catch (e) {
        mostrarMensaje(e.message, 'err');
    } finally {
        btn.disabled = false;
        btn.textContent = 'Guardar';
    }
}

// Renderizar tabla
function renderTabla(data) {
    if (!data.length) {
        document.getElementById('tablaMovimientos').innerHTML =
            '<tr><td colspan="7" class="empty">No hay movimientos registrados</td></tr>';
        return;
    }

    document.getElementById('tablaMovimientos').innerHTML = data.map(m => `
        <tr>
            <td style="color:var(--muted);font-size:12px">#${m.id}</td>
            <td>${badgeTipo(m.tipo)}</td>
            <td>${m.bodegaOrigen?.nombre || '—'}</td>
            <td>${m.bodegaDestino?.nombre || '—'}</td>
            <td>${m.usuario?.nombre || '—'}</td>
            <td class="prod-list">
                ${m.detalles?.map(d => `${d.producto?.nombre} (${d.cantidad})`).join('<br>') || '—'}
            </td>
            <td style="font-size:12px;color:var(--muted)">${fmtFecha(m.fecha)}</td>
        </tr>`).join('');
}

// Cargar todos los movimientos
async function cargarMovimientos() {
    document.getElementById('tablaMovimientos').innerHTML =
        '<tr><td colspan="7" class="loading">Cargando...</td></tr>';

    const r = await apiFetch('/api/movimientos');
    if (!r) return;
    const data = await r.json();

    document.getElementById('totalCount').textContent = `${data.length} movimientos`;
    renderTabla(data);
}

// Inicializar
cargarMovimientos();