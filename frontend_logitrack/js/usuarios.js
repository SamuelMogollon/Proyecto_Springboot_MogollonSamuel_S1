const API = 'http://localhost:8080';

// Verificar autenticación
if (!localStorage.getItem('token')) {
    window.location.href = '../index.html';
}

// Variable para saber si estamos editando o creando
let usuarioEditandoId = null;

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

// Abrir modal para crear usuario
function abrirModal() {
    usuarioEditandoId = null;
    document.getElementById('modalTitle').textContent = 'Nuevo usuario';
    document.getElementById('inputNombre').value = '';
    document.getElementById('inputEmail').value = '';
    document.getElementById('inputPassword').value = '';
    document.getElementById('inputRol').value = 'ADMIN';
    document.getElementById('inputPassword').required = true;
    document.getElementById('inputPassword').placeholder = 'Mínimo 6 caracteres';
    limpiarMensaje();
    document.getElementById('modalOverlay').classList.add('show');
    document.getElementById('modal').classList.add('show');
}

// Abrir modal para editar usuario
function editarUsuario(id, nombre, email, rol) {
    usuarioEditandoId = id;
    document.getElementById('modalTitle').textContent = 'Editar usuario';
    document.getElementById('inputNombre').value = nombre;
    document.getElementById('inputEmail').value = email;
    document.getElementById('inputPassword').value = '';
    document.getElementById('inputPassword').required = false;
    document.getElementById('inputPassword').placeholder = 'Dejar vacío para no cambiar';
    document.getElementById('inputRol').value = rol;
    limpiarMensaje();
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

// Guardar usuario — crea o actualiza según usuarioEditandoId
async function guardarUsuario() {
    const nombre = document.getElementById('inputNombre').value.trim();
    const email = document.getElementById('inputEmail').value.trim();
    const password = document.getElementById('inputPassword').value.trim();
    const rol = document.getElementById('inputRol').value;
    const btn = document.getElementById('btnGuardar');

    // Validaciones básicas
    if (!nombre || !email) {
        mostrarMensaje('Nombre y email son obligatorios.', 'err');
        return;
    }

    if (!usuarioEditandoId && !password) {
        mostrarMensaje('La contraseña es obligatoria al crear un usuario.', 'err');
        return;
    }

    btn.disabled = true;
    btn.textContent = 'Guardando...';

    // Construir body — si estamos editando y no hay password, no lo incluimos
    const body = { nombre, email, rol };
    if (password) body.password = password;

    try {
        const r = await apiFetch(
            usuarioEditandoId ? `/api/usuarios/${usuarioEditandoId}` : '/api/usuarios',
            {
                method: usuarioEditandoId ? 'PUT' : 'POST',
                body: JSON.stringify(body)
            }
        );

        if (!r) return;

        if (!r.ok) {
            const d = await r.json();
            throw new Error(d.mensaje || 'Error al guardar');
        }

        mostrarMensaje('✓ Usuario guardado correctamente', 'ok');

        // Recargar tabla y cerrar modal
        setTimeout(() => {
            cerrarModal();
            cargarUsuarios();
        }, 1000);

    } catch (e) {
        mostrarMensaje(e.message, 'err');
    } finally {
        btn.disabled = false;
        btn.textContent = 'Guardar';
    }
}

// Eliminar usuario
// Variable para guardar el id a eliminar
let usuarioEliminandoId = null;

// Abrir modal de confirmación
function eliminarUsuario(id, nombre) {
    usuarioEliminandoId = id;
    document.getElementById('confirmNombre').textContent = nombre;
    document.getElementById('modalConfirmOverlay').classList.add('show');
    document.getElementById('modalConfirm').classList.add('show');
    document.getElementById('btnConfirmEliminar').onclick = confirmarEliminar;
}

// Cerrar modal de confirmación
function cerrarConfirm() {
    document.getElementById('modalConfirmOverlay').classList.remove('show');
    document.getElementById('modalConfirm').classList.remove('show');
    usuarioEliminandoId = null;
}

// Ejecutar eliminación
async function confirmarEliminar() {
    if (!usuarioEliminandoId) return;
    try {
        const r = await apiFetch(`/api/usuarios/${usuarioEliminandoId}`, { method: 'DELETE' });
        if (!r) return;
        if (!r.ok) throw new Error('Error al eliminar');
        cerrarConfirm();
        cargarUsuarios();
    } catch (e) {
        alert(e.message);
    }
}

// Cargar y renderizar tabla de usuarios
async function cargarUsuarios() {
    document.getElementById('tablaUsuarios').innerHTML =
        '<tr><td colspan="5" class="loading">Cargando...</td></tr>';

    const r = await apiFetch('/api/usuarios');
    if (!r) return;
    const data = await r.json();

    document.getElementById('totalCount').textContent = `${data.length} usuarios`;

    if (!data.length) {
        document.getElementById('tablaUsuarios').innerHTML =
            '<tr><td colspan="5" class="empty">No hay usuarios registrados</td></tr>';
        return;
    }

    document.getElementById('tablaUsuarios').innerHTML = data.map(u => `
        <tr>
            <td style="color:var(--muted);font-size:12px">#${u.id}</td>
            <td style="font-weight:500">${u.nombre}</td>
            <td style="color:var(--text-2)">${u.email}</td>
            <td>
                <span class="badge-${u.rol === 'ADMIN' ? 'admin' : 'empleado'}">
                    ${u.rol}
                </span>
            </td>
            <td>
                <button class="btn-edit" onclick="editarUsuario(${u.id}, '${u.nombre}', '${u.email}', '${u.rol}')">
                    Editar
                </button>
                <button class="btn-delete" onclick="eliminarUsuario(${u.id}, '${u.nombre}')">
                    Eliminar
                </button>
            </td>
        </tr>`).join('');
}

// Inicializar
cargarUsuarios();