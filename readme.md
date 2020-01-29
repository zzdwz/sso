这是一个测试单点登录的例子

sso-server是sso认证中心
sso-taobao 为模似淘宝的客户端
sso-tmall 为模似天猫的客户端

为三个服务配置模似dns
在C:\Windows\System32\drivers\etc 下修改hosts文件增加如下配置
127.0.0.1 www.sso.com
127.0.0.1 www.tb.com
127.0.0.1 www.tmall.com 

分别启动三个应用服务器

测试步骤如下
1、打开chrome流览器,开启两个窗口,在第一个窗口输入地址http://www.tb.com:8002/taobao,将会跳转到认证中心的登录界面
2、输入userName: admin   password:123456 ,登录后跳转到淘宝首页
3、在别一个窗口输入地址http://www.tmall.com:8003/tmall, 直接会进入天猫的首页（免登录）


OSS单点登录原理
1、客户端将登录功能委托给SSO认证中心
2、客户端设置拦截器,拦截所有请求,先判断有没有局部登录（本应用登录,就是本应用的session会话中存在isLogin为true的值）
3、如果没有局部登录,判断请求中有没有token参数,如果没有,则重定向到SSO的checkLogin中判断有没有全局登录(在SSO认证服务器中的会话中存在token的值),如果没有说明未在SSO认证服务器中登录中,重定向到SSO的登录页面
4、在SSO登录页面登录后,就认为是全局登录了,生成唯一token,保存在全局会话中(认证服务器域名会话),并保存到数据库或redis中(token正确性认证需要),带上token参数重定向至客户端请求的地址
5、又回到了客户端的拦截器中,拦截器先判断有没有局部登录（没有）,再判断请求中有没有token参数(有),拿着这个token到认证服务器上认证,是不是合法的token(是),进行局部登录(设置本应用的session会话中isLogin的值为true),请求放行
6、同一客户端再请求时,在拦截器中发现已经局部登录了,直接放行请求,不再和SSO认证服务器交互
7、别外一个客户端做请求时,通过拦截器,判断没有局登录,也没有token参数,则重定向到SSO的checkLogin中判断有没有全局登录(在SSO认证服务器中的会话中存在token的值),此时存在全局登录（这是重点,为什么？因为所有客户端重定向请求的域名都是SSO认证服务器的域名,重局登录的会话是在此域名下的,所有客户端都共享此域名下的会话）,判断合局会话中存不存在token值(此时存在),带上此token重定向到客户端的原始请求,在客户端的拦截器中再次判断,此时未局部登录,但取到了token参数,拿着这个token参数到SSO认证服务器认证确实是真实的,进行局部登录,放行请求

OSS注销
1、客户端的注销请求是请求的OSS认证中心的全局会话的注销（或者全局会话到期自动失效）
2、OSS认证服务器的全局会话失效时,在LogoutSessionListener会话监听器中监测到后,移除OSS认证服务器中存储的全局token,然后根据每个客户端在OSS认证中心verify认证时携带的客户端信息(客户端注销地址和jsessionid)逐个通知客户端注销, 注销通知时调用HttpUtil中的sendDestorySessionRequest方法,此方法会把客户端的cookie的jsessionid值模似传入,客户端的logout方法中session.invalidate()失效的就是此jsessionid的会话,完成局部会话的注销
