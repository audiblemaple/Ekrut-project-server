##Vending Machine Server<br>
This repository contains the server-side code for a vending machine project developed for a vending machine company.<br>
The server is implemented in Java and utilizes the OCSF (Object Client-Server Framework) to establish communication between the client and the server using WebSockets.<br>
<br>
Prerequisites<br>
Java JDK (version X.X.X)<br>
OCSF framework (version X.X.X)<br>
Installation<br>
<br>
<br>
Clone the repository:
```
git clone https://github.com/your-username/vending-machine-server.git
```
<br>
Install the required dependencies, including the OCSF framework.<br>
<br>
Build the project:<br>
```
javac -d bin src/*.java
```
<br>
###Usage<br>
Start the server:<br>
The server will start running and will listen for client connections on the specified port.

Configuration
You can modify the server's configuration by editing the config.properties file. This file contains the following properties:

server.port: The port number on which the server should listen for client connections.
server.maxConnections: The maximum number of concurrent client connections allowed.
server.timeout: The timeout value (in milliseconds) for client connections.
Make sure to restart the server for the changes to take effect.

Contributing
Contributions are welcome! If you find any issues or have suggestions for improvements, please open an issue or submit a pull request.

License
This project is licensed under the MIT License.

Acknowledgements
The OCSF framework: link to OCSF repository
Contact
If you have any questions or need further assistance, please feel free to contact us at email@example.com.
