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

外部计算机访问指定目的地的服务器资源的流程。
静态：资源是固定的，每个人看到的都是相同的。
动态：根据用户的信息，操作来展现不同的资源。比如商城的购物车。

如最常使用的nginx,在不引入外部模块的前提下能提供静态资源服务器的能力。而使用了cgi技术后，则能访问特定资源解释器，如php,python等，返回动态资源。java也可以通过实现cgi从而使用nginx来当作动态web服务器。
然而java拥有更高级的抽象服务--servlet。能够将资源和用户的请求信息抽象出来管理，使用web路径来绑定每一个servlet,而servlet不必是一个单独文件。
nginx没有提供servlet管理的功能，我们需要使用tomcat。tomcat是一个内置了类似于apache服务器的静态资源管理功能，servlet容器，jsp解析技术的程序。现在大多数的java web应用都是跑在tomcat上。

页面是哪里渲染的？是浏览器。页面资源是哪里来的？是服务器。在过去前后端未分离的基础上，页面html,css,js等静态资源，和动态资源都用同一个服务器提供。单独使用servlet或者是cgi技术的，都得考虑到最终的页面元素应该如何处理。php能直接编写html元素非常方便，可java不行，servlet的返回要写成html元素的拼接，这无疑非常的丑陋。于是参考微软的asp技术，发展出了jsp,能够像php一样编写页面元素，并且在页面中使用java程序运行时的资源。

在前后端分离后，不再需要返回页面元素，于是jsp的需求就减少了。只返回给前端json数据，就是字符串。

除了js全栈的项目外，大多前端都还使用静态资源服务器来部署。而需要与后台交互的技术就由浏览器来完成，早期的XMLHttpRequest (XHR)，还有现在浏览器原生的Fetch API等都是提供了请求服务器的功能（不限于http）。

## spring web 后端处理流程

（servlet规范流程）
请求-tomcat-filter（请求和离开servlet时都生效）-servlet
（框架实现）
-Inteceptor（指定针对servlet的生命周期的操作）-contoller-viewResolver-生成相应
-tomcat返回响应。

## 限流

看limit文件夹

- 无上游单体程序
- 由上游nginx单独或者配合redis限流
- 由分布式程序通过redis限流

实现上:

- 计数器
- 漏桶
- 令牌

### 分布式

使用lua,注意lua的执行应该是一个原子性的操作

