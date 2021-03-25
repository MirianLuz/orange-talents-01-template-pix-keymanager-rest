package br.com.zup.edu.pix

import br.com.zup.edu.CarregaChavePixRequest
import br.com.zup.edu.KeymanagerCarregaGrpcServiceGrpc
import br.com.zup.edu.KeymanagerListaGrpcServiceGrpc
import br.com.zup.edu.ListaChavePixRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import org.slf4j.LoggerFactory
import java.util.*

@Controller("/api/v1/clientes/{clienteId}")
class CarregaChavePixController (val carregaChavePixClient: KeymanagerCarregaGrpcServiceGrpc.KeymanagerCarregaGrpcServiceBlockingStub,
                                 val listaChavePixClient: KeymanagerListaGrpcServiceGrpc.KeymanagerListaGrpcServiceBlockingStub){

    private val Logger = LoggerFactory.getLogger(this::class.java)

    @Get("/pix/{pixId}")
    fun carrega(clienteId: UUID, pixId: UUID): HttpResponse<Any> {
        Logger.info("[$clienteId] buscando uma chave pix com $pixId")

        val chaveResponse = carregaChavePixClient.carrega(CarregaChavePixRequest.newBuilder()
            .setPixId(CarregaChavePixRequest.FiltroPorPixId.newBuilder()
                .setClientId(clienteId.toString())
                .setPixId(pixId.toString())
                .build())
            .build())

        return HttpResponse.ok(DetalheChavePixResponse(chaveResponse))
    }

    @Get("/pix")
    fun lista(clienteId: UUID):HttpResponse<Any>{
        Logger.info("[$clienteId] buscando uma chave pix ")

        val pix = listaChavePixClient.lista(ListaChavePixRequest.newBuilder()
            .setClientId(clienteId.toString())
            .build())

        val chaves = pix.chavesList.map { ChavePixResponse(it) }
        return HttpResponse.ok(chaves)
    }
}