package bg.sofia.uni.fmi.mjt.cache;

import bg.sofia.uni.fmi.mjt.cache.exception.ItemNotFound;
import bg.sofia.uni.fmi.mjt.cache.storage.Storage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeastRecentlyUsedCacheTest {

    @Mock
    Storage<Integer, Integer> storage;

    @Test
    void sizeTest_whenCacheIsCreated_thenSizeIs0() {
        var lru = new LeastRecentlyUsedCache<>(storage, 10);
        assertEquals(0, lru.size());
    }

    @Test
    void sizeTest_whenItemIsMissing_thenThrowItemNotFound() {
        var lru = new LeastRecentlyUsedCache<>(storage, 10);

        when(storage.retrieve(1)).thenReturn(null);

        assertThrows(ItemNotFound.class, () -> lru.get(1));
    }

    @Test
    void sizeTest_whenCacheIsUnderLimit_thenSizeIsCorrect() throws ItemNotFound {
        int capacity = 4;
        var lru = new LeastRecentlyUsedCache<>(storage, capacity);

        when(storage.retrieve(anyInt())).thenReturn(3);

        for (int i = 1; i <= capacity; i++) {
            lru.get(i);
            assertEquals(i, lru.size());
        }
    }

    @Test
    void sizeTest_whenCacheIsOverLimit_thenSizeIsCorrect() throws ItemNotFound {
        int capacity = 4;
        var lru = new LeastRecentlyUsedCache<>(storage, capacity);

        when(storage.retrieve(anyInt())).thenReturn(3);

        for (int i = 1; i <= capacity + 1; i++) {
            lru.get(i);
        }

        assertEquals(capacity, lru.size());
    }

    @Test
    void valuesTest_returnsUnmodifiableCollection() throws ItemNotFound {
        int capacity = 4;
        var lru = new LeastRecentlyUsedCache<>(storage, capacity);

        when(storage.retrieve(anyInt())).thenReturn(3);

        lru.get(3);

        assertThrows(UnsupportedOperationException.class, () -> lru.values().add(null));
    }

    @Test
    void valuesTest_returnCorrectValues() throws ItemNotFound {
        final int capacity = 3;
        var lru = new LeastRecentlyUsedCache<>(storage, capacity);

        when(storage.retrieve(anyInt()))
            .thenReturn(1)
            .thenReturn(2)
            .thenReturn(3);

        for (int i = 1; i <= 9; i++) {
            lru.get(i);
        }

        assertTrue(Arrays.asList(1, 2, 3).containsAll(lru.values()) &&
            lru.values().size() == 3);
    }

    @Test
    void clearTest_clears() throws ItemNotFound {
        final int capacity = 3;
        var lru = new LeastRecentlyUsedCache<>(storage, capacity);

        when(storage.retrieve(anyInt()))
            .thenReturn(1)
            .thenReturn(2)
            .thenReturn(3);

        for (int i = 1; i <= 9; i++) {
            lru.get(i);
        }

        lru.clear();

        assertEquals(0, lru.values().size());
    }

    @Test
    void getHitRateTest_isCalculatedProperly() throws ItemNotFound {
        final int capacity = 3;
        final int expectedNumberOfHits = 10;

        var lru = new LeastRecentlyUsedCache<>(storage, capacity);

        when(storage.retrieve(anyInt()))
            .thenReturn(1);

        // miss
        lru.get(1);

        // hits
        for (int i = 1; i <= expectedNumberOfHits; i++) {
            lru.get(1);
        }

        assertEquals(expectedNumberOfHits / (expectedNumberOfHits + 1.0), lru.getHitRate(), 0.001);
    }

    @Test
    void getTest_returnsCorrectValue() throws ItemNotFound {
        final int capacity = 3;

        var lru = new LeastRecentlyUsedCache<>(storage, capacity);

        when(storage.retrieve(anyInt()))
            .thenReturn(1);

        assertEquals(1, lru.get(3));
        assertEquals(1, lru.get(3));
    }

    @Test
    void correctElementIsEvicted() throws ItemNotFound {
        final int capacity = 5;
        var lru = new LeastRecentlyUsedCache<>(storage, capacity);

        for (int i = 1; i <= 6; i++) {
            when(storage.retrieve(i)).thenReturn(i);
        }

        // 2 is now least frequently used
        lru.get(1);
        lru.get(2);
        lru.get(3);
        lru.get(4);
        lru.get(5);
        lru.get(1);

        // evict 2
        lru.get(6);

        assertTrue(Arrays.asList(1, 3, 4, 5, 6).containsAll(lru.values()));
    }
}