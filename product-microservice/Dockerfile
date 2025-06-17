FROM eclipse-temurin:21-jdk

# Configurar variables de entorno
ENV LANG=en_US.UTF-8

# Instalar fuentes necesarias para JasperReports
RUN apt-get update && \
    apt-get install -y fontconfig fonts-dejavu && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Copiar el jar
COPY target/*.jar app.jar

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app.jar"]
