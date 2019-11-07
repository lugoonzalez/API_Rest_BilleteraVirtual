package ar.com.ada.api.billeteravirtual.models.request;

import java.math.BigDecimal;

/**
 * TransferenciaRequest
 */
public class TransferenciaRequest {

    public String emailDestino;
    public BigDecimal importe;
    public String moneda;
    public String concepto;
    public String detalle;

}