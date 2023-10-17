package org.makaia.transactionBankingSystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.makaia.transactionBankingSystem.dto.dtoAccount.DTOAccountCreation;
import org.makaia.transactionBankingSystem.dto.dtoAccount.DTOAccountDeposit;
import org.makaia.transactionBankingSystem.dto.dtoAccount.DTOAccountTransfer;
import org.makaia.transactionBankingSystem.dto.dtoPocket.DTOPocketConsultIn;
import org.makaia.transactionBankingSystem.dto.dtoPocket.DTOPocketConsultOut;
import org.makaia.transactionBankingSystem.exception.ApiException;
import org.makaia.transactionBankingSystem.model.Account;
import org.makaia.transactionBankingSystem.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Controlador de cuentas del sistema.")
public class AccountController {

    AccountService accountService;
    @Autowired
    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @Operation(summary = "Permite a los usuarios consultar los detalles y el " +
            "saldo actual de una cuenta específica en el sistema de " +
            "transacciones bancarias.",
            description = "Recibe un número de cuenta por la url y se retorna" +
                    " la información de la cuenta asociada a este.",
            responses = {
                    @ApiResponse(responseCode = "200", description =
                            "Cuenta bancaria asociada al número de cuenta  " +
                                    "ingresado hallada exitosamente.",
                            content = @Content(schema =
                            @Schema(implementation = Account.class))),
                    @ApiResponse(responseCode = "400", description =
                            "Posibles errores:\n- Error en el ingreso de " +
                            "los datos.\n- No existe una cuenta asociada al " +
                            "número de cuenta pasado por parámetro.", content =
                            @Content(schema =@Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description =
                            "Posibles errores:\n- Error en el ingreso de los " +
                                    "datos.\n- No existe un usuario asociado " +
                                    "a los datos pasados por parámetro.",
                            content = @Content(schema =@Schema(hidden = true)))
            }
    )
    @Parameter(name = "id",description = "Número de cuenta de 15 dígitos.")
    @GetMapping ("/{id}")
    public ResponseEntity<Account> getAccount (HttpServletRequest request,
                                               @PathVariable Long id) throws ApiException {
        this.accountService.isLoggedInUserTheOwnerOfTheAccount(id,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(this.accountService.getAccountById(id));
    }

    @Operation(summary = "Permite a los usuarios abrir una nueva cuenta bancaria" +
            " proporcionando su información y el saldo inicial.",
            description = "Recibe la información del propietario y su saldo " +
                    "inicial por el cuerpo de la petición y se " +
                    "retorna la información de la cuenta creada.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cuenta " +
                            "bancaria creada exitosamente con la información " +
                            "suministrada.",
                            content = @Content(schema =
                            @Schema(implementation = Account.class))),
                    @ApiResponse(responseCode = "400", description = "Error " +
                            "en el ingreso de los datos.",
                            content = @Content(schema =@Schema(hidden = true))),
            }
    )
    @PostMapping
    public ResponseEntity<Account> createAccount(@Parameter(description =
            "Información del propietario y saldo inicial de la cuenta " +
                    "bancaria a crear.",
            required = true) @RequestBody DTOAccountCreation dtoAccountCreation)
            throws ApiException
    {
        return ResponseEntity.ok(this.accountService.createAccount(dtoAccountCreation));
    }

