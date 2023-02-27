package ru.otus.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends CrudRepository<Client, Long> {

    @Query(value = """
                select  c.id as client_id,
                        c.first_name as first_name,
                        c.last_name as last_name,
                        p.id as phone_id,
                        p.number as number,
                        a.client_address as client_address
                        from client c
                            left join phone p on c.id = p.client_id
                            left join address a on c.id = a.client_id
                        order by c.id
            """, resultSetExtractorClass = ClientResultSetExtractorClass.class)
    List<Client> findAllByCustomQuery();

    @Override
    List<Client> findAll();

    Optional<Client> findByFirstName(String name);

    @Override
    void deleteById(Long id);

    @Modifying
    @Query("update client set first_name = :firstName, last_name = :lastName where id = :id")
    void updateClient(@Param("id") long id,
                      @Param("firstName") String firstName,
                      @Param("lastName") String lastName);
}
