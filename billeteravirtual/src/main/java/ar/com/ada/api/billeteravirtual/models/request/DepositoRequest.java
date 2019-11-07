package ar.com.ada.api.billeteravirtual.models.request;

import java.math.BigDecimal;

/**
 * DepositoRequest
 */
public class DepositoRequest {

    public BigDecimal importe;
    public String tipoOperacion;
    public String conceptoOperacion;
    public String detalle; 
}