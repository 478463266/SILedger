package org.fisco.bcos.asset.client;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Properties;
import java.io.BufferedReader;
import java.io.*;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.fisco.bcos.asset.contract.Asset;
import org.fisco.bcos.asset.contract.Asset.RegisterEventEventResponse;
import org.fisco.bcos.asset.contract.Asset.TransferEventEventResponse;
import org.fisco.bcos.channel.client.Service;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.crypto.Keys;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.channel.ChannelEthereumService;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tuples.generated.Tuple2;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
public class AssetClient {

	static Logger logger = LoggerFactory.getLogger(AssetClient.class);

	private Web3j web3j;

	private Credentials credentials;

	public Web3j getWeb3j() {
		return web3j;
	}

	public void setWeb3j(Web3j web3j) {
		this.web3j = web3j;
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

	public void recordAssetAddr(String address) throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.setProperty("address", address);
		final Resource contractResource = new ClassPathResource("contract.properties");
		FileOutputStream fileOutputStream = new FileOutputStream(contractResource.getFile());
		prop.store(fileOutputStream, "contract address");
	}

	public String loadAssetAddr() throws Exception {
		// load Asset contact address from contract.properties
		Properties prop = new Properties();
		final Resource contractResource = new ClassPathResource("contract.properties");
		prop.load(contractResource.getInputStream());

		String contractAddress = prop.getProperty("address");
		if (contractAddress == null || contractAddress.trim().equals("")) {
			throw new Exception(" load Asset contract address failed, please deploy it first. ");
		}
		logger.info(" load Asset address from contract.properties, address is {}", contractAddress);
		return contractAddress;
	}

	public void initialize() throws Exception {

		// init the Service
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		Service service = context.getBean(Service.class);
		service.run();

		ChannelEthereumService channelEthereumService = new ChannelEthereumService();
		channelEthereumService.setChannelService(service);
		Web3j web3j = Web3j.build(channelEthereumService, 1);

		// init Credentials
		Credentials credentials = Credentials.create(Keys.createEcKeyPair());

		setCredentials(credentials);
		setWeb3j(web3j);

		logger.debug(" web3j is " + web3j + " ,credentials is " + credentials);
	}

	private static BigInteger gasPrice = new BigInteger("30000000");
	private static BigInteger gasLimit = new BigInteger("30000000");

	public void deployAssetAndRecordAddr() {

		try {
			Asset asset = Asset.deploy(web3j, credentials, new StaticGasProvider(gasPrice, gasLimit)).send();
			System.out.println(" deploy Asset success, contract address is " + asset.getContractAddress());

			recordAssetAddr(asset.getContractAddress());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println(" deploy Asset contract failed, error message is  " + e.getMessage());
		}
	}




