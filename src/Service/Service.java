package Service;

import java.util.List;

public interface Service<T> {
    void insertar(T entidad) throws Exception;
    void actualizar(T entidad) throws Exception;
    void eliminar(Long id) throws Exception;
    T getById(Long id) throws Exception;
    List<T> getAll() throws Exception;
}
