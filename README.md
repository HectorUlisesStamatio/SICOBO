# SICOBO
 Una aplicación web encargada de la gestión de bodegas.

<h1>Tecnologías</h1>
<div align="center">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/html5/html5-original.svg" height="40" width="52" alt="html5 logo"  />
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/javascript/javascript-original.svg" height="40" width="52" alt="javascript logo"  />
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/css3/css3-original.svg" height="40" width="52" alt="css3 logo"  />
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/sass/sass-original.svg" height="40" width="52" alt="sass logo"  />
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/bootstrap/bootstrap-original.svg" height="40" width="52" alt="bootstrap logo"  />
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" height="40" width="52" alt="java logo"  />
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" height="40" width="52" alt="spring logo"  />
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/mysql/mysql-original.svg" height="40" width="52" alt="mysql logo"  />
</div>

###

<h1>✔️Características</h1>

- [X] 🔑Control de Acceso
    - [X] Inicio de Sesión - [@Ulises](https://www.github.com/HectorUlisesStamatio) 
    - [X] AutoRegistro - [@Ulises](https://www.github.com/HectorUlisesStamatio) 
- [X] 👤Perfiles
    - [X] Modificación de Perfil - [@Nathaly](https://www.github.com/20203tn082)
    - [X] Recuperar Contraseña - [@Nahum](https://www.github.com/GaticaNahum)

- [X] 🏫Sitios
    - [X] Consulta de sitios - [@Omar](https://www.github.com/20203tn096)
    - [X] Registro de sitio - [@Omar](https://www.github.com/20203tn096)
    - [X] Modificación de sitio - [@Omar](https://www.github.com/20203tn096)
    - [X] Cambiar estado del sitio - [@Omar](https://www.github.com/20203tn096)
- [X] 👷Gestores
    - [X] Consulta de gestores - [@Nathaly](https://www.github.com/20203tn082)
    - [X] Registro de gestores - [@Nathaly](https://www.github.com/20203tn082)
    - [X] Modificación de gestor - [@Nathaly](https://www.github.com/20203tn082)
    - [X] Cambiar estado del gestor - [@Nathaly](https://www.github.com/20203tn082)
    - [X] Asignación de gestor a bodega - [@Nathaly](https://www.github.com/20203tn082)
    - [X] Desasignación de gestor a bodega - [@Nathaly](https://www.github.com/20203tn082)
- [ ] 🗃️Bodegas
    - [X] Consulta de bodegas por sitio - [@Diego](https://www.github.com/DevDiegoVillalobos)
    - [X] Registro de bodega - [@Diego](https://www.github.com/DevDiegoVillalobos)
    - [X] Modificación de bodega - [@Diego](https://www.github.com/DevDiegoVillalobos)
    - [X] Cambiar estado de bodega - [@Diego](https://www.github.com/DevDiegoVillalobos)
    - [X] Consulta general de bodegas - [@Nathaly](https://www.github.com/20203tn082)
    - [X] Desalojar bodega - [@Diego](https://www.github.com/DevDiegoVillalobos)
    - [ ] Consultar bodegas rentadas y disponibles - [@Nahum](https://www.github.com/GaticaNahum)
    - [ ] Consultar bodegas por cliente - [@Nahum](https://www.github.com/GaticaNahum)

- [ ] 💵Pagos
    - [ ] Consultar resumen de pagos por bodega - [@Nahum](https://www.github.com/GaticaNahum)
    - [ ] Pago de bodega - [@Omar](https://www.github.com/20203tn096)
    - [ ] Rentar bodega - [@Omar](https://www.github.com/20203tn096)
    - [ ] Notificación por renovación de pago - [@Nahum](https://www.github.com/GaticaNahum)
    - [ ] Notificación por desalojo - [@Nahum](https://www.github.com/GaticaNahum)

 - [X] ⚙️Extras
    - [X] Consulta de términos y condiciones - [@Diego](https://www.github.com/DevDiegoVillalobos)
    - [X] Modificación de términos y condiciones - [@Diego](https://www.github.com/DevDiegoVillalobos) 
    - [X] Páginas de error - [@Ulises](https://www.github.com/HectorUlisesStamatio) 
    - [X] LandingPage - [@Ulises](https://www.github.com/HectorUlisesStamatio) 
    - [X] Base del Proyecto - [@Ulises](https://www.github.com/HectorUlisesStamatio)
    - [X] Base de datos - [@Ulises](https://www.github.com/HectorUlisesStamatio) 
    
 - [X] 💰Tipos de bodegas
    - [X] Consulta de tipos de bodegas
    - [X] Modificación de tipos de bodegas

## ⚠️Commits

Se deberá respetar la nomenclatura de commits.

```bash
  [➕ADD] inicio de sesión
```

```bash
  [✏️UPDATE] inicio de sesión - validación de datos
```

```bash
  [❌FIX] inicio de sesión - corrección validación de datos
```

```bash
  [✏️UPDATE | ❌FIX] inicio de sesión - validación de datos - corrección responsividad
```

## ⌨️Desarrollo

> No es necesario crear la base de datos, esta se creará cuando corras el proyecto de manera automática.  

> Tampoco es necesario crear los constructores, getters ni setters en las entidades ni en los DTO's ya que ya los tienen gracias a la etiqueta @Data


### 🏗️Estructura de Archivos

| Archivos  | Descripción                       |
| :-------- | :-------------------------------- |
| `Bean`    | Este tipo de archivos son las entidades de nuestra base de datos por lo que si desean cambiar algo de la entidad es ahí donde debes hacer tu cambio |
| `DTO`     | Aquí es donde se hacen las validaciones de todos los datos que vamos a recibir (por lo que no es necesario mandar nada de Id ni nada de ese estilo), en caso de agregarle algún campo que se reciba al Bean, también se le debe agregar al DTO o no servirá |
| `DAO`     | Interfaz que implementa las consultas a las bases de datos, JPA ya te da unas por defecto como los findAll pero en caso de hacer una consulta muy específica a la base de datos ahí es donde deberá de crearse |
| `Service`     | Interfaz que tiene los métodos generales que usará nuestro ServiceImpl |
| `ServiceImpl`     | Encargado de realizar todo el proceso de consulta y manipulación de datos, con el objetivo de que el controller esté lo menos retacado posible |
| `Controller`     | El que tendrá los nombres de metodos y las rutas para solo mandar a llamar al ServiceImpl |

## 🚩 Consideraciones

A la hora de desarrollar su módulo solo copien, peguen y cambien de nombre el archivo llamado paginaEnBlanco.html, ya que esta posee un interfaz por defecto para que solo la adapten a lo que necesiten.

Cualquier tipo de mensaje de error lo más recomendable será que lo pongan en el messages.properties como se ve el ejemplo del Login.

Pueden crear los controladores específicos para cada módulo si así lo desean, solo cuiden que las rutas generales de su controlador vayan a juego con el tipo de usuario:


| Ruta          | Descripción                       |
| :--------     |  :-------------------------------- |
| `/usuario/**` | Para los clientes |
| `/gestor/**`  | Para los gestores |
| `/admin/**`   | Para el administrador general |

> Recuerden que todos los return serán ResponseEntity y que devolverán el objeto message como se ve en el archivo CostTypeServiceImpl.

> El archivo import.sql servirá para poner los comandos como los triggers para que inmediatamente al correr el proyecto estos se creen en la base de datos.

