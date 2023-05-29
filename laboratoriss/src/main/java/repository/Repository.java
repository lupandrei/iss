package repository;

import java.util.List;

public interface Repository<E,ID> {
    void add(E entity);
    List<E> getAll();
}
