package pe.edu.cibertec.tareas_api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.cibertec.tareas_api.model.Usuario;
import pe.edu.cibertec.tareas_api.repository.UsuarioRepository;
import pe.edu.cibertec.tareas_api.service.UsuarioService;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp(){

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan Pérez");
        usuario.setEmail("juan.perez@example.com");
        usuario.setRol("Jefe");
        usuario.setActivo(true);
    }

    @Test
    void listarTodoslosUsuarios(){
        when(usuarioRepository.findAll()).thenReturn(Collections.singletonList(usuario));

        var resultado = usuarioService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_Exitoso(){
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        var resultado = usuarioService.buscarPorId(1L);

        assertNotNull("juan.perez@example.com", resultado.get().getEmail());
        assertEquals("Juan Pérez", resultado.get().getNombre());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void crear_Exitoso(){
        when(usuarioRepository.existsByEmail("juan.perez@example.com")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        var resultado = usuarioService.crear(usuario);

        assertNotNull(resultado);
        assertEquals("juan.perez@example.com", resultado.getEmail());
        assertEquals(true, resultado.getActivo());
        verify(usuarioRepository, times(1)).existsByEmail("juan.perez@example.com");
        verify(usuarioRepository, times(1)).save(usuario);

    }
}






















