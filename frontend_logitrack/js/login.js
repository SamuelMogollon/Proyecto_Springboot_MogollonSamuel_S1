// Función principal que maneja el login
async function doLogin() {

    // Obtener los valores de los campos
    const email = document.getElementById('email').value.trim();
    const pass = document.getElementById('password').value.trim();

    // Referencias a elementos del DOM
    const btn = document.getElementById('btn');
    const txt = document.getElementById('btnTxt');
    const icon = document.getElementById('btnIcon');
    const msg = document.getElementById('msg');

    // Limpiar mensaje anterior
    msg.className = 'msg';
    msg.textContent = '';

    // Validar que los campos no estén vacíos
    if (!email || !pass) {
        msg.textContent = 'Por favor completa todos los campos.';
        msg.className = 'msg err show';
        return;
    }

    // Deshabilitar botón y mostrar spinner
    btn.disabled = true;
    txt.textContent = 'Verificando';
    icon.innerHTML = '<div class="spinner"></div>';

    try {
        // Petición POST al backend usando la función de api.js
        const r = await fetch('http://localhost:8080/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password: pass })
        });

        const d = await r.json();

        // Si la respuesta no es exitosa lanzar error
        if (!r.ok) throw new Error(d.mensaje || 'Credenciales incorrectas');

        // Guardar token en localStorage
        localStorage.setItem('token', d.token);

        // Mostrar mensaje de éxito y redirigir al dashboard
        msg.textContent = '✓ Acceso concedido — redirigiendo';
        msg.className = 'msg ok show';
        setTimeout(() => { window.location.href = 'pages/dashboard.html'; }, 1200);

    } catch (e) {
        // Mostrar error y restaurar botón
        msg.textContent = e.message;
        msg.className = 'msg err show';
        btn.disabled = false;
        txt.textContent = 'Ingresar al sistema';
        icon.textContent = '→';
    }
}

// Permitir login con la tecla Enter
document.addEventListener('keydown', e => {
    if (e.key === 'Enter') doLogin();
});