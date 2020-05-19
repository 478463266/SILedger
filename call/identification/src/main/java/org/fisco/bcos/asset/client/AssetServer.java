// A节点本地文件(需先启动另一服务器）
public class ServerA{
       public static void main(String[] args)throws Exception{
              File file=new File("token.txt");
              FileInputStream fos=new FileInputStream(file);
              ServerSocket ss=new ServerSocket(port);                //port为A端端口号
              Socket client=ss.accept();
              OutputStream netOut=client.getOutputStream();
              OutputStream doc=new DataOutputStream(new BufferedOutputStream(netOut));
              byte[] buf=new byte[2048];
              int num=fos.read(buf);
              while(num!=(-1)){//是否读完文件
                     doc.write(buf,0,num);//把文件数据写出网络缓冲区
                     doc.flush();//刷新缓冲区把数据写往客户端
                     num=fos.read(buf);//继续从文件中读取数据

              }
              fos.close();
              doc.close();

       }