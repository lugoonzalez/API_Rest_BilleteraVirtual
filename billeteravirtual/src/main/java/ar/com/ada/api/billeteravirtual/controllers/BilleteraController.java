package ar.com.ada.api.billeteravirtual.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ar.com.ada.api.billeteravirtual.entities.Billetera;
import ar.com.ada.api.billeteravirtual.entities.Movimiento;
import ar.com.ada.api.billeteravirtual.entities.Persona;
import ar.com.ada.api.billeteravirtual.models.request.DepositoRequest;
import ar.com.ada.api.billeteravirtual.models.request.TransferenciaRequest;
import ar.com.ada.api.billeteravirtual.models.response.PostResponse;
import ar.com.ada.api.billeteravirtual.models.response.SaldoResponse;
import ar.com.ada.api.billeteravirtual.services.BilleteraService;
import ar.com.ada.api.billeteravirtual.services.MovimientoService;

/**
 * BilleteraController
 */
@RestController
public class BilleteraController {

    @Autowired
    BilleteraService billeteraService;

    @Autowired
    MovimientoService movimientoService;

    @GetMapping("/billeteras")
    public List<Billetera> getBilleteras() {
        List<Billetera> lb = billeteraService.getBilleteras();

        return lb;
    }

    @GetMapping("/billeteras/{id}")
    public Billetera getBilleteraById(@PathVariable int id) {
        Billetera b = billeteraService.buscarBilleteraPorId(id);

        return b;
    }

    @GetMapping("/movimientos")
    public List<Movimiento> getMovimientos() {
        List<Movimiento> lm = movimientoService.getMovimientos();

        return lm;
    }

    @GetMapping("/movimientos/{id}")
    public Movimiento getMovimientoById(@PathVariable int id) {
        Movimiento m = movimientoService.buscarMovimientoPorId(id);

        return m;
    }

    @GetMapping("/billeteras/{id}/saldos")
    public SaldoResponse getSaldoBilletera(@PathVariable int id)
    {
        SaldoResponse s = new SaldoResponse();
        s.idBilletera = id;
        s.saldo = billeteraService.buscarBilleteraPorId(id).cuentaPrincipal().getSaldo();
        s.moneda = billeteraService.buscarBilleteraPorId(id).cuentaPrincipal().getMoneda();
        return s;
       
   }

    @PostMapping("billeteras/{id}/transferencias")
    public PostResponse postNewTransferencia(@PathVariable int id, @RequestBody TransferenciaRequest req) {

        PostResponse r = new PostResponse();
        Billetera bOrigen = billeteraService.buscarBilleteraPorId(id);
        billeteraService.transferencia(bOrigen, req.emailDestino, req.importe, req.moneda, req.concepto, req.detalle);

        r.isOk = true;
        r.message = "Transferencia realizada con éxito";
        return r;
    }

    //revisar
    @PostMapping("billeteras/{id}/depositos")
    public PostResponse postNewDeposito(@PathVariable int id, @RequestBody DepositoRequest req, Persona p){

        PostResponse r = new PostResponse();
        Billetera bOrigen = billeteraService.buscarBilleteraPorId(id);
        billeteraService.deposito(p, bOrigen, req.importe, req.conceptoOperacion, req.tipoOperacion, req.detalle);

        r.isOk = true;
        r.message = "Transferencia realizada con éxito";
        return r;

    }

}