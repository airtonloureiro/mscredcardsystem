package info.airtonsouza.msavaliadorcredito.application.exceptions;

public class DadosClienteNotFoundException extends Exception{

    public DadosClienteNotFoundException() {
        super("Dados do cliente não encontrado para o CPF informado.");
    }
}
