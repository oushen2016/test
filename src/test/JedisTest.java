package test;

//import net.spy.memcached.compat.CloseUtil;  
//import net.spy.memcached.compat.log.Logger;  
//import net.spy.memcached.compat.log.LoggerFactory;  
import redis.clients.jedis.Client;  
import redis.clients.jedis.Jedis;  
import redis.clients.jedis.JedisPool;  
import redis.clients.jedis.JedisPoolConfig;  
  
import java.io.*;  
import java.util.ArrayList;  
import java.util.List;  
import java.util.Random;  
public class JedisTest {
	//protected Logger logger = Logger.getLogger(this.getClass());   
	  
    /** 
     * Jedis Pool for Jedis Resource 
     * @return 
     */  
    public static JedisPool buildJedisPool(){  
        JedisPoolConfig config = new JedisPoolConfig();  
        config.setMaxActive(1);  
        config.setMinIdle(50);  
        config.setMaxIdle(3000);  
        config.setMaxWait(5000);  
        JedisPool jedisPool = new JedisPool(config,  
                "*****");  
        return jedisPool;  
    }  
  
    /** 
     * Test Data 
     * @return 
     */  
    public static List<User> buildTestData(){  
        User a = new User();  
        a.setName("a");  
        User b = new User();  
        b.setName("b");  
        List<User> list = new ArrayList<User>();  
        list.add(a);  
        list.add(b);  
        return list;  
    }  
  
    /** 
     * Test for 
     */  
    public static void testSetElements(){  
        List<User> testData = buildTestData();  
        Redis redis=new Redis();
        Jedis jedis = redis.getJedis(); 
        String key = "testSetElements" + new Random(1000).nextInt();  
        jedis.set(key.getBytes(), ObjectsTranscoder.serialize(testData));  
  
        //验证  
        byte[] in = jedis.get(key.getBytes());  
        List<User> list = ObjectsTranscoder.deserialize(in);  
        for(User user : list){  
            System.out.println("testSetElements user name is:" + user.getName());  
        }  
    }  
  
    public static void testSetEnsemble(){  
        List<User> testData = buildTestData();  
        Redis redis=new Redis();
        Jedis jedis = redis.getJedis(); 
        String key = "testSetEnsemble" + new Random(1000).nextInt();  
        jedis.set(key.getBytes(), ListTranscoder.serialize(testData));  
  
        //验证  
        byte[] in = jedis.get(key.getBytes());  
        List<User> list = (List<User>)ListTranscoder.deserialize(in);  
        for(User user : list){  
            System.out.println("testSetEnsemble user name is:" + user.getName());  
        }  
    }  
  
    public static void main(String[] args) {  
        //testSetElements();  
        testSetEnsemble();  
    }  
  
    public static void close(Closeable closeable) {  
        if (closeable != null) {  
            try {  
                closeable.close();  
            } catch (Exception e) {  
               // logger.info("Unable to close %s", closeable, e);  
            }  
        }  
    }  
  
    static class User implements Serializable{  
        String name;  
  
        public String getName() {  
            return name;  
        }  
  
        public void setName(String name) {  
            this.name = name;  
        }  
    }  
  
    static class ObjectsTranscoder{  
          
        public static byte[] serialize(List<User> value) {  
            if (value == null) {  
                throw new NullPointerException("Can't serialize null");  
            }  
            byte[] rv=null;  
            ByteArrayOutputStream bos = null;  
            ObjectOutputStream os = null;  
            try {  
                bos = new ByteArrayOutputStream();  
                os = new ObjectOutputStream(bos);  
                for(User user : value){  
                    os.writeObject(user);  
                }  
                os.writeObject(null);  
                os.close();  
                bos.close();  
                rv = bos.toByteArray();  
            } catch (IOException e) {  
                throw new IllegalArgumentException("Non-serializable object", e);  
            } finally {  
                close(os);  
                close(bos);  
            }  
            return rv;  
        }  
  
        public static List<User> deserialize(byte[] in) {  
            List<User> list = new ArrayList<User>();  
            ByteArrayInputStream bis = null;  
            ObjectInputStream is = null;  
            try {  
                if(in != null) {  
                    bis=new ByteArrayInputStream(in);  
                    is=new ObjectInputStream(bis);  
                    while (true) {  
                        User user = (User) is.readObject();  
                        if(user == null){  
                            break;  
                        }else{  
                            list.add(user);  
                        }   
                    }  
                    is.close();  
                    bis.close();  
                }  
            } catch (IOException e) {  
               // logger.warn("Caught IOException decoding %d bytes of data",  
                       // in == null ? 0 : in.length, e);  
            } catch (ClassNotFoundException e) {  
               // logger.warn("Caught CNFE decoding %d bytes of data",  
                       // in == null ? 0 : in.length, e);  
            } finally {  
//                CloseUtil.close(is);  
//                CloseUtil.close(bis);  
            }  
            return list;  
        }  
    }  
      
    static class ListTranscoder{  
        public static byte[] serialize(Object value) {  
            if (value == null) {  
                throw new NullPointerException("Can't serialize null");  
            }  
            byte[] rv=null;  
            ByteArrayOutputStream bos = null;  
            ObjectOutputStream os = null;  
            try {  
                bos = new ByteArrayOutputStream();  
                os = new ObjectOutputStream(bos);  
                os.writeObject(value);  
                os.close();  
                bos.close();  
                rv = bos.toByteArray();  
            } catch (IOException e) {  
                throw new IllegalArgumentException("Non-serializable object", e);  
            } finally {  
                close(os);  
                close(bos);  
            }  
            return rv;  
        }  
  
        public static Object deserialize(byte[] in) {  
            Object rv=null;  
            ByteArrayInputStream bis = null;  
            ObjectInputStream is = null;  
            try {  
                if(in != null) {  
                    bis=new ByteArrayInputStream(in);  
                    is=new ObjectInputStream(bis);  
                    rv=is.readObject();  
                    is.close();  
                    bis.close();  
                }  
            } catch (IOException e) {  
               // logger.warn("Caught IOException decoding %d bytes of data",  
                       // in == null ? 0 : in.length, e);  
            } catch (ClassNotFoundException e) {  
              //  logger.warn("Caught CNFE decoding %d bytes of data",  
                      //  in == null ? 0 : in.length, e);  
            } finally {  
               // CloseUtil.close(is);  
              //  CloseUtil.close(bis);  
            }  
            return rv;  
        }  
    }  
}
