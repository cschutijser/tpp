Application layer
=================

Basic layout
------------

The applications in the application layer use the same basic protocol:

| Bit # | 0 - 7   | 8 - ... |
| -----:| ------- | ------- |
|       | Command | Payload |

The command part of the protocol is eight bits (one byte) long. The desired
representation for this, is a ```byte```. The payload has a length that is only
limited by the transport / network layer, which is 2^32 bytes minus one byte.

The following commands are currently available, and should be handled by the
implementing application:

| Command ```byte```  | Application   | Action |
| ------------------- | ------------- | ------ |
| C (```%67```)       | Chat          | Send chat message |
| F (```%70```)       | File transfer | Send request to transfer file |
| A (```%65```)       | File transfer | Accept file transfer |
| S (```%83```)       | File transfer | Transfer file |
| W (```%87```)       | WHOIS*        | WHOIS request |
| I (```%73```)       | WHOIS*        | WHOIS response |

All text, char and file data should be UTF-8 encoded, unless otherwise
specified.

Applications marked with * are optional. Hosts MAY choose to ignore them, 
at their option. Hosts SHOULD NOT respond to commands they do not recognize.
Of course, hosts who use optional applications MUST be able to handle hosts
who don't.

Chat
----

The payload for chat messages is very simple. It starts with the nickname of the
user, followed by a null byte and then the chat message.

| Byte # | 0 - n-1  | n - n+1   | n+2 - ...    |
| ------:| -------- | --------- | ------------ |
|        | Nickname | ```%00``` | Chat message |


File transfers
--------------

The payload for file transfers always starts with the following data:

| Bit # | 0 - 31                       | 32 - ...  |
| -----:| ---------------------------- | --------- |
|       | File size (in bytes, signed) | File name |

__Important note:__ The file size integer is signed instead of unsigned. This is
done to accomodate for extra application layers and to make the application
layer package into a transport / network layer package.

### Requesting a file transfer

For a transfer request, no additional data is needed. When no acceptation is
received, the file transfer can be cancelled (after any time). Invalid and/or
unknown transfer accept packages should be ignored.

### Accepting a file transfer

For accepting a file transfer, no additional data is needed. You should reply
with the file size and file name you received in the request. Accepting a
transfer does not automatically lead to receiving a file, the sending client
may decide to not send the file for any reason.

### Sending a file

Sending a file should only happen after requesting and accepting the transfer
of a file. Again, the payload should start with the standard payload for file
transfers. Additionaly, a null character should be appended after this basic
payload. The contents of the file can be put hereafter.

In conclusion, the following should be appended to the basic file transfer
header:

| Byte # | 0         | 1 - ...       |
| ------:| --------- | ------------- |
|        | ```%00``` | File contents |

WHOIS
-----

The WHOIS application allows hosts on the network to identify each other in a
human-readable way. In a way, it does the reverse of what the Domain Name
System does on the Internet. It is similar in function to (and named after) 
the WHOIS protocol.

A host that wishes to identify another host MAY send it a WHOIS request
packet command, which is a single command byte (defined to be ASCII 'W').
The receiving host MAY then choose to return a packet starting with the
WHOIS response byte ('I') followed by an identification string.

    Host A -> Host B: W
    Host B -> Host A: Ifreddy (Example TPP Implementation v0.1)

The format of identification strings ('idents') is implementation-defined
and not otherwise restricted. For example, hosts MAY choose to use their
chat nickname as an ident, or an identifier related to the name of the
group or the name of the implementation. Of course, WHOIS support is most
useful when different hosts (even within an implementation group)
return different identification strings, and this identification string is
constant at least while the host is connected to the network, but neither
of those are required.

Support for the WHOIS application is optional. Hosts MAY choose to ignore
WHOIS requests.

Notes
-----

Please remind yourself of the fact that network layer packets may not arrive in order, and multiple sequences (e.g. multiple file transfers) might take place in the same timeframe, due to the inner workings of the network layer. The application layer will ideally handle this property (e.g. keep a list of all file transfer requests/acceptances), but due to the fact that requests may expire at any time another implementation won't break things. Sending files to this particular client might be difficult however.
