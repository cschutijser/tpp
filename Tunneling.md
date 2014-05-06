# Internet Tunneling

This is the tunneling of packets through a TCP connection. Every computer should have a server socket opened on port 1337. The computer with the highest adress number has to initiate the connection. If there is no succes setting up the connection the computer mentioned in the previous sentence has to retry periodically.

The maximum length of packets is the same as the maximum length of the Transport layer.

The tunnel should be left open while the network is up and running.

The tunnel may (but should not) be closed at any time and should be recreated as described above.

To summarize:
- TCP
- Port = 1337
- Leave the connection open
