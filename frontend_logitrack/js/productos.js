const API = 'http://172.16.41.128:8080';

// Verificar autenticación
if (!localStorage.getItem('token')) {
    window.location.href = '../index.html';
}

// Variables de estado
let productoEditandoId = null;
let productoEliminandoId = null;
let mostrandoStockBajo = false;

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

// Toggle entre todos los productos y stock bajo
function toggleStockBajo() {
    mostrandoStockBajo = !mostrandoStockBajo;
    const btn = document.getElementById('btnFiltro');
    btn.classList.toggle('active', mostrandoStockBajo);

    if (mostrandoStockBajo) {
        document.getElementById('panelTitle').textContent = '⚠ Productos con stock bajo';
        cargarStockBajo();
    } else {
        document.getElementById('panelTitle').textContent = 'Productos registrados';
        cargarProductos();
    }
}

// Abrir modal para crear producto
function abrirModal() {
    productoEditandoId = null;
    document.getElementById('modalTitle').textContent = 'Nuevo producto';
    document.getElementById('inputNombre').value = '';
    document.getElementById('inputCategoria').value = '';
    document.getElementById('inputPrecio').value = '';
    limpiarMensaje();
    document.getElementById('modalOverlay').classList.add('show');
    document.getElementById('modal').classList.add('show');
}

// Abrir modal para editar producto
function editarProducto(id, nombre, categoria, precio) {
    productoEditandoId = id;
    document.getElementById('modalTitle').textContent = 'Editar producto';
    document.getElementById('inputNombre').value = nombre;
    document.getElementById('inputCategoria').value = categoria;
    document.getElementById('inputPrecio').value = precio;
    limpiarMensaje();
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

// Mostrar mensaje en modal
function mostrarMensaje(texto, tipo) {
    const msg = document.getElementById('modalMsg');
    msg.textContent = texto;
    msg.className = `modal-msg ${tipo} show`;
}

// Guardar producto — crea o actualiza
async function guardarProducto() {
    const nombre = document.getElementById('inputNombre').value.trim();
    const categoria = document.getElementById('inputCategoria').value.trim();
    const precio = document.getElementById('inputPrecio').value;
    const btn = document.getElementById('btnGuardar');

    // Validaciones
    if (!nombre || !categoria || !precio) {
        mostrarMensaje('Todos los campos son obligatorios.', 'err');
        return;
    }

    btn.disabled = true;
    btn.textContent = 'Guardando...';

    const body = {
        nombre,
        categoria,
        precio: parseFloat(precio)
    };

    try {
        const r = await apiFetch(
            productoEditandoId ? `/api/productos/${productoEditandoId}` : '/api/productos',
            {
                method: productoEditandoId ? 'PUT' : 'POST',
                body: JSON.stringify(body)
            }
        );

        if (!r) return;

        if (!r.ok) {
            const d = await r.json();
            throw new Error(d.mensaje || 'Error al guardar');
        }

        mostrarMensaje('✓ Producto guardado correctamente', 'ok');

        setTimeout(() => {
            cerrarModal();
            cargarProductos();
        }, 1000);

    } catch (e) {
        mostrarMensaje(e.message, 'err');
    } finally {
        btn.disabled = false;
        btn.textContent = 'Guardar';
    }
}

// Abrir modal de confirmación eliminar
function eliminarProducto(id, nombre) {
    productoEliminandoId = id;
    document.getElementById('confirmNombre').textContent = nombre;
    document.getElementById('modalConfirmOverlay').classList.add('show');
    document.getElementById('modalConfirm').classList.add('show');
    document.getElementById('btnConfirmEliminar').onclick = confirmarEliminar;
}

// Cerrar modal de confirmación
function cerrarConfirm() {
    document.getElementById('modalConfirmOverlay').classList.remove('show');
    document.getElementById('modalConfirm').classList.remove('show');
    productoEliminandoId = null;
}

// Ejecutar eliminación
async function confirmarEliminar() {
    if (!productoEliminandoId) return;
    try {
        const r = await apiFetch(`/api/productos/${productoEliminandoId}`, { method: 'DELETE' });
        if (!r) return;
        if (!r.ok) throw new Error('Error al eliminar');
        cerrarConfirm();
        cargarProductos();
    } catch (e) {
        alert(e.message);
    }
}

// Renderizar filas de la tabla
function renderTabla(data) {
    if (!data.length) {
        document.getElementById('tablaProductos').innerHTML =
            '<tr><td colspan="6" class="empty">No hay productos registrados</td></tr>';
        return;
    }

    document.getElementById('tablaProductos').innerHTML = data.map(p => `
        <tr>
            <td style="color:var(--muted);font-size:12px">#${p.id}</td>
            <td style="font-weight:500">${p.nombre}</td>
            <td>
                <span class="badge" style="background:var(--off);color:var(--muted)">
                    ${p.categoria}
                </span>
            </td>
            <td>$${Number(p.precio).toLocaleString('es-CO')}</td>
            <td class="${p.stockTotal < 10 ? 'stock-low' : ''}">${p.stockTotal} uds</td>
            <td>
                <button class="btn-edit" onclick="editarProducto(${p.id}, '${p.nombre}', '${p.categoria}', ${p.precio})">
                    Editar
                </button>
                <button class="btn-delete" onclick="eliminarProducto(${p.id}, '${p.nombre}')">
                    Eliminar
                </button>
            </td>
        </tr>`).join('');
}

// Cargar todos los productos
async function cargarProductos() {
    document.getElementById('tablaProductos').innerHTML =
        '<tr><td colspan="6" class="loading">Cargando...</td></tr>';

    const r = await apiFetch('/api/productos');
    if (!r) return;
    const data = await r.json();

    document.getElementById('totalCount').textContent = `${data.length} productos`;
    renderTabla(data);
}

// Cargar productos con stock bajo
async function cargarStockBajo() {
    document.getElementById('tablaProductos').innerHTML =
        '<tr><td colspan="6" class="loading">Cargando...</td></tr>';

    const r = await apiFetch('/api/productos/stock-bajo');
    if (!r) return;
    const data = await r.json();

    document.getElementById('totalCount').textContent = `${data.length} productos con stock bajo`;
    renderTabla(data);
}

// Inicializar
cargarProductos();