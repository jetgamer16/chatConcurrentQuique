# Concurrent Chat Quique ---- Jose Enrique 2024

Este proyecto implementa un servidor de chat en Java que permite la comunicación entre múltiples clientes. El servidor utiliza sockets para gestionar las conexiones y permite a los clientes intercambiar mensajes en un entorno de chat concurrente.

## Instrucciones de Compilación y Ejecución

1. **Compilación:**
   - Abre una terminal en el directorio donde se encuentran los archivos (`ChatServer.java`, `ChatClientHandler.java`, y `ChatClient.java`).
   - Compila los archivos usando el siguiente comando:

     ```bash
     javac ChatServer.java ChatClientHandler.java ChatClient.java
     ```

2. **Ejecución del Servidor:**
   - Inicia el servidor ejecutando main

   - El servidor se iniciará y mostrará un mensaje indicando que está escuchando en un puerto específico.

3. **Conexión de Clientes:**
   - Puedes conectar clientes utilizando el comando `telnet` en una terminal. Por ejemplo:

     ```bash
     telnet localhost <puerto>
     ```

     Reemplaza `<puerto>` con el puerto en el que está escuchando el servidor. (En este caso en main es 21010)

4. **Desconexión del Servidor:**
   - Para cerrar el servidor de manera ordenada, simplemente presiona `Ctrl + C` en la terminal donde se está ejecutando el servidor.

## Arquitectura y Características Principales

- **ChatServer:**
  - Utiliza `ServerSocket` para aceptar conexiones de clientes.
  - Gestiona múltiples conexiones concurrentemente mediante `ThreadPoolExecutor`.
  - Almacena clientes conectados en un `HashMap`.
  - Detecta la señal SIGTERM para cerrar el servidor de forma ordenada.
  - Muestra mensajes en el servidor sobre la conexión y desconexión de clientes.

- **ChatClientHandler:**
  - Cada instancia maneja la comunicación a través de un hilo para cada cliente específico.
  - En el inicio, solicita al cliente un nombre de usuario.
  - Detecta la desconexión del cliente y realiza las acciones correspondientes.

- **ChatClient:**
  - Representa a un cliente conectado al servidor.
  - Permite enviar mensajes al cliente.
  - Almacena mensajes en una cola para su entrega si el cliente se reconecta. (Aunque no consigo que funcione correctamente)
