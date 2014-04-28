

package tpp;

import java.util.Random;

public class Header
{
	private final byte[] data = new byte[13];
	
	public static void main(String[] args)
	{
		//Generate a large number of instances of this class,
		//fill them with random values and see if the same values come out.
		System.out.println("Testing Header.java:");
		
		Header h = new Header();
		Random r = new Random();
		
		for (int index = 0; index < 1000000; index++)
		{
			//Choose random values for the header fields.
			boolean ackFlag = r.nextBoolean();
			byte acknowledgementNumber = (byte)(r.nextInt(256));
			long checksum = r.nextLong() & 0x1FFFFFFFFL; //--> Only 33 of 64 bits allowed.
			byte destination = (byte)(r.nextInt(256));
			short length = (short)(r.nextInt(1025));
			boolean moreFlag = r.nextBoolean();
			boolean reservedFlag1 = r.nextBoolean();
			boolean reservedFlag2 = r.nextBoolean();
			int segmentNumber = r.nextInt() & 0xFFFFFF; //--> Only 24 of 32 bits allowed.
			byte sender = (byte)(r.nextInt(256));
			byte sequenceNumber = (byte)(r.nextInt(256));
			
			//Set the header fields.
			h.setAckFlag(ackFlag);
			h.setAcknowledgementNumber(acknowledgementNumber);
			h.setChecksum(checksum);
			h.setDestination(destination);
			h.setLength(length);
			h.setMoreFlag(moreFlag);
			h.setReservedFlag1(reservedFlag1);
			h.setReservedFlag2(reservedFlag2);
			h.setSegmentNumber(segmentNumber);
			h.setSender(sender);
			h.setSequenceNumber(sequenceNumber);
			
			//Check if the extracted value matches the original value.
			if (h.getAckFlag() != ackFlag) System.out.println("AckFlag failed (" + ackFlag + " vs " + h.getAckFlag() + ")!");
			if (h.getAcknowledgementNumber() != acknowledgementNumber) System.out.println("AcknowledgementNumber failed (" + acknowledgementNumber + " vs " + h.getAcknowledgementNumber() + ")!");
			if (h.getChecksum() != checksum) System.out.println("Checksum failed (" + checksum + " vs " + h.getChecksum() + ")!");
			if (h.getDestination() != destination) System.out.println("Destination failed (" + destination + " vs " + h.getDestination() + ")!");
			if (h.getLength() != length) System.out.println("Length failed (" + length + " vs " + h.getLength() + ")!");
			if (h.getMoreFlag() != moreFlag) System.out.println("MoreFlag failed (" + moreFlag + " vs " + h.getMoreFlag() + ")!");
			if (h.getReservedFlag1() != reservedFlag1) System.out.println("ReservedFlag1 failed (" + reservedFlag1 + " vs " + h.getReservedFlag1() + ")!");
			if (h.getReservedFlag2() != reservedFlag2) System.out.println("ReservedFlag2 failed (" + reservedFlag2 + " vs " + h.getReservedFlag2() + ")!");
			if (h.getSegmentNumber() != segmentNumber) System.out.println("SegmentNumber failed (" + segmentNumber + " vs " + h.getSegmentNumber() + ")!");
			if (h.getSender() != sender) System.out.println("Sender failed (" + sender + " vs " + h.getSender() + ")!");
			if (h.getSequenceNumber() != sequenceNumber) System.out.println("SequenceNumber failed (" + sequenceNumber + " vs " + h.getSequenceNumber() + ")!");
		}
		
		System.out.println("Done!");
	}
	
	/**
	 * Casts the specified value to an integer, and then sets the first 24 bits to zero.
	 * This effectively casts a byte to an unsigned integer.
	 * @param value Value.
	 * @return Casted value.
	 */
	private int uint(byte value)
	{
		return ((int)value) & 0xFF;
	}
	
	/**
	 * Casts the specified value to a long, and then sets the first 56 bits to zero.
	 * This effectively casts a byte to an unsigned long.
	 * @param value Value.
	 * @return Casted value.
	 */
	private long ulong(byte value)
	{
		return ((long)value) & 0xFF;
	}
	
	/**
	 * @return CRC32-checksum (a 33-bit sequence).
	 * This value is contained within <ul>
	 * <li> all bits of <tt>data[0]</tt> to <tt>data[3]</tt> and
	 * <li> the 1st bit of <tt>data[4]</tt>.
	 * </ul>
	 */
	public long getChecksum()
	{
		return (ulong(data[0]) << 25) | (ulong(data[1]) << 17) | (ulong(data[2]) << 9) | (ulong(data[3]) << 1) | ((ulong(data[4]) & 0x80) >> 7);
	}
	
	/**
	 * @param checksum New CRC32-checksum (a 33-bit sequence).
	 */
	public void setChecksum(long checksum)
	{
		data[0] = (byte)(checksum >> 25);
		data[1] = (byte)(checksum >> 17);
		data[2] = (byte)(checksum >> 9);
		data[3] = (byte)(checksum >> 1);
		data[4] = (byte)((data[4] & 0x7F) | ((checksum & 0x01) << 7));
	}
	
