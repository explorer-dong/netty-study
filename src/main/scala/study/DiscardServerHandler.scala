package study

import io.netty.buffer.ByteBuf
import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter}
import io.netty.util.ReferenceCountUtil

/*
DiscardServerHandler extends ChannelInboundHandlerAdapter,
 which is an implementation of ChannelInboundHandler.
 ChannelInboundHandler provides various event handler methods that you can override.
  For now, it is just enough to extend ChannelInboundHandlerAdapter rather than
  to implement the handler interface by yourself.
 */
class DiscardServerHandler extends ChannelInboundHandlerAdapter {

  //This method is called with the received message, whenever new data is received from a client.
  // In this example, the type of the received message is ByteBuf.
  override def channelRead(ctx: ChannelHandlerContext, msg: Any): Unit = {

    val in = msg.asInstanceOf[ByteBuf]

    try {
      print(in.toString(io.netty.util.CharsetUtil.US_ASCII))
    } finally {
      ReferenceCountUtil.release(msg)
    }

  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
    // Close the connection when an exception is raised.
    cause.printStackTrace
  }
}
