package ir.maktabsharif.OnlineExamManagementProject.service.base;


public interface BaseService<T> {

     T save(T entity);

    void deleteById(Long id);
}

