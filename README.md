## RPC框架
这是一个基于Java的手写RPC框架项目，用于分布式系统中不同服务间通信。项目主要包含四个核心模块：
- `common`：定义通用类和接口。
- `consumer`：服务消费者，通过 ServiceProxyFactory 创建代理对象发起远程调用。
- `provider`：服务提供者，将服务本地注册到 LocalRegistry，启动 HTTP 服务器监听端口并处理请求。
- `lzl-rpc-core`：框架核心模块，包含序列化器、代理、本地注册中心和 HTTP 服务器等功能。

主要内容：
1. RPC框架中需要维护一个全局的配置对象。在某个Provider引入 RPC 框架的项目启动时，从配置文件application.properties（若没有则使用默认配置）中读取配置并创建全局配置对象实例（使用双检锁单例模式），之后就可以集中地从这个对象中获取配置信息，而不用每次加载配置时再重新读取配置、并创建新的对象，减少了性能开销。
2. Consumer在配置文件中设置mock=true，RPC框架通过工厂模式给服务接口创建Mock代理对象，即可通过动态代理即可实现Mock接口调用。
3. 实现主流序列化器（JDK，Json，Kryo，Hessian）。可扩展设计：使用工厂模式+单例模式简化创建和获取序列化器对象的操作。并通过扫描资源路径+反射自实现了SPI机制（工厂使用静态代码块，在工厂首次(加载/主动使用)时，就会调用SpiLoader的load方法加载序列化器接口Serializer的所有实现类，之后就可以通过调用getInstance方法并指定key来获取对应的实现类实例了），用户也可通过编写配置的方式扩展和指定自己的序列化器。
4. 基于 Etcd 云原生中间件实现了高可用的分布式注册中心(结构类似于redis主从集群)，采用层级结构的存储结构。Etcd的Java客户端 Jetcd 中的 KvClient 用来存储服务和节点信息，leaseClient管理租约机制，用于为键值对分配生存时间。框架支持通过 SPI机制扩展注册中心。服务提供者启动时进行服务注册（key键名的层级结构规则是 /rpc/服务名:版本号/服务节点地址，value为服务元信息），服务消费者从注册中心获取服务提供者地址，即根据（/rpc/服务名称:版本号）前缀查询，从 Etcd 查询到某个服务的所有节点（服务元信息列表）。
5. 注册中心优化:（1）心跳检测和续期机制：服务提供者本地维护一个已注册节点集台，并定期发送（10s）请求到 Etcd 续签自己的注册信息，重写 TTL（30s，重新注册该service即可）。（2）服务节点下线机制：主动下线 -- 服务提供者利用 JVM 的 ShutdownHook 机制（JVM关闭前调用自己的destroy方法），主动从注册中心移除掉已注册的节点（service）。被动下线 -- 服务提供者宕机异常退出，利用 Etcd 的 key 过期机制自动移除。（3）消费端的服务缓存：消费端远程调用之前都会进行服务发现（同时利用 watchClient 对获取到的服务节点 key 进行监听），第一次会查询注册中心并将服务节点信息缓存到本地，之后就不用再去请求注册中心，提高性能。当监听的某个key发生修改或删除时（比如节点下线），则消费端会清理该服务的缓存。 

## 简易版RPC框架
这是一个基于Java的手写简易RPC框架项目，用于分布式系统中不同服务间通信。项目主要包含四个核心模块：
- `common`：定义通用类和接口，如 User 类和 UserService 接口。
- `consumer`：服务消费者，通过 ServiceProxyFactory 创建代理对象发起远程调用。
- `provider`：服务提供者，将服务本地注册到 LocalRegistry，启动 HTTP 服务器监听端口并处理请求。
- `lzl-rpc-easy`：框架核心模块，包含序列化器、代理、本地注册中心和 HTTP 服务器等功能。
  
主要内容:
1. 使用高性能的NIO框架Vert.x来作为RPC框架的Web服务器
2. 实现本地服务注册器，使用线程安全的 ConcurrentHashMap 存储本地服务注册信息，可以根据服务名称获取到对应实现类，并通过反射完成方法调用。
3. 实现序列化器，使得Java对象能够网络传输。RPC请求对象主要包括服务接口名，方法名，方法参数类型以及方法实际参数。
4. 请求处理器:基于 Vert.x的 Handler 接口实现对请求的异步处理，将请求数据反序列化后，从本地服务注册器中找到服务实现类并通过反射机制调用。
5. 实现代理工厂，通过工厂模式给服务接口创建代理对象。Consumer通过动态代理即可实现远程服务调用。