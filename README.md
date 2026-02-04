Optimus Desktop

Descripción
-----------
Optimus Desktop es una aplicación de escritorio desarrollada en Java 21 y JavaFX.
La aplicación se distribuye como una App Image para Windows, completamente autocontenida.
El usuario final no necesita tener Java ni JavaFX instalados para ejecutarla.

Requisitos para ejecutar (usuario final)
-----------------------------------------
- Sistema operativo: Windows 10 o Windows 11 (64 bits)
- No requiere Java instalado
- No requiere Maven
- No requiere permisos de administrador

Ejecución
---------
1. Copiar la carpeta entregada (por ejemplo: Optimus)
2. No modificar ni eliminar archivos internos
3. Ejecutar el archivo:
   Optimus.exe

Nota:
Windows puede mostrar un aviso de seguridad (SmartScreen).
Seleccionar "Más información" y luego "Ejecutar de todas formas".

Requisitos para compilar (desarrollador)
----------------------------------------
- Windows 10 o 11
- JDK 21 (LTS)
- Maven 3.8 o superior
- Variable de entorno JAVA_HOME configurada
- jlink y jpackage disponibles (incluidos en el JDK)

Configuración previa
--------------------
Antes de compilar, verificar JAVA_HOME:

PowerShell:

$env:JAVA_HOME="C:\Program Files\Java\jdk-21.0.10"

$env:Path="$env:JAVA_HOME\bin;$env:Path"


Verificación:
java -version
jlink --version
jpackage --help

Compilación
-----------
Paso 1: Compilación base
Genera el JAR ejecutable, copia JavaFX y crea el runtime de Java.

mvn -DskipTests=true clean package

Paso 2: Generar App Image para Windows
Crea la aplicación portable lista para distribución.

mvn -DskipTests=true -Pwindows-app-image package

Resultado de la compilación
---------------------------
El resultado se genera en:

target\installer\Optimus\

Esta carpeta contiene todo lo necesario para ejecutar la aplicación.
Debe entregarse completa al usuario final.

Distribución recomendada
------------------------
- Comprimir la carpeta Optimus en un archivo ZIP
- El usuario solo debe descomprimir y ejecutar Optimus.exe

Notas importantes
-----------------
- No ejecutar el .exe fuera de su carpeta
- No eliminar archivos internos
- La aplicación es portable y autocontenida
- Compatible solo con Windows 64 bits

Licencia
--------
Propiedad de Yobel.
Uso y distribución sujetos a las políticas del proyecto.

Soporte
-------
Para soporte o incidencias, contactar con el equipo de desarrollo de Optimus Desktop.