# Apk签名hook服务

### 使用
1. 安装java,配置安卓环境变量和aapt工具
    ```bash
    export ANDROID_HOME="$HOME/Library/Android/sdk"
    export ANDROID_TOOLS="$ANDROID_HOME/tools/bin"
    export ANDROID_PLATFORM_TOOLS="$ANDROID_HOME/platform-tools"
    export ANDROID_BUILD_TOOLS="$ANDROID_HOME/build-tools/28.0.3/"
    export NDK_ROOT="$HOME/Library/Android/sdk/ndk-bundle/"
    export PATH="$HOME/node_modules/.bin:$ANDROID_BUILD_TOOLS:$ANDROID_PLATFORM_TOOLS:$ANDROID_TOOLS:$NDK_ROOT:$PATH"
    alias appt="$ANDROID_HOME/build-tools/28.0.3//aapt"
    ```
2. 安装smali
    ```bash
    brew install smali
    ```
3. 修改config.txt配置文件  
4. 运行Repack 