# MinnieTwitter demo application

Follow the following steps to build and run minnie-twitter demo app.

### Add Android Properties
```shell
$ cd DCAP-Sapphire-Examples/android-examples/example-minnietwitter/
> cat >> local.properties  << EOF
ndk.dir=<your ndk dir>
sdk.dir=<your sdk dir>
EOF
```

### Build App
```
$ cd DCAP-Sapphire-Examples/android-examples/example-minnietwitter/
$ ./gradlew build
```
