package br.com.zup.edu.pix

import br.com.zup.edu.RegistraChavePixRequest
import br.com.zup.edu.TipoChave
import br.com.zup.edu.TipoConta
import br.com.zup.edu.validation.ValidPixKey
import io.micronaut.core.annotation.Introspected
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator
import io.micronaut.validation.validator.constraints.EmailValidator
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ValidPixKey
@Introspected
class NovaChavePixRequest (@field:NotNull val tipoConta: TipoDeContaRequest?,
                           @field:Size(max = 77) val chave: String?,
                           @field:NotNull val tipoChave: TipoDeChaveRequest?){

    fun paraModeloGrpc(clienteId: UUID): RegistraChavePixRequest{
        return RegistraChavePixRequest.newBuilder()
            .setClientId(clienteId.toString())
            .setTipoConta(tipoConta?.atributoGrpc ?: TipoConta.UNKNOW_ACCOUNT_TYPE)
            .setTipoChave(tipoChave?.atributoGrpc ?: TipoChave.UNKNOW_KEY_TYPE)
            .setChave(chave ?: "")
            .build()
    }

}

enum class TipoDeChaveRequest(val atributoGrpc: TipoChave) {
    CPF(TipoChave.CPF){
        override fun valida(chave: String?): Boolean{
            if(chave.isNullOrBlank()){
                return false
            }

            if(!chave.matches("[0-9]+".toRegex())){
                return false
            }

            return CPFValidator().run{
                initialize(null)
                isValid(chave, null)
            }
        }
    },
    CELULAR(TipoChave.CELULAR){
        override fun valida(chave: String?): Boolean {
            if(chave.isNullOrBlank()){
                return false
            }
            return chave.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
        }
    },
    EMAIL(TipoChave.EMAIL){
        override fun valida(chave: String?): Boolean {
            if(chave.isNullOrBlank()){
                return false
            }
            return EmailValidator().run {
                initialize(null)
                isValid(chave, null)
            }
        }
    },
    CHAVE_ALEATORIA(TipoChave.ALEATORIO){
        override fun valida(chave: String?) =  chave.isNullOrBlank()
    };

    abstract fun valida(chave: String?): Boolean
}

enum class TipoDeContaRequest(val atributoGrpc: TipoConta) {
    CONTA_CORRENTE(TipoConta.CONTA_CORRENTE),
    CONTA_POUPANCA(TipoConta.CONTA_POUPANCA)
}