    @Operation(summary = "Permite a los usuarios realizar un depósito en una " +
            "cuenta existente proporcionando el número de cuenta y la " +
            "cantidad a depositar.",
            description = "Recibe un número de cuenta por la url y una " +
                    "cantidad a depositar a través del cuerpo de la petición " +
                    " y se retorna la información de la cuenta asociada a " +
                    "después de hacer el depósito.",
            responses = {
                    @ApiResponse(responseCode = "200", description =
                            "Depósito a cuenta bancaria realizado " +
                                    "exitosamente con la información " +
                                    "suministrada.",
                            content = @Content(schema =
                            @Schema(implementation = Account.class))),
                    @ApiResponse(responseCode = "400", description =
                            "Posibles errores:\n- Error en el ingreso de " +
                            "los datos.\n- No existe una cuenta asociada al " +
                            "número de cuenta pasado por parámetro.", content =
                            @Content(schema =@Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description =
                            "Posibles errores:\n- Acceso no autorizado debido" +
                                    " a que el token es inválido o caducó" +
                                    ".\n- Acceso no autorizado debido a que " +
                                    "el usuario no tiene autorización para " +
                                    "realizar la acción.", content =
                            @Content(schema =@Schema(hidden = true)))
            }
    )
    @Parameter(name = "account_number",description = "Número de cuenta de 15 dígitos.")
    @PostMapping("/{account_number}")
    public ResponseEntity<Account> depositAccount(@PathVariable Long account_number,
              @Parameter(description = "Información de la cantidad a " +
                      "depositar en la cuenta bancaria.", required = true)
              @RequestBody DTOAccountDeposit dtoAccountDeposit) throws ApiException
    {
        return ResponseEntity.ok(this.accountService.depositInAccount(account_number, dtoAccountDeposit));
    }

    @Operation(summary = "Permite a los usuarios transferir dinero de una " +
            "cuenta a otra, proporcionando los números de cuenta de origen y " +
            "destino, así como la cantidad a transferir.",
            description = "Recibe los números de cuenta asociados a las " +
                    "cuentas de origen y de destino de la transferencia junto" +
                    "con el monto de esta y se retorna la información de la " +
                    "transferencia realizada.",
            responses = {
                    @ApiResponse(responseCode = "200", description =
                            "Transferencia realizada exitosamente con la " +
                                    "información suministrada.", content =
                            @Content(schema = @Schema(implementation =
                                    DTOAccountTransfer.class))),
                    @ApiResponse(responseCode = "400", description =
                            "Posibles errores:\n- Error en el ingreso de " +
                            "los datos.\n- No existe(n) cuenta(s) asociada al" +
                            "(los) número(s) de cuenta pasado(s) por el " +
                            "cuerpo de la petición.\n- Las cuentas de origen " +
                                    "y destino de la transferencia son " +
                                    "iguales.\n- El saldo disponible de la " +
                                    "cuenta de origen no es suficiente.", content =
                            @Content(schema =@Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description =
                            "Posibles errores:\n- Acceso no autorizado debido" +
                                    " a que el token es inválido o caducó" +
                                    ".\n- Acceso no autorizado debido a que " +
                                    "el usuario no tiene autorización para " +
                                    "realizar la acción.", content =
                            @Content(schema =@Schema(hidden = true)))
            }
    )
    @PostMapping("/transfer")
    public ResponseEntity<DTOAccountTransfer> transferAccount(HttpServletRequest request,
              @Parameter(description = "Información de las cuentas bancarias " +
                      "de origen y destino y la cantidad a transferir.",
                      required = true)
              @RequestBody DTOAccountTransfer dtoAccountTransfer) throws ApiException
    {
        this.accountService.isLoggedInUserTheOwnerOfTheAccount(dtoAccountTransfer
                        .getSourceAccountNumber(), request.getUserPrincipal()
                        .getName());
        return ResponseEntity.ok(this.accountService.transferToAccount(dtoAccountTransfer));
    }

    @Operation(summary = "Permite a los usuarios obtener una lista de los " +
            "bolsillos asociados a una cuenta específica",
            description = "Recibe un número de cuenta por la url de la " +
                    "petición y se retorna una lista de bolsillos " +
                    "pertenecientes a la cuenta asociada.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Obtenci" +
                            "ón exitosa de la lista de bolsillos asociados a" +
                            " la cuenta pasada por parámetro.",
                            content = @Content(schema =
                        @Schema(implementation = DTOPocketConsultOut.class))),
                    @ApiResponse(responseCode = "400", description =
                            "Posibles errores:\n- Error " +
                            "en el ingreso de los datos.\n- No existe una " +
                            "cuenta asociada al número de cuenta pasado por " +
                            "parámetro.\n- La cuenta bancaria no cuenta con " +
                            "bolsillos.", content =
                            @Content(schema =@Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description =
                            "Posibles errores:\n- Acceso no autorizado debido" +
                                    " a que el token es inválido o caducó" +
                                    ".\n- Acceso no autorizado debido a que " +
                                    "el usuario no tiene autorización para " +
                                    "realizar la acción.", content =
                            @Content(schema =@Schema(hidden = true)))
            }
    )
    @Parameter(name = "accountNumber",description = "Número de cuenta de 15 dígitos.")
    @GetMapping("/{accountNumber}/pockets")
    public ResponseEntity<List<DTOPocketConsultIn>> getPockets (HttpServletRequest request,
                @PathVariable Long accountNumber) throws ApiException
    {
        this.accountService.isLoggedInUserTheOwnerOfTheAccount(accountNumber,
                request.getUserPrincipal().getName());
        return ResponseEntity.ok(this.accountService.getPocketsByAccountNumber(accountNumber));
    }
}
