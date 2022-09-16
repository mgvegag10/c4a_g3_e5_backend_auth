# c4a_g3_e5_backend_auth
Backend logica autentificacion (ENTREGA SPRINT 2)

Pasos para ejectuar el Sprint:
  1- Clonar el repositorio en la rama main.
  
  2-En el directorio "Resourses" abrir "application.propierties" y modificar la conexión a mongo si se desea trabajar con una base de datos propia.
    De forma que: 
            spring.data.mongodb.uri=mongodb+srv://<username>:<password>@cluster0.os5d5p2.mongodb.net/?retryWrites=true&w=majority
            spring.data.mongodb.database=<database_name>
            server.error.include-message=always
            
  3- Realizar pruebas de peticiones HTTP con Postman