   // 授权步骤1：A向B申请交易，并将交易申请记入区块中
public void applyTransaction(String node1,String node2) {
		try {
			String contractAddress = loadAssetAddr();                               
			Asset asset = Asset.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
			TransactionReceipt receipt = asset.applyTransaction(node1, node2).send(); //将交易申请记录记入区块链中（调用sol合约中函数）
			List<applyTransactionEventResponse> response = asset.getApplyTransactionEvents(receipt);   //链反馈信息（是否记录成功）
			if (!response.isEmpty()) {
				if (response.get(0).ret.compareTo(new BigInteger("0")) == 0) {  //成功
					System.out.printf("  %s,apply for a transaction to %s\n",
							node1, node2);
                                                  File file=new File("/fisco/nodes/127.0.0.1/node0/token.txt.cpabe");   //获取已被cpabe加密的token文件
                                                
				} else {
					System.out.printf("Failed, ret code is %s \n",
							response.get(0).ret.toString());
				}
			} else {
				System.out.println(" event log not found, maybe transaction not exec. ");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			logger.error("Exception, error message is {}", e.getMessage());
		}
	}



// 授权步骤2：B收到A的请求申请并向A发布交易，将交易内嵌入加密好的token文件记入区块链中
public void postTransaction(String node2,String node1，File file) {
		try {
			String contractAddress = loadAssetAddr();                    //实验三位于不同区块节点时该地址与步骤1中地址不同
			Asset asset = Asset.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
			TransactionReceipt receipt = asset.postTransaction(node2, node1,file).send();     //发布交易，将交易内嵌入加密好的token文件记入区块链中（调用sol合约中函数）
			List<postTransactionEventResponse> response = asset.getPostTransactionEvents(receipt); //链反馈信息（是否记录成功）
			if (!response.isEmpty()) {
				if (response.get(0).ret.compareTo(new BigInteger("0")) == 0) {    //成功
					System.out.printf("  %s,post for a transaction to %s\n",
							node2, node1);
                                              queryTranscation(node1);            //调用授权步骤3
				} else {
					System.out.printf("Failed, ret code is %s \n",
							response.get(0).ret.toString());
				}
			} else {
				System.out.println(" event log not found, maybe transaction not exec. ");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			logger.error(" Exception, error message is {}", e.getMessage());
		}
	}

//授权步骤3：A收到B的发布交易，获取加密的文件并解密


public void queryTransaction(String node1) {
		try {
			String contractAddress = loadAssetAddr();
			Asset asset = Asset.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
			Tuple2<BigInteger, BigInteger> result = asset.queryTransaction(node1).send();       //A查询链上加密token文件
                        List<queryTransactionEventResponse> response = asset.getQueryTransactionEvents(receipt); //链反馈信息（是否记录成功）
			if (result.getValue(0).compareTo(new BigInteger("0")) == 0) {
                                           saveFile(result.getValue(1));                //获取链上加密文件保存至A的本地
	               try {
                                      String cmd = "cpabe-dec /fisco/nodes/127.0.0.1/node1/pub_key /fisco/nodes/127.0.0.1/node1/node1_priv_key /fisco/nodes/127.0.0.1/node1/token.txt.cpabe";    //解密文件
                                       Process ps = Runtime.getRuntime().exec(cmd);
                                       System.out.println("A decrypt sucessfully");
                                         decryptTransaction(node1);
                          } catch (Exception e) {
                             e.printStackTrace();
                                     }
                                      }
			} else {
				System.out.printf(" %s asset account is not exist \n", assetAccount);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			logger.error(" queryAssetAmount exception, error message is {}", e.getMessage());

			System.out.printf(" query asset account failed, error message is %s\n", e.getMessage());
		}
	}



// 授权步骤4：A将token的解密结果记入区块中
public void decryptTransaction(String node1) {
		try {
			String contractAddress = loadAssetAddr();
			Asset asset = Asset.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
			TransactionReceipt receipt = asset.decryptTransaction(node1).send();     //A将token的解密结果记入区块中（调用sol合约中函数）
			List<decryptTransactionEventResponse> response = asset.getDecryptTransactionEvents(receipt); //链反馈信息（是否记录成功）
			if (!response.isEmpty()) {
				if (response.get(0).ret.compareTo(new BigInteger("0")) == 0) {    //成功
					System.out.printf("successfully!\n");  
				} else {
					System.out.printf("Failed, ret code is %s \n",
							response.get(0).ret.toString());
				}
			} else {
				System.out.println(" event log not found, maybe transaction not exec. ");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			logger.error(" Exception, error message is {}", e.getMessage());
		}
	}

//授权1-4
public void authorize(String node1,String node2) {
		try {
			String contractAddress = loadAssetAddr();                               
			Asset asset = Asset.load(contractAddress, web3j, credentials, new StaticGasProvider(gasPrice, gasLimit));
			TransactionReceipt receipt = asset.applyTransaction(node1, node2).send(); //将交易申请记录记入区块链中（调用sol合约中函数）
			List<applyTransactionEventResponse> response = asset.getApplyTransactionEvents(receipt);   //链反馈信息（是否记录成功）
			if (!response.isEmpty()) {
				if (response.get(0).ret.compareTo(new BigInteger("0")) == 0) {  //成功
					System.out.printf("  %s,apply for a transaction to %s\n",
							node1, node2);
                                                  File file=new File("/fisco/nodes/127.0.0.1/node0/token.txt.cpabe");   //获取已被cpabe加密的token文件
                                                 applyTransaction(node2,node1,file);   //调用授权步骤2
				} else {
					System.out.printf("Failed, ret code is %s \n",
							response.get(0).ret.toString());
				}
			} else {
				System.out.println(" event log not found, maybe transaction not exec. ");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			logger.error("Exception, error message is {}", e.getMessage());
		}
	}


	public static void Usage() {
		System.out.println(" Usage:");
		System.out.println("\t java -cp conf/:lib/*:apps/* org.fisco.bcos.asset.client.AssetClient deploy");
		System.out.println("\t java -cp conf/:lib/*:apps/* org.fisco.bcos.asset.client.AssetClient query account");
		System.out.println(
				"\t java -cp conf/:lib/*:apps/* org.fisco.bcos.asset.client.AssetClient register account value");
		System.out.println(
				"\t java -cp conf/:lib/*:apps/* org.fisco.bcos.asset.client.AssetClient transfer from_account to_account amount");
		System.exit(0);
	}

	public static void main(String[] args) throws Exception {

		if (args.length < 1) {
			Usage();
		}

		AssetClient client = new AssetClient();
		client.initialize();

		switch (args[0]) {
		case "applyTransaction":
                          if (args.length < 2) {
			Usage();
                             }
                         client.applyTransaction(args[1],args[2]);
			break;
		case "postTransaction":
			if (args.length < 3) {
				Usage();
			}
			client.postTransaction(args[1],args[2],args[3]);
			break;
		case "queryTransaction":
			if (args.length < 1) {
				Usage();
			}
			client.registerAssetAccount(args[1]);
			break;
		case "decryptTransaction":
			if (args.length < 2) {
				Usage();
			}
			client.decryptTransaction(args[1],args[2]);
			break;
                case "authorize":
			if (args.length < 2) {
				Usage();
			}
			client.authorize(args[1],args[2]);
			break;
		default: {
			Usage();
		}
		}

		System.exit(0);
	}
}