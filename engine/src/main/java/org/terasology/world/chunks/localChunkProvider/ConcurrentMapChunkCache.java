/*
 * Copyright 2018 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.world.chunks.localChunkProvider;

import com.google.common.collect.Maps;
import org.joml.Vector3i;
import org.terasology.world.chunks.Chunk;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

class ConcurrentMapChunkCache implements ChunkCache {

    private Map<Vector3i, Chunk> cache = Maps.newConcurrentMap();

    @Override
    public Chunk get(final Vector3i chunkPosition) {
        return cache.get(chunkPosition);
    }

    @Override
    public void put(final Vector3i chunkPosition, final Chunk chunk) {
        cache.put(chunkPosition, chunk);
    }

    @Override
    public Iterator<Vector3i> iterateChunkPositions() {
        return cache.keySet().iterator();
    }

    @Override
    public Collection<Chunk> getAllChunks() {
        return cache.values();
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public boolean containsChunkAt(final Vector3i chunkPosition) {
        return cache.containsKey(chunkPosition);
    }

    @Override
    public void removeChunkAt(final Vector3i chunkPosition) {
        cache.remove(chunkPosition);
    }
}
