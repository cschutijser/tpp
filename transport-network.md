# Transport/network layer

## Header format

The header for our transport/network layer looks like below.
 
|Header field name|Number of bits|Purpose/notes|
|Checksum|33|Checksum of header (excluding checksum, of course) + payload|
|Sender|8|Sender address|
|Destination|8|Destination address|
|Length|11|Length of payload in bytes. 0 >= length <= 1024|
|Sequence number|32|Sequence number of the first payload byte of this segment|
|Acknowledgement number|32|Next sequence number that receiver is expecting|
|Segment number|22|Starts at 0|
|More|1|More segments to come? 0=no, 1=yes|

The payload follows after the header. The size of the payload is >= 0 && <= 1024
bytes.

## Notes

 * Packets must be dropped if the checksum is invalid.
 * When a packet has been sent, the sender must wait for an acknowledgement to
   arrive before sending another packet.
 * When a packet has been sent, the sender must start a timer. When the timer
   expires, the packet can be sent again. The time-out has yet to be determined.



DRAFT:

Header format:
 * Checksum (header + payload, checksum is assumed to be 0). CRC: 
 * Sender: 8 bits
 * Destination: 8 bits
 * Length: 10 bits
 * Sequence number: 32 bits
 * Acknowledgement number: 32 bits
 * Segment number: 22 bits
 * More segments to come: 0=no, 1=yes


22 bits segment number
