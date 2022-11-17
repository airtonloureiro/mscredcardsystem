package info.airtonsouza.msavaliadorcredito.domain.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartaoCliente {

    private String nome;
    private String bandeira;
    private BigDecimal limiteLiberado;

    @Data
    public static class DadosAvaliacao {

        private String cpf;
        private Long renda;
    }
}
