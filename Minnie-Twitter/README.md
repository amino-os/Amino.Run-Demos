# MinnieTwitter demo application

Follow the following steps to build and run minnie-twitter demo app.

### Add Android Properties
```shell
$ cd DCAP-Sapphire-Examples/Minnie-Twitter/
> cat >> local.properties  << EOF
ndk.dir=<your ndk dir>
sdk.dir=<your sdk dir>
EOF
```

## Signing the release-apk file and building App
### Generate keystores for signing Android apps
```
$ cd DCAP-Sapphire-Examples/Minnie-Twitter/
$ keytool -genkey -v -keystore release.keystore -storepass xxxxxx -alias xxxxxx -keypass xxxxxx -keyalg RSA -keysize 2048 -validity 10000
$ ./gradlew build
```

### Installing apk file in android device
```
$ cd DCAP-Sapphire-Examples/Minnie-Twitter/
$ sudo adb install build/outputs/apk/Minnie-Twitter-release.apk
```

### Uninstalling apk file in android device
```
$ sudo adb uninstall com.example.minnietwitter
```

## Local deployment

### Run Oms
```
$ ./gradlew runOms
```

### Run KernelServer
```
$ ./gradlew runKernelServer
```

### Run App
```
$ ./gradlew runApp
```
