FROM tomcat:8-jre8-alpine

# Borrar aplicaciones por defecto de Tomcat que no se necesitan
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiar el archivo .war al directorio webapps de Tomcat
COPY target/API-MH-1.0.0.RELEASE.war /usr/local/tomcat/webapps/ROOT.war

# Exponer el puerto por defecto de Tomcat
EXPOSE 8080

# Tomcat inicia automáticamente cuando se arranca el contenedor
CMD ["catalina.sh", "run"]
