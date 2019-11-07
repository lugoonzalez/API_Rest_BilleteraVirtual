package ar.com.ada.api.billeteravirtual.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import ar.com.ada.api.billeteravirtual.entities.*;
import ar.com.ada.api.billeteravirtual.repo.UsuarioRepository;
import ar.com.ada.api.billeteravirtual.security.Crypto;
import ar.com.ada.api.billeteravirtual.sistema.comms.EmailService;
/**
 * UsuarioService
 */
@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository repositorio;

    @Autowired
    PersonaService personaService;
    
    @Autowired
    BilleteraService billeteraService;

    @Autowired
    EmailService emailService;

    public List<Usuario> getUsuarios() {
        
        return repositorio.findAll();
    }

    public Usuario buscarPorId(int id) {

        Optional<Usuario> u = repositorio.findById(id);
        
        if (u.isPresent())
            return u.get();
        return null;
    }

    public void save(Persona p, String password) {

        Usuario u = new Usuario();
        u.setEmail(p.getEmail());
        u.setUsername(p.getEmail());
        u.setPassword(Crypto.encrypt(password, u.getEmail()));

        p.setUsuario(u);
        personaService.save(p);

        emailService.SendEmail(u.getUsername(),"Bienvenido a la Billetera Virtual ADA!!!", 
            "Hola "+p.getNombre()+"\nBienvenido a este hermoso proyecto hecho por todas las alumnas de ADA Backend 8va Mañana\n"+
            "Además te regalamos 100 pesitos de bienvenida");
    }

    public Usuario buscarPorEmail(String email) {
    
        return repositorio.findByEmail(email);
    }

    public Usuario buscarPorUsername(String username){
        return repositorio.findByUsername(username);
    }

    public void login(String username, String password) {

        Usuario u = repositorio.findByUsername(username);

        if (u == null || !u.getPassword().equals(Crypto.encrypt(password, u.getUsername()))) {

            throw new BadCredentialsException("Usuario o contraseña invalida");
        }
    }
    
}