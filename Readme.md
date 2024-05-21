## Java-class Encrypt/Decrypt 加密/解密

```text
javaagent
├─agent             # 启动时的agent-jar
├─gradle-plugin     # gradle插件
├─lib               # 基础代码
├─maven-plugin      # maven插件
└─test              # 只有这个模块下有测试
```
### 环境
1. 理论上Jdk8以上都可以
2. 只支持jar
3. 只测试了java -jar 启动方式，没有测试tomcat原生启动方式


### 使用方式
#### 用自己的方式使其能作为插件被打包工具使用
比如，将其上传到nexus服务

#### gradle
```kotlin
plugins {
    id("fuck.world.re-compile-gradle") version "037-SNAPSHOT"
}

tasks.reCompilePlugin {
    //true加密 false解密
    encrypt = true

    //加密用
    pubKey = "d:\\pub"
    //解密用
    priKey = "d:\\pri"
}
```


##### 如果使用nexus服务
别忘了修改`setting.gradle.kts`
```kotlin
pluginManagement {
    repositories {
        maven {
            url = uri("http://192.168.1.23:36307/nexus/repository/maven-snapshots/")

            // 如果仓库需要身份验证，配置用户名和密码
            credentials {
                username = "admin"
                password = "password123"
            }
            isAllowInsecureProtocol = true
        }
        mavenCentral()
    }
}
```

##### 打包的时候可能会遇到覆盖加密class
排除编译就好了
```shell
gradle jar -x compileJava
```

#### maven
先修改`setting.xml`文件在这里
```text
├─gradle
   └─setting.xml
```
再修改项目的pom.xml
```xml
<build>
    <plugins>
        <plugin>
            <groupId>fuck.world</groupId>
            <artifactId>re-compile-maven</artifactId>
            <version>037-SNAPSHOT</version>
            <configuration>
                <!--加密用-->
                <pubKey>d:\\pub</pubKey>
                <!--解密用-->
                <priKey>d:\\pri</priKey>
                <!--true加密 false解密-->
                <encrypt>true</encrypt>
            </configuration>
        </plugin>
    </plugins>
</build>
```
### 随便说说
###### 其实只是加个壳，可以使用 arthas 运行时保存class文件