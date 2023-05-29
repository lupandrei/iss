package repository;

public interface IUserRepository<E,ID> {
    E findById(ID id);
}
