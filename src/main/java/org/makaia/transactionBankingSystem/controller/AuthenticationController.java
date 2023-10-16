package org.makaia.transactionBankingSystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.makaia.transactionBankingSystem.dto.dtoLogin.DTOLogin;
import org.makaia.transactionBankingSystem.dto.dtoLogin.DTOToken;
import org.makaia.transactionBankingSystem.exception.ApiException;
import org.makaia.transactionBankingSystem.model.User;
import org.makaia.transactionBankingSystem.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
@Tag(name = "Controlador de autenticación del sistema.")
public class AuthenticationController {

    AuthenticationManager authenticationManager;
    TokenService tokenService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    TokenService tokenService)
    {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @Operation(summary = "Permite a los usuarios realizar su proceso de " +
            "autenticación ante el sistema utilizando un inicio de sesión con" +
            " usuario y contraseña.",
            description = "Recibe un usuario y contraseña por el cuerpo de " +
                    "la petición y retorna la información asociada a este " +
                    "usuario en la base de datos en un JWT.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Se " +
                            "encontró usuario asociado a la información " +
                            "suministrada." +
                            ".", content = @Content(schema =
                            @Schema(implementation = DTOToken.class))),
                    @ApiResponse(responseCode = "403", description = "Error " +
                            "en el ingreso de los datos o no existe una " +
                            "un usuario asociado al nombre de usuario pasado " +
                            "por parámetro.", content =
                            @Content(schema =@Schema(hidden = true))),
                    @ApiResponse(responseCode = "500", description = "Error " +
                            "al intentar crear el JWT.", content =
                    @Content(schema =@Schema(hidden = true)))
            }
    )
    @PostMapping
    public ResponseEntity<DTOToken> authenticateUser(@Parameter(description =
            "Información del usuario y contraseña con que se identifica a " +
                    "una persona para ingresar al sistema.",
            required = true)
             @RequestBody DTOLogin dtoLogin) throws ApiException
    {
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                dtoLogin.getUserName(), dtoLogin.getPassword()
        );
        var authenticatedUser = authenticationManager
                .authenticate(authToken);
        var JWTToken = tokenService.generateToken((User) authenticatedUser
                .getPrincipal());
        return ResponseEntity.ok(new DTOToken(JWTToken));
    }
}
