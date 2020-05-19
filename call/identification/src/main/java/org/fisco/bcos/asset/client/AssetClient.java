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



//本地访问
           public void access(){
            getAccessFromA();                     //此函数为网络传输，若跨节点访问则调用此函数，单节点无需调用此函数
          String[] token = new String[15];                  
                            try {
         BufferedReader reader1 = null;
	File file1 = new File("/fisco/nodes/127.0.0.1/node2/node.txt");                 //B打开本地文件并提取token

                          reader1 = new BufferedReader(new FileReader(file1));
                            String tempString1 = null;
                             int line1 = 0;
            while ((tempString1 = reader1.readLine()) != null) {
                // 显示行号
                    token[line1] = tempString1;
              
                line1++;
            }
          
             reader1.close();

        } catch (IOException e) {
            e.printStackTrace();
        }  
         
             
            String[] token2 = new String[15];
                            try {
         BufferedReader reader2 = null;
	File file2 = new File("/fisco/nodes/127.0.0.1/node2/RequestFromA.txt");                 //B打开A资源请求文件并提取token

                          reader2 = new BufferedReader(new FileReader(file2));
                            String tempString2 = null;
                             int line2 = 0;
            while ((tempString2 = reader2.readLine()) != null) {
                // 显示行号
                    token2[line2] = tempString2;
                line2++;
            }
             reader2.close();

        } catch (IOException e) {
            e.printStackTrace();
        } 


      String[] privateKey = new String[15];
                            try {
         BufferedReader reader3 = null;
	File file3 = new File("/fisco/nodes/127.0.0.1/node2/privateKey.txt");                 //B节点私钥

                          reader3 = new BufferedReader(new FileReader(file3));
                            String tempString3 = null;
                             int line3 = 0;
            while ((tempString3 = reader3.readLine()) != null) {
                // 显示行号
                    privateKey[line3] = tempString3;
                line3++;
            }
             reader3.close();

        } catch (IOException e) {
            e.printStackTrace();
        } 

                 String message=token2[0];                                                    
                 String messageEn = decrypt(message,privateKey[0]);                //此函数为私钥解密token，若跨节点访问则调用此函数，单节点无需调用此函数
            if(token1[0].equals(ertification){                                          //若跨节点访问,则Certification为messageEn，若单节点访问，则Certification为message
                  System.out.print("A has been authorized");
              }else{
                   System.out.print("A has not been authorized");
                     }

            }



//B节点通过网络获取A节点含token文件

public void getAccessFromA{
         File file=new File("/fisco/nodes/127.0.0.1/node2/RequestFromA.txt");    建立新文件，并将从A端文件信息存至此位置
              file.createNewFile();
              RandomAccessFile raf=new RandomAccessFile(file,"rw");
              Socket server=new Socket(ip,port);                    //ip为A端ip，port为A端端口号
              InputStream netIn=server.getInputStream();
              InputStream in=new DataInputStream(new BufferedInputStream(netIn));
              byte[] buf=new byte[2048];
              int num=in.read(buf);  
              while(num!=(-1)){//是否读完所有数据
                     raf.write(buf,0,num);//将数据写往文件
                     raf.skipBytes(num);//顺序写文件字节
                     num=in.read(buf);//继续从网络中读取文件
              }
              in.close();
              raf.close();
        }

//私钥解密
public static String decrypt(String str, String privateKey) throws Exception{
		byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
		byte[] decoded = Base64.decodeBase64(privateKey);  
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));  
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, priKey);
		String outStr = new String(cipher.doFinal(inputByte));
		return outStr;
	}


	public static void Usage() {
		System.out.println(" Usage:");
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
                          if (args.length <1) {
			Usage();
                             }
                         client.access();
			break;
		default: {
			Usage();
		}
		}

		System.exit(0);
	}
}