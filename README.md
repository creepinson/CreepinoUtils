# CreepinoUtils
 my utility bukkit plugin (required for most of my other plugins to function)



## Developers
### Documentation
Head to 
https://creepinson.github.io/docs/creepinoutils/
for javadoc documentation on using CreepinoUtils in your plugin.
### Maven Installation
Install using Maven by
adding the following code to your project's pom.xml file.
```xml
<repositories>
    <repository>
        <id>creepinson-repo</id>
        <url>https://raw.githubusercontent.com/creepinson/CreepinoUtils/master/Repository</url>
    </repository>
</repositories>
<dependencies>
    <dependency>
        <groupId>me.creepinson</groupId>
        <artifactId>creepinoutils</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```
Unfortunately,
Github requires you to add your username and password to your maven home (.m2) settings.xml file. 
You can add these lines and replace the username and password with your own, and replace the 'TOKEN' part with 
the Base64 Encoded version of 'username:password' in that format.
You can encode your username and password in the format above using https://www.base64encode.org/.
Add the following to your settings.xml in the <servers> section.
```xml
<server>
  <id>github</id>
  <username>USERNAME</username>
  <password>PASSWORD</password>
  <configuration>
    <httpHeaders>
      <property>
        <name>Authorization</name>
        <!-- Base64-encoded "<username>:<password>" -->
        <value>Basic TOKEN</value>
      </property>
    </httpHeaders>
  </configuration>
</server>
```
