# CreepinoUtils

my utility bukkit plugin (required for most of my other plugins to function)

## Developers

To use this plugin in your own plugin make sure you have CreepinoUtils installed on your server.

### Documentation

WIP

### Maven Installation

Install using Maven by
adding the following code to your project's pom.xml file.

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
<dependencies>
    <dependency>
        <groupId>com.github.creepinson</groupId>
        <artifactId>CreepinoUtils</artifactId>
        <version>-SNAPSHOT</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

### Gradle

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
        implementation 'com.github.creepinson:throw-out-utils-java:Tag'
}
```
