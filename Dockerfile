# Usar una imagen base con JDK 21
FROM mcr.microsoft.com/openjdk/jdk:21-jdk-windowsservercore

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo JAR generado por Maven
COPY target/inventario-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto en el que corre tu aplicación
EXPOSE 8081

# Comando para iniciar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
