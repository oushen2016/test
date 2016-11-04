package test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.text.html.HTML;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.SortingParams;

public class Redis {
	private Jedis jedis;//����Ƭ��ͻ�������
    private JedisPool jedisPool;//����Ƭ���ӳ�
    private ShardedJedis shardedJedis;//��Ƭ��ͻ�������
    private ShardedJedisPool shardedJedisPool;//��Ƭ���ӳ�
    
   String  ip="192.168.3.113";
    public Jedis getJedis() {
		return jedis;
	}

	public void setJedis(Jedis jedis) {
		this.jedis = jedis;
	}

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	public ShardedJedis getShardedJedis() {
		return shardedJedis;
	}

	public void setShardedJedis(ShardedJedis shardedJedis) {
		this.shardedJedis = shardedJedis;
	}

	public ShardedJedisPool getShardedJedisPool() {
		return shardedJedisPool;
	}

	public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
		this.shardedJedisPool = shardedJedisPool;
	}

	public Redis() 
    { 
        initialPool(); 
        initialShardedPool(); 
        shardedJedis = shardedJedisPool.getResource(); 
        jedis = jedisPool.getResource(); 
        
        
    } 
 
    /**
     * ��ʼ������Ƭ��
     */
    private void initialPool() 
    { 
        // �ػ������� 
        JedisPoolConfig config = new JedisPoolConfig(); 
        config.setMaxActive(20); 
        config.setMaxIdle(5); 
        config.setMaxWait(1000l); 
        config.setTestOnBorrow(false); 
        
        jedisPool = new JedisPool(config,ip,6379);
    }
    
    /** 
     * ��ʼ����Ƭ�� 
     */ 
    private void initialShardedPool() 
    { 
        // �ػ������� 
        JedisPoolConfig config = new JedisPoolConfig(); 
        config.setMaxActive(20); 
        config.setMaxIdle(5); 
        config.setMaxWait(1000l); 
        config.setTestOnBorrow(false); 
        // slave���� 
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>(); 
        shards.add(new JedisShardInfo(ip, 6379, "master")); 

        // ����� 
        shardedJedisPool = new ShardedJedisPool(config, shards); 
    } 

    public void show() {     
//        KeyOperate(); 
//        StringOperate(); 
//        ListOperate(); 
//        SetOperate();
//        SortedSetOperate();
//        HashOperate(); 
        jedisPool.returnResource(jedis);
        shardedJedisPool.returnResource(shardedJedis);
    } 


     public void ss(){
    	Set<String> keys = jedis.keys("sensitive_word:*");
    	for (String string : keys) {
			System.out.println(string);
		}
//    	System.out.println(jedis.dbSize());
    	
	}

      public static void main(String[] args) {
//          new Redis().show();
    	  Redis redis = new Redis();
    	  System.out.println();
//    	  redis.ss();
//    	  redis.KeyOperate();
//    	  redis.StringOperate();
//    	  redis.HashOperate();
//    	  redis.ListOperate();
//    	  redis.delete();
//    	 redis.KeyOperate();
    	  redis.ping();
      }
      public  void ping(){
    	  System.out.println(jedis.ping());
      }
      private void delete(){
//    	  Long hset = shardedJedis.hset("f", "s", "f");
//    	  Map<String, String> hgetAll = shardedJedis.hgetAll("user_token:61");
//    	  Set<String> keySet = hgetAll.keySet();
//    	  for (String string : keySet) {
//			System.out.println(string+hgetAll.get(string));
//		}
//    	  System.out.println(hset);
//    	    Set<String> keys = jedis.keys("*");
//    	    List<String> sort = jedis.sort("user_token:*");
//    	    for (String string : keys) {
//				System.out.print(string);
//				System.out.println(":"+jedis.type(string));
//			}
//    	   System.out.println(keys);
    	  String type = jedis.type("fs");
    	  Long ttl = jedis.ttl("fs");
    	  System.out.println(ttl);
//    	 Set<String> zrevrangeByScore = jedis.zrevrangeByScore("wallet:1016", 10, 2);
//    	 for (String string : zrevrangeByScore) {
//				System.out.println(string);
//			}
      }
      //key ����
      private void KeyOperate() 
      { 
          System.out.println("======================key=========================="); 
          // ������� 
//          System.out.println("��տ����������ݣ�"+jedis.flushDB());
          // �ж�key����� 
          System.out.println("�ж�key999���Ƿ���ڣ�"+shardedJedis.exists("key999")); 
          System.out.println("����key001,value001��ֵ�ԣ�"+shardedJedis.set("key001", "value001")); 
          System.out.println("�ж�key001�Ƿ���ڣ�"+shardedJedis.exists("key001"));
          // ���ϵͳ�����е�key
          System.out.println("����key002,value002��ֵ�ԣ�"+shardedJedis.set("key002", "value002"));
          System.out.println("ϵͳ�����м����£�");
          Set<String> keys = jedis.keys("*"); 
          Iterator<String> it=keys.iterator() ;   
          while(it.hasNext()){   
              String key = it.next();   
              System.out.println(key);   
          }
          // ɾ��ĳ��key,��key�����ڣ�����Ը����
          System.out.println("ϵͳ��ɾ��key002: "+jedis.del("key002"));
          System.out.println("�ж�key002�Ƿ���ڣ�"+shardedJedis.exists("key002"));
          // ���� key001�Ĺ���ʱ��
          System.out.println("���� key001�Ĺ���ʱ��Ϊ5��:"+jedis.expire("key001", 5));
          try{ 
              Thread.sleep(2000); 
          } 
          catch (InterruptedException e){ 
          } 
          // �鿴ĳ��key��ʣ������ʱ��,��λ���롿.����������߲����ڵĶ�����-1
          System.out.println("�鿴key001��ʣ������ʱ�䣺"+jedis.ttl("key001"));
          // �Ƴ�ĳ��key������ʱ��
          System.out.println("�Ƴ�key001������ʱ�䣺"+jedis.persist("key001"));
          System.out.println("�鿴key001��ʣ������ʱ�䣺"+jedis.ttl("key001"));
          // �鿴key�������ֵ������
          System.out.println("�鿴key�������ֵ�����ͣ�"+jedis.type("key001"));
          /*
           * һЩ����������1���޸ļ�����jedis.rename("key6", "key0");
           *             2������ǰdb��key�ƶ���������db���У�jedis.move("foo", 1)
           */
      } 
      //string����
      private void StringOperate() 
      {  
          System.out.println("======================String_1=========================="); 
          // ������� 
//          System.out.println("��տ����������ݣ�"+jedis.flushDB());
          
          System.out.println("=============��=============");
          jedis.set("key001","value001");
          jedis.set("key002","value002");
          jedis.set("key003","value003");
          System.out.println("��������3����ֵ�����£�");
          System.out.println(jedis.get("key001"));
          System.out.println(jedis.get("key002"));
          System.out.println(jedis.get("key003"));
          
          System.out.println("=============ɾ=============");  
          System.out.println("ɾ��key003��ֵ�ԣ�"+jedis.del("key003"));  
          System.out.println("��ȡkey003����Ӧ��ֵ��"+jedis.get("key003"));
          System.out.println("=============��=============");
          //1��ֱ�Ӹ���ԭ��������
          System.out.println("ֱ�Ӹ���key001ԭ�������ݣ�"+jedis.set("key001","value001-update"));
          System.out.println("��ȡkey001��Ӧ����ֵ��"+jedis.get("key001"));
          //2��ֱ�Ӹ���ԭ��������  
          System.out.println("��key002ԭ��ֵ����׷�ӣ�"+jedis.append("key002","+appendString"));
          System.out.println("��ȡkey002��Ӧ����ֵ"+jedis.get("key002")); 
     
          System.out.println("=============����ɾ���飨�����=============");
          /** 
           * mset,mgetͬʱ�������޸ģ���ѯ�����ֵ�� 
           * �ȼ��ڣ�
           * jedis.set("name","ssss"); 
           * jedis.set("jarorwar","xxxx"); 
           */  
          System.out.println("һ��������key201,key202,key203,key204�����Ӧֵ��"+jedis.mset("key201","value201",
                          "key202","value202","key203","value203","key204","value204"));  
          System.out.println("һ���Ի�ȡkey201,key202,key203,key204���Զ�Ӧ��ֵ��"+
                          jedis.mget("key201","key202","key203","key204"));
          System.out.println("һ����ɾ��key201,key202��"+jedis.del(new String[]{"key201", "key202"}));
          System.out.println("һ���Ի�ȡkey201,key202,key203,key204���Զ�Ӧ��ֵ��"+
                  jedis.mget("key201","key202","key203","key204")); 
          System.out.println();
                  
              
          //jedis�߱��Ĺ���shardedJedis��Ҳ��ֱ��ʹ�ã��������һЩǰ��û�ù��ķ���
          System.out.println("======================String_2=========================="); 
          // ������� 
//          System.out.println("��տ����������ݣ�"+jedis.flushDB());       
         
          System.out.println("=============������ֵ��ʱ��ֹ����ԭ��ֵ=============");
          System.out.println("ԭ��key301������ʱ������key301��"+shardedJedis.setnx("key301", "value301"));
          System.out.println("ԭ��key302������ʱ������key302��"+shardedJedis.setnx("key302", "value302"));
          System.out.println("��key302����ʱ����������key302��"+shardedJedis.setnx("key302", "value302_new"));
          System.out.println("��ȡkey301��Ӧ��ֵ��"+shardedJedis.get("key301"));
          System.out.println("��ȡkey302��Ӧ��ֵ��"+shardedJedis.get("key302"));
          
          System.out.println("=============������Ч�ڼ�ֵ�Ա�ɾ��=============");
          // ����key����Ч�ڣ����洢���� 
          System.out.println("����key303����ָ������ʱ��Ϊ2��"+shardedJedis.setex("key303", 2, "key303-2second")); 
          System.out.println("��ȡkey303��Ӧ��ֵ��"+shardedJedis.get("key303")); 
          try{ 
              Thread.sleep(3000); 
          } 
          catch (InterruptedException e){ 
          } 
          System.out.println("3��֮�󣬻�ȡkey303��Ӧ��ֵ��"+shardedJedis.get("key303")); 
          
          System.out.println("=============��ȡԭֵ������Ϊ��ֵһ�����=============");
          System.out.println("key302ԭֵ��"+shardedJedis.getSet("key302", "value302-after-getset"));
          System.out.println("key302��ֵ��"+shardedJedis.get("key302"));
          
          System.out.println("=============��ȡ�Ӵ�=============");
          System.out.println("��ȡkey302��Ӧֵ�е��Ӵ���"+shardedJedis.getrange("key302", 5, 7));         
      } 
     
      //list
      private void ListOperate() 
      { 
          System.out.println("======================list=========================="); 
          // ������� 
//          System.out.println("��տ����������ݣ�"+jedis.flushDB()); 

          System.out.println("=============��=============");
          String []strings={"d"};
          shardedJedis.lpush("stringlists", "vector"); 
          shardedJedis.lpush("stringlists", "ArrayList"); 
          shardedJedis.lpush("stringlists", "vector");
          shardedJedis.lpush("stringlists", "vector");
          shardedJedis.lpush("stringlists", "LinkedList");
          shardedJedis.lpush("stringlists", "MapList");
          shardedJedis.lpush("stringlists", "SerialList");
          shardedJedis.lpush("stringlists", "HashList");
          shardedJedis.lpush("numberlists", "3");
          shardedJedis.lpush("numberlists", "1");
          shardedJedis.lpush("numberlists", "5");
          shardedJedis.lpush("numberlists", "2");
          System.out.println("����Ԫ��-stringlists��"+shardedJedis.lrange("stringlists", 0, -1));
          System.out.println("����Ԫ��-numberlists��"+shardedJedis.lrange("numberlists", 0, -1));
          
//          System.out.println("=============ɾ=============");
//          // ɾ���б�ָ����ֵ ���ڶ�������Ϊɾ���ĸ��������ظ�ʱ������add��ȥ��ֵ�ȱ�ɾ�������ڳ�ջ
//          System.out.println("�ɹ�ɾ��ָ��Ԫ�ظ���-stringlists��"+shardedJedis.lrem("stringlists", 2, "vector")); 
//          System.out.println("ɾ��ָ��Ԫ��֮��-stringlists��"+shardedJedis.lrange("stringlists", 0, -1));
//          // ɾ��������������� 
//          System.out.println("ɾ���±�0-3����֮���Ԫ�أ�"+shardedJedis.ltrim("stringlists", 0, 3));
//          System.out.println("ɾ��ָ������֮��Ԫ�غ�-stringlists��"+shardedJedis.lrange("stringlists", 0, -1));
//          // �б�Ԫ�س�ջ 
//          System.out.println("��ջԪ�أ�"+shardedJedis.lpop("stringlists")); 
//          System.out.println("Ԫ�س�ջ��-stringlists��"+shardedJedis.lrange("stringlists", 0, -1));
//          
//          System.out.println("=============��=============");
//          // �޸��б���ָ���±��ֵ 
//          shardedJedis.lset("stringlists", 0, "hello list!"); 
//          System.out.println("�±�Ϊ0��ֵ�޸ĺ�-stringlists��"+shardedJedis.lrange("stringlists", 0, -1));
//          System.out.println("=============��=============");
//          // ���鳤�� 
//          System.out.println("����-stringlists��"+shardedJedis.llen("stringlists"));
//          System.out.println("����-numberlists��"+shardedJedis.llen("numberlists"));
//          // ���� 
//          /*
//           * list�д��ַ���ʱ����ָ������Ϊalpha�������ʹ��SortingParams������ֱ��ʹ��sort("list")��
//           * �����"ERR One or more scores can't be converted into double"
//           */
//          SortingParams sortingParameters = new SortingParams();
//          sortingParameters.alpha();
//          sortingParameters.limit(0, 3);
//          System.out.println("���������Ľ��-stringlists��"+shardedJedis.sort("stringlists",sortingParameters)); 
//          System.out.println("���������Ľ��-numberlists��"+shardedJedis.sort("numberlists"));
//          // �Ӵ���  startΪԪ���±꣬endҲΪԪ���±ꣻ-1������һ��Ԫ�أ�-2�������ڶ���Ԫ��
//          System.out.println("�Ӵ�-�ڶ�����ʼ��������"+shardedJedis.lrange("stringlists", 1, -1));
//          // ��ȡ�б�ָ���±��ֵ 
//          System.out.println("��ȡ�±�Ϊ2��Ԫ�أ�"+shardedJedis.lindex("stringlists", 2)+"\n");
      } 
      //set
      private void SetOperate() 
      { 

          System.out.println("======================set=========================="); 
          // ������� 
//          System.out.println("��տ����������ݣ�"+jedis.flushDB());
         
          System.out.println("=============��=============");
          System.out.println("��sets�����м���Ԫ��element001��"+jedis.sadd("sets", "element001")); 
          System.out.println("��sets�����м���Ԫ��element002��"+jedis.sadd("sets", "element002")); 
          System.out.println("��sets�����м���Ԫ��element003��"+jedis.sadd("sets", "element003"));
          System.out.println("��sets�����м���Ԫ��element004��"+jedis.sadd("sets", "element004"));
          System.out.println("�鿴sets�����е�����Ԫ��:"+jedis.smembers("sets")); 
          System.out.println();
          
          System.out.println("=============ɾ=============");
          System.out.println("����sets��ɾ��Ԫ��element003��"+jedis.srem("sets", "element003"));
          System.out.println("�鿴sets�����е�����Ԫ��:"+jedis.smembers("sets"));
          /*System.out.println("sets����������λ�õ�Ԫ�س�ջ��"+jedis.spop("sets"));//ע����ջԪ��λ�þ�Ȼ������--��ʵ������
          System.out.println("�鿴sets�����е�����Ԫ��:"+jedis.smembers("sets"));*/
          System.out.println();
          
          System.out.println("=============��=============");
          System.out.println();
          
          System.out.println("=============��=============");
          System.out.println("�ж�element001�Ƿ��ڼ���sets�У�"+jedis.sismember("sets", "element001"));
          System.out.println("ѭ����ѯ��ȡsets�е�ÿ��Ԫ�أ�");
          Set<String> set = jedis.smembers("sets");   
          Iterator<String> it=set.iterator();   
          while(it.hasNext()){   
              Object obj=it.next();   
              System.out.println(obj);   
          }  
          System.out.println();
          
          System.out.println("=============��������=============");
          System.out.println("sets1�����Ԫ��element001��"+jedis.sadd("sets1", "element001")); 
          System.out.println("sets1�����Ԫ��element002��"+jedis.sadd("sets1", "element002")); 
          System.out.println("sets1�����Ԫ��element003��"+jedis.sadd("sets1", "element003")); 
          System.out.println("sets1�����Ԫ��element002��"+jedis.sadd("sets2", "element002")); 
          System.out.println("sets1�����Ԫ��element003��"+jedis.sadd("sets2", "element003")); 
          System.out.println("sets1�����Ԫ��element004��"+jedis.sadd("sets2", "element004"));
          System.out.println("�鿴sets1�����е�����Ԫ��:"+jedis.smembers("sets1"));
          System.out.println("�鿴sets2�����е�����Ԫ��:"+jedis.smembers("sets2"));
          System.out.println("sets1��sets2������"+jedis.sinter("sets1", "sets2"));
          System.out.println("sets1��sets2������"+jedis.sunion("sets1", "sets2"));
          System.out.println("sets1��sets2���"+jedis.sdiff("sets1", "sets2"));//���set1���У�set2��û�е�Ԫ��
          
      }
      //sortedSet
      private void SortedSetOperate() 
      { 
          System.out.println("======================zset=========================="); 
          // ������� 
          System.out.println(jedis.flushDB()); 
          
          System.out.println("=============��=============");
          System.out.println("zset�����Ԫ��element001��"+shardedJedis.zadd("zset", 7.0, "element001")); 
          System.out.println("zset�����Ԫ��element002��"+shardedJedis.zadd("zset", 8.0, "element002")); 
          System.out.println("zset�����Ԫ��element003��"+shardedJedis.zadd("zset", 2.0, "element003")); 
          System.out.println("zset�����Ԫ��element004��"+shardedJedis.zadd("zset", 3.0, "element004"));
          System.out.println("zset�����е�����Ԫ�أ�"+shardedJedis.zrange("zset", 0, -1));//����Ȩ��ֵ����
          System.out.println();
          
          System.out.println("=============ɾ=============");
          System.out.println("zset��ɾ��Ԫ��element002��"+shardedJedis.zrem("zset", "element002"));
          System.out.println("zset�����е�����Ԫ�أ�"+shardedJedis.zrange("zset", 0, -1));
          System.out.println();
          
          System.out.println("=============��=============");
          System.out.println();
          
          System.out.println("=============��=============");
          System.out.println("ͳ��zset�����е�Ԫ���и�����"+shardedJedis.zcard("zset"));
          System.out.println("ͳ��zset������Ȩ��ĳ����Χ�ڣ�1.0����5.0����Ԫ�صĸ�����"+shardedJedis.zcount("zset", 1.0, 5.0));
          System.out.println("�鿴zset������element004��Ȩ�أ�"+shardedJedis.zscore("zset", "element004"));
          System.out.println("�鿴�±�1��2��Χ�ڵ�Ԫ��ֵ��"+shardedJedis.zrange("zset", 1, 2));

      }
      //hash
      private void HashOperate() 
      { 
          System.out.println("======================hash==========================");
          //������� 
//          System.out.println(jedis.flushDB()); 
//          System.out.println(shardedJedis.hget("hashs", "key004"));
          System.out.println("=============��=============");
//          System.out.println("hashs�����key001��value001��ֵ�ԣ�"+shardedJedis.hset("hashs:1", "key001", "value001")); 
//          System.out.println("hashs�����key002��value002��ֵ�ԣ�"+shardedJedis.hset("has1", "key002", "value002")); 
//          System.out.println("hashs�����key003��value003��ֵ�ԣ�"+shardedJedis.hset("hashs", "key003", "value003"));
          
          Object object="d";
//          System.out.println("����key004��4�����ͼ�ֵ�ԣ�"+shardedJedis.hset("hashs", "key004", "1"));
//          Map<String, String>map=new HashMap<String, String>();
//          map.put("a-22","a");
//          map.put("a1","a1");
//          map.put("content", "fff");
//  		map.put("pic", "fff");
//  		map.put("shop_color","fff");
//  		map.put("shop_size", "fff");
//  		map.put("store_code", "fff");
//  		map.put("user_name", "fff");
//  		map.put("verify_info", comment.getVerify_info());
//  		map.put("add_date", new Date().toString());
//  		map.put("comment_type","fff");
//  		map.put("order_shop_id","fff");
//  		map.put("id", comment.getId().toString());
//  		map.put("is_del", "fff");
//  		map.put("verify_user", comment.getVerify_user().toString());
//  		map.put("verify_time", new Date().toString());
//  		map.put("verify_status", "fff");
//  		map.put("user_id", "fff");
//  		map.put("shop_id", "fff");
//          map.put("a2",new Date().toString());
//          shardedJedis.hmset("hashs", map);
          //hincrby ����ԭ���������ϼ�
//          System.out.println("����key004��4�����ͼ�ֵ�ԣ�"+shardedJedis.hincrBy("hashs", "key004", 46));
//          System.out.println("hashs�е�����ֵ��"+shardedJedis.hvals("hashs"));
          System.out.println();
         
//          System.out.println("=============ɾ=============");
//          System.out.println("hashs��ɾ��key002��ֵ�ԣ�"+shardedJedis.hdel("hashs", "key002"));
//          System.out.println("hashs�е�����ֵ��"+shardedJedis.hvals("hashs"));
//          System.out.println();
//          
//          System.out.println("=============��=============");
//          System.out.println("key004���ͼ�ֵ��ֵ����100��"+shardedJedis.hincrBy("hashs", "key004", 100l));
//          System.out.println("hashs�е�����ֵ��"+shardedJedis.hvals("hashs"));
//          System.out.println();
//          
//          System.out.println("=============��=============");
//          System.out.println("�ж�key003�Ƿ���ڣ�"+shardedJedis.hexists("shopType", "key003"));
//          System.out.println("��ȡkey004��Ӧ��ֵ��"+shardedJedis.hget("hashs", "key004"));
//          System.out.println("������ȡkey001��key003��Ӧ��ֵ��"+shardedJedis.hmget("hashs", "key001", "key003")); 
          System.out.println("��ȡhashs�����е�key��"+shardedJedis.hkeys("shop_type"));
//          System.out.println("��ȡhashs�����е�value��"+shardedJedis.hvals("hashsaa").toString().equals("[]"));
//          List<String> hvals = shardedJedis.hvals("hashs");
//          System.out.println( shardedJedis.exists("12-*"));
//          Map<String, String> hgetAll = shardedJedis.hgetAll("000001-1");
//          System.out.println(shardedJedis.hgetAll("000001-1"));
      }
      
     /* hset(key, field, value)��������Ϊkey��hash�����Ԫ��field
      hget(key, field)����������Ϊkey��hash��field��Ӧ��value
      hmget(key, (fields))����������Ϊkey��hash��field i��Ӧ��value
      hmset(key, (fields))��������Ϊkey��hash�����Ԫ��field 
      hincrby(key, field, integer)��������Ϊkey��hash��field��value����integer
      hexists(key, field)������Ϊkey��hash���Ƿ���ڼ�Ϊfield����
      hdel(key, field)��ɾ������Ϊkey��hash�м�Ϊfield����
      hlen(key)����������Ϊkey��hash��Ԫ�ظ���
      hkeys(key)����������Ϊkey��hash�����м�
      hvals(key)����������Ϊkey��hash�����м���Ӧ��value
      hgetall(key)����������Ϊkey��hash�����еļ���field�������Ӧ��value*/
}

