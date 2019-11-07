package ar.com.ada.api.billeteravirtual.services;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.ada.api.billeteravirtual.entities.*;
import ar.com.ada.api.billeteravirtual.repo.BilleteraRepository;
import ar.com.ada.api.billeteravirtual.repo.MovimientoRepository;
import ar.com.ada.api.billeteravirtual.sistema.comms.EmailService;

/**
 * BilleteraService
 */
@Service
public class BilleteraService {

    @Autowired
    BilleteraRepository repoBilletera;

    @Autowired
    MovimientoRepository repoMovimiento;

    @Autowired
    PersonaService personaService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    EmailService emailService;

    public List<Billetera> getBilleteras() {

        return repoBilletera.findAll();
    }

    public void save(Billetera b) {
        this.repoBilletera.save(b);
    }

    public Billetera buscarBilleteraPorId(int id) {

        Optional<Billetera> b = repoBilletera.findById(id);

        if (b.isPresent())
            return b.get();
        return null;
    }

    public void crearBilletera(Persona p) {

        Billetera b = new Billetera();
        b.setPersona(p);

        Cuenta c = new Cuenta();
        c.setMoneda("ARS");
        b.agregarCuenta(c);

        repoBilletera.save(b);
    }

    public void depositoBienvenida(Persona p) {

        Movimiento m = new Movimiento();
        m.setImporte(new BigDecimal(200.00));
        m.setConceptoOperacion("Bienvenida");
        m.setFechaMovimiento(new Date());
        m.setTipoOperacion("Deposito");
        m.setEstado(1);
        m.setDetalle("$200 de regalo");
        m.setDeUsuarioId(p.getUsuario().getUsuarioId());
        m.setaUsuarioId(p.getUsuario().getUsuarioId());
        m.setCuentaOrigenId(p.getBilletera().cuentaPrincipal().getcuentaId());
        m.setCuentaDestinoId(p.getBilletera().cuentaPrincipal().getcuentaId());

        p.getBilletera().cuentaPrincipal().agregarMovimiento(m);

        repoBilletera.save(p.getBilletera());
    }

    public void transferencia(Billetera bOrigen, String emailDestino, BigDecimal importe, String moneda, String concepto,
            String detalle) {

        Usuario u = usuarioService.buscarPorEmail(emailDestino);

        Movimiento transfiere = new Movimiento();
        transfiere.setImporte(importe.negate());
        transfiere.setConceptoOperacion(concepto);
        transfiere.setFechaMovimiento(new Date());
        transfiere.setTipoOperacion("Transferencia");
        transfiere.setEstado(1);
        transfiere.setDetalle(detalle);
        transfiere.setDeUsuarioId(bOrigen.getUsuario().getUsuarioId());
        transfiere.setaUsuarioId(u.getUsuarioId());
        transfiere.setCuentaOrigenId(bOrigen.getPersona().getBilletera().cuentaPrincipal().getcuentaId());
        transfiere.setCuentaDestinoId(u.getPersona().getBilletera().cuentaPrincipal().getcuentaId());

        bOrigen.cuentaPrincipal().agregarMovimiento(transfiere);

        Movimiento recibe = new Movimiento();
        recibe.setImporte(importe);
        recibe.setConceptoOperacion(concepto);
        recibe.setFechaMovimiento(new Date());
        recibe.setTipoOperacion("Transferencia");
        recibe.setEstado(2);
        recibe.setDetalle(detalle);
        recibe.setDeUsuarioId(bOrigen.getUsuario().getUsuarioId());
        recibe.setaUsuarioId(u.getUsuarioId());
        recibe.setCuentaOrigenId(bOrigen.getPersona().getBilletera().cuentaPrincipal().getcuentaId());
        recibe.setCuentaDestinoId(u.getPersona().getBilletera().cuentaPrincipal().getcuentaId());

        u.getPersona().getBilletera().cuentaPrincipal().agregarMovimiento(recibe);

        repoBilletera.save(bOrigen);
        repoBilletera.save(u.getPersona().getBilletera());

        emailService.SendEmail(bOrigen.getUsuario().getUsername(),"Transferencia realizada con éxito por Billetera Virtual ADA", 
            "Hola "+bOrigen.getPersona().getNombre()+"\nla transferencia de $"+importe+" realizada a "+u.getUsername()+" ya está hecha."+"\n¡Gracias por usar nuestros servicios!");

        emailService.SendEmail(u.getUsername(),"Recibiste una transferencia por Billetera Virtual ADA", 
            "Hola "+u.getPersona().getNombre()+"\nte llegó una transferencia de $"+importe+" por parte de "+bOrigen.getUsuario().getUsername()+"\n¡Gracias por usar nuestros servicios!");
    }

    public void deposito(Persona p, Billetera b, BigDecimal importe, String conceptoOperacion, String tipoOperacion, String detalle) {
    
        Movimiento m = new Movimiento();
        m.setImporte(importe);
        m.setConceptoOperacion(conceptoOperacion);
        m.setFechaMovimiento(new Date());
        m.setTipoOperacion(tipoOperacion);
        m.setEstado(1);
        m.setDetalle(detalle);
        m.setDeUsuarioId(p.getUsuario().getUsuarioId());
        m.setaUsuarioId(p.getUsuario().getUsuarioId());
        m.setCuentaOrigenId(p.getBilletera().cuentaPrincipal().getcuentaId());
        m.setCuentaDestinoId(p.getBilletera().cuentaPrincipal().getcuentaId());

        p.getBilletera().cuentaPrincipal().agregarMovimiento(m);

        repoBilletera.save(p.getBilletera());

        emailService.SendEmail(p.getUsuario().getUsername(),"Depósito realizado con éxito en Billetera Virtual ADA", 
            "Hola "+p.getNombre()+"\nEl depósito de $"+importe+" ya está en la billetera."+
            "\n¡Gracias por usar nuestros servicios!");
    }

    public BigDecimal consultarSaldo(Integer billeteraId) {
        Billetera b = repoBilletera.findById(billeteraId).get();
        return b.cuentaPrincipal().getSaldo();
    }
}