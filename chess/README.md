# Chess android demo application

### About the App
The chess app is an android board game in which the user competes against the system.

### Amino MicroServices
1. **ChessManager** :
    This class implements the microservice and makes use of the Default Deployment Manager(DM).

2. **SimpleEngine** :
    This class implements the microservice and makes use of the ExplicitMigration DM. Upon every 5<sup>th</sup> move by the user, the microservice migrates to the other kernelServer.

## Local Deployment

### Add Android Properties
This can be done in two ways:

1. **Through IDE**:
    Import the gradle project in the Android Studio and the IDE itself adds a local.properties file in your project structure with the path to your sdk defined.
        
Alternatively,

2. **From the command prompt**:
```shell
    $ cd Amino.Run-Demos/chess/
    > cat >> local.properties  << EOF
    ndk.dir=<your ndk dir>
    sdk.dir=<your sdk dir>
    EOF
```

### Build App
Start by building the app, this ensures the generation of the required stubs.
```

$ cd Amino.Run-Demos/
$ ./gradlew build
```
### Environment Setup
1. Replace the below ip addresses and ports with that of oms and kernelServers respectively:

    Amino.Run-Demos/**gradle.properties** 
    ```
    omsIp = 127.0.0.1
    kernelServer1Ip = 127.0.0.1
    kernelServer2Ip = 127.0.0.1
    omsPort=22346
    kernelServer1Port=22345
    kernelServer2Port=22344
    ```
    
2.  Replace the first and second argument passed to the **hostAddress** with the ```omsIp``` and ```omsPort``` respectively:

    Amino.Run-Demos/chess/src/main/java/kobi/chess/**Configuration.java**
    ```
    public static String[] hostAddress = { "127.0.0.1", "22346", "10.0.2.15", "22345" };
    ```
    The third and fourth argument above in the hostAddress are your device kernelServer ip and port respectively.
    
    Replace the arguments passed to the **kernelServerAddress** with ```kernelServer1Ip```, ```kernelServer1Port```, ```kernelServer2Ip``` and ```kernelServer2Port``` respectively:
    ```
    public static String[] kernelServerAddress = { "127.0.0.1", "22345", "127.0.0.1", "22344" };
    ```

### Run OMS
```
$ cd Amino.Run-Demos/
$ ./gradlew chess:subprojects:runoms
```

### Run KernelServer
```
$ cd Amino.Run-Demos/
$ ./gradlew chess:subprojects:runks
```

### Run Second KernelServer
```
$ cd Amino.Run-Demos/
$ ./gradlew chess:subprojects:runks2
```

### Run App
Start the app from within the IDE by clicking on the run app icon.
