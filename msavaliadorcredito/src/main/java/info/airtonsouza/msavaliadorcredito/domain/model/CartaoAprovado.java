package info.airtonsouza.msavaliadorcredito.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartaoAprovado {

    private String cartao;
    private String bandeira;
    private BigDecimal limiteAprovado;

}
