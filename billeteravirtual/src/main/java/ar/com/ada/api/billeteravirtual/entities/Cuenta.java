package ar.com.ada.api.billeteravirtual.entities;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * Cuenta
 */
@Entity
@Table(name = "cuenta")
public class Cuenta {

    @Id
    @Column(name = "cuenta_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cuentaId;
    private String moneda;
    private BigDecimal saldo = new BigDecimal(0);
    private BigDecimal saldoDisponible = new BigDecimal(0);

    @ManyToOne
    @JoinColumn(name = "billetera_id", referencedColumnName = "billetera_id")
    private Billetera billetera;

    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL)
    //@LazyCollection(LazyCollectionOption.FALSE)
    private List<Movimiento> movimientos = new ArrayList<Movimiento>();

    public Cuenta(int cuentaId, String moneda, BigDecimal saldo, BigDecimal saldoDisponible) {
        this.cuentaId = cuentaId;
        this.moneda = moneda;
        this.saldo = saldo;
        this.saldoDisponible = saldoDisponible;
    }

    public Cuenta() {
    }

    public void agregarMovimiento(Movimiento movimiento) {

        movimiento.setCuenta(this);
        movimientos.add(movimiento);

        this.setSaldo(this.getSaldo().add(movimiento.getImporte()));
        this.setSaldoDisponible(this.getSaldo());

    }

    public void agregarDinero(int usuarioDe, BigDecimal importe, String concepto, String detalle) {

        Movimiento m = new Movimiento();

        m.setCuenta(this);
        m.setTipoOperacion("Deposito de regalo");
        m.setImporte(importe);
        m.setConceptoOperacion(concepto);
        m.setDetalle(detalle);
        m.setFechaMovimiento(new Date());
        m.setDeUsuarioId(usuarioDe);
        m.setaUsuarioId(usuarioDe);
        m.setCuentaOrigenId(this.cuentaId);
        m.setCuentaDestinoId(this.cuentaId);

        this.movimientos.add(m);
    }

    public int getcuentaId() {
        return cuentaId;
    }

    public void setcuentaId(int cuentaId) {
        this.cuentaId = cuentaId;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigDecimal getSaldoDispobible() {
        return saldoDisponible;
    }

    public void setSaldoDisponible(BigDecimal saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
    }

    public Billetera getBilletera() {
        return billetera;
    }

    public void setBilletera(Billetera billetera) {
        this.billetera = billetera;
    }

    public List<Movimiento> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<Movimiento> movimientos) {
        this.movimientos = movimientos;
    }

}