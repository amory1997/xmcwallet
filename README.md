# XMCWallet
Another Android Monero-Classic Wallet for Monero-Classic
	

### QUICKSTART
- Download the APK for the most current release [here](https://github.com/monero-classic-lab/Monero-Classic-V4-Wallet/releases) and install it
- Run the App and select "Generate Wallet" to create a new wallet or recover a wallet
- See the [FAQ](doc/FAQ.md)

### Disclaimer
You may lose all your XMC if you use this App. Be cautious when spending on the mainnet.

### Random Notes
- works on the mainnet & stagenet
- use your own daemon - it's easy
- XMCWallet  is modified from Monerujo; Monerujo means "Monero Wallet" according to https://www.reddit.com/r/Monero/comments/3exy7t/esperanto_corner/
- Thanks to Monerujo builders remarkable works


### Issues / Pitfalls
- Users of Zenfone MAX & Zenfone 2 Laser (possibly others) **MUST** use the armeabi-v7a APK as the arm64-v8a build uses hardware AES
functionality these models don't have.
- You should backup your wallet files in the "monerujo" folder periodically.
- Also note, that on some devices the backups will only be visible on a PC over USB after a reboot of the device (it's an Android bug/feature)
- Created wallets on a private testnet are unusable because the restore height is set to that
of the "real" testnet.  After creating a new wallet, make a **new** one by recovering from the seed.
The official monero client shows the same behaviour.

### HOW TO BUILD

See [the instructions](doc/BUILDING-external-libs.md)

Then, fire up Android Studio and build the APK.

### Donations
- Address: 4AdkPJoxn7JCvAby9szgnt93MSEwdnxdhaASxbTBm6x5dCwmsDep2UYN4FhStDn5i11nsJbpU7oj59ahg8gXb1Mg3viqCuk
- Viewkey: b1aff2a12191723da0afbe75516f94dd8b068215f6e847d8da57aca5f1f98e0c