	/**
	 * @return Value of the 1st flag that is reserved for future use.
	 * This value is contained within the 2nd bit of <tt>data[4]</tt>.
	 */
	public boolean getReservedFlag1()
	{
		return (data[4] & 0x40) == 0x40;
	}
	
	/**
	 * @param reservedFlag1 New value for the 1st flag that is reserved for future use.
	 */
	public void setReservedFlag1(boolean reservedFlag1)
	{
		data[4] = (byte)((data[4] & 0xBF) | (reservedFlag1 ? 0x40 : 0x00));
	}
	
	/**
	 * @return Value of the 2nd flag that is reserved for future use.
	 * This value is contained within the 3rd bit of <tt>data[4]</tt>.
	 */
	public boolean getReservedFlag2()
	{
		return (data[4] & 0x20) == 0x20;
	}
	
	/**
	 * @param reservedFlag2 New value for the 2nd flag that is reserved for future use.
	 */
	public void setReservedFlag2(boolean reservedFlag2)
	{
		data[4] = (byte)((data[4] & 0xDF) | (reservedFlag2 ? 0x20 : 0x00));
	}
	
	/**
	 * @return Value of the ACK flag.
	 * This value is contained within the 4th bit of <tt>data[4]</tt>.
	 */
	public boolean getAckFlag()
	{
		return (data[4] & 0x10) == 0x10;
	}
	
	/**
	 * @param ackFlag New value for the ACK flag.
	 */
	public void setAckFlag(boolean ackFlag)
	{
		data[4] = (byte)((data[4] & 0xEF) | (ackFlag ? 0x10 : 0x00));
	}
	
	/**
	 * @return Value of the MORE flag.
	 * This value is contained within the 5th bit of <tt>data[4]</tt>.
	 */
	public boolean getMoreFlag()
	{
		return (data[4] & 0x08) == 0x08;
	}
	
	/**
	 * @param moreFlag New value for the MORE flag.
	 */
	public void setMoreFlag(boolean moreFlag)
	{
		data[4] = (byte)((data[4] & 0xF7) | (moreFlag ? 0x08 : 0x00));
	}
	
	/**
	 * @return Sender address (0..1024).
	 * This value is contained within<ul>
	 * <li> the last 3 bits of <tt>data[4]</tt> and
	 * <li> all bits of <tt>data[5]</tt>.
	 * </ul>
	 */
	public short getLength()
	{
		return (short)(((data[4] & 0x07) << 8) | uint(data[5]));
	}
	
	/**
	 * @param length New length value (0..1024).
	 */
	public void setLength(short length)
	{
		data[4] = (byte)((data[4] & 0xF8) | ((length >> 8) & 0x07));
		data[5] = (byte)length;
	}
	
	/**
	 * @return Sender address (0..255).
	 * This value is contained within <tt>data[6]</tt>.
	 */
	public byte getSender()
	{
		return data[6];
	}
	
	/**
	 * @param sender New value for the sender address (0..255).
	 */
	public void setSender(byte sender)
	{
		data[6] = sender;
	}
	
	/**
	 * @return Destination address (0..255).
	 * This value is contained within <tt>data[7]</tt>.
	 */
	public byte getDestination()
	{
		return data[7];
	}
	
	/**
	 * @param sender New value for the destination address (0..255).
	 */
	public void setDestination(byte destination)
	{
		data[7] = destination;
	}
	
	/**
	 * @return Sequence number (0..255).
	 * This value is contained within <tt>data[8]</tt>.
	 */
	public byte getSequenceNumber()
	{
		return data[8];
	}
	
	/**
	 * @param sender New value for the sequence number (0..255).
	 */
	public void setSequenceNumber(byte sequenceNumber)
	{
		data[8] = sequenceNumber;
	}
	
	/**
	 * @return Acknowledgement number (0..255).
	 * This value is contained within <tt>data[9]</tt>.
	 */
	public byte getAcknowledgementNumber()
	{
		return data[9];
	}
	
	/**
	 * @param sender New value for the acknowledgement number (0..255).
	 */
	public void setAcknowledgementNumber(byte acknowledgementNumber)
	{
		data[9] = acknowledgementNumber;
	}
	
	/**
	 * @return Segment number (a 24-bit number).
	 * This value is contained within <tt>data[10]</tt> to <tt>data[12]</tt>.
	 */
	public int getSegmentNumber()
	{
		return (uint(data[10]) << 16) | (uint(data[11]) << 8) | uint(data[12]);
	}
	
	/**
	 * @param sender New value for the segment number (a 24-bit number).
	 */
	public void setSegmentNumber(int segmentNumber)
	{
		data[10] = (byte)(segmentNumber >> 16);
		data[11] = (byte)(segmentNumber >> 8);
		data[12] = (byte)segmentNumber;
	}
	
	/**
	 * @return Array of bytes in which the header data is stored.
	 * This data can also be modified and then used to extract the separate header fields.
	 */
	public byte[] data()
	{
		return data;
	}
}

