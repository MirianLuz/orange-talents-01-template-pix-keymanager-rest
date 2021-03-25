package br.com.zup.edu.pix

import br.com.zup.edu.KeymanagerRemoveGrpcServiceGrpc
import br.com.zup.edu.RemoveChavePixRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import org.slf4j.LoggerFactory
import java.util.*


@Controller("/api/v1/clientes/{clienteId}")
class RemoveChavePixController(private val removeChavePixClient
: KeymanagerRemoveGrpcServiceGrpc.KeymanagerRemoveGrpcServiceBlockingStub) {

    private val Logger = LoggerFactory.getLogger(this::class.java)

    @Delete("/pix/{pixId}")
    fun delete(clienteId: UUID, pixId: UUID): HttpResponse<Any>{
        Logger.info("[$clienteId] removendo uma chave pix com $pixId")

    removeChavePixClient.remove(RemoveChavePixRequest.newBuilder()
        .setClientId(clienteId.toString())
        .setPixId(pixId.toString())
        .build())

        return HttpResponse.ok()
    }
}