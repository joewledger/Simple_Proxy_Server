This project implements a simple multithreaded proxy server in Java.

The server accepts HTTP GET requests, forwards the requests to their intended recipient, recieves the responses, and forwards the responses to the original sender.

To run the code, cd into the src directory and type the following commands:

`javac proxyd.java`

`java proxyd x` where x is the port you want to run the proxy server on.

Then you can test the proxy server by changing your browser settings to point to the proxy server you just started.