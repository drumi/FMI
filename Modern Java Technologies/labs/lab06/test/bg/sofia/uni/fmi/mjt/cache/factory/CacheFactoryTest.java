package bg.sofia.uni.fmi.mjt.cache.factory;

import bg.sofia.uni.fmi.mjt.cache.LeastFrequentlyUsedCache;
import bg.sofia.uni.fmi.mjt.cache.LeastRecentlyUsedCache;
import bg.sofia.uni.fmi.mjt.cache.storage.Storage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CacheFactoryTest {

    @Mock
    Storage<Integer, Integer> storage;

    @Test
    void testGetInstance_whenNegativeCapacity_thenThrow() {
        assertThrows(IllegalArgumentException.class, () -> CacheFactory.getInstance(storage, -1, EvictionPolicy.LEAST_FREQUENTLY_USED));
    }

    @Test
    void testGetInstance_returnsCorrectClass() {
        assertEquals(LeastFrequentlyUsedCache.class, CacheFactory.getInstance(storage, 1000, EvictionPolicy.LEAST_FREQUENTLY_USED).getClass());
        assertEquals(LeastRecentlyUsedCache.class, CacheFactory.getInstance(storage, 1000, EvictionPolicy.LEAST_RECENTLY_USED).getClass());

        assertEquals(LeastFrequentlyUsedCache.class, CacheFactory.getInstance(storage, EvictionPolicy.LEAST_FREQUENTLY_USED).getClass());
        assertEquals(LeastRecentlyUsedCache.class, CacheFactory.getInstance(storage, EvictionPolicy.LEAST_RECENTLY_USED).getClass());
    }

    @Test
    void testGetInstance() {
    }
}