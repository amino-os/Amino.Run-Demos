# HanksTodo android demo application

### About the App
The hanksTodo app allows users to create multiple todo lists. Users can maintain multiple todo items within a todo list. Once you are done with a todo item or a list, you can go ahead and delete them.

### Amino MicroServices
1. **TodoListManager** :
    This class implements the microservice and makes use of the Default Deployment Manager(DM).
    
2. **TodoList** :
    This class implements the microservice and uses the combination of DHT and ConsensusRSM DMs.


## Local Deployment

### Add Android Properties
This can be done in two ways:

1. **Through IDE**:
    Import the gradle project in the Android Studio and the IDE itself adds a local.properties file in your project structure with the path to your sdk defined.
        
Alternatively,

2. **From the command prompt**:
```shell
    $ cd Amino.Run-Demos/hanksTodo/
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
1. Replace the below ip addresses and ports with that of oms and kernelServer respectively:

    Amino.Run-Demos/**gradle.properties** 
    ```
    omsIp = 127.0.0.1
    kernelServer1Ip = 127.0.0.1
    omsPort=22346
    kernelServer1Port=22345
    ```
    
2.  Replace the first and second argument passed to the **hostAddress** with the ```omsIp``` and ```omsPort``` respectively:

    Amino.Run-Demos/hanksTodo/src/main/java/amino/run/appexamples/hankstodo/glue/**Configuration.java**
    ```
    public static String [] hostAddress = { "127.0.0.1", "22346", "10.0.2.15", "22345" };
    ```
    The third and fourth argument above in the hostAddress are your device kernelServer ip and port respectively.

### Run OMS
```
$ cd Amino.Run-Demos/
$ ./gradlew hankTodo:subprojects:runoms
```

### Run KernelServer
```
$ cd Amino.Run-Demos/
$ ./gradlew hanksTodo:subprojects:runks
```

### Run App
Start the app from within the IDE by clicking on the run app icon.