package com.logitrack.logitrack.auth;

import com.logitrack.logitrack.model.Usuario;

public record RegisterRequest(String nombre, String email, String password, Usuario.Rol rol) {}