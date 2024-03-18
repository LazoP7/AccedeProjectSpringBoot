
package MyProjects.Accede.security.ott;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

@Component
public class OTTUtility {
    private static final Integer EXPIRES_IN_MIN = 5;
    private final LoadingCache<String, Integer> ottCache;

    public OTTUtility()
    {
        this.ottCache = CacheBuilder.newBuilder().expireAfterWrite(EXPIRES_IN_MIN, TimeUnit.MINUTES).build(new CacheLoader<>()
            //building ott cache and setting its expiration time
        {
            public Integer load(String s) {
                return 0;
            }
        });
    }

    public Integer generateOtt(String key) //generating ott
    {
        Random random = new Random();
        Integer ottCode = 1000000 + random.nextInt(9000000);
        this.ottCache.put(key, ottCode);
        return ottCode;
    }

    public Integer getOTTByKey(String key) //key signature for ott
    {
        return this.ottCache.getIfPresent(key);
    }
}
