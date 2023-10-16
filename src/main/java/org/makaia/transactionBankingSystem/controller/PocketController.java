package org.makaia.transactionBankingSystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.makaia.transactionBankingSystem.dto.dtoPocket.DTOPocketConsultOut;
import org.makaia.transactionBankingSystem.dto.dtoPocket.DTOPocketCreation;
import org.makaia.transactionBankingSystem.dto.dtoPocket.DTOPocketTransfer;
import org.makaia.transactionBankingSystem.exception.ApiException;
import org.makaia.transactionBankingSystem.service.AccountService;
import org.makaia.transactionBankingSystem.service.PocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/pockets")
@Tag(name = "Controlador de bolsillos del sistema.")
public class PocketController {

    PocketService pocketService;
    AccountService accountService;

    @Autowired
    public PocketController(PocketService pocketService, AccountService accountService){
        this.pocketService = pocketService;
        this.accountService = accountService;
    }

    @Operation(hidden = true)
    @GetMapping("/{accountNumber}")
    public ResponseEntity<DTOPocketConsultOut> getPockets (HttpServletRequest request, @PathVariable Long accountNumber) throws ApiException {
        this.accountService.isLoggedInUserTheOwnerOfTheAccount(accountNumber,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(this.pocketService.getPocketsByAccountNumber(accountNumber));
    }

    @Operation(summary = "Permite a los usuarios crear un bolsillo " +
            "(subcuenta) asociado a su cuenta principal. El dinero almacenado" +
            " en el bolsillo no se encuentra disponible en la cuenta " +
            "principal.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Se creó" +
                            " bolsillo con la información ingresada.",
                            content = @Content(schema =
                            @Schema(implementation = DTOPocketCreation.class))),
                    @ApiResponse(responseCode = "400", description = "Error en el ingreso de " +
                            "los datos ya sea para número de cuenta asociada," +
                            " nombre o su saldo inicial, o el saldo " +
                            "disponible en la cuenta no es suficiente.",
                            content = @Content(schema =@Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Acceso " +
                            "no autorizado debido a que el token es inválido " +
                            "o caducó o debido a que el usuario no puede " +
                            "realizar la acción (la cuenta de origen no es de" +
                            " su propiedad).", content =
                    @Content(schema =@Schema(hidden = true)))

            }
    )
    @PostMapping
    public ResponseEntity<DTOPocketCreation> createPocket(HttpServletRequest request,
          @Parameter(description = "Información de la cuenta bancaria de " +
                  "origen el nombre del bolsillo a crear y la cantidad a " +
                  "transferir.", required = true) @RequestBody DTOPocketCreation
                  dtoPocketCreation)
            throws ApiException
    {
        this.accountService.isLoggedInUserTheOwnerOfTheAccount(dtoPocketCreation.getAccountNumber(),
                request.getUserPrincipal().getName());
        System.out.println(dtoPocketCreation.getAccountNumber());
        return ResponseEntity.ok(this.pocketService.createPocket(dtoPocketCreation));
    }

    @Operation(summary = "Permite a los usuarios transferir dinero desde la " +
            "cuenta principal a un bolsillo existente, proporcionando el " +
            "número de cuenta y bolsillo, así como la cantidad a transferir.",
            description = "Recibe el número de cuenta bancaria " +
                    "asociado a la cuenta bancaria junto con el nombre " +
                    "del bolsillo y el monto a transferir y se retorna la " +
                    "información de la transferencia realizada.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Se " +
                            "realizó la transacción." +
                            ".", content = @Content(schema =
                            @Schema(implementation = DTOPocketTransfer.class))),
                    @ApiResponse(responseCode = "400", description = "Error " +
                            "en el ingreso de los datos o no existe cuenta o " +
                            "bolsillo asociados a la información enviada por" +
                            " el cuerpo de la petición el saldo disponible de la " +
                            "cuenta de origen no es suficiente.", content =
                            @Content(schema =@Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Acceso " +
                            "no autorizado debido a que el token es inválido " +
                            "o caducó o debido a que el usuario no puede " +
                            "realizar la acción (la cuenta de origen no es de" +
                            " su propiedad).", content =
                    @Content(schema =@Schema(hidden = true)))
            }
    )
    @PostMapping("/transfer")
    public ResponseEntity<DTOPocketTransfer> transferToPocket(HttpServletRequest request,
          @Parameter(description = "Información de la cuenta bancaria de " +
            "origen, el nombre del bolsillo y la cantidad a transferir.",
              required = true) @RequestBody DTOPocketTransfer dtoPocketTransfer)
            throws ApiException
    {
        this.accountService.isLoggedInUserTheOwnerOfTheAccount(dtoPocketTransfer.getAccountNumber(),
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(this.pocketService.transferToPocket(dtoPocketTransfer));
    }
}
