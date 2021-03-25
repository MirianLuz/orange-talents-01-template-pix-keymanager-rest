package br.com.zup.edu.pix

import br.com.zup.edu.CarregaChavePixRequest
import br.com.zup.edu.KeymanagerListaGrpcServiceGrpc
import br.com.zup.edu.KeymanagerRegistraGrpcServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpResponse.uri
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*
import javax.validation.Valid

@Validated
@Controller("/api/v1/clientes/{clienteId}")
class RegistraChavePixController (private val registraChavePixClient: KeymanagerRegistraGrpcServiceGrpc.KeymanagerRegistraGrpcServiceBlockingStub){

    private val Logger = LoggerFactory.getLogger(this::class.java)

    @Post("/pix")
    fun create(clienteId: UUID,
               @Valid @Body request: NovaChavePixRequest) : HttpResponse<Any> {

        Logger.info("[$clienteId] criando uma nova chave pix com $request")
        val grpcResponse = registraChavePixClient.registra(request.paraModeloGrpc(clienteId))

        return HttpResponse
            .created(location(clienteId, grpcResponse.pixId))
    }

    private  fun location(clienteId: UUID, pixId: String) = HttpResponse
        .uri("/api/v1/clientes/$clienteId/pix/${pixId}")
}