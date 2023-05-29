package repository;

import domain.Client;

public interface IClientRepository extends IUserRepository<Client,String>{
    void updatePenaltyValue(int penaltyValue, String idClient);
}
