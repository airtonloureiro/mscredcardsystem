package info.airtonsouza.mscartoes.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
public class ClienteCartao {

    @Id
    private Long id;
    private String cpf;

    @ManyToOne
    @JoinColumn(name = "cartao_id")
    private Cartao cartao;

    private BigDecimal limite;
}
