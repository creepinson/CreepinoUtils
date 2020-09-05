# CreepinoUtils

My utility plugin (required for most of my other plugins to function)
## Features

- Configurable automatic broadcaster
    - Configurable color-coded prefix (example: [Announcement] message)
    - Configurable interval
- Database Options (WIP)
    - MySQL & MongoDB
    - Option to use default yaml file
- /announce command
    - Usage: /announce <message>
    - The message can be multiple words with spaces.
- Event Manager (WIP)
    - Database integration (or you can set it to disabled to 
        enable the default config file.
    - Manage events (requires permission node ```creepinoutils.event.admin```)
        - Use ```/event create <name> <server>``` to create an event with the specified server
            (can be a multiverse world, but not recommended)
        - Use ```/event remove <name>``` to delete your whole event (**WARNING: this is not recoverable**)
        - Use ```/event start <name>``` or ```/event stop <name>``` to start and stop the event.
            Stopping the event disables the ability for players to join the event.
    - Player commands
        - Use ```/event join <name>``` to join an event. Only works if the event is active.
        - Use ```/event list``` to see a list of events.
        
## Planned Features

- Seperate permission nodes for each event command
- Other plugin integration for plugins like UHCCore for adding minigames. 
- Multiverse multi-world support
 
## Developers

### Documentation
WIP
### Maven Installation
Install using Maven by
adding the following code to your project's pom.xml file.
```xml
<repositories>
    <repository>
        <id>creepinoutils-repo</id>
        <url></url>
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
