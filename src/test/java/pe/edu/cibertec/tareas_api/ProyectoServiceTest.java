package pe.edu.cibertec.tareas_api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.cibertec.tareas_api.model.Proyecto;
import pe.edu.cibertec.tareas_api.model.Usuario;
import pe.edu.cibertec.tareas_api.repository.ProyectoRepository;
import pe.edu.cibertec.tareas_api.repository.UsuarioRepository;
import pe.edu.cibertec.tareas_api.service.ProyectoService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProyectoServiceTest {
    @Mock
    private ProyectoRepository proyectoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ProyectoService proyectoService;

    private Usuario usuario;
    private Proyecto proyecto;

    @BeforeEach
    void setUp(){
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan Pérez");
        usuario.setEmail("juan.perez@example.com");
        usuario.setRol("Jefe");
        usuario.setActivo(true);

        proyecto = new Proyecto();
        proyecto.setId(1L);
        proyecto.setNombre("App de Reservas");
        proyecto.setDescripcion("Aplicación móvil para reservas en restaurantes.");
        proyecto.setFechaInicio(LocalDate.of(2025,6, 10));
        proyecto.setFechaFin(LocalDate.of(2025, 11, 23));
        proyecto.setUsuario(usuario);
        proyecto.setActivo(true);
    }

    @Test
    void listarTodosProyectos(){
        when(proyectoRepository.findAll()).thenReturn(Collections.singletonList(proyecto));

        var resultado = proyectoService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(proyectoRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_Exitoso(){
        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));

        var resultado = proyectoService.buscarPorId(1L);

        assertNotNull("Aplicación móvil para reservas en restaurantes.", resultado.get().getDescripcion());
        assertEquals("App de Reservas", resultado.get().getNombre());
        verify(proyectoRepository, times(1)).findById(1L);
    }

    @Test
    void crear_Exitoso(){
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(proyectoRepository.save(any(Proyecto.class))).thenReturn(proyecto);

        Proyecto resultado = proyectoService.crear(proyecto);

        assertNotNull(resultado);
        assertEquals("App de Reservas", resultado.getNombre());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(proyectoRepository, times(1)).save(proyecto);
    }

    @Test
    void crear_FechaInvalida(){
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setActivo(true);

        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Proyecto inválido");
        proyecto.setFechaInicio(LocalDate.of(2025, 5, 10));
        proyecto.setFechaFin(LocalDate.of(2025, 4, 10));
        proyecto.setUsuario(usuario);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> proyectoService.crear(proyecto)
        );

        assertEquals(
                "La fecha fin no puede ser anterior a la fecha inicio",
                ex.getMessage()
        );
    }
}
















