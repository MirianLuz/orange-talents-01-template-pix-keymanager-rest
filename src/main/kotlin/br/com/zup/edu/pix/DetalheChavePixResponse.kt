package br.com.zup.edu.pix

import br.com.zup.edu.CarregaChavePixResponse
import br.com.zup.edu.TipoConta
import io.micronaut.core.annotation.Introspected
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Introspected
class DetalheChavePixResponse(chaveResponse: CarregaChavePixResponse) {

    val pixId = chaveResponse.pixId
    val tipo = chaveResponse.chave.tipoChave
    val chave = chaveResponse.chave.chave

    val criadaEm = chaveResponse.chave.criadaEm.let {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
    }

    val tipoConta = when (chaveResponse.chave.conta.tipoConta){
        TipoConta.CONTA_CORRENTE -> "CONTA_CORRENTE"
        TipoConta.CONTA_POUPANCA -> "CONTA_POUPANCA"
        else -> "NAO_RECONHECIDA"
    }

    val conta = mapOf(Pair("tipo", tipoConta),
    Pair("instituicao", chaveResponse.chave.conta.instituicao),
    Pair("nomeDoTitular", chaveResponse.chave.conta.nomeDoTitular),
    Pair("cpfDoTitular", chaveResponse.chave.conta.cpfDoTitular),
    Pair("Agencia", chaveResponse.chave.conta.agencia),
    Pair("numero", chaveResponse.chave.conta.numeroDaConta))

}
