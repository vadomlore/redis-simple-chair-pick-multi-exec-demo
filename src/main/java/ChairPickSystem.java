import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class ChairPickSystem {
    JedisPool jedisPool;
    public void init(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(200);
        config.setMaxTotal(300);
        config.setTestOnBorrow(false);
        config.setTestOnReturn(false);

        jedisPool = new JedisPool(config, "localhost", 6379, 3000);
    }

    public void initChairs(int num){
        Jedis jedis = jedisPool.getResource();
        try{
            for(int i = 0; i < num; i++){
                jedis.set("chair:" + i, "0");
            }
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public void closeJedisPool(Jedis jedis,int flag) {
        if (flag == 0) {
            jedis.close();
            if (jedis.isConnected()) {
                try {
                    System.out.println("退出" + jedis.toString() + ":" + jedis.quit());
                    jedis.disconnect();
                } catch (Exception e) {
                    System.out.println("退出失败");
                    e.printStackTrace();
                }
            }
            jedis.close();
        }
    }

    public void pickChair(String userName, int chairId){
        Jedis jedis = jedisPool.getResource();
        try{
            String key = "chair:" + chairId;
            jedis.watch(key);
            String value = jedis.get(key);
            if(!value.equals("0")){
                Logger.getGlobal().info("chair:" + chairId + " occupy by " + value);
                return;
            }
            Transaction multi = jedis.multi();
            if(value.equals("0")){
                multi.set(key, userName);
            }
            List<Object> execResult = multi.exec();
            if(execResult == null || execResult.isEmpty()){
                Logger.getGlobal().info("chair:" + chairId + " occupy by " + value);
            }else if(execResult.get(0).equals("OK")){
                Logger.getGlobal().info(userName + " pick chair:" + chairId + "success");
            }
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


}
