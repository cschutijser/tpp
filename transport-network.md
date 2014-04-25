# Transport/network layer

## Header format

The header for our transport/network layer looks like below.
 
| Header field name | Number of bits | First bit in packet | Last bit in packet | Purpose/notes |
| --- | --- | --- | --- | --- |
| Checksum | 33 | 0 | 32 | Checksum of header (excluding checksum, of course) + payload |
| Sequence number | 7 | 33 | 39 | Sequence number of the first payload byte of this segment |
| Sender | 8 | 40 | 47 | Sender address |
| Destination | 8 | 48 | 55 | Destination address |
| Acknowledgement number | 7 | 56 | 62 | Next sequence number that receiver is expecting |
| Segment number | 22 | 63 | 84 | Starts at 0 |
| ACK | 1 | 85 | 85 | ACK bit |
| More | 1 | 86 | 86 | More segments to come? 0=no, 1=yes |
| Length | 11 | 87 | 97 | Length of payload in bytes. 0 >= length <= 1024 |

XXX: blokjes van 8, reserved for future use

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
