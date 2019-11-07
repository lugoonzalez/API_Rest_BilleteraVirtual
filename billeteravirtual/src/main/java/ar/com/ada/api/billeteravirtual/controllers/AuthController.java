package ar.com.ada.api.billeteravirtual.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ar.com.ada.api.billeteravirtual.entities.Persona;
import ar.com.ada.api.billeteravirtual.excepciones.PersonaEdadException;
import ar.com.ada.api.billeteravirtual.models.request.LoginRequest;
import ar.com.ada.api.billeteravirtual.models.request.RegistrationRequest;
import ar.com.ada.api.billeteravirtual.models.response.JwtResponse;
import ar.com.ada.api.billeteravirtual.models.response.PostResponse;
import ar.com.ada.api.billeteravirtual.security.jwt.JWTTokenUtil;
import ar.com.ada.api.billeteravirtual.services.BilleteraService;
import ar.com.ada.api.billeteravirtual.services.JWTUserDetailsService;
import ar.com.ada.api.billeteravirtual.services.MovimientoService;
import ar.com.ada.api.billeteravirtual.services.UsuarioService;;

/**
 * AuthController
 */
@RestController
public class AuthController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    BilleteraService billeteraService;

    @Autowired
    MovimientoService movimientoService;

    @Autowired
    private JWTTokenUtil jwtTokenUtil;

    @Autowired
    private JWTUserDetailsService userDetailsService;

    @PostMapping("auth/register")
    public PostResponse postRegisterUser(@RequestBody RegistrationRequest req) throws PersonaEdadException{
        PostResponse r = new PostResponse();

        Persona p = new Persona();
        p.setNombre(req.nombre);
        p.setEdad(req.edad);
        p.setDni(req.dni);
        p.setEmail(req.email);
        
        usuarioService.save(p, req.password);

        billeteraService.crearBilletera(p);

        billeteraService.depositoBienvenida(p);

        r.isOk = true;
        r.message = "Te registraste con éxito";
        return r;

    }

    @PostMapping("auth/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest)
            throws Exception {

        usuarioService.login(authenticationRequest.username, authenticationRequest.password);

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.username);

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));

    }

    
}