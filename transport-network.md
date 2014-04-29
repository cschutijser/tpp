# Transport/network layer

## Header format

The header for our transport/network layer is defined below.
 
| Header field name | Number of bits | First bit in packet | Last bit in packet | Purpose/notes |
| --- | --- | --- | --- | --- |
| Checksum | 33 | 0 | 32 | Checksum over header + payload |
| (Reserved for future use) | 2 | 33 | 34 | Reserved for future use |
| ACK | 1 | 35 | 35 | ACK bit |
| More | 1 | 36 | 36 | More segments to come? 0=no, 1=yes |
| Length | 11 | 37 | 47 | Length of payload in bytes. 0 >= length <= 1024 |
| Sender | 8 | 48 | 55 | Sender address |
| Destination | 8 | 56 | 63 | Destination address |
| Sequence number | 8 | 64 | 71 | Packet sequence number. Starts at 0, increment by 1 for every new packet |
| Acknowledgement number | 8 | 72 | 79 | Next sequence number that receiver is expecting |
| Segment number | 24 | 80 | 103 | Starts at 0 |

The payload follows after the header.
The minimum size of the payload is 0 bytes, the maximum size of the payload is
1024 bytes (this is defined by the Length field).

## Composing a packet

When you want to send some data, you can send one or more packets.

### Checksum

For the checksum, we use CRC-32.
The checksum is calculated over the header and the payload.
When calculating the checksum, the checksum area is assumed to be 0.

Because the checksum has a length of 33 bits, the most significant bit of a byte
you calculate the checksum over is changed, but the checksum is calculated with
the old value of this bit in mind.
This bit (bit 32 of the header) should be set to 0.

### Sending more than 1024 bytes

If the size of the application layer data exceeds the maximum length of a packet
(1024 bytes), you need to send multiple packets, each containing a segment
number.
The segment number starts at 0, and is incremented with each new packet that
belongs to this series of packets.
Setting the More bit to 1 indicates that more packets belonging to this series
are coming.
This obviously means that the last packet of this series has the More bit set to
0.

### ACKing a packet

Each received packet contains a sequence number.
With an ACK, you can indicate which sequence number you expect next.
This acknowledgement number is put in the `Acknowledgement number' field of the
packet.
When ACKing a packet, the ACK bit must be set to 1.

### Notes

 * Packets must be dropped if the checksum is invalid.
 * When a packet has been sent, the sender must wait for an acknowledgement to
   arrive before sending another packet.
 * When a packet has been sent, the sender must start a timer. When the timer
   expires, the packet can be sent again. The time-out has yet to be determined.

