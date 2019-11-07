package ar.com.ada.api.billeteravirtual.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

import ar.com.ada.api.billeteravirtual.entities.*;
import ar.com.ada.api.billeteravirtual.repo.*;

/**
 * MovimientoService
 */
@Service
public class MovimientoService {

    @Autowired
    MovimientoRepository repoMovimiento;

    @Autowired
    BilleteraRepository repoBilletera;

    public List<Movimiento> getMovimientos() {

        return repoMovimiento.findAll();
    }

    public Movimiento buscarMovimientoPorId(int id) {

        Optional<Movimiento> m = repoMovimiento.findById(id);
        
        if (m.isPresent())
            return m.get();
        return null;
    }

    public void save(Movimiento m) {
        repoMovimiento.save(m);
    }


}