## Дополнительные коллекции Java

В данном репозитории размещены исходники библиотеки 
дополнительных коллекций `center.buran.jcollections`

Чтобы подключить библиотеку, нужно добавить репозиторий в файл `pom.xml`:

```xml
   <repositories>
        <repository>
            <id>buran-center</id>
            <url>https://mvn.buran.center/releases</url>
        </repository>
    </repositories>
```

Также необходимо подключить саму зависимость:

```xml
    <dependencies>
        <dependency>
            <groupId>center.buran.jcollections</groupId>
            <artifactId>patterns</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
```
