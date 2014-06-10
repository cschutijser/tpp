# Transport/network layer

## Requirements to lower layer (link layer)

The link layer must provide packets to the transport/network layer.

## Header format

The header for our transport/network layer is defined below.
 
| Header field name | Number of bits | First bit in packet | Last bit in packet | Purpose/notes |
| --- | --- | --- | --- | --- |
| Checksum | 32 | 0 | 31 | Checksum over header + payload |
| TTL | 3 | 32 | 34 | Time to live. Starts at 7 |
| ACK | 1 | 35 | 35 | ACK bit |
| More | 1 | 36 | 36 | More segments to come? 0=no, 1=yes |
| Length | 11 | 37 | 47 | Length of payload in bytes. 0 >= length <= 1024 |
| Sender | 8 | 48 | 55 | Sender address |
| Destination | 8 | 56 | 63 | Destination address |
| Sequence number | 8 | 64 | 71 | Packet sequence number. Starts at 0, increment by 1 for every new packet that is not an ACK |
| Acknowledgement number | 8 | 72 | 79 | Next sequence number that receiver is expecting |
| Segment number | 24 | 80 | 103 | Starts at 0 |

The payload follows after the header.
The minimum size of the payload is 0 bytes, the maximum size of the payload is
1024 bytes (this is defined by the Length field).

All numbers are unsigned, unless specified otherwise.

## Composing a packet

When you want to send some data, you can send one or more packets.

### Checksum

For the checksum, we use CRC-32.
The checksum is calculated over the header and the payload.
When calculating the checksum, the checksum area is assumed to be 0.

### TTL

We use a TTL to prevent packets from circulating indefinitely through the
network.
When a packet is sent, the TTL must be set to 7.
Each node that forwards the packet must decrement the TTL by 1.
Of course, the checksum needs to be recalculated in that case.
Before forwarding a packet, the node must verify that the TTL in the received
packet is not 0.
If the TTL in a received packet is 0 and the packet is not destined to that
node, the packet must be dropped.

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

Each received packet contains a sequence number and optionally a segment
number.
With an ACK, you can indicate which sequence number (and optionally which
segment number) you received.
The sequence number that you want to ACK is put in the 'Acknowledgement number'
field of the packet.
The segment number is put in the 'Segment number' field of the packet.
When ACKing a packet, the ACK bit must be set to 1.

Packets containing an ACK (so the ACK bit set to 1) must not contain a payload.
Therefore, an ACK does not need to have a valid sequence number, which means
the sequence number can be ignored.

### Miscellaneous

 * Packets must be dropped if the checksum is invalid.
 * When a packet has been sent, the sender must wait for an acknowledgement to
   arrive before sending another packet.
 * When a packet has been sent, the sender must start a timer. When the timer
   expires, the packet can be sent again. The time-out has yet to be determined.
 * When a packet has been retransmitted 3 times, the packet must be dropped.
 * When a field is unused, the field must be set to 0. So for example, when the
   ACK bit is 0, the Acknowledgement number field must be 0.
 * Note that sequence numbers do not necessarily need to be consecutive.

