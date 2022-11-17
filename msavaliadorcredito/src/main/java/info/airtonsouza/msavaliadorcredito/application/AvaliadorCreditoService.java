package info.airtonsouza.msavaliadorcredito.application;

import feign.FeignException;
import info.airtonsouza.msavaliadorcredito.application.exceptions.DadosClienteNotFoundException;
import info.airtonsouza.msavaliadorcredito.application.exceptions.ErroComunicacaoMicroServicesException;
import info.airtonsouza.msavaliadorcredito.application.exceptions.ErroSolicitacaoCartaoException;
import info.airtonsouza.msavaliadorcredito.domain.model.*;
import info.airtonsouza.msavaliadorcredito.infra.CartoesResourceClient;
import info.airtonsouza.msavaliadorcredito.infra.ClienteResourceClient;
import info.airtonsouza.msavaliadorcredito.infra.SolicitacaoEmissaCartaoPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient clientesClient;
    private final CartoesResourceClient cartoesClient;
    private final SolicitacaoEmissaCartaoPublisher emissaCartaoPublisher;

    public SituacaoCliente obterSituacaoCliente(String cpf) throws DadosClienteNotFoundException, ErroComunicacaoMicroServicesException {

        try {
            ResponseEntity<DadosCliente> dadosClientResponse = clientesClient.dadosCliente(cpf);
            ResponseEntity<List<CartaoCliente>> cartoesResponse = cartoesClient.getCartoesByClient(cpf);

            return SituacaoCliente
                    .builder()
                    .cliente(dadosClientResponse.getBody())
                    .cartoes(cartoesResponse.getBody())
                    .build();

        } catch (FeignException.FeignClientException e) {
            int status  = e.status();

            if(HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
            }

            throw new ErroComunicacaoMicroServicesException(e.getMessage(), status);
        }

    }

    public RetornoAvaliacaoCliente realizarAvaliacao(String cpf, Long renda) throws DadosClienteNotFoundException, ErroComunicacaoMicroServicesException {
        
        
        try {
            ResponseEntity<DadosCliente> dadosClienteResponse = clientesClient.dadosCliente(cpf);
            ResponseEntity<List<Cartao>> cartoesResponse = cartoesClient.getCartoesRendaAte(renda);

            List<Cartao> cartoes = cartoesResponse.getBody();

            var cartoesAprovados = cartoes.stream().map(cartao -> {

                DadosCliente dadosCliente = dadosClienteResponse.getBody();

                BigDecimal limiteBasico = cartao.getLimiteBasico();
                BigDecimal idadeBD = BigDecimal.valueOf(dadosCliente.getIdade());
                var fator = idadeBD.divide(BigDecimal.valueOf(10));
                BigDecimal limiteAprovado = fator.multiply(limiteBasico);

                CartaoAprovado aprovado = new CartaoAprovado();
                aprovado.setCartao(cartao.getNome());
                aprovado.setBandeira(cartao.getBandeira());
                aprovado.setLimiteAprovado(limiteAprovado);

                return aprovado;
            }).collect(Collectors.toList());


            return new RetornoAvaliacaoCliente(cartoesAprovados);

        } catch (FeignException.FeignClientException e) {
            int status  = e.status();

            if(HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
            }

            throw new ErroComunicacaoMicroServicesException(e.getMessage(), status);
        }
    }

    public ProtocoloSolicitacaoCartao solicitarEmissaoCartao(DadosSolicitacaoEmissaoCartao dados) throws ErroSolicitacaoCartaoException {
        try {
            emissaCartaoPublisher.solicitarCartao(dados);
            var protocolo = UUID.randomUUID().toString();

            return new ProtocoloSolicitacaoCartao(protocolo);
        } catch (Exception e) {
                throw new ErroSolicitacaoCartaoException(e.getMessage());
        }
    }
}
