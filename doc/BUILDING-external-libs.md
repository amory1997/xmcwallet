# 1 External lib build

## Requirements:

1. Docker

2. `make` (sudo apt install make, little tricky to get it on Windows, https://stackoverflow.com/questions/32127524/how-to-install-and-use-make-in-windows)

3. Huge amount of RAM and free disk space

## Building in Linux:

- Clone https://github.com/monero-classic-lab/Monero-Classic-V4-Wallet-Android repo.
```bash
git clone https://github.com/monero-classic-lab/Monero-Classic-V4-Wallet-Android.git
cd Monero-Classic-V4-Wallet-Android/external-libs
make
```
It will fail at end on Windows, but if `wallet2_api.h` exists in `include` folder, the build was successful.

<details>
<summary><h2>Proxy Configuration (Optional)  - Click to expand </h2></summary>

If you are building behind a corporate firewall or need to use a proxy server, the build process supports proxy configuration.

### Option 1: Using the Enhanced Makefile (Recommended)

**Important**: The proxy support is not automatic. You need to manually enable it by replacing the default Makefile.

1. Use the proxy-enabled Makefile:
   ```bash
   cd external-libs
   
   # Backup the original Makefile
   cp Makefile Makefile.original
   
   # Replace with the proxy-enabled version
   cp Makefile.proxy Makefile
   ```

2. Build with proxy:
   ```bash
   # Set proxy environment variables and build
   HTTP_PROXY=http://your-proxy-server:port HTTPS_PROXY=http://your-proxy-server:port make
   
   # Example with actual proxy address
   HTTP_PROXY=http://192.168.1.100:8080 HTTPS_PROXY=http://192.168.1.100:8080 make
   ```

3. Build without proxy (same as before):
   ```bash
   make
   ```

**Alternative**: You can also use the proxy Makefile directly without replacing:
```bash
# Use proxy Makefile directly
HTTP_PROXY=http://proxy:port make -f Makefile.proxy
```

### Option 2: Using Environment Variables

**Prerequisite**: First enable proxy support by replacing the Makefile (as shown in Option 1).

You can also export the proxy settings to your shell environment:

```bash
# First, replace the Makefile with proxy-enabled version
cp Makefile.proxy Makefile

# Export proxy settings
export HTTP_PROXY=http://your-proxy-server:port
export HTTPS_PROXY=http://your-proxy-server:port

# Then build normally
make
```

### Option 3: Building Specific Architectures

**Prerequisite**: Ensure you're using the proxy-enabled Makefile (replace as shown in Option 1).

If you only need specific architectures, you can build them individually:

```bash
# Build only ARM64 with proxy
HTTP_PROXY=http://proxy:port make arm64-v8a

# Build only ARM32 with proxy  
HTTP_PROXY=http://proxy:port make armeabi-v7a

# Build only x86_64 with proxy
HTTP_PROXY=http://proxy:port make x86_64

# Build only x86 with proxy
HTTP_PROXY=http://proxy:port make x86
```

**Alternative**: Use the proxy Makefile directly:
```bash
HTTP_PROXY=http://proxy:port make -f Makefile.proxy arm64-v8a
```

### Troubleshooting Proxy Issues

- **Test proxy connection**: `curl -x http://your-proxy:port https://www.google.com`
- **Check proxy settings**: `echo $HTTP_PROXY $HTTPS_PROXY`
- **View build commands**: `make -n` (shows commands without executing)
- **Clean and rebuild**: `make clean && HTTP_PROXY=http://proxy:port make`

**Note**: The proxy configuration is completely optional. If you don't need a proxy, simply use `make` as described in the original instructions.

</details> 

# 2 APK Build
Change directory to the Android wallet project and assemble the wallet apk file:
```bash
cd ..
./gradlew assembleDebug
```
The apk file will be located in `Monero-Classic-V4-Wallet-Android/app/build/outputs/apk/`.
