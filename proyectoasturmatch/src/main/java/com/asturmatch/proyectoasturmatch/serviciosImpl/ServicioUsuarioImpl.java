package com.asturmatch.proyectoasturmatch.serviciosImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.asturmatch.proyectoasturmatch.modelo.Rol;
import com.asturmatch.proyectoasturmatch.modelo.Usuario;
import com.asturmatch.proyectoasturmatch.repositorios.UsuarioRepository;
import com.asturmatch.proyectoasturmatch.servicios.ServicioUsuario;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ServicioUsuarioImpl implements ServicioUsuario {

    @Autowired
    private UsuarioRepository usuario_R;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodosUsuarios() {
        return usuario_R.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuario_R.findById(id);
    }
    
    @Override
    @Transactional
    public Usuario obtenerUsuarioPorEmail(String email) {
        return usuario_R.findByEmail(email); 
    }
    
    @Transactional
    public Usuario obtenerUsuarioPorNombre(String nombre) {
        return usuario_R.findByNombre(nombre);
    }
    
    @PostConstruct
    public void crearAdminSiNoExiste() {
        Usuario admin = usuario_R.findByNombre("admin");
        if (admin == null) {
            Usuario usuarioAdmin = new Usuario();
            usuarioAdmin.setNombre("Admin");
            usuarioAdmin.setEmail("admin@admin.com");
            usuarioAdmin.setContraseña("admin");
            usuarioAdmin.setRol(Rol.ADMIN);
            usuarioAdmin = usuario_R.save(usuarioAdmin);

            System.out.println("Administrador creado con éxito.");
        } else {
            System.out.println("El usuario administrador ya existe.");
        }
    }

    /**
     * Guardamos un nuevo usuario tras validar:
     * <ul>
     *   <li>Formato correcto de email.</li>
     *   <li>Unicidad del email en la base de datos.</li>
     *   <li>El nombre solo contiene letras (incluye acentos) y espacios.</li>
     *   <li>La contraseña tiene al menos 8 caracteres, incluyendo al menos un dígito y un carácter especial.</li>
     * </ul>
     *
     * @param usuario el objeto Usuario a guardar
     * @return el usuario persistido
     * @throws IllegalArgumentException si alguna validación de formato o negocio falla
     */
    @Override
    @Transactional
    public Usuario guardarUsuario(Usuario usuario) {
    	
    	//EXPRESIONES REGULARES PARA LAS VALIDACIONES
        String regexEmail = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        String regexNombre = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$";
        String regexPassword = "^(?=.*\\d)" + "(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?])" + ".{8,}$";
        
        if (usuario.getEmail() == null || !usuario.getEmail().matches(regexEmail)) {
            throw new IllegalArgumentException("El email no tiene un formato válido");
        }

        if (usuario_R.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("El email ya está en uso");
        }

        if (usuario.getNombre() == null || !usuario.getNombre().matches(regexNombre)) {
            throw new IllegalArgumentException("El nombre solo puede contener letras y espacios");
        }

        if (usuario.getContraseña() == null || !usuario.getContraseña().matches(regexPassword)) {
            throw new IllegalArgumentException(
                "La contraseña debe tener al menos 8 caracteres, incluyendo al menos un número " +
                "y un carácter especial"
            );
        }

        return usuario_R.save(usuario);
    }


    @Override
    @Transactional
    public void eliminarUsuario(Long id) {
        if (!usuario_R.existsById(id)) {
            throw new EntityNotFoundException("Usuario no encontrado con ID: " + id);
        }
        usuario_R.deleteById(id);
    }

    @Override
    @Transactional
    public Usuario actualizarUsuario(Long id, Usuario usuario) {
        if (!usuario_R.existsById(id)) {
            throw new EntityNotFoundException("Usuario no encontrado con ID: " + id);
        }
        usuario.setId(id);
        validarUsuario(usuario);
        return usuario_R.save(usuario);
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del usuario es obligatorio");
        }
        if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            throw new IllegalArgumentException("El email del usuario es obligatorio");
        }
        if (usuario.getContraseña() == null || usuario.getContraseña().length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        }
    }
}

