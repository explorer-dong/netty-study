package study

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.{ChannelInitializer, ChannelOption}

/**
  * https://netty.io/wiki/user-guide-for-4.x.html
  */
class DiscardServer {

  def run(host: String, port: Int): Unit = {
    val bossGroup = new NioEventLoopGroup()
    val workerGroup = new NioEventLoopGroup()

    try {

      val b = new ServerBootstrap()
      b.group(bossGroup, workerGroup)
        .channel(classOf[NioServerSocketChannel])
        .childHandler(new ChannelInitializer[SocketChannel] {
          override def initChannel(ch: SocketChannel): Unit = {
            ch.pipeline().addLast(new DiscardServerHandler())
          }
        })
//        .option(ChannelOption.SO_BACKLOG, 128)
//        .childOption(ChannelOption.SO_KEEPALIVE, true)

      // Bind and start to accept incoming connections.
      val f = b.bind(port).sync()
      f.channel.closeFuture.sync()
    } finally {
      workerGroup.shutdownGracefully()
      bossGroup.shutdownGracefully()
    }
  }


}

object DiscardServer {

  /*
  启动之后，telnet localhost 8080
  输入文字，查看服务器是否打印。
   */
  def main(args: Array[String]): Unit = {
    val port = 8080
    val host = "localhost"

    new DiscardServer().run(host, port)
  }
}