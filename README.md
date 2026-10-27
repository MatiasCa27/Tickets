# Tickets MPAZ

## Descripción del proyecto

Tickets MPAZ es una aplicación móvil desarrollada en Android Studio para la Escuela Escritora Marcela Paz.

Su objetivo es proporcionar una plataforma sencilla para el registro e inicio de sesión de funcionarios, la edición de datos de usuario y la gestión local de documentos PDF y Word.

El proyecto corresponde a la Evaluación Nota 2 de la asignatura Desarrollo de Aplicaciones Móviles de Duoc UC.

## Integrantes

- Matías Cabezas
- José Lobos

## Tecnologías utilizadas

- **Lenguaje:** Kotlin.
- **Entorno de desarrollo:** Android Studio.
- **Interfaz gráfica:** Jetpack Compose y Material 3.
- **Base de datos:** Room SQLite.
- **Navegación:** Activities, Intent, TopAppBar y BottomAppBar.
- **Almacenamiento de documentos:** almacenamiento local de Android.
- **Recursos nativos:** selector de archivos de Android y FileProvider.
- **Animaciones:** transiciones de deslizamiento mediante archivos XML.

## Funcionalidades implementadas

### 1. Pantalla de bienvenida

- Presentación inicial de la aplicación.
- Acceso a registro de usuarios.
- Acceso a inicio de sesión.

### 2. Registro de usuarios

- Formulario para registrar funcionarios.
- Validación de campos obligatorios.
- Validación de correo electrónico.
- Validación y confirmación de contraseña.
- Almacenamiento local mediante Room SQLite.
- Protección de contraseñas mediante hash y sal.

### 3. Inicio de sesión

- Acceso mediante correo electrónico y contraseña.
- Verificación de credenciales.
- Consulta de usuarios registrados en Room.
- Acceso a la pantalla principal.

### 4. Pantalla principal

- Mensaje de bienvenida al usuario.
- Menú de navegación superior con TopAppBar.
- Barra de navegación inferior con BottomAppBar.
- Acceso a edición de perfil y documentos.
- Opción para cerrar sesión.

### 5. Edición de datos de usuario

- Visualización de los datos del funcionario.
- Modificación de nombre y cargo.
- Validación de campos.
- Actualización de información en Room SQLite.

### 6. Gestión de documentos

- Selección de documentos PDF, DOC y DOCX.
- Guardado de documentos en el almacenamiento local.
- Visualización de documentos guardados.
- Apertura de archivos mediante aplicaciones compatibles.
- Eliminación de documentos con confirmación.

### 7. Animaciones

- Animaciones de transición entre actividades.
- Efectos de deslizamiento al abrir y cerrar pantallas.

## Requisitos para ejecutar

- Android Studio instalado.
- SDK de Android configurado.
- Dispositivo Android con versión 7.0 o superior.
- Git instalado para trabajar con el repositorio.

## Pasos para ejecutar el proyecto

1. Descargar o clonar el repositorio desde GitHub.
2. Abrir Android Studio.
3. Seleccionar **Open** y abrir la carpeta del proyecto Tickets.
4. Esperar a que Gradle sincronice las dependencias.
5. Conectar un dispositivo Android mediante USB o seleccionar un emulador.
6. Habilitar la depuración USB si se utiliza un dispositivo físico.
7. Seleccionar la configuración **app** en Android Studio.
8. Presionar **Run** para compilar e instalar la aplicación.
9. Abrir Tickets MPAZ en el dispositivo.

## Almacenamiento de información

La aplicación utiliza Room SQLite para almacenar los usuarios registrados.

Los documentos se guardan en el almacenamiento privado de la aplicación y pueden consultarse o eliminarse desde la pantalla de documentos.

No se requiere un servidor externo para el funcionamiento de estas características.

## Estado del proyecto

Las funcionalidades descritas se han implementado como parte de la Nota 2. Se encuentran pendientes las pruebas completas de compilación y funcionamiento en un dispositivo Android.

El proyecto utiliza datos ficticios con fines académicos.

## Desarrollo futuro

En próximas etapas se contempla incorporar la gestión de solicitudes de mantención, incluyendo la creación de tickets, seguimiento de estados y funciones específicas para administradores y responsables.
