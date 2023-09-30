package bg.sofia.uni.fmi.mjt.cache;

import bg.sofia.uni.fmi.mjt.cache.exception.ItemNotFound;
import bg.sofia.uni.fmi.mjt.cache.storage.Storage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeastFrequentlyUsedCacheTest {

    @Mock
    Storage<Integer, Integer> storage;

    @Test
    void sizeTest_whenCacheIsCreated_thenSizeIs0() {
        var lfu = new LeastFrequentlyUsedCache<Integer, Integer>(storage, 10);
        assertEquals(0, lfu.size());
    }

    @Test
    void sizeTest_whenItemIsMissing_thenThrowItemNotFound() {
        var lfu = new LeastFrequentlyUsedCache<Integer, Integer>(storage, 10);

        when(storage.retrieve(1)).thenReturn(null);

        assertThrows(ItemNotFound.class, () -> lfu.get(1));
    }

    @Test
    void sizeTest_whenCacheIsUnderLimit_thenSizeIsCorrect() throws ItemNotFound {
        int capacity = 4;
        var lfu = new LeastFrequentlyUsedCache<Integer, Integer>(storage, capacity);

        when(storage.retrieve(anyInt())).thenReturn(3);

        for (int i = 1; i <= capacity; i++) {
            lfu.get(i);
            assertEquals(i, lfu.size());
        }
    }

    @Test
    void sizeTest_whenCacheIsOverLimit_thenSizeIsCorrect() throws ItemNotFound {
        int capacity = 4;
        var lfu = new LeastFrequentlyUsedCache<Integer, Integer>(storage, capacity);

        when(storage.retrieve(anyInt())).thenReturn(3);

        for (int i = 1; i <= capacity + 1; i++) {
            lfu.get(i);
        }

        assertEquals(capacity, lfu.size());
    }

    @Test
    void valuesTest_returnsUnmodifiableCollection() throws ItemNotFound {
        int capacity = 4;
        var lfu = new LeastFrequentlyUsedCache<Integer, Integer>(storage, capacity);

        when(storage.retrieve(anyInt())).thenReturn(3);

        lfu.get(3);

        assertThrows(UnsupportedOperationException.class, () -> lfu.values().add(null));
    }

    @Test
    void valuesTest_returnCorrectValues() throws ItemNotFound {
        final int capacity = 3;
        var lfu = new LeastFrequentlyUsedCache<Integer, Integer>(storage, capacity);

        when(storage.retrieve(anyInt()))
            .thenReturn(1)
            .thenReturn(2)
            .thenReturn(3);

        for (int i = 1; i <= 9; i++) {
            lfu.get(i);
        }

        assertTrue(Arrays.asList(1, 2, 3).containsAll(lfu.values()) &&
            lfu.values().size() == 3);
    }

    @Test
    void clearTest_clears() throws ItemNotFound {
        final int capacity = 3;
        var lfu = new LeastFrequentlyUsedCache<Integer, Integer>(storage, capacity);

        when(storage.retrieve(anyInt()))
            .thenReturn(1)
            .thenReturn(2)
            .thenReturn(3);

        for (int i = 1; i <= 9; i++) {
            lfu.get(i);
        }

        lfu.clear();

        assertEquals(0, lfu.values().size());
    }

    @Test
    void getHitRateTest_isCalculatedProperly() throws ItemNotFound {
        final int capacity = 3;
        final int expectedNumberOfHits = 10;

        var lfu = new LeastFrequentlyUsedCache<Integer, Integer>(storage, capacity);

        when(storage.retrieve(anyInt()))
            .thenReturn(1);

        // miss
        lfu.get(1);

        // hits
        for (int i = 1; i <= expectedNumberOfHits; i++) {
            lfu.get(1);
        }

        assertEquals(expectedNumberOfHits / (expectedNumberOfHits + 1.0), lfu.getHitRate(), 0.001);
    }

    @Test
    void getTest_returnsCorrectValue() throws ItemNotFound {
        final int capacity = 3;

        var lfu = new LeastFrequentlyUsedCache<Integer, Integer>(storage, capacity);

        when(storage.retrieve(anyInt()))
            .thenReturn(1);

        assertEquals(1, lfu.get(3));
        assertEquals(1, lfu.get(3));
    }

    @Test
    void correctElementIsEvicted() throws ItemNotFound {
        final int capacity = 5;
        var lfu = new LeastFrequentlyUsedCache<Integer, Integer>(storage, capacity);

        for (int i = 1; i <= 6; i++) {
            when(storage.retrieve(i)).thenReturn(i);
        }

        // 1 is now least frequently used
        lfu.get(2);
        lfu.get(1);
        lfu.get(2);
        lfu.get(3);
        lfu.get(3);
        lfu.get(4);
        lfu.get(4);
        lfu.get(5);
        lfu.get(5);

        // evict 1
        lfu.get(6);

        assertTrue(Arrays.asList(2, 3, 4, 5, 6).containsAll(lfu.values()) &&
            lfu.values().size() == capacity);
    }

}