# Chess demo application


Follow the following steps to build and run hanksTodo demo app.

### Add Android Properties
```shell
$ cd DCAP-Sapphire-Examples/chess/
> cat >> local.properties  << EOF
ndk.dir=<your ndk dir>
sdk.dir=<your sdk dir>
EOF
```

### Build App
```
$ cd DCAP-Sapphire-Examples/chess/
$ ./gradlew build

```

## Local deployment

### Run Oms
```
$ ./gradlew runoms
```

### Run KernelServer
```
$ ./gradlew runks

$ ./gradlew runks2
```
### Run App
```
Run the App using Emulator 

```
