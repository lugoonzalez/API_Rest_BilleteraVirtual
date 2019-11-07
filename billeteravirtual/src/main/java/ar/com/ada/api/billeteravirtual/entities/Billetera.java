package ar.com.ada.api.billeteravirtual.entities;

import javax.persistence.*;

import java.util.*;
import java.math.BigDecimal;

/**
 * Billetera
 */
@Entity
@Table (name = "billetera")
public class Billetera {

    @Id
    @Column(name = "billetera_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int billeteraId;

    @OneToOne
    @JoinColumn(name = "persona_id", referencedColumnName = "persona_id")
    private Persona persona;

    @OneToMany(mappedBy = "billetera", cascade = CascadeType.ALL)
    //@LazyCollection(LazyCollectionOption.FALSE)
    private List <Cuenta> cuentas = new ArrayList<Cuenta>();   

    public Billetera(int billeteraId) {
        this.billeteraId = billeteraId;
    }

    public Billetera() {
    }

    public void agregarCuenta(Cuenta cuenta) {
        cuenta.setBilletera(this);
        this.cuentas.add(cuenta);
    }

    public BigDecimal consultarSaldo() {
        return this.cuentas.get(0).getSaldo();
    }

    public Cuenta cuentaPrincipal() {
        return getCuentas().get(0);
    }

    public Usuario getUsuario() {
        return getPersona().getUsuario();
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
        this.persona.setBilletera(this);
    }

    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }

    public int getBilleteraId() {
        return billeteraId;
    }

    public void setBilleteraId(int billeteraId) {
        this.billeteraId = billeteraId;
    }

}