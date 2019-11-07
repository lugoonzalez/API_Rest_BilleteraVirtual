package ar.com.ada.api.billeteravirtual.services;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.ada.api.billeteravirtual.entities.*;
import ar.com.ada.api.billeteravirtual.repo.PersonaRepository;

/**
 * PersonaService
 */
@Service
public class PersonaService {

    @Autowired
    PersonaRepository repositorio;

    public List<Persona> getPersonas() {

        return repositorio.findAll();
    }

    public Persona buscarPorNombre(String nombre) {

        return repositorio.findByNombre(nombre);
    }
 
    
    public Persona buscarPorDni(String dni) {

        return repositorio.findByDni(dni);
    }

    public Persona buscarPorId(int id) {

        Optional<Persona> p = repositorio.findById(id);
        
        if (p.isPresent())
            return p.get();
        return null;
    }

    public void save(Persona p) {
        repositorio.save(p);
    }

    
}