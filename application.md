Application layer
=================

Basic layout
------------

The applications in the application layer use the same basic protocol:

| Bit # | 0 - 7   | 8 - ... |
| -----:| ------- | ------- |
|       | Command | Payload |

The command part of the protocol is eight bits (one byte) long. The desired
representation for this, is a ```char```. The payload has a length that is only
limited by the transport / network layer, which is 2^32 bytes minus one byte.

The following commands are currently available, and should be handled by the
implementing application:

| Command ```char``` | Application   | Action |
| ------------------ | ------------- | ------ |
| C (```67```)       | Chat          | Send chat message |
| F (```70```)       | File transfer | Send request to transfer file |
| A (```65```)       | File transfer | Accept file transfer |
| S (```83```)       | File transfer | Transfer file |


File transfers
--------------

The payload for file transfers always starts with the following data:

| Bit # | 0 - 31                       | 32 - ...  |
| -----:| ---------------------------- | --------- |
|       | File size (in bytes, signed) | File name |

__Important note:__ The file size integer is signed instead of unsigned. This is
done to accomodate for extra application layers and to make the application
layer package into a transport / network layer package.


