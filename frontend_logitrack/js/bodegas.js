const API = 'http://172.16.41.128:8080';

// Verificar autenticación
if (!localStorage.getItem('token')) {
    window.location.href = '../index.html';
}

// Variable para saber si estamos editando o creando
let bodegaEditandoId = null;
let bodegaEliminandoId = null;

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

// Cargar usuarios en el select del modal
async function cargarUsuariosSelect() {
    const r = await apiFetch('/api/usuarios');
    if (!r) return;
    const usuarios = await r.json();
    const select = document.getElementById('inputEncargado');
    select.innerHTML = '<option value="">Seleccionar encargado...</option>';
    usuarios.forEach(u => {
        select.innerHTML += `<option value="${u.id}">${u.nombre}</option>`;
    });
}

// Abrir modal para crear bodega
async function abrirModal() {
    bodegaEditandoId = null;
    document.getElementById('modalTitle').textContent = 'Nueva bodega';
    document.getElementById('inputNombre').value = '';
    document.getElementById('inputUbicacion').value = '';
    document.getElementById('inputCapacidad').value = '';
    limpiarMensaje();
    await cargarUsuariosSelect();
    document.getElementById('modalOverlay').classList.add('show');
    document.getElementById('modal').classList.add('show');
}

// Abrir modal para editar bodega
async function editarBodega(id, nombre, ubicacion, capacidad, idEncargado) {
    bodegaEditandoId = id;
    document.getElementById('modalTitle').textContent = 'Editar bodega';
    document.getElementById('inputNombre').value = nombre;
    document.getElementById('inputUbicacion').value = ubicacion;
    document.getElementById('inputCapacidad').value = capacidad;
    limpiarMensaje();
    await cargarUsuariosSelect();
    document.getElementById('inputEncargado').value = idEncargado;
    document.getElementById('modalOverlay').classList.add('show');
    document.getElementById('modal').classList.add('show');
}

// Cerrar modal
function cerrarModal() {
    document.getElementById('modalOverlay').classList.remove('show');
    document.getElementById('modal').classList.remove('show');
}

// Limpiar mensaje del modal
function limpiarMensaje() {
    const msg = document.getElementById('modalMsg');
    msg.className = 'modal-msg';
    msg.textContent = '';
}

// Mostrar mensaje en el modal
function mostrarMensaje(texto, tipo) {
    const msg = document.getElementById('modalMsg');
    msg.textContent = texto;
    msg.className = `modal-msg ${tipo} show`;
}

// Guardar bodega — crea o actualiza
async function guardarBodega() {
    const nombre = document.getElementById('inputNombre').value.trim();
    const ubicacion = document.getElementById('inputUbicacion').value.trim();
    const capacidad = document.getElementById('inputCapacidad').value;
    const idEncargado = document.getElementById('inputEncargado').value;
    const btn = document.getElementById('btnGuardar');

    // Validaciones
    if (!nombre || !ubicacion || !capacidad || !idEncargado) {
        mostrarMensaje('Todos los campos son obligatorios.', 'err');
        return;
    }

    btn.disabled = true;
    btn.textContent = 'Guardando...';

    const body = {
        nombre,
        ubicacion,
        capacidad: parseInt(capacidad),
        idEncargado: parseInt(idEncargado)
    };

    try {
        const r = await apiFetch(
            bodegaEditandoId ? `/api/bodegas/${bodegaEditandoId}` : '/api/bodegas',
            {
                method: bodegaEditandoId ? 'PUT' : 'POST',
                body: JSON.stringify(body)
            }
        );

        if (!r) return;

        if (!r.ok) {
            const d = await r.json();
            throw new Error(d.mensaje || 'Error al guardar');
        }

        mostrarMensaje('✓ Bodega guardada correctamente', 'ok');

        setTimeout(() => {
            cerrarModal();
            cargarBodegas();
        }, 1000);

    } catch (e) {
        mostrarMensaje(e.message, 'err');
    } finally {
        btn.disabled = false;
        btn.textContent = 'Guardar';
    }
}

// Abrir modal de confirmación de eliminación
function eliminarBodega(id, nombre) {
    bodegaEliminandoId = id;
    document.getElementById('confirmNombre').textContent = nombre;
    document.getElementById('modalConfirmOverlay').classList.add('show');
    document.getElementById('modalConfirm').classList.add('show');
    document.getElementById('btnConfirmEliminar').onclick = confirmarEliminar;
}

// Cerrar modal de confirmación
function cerrarConfirm() {
    document.getElementById('modalConfirmOverlay').classList.remove('show');
    document.getElementById('modalConfirm').classList.remove('show');
    bodegaEliminandoId = null;
}

// Ejecutar eliminación
async function confirmarEliminar() {
    if (!bodegaEliminandoId) return;
    try {
        const r = await apiFetch(`/api/bodegas/${bodegaEliminandoId}`, { method: 'DELETE' });
        if (!r) return;
        if (!r.ok) throw new Error('Error al eliminar');
        cerrarConfirm();
        cargarBodegas();
    } catch (e) {
        alert(e.message);
    }
}

// Cargar y renderizar tabla de bodegas
async function cargarBodegas() {
    document.getElementById('tablaBodegas').innerHTML =
        '<tr><td colspan="6" class="loading">Cargando...</td></tr>';

    const r = await apiFetch('/api/bodegas');
    if (!r) return;
    const data = await r.json();

    document.getElementById('totalCount').textContent = `${data.length} bodegas`;

    if (!data.length) {
        document.getElementById('tablaBodegas').innerHTML =
            '<tr><td colspan="6" class="empty">No hay bodegas registradas</td></tr>';
        return;
    }

    document.getElementById('tablaBodegas').innerHTML = data.map(b => `
        <tr>
            <td style="color:var(--muted);font-size:12px">#${b.id}</td>
            <td style="font-weight:500">${b.nombre}</td>
            <td style="color:var(--text-2)">${b.ubicacion}</td>
            <td>${b.capacidad} uds</td>
            <td>${b.encargado?.nombre || '—'}</td>
            <td>
                <button class="btn-edit" onclick="editarBodega(${b.id}, '${b.nombre}', '${b.ubicacion}', ${b.capacidad}, ${b.encargado?.id})">
                    Editar
                </button>
                <button class="btn-delete" onclick="eliminarBodega(${b.id}, '${b.nombre}')">
                    Eliminar
                </button>
            </td>
        </tr>`).join('');
}

// Inicializar
cargarBodegas();