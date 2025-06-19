# Dockerfile Proxy Configuration

## Overview

This document provides detailed instructions for configuring proxy settings when building external libraries for this project. **This is a supplementary guide to `doc/BUILDING-external-libs.md`** - please read the main build documentation first for basic setup requirements.

The four Dockerfile files in this project have been updated to remove hardcoded proxy settings and add support for external proxy input. This makes building external-libs more flexible in different network environments.

**Important Notes:**
- Proxy configuration is completely optional
- You must manually enable proxy support (it's not automatic)
- All instructions assume you've completed the basic setup from `doc/BUILDING-external-libs.md`

## Background

This project uses Docker containers to build external libraries for Android platforms, including:
- `android32.Dockerfile` - builds libraries for armeabi-v7a architecture
- `android32_x86.Dockerfile` - builds libraries for x86 architecture  
- `android64.Dockerfile` - builds libraries for arm64-v8a architecture
- `android64_x86.Dockerfile` - builds libraries for x86_64 architecture

These Dockerfiles need to download numerous dependencies from the internet during the build process, which may require proxy usage in certain network environments.

## Changes Made

1. **Removed hardcoded proxies**: Deleted all hardcoded proxy settings from Dockerfiles
2. **Added proxy parameters**: Added `ARG HTTP_PROXY` and `ARG HTTPS_PROXY` parameters at the beginning of each Dockerfile
3. **Dynamic proxy environment variables**: Set environment variables using `ENV http_proxy=${HTTP_PROXY}` and `ENV https_proxy=${HTTPS_PROXY}`
4. **Conditional proxy usage**: Used conditional proxy parameters in curl and aria2c commands, only applying proxies when they are set

## Usage Instructions

### Prerequisites

Before starting the build, ensure you have completed the preparation steps according to `doc/BUILDING-external-libs.md`:

1. Install Docker
2. Install `make` tool (`sudo apt install make`)
3. Clone the monero repository: `https://github.com/m2049r/monero`
4. Switch to the correct branch (example: `git checkout release-v0.17.1.9-monerujo`)
5. Update submodules: `git submodule update --init --force`
6. Create symbolic link:
   - Linux: `ln -s ~/monero ~/xmrwallet/external-libs/monero`
   - Windows: `mklink /D "C:\Users\<USERNAME>\xmrwallet\external-libs\monero" "C:\Users\<USERNAME>\monero"`
7. Ensure you have huge amount of RAM and free disk space

**Important**: Proxy support is NOT automatic. You must manually enable it by replacing the default Makefile.

### 1. Enable Proxy Support (Required First Step)

Before using any proxy features, you must enable proxy support:

```bash
cd external-libs

# Backup the original Makefile
cp Makefile Makefile.original

# Replace with the proxy-enabled version
cp Makefile.proxy Makefile
```

### 2. Build without Proxy (Default Method)

After enabling proxy support, you can still build without proxy (fully backward compatible):

```bash
# Run make directly in the external-libs directory
make
```

This will build libraries for all architectures in sequence: arm64-v8a, armeabi-v7a, x86_64, x86

### 3. Build with Proxy (All Architectures)

```bash
# Method 1: Set proxy environment variables and build
HTTP_PROXY=http://your-proxy-server:port HTTPS_PROXY=http://your-proxy-server:port make

# Example with actual proxy address
HTTP_PROXY=http://192.168.1.100:8080 HTTPS_PROXY=http://192.168.1.100:8080 make

# Method 2: Export proxy settings to environment
export HTTP_PROXY=http://your-proxy-server:port
export HTTPS_PROXY=http://your-proxy-server:port
make
```

### 4. Build Specific Architectures with Proxy

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

### 5. Alternative: Use Proxy Makefile Directly

If you don't want to replace the original Makefile, you can use the proxy-enabled Makefile directly:

```bash
# Build all architectures with proxy
HTTP_PROXY=http://proxy:port make -f Makefile.proxy

# Build specific architecture with proxy
HTTP_PROXY=http://proxy:port make -f Makefile.proxy arm64-v8a

# Build without proxy
make -f Makefile.proxy
```

### 6. Advanced: Manual Docker Build (For Development/Debugging)

For advanced users who want more control or need to debug build issues, you can manually build individual Docker containers:

```bash
# Build arm64-v8a architecture manually
docker build \
  --build-arg HTTP_PROXY=http://your-proxy-server:port \
  --build-arg HTTPS_PROXY=http://your-proxy-server:port \
  -f android64.Dockerfile \
  -t monero-classic-v4-android-arm64 \
  monero-classic-v4

# Build armeabi-v7a architecture manually  
docker build \
  --build-arg HTTP_PROXY=http://your-proxy-server:port \
  --build-arg HTTPS_PROXY=http://your-proxy-server:port \
  -f android32.Dockerfile \
  -t monero-classic-v4-android-arm32 \
  monero-classic-v4

# Build x86_64 architecture manually
docker build \
  --build-arg HTTP_PROXY=http://your-proxy-server:port \
  --build-arg HTTPS_PROXY=http://your-proxy-server:port \
  -f android64_x86.Dockerfile \
  -t monero-classic-v4-android-x86_64 \
  monero-classic-v4

# Build x86 architecture manually
docker build \
  --build-arg HTTP_PROXY=http://your-proxy-server:port \
  --build-arg HTTPS_PROXY=http://your-proxy-server:port \
  -f android32_x86.Dockerfile \
  -t monero-classic-v4-android-x86 \
  monero-classic-v4
```

**Note**: Manual Docker builds require additional steps to extract the built libraries from containers. The Makefile approach is recommended for normal usage.

## Build Process Description

The complete build process includes the following steps:

1. **arm64-v8a**: Build 64-bit ARM architecture libraries
2. **armeabi-v7a**: Build 32-bit ARM architecture libraries  
3. **x86_64**: Build 64-bit x86 architecture libraries
4. **x86**: Build 32-bit x86 architecture libraries
5. **include/wallet2_api.h**: Copy API header file
6. **VERSION**: Generate version information

Each architecture build will:
- Remove old output directories
- Build Docker image
- Create container
- Copy build results from container to corresponding architecture directory

## Technical Details

- `${HTTP_PROXY:+-x $HTTP_PROXY}`: This bash syntax means if the HTTP_PROXY variable exists and is non-empty, add the `-x $HTTP_PROXY` parameter to curl
- `${HTTP_PROXY:+--all-proxy="${HTTP_PROXY}"}`: For aria2c commands, if HTTP_PROXY exists, add the `--all-proxy` parameter
- If proxy parameters are not set, the build process will proceed normally without using any proxy
- Proxy settings will affect all network requests within the container, including apt-get update, curl downloads, git clone, etc.

## Frequently Asked Questions

### Q: Build fails with network connection issues
A: Check if you need to set up a proxy, or if the proxy server is working properly. Test your proxy connection with: `curl -x http://your-proxy:port https://www.google.com`

### Q: Some packages download very slowly during build
A: Try using a proxy, or use domestic mirror sources in the Dockerfile

### Q: How to verify if the build was successful?
A: Check if the `include/wallet2_api.h` file exists, and if each architecture directory contains the corresponding library files. On Windows, the build may fail at the end, but if `wallet2_api.h` exists in the `include` folder, the build was successful.

### Q: How do I know if I'm using the proxy-enabled Makefile?
A: Check if your current Makefile contains `PROXY_ARGS` variables. You can verify with: `grep -n "PROXY_ARGS" Makefile`

### Q: Can I switch back to the original Makefile?
A: Yes, if you backed up the original: `cp Makefile.original Makefile`

### Q: Clean build files
A: Use `make clean` to clean output files, use `make distclean` to clean all files including Docker images

## Troubleshooting Proxy Issues

- **Test proxy connection**: `curl -x http://your-proxy:port https://www.google.com`
- **Check proxy settings**: `echo $HTTP_PROXY $HTTPS_PROXY`
- **View build commands**: `make -n` (shows commands without executing)
- **Clean and rebuild**: `make clean && HTTP_PROXY=http://proxy:port make`
- **Verify Makefile**: `grep -n "PROXY_ARGS" Makefile` to confirm you're using the proxy-enabled version
- **Check Docker build args**: Look for `--build-arg HTTP_PROXY=` in the build output

## Important Notes

1. **Read the main documentation first**: This guide supplements `doc/BUILDING-external-libs.md`. Complete all basic setup steps before configuring proxy settings.

2. **Manual proxy activation required**: Proxy support is NOT automatic. You must replace the Makefile with `Makefile.proxy` to enable proxy functionality.

3. **Memory and disk space**: Building requires a huge amount of RAM and free disk space, ensure sufficient system resources.

4. **Proxy settings are optional**: You can build normally without proxy settings if your network environment allows direct internet access.

5. **Network access**: Proxy server must be accessible during build time.

6. **Docker permissions**: Ensure Docker has sufficient permissions to run and create containers.

7. **Symbolic links**: Must correctly create symbolic link to monero directory, otherwise build will fail.

8. **Windows considerations**: On Windows, the build may fail at the end, but if `wallet2_api.h` exists in the `include` folder, the build was successful.

9. **Backward compatibility**: The enhanced Makefile (`Makefile.proxy`) is fully compatible with the original - you can build without proxy even after enabling proxy support.

10. **Troubleshooting**: If you encounter issues, refer to the troubleshooting section above and the main build documentation.
