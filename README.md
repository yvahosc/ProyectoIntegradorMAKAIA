# Sistema de transacciones bancarias
## Desarrollo del proyecto integrador dentro del desarrollo del BootCamp de desarrollo web BackEnd de MAKAIA.
### Equipo de trabajo:
* Paula Múnera - paulamunera8@gmail.com
* Yeisson Vahos - yvahosc@unal.edu.co

### El repositorio corresponde al desarrollo del proyecto integrador dentro del bootcamp de MAKAIA, en él se desarrollará la simulación de un sistema de transacciones bancaria utilizando Java, MySQL, SpringBoot, SpringData, Spring security, JWT, JUnit y Swagger.

## Introducción

### Los sistemas de transacciones bancarias desempeñan un papel fundamental al facilitar y agilizar la gestión de los movimientos de dinero entre cuentas y entidades financieras. Estos sistemas representan la columna vertebral de las operaciones bancarias, permitiendo a individuos, empresas e instituciones realizar una variedad de transacciones de manera segura, eficiente y conveniente.

## Objetivo
### El objetivo de este ejercicio es simular un sistema de transacciones bancarias básico. Los usuarios podrán abrir cuentas, realizar depósitos en sus cuentas, y transferir dinero entre cuentas.

## Features

### Requerimientos funcionales
1. Cuentas
   * Apertura de Cuentas
   * Depósitos en Cuentas
   * Transferencias entre Cuentas
   * Consultar cuenta
2. Bolsillos
   * Creación de Bolsillos
   * Transferencias a Bolsillos
   * Consulta de Bolsillos
3. Implementar Autenticación en el API REST

### Requerimientos no funcionales
1. Documentar el API

## Especificaciones

1. Cuentas
* Apertura de Cuentas

Endpoint para Abrir una Cuenta: permite a los usuarios abrir una nueva cuenta bancaria proporcionando su nombre y saldo inicial. POST -> /api/accounts

Cuerpo de la petición:
```json
{
   "owner": {
      "person": {
         "id": "1010458963",
         "firstName": "Juana",
         "lastName": "Riquelme",
         "phone": "+57-3004265987",
         "email": "jriquelme@gmail.com"
      },
      "password": "1234"
   },
   "initialBalance": 0
}
```

Respuesta 200 de la petición:
```json
{
   "accountNumber": 791534917141140,
   "balance": 10000
}
```

* Depósitos en cuentas

Endpoint para Realizar un Depósito: permite a los usuarios realizar un depósito en una cuenta existente proporcionando el número de cuenta y la cantidad a depositar. POST -> /api/accounts/{account_number}/deposit

Cuerpo de la petición:
```json
{
  "amount": 10000
}
```

Respuesta 200 de la petición:
```json
{
   "accountNumber": 791534917141140,
   "balance": 10000
}
```

* Transferencias entre cuentas

Endpoint para Realizar una Transferencia: permite a los usuarios transferir dinero de una cuenta a otra, proporcionando los números de cuenta de origen y destino, así como la cantidad a transferir. POST -> /api/accounts/transfer

Cuerpo y respuesta 200 de la petición:
```json
{
   "sourceAccountNumber": 791534917141140,
   "destinationAccountNumber": 523754106790787,
   "amount": 20000
}
```

* Consultar cuenta

Endpoint para Consultar una Cuenta: permite a los usuarios consultar los detalles y el saldo actual de una cuenta específica en el sistema de transacciones bancarias. GET -> /api/accounts/{account_number} 

Respuesta 200 de la petición:  
```json
{
  "sourceAccountNumber": 791534917141140,
  "destinationAccountNumber": 523754106790787,
  "amount": 20000
}
```

2. Bolsillos

* Creación de bolsillos

Endpoint para Crear un Bolsillo: permite a los usuarios crear un bolsillo (subcuenta) asociado a su cuenta principal. El dinero almacenado en el bolsillo se descuenta del saldo de la cuenta principal. POST -> /api/pockets

Cuerpo y respuesta 200 de la petición:
```json
{
  "accountNumber": 464783411264004,
  "name": "Vacaciones",
  "initialBalance": 1000
}
```

* Transferencias a bolsillos

Endpoint para Transferir Dinero al Bolsillo: permite a los usuarios transferir dinero desde la cuenta principal a un bolsillo existente, proporcionando el número de cuenta y bolsillo, así como la cantidad a transferir. POST -> /api/pockets/transfer

Cuerpo y respuesta 200 de la petición:
```json
{
  "accountNumber": 464783411264004,
  "pocketNumber": 1,
  "amount": 1000
}
```

* Consulta de bolsillos

Endpoint para Obtener Bolsillos de una Cuenta: permite a los usuarios obtener una lista de los bolsillos asociados a una cuenta específica. GET -> /api/accounts/{account_number}/pockets

Respuesta 200 de la petición:
```json
{
   "pockets": [
      {
         "numberPocket": 1,
         "name": "Vacaciones",
         "amount": 20000
      }
   ]
}
```

3. Login

* El proyecto cuenta con autenticación y autorización utilizando usuario y contraseña y jwt respectivamente.

Endpoint para login: permite a los ususarios acceder al sistema con su usuario (corresponde a su número de identificación) y una constraseña de cuatro dígitos creada al momento de crear la cuenta. Al ingresar correctamente se genera un Json web token con el que se identifica durante su estancia en el sistema. POST -> /api/login

Cuerpo de la petición:
```json
{
  "userName": "string",
  "password": "string"
}
```

Respuesta 200 de la petición:
```json
{
  "jwtToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMDIwNDc1NTg1IiwiaXNzIjoiTUFLQUlBIGJhbmsiLCJleHAiOjE2OTY3NzQwNzF9.9n78MwDEzekaVS_S40EHaMNd685aJ9PvAdAKzM5DUIw"
}
```

Es posible acceder sin restricciones a los endpoints de /api/login, /api/accounts/{account_number}/deposit y /api/accounts para los demás endpoints es necesario haber pasado inicialmente por el endpoint /api/login para generar el JWT y poder validar si las acciones que se desea realizar están autorizadas (se debe ser el propietario de una cuenta para poder transferir desde ella, ver su información, crear bolsillos, etc.)


4. Documentación con Open Api - Swagger y despliegue con railway

El API se encuentra desplegado utilizando el servicio de railway.app ys posible visualizar dicha documentación accediendo a [Despliegue](https://proyectointegradormakaia-production.up.railway.app/swagger-ui/index.html) y desde allí es posible además realizar pruebas a cada uno de los endpoint desarrollados.

5. Entidades

El sistema cuenta con las siguientes entidades para la persistencia de datos:

- Persona: para registrar la información de las personas propietarias de las cuentas bancarias y los bolsillos.
- Cuenta: para registrar la información correpondiente a las cuentas bancarias y sus propietarios
- Bolsillo: para registrar la información correpondiente a los bolsillos y sus cuentas bancarias asociadas
- Usuario: para registrar la información correspondiente a los usuarios y contraseñas para la autenticación de los usuarios en el sistema.

Estas entidades se relacionan mediante el siguiente diagrama entidad relación:

![Diagrama ER](https://github.com/yvahosc/ProyectoIntegradorMAKAIA/assets/97228219/436e8608-b84d-42f8-b38b-6fb0d533b0a9)
