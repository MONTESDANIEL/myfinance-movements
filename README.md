# 💰 MyFinance Movimientos

## 📌 Descripción

Este repositorio contiene el **microservicio de movimientos** de **MyFinance**, encargado de gestionar las transacciones financieras de los usuarios.  
Permite registrar, editar y eliminar ingresos y egresos, así como clasificarlos en categorías y asignarlos a presupuestos.

Este microservicio está desarrollado con **Spring Boot**, proporcionando una API REST segura y eficiente para manejar los movimientos financieros de los usuarios.

---

## ✨ Características Principales

- ✅ **Registro de Movimientos** – Permite registrar ingresos y egresos.
- ✅ **Edición y Eliminación** – Modificación y eliminación de movimientos financieros.
- ✅ **Asociación a Presupuestos** – Vinculación con presupuestos y metas financieras.
- ✅ **Historial de Transacciones** – Registro detallado de movimientos.
- ✅ **APIs Seguras** – Protección con autenticación basada en JWT.
- ✅ **Base de Datos SQL** – Persistencia eficiente de los movimientos.

---

## 🛠 Tecnologías Utilizadas

- **Spring Boot** – Framework para el desarrollo del backend.
- **Spring Security & JWT** – Manejo de autenticación segura.
- **Spring Data JPA** – Interacción con la base de datos.
- **MySQL** – Base de datos relacional para almacenamiento.
- **Docker** – Contenedorización del microservicio.

---

## 🚀 Instalación y Ejecución

### 📌 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

- **JDK 17 o superior**
- **Maven**
- **Docker** (opcional)
- **Base de datos MySQL**

### 📥 Clonar el Repositorio

```sh
git clone https://github.com/MONTESDANIEL/myfinance-movimientos.git
cd myfinance-movimientos
```

### 🗃️ Configurar la base de datos

```sh
Utilizar el archivo .sql del proyecto para generar la base.
```

### ⚙️ Configurar el application.properties

Ajustar el application.properties de la siguiente forma según la base de datos:

```sh
spring.datasource.url=           # Url de acceso a la base de datos.
spring.datasource.username=      # Usuario de la base de datos
spring.datasource.password=      # Contraseña de la base de datos
```

### 📦 Construir y Ejecutar el Proyecto

Para compilar y ejecutar el proyecto:

```sh
mvn clean install
mvn spring-boot:run
```

---

## 📂 Estructura del Proyecto

```sh
myfinance-movements/
│── src/main/java/com/myfinance/backend/movements/
│   ├── config/         # Configuración de seguridad
│   ├── controllers/    # Controladores REST
│   ├── entities/       # Entidades
│   ├── exceptions/     # Control de excepciones
│   ├── repositories/   # Acceso a la base de datos
│   ├── services/       # Lógica de negocio
│── src/main/java/com/myfinance/backend/movements/resources/
│   ├── application.properties      # Configuración del microservicio
│── Dockerfile          # Configuración para contenedorización
│── movements_db.sql    # Archivo de creación de la base de datos
│── README.md           # Documentación del repositorio
```

## 📜 Licencia

Este proyecto está bajo la licencia MIT, por lo que puedes usarlo y modificarlo libremente.

## ⛓️Relacionado

🔗 Repositorio Principal: [MyFinance](https://github.com/MONTESDANIEL/myfinance)
