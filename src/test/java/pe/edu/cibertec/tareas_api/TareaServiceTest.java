package pe.edu.cibertec.tareas_api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.cibertec.tareas_api.model.Proyecto;
import pe.edu.cibertec.tareas_api.model.Tarea;
import pe.edu.cibertec.tareas_api.model.Usuario;
import pe.edu.cibertec.tareas_api.repository.ProyectoRepository;
import pe.edu.cibertec.tareas_api.repository.TareaRepository;
import pe.edu.cibertec.tareas_api.service.TareaService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TareaServiceTest {
    @Mock
    private TareaRepository tareaRepository;

    @Mock
    private ProyectoRepository proyectoRepository;

    @InjectMocks
    private TareaService tareaService;

    private Tarea tarea;
    private Proyecto proyecto;
    private Usuario usuario;

    @BeforeEach
    void setUp(){
        proyecto = new Proyecto();
        proyecto.setId(1L);
        proyecto.setNombre("App de Reservas");
        proyecto.setDescripcion("Aplicación móvil para reservas en restaurantes.");
        proyecto.setFechaInicio(LocalDate.of(2025,6, 10));
        proyecto.setFechaFin(LocalDate.of(2025, 11, 23));
        proyecto.setUsuario(usuario);
        proyecto.setActivo(true);

        tarea = new Tarea();
        tarea.setId(1L);
        tarea.setTitulo("Diseñar base de datos");
        tarea.setDescripcion("Crear modelo entidad-relación para el sistema.");
        tarea.setEstado("EN_PROGRESO");
        tarea.setPrioridad("ALTA");
        tarea.setProyecto(proyecto);
        tarea.setActivo(true);
    }

    @Test
    void listarTodosTareas(){
        when(tareaRepository.findAll()).thenReturn(Collections.singletonList(tarea));

        var resultado = tareaService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(tareaRepository, times(1)).findAll();
    }

    @Test
    void crear_Exitoso(){
        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));
        when(tareaRepository.save(any(Tarea.class))).thenReturn(tarea);

        Tarea resultado = tareaService.crear(tarea);

        assertNotNull(resultado);
        assertEquals("EN_PROGRESO", resultado.getEstado());
        verify(proyectoRepository, times(1)).findById(1L);
        verify(tareaRepository, times(1)).save(tarea);
    }

    @Test
    void crear_EstadoInavalido(){

        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            tareaService.crear(tarea);
        });

        assertEquals("Estado de tarea inválido", exception.getMessage());
        verify(proyectoRepository, times(1)).findById(1L);
        verify(tareaRepository, never()).save(any());
    }
}




















