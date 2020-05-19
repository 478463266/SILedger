1. The nodes file contains the related configuration and storage files of the BCOS blockchain nodes (e.g., encrypted and decrypted token files, encryption and decryption related public keys and private keys, authentication files)

2. The authorization file includes the configuration and operation code during the authorization process:

   --- authorize\src\main\java\org\fisco\bcos\asset\client\AssetClient.java is control logic code.

   --- authorize\src\main\java\org\fisco\bcos\asset\contract\Asset.java is smart-contract code.

   --- authorize\dist\contract\Asset.sol and \authorize\dist\contract\Table.sol is the dataset operation file

   --- authorize\dist\asset_run.sh is the configuration script for startup.

   --- authorize\src\test\resources is the SDK certificate of the blockchain node.

   --- The other are system-related configuration files.

3. The call file includes the configuration and operation codes during the call process:

   --- authorize\src\main\java\org\fisco\bcos\asset\client\AssetClient.java and authorize\src\main\java\org\fisco\bcos\asset\client\AssetServer.java are control logic code.

   --- Other files are similar to those in the authorization file.

