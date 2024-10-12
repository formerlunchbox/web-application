# springweb学习

## 环境

```shell
docker run -d \
  -v xx/redis.conf:/usr/local/etc/redis/redis.conf \
  -v xx/lua:/usr/local/etc/redis/lua \
  -p 6379:6379 \
  redis/redis-stack-server:latest \
  redis-server /usr/local/etc/redis/redis.conf

#redis conf
protected-mode no
bind 0.0.0.0
```

## web工作流程

静态资源： 由服务器直接提供的固定资源，每个用户看到的内容都是相同的，比如 HTML、CSS、JS 文件、图片等。这类资源一般不需要处理用户的输入，直接从服务器返回到客户端浏览器。

动态资源： 根据用户输入、请求参数或会话状态生成的资源。比如购物车页面会根据用户添加的商品显示不同内容。动态资源通常由后端应用程序生成，比如 PHP、Java 等服务器端语言根据用户请求返回不同的数据或页面。

### Nginx 的静态和动态资源处理

静态资源处理： Nginx 是一个高性能的 HTTP 服务器，它可以在不引入任何外部模块的前提下，提供静态资源服务，比如图片、HTML、CSS 等。这使得 Nginx 在作为反向代理服务器的同时，也能很好地处理静态资源。

动态资源处理： Nginx 本身不直接处理动态内容。通过使用 CGI（通用网关接口）技术，Nginx 可以将请求转发到后端应用程序服务器，比如 PHP、Python 或 Java 程序，然后将其生成的动态内容返回给客户端。对于 PHP，通常结合 PHP-FPM 来实现动态请求的处理。

### Java 和 Servlet 技术

相比 CGI，Java 提供了更高级的抽象来处理动态资源，即 Servlet。Servlet 是一种 Java 程序，它可以处理客户端的 HTTP 请求并生成动态内容。通过将请求和响应对象抽象出来，Servlet 能够为每个请求生成个性化的响应。Servlet 使用 Web 路径与客户端请求绑定，并不需要为每个动态页面创建单独的文件。

### Nginx 和 Servlet

Nginx 不支持原生的 Java Servlet 容器功能。为了运行基于 Servlet 的 Java Web 应用，我们通常使用 Tomcat 等专门的 Java 服务器。Tomcat 是一个带有内置 Servlet 容器、JSP 解析功能以及静态资源管理功能的服务器。它可以处理基于 Java 的 Web 应用，包括 Servlet 和 JSP。

大多数 Java Web 应用部署在 Tomcat 等支持 Servlet 技术的容器中，而 Nginx 通常作为反向代理，将请求转发到后端的 Tomcat 服务器进行处理。

### 页面渲染和前后端分离

页面渲染： 最终的页面是在客户端浏览器中渲染的。浏览器从服务器获取 HTML、CSS、JS 等资源后，进行解析并生成页面元素。

页面资源来源： 过去，前后端未分离时，服务器同时提供静态资源（HTML、CSS、JS 等）和动态资源，返回完整的网页。动态内容通常通过 CGI、Servlet、JSP 或 PHP 生成。

在 Java 的 Servlet 中，手动拼接 HTML 元素是十分繁琐的。为了简化页面开发，Java 引入了 JSP（JavaServer Pages）。JSP 类似于 PHP，允许在 HTML 页面中嵌入 Java 代码，通过运行时动态生成页面内容。

### 前后端分离后的演变

在现代的 前后端分离 架构中，后端通常只返回 JSON 数据，而不再直接生成 HTML 页面。前端通过 AJAX 请求（早期使用 XMLHttpRequest，现如今使用 Fetch API）向后端请求数据。返回的数据（通常为 JSON 格式）由前端 JavaScript 渲染并展示到页面上。

在这种模式下，后端负责处理业务逻辑，前端则负责用户界面和交互。JSP 等服务器端模板技术的需求逐渐减少，因为现代前端框架（如 React、Vue 等）在客户端进行渲染。

静态资源部署： 即使在前后端分离的项目中，静态资源服务器（如 Nginx）依然被广泛使用，用于部署前端构建生成的静态资源。与后端的交互则通过 API 请求来实现。

## spring web 后端处理流程

（servlet规范流程）
请求-tomcat-filter（请求和离开servlet时都生效）-servlet
（框架实现）
-Inteceptor（指定针对servlet的生命周期的操作）-contoller-viewResolver-生成相应
-tomcat返回响应。

## servlet生命周期

1. 加载与实例化（Initialization）：

    容器（如 Tomcat）启动时，Servlet 类会被加载。加载可以是容器启动时（eager loading）或首次请求时（lazy loading）。
    加载后，容器会实例化该 Servlet 类。

2. 初始化（Initialization, init() 方法）：

    容器会调用 init() 方法来完成初始化操作。该方法只会被调用一次。
    init() 方法可以接受一个 ServletConfig 对象，允许获取配置参数。
    初始化完成后，Servlet 处于就绪状态，可以响应客户端请求。

3. 请求处理（Request Handling, service() 方法）：

    每次客户端发送请求时，容器调用 service() 方法。
    service() 方法根据请求类型（如 GET、POST 等）将请求委派给相应的处理方法（如 doGet()、doPost()）。
    在这个过程中，Servlet 从请求中获取数据，处理后生成响应返回给客户端。

4. 销毁（Destruction, destroy() 方法）：

    当 Servlet 被卸载或容器关闭时，容器会调用 destroy() 方法进行清理操作。
    该方法在 Servlet 的生命周期中只会调用一次，确保资源正确释放。

### springmvc的处理

Spring MVC 的核心是 DispatcherServlet，它本质上是一个特殊的 Servlet。DispatcherServlet 负责将 HTTP 请求分发给不同的控制器（Controller）进行处理。具体流程如下：

1. Spring 容器初始化 DispatcherServlet：
        在 Spring Boot 或 Spring MVC 项目中，DispatcherServlet 被自动配置为一个标准的 Servlet。
        它会拦截所有传入的请求，并根据请求路径匹配到合适的控制器。

2. DispatcherServlet 解析请求：
        当客户端发送 HTTP 请求时，DispatcherServlet 接收到请求并根据配置找到相应的处理器（Controller 方法）。

3. HandlerMapping 匹配路径：
        Spring 的 HandlerMapping 会根据路径和 HTTP 方法找到与请求匹配的控制器方法。

4. HandlerAdapter 调用控制器方法：
        HandlerAdapter 执行找到的处理器，并将处理结果返回给 DispatcherServlet。

5. 响应处理：
        DispatcherServlet 将控制器的返回值转换为 HTTP 响应，返回给客户端。

传统的servlet处理需要每个路径配置一个servlet,由servlet容器分发相应的请求。

## 限流

看limit文件夹

服务的构建实现上：

- 无上游单体程序
- 由上游nginx单独或者配合redis限流
- 由分布式程序通过redis限流

粒度：

- nginx
- gateway
- servlet
- interceptor
- 或是具体的业务单独处理

逻辑实现上:

- 计数器
- 漏桶
- 令牌

### 分布式

使用lua,注意lua的执行应该是一个原子性的操作
