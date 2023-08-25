package server;

import commands.CommandRequest;
import commands.CommandResponse;
import managers.CollectionManager;
import managers.Executor;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class that manages connections with clients
 */
public class Server {
    private final CollectionManager collectionManager;
    private final Executor executor;
    public Selector selector;
    private int port;
    private ServerSocketChannel serverSocketChannel;
    private ExecutorService channelThreadPool;

    public Server(CollectionManager collectionManager) {
        this.port = 5555;
        boolean isRunning = false;


        while (!isRunning) {
            try {
                serverSocketChannel = ServerSocketChannel.open();
                serverSocketChannel.socket().bind(new InetSocketAddress(port));
                serverSocketChannel.configureBlocking(false);

                this.selector = Selector.open();
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                isRunning = true;
                System.out.println("Server is running on port " + port);
            } catch (IOException e) {
                port++;
            }
        }

        this.collectionManager = collectionManager;
        this.executor = new Executor(collectionManager);
        this.channelThreadPool = Executors.newFixedThreadPool(10);
    }

    public void run() {
        try {
            while (true) {
                if (selector.select(5000) == 0) {
                    continue;
                }
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (!key.isValid()) {
                        continue;
                    }
                    if (key.isAcceptable()) {
                        SocketChannel channel = serverSocketChannel.accept();
                        channel.configureBlocking(false);
                        channel.register(selector, SelectionKey.OP_READ);
                        System.out.println("new connection");
                    } else if (key.isReadable()) {
                        Runnable runnable = () -> handleRead(key, selector);
                        channelThreadPool.execute(runnable);
                        //handleRead(key, selector);
                    } else if (key.isWritable()) {
                        // handleWrite(key);
                        key.channel().register(selector, SelectionKey.OP_READ);
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void handleRead(SelectionKey key, Selector selector) {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(65536);
        try {
            int readBytes = channel.read(buffer);
            if (readBytes == -1) {
                //System.out.println("Client disconnected");
                channel.close();
                return;
            }
            if (buffer.position() == 0) {
                return;
            }
            byte[] data = buffer.array();
            buffer.clear();
            Object receivedObject = deserialize(data);
            CommandResponse response = new CommandResponse("Command not found", null);

            if (receivedObject instanceof CommandRequest) {
                CommandRequest request = (CommandRequest) receivedObject;
                CommandResponse commandResponse = runCommand(request);
                sendResponse(channel, commandResponse);
                channel.register(selector, SelectionKey.OP_WRITE, serialize(commandResponse));
            }
        } catch (SocketException | ClosedChannelException e) {
            //System.out.println("Client disconnected");
            try {
                channel.close();
            } catch (IOException ex) {
                System.out.println("Error while closing channel");
            }
        } catch (Exception e) {
            System.out.println("Something went wrong");
        }
    }

    private void sendResponse(SocketChannel socketChannel, Object response) {
        ByteBuffer buffer = ByteBuffer.allocate(65536);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(response);
            objectOutputStream.flush();

            byte[] bytes = byteArrayOutputStream.toByteArray();
            buffer.put(bytes);

            buffer.flip();
            socketChannel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (objectOutputStream != null)
                    objectOutputStream.close();
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public CommandResponse runCommand(CommandRequest userCommand) {
        return executor.executeCommand(userCommand);
    }

    public byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }

    public Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return objectInputStream.readObject();
    }
}
