package top.starrysea.kql.facede;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import static top.starrysea.kql.common.Common.jsonToList;

@Component
public class KumaRedisDaoImpl implements KumaRedisDao {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private JedisPool jedisPool;

	@Override
	public <T> List<T> getList(String key, Class<T> clazz) {
		try (Jedis jedis = jedisPool.getResource();) {
			if (jedis.exists(key)) {
				return jsonToList(jedis.get(key), clazz);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return Collections.emptyList();
	}

	@Override
	public String set(String key, String value) {
		try (Jedis jedis = jedisPool.getResource();) {
			return jedis.set(key, value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}

	@Override
	public Long delete(String key) {
		try (Jedis jedis = jedisPool.getResource();) {
			return jedis.del(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return 0L;
	}

	@Override
	public void mapSet(String hashKey, String key, String value) {
		try (Jedis jedis = jedisPool.getResource();) {
			jedis.hset(hashKey, key, value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public Map<String, String> mapGetAll(String hashKey) {
		try (Jedis jedis = jedisPool.getResource();) {
			return jedis.hgetAll(hashKey);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return Collections.emptyMap();
	}

	@Override
	public void mapDel(String hashKey, String... key) {
		try (Jedis jedis = jedisPool.getResource();) {
			jedis.hdel(hashKey, key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
