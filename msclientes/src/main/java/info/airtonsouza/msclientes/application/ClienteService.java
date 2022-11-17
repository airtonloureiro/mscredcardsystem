package info.airtonsouza.msclientes.application;

import info.airtonsouza.msclientes.domain.Cliente;
import info.airtonsouza.msclientes.infra.repositories.ClienteRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service()
@RequiredArgsConstructor
public class ClienteService {


    private final ClienteRepository repository;


    public Cliente save(Cliente cliente) {
        return repository.save(cliente);
    }

    public Optional<Cliente> findByCpf(String cpf) {
        return repository.findByCpf(cpf);
    }
}